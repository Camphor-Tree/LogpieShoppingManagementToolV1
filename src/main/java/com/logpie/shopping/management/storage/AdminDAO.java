// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.logpie.shopping.management.auth.logic.Admin;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * @author zhoyilei
 *
 */
public class AdminDAO extends LogpieBaseDAO<Admin>
{
    private static final Logger LOG = Logger.getLogger(AdminDAO.class);

    private static final String sAdminTableName = "Admins";

    /**
     * This API is used to authenticate account.
     * 
     * @param email
     * @param password
     * @return
     */
    public Admin verifyAccount(final String email, final String password)
    {
        LogpieDataQuery<Admin> verifyAccountQuery = new VerifyAccountQuery(email, password);
        List<Admin> queryResult = super.queryResult(verifyAccountQuery);
        if (CollectionUtils.isEmpty(queryResult) || queryResult.size() > 1)
        {
            LOG.error("Email-password is not correct");
            return null;
        }
        return queryResult.get(0);
    }

    public Admin queryAccountByAdminId(final String adminId)
    {
        LogpieDataQuery<Admin> queryAccountByIdQuery = new QueryAccountByAdminIdQuery(adminId);
        List<Admin> queryResult = super.queryResult(queryAccountByIdQuery);
        if (CollectionUtils.isEmpty(queryResult) || queryResult.size() > 1)
        {
            LOG.error("Admin id is not correct or not unique");
            return null;
        }
        return queryResult.get(0);
    }

    // public Admin queryAccountByAdminEmail(final String adminEmail)
    // {
    //
    // }

    private class VerifyAccountQuery implements LogpieDataQuery<Admin>
    {
        final String mEmail;
        final String mPassword;

        VerifyAccountQuery(final String email, final String password)
        {
            mEmail = email;
            mPassword = password;
        }

        @Override
        public RowMapper<Admin> getQueryResultMapper()
        {
            return new Admin();
        }

        @Override
        public Set<String> getQueryConditions()
        {
            Set<String> queryConditionsSet = new HashSet<String>();
            queryConditionsSet.add("AdminEmail=\"" + mEmail + "\"");
            queryConditionsSet.add("AdminPassword=\"" + mPassword + "\"");
            return queryConditionsSet;
        }

        @Override
        public Set<String> getQueryTables()
        {
            Set<String> tableSet = new HashSet<String>();
            tableSet.add(AdminDAO.sAdminTableName);
            return tableSet;
        }
    }

    private class QueryAccountByAdminIdQuery implements LogpieDataQuery<Admin>
    {
        final String mAdminId;

        QueryAccountByAdminIdQuery(final String adminId)
        {
            mAdminId = adminId;
        }

        @Override
        public RowMapper<Admin> getQueryResultMapper()
        {
            return new Admin();
        }

        @Override
        public Set<String> getQueryConditions()
        {
            Set<String> queryConditionsSet = new HashSet<String>();
            queryConditionsSet.add("AdminId=\"" + mAdminId + "\"");
            return queryConditionsSet;
        }

        @Override
        public Set<String> getQueryTables()
        {
            Set<String> tableSet = new HashSet<String>();
            tableSet.add(AdminDAO.sAdminTableName);
            return tableSet;
        }
    }

}
