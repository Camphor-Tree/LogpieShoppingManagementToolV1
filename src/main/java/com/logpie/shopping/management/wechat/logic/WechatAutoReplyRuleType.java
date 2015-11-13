// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.wechat.logic;

public enum WechatAutoReplyRuleType
{
    ParallelParametersRule("(\\[\\p{L}{1,20}\\])(\\s\\$[a-zA-Z]{1,20}\\$\\*)", "\\s\\$(.*?)\\$\\*"),

    MultipleParametersRule("(\\[\\p{L}{1,20}\\])(\\s\\$[a-zA-Z]{1,20}\\$){0,10}", "\\s\\$(.*?)\\$");

    /* package-private */String mAutoReplyRuleRegex;
    /* package-private */String mParameterRegex;

    private WechatAutoReplyRuleType(final String ruleRegex, final String parameterRegex)
    {
        mAutoReplyRuleRegex = ruleRegex;
        mParameterRegex = parameterRegex;
    }
}
