// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author zhoyilei
 *
 */
public class LogpieBaseQuerySingleRecordByIdTemplateQuery<T> implements LogpieDataQuery<T>
{
    private RowMapper<T> mRowMapper;
    private String mTableName;
    protected String mKeyForId;
    protected String mValueForId;

    public LogpieBaseQuerySingleRecordByIdTemplateQuery(final RowMapper<T> rowMapper,
            final String tableName, final String keyForId, final String valueForId)
    {
        mRowMapper = rowMapper;
        mTableName = tableName;
        mKeyForId = keyForId;
        mValueForId = valueForId;
    }

    @Override
    public RowMapper<T> getQueryResultMapper()
    {
        return mRowMapper;
    }

    @Override
    public Set<String> getQueryConditions()
    {
        final Set<String> queryConditionsSet = new HashSet<String>();
        queryConditionsSet.add(mKeyForId + "=\"" + mValueForId + "\"");
        return queryConditionsSet;
    }

    @Override
    public Map<String, String> getQueryTables()
    {
        final Map<String, String> tableMap = new HashMap<String, String>();
        tableMap.put(LogpieBaseDAO.sNonAliasPrefix + mTableName, mTableName);
        return tableMap;
    }

    @Override
    public Set<String> getOrderBy()
    {
        return null;
    }

}
