// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.wechat.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class WechatAutoReplyRuleCompiler
{
    /*
     * Each auto reply rule must matches the following format:
     *
     * [ ] => command name. Maximum length is 20.
     * 
     * <parameter> => parameter. Maximum length is 20.
     * 
     * * => allow multiple parallel parameters. Maximum number is 10
     * 
     * Two formats:
     * 
     * 1. multiple parallel parameters. But only allow one type parameter.
     * 
     * Examples:
     * 
     * [订单查询] $id$*
     * 
     * [尺码] $brandName$*
     * 
     * means user can input multiple order ids at one time. like 订单查询 20 40 60
     * 
     * 2. multiple different parameters. Maximum allow 10 different parameters
     * 
     * Examples:
     * 
     * [注册] $realName$ $phoneNumber$ $weiboName$
     * 
     * Regex explanation:
     * 
     * (\\[\\p{L}{1,20}\\]) matches [订单哪查询] \\p{L}matches any language
     * characters. {1,20} means the command name should be between 1 and 20.
     * 
     * ((\\s\\$[a-zA-Z]{1,20}\\$){1,10}|(\\s\\$[a-zA-Z]{1,20}\\$\\*)) matches
     * either 1~10 different parameters or one parallel parameters. The
     * parameter name should also be between 0 and 20
     */
    private static final String sAutoReplyRuleRegularExpression = "(\\[\\p{L}{1,20}\\])((\\s\\$[a-zA-Z]{1,20}\\$){0,10}|(\\s\\$[a-zA-Z]{1,20}\\$\\*))";

    private static final String sKeywordPattern = "\\[(.*?)\\]";

    public static WechatAutoReplyRuleType getAutoReplyRuleType(final String rule)
    {
        if (rule == null)
        {
            return null;
        }
        else
        {
            if (rule.matches(WechatAutoReplyRuleType.MultipleParametersRule.mAutoReplyRuleRegex))
            {
                return WechatAutoReplyRuleType.MultipleParametersRule;
            }
            else if (rule
                    .matches(WechatAutoReplyRuleType.ParallelParametersRule.mAutoReplyRuleRegex))
            {
                return WechatAutoReplyRuleType.ParallelParametersRule;
            }
        }
        return null;
    }

    public static boolean isAutoReplyRuleLegal(final String rule, final String autoReplyString)
    {
        if (!StringUtils.isEmpty(rule) && rule.matches(sAutoReplyRuleRegularExpression))
        {
            // check whether there is duplicate parameters
            if (rule.matches(WechatAutoReplyRuleType.MultipleParametersRule.mAutoReplyRuleRegex))
            {
                final Set<String> parameterSet = new HashSet<String>();
                final Pattern keyworkPatterm = Pattern
                        .compile(WechatAutoReplyRuleType.MultipleParametersRule.mParameterRegex);
                final Matcher matcher = keyworkPatterm.matcher(rule);
                while (matcher.find())
                {
                    if (matcher.groupCount() >= 1)
                    {
                        final String parameter = matcher.group(1);
                        if (parameterSet.contains(parameter))
                        {
                            return false;
                        }
                        parameterSet.add(parameter);
                    }
                }
            }
        }
        else
        {
            return false;
        }

        // Check whether reply string contains all the parameters in rule
        final List<String> parameters = getParameters(rule);

        if (parameters == null || parameters.isEmpty())
        {
            return !autoReplyString.matches("\\p{L}*\\s*\\$[a-zA-Z]{1,20}\\$\\s*\\p{L}*");
        }
        else
        {
            for (final String parameter : parameters)
            {
                if (!autoReplyString.contains(String.format("$%s$", parameter)))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public static String getKeyword(final String rule)
    {
        final Pattern keyworkPatterm = Pattern.compile(sKeywordPattern);
        Matcher matcher = keyworkPatterm.matcher(rule);
        if (matcher.find())
        {
            if (matcher.groupCount() >= 1)
            {
                return matcher.group(1);
            }
        }
        return null;
    }

    public static List<String> getParameters(final String rule)
    {
        final List<String> parameters = new ArrayList<String>();

        if (rule.matches(WechatAutoReplyRuleType.ParallelParametersRule.mAutoReplyRuleRegex))
        {
            final Pattern keyworkPatterm = Pattern
                    .compile(WechatAutoReplyRuleType.ParallelParametersRule.mParameterRegex);
            final Matcher matcher = keyworkPatterm.matcher(rule);
            if (matcher.find())
            {
                if (matcher.groupCount() >= 1)
                {
                    parameters.add(matcher.group(1));
                }
            }
        }
        else if (rule.matches(WechatAutoReplyRuleType.MultipleParametersRule.mAutoReplyRuleRegex))
        {
            final Pattern keyworkPatterm = Pattern
                    .compile(WechatAutoReplyRuleType.MultipleParametersRule.mParameterRegex);
            final Matcher matcher = keyworkPatterm.matcher(rule);
            while (matcher.find())
            {
                if (matcher.groupCount() >= 1)
                {
                    parameters.add(matcher.group(1));
                }
            }
        }
        return parameters;
    }

    public boolean verifyReplyString(final String rule, final String replyString)
    {
        final List<String> parameters = getParameters(rule);

        if (parameters == null || parameters.isEmpty())
        {
            return true;
        }
        else
        {
            for (final String parameter : parameters)
            {
                if (!replyString.contains(String.format(" <%s>", parameter)))
                {
                    return false;
                }
            }
        }
        return true;
    }

}
