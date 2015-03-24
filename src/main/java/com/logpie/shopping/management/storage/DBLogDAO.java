// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.DBLog;

/**
 * @author zhoyilei
 *
 */
public class DBLogDAO extends LogpieBaseDAO<DBLog>
{
    /**
     * @param admin
     */
    public DBLogDAO(Admin admin)
    {
        super(admin);
    }

    private static final Logger LOG = Logger.getLogger(DBLogDAO.class);
    public static final String sDBLogTableName = "DbLog";

    public boolean addDBLog(final DBLog dbLog)
    {
        final LogpieDataInsert<DBLog> addDBLogInsert = new AddDBLogInsert(dbLog);
        return super.insertData(addDBLogInsert);
    }

    /**
     * For getting all existing DBLog
     * 
     * @return All existing DBLog
     */
    public List<DBLog> getAllDBLog()
    {
        GetAllDBLogQuery getAllDBLogQuery = new GetAllDBLogQuery();
        return super.queryResult(getAllDBLogQuery);
    }

    private class AddDBLogInsert implements LogpieDataInsert<DBLog>
    {
        private DBLog mDBLog;

        AddDBLogInsert(final DBLog dbLog)
        {
            mDBLog = dbLog;
        }

        @Override
        public String getInsertTable()
        {
            return sDBLogTableName;
        }

        @Override
        public Map<String, Object> getInsertValues()
        {
            return mDBLog.getModelMap();
        }
    }

    private class GetAllDBLogQuery extends LogpieBaseQueryAllTemplateQuery<DBLog>
    {
        GetAllDBLogQuery()
        {
            super(new DBLog(), DBLogDAO.sDBLogTableName);
        }

        // foreign key connection
        @Override
        public Set<String> getQueryConditions()
        {
            return getForeignKeyConnectionConditions();
        }

        @Override
        public Map<String, String> getJoinTables()
        {
            return getForeignKeyConnectionTables();
        }
    }

    private static Set<String> getForeignKeyConnectionConditions()
    {
        final Set<String> conditions = new HashSet<String>();
        conditions
                .add(String.format("%s = %s", DBLog.DB_KEY_DBLOG_ADMIN_ID, Admin.DB_KEY_ADMIN_ID));
        return conditions;
    }

    public static Map<String, String> getForeignKeyConnectionTables()
    {
        final Map<String, String> tableMap = new HashMap<String, String>();
        tableMap.put(sNonAliasPrefix + AdminDAO.sAdminTableName, AdminDAO.sAdminTableName);
        return tableMap;
    }
}
