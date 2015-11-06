// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.wechat.model;

/**
 * 文本消息
 */
public class WechatTextResponseMessage extends WechatBaseResponseMessage
{
    /**
     * 回复的消息内容
     */
    private String Content;

    public String getContent()
    {
        return Content;
    }

    public void setContent(String content)
    {
        Content = content;
    }
}