// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.wechat.logic;

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
     * [订单查询] <id>*
     * 
     * [尺码] <brandName>*
     * 
     * means user can input multiple order ids at one time. like 订单查询 20 40 60
     * 
     * 2. multiple different parameters. Maximum allow 10 different parameters
     * 
     * Examples:
     * 
     * [注册] <realName> <phoneNumber> <weiboName>
     * 
     * Regex explanation:
     * 
     * (\\[\\p{L}{1,20}\\]) matches [订单哪查询] \\p{L}matches any language
     * characters. {1,20} means the command name should be between 1 and 20.
     * 
     * ((\\s<[a-zA-Z]{1,20}>){1,10}|(\\s<[a-zA-Z]{1,20}>\\*)) matches either
     * 1~10 different parameters or one parallel parameters. The parameter name
     * should also be between 1 and 20
     */
    private static final String sAutoReplyRuleRegularExpression = "(\\[\\p{L}{1,20}\\])((\\s<[a-zA-Z]{1,20}>){1,10}|(\\s<[a-zA-Z]{1,20}>\\*))";

    public boolean isAutoReplyRuleLegal(final String rule)
    {
        if (!StringUtils.isEmpty(rule) && rule.matches(sAutoReplyRuleRegularExpression))
        {
            return true;
        }
        return false;
    }
}
