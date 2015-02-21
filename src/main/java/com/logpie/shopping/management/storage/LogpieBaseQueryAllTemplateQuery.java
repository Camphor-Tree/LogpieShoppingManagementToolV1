// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;

import com.logpie.shopping.management.model.LogpieModel;

/**
 * @author zhoyilei
 *
 */
public class LogpieBaseQueryAllTemplateQuery<T> implements LogpieDataQuery<T>
{
    private RowMapper<T> mRowMapper;
    private String mTableName;

    public LogpieBaseQueryAllTemplateQuery(final RowMapper<T> rowMapper, final String tableName)
    {
        mRowMapper = rowMapper;
        mTableName = tableName;
    }

    @Override
    public RowMapper<T> getQueryResultMapper()
    {
        return mRowMapper;
    }

    // no query condition for getting all query.
    @Override
    public Set<String> getQueryConditions()
    {
        return null;
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
        if (mRowMapper instanceof LogpieModel)
        {
            final Set<String> orderBySet = new HashSet<String>();
            final LogpieModel model = (LogpieModel) mRowMapper;
            orderBySet.add(model.getPrimaryKey() + " DESC");
            return orderBySet;
        }
        return null;
    }
}
