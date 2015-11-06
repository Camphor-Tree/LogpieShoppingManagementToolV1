package com.logpie.shopping.management.wechat.logic;

import java.io.Writer;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.util.XMLParserUtils;
import com.logpie.shopping.management.wechat.model.WechatTextResponseMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class WechatRequestMessageHandler
{
    public static Logger log = Logger.getLogger(WechatRequestMessageHandler.class);

    private static final String MESSAGE_TYPE_TEXT = "text";

    public String processCommingMessage(HttpServletRequest request)
    {
        String respMessage = null;
        try
        {
            // xml请求解析
            Map<String, String> requestMap = XMLParserUtils.parseXml(request);

            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");

            WechatTextResponseMessage textMessage = new WechatTextResponseMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);
            // 文本消息
            if (msgType.equals(MESSAGE_TYPE_TEXT))
            {
                // 接收用户发送的文本消息内容
                String content = requestMap.get("Content");

                if ("1".equals(content))
                {
                    textMessage.setContent("1是很好的");
                    // 将文本消息对象转换成xml字符串
                    respMessage = textMessageToXml(textMessage);
                }
                else if ("2".equals(content))
                {
                    textMessage.setContent("我不是2货");
                    // 将文本消息对象转换成xml字符串
                    respMessage = textMessageToXml(textMessage);
                }
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
