// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.SettleDownRecord;

/**
 * @author zhoyilei
 *
 */
public class SettleDownRecordDAO extends LogpieBaseDAO<SettleDownRecord>
{
    private static final Logger LOG = Logger.getLogger(CouponDAO.class);
    public static final String sSettleDownRecordTable = "SettleDownRecords";

    /**
     * @param admin
     */
    public SettleDownRecordDAO(Admin admin)
    {
        super(admin);
    }

    /**
     * For recording a used coupon into the database
     * 
     * @param coupon
     * @return true if adding coupon successfully. false if adding coupon fails
     */
    public boolean addSettleDownRecord(final SettleDownRecord record)
    {
        final LogpieDataInsert<SettleDownRecord> addSettleDownRecordInsert = new AddSettleDownRecordInsert(
                record);
        return super.insertData(addSettleDownRecordInsert);
    }

    public List<SettleDownRecord> getAllSettleDownRecords()
    {
        final LogpieDataQuery<SettleDownRecord> getAllSettleDownRecordQuery = new GetAllSettleDownRecordQuery();
        return super.queryResult(getAllSettleDownRecordQuery);
    }

    public List<SettleDownRecord> getSettleDownRecordsByAdmin(final Admin admin)
    {
        final LogpieDataQuery<SettleDownRecord> getSettleDownRecordsByAdminQuery = new GetSettleDownRecordsByAdminQuery(
                admin);
        return super.queryResult(getSettleDownRecordsByAdminQuery);
    }

    private static Map<String, String> getForeignKeyConnectionTables()
    {
        final Map<String, String> tableMap = new HashMap<String, String>();
        tableMap.put(sNonAliasPrefix + AdminDAO.sAdminTableName, AdminDAO.sAdminTableName);
        return tableMap;
    }

    private static Set<String> getForeignKeyConnectionConditions()
    {
        final Set<String> conditions = new HashSet<String>();
        conditions.add(String.format("%s = %s", SettleDownRecord.DB_KEY_SETTLE_DOWN_RECORD_ADMIN,
                Admin.DB_KEY_ADMIN_ID));
        return conditions;
    }

    private class GetAllSettleDownRecordQuery extends
            LogpieBaseQueryAllTemplateQuery<SettleDownRecord>
    {

        GetAllSettleDownRecordQuery()
        {
            super(new SettleDownRecord(), SettleDownRecordDAO.sSettleDownRecordTable);
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

        @Override
        public Set<String> getLeftJoinCondition()
        {
            return null;
        };

        @Override
        public Set<String> getOrderBy()
        {
            return super.getOrderBy();
        }
    }

    private class GetSettleDownRecordsByAdminQuery extends
            LogpieBaseQueryAllTemplateQuery<SettleDownRecord>
    {
        final Admin mAdmin;

        GetSettleDownRecordsByAdminQuery(final Admin admin)
        {
            super(new SettleDownRecord(), SettleDownRecordDAO.sSettleDownRecordTable);
            mAdmin = admin;
        }

        // foreign key connection
        @Override
        public Set<String> getQueryConditions()
        {
            Set<String> conditions = getForeignKeyConnectionConditions();
            conditions.add(String.format("%s=%s", SettleDownRecord.DB_KEY_SETTLE_DOWN_RECORD_ADMIN,
                    mAdmin.getAdminId()));
            return conditions;
        }

        @Override
        public Map<String, String> getJoinTables()
        {
            return getForeignKeyConnectionTables();
        }

        @Override
        public Set<String> getLeftJoinCondition()
        {
            return null;
        };

        @Override
        public Set<String> getOrderBy()
        {
            return super.getOrderBy();
        }
    }

    private class AddSettleDownRecordInsert implements LogpieDataInsert<SettleDownRecord>
    {
        private SettleDownRecord mSettleDownRecord;

        AddSettleDownRecordInsert(final SettleDownRecord settleDownRecord)
        {
            mSettleDownRecord = settleDownRecord;
        }

        @Override
        public String getInsertTable()
        {
            return sSettleDownRecordTable;
        }

        @Override
        public Map<String, Object> getInsertValues()
        {
            return mSettleDownRecord.getModelMap();
        }
    }

}
