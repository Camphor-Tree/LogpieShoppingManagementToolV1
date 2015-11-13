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
import com.logpie.shopping.management.model.Setting;
import com.logpie.shopping.management.storage.SettingDAO;
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
    private SettingDAO mSettingDAO;

    private ConcurrentHashMap<String, WechatAutoReplyRule> mEngineCache;

    private LogpieWechatAutoReplyEngine()
    {
        LOG.info("Start wechat auto reply engine");
        mSettingDAO = new SettingDAO(Admin.buildSystemSuperAdmin());
        final List<Setting> settings = mSettingDAO
                .getAllSettingsByNameSpace(sAutoReplyRuleNameSpace);
        for (final Setting setting : settings)
        {
            final String rule = setting.getSettingKey();
            final String replyString = setting.getSettingValue();

            final WechatAutoReplyRule autoReplyRule = new WechatAutoReplyRule(rule, replyString);
            mEngineCache.put(autoReplyRule.getKeyword(), autoReplyRule);
        }
    }

    public boolean addAutoReplyRule(final String rule)
    {
        return true;
    }

    public synchronized static LogpieWechatAutoReplyEngine getInstance()
    {
        if (sInstance == null)
        {
            sInstance = new LogpieWechatAutoReplyEngine();
        }
        return sInstance;
    }

    public String processCommingMessage(HttpServletRequest request)
    {
        String respMessage = null;
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
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return respMessage;
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
