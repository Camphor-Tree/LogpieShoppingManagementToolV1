// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.logpie.shopping.management.util.CollectionUtils;

/**
 * The base DAO to handle the logic of query/insert/update/delete operations
 * 
 * @author zhoyilei
 */
public class LogpieBaseDAO<T>
{
    private static final Logger LOG = Logger.getLogger(LogpieBaseDAO.class);
    private static final String sBaseQuerySQL = "select * from ";

    public LogpieBaseDAO()
    {
    }

    public List<T> queryResult(final LogpieDataQuery<T> query)
    {
        final JdbcTemplate jdbcTemplate = LogpieDataSourceFactory.getJdbcTemplate();
        final RowMapper<T> resultMapper = query.getQueryResultMapper();
        final Set<String> queryConditions = query.getQueryConditions();
        final Set<String> queryTables = query.getQueryTables();

        if (CollectionUtils.isEmpty(queryTables))
        {
            throw new IllegalArgumentException("The query talbe cannot be null or empty");
        }
        final String queryTablesSql = buildQueryTablesSQL(queryTables);
        final String querySQL = buildQuerySQL(queryConditions, queryTablesSql);
        LOG.debug("querySQL is:" + querySQL);
        final List<T> results = jdbcTemplate.query(querySQL, resultMapper);
        return results;
    }

    /**
     * @param queryConditions
     * @param queryTablesSql
     * @return
     */
    private String buildQuerySQL(final Set<String> queryConditions, final String queryTablesSql)
    {
        String querySQL;
        if (!CollectionUtils.isEmpty(queryConditions))
        {
            final String queryConditionSql = buildQueryConditionsSQL(queryConditions);
            querySQL = sBaseQuerySQL + queryTablesSql + queryConditionSql;
        }
        else
        {
            querySQL = sBaseQuerySQL + queryTablesSql;
        }
        return querySQL;
    }

    /**
     * @param queryTables
     */
    private String buildQueryTablesSQL(final Set<String> queryTables)
    {
        final StringBuilder queryTablesBuilder = new StringBuilder();
        final int countTables = queryTables.size();
        int i = 0;
        for (final String table : queryTables)
        {
            queryTablesBuilder.append(table);
            if (++i < countTables)
            {
                queryTablesBuilder.append(",");
            }
            else
            {
                queryTablesBuilder.append(" ");
            }
        }
        return queryTablesBuilder.toString();
    }

    /**
     * @param queryConditions
     */
    private String buildQueryConditionsSQL(final Set<String> queryConditions)
    {
        final StringBuilder queryConditionBuilder = new StringBuilder("where ");
        final int countTables = queryConditions.size();
        int i = 0;
        for (final String condition : queryConditions)
        {
            queryConditionBuilder.append(condition);
            if (++i < countTables)
            {
                queryConditionBuilder.append(" AND ");
            }
            else
            {
                queryConditionBuilder.append(" ");
            }
        }
        return queryConditionBuilder.toString();
    }
}
