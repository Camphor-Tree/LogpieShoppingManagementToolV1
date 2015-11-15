// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.LogpieModel;
import com.logpie.shopping.management.model.TextAutoReplyRule;
import com.logpie.shopping.management.util.CollectionUtils;

public class TextAutoReplyRuleDAO extends LogpieBaseDAO<TextAutoReplyRule>
{
    public static final String sTextAutoReplyRuleTableName = "TextAutoReplyRule";
    private static final Logger LOG = Logger.getLogger(TextAutoReplyRuleDAO.class);

    public TextAutoReplyRuleDAO(final Admin admin)
    {
        super(admin);
    }

    public boolean addTextAutoReplyRule(final TextAutoReplyRule textAutoReplyRule)
    {
        final LogpieDataInsert<TextAutoReplyRule> adddTextAutoReplyRuleInsert = new AddTextAutoReplyRuleInsert(
                textAutoReplyRule);
        return super.insertData(adddTextAutoReplyRuleInsert);
    }

    public List<TextAutoReplyRule> getTextAutoReplyRuleByKeyword(final String keyword)
    {
        final LogpieDataQuery<TextAutoReplyRule> getTextAutoReplyRulesByNameSpaceAndKeyQuery = new GetTextAutoReplyRulesByKeywordQuery(
                keyword);
        return super.queryResult(getTextAutoReplyRulesByNameSpaceAndKeyQuery);
    }

    public List<TextAutoReplyRule> getAllTextAutoReplyRules()
    {
        final LogpieDataQuery<TextAutoReplyRule> getAllTextAutoReplyRulesQuery = new GetAllTextAutoReplyRulesQuery();
        return super.queryResult(getAllTextAutoReplyRulesQuery);
    }

    public TextAutoReplyRule getTextAutoReplyRuleById(final String id)
    {
        final LogpieDataQuery<TextAutoReplyRule> getTextAutoReplyRuleByIdQuery = new GetTextAutoReplyRuleByIdQuery(
                id);

        List<TextAutoReplyRule> textAutoReplyRuleList = super.queryResult(
                getTextAutoReplyRuleByIdQuery);
        if (CollectionUtils.isEmpty(textAutoReplyRuleList) || textAutoReplyRuleList.size() > 1)
        {
            LOG.error("The TextAutoReplyRule cannot be found by this id:" + id);
            return null;
        }
        return textAutoReplyRuleList.get(0);
    }

    /**
     * Update the textAutoReplyRule
     * 
     * @param textAutoReplyRule
     * @return
     */
    public boolean updateTextAutoReplyRule(final TextAutoReplyRule textAutoReplyRule)
    {
        final UpdateTextAutoReplyRuleUpdate updateTextAutoReplyRuleUpdate = new UpdateTextAutoReplyRuleUpdate(
                textAutoReplyRule, sTextAutoReplyRuleTableName,
                textAutoReplyRule.getTextAutoReplyRuleId());
        return super.updateData(updateTextAutoReplyRuleUpdate,
                "更新了自动回复规则 " + textAutoReplyRule.getTextAutoReplyRuleKeyword() + ":"
                        + textAutoReplyRule.getTextAutoReplyRuleReplyString());
    }

    private class GetTextAutoReplyRulesByKeywordQuery
            extends LogpieBaseQueryAllTemplateQuery<TextAutoReplyRule>
    {
        final String mKeyword;

        GetTextAutoReplyRulesByKeywordQuery(final String keyword)
        {
            super(new TextAutoReplyRule(), TextAutoReplyRuleDAO.sTextAutoReplyRuleTableName);
            mKeyword = keyword;
        }

        // no query condition for getting all query.
        @Override
        public Set<String> getQueryConditions()
        {
            final Set<String> queryConditions = new HashSet<String>();
            queryConditions.add(String.format("%s=\"%s\"",
                    TextAutoReplyRule.DB_KEY_TEXTAUTOREPLYRULE_KEYWORD, mKeyword));
            return queryConditions;
        }
    }

    private class GetAllTextAutoReplyRulesQuery
            extends LogpieBaseQueryAllTemplateQuery<TextAutoReplyRule>
    {
        GetAllTextAutoReplyRulesQuery()
        {
            super(new TextAutoReplyRule(), TextAutoReplyRuleDAO.sTextAutoReplyRuleTableName);
        }
    }

    private class GetTextAutoReplyRuleByIdQuery
            extends LogpieBaseQuerySingleRecordByIdTemplateQuery<TextAutoReplyRule>
    {
        GetTextAutoReplyRuleByIdQuery(final String textAutoReplyRuleId)
        {
            super(new TextAutoReplyRule(), TextAutoReplyRuleDAO.sTextAutoReplyRuleTableName,
                    TextAutoReplyRule.DB_KEY_TEXTAUTOREPLYRULE_ID, textAutoReplyRuleId);
        }

    }

    private class UpdateTextAutoReplyRuleUpdate
            extends LogpieBaseUpdateRecordTemplateUpdate<TextAutoReplyRule>
    {
        /**
         * @param model
         * @param tableName
         */
        public UpdateTextAutoReplyRuleUpdate(LogpieModel model, String tableName,
                String textAutoReplyRuleId)
        {
            super(model, tableName, TextAutoReplyRule.DB_KEY_TEXTAUTOREPLYRULE_ID,
                    textAutoReplyRuleId);
        }
    }

    private class AddTextAutoReplyRuleInsert implements LogpieDataInsert<TextAutoReplyRule>
    {
        private TextAutoReplyRule mTextAutoReplyRule;

        AddTextAutoReplyRuleInsert(final TextAutoReplyRule textAutoReplyRule)
        {
            mTextAutoReplyRule = textAutoReplyRule;
        }

        @Override
        public String getInsertTable()
        {
            return sTextAutoReplyRuleTableName;
        }

        @Override
        public Map<String, Object> getInsertValues()
        {
            return mTextAutoReplyRule.getModelMap();
        }
    }
}
