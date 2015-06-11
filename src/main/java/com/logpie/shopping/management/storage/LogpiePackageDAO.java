// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.LogpieModel;
import com.logpie.shopping.management.model.LogpiePackage;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * @author zhoyilei
 *
 */
public class LogpiePackageDAO extends LogpieBaseDAO<LogpiePackage>
{
    /**
     * @param admin
     */
    public LogpiePackageDAO(Admin admin)
    {
        super(admin);
    }

    private static final Logger LOG = Logger.getLogger(LogpiePackageDAO.class);
    public static final String sPackageTableName = "Packages";

    /**
     * For adding a new package into the database
     * 
     * @param package
     * @return true if adding package successfully. false if adding package
     *         fails
     */
    public boolean addPackage(final LogpiePackage logpiePackage)
    {
        final LogpieDataInsert<LogpiePackage> addPackageInsert = new AddPackageInsert(logpiePackage);
        return super.insertData(addPackageInsert);
    }

    /**
     * For getting all existing categories
     * 
     * @return All existing categories
     */
    public List<LogpiePackage> getAllPackage()
    {
        GetAllPackageQuery getAllPackageQuery = new GetAllPackageQuery();
        return super.queryResult(getAllPackageQuery);
    }

    /**
     * Query packages
     * 
     * @return All existing categories
     */
    public List<LogpiePackage> searchPackage(final String searchString)
    {
        SearchPackageQuery getAllPackageQuery = new SearchPackageQuery(searchString);
        return super.queryResult(getAllPackageQuery);
    }

    /**
     * Update the package profile
     * 
     * @param logpiePackage
     * @return
     */
    public boolean updateLogpiePackageProfile(final LogpiePackage logpiePackage)
    {
        final LogpiePackage previousPackage = this.getPackageById(logpiePackage.getPackageId());
        final String updateLog = logpiePackage.getDeltaChange(previousPackage);
        final UpdateLogpiePackageUpdate updateLogpiePackageUpdate = new UpdateLogpiePackageUpdate(
                logpiePackage, sPackageTableName, logpiePackage.getPackageId());
        return super.updateData(updateLogpiePackageUpdate, updateLog);
    }

    /**
     * For querying specific Package by PackageId
     * 
     * @param packageId
     * @return Package corresponding to the PackageId
     */
    public LogpiePackage getPackageById(final String packageId)
    {
        if (StringUtils.isEmpty(packageId))
        {
            return null;
        }
        GetPackageByIdQuery getPackageByIdQuery = new GetPackageByIdQuery(packageId);
        List<LogpiePackage> packageList = super.queryResult(getPackageByIdQuery);
        if (CollectionUtils.isEmpty(packageList) || packageList.size() > 1)
        {
            LOG.error("The package cannot be found by this id:" + packageId);
            return null;
        }
        return packageList.get(0);

    }

    private class AddPackageInsert implements LogpieDataInsert<LogpiePackage>
    {
        private LogpiePackage mPackage;

        AddPackageInsert(final LogpiePackage logpiePackage)
        {
            mPackage = logpiePackage;
        }

        @Override
        public String getInsertTable()
        {
            return sPackageTableName;
        }

        @Override
        public Map<String, Object> getInsertValues()
        {
            return mPackage.getModelMap();
        }
    }

    private class GetAllPackageQuery extends LogpieBaseQueryAllTemplateQuery<LogpiePackage>
    {
        GetAllPackageQuery()
        {
            super(new LogpiePackage(), LogpiePackageDAO.sPackageTableName);
        }
    }

    private class SearchPackageQuery extends LogpieBaseQueryAllTemplateQuery<LogpiePackage>
    {
        private String mSearchString;

        SearchPackageQuery(final String searchString)
        {
            super(new LogpiePackage(), LogpiePackageDAO.sPackageTableName);
            mSearchString = searchString;
        }

        @Override
        public Set<String> getQueryConditions()
        {
            final Set<String> conditions = new HashSet<String>();
            // add search condition
            final StringBuilder searchConditionBuilder = new StringBuilder("(");
            searchConditionBuilder.append(LogpiePackage.DB_KEY_PACKAGE_TRACKING_NUMBER);
            searchConditionBuilder.append(" like '%" + mSearchString + "%' OR ");
            searchConditionBuilder.append(LogpiePackage.DB_KEY_PACKAGE_RECEIVER);
            searchConditionBuilder.append(" like '%" + mSearchString + "%' OR ");
            searchConditionBuilder.append(LogpiePackage.DB_KEY_PACKAGE_DESTINATION);
            searchConditionBuilder.append(" like '%" + mSearchString + "%' OR ");
            searchConditionBuilder.append(LogpiePackage.DB_KEY_PACKAGE_NOTE);
            searchConditionBuilder.append(" like '%" + mSearchString + "%')");
            conditions.add(searchConditionBuilder.toString());
            return conditions;
        }
    }

    private class GetPackageByIdQuery extends
            LogpieBaseQuerySingleRecordByIdTemplateQuery<LogpiePackage>
    {
        GetPackageByIdQuery(final String packageId)
        {
            super(new LogpiePackage(), LogpiePackageDAO.sPackageTableName,
                    LogpiePackage.DB_KEY_PACKAGE_ID, packageId);
        }
    }

    private class UpdateLogpiePackageUpdate extends
            LogpieBaseUpdateRecordTemplateUpdate<LogpiePackage>
    {
        /**
         * @param model
         * @param tableName
         */
        public UpdateLogpiePackageUpdate(LogpieModel model, String tableName, String packageId)
        {
            super(model, tableName, LogpiePackage.DB_KEY_PACKAGE_ID, packageId);
        }
    }

}
