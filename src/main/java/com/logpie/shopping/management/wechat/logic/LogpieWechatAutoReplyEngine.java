// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.wechat.logic;

import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.TextAutoReplyRule;
import com.logpie.shopping.management.storage.TextAutoReplyRuleDAO;
import com.logpie.shopping.management.util.XMLParserUtils;
import com.logpie.shopping.management.wechat.model.WechatTextResponseMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class LogpieWechatAutoReplyEngine
{
    // Auto reply rule stores in setting table with name space
    public static final String sAutoReplyRuleNameSpace = "com.logpie.shopping.management.wechat.autoreply.rule";
    private static Logger LOG = Logger.getLogger(LogpieWechatAutoReplyEngine.class);

    private static final String MESSAGE_TYPE_TEXT = "text";
    private static LogpieWechatAutoReplyEngine sInstance;
    private TextAutoReplyRuleDAO mTextAutoReplyRuleDAO;

    private ConcurrentHashMap<String, WechatAutoReplyRule> mEngineCache;

    private LogpieWechatAutoReplyEngine()
    {
        LOG.info("Start wechat auto reply engine");
        mTextAutoReplyRuleDAO = new TextAutoReplyRuleDAO(Admin.buildSystemSuperAdmin());
        mEngineCache = new ConcurrentHashMap<String, WechatAutoReplyRule>();
        final List<TextAutoReplyRule> replyRules = mTextAutoReplyRuleDAO.getAllTextAutoReplyRules();
        for (final TextAutoReplyRule replyRule : replyRules)
        {
            final Boolean activated = replyRule.getTextAutoReplyRuleActivated();

            if (activated)
            {
                final String rule = replyRule.getTextAutoReplyRuleKeyword();
                final String replyString = replyRule.getTextAutoReplyRuleReplyString();

                final WechatAutoReplyRule autoReplyRule = new WechatAutoReplyRule(rule,
                        replyString);

                if (autoReplyRule.getKeyword() != null)
                {
                    LOG.info("rule:" + rule + " replyString:" + replyString);
                    LOG.info("rule parsed keyword:" + autoReplyRule.getKeyword() + " parameters:"
                            + autoReplyRule.getParameters().toString());
                    mEngineCache.put(autoReplyRule.getKeyword(), autoReplyRule);
                }
            }
        }
    }

    public synchronized static LogpieWechatAutoReplyEngine getInstance()
    {
        if (sInstance == null)
        {
            sInstance = new LogpieWechatAutoReplyEngine();
        }
        return sInstance;
    }

    // The rule passed in this method should already be verified.
    public boolean addAutoReplyRule(final Boolean activated, final String rule,
            final String replyString)
    {
        final TextAutoReplyRule textAutoReplyRule = new TextAutoReplyRule(activated, rule,
                replyString);
        if (mTextAutoReplyRuleDAO.addTextAutoReplyRule(textAutoReplyRule) && activated)
        {
            final WechatAutoReplyRule autoReplyRule = new WechatAutoReplyRule(rule, replyString);
            mEngineCache.put(autoReplyRule.getKeyword(), autoReplyRule);
            return true;
        }
        return false;
    }

    private boolean doesKeywordRuleExist(final String keyword)
    {
        return mEngineCache.containsKey(keyword);
    }

    public String processCommingMessage(HttpServletRequest request)
    {
        try
        {
            // xml请求解析
            final Map<String, String> requestMap = XMLParserUtils.parseXml(request);

            // 发送方帐号（open_id）
            final String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            final String toUserName = requestMap.get("ToUserName");
            // 消息类型
            final String msgType = requestMap.get("MsgType");

            final WechatTextResponseMessage textMessage = new WechatTextResponseMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);
            // 文本消息
            if (msgType.equals(MESSAGE_TYPE_TEXT))
            {
                // 接收用户发送的文本消息内容
                final String content = requestMap.get("Content");
                return processCommingMessage(content);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return getNoSupportString();
    }

    public String processCommingMessage(final String content)
    {
        final String[] contentComponents = content.split(" ");
        if (contentComponents == null || contentComponents.length == 0)
        {
            return getNoSupportString();
        }
        final String keyword = contentComponents[0];

        if (doesKeywordRuleExist(keyword))
        {
            final WechatAutoReplyRule autoReplyRule = mEngineCache.get(keyword);

            final String replyString = autoReplyRule.buildLopgieAutoReplyString(contentComponents);
            if (replyString != null)
            {
                return replyString;
            }
        }

        return getNoSupportString();
    }

    public String getNoSupportString()
    {
        return "抱歉 您输入的请求格式有误。 请回复\"帮助\" 查看功能文档说明";
    }

    /**
     * 文本消息对象转换成xml
     * 
     * @param textMessage
     *            文本消息对象
     * @return xml
     */
    public static String textMessageToXml(WechatTextResponseMessage textMessage)
    {
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }

    /**
     * 扩展xstream，使其支持CDATA块
     * 
     */
    private static XStream xstream = new XStream(new XppDriver()
    {
        @Override
        public HierarchicalStreamWriter createWriter(Writer out)
        {
            return new PrettyPrintWriter(out)
            {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                @Override
                protected void writeText(QuickWriter writer, String text)
                {
                    if (cdata)
                    {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    }
                    else
                    {
                        writer.write(text);
                    }
                }
            };
        }
    });

}
