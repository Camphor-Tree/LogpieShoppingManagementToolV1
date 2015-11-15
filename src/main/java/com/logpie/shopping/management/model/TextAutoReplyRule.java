// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class TextAutoReplyRule implements RowMapper<TextAutoReplyRule>, LogpieModel
{
    private static final Logger LOG = Logger.getLogger(TextAutoReplyRule.class);

    public static final String DB_KEY_TEXTAUTOREPLYRULE_ID = "TextAutoReplyRuleId";
    public static final String DB_KEY_TEXTAUTOREPLYRULE_ACTIVATED = "TextAutoReplyRuleActivated";
    public static final String DB_KEY_TEXTAUTOREPLYRULE_KEYWORD = "TextAutoReplyRuleKeyword";
    public static final String DB_KEY_TEXTAUTOREPLYRULE_REPLY_STRING = "TextAutoReplyRuleReplyString";

    private String mTextAutoReplyRuleId;
    private Boolean mTextAutoReplyRuleActivated;
    private String mTextAutoReplyRuleKeyword;
    private String mTextAutoReplyRuleReplyString;

    public TextAutoReplyRule(String textAutoReplyRuleId, Boolean textAutoReplyRuleActivated,
            String textAutoReplyRuleKeyword, String textAutoReplyRuleReplyString)
    {
        super();
        mTextAutoReplyRuleId = textAutoReplyRuleId;
        mTextAutoReplyRuleActivated = textAutoReplyRuleActivated;
        mTextAutoReplyRuleKeyword = textAutoReplyRuleKeyword;
        mTextAutoReplyRuleReplyString = textAutoReplyRuleReplyString;
    }

    public TextAutoReplyRule(Boolean textAutoReplyRuleActivated, String textAutoReplyRuleKeyword,
            String textAutoReplyRuleReplyString)
    {
        super();
        mTextAutoReplyRuleActivated = textAutoReplyRuleActivated;
        mTextAutoReplyRuleKeyword = textAutoReplyRuleKeyword;
        mTextAutoReplyRuleReplyString = textAutoReplyRuleReplyString;
    }

    // For row mapper
    public TextAutoReplyRule()
    {

    }

    @Override
    public Map<String, Object> getModelMap()
    {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put(TextAutoReplyRule.DB_KEY_TEXTAUTOREPLYRULE_KEYWORD,
                this.mTextAutoReplyRuleKeyword);
        modelMap.put(TextAutoReplyRule.DB_KEY_TEXTAUTOREPLYRULE_REPLY_STRING,
                this.mTextAutoReplyRuleReplyString);
        modelMap.put(TextAutoReplyRule.DB_KEY_TEXTAUTOREPLYRULE_ACTIVATED,
                this.mTextAutoReplyRuleActivated);
        return modelMap;
    }

    @Override
    public String getPrimaryKey()
    {
        return DB_KEY_TEXTAUTOREPLYRULE_ID;
    }

    @Override
    public boolean compareTo(Object object)
    {
        if (object instanceof TextAutoReplyRule)
        {
            final TextAutoReplyRule compareToTextAutoReplyRule = (TextAutoReplyRule) object;
            if (compareToTextAutoReplyRule.mTextAutoReplyRuleId.equals(mTextAutoReplyRuleId)
                    && compareToTextAutoReplyRule.mTextAutoReplyRuleKeyword
                            .equals(mTextAutoReplyRuleKeyword)
                    && compareToTextAutoReplyRule.mTextAutoReplyRuleReplyString
                            .equals(mTextAutoReplyRuleReplyString)
                    && compareToTextAutoReplyRule.mTextAutoReplyRuleActivated
                            .equals(mTextAutoReplyRuleActivated))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public TextAutoReplyRule mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        return getTextAutoReplyRuleByResultSet(rs, rowNum);
    }

    public static TextAutoReplyRule getTextAutoReplyRuleByResultSet(final ResultSet rs,
            final int rowNum) throws SQLException
    {
        if (rs == null)
        {
            return null;
        }
        final String textAutoReplyRuleId = rs.getString(DB_KEY_TEXTAUTOREPLYRULE_ID);
        final String textAutoReplyRuleKeyword = rs.getString(DB_KEY_TEXTAUTOREPLYRULE_KEYWORD);
        final String textAutoReplyRuleReplyString = rs
                .getString(DB_KEY_TEXTAUTOREPLYRULE_REPLY_STRING);
        final boolean textAutoReplyRuleActivated = rs
                .getBoolean(DB_KEY_TEXTAUTOREPLYRULE_ACTIVATED);

        return new TextAutoReplyRule(textAutoReplyRuleId, textAutoReplyRuleActivated,
                textAutoReplyRuleKeyword, textAutoReplyRuleReplyString);
    }

    public String getTextAutoReplyRuleId()
    {
        return mTextAutoReplyRuleId;
    }

    public void setTextAutoReplyRuleId(String textAutoReplyRuleId)
    {
        mTextAutoReplyRuleId = textAutoReplyRuleId;
    }

    public Boolean getTextAutoReplyRuleActivated()
    {
        return mTextAutoReplyRuleActivated;
    }

    public void setTextAutoReplyRuleActivated(Boolean textAutoReplyRuleActivated)
    {
        mTextAutoReplyRuleActivated = textAutoReplyRuleActivated;
    }

    public String getTextAutoReplyRuleKeyword()
    {
        return mTextAutoReplyRuleKeyword;
    }

    public void setTextAutoReplyRuleKeyword(String textAutoReplyRuleKeyword)
    {
        mTextAutoReplyRuleKeyword = textAutoReplyRuleKeyword;
    }

    public String getTextAutoReplyRuleReplyString()
    {
        return mTextAutoReplyRuleReplyString;
    }

    public void setTextAutoReplyRuleReplyString(String textAutoReplyRuleReplyString)
    {
        mTextAutoReplyRuleReplyString = textAutoReplyRuleReplyString;
    }
}
