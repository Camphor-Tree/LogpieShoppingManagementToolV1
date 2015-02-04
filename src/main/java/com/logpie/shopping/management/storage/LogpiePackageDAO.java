// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.LogpiePackage;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * @author zhoyilei
 *
 */
public class LogpiePackageDAO extends LogpieBaseDAO<LogpiePackage>
{
    private static final Logger LOG = Logger.getLogger(LogpiePackageDAO.class);
    private static final String sPackageTableName = "Packages";

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
     * For querying specific Package by PackageId
     * 
     * @param packageId
     * @return Package corresponding to the PackageId
     */
    public LogpiePackage getPackageById(final String packageId)
    {
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
            final String packageProxyName = mPackage.getPackgeProxyName();
            final String packageTrackingNumber = mPackage.getPackageTrackingNumber();
            final String packageReceiver = mPackage.getPackageReceiver();
            final String packageDestination = mPackage.getPackageDestination();
            final String packageDate = mPackage.getPackageDate();
            final Integer packageWeight = mPackage.getPackageWeight();
            final Integer packgeShippingFee = mPackage.getPackgeShippingFee();
            final Integer packageAdditionalCustomTaxFee = mPackage
                    .getPackageAdditionalCustomTaxFee();
            final Integer packageAdditionalInsuranceFee = mPackage
                    .getPackageAdditionalInsuranceFee();
            final Boolean packageIsShipped = mPackage.getPackageIsShipped();
            final Boolean packageIsDelivered = mPackage.getPackageIsDelivered();
            final String packageNote = mPackage.getPackageNote();

            final Map<String, Object> insertValues = new HashMap<String, Object>();
            insertValues.put(LogpiePackage.DB_KEY_PACKAGE_PROXY_NAME, packageProxyName);
            insertValues.put(LogpiePackage.DB_KEY_PACKAGE_TRACKING_NUMBER, packageTrackingNumber);
            insertValues.put(LogpiePackage.DB_KEY_PACKAGE_RECEIVER, packageReceiver);
            insertValues.put(LogpiePackage.DB_KEY_PACKAGE_DESTINATION, packageDestination);
            insertValues.put(LogpiePackage.DB_KEY_PACKAGE_DATE, packageDate);
            insertValues.put(LogpiePackage.DB_KEY_PACKAGE_WEIGHT, packageWeight);
            insertValues.put(LogpiePackage.DB_KEY_PACKAGE_SHIPPING_FEE, packgeShippingFee);
            insertValues.put(LogpiePackage.DB_KEY_PACKAGE_ADDITIONAL_CUSTOM_TAX_FEE,
                    packageAdditionalCustomTaxFee);
            insertValues.put(LogpiePackage.DB_KEY_PACKAGE_ADDITIONAL_INSURANCE_FEE,
                    packageAdditionalInsuranceFee);
            insertValues.put(LogpiePackage.DB_KEY_PACKAGE_IS_SHIPPED, packageIsShipped);
            insertValues.put(LogpiePackage.DB_KEY_PACKAGE_IS_DELIVERED, packageIsDelivered);
            insertValues.put(LogpiePackage.DB_KEY_PACKAGE_NOTE, packageNote);
            return insertValues;
        }
    }

    private class GetAllPackageQuery extends LogpieBaseQueryAllTemplateQuery<LogpiePackage>
    {
        GetAllPackageQuery()
        {
            super(new LogpiePackage(), LogpiePackageDAO.sPackageTableName);
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

}
