// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.wechat.logic;

import java.util.List;

/**
 * Logpie auto reply rule
 * 
 * @author yilei
 *
 */
public class WechatAutoReplyRule
{
    private WechatAutoReplyRuleType mType;
    private String mKeyword;
    private List<String> mParameters;
    private String mReplyString;

    public WechatAutoReplyRule(WechatAutoReplyRuleType type, String keyword,
            List<String> parameters, String replyString)
    {
        super();
        mType = type;
        mKeyword = keyword;
        mParameters = parameters;
        mReplyString = replyString;
    }

    public WechatAutoReplyRule(String rule, String replyString)
    {
        super();
        mType = WechatAutoReplyRuleCompiler.getAutoReplyRuleType(rule);
        mKeyword = WechatAutoReplyRuleCompiler.getKeyword(rule);
        mParameters = WechatAutoReplyRuleCompiler.getParameters(rule);
        mReplyString = replyString;
    }

    private String buildReplyString(final List<String> parameterList, final String replyString)
    {
        // No parameters, just return replyString
        if (parameterList == null || parameterList.isEmpty())
        {
            return replyString;
        }

        for (final String parameter : parameterList)
        {
            replyString.replaceAll("<" + parameter + ">", replyString);
        }

        return null;
    }

    public String getKeyword()
    {
        return mKeyword;
    }

    public void setKeyword(String keyword)
    {
        mKeyword = keyword;
    }

    public List<String> getParameters()
    {
        return mParameters;
    }

    public void setParameters(List<String> parameters)
    {
        mParameters = parameters;
    }

    public String getReplyString()
    {
        return mReplyString;
    }

    public void setReplyString(String replyString)
    {
        mReplyString = replyString;
    }

}
