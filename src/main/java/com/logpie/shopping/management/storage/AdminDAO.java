// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.LogpieModel;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * @author zhoyilei
 *
 */
public class AdminDAO extends LogpieBaseDAO<Admin>
{
    private static final Logger LOG = Logger.getLogger(AdminDAO.class);

    public static final String sAdminTableName = "Admins";

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
        if (adminId == null)
        {
            return null;
        }
        LogpieDataQuery<Admin> queryAccountByIdQuery = new QueryAccountByAdminIdQuery(adminId);
        List<Admin> queryResult = super.queryResult(queryAccountByIdQuery);
        if (CollectionUtils.isEmpty(queryResult) || queryResult.size() > 1)
        {
            LOG.error("Admin id is not correct or not unique");
            return null;
        }
        return queryResult.get(0);
    }

    /**
     * For adding a new admin account into the database
     * 
     * @param admin
     * @return true if adding admin successfully. false if adding admin fails
     */
    public boolean addAdmin(final Admin admin)
    {
        final LogpieDataInsert<Admin> addAdminInsert = new AddAdminInsert(admin);
        return super.insertData(addAdminInsert);
    }

    /**
     * Update the admin profile
     * 
     * @param admin
     * @return
     */
    public boolean updateAdminProfile(final Admin admin)
    {
        final UpdateAdminUpdate updateAdminUpdate = new UpdateAdminUpdate(admin, sAdminTableName,
                admin.getAdminId());
        return super.updateData(updateAdminUpdate);
    }

    public List<Admin> getAllAdmins()
    {
        final GetAllAdminQuery getAllAdminQuery = new GetAllAdminQuery();
        final List<Admin> adminList = super.queryResult(getAllAdminQuery);
        return adminList;
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
            final Set<String> queryConditionsSet = new HashSet<String>();
            queryConditionsSet.add("AdminEmail=\"" + mEmail + "\"");
            queryConditionsSet.add("AdminPassword=\"" + mPassword + "\"");
            return queryConditionsSet;
        }

        @Override
        public Map<String, String> getQueryTables()
        {
            final Map<String, String> tableMap = new HashMap<String, String>();
            tableMap.put(sNonAliasPrefix + AdminDAO.sAdminTableName, AdminDAO.sAdminTableName);
            return tableMap;
        }
    }

    private class QueryAccountByAdminIdQuery extends
            LogpieBaseQuerySingleRecordByIdTemplateQuery<Admin>
    {
        QueryAccountByAdminIdQuery(final String adminId)
        {
            super(new Admin(), AdminDAO.sAdminTableName, Admin.DB_KEY_ADMIN_ID, adminId);
        }
    }

    private class AddAdminInsert implements LogpieDataInsert<Admin>
    {
        private Admin mAdmin;

        AddAdminInsert(final Admin admin)
        {
            mAdmin = admin;
        }

        @Override
        public String getInsertTable()
        {
            return sAdminTableName;
        }

        @Override
        public Map<String, Object> getInsertValues()
        {
            return mAdmin.getModelMap();
        }
    }

    private class UpdateAdminUpdate extends LogpieBaseUpdateRecordTemplateUpdate<Admin>
    {
        /**
         * @param model
         * @param tableName
         */
        public UpdateAdminUpdate(LogpieModel model, String tableName, final String adminId)
        {
            super(model, tableName, Admin.DB_KEY_ADMIN_ID, adminId);
        }
    }

    private class GetAllAdminQuery extends LogpieBaseQueryAllTemplateQuery<Admin>
    {
        GetAllAdminQuery()
        {
            super(new Admin(), AdminDAO.sAdminTableName);
        }
    }

}
