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

    public String buildLopgieAutoReplyString(final String[] userRequestComponents)
    {

        // No parameters, just return replyString
        if (mParameters == null || mParameters.isEmpty())
        {
            return mReplyString;
        }

        int userRquestParameterSize = userRequestComponents.length - 1;

        // Check auto reply rule type
        if (mType.equals(WechatAutoReplyRuleType.ParallelParametersRule))
        {
            // Get a new copy of reply string template
            String replyStringTemplate = new String(mReplyString);
            String replyString = "";

            // parameter count must between 1 and 10
            if (userRquestParameterSize < 1 || userRquestParameterSize > 10)
            {
                return null;
            }

            for (int i = 1; i < userRequestComponents.length; i++)
            {
                replyString += replyStringTemplate.replaceAll("\\$" + mParameters.get(0) + "\\$",
                        userRequestComponents[i]) + "\n";
            }
            return replyString;
        }
        else if (mType.equals(WechatAutoReplyRuleType.MultipleParametersRule))
        {
            // parameter size doesn't match
            if (userRquestParameterSize != mParameters.size())
            {
                return null;
            }

            // Get a new copy of reply string template
            String replyString = new String(mReplyString);

            // first element should be keyword. Get parameter from the second
            // component
            int i = 1;
            // replace with the actual parameters
            for (final String parameter : mParameters)
            {
                replyString = replyString.replaceAll("\\$" + parameter + "\\$",
                        userRequestComponents[i]);
                i++;
            }
            return replyString;
        }
        else
        {
            return null;
        }
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
