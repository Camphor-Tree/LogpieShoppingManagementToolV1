// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.DBLog;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * The base DAO to handle the logic of query/insert/update/delete operations
 * 
 * @author zhoyilei
 */
public class LogpieBaseDAO<T>
{
    private static final Logger LOG = Logger.getLogger(LogpieBaseDAO.class);
    private static final String sBaseQuerySQL = "SELECT * FROM ";
    private static final String sBaseInsertSQL = "INSERT INTO ";
    private static final String sBaseUpdateSQL = "UPDATE ";
    private static final String sBaseOrderBySQL = " ORDER BY ";

    public static final String sNonAliasPrefix = "LogpieNoAlias";

    protected Admin mCurrentAdmin;

    public LogpieBaseDAO(final Admin admin)
    {
        mCurrentAdmin = admin;
    }

    /**
     * Insert data into the database
     * 
     * @return
     */
    public boolean insertData(final LogpieDataInsert<T> dataInsert)
    {
        final JdbcTemplate jdbcTemplate = LogpieDataSourceFactory.getJdbcTemplate();
        final String insertSQL = buildInsertSQL(dataInsert);
        LOG.debug("insert sql is:" + insertSQL);
        try
        {
            jdbcTemplate.execute(insertSQL);
            return true;
        } catch (Exception e)
        {
            LOG.error("Error happened when inserting the data", e);
        }
        return false;
    }

    /**
     * Query result from database
     * 
     * @param query
     * @return
     */
    public List<T> queryResult(final LogpieDataQuery<T> query)
    {
        final JdbcTemplate jdbcTemplate = LogpieDataSourceFactory.getJdbcTemplate();
        final RowMapper<T> resultMapper = query.getQueryResultMapper();
        final Set<String> queryConditions = query.getQueryConditions();
        final Map<String, String> joinTables = query.getJoinTables();
        final Set<String> queryOrderBy = query.getOrderBy();
        final String mainTable = query.getMainQueryTable();
        // Left join condition is used to handle the situation foreign key may
        // be null.
        final Set<String> leftJoinCondition = query.getLeftJoinCondition();

        final String joinTablesSql = buildJoinTablesSQL(joinTables, leftJoinCondition);
        final String querySQL = buildQuerySQL(mainTable, queryConditions, joinTablesSql,
                queryOrderBy);
        LOG.debug("querySQL is:" + querySQL);
        final List<T> results = jdbcTemplate.query(querySQL, resultMapper);
        return results;
    }

    public boolean updateData(final LogpieDataUpdate<T> update, String updateLog)
    {
        final JdbcTemplate jdbcTemplate = LogpieDataSourceFactory.getJdbcTemplate();
        final String updateSQL = buildUpdateSQL(update);
        if (updateLog != null)
        {
            logInformation(updateSQL, updateLog);
        }

        try
        {
            jdbcTemplate.execute(updateSQL);
            return true;
        } catch (Exception e)
        {
            LOG.error("Error happened when updating the data", e);
        }
        return false;
    }

    /**
     * 
     * @param mainTable
     * @param queryConditions
     * @param queryTablesSql
     * @param queryOrderBy
     * @return
     */
    private String buildQuerySQL(final String mainTable, final Set<String> queryConditions,
            final String joinTablesSql, final Set<String> queryOrderBy)
    {
        String querySQL = sBaseQuerySQL + mainTable + " ";
        if (joinTablesSql != null)
        {
            querySQL += joinTablesSql;
        }
        if (!CollectionUtils.isEmpty(queryConditions))
        {
            final String queryConditionSql = buildConditionsSQL(queryConditions);
            querySQL += queryConditionSql;
        }

        if (!CollectionUtils.isEmpty(queryOrderBy))
        {
            querySQL = querySQL + buildOrderBySQL(queryOrderBy);

        }
        return querySQL;
    }

    private String buildOrderBySQL(final Set<String> orderBySet)
    {
        final StringBuilder orderByBuilder = new StringBuilder(sBaseOrderBySQL);
        final int size = orderBySet.size();
        int i = 0;
        for (final String orderBy : orderBySet)
        {
            orderByBuilder.append(orderBy);
            if (++i < size)
            {
                orderByBuilder.append(",");
            }
            else
            {
                orderByBuilder.append(" ");
            }
        }
        return orderByBuilder.toString();
    }

    /**
     * @param joinTables
     */
    private String buildJoinTablesSQL(final Map<String, String> joinTables,
            final Set<String> leftJoinCondition)
    {
        if (joinTables == null || joinTables.size() == 0)
        {
            return null;
        }
        final StringBuilder queryTablesBuilder = new StringBuilder();
        queryTablesBuilder.append(" ");
        for (final Map.Entry<String, String> tableEntry : joinTables.entrySet())
        {
            queryTablesBuilder.append("JOIN ");
            // value is the real table name
            queryTablesBuilder.append(tableEntry.getValue());
            // Append table name alias, which is used in multiple foreign keys
            // connections
            if (!tableEntry.getKey().startsWith(sNonAliasPrefix))
            {
                queryTablesBuilder.append(" AS ");
                // key is the alias name, alias names won't be duplicate
                queryTablesBuilder.append(tableEntry.getKey());
            }
            queryTablesBuilder.append(" ");
        }

        if (!CollectionUtils.isEmpty(leftJoinCondition))
        {
            for (final String leftJoin : leftJoinCondition)
            {
                queryTablesBuilder.append(leftJoin);
                queryTablesBuilder.append(" ");
            }
        }
        return queryTablesBuilder.toString();
    }

    /**
     * @param queryConditions
     */
    private String buildConditionsSQL(final Set<String> queryConditions)
    {
        final StringBuilder queryConditionBuilder = new StringBuilder("WHERE ");
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

    private String buildInsertSQL(final LogpieDataInsert<T> dataInsert)
    {
        final StringBuilder insertSqlBuilder = new StringBuilder(sBaseInsertSQL);
        final String table = dataInsert.getInsertTable();
        final Map<String, Object> dataMap = dataInsert.getInsertValues();
        // append the table name
        insertSqlBuilder.append(table);

        final StringBuilder keyBuilder = new StringBuilder(" (");
        final StringBuilder valueBuilder = new StringBuilder(" values (");
        int i = 0;
        int size = dataMap.size();
        for (Map.Entry<String, Object> dataEntry : dataMap.entrySet())
        {
            final String key = dataEntry.getKey();
            final Object value = dataEntry.getValue();
            keyBuilder.append(key);
            if (++i < size)
            {
                // If not last element, append ","
                keyBuilder.append(",");
            }

            if (value instanceof String)
            {
                valueBuilder.append("\"");
                valueBuilder.append(value);
                valueBuilder.append("\"");
            }
            else
            {
                // if the value is null, then String.valueOf will return "null"
                // which is accepted by MySQL.
                valueBuilder.append(String.valueOf(value));
            }

            // If not last element, append ","
            if (i < size)
            {
                valueBuilder.append(",");
            }

        }
        keyBuilder.append(")");
        valueBuilder.append(")");

        // append the keys and values sql statement
        insertSqlBuilder.append(keyBuilder.toString());
        insertSqlBuilder.append(valueBuilder.toString());
        return insertSqlBuilder.toString();
    }

    private String buildUpdateSQL(LogpieDataUpdate<T> update)
    {
        final StringBuilder updateSqlBuilder = new StringBuilder(sBaseUpdateSQL);
        final String table = update.getUpdateTable();
        final Set<String> condition = update.getUpdateConditions();
        final Map<String, Object> updateValues = update.getUpdateValues();

        updateSqlBuilder.append(table);
        updateSqlBuilder.append(" SET ");

        int i = 0;
        int size = updateValues.size();
        for (Map.Entry<String, Object> dataEntry : updateValues.entrySet())
        {
            final String key = dataEntry.getKey();
            final Object value = dataEntry.getValue();
            updateSqlBuilder.append(key);
            updateSqlBuilder.append("=");
            if (value instanceof String)
            {
                updateSqlBuilder.append("\'");
                updateSqlBuilder.append(value);
                updateSqlBuilder.append("\'");
            }
            else
            {
                // if the value is null, then String.valueOf will return "null"
                // which is accepted by MySQL.
                updateSqlBuilder.append(String.valueOf(value));
            }
            if (++i < size)
            {
                // If not last element, append ","
                updateSqlBuilder.append(",");
            }
            else
            {
                updateSqlBuilder.append(" ");
            }
        }
        if (!CollectionUtils.isEmpty(condition))
        {
            updateSqlBuilder.append(buildConditionsSQL(condition));
        }
        return updateSqlBuilder.toString();
    }

    public void logInformation(final String SQL, final String comment)
    {
        final DBLog dbLog = new DBLog(mCurrentAdmin, SQL, comment);
        final DBLogDAO dbLogDAO = new DBLogDAO(mCurrentAdmin);
        dbLogDAO.addDBLog(dbLog);
    }
}
