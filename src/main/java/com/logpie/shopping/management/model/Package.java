// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author zhoyilei
 *
 */
public class Package implements RowMapper<Package>
{
    public static String DB_KEY_PACKAGE_ID = "PackageId";
    public static String DB_KEY_PACKAGE_PROXY_NAME = "PackageProxyName";
    public static String DB_KEY_PACKAGE_TRACKING_NUMBER = "PackageTrackingNumber";
    public static String DB_KEY_PACKAGE_RECEIVER = "PackageReceiver";
    public static String DB_KEY_PACKAGE_DESTINATION = "PackageDestination";
    public static String DB_KEY_PACKAGE_DATE = "PackageDate";
    public static String DB_KEY_PACKAGE_WEIGHT = "PackageWeight";
    public static String DB_KEY_PACKAGE_SHIPPING_FEE = "PackgeShippingFee";
    public static String DB_KEY_PACKAGE_ADDITIONAL_CUSTOM_TAX_FEE = "PackageAdditionalCustomTaxFee";
    public static String DB_KEY_PACKAGE_ADDITIONAL_INSURANCE_FEE = "PackageAdditionalInsuranceFee";
    public static String DB_KEY_PACKAGE_IS_SHIPPED = "PackageIsShipped";
    public static String DB_KEY_PACKAGE_IS_DELIVERED = "PackageIsDelivered";
    public static String DB_KEY_PACKAGE_NOTE = "PackageNote";

    private String mPackageId;
    private String mPackgeProxyName;
    private String mPackageTrackingNumber;
    private String mPackageReceiver;
    private String mPackageDestination;
    private String mPackageDate;
    private int mPackageWeight;
    private int mPackgeShippingFee;
    private int mPackageAdditionalCustomTaxFee;
    private int mPackageAdditionalInsuranceFee;
    private boolean mPackageIsShipped;
    private boolean mPackageIsDelivered;
    private String mPackageNote;

    /**
     * @param packageId
     * @param packgeProxyName
     * @param trackingNumber
     * @param packageReceiver
     * @param packageDestination
     * @param packageDate
     * @param packageWeight
     * @param packgeShippingFee
     * @param packageAdditionalCustomTaxFee
     * @param packageAdditionalInsuranceFee
     * @param packageIsShipped
     * @param packageIsDelivered
     * @param packageNote
     */
    public Package(String packageId, String packgeProxyName, String packageTrackingNumber,
            String packageReceiver, String packageDestination, String packageDate,
            int packageWeight, int packgeShippingFee, int packageAdditionalCustomTaxFee,
            int packageAdditionalInsuranceFee, boolean packageIsShipped,
            boolean packageIsDelivered, String packageNote)
    {
        super();
        mPackageId = packageId;
        mPackgeProxyName = packgeProxyName;
        mPackageTrackingNumber = packageTrackingNumber;
        mPackageReceiver = packageReceiver;
        mPackageDestination = packageDestination;
        mPackageDate = packageDate;
        mPackageWeight = packageWeight;
        mPackgeShippingFee = packgeShippingFee;
        mPackageAdditionalCustomTaxFee = packageAdditionalCustomTaxFee;
        mPackageAdditionalInsuranceFee = packageAdditionalInsuranceFee;
        mPackageIsShipped = packageIsShipped;
        mPackageIsDelivered = packageIsDelivered;
        mPackageNote = packageNote;
    }

    @Override
    public Package mapRow(ResultSet arg0, int arg1) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the dbKeyPackageId
     */
    public static String getDbKeyPackageId()
    {
        return DB_KEY_PACKAGE_ID;
    }

    /**
     * @param dbKeyPackageId
     *            the dbKeyPackageId to set
     */
    public static void setDbKeyPackageId(String dbKeyPackageId)
    {
        DB_KEY_PACKAGE_ID = dbKeyPackageId;
    }

    /**
     * @return the dbKeyPackageProxyName
     */
    public static String getDbKeyPackageProxyName()
    {
        return DB_KEY_PACKAGE_PROXY_NAME;
    }

    /**
     * @param dbKeyPackageProxyName
     *            the dbKeyPackageProxyName to set
     */
    public static void setDbKeyPackageProxyName(String dbKeyPackageProxyName)
    {
        DB_KEY_PACKAGE_PROXY_NAME = dbKeyPackageProxyName;
    }

    /**
     * @return the dbKeyPackageTrackingNumber
     */
    public static String getDbKeyPackageTrackingNumber()
    {
        return DB_KEY_PACKAGE_TRACKING_NUMBER;
    }

    /**
     * @param dbKeyPackageTrackingNumber
     *            the dbKeyPackageTrackingNumber to set
     */
    public static void setDbKeyPackageTrackingNumber(String dbKeyPackageTrackingNumber)
    {
        DB_KEY_PACKAGE_TRACKING_NUMBER = dbKeyPackageTrackingNumber;
    }

    /**
     * @return the dbKeyPackageReceiver
     */
    public static String getDbKeyPackageReceiver()
    {
        return DB_KEY_PACKAGE_RECEIVER;
    }

    /**
     * @param dbKeyPackageReceiver
     *            the dbKeyPackageReceiver to set
     */
    public static void setDbKeyPackageReceiver(String dbKeyPackageReceiver)
    {
        DB_KEY_PACKAGE_RECEIVER = dbKeyPackageReceiver;
    }

    /**
     * @return the dbKeyPackageDestination
     */
    public static String getDbKeyPackageDestination()
    {
        return DB_KEY_PACKAGE_DESTINATION;
    }

    /**
     * @param dbKeyPackageDestination
     *            the dbKeyPackageDestination to set
     */
    public static void setDbKeyPackageDestination(String dbKeyPackageDestination)
    {
        DB_KEY_PACKAGE_DESTINATION = dbKeyPackageDestination;
    }

    /**
     * @return the dbKeyPackageDate
     */
    public static String getDbKeyPackageDate()
    {
        return DB_KEY_PACKAGE_DATE;
    }

    /**
     * @param dbKeyPackageDate
     *            the dbKeyPackageDate to set
     */
    public static void setDbKeyPackageDate(String dbKeyPackageDate)
    {
        DB_KEY_PACKAGE_DATE = dbKeyPackageDate;
    }

    /**
     * @return the dbKeyPackageWeight
     */
    public static String getDbKeyPackageWeight()
    {
        return DB_KEY_PACKAGE_WEIGHT;
    }

    /**
     * @param dbKeyPackageWeight
     *            the dbKeyPackageWeight to set
     */
    public static void setDbKeyPackageWeight(String dbKeyPackageWeight)
    {
        DB_KEY_PACKAGE_WEIGHT = dbKeyPackageWeight;
    }

    /**
     * @return the dbKeyPackageShippingFee
     */
    public static String getDbKeyPackageShippingFee()
    {
        return DB_KEY_PACKAGE_SHIPPING_FEE;
    }

    /**
     * @param dbKeyPackageShippingFee
     *            the dbKeyPackageShippingFee to set
     */
    public static void setDbKeyPackageShippingFee(String dbKeyPackageShippingFee)
    {
        DB_KEY_PACKAGE_SHIPPING_FEE = dbKeyPackageShippingFee;
    }

    /**
     * @return the dbKeyPackageAdditionalCustomTaxFee
     */
    public static String getDbKeyPackageAdditionalCustomTaxFee()
    {
        return DB_KEY_PACKAGE_ADDITIONAL_CUSTOM_TAX_FEE;
    }

    /**
     * @param dbKeyPackageAdditionalCustomTaxFee
     *            the dbKeyPackageAdditionalCustomTaxFee to set
     */
    public static void setDbKeyPackageAdditionalCustomTaxFee(
            String dbKeyPackageAdditionalCustomTaxFee)
    {
        DB_KEY_PACKAGE_ADDITIONAL_CUSTOM_TAX_FEE = dbKeyPackageAdditionalCustomTaxFee;
    }

    /**
     * @return the dbKeyPackageAdditionalInsuranceFee
     */
    public static String getDbKeyPackageAdditionalInsuranceFee()
    {
        return DB_KEY_PACKAGE_ADDITIONAL_INSURANCE_FEE;
    }

    /**
     * @param dbKeyPackageAdditionalInsuranceFee
     *            the dbKeyPackageAdditionalInsuranceFee to set
     */
    public static void setDbKeyPackageAdditionalInsuranceFee(
            String dbKeyPackageAdditionalInsuranceFee)
    {
        DB_KEY_PACKAGE_ADDITIONAL_INSURANCE_FEE = dbKeyPackageAdditionalInsuranceFee;
    }

    /**
     * @return the dbKeyPackageIsShipped
     */
    public static String getDbKeyPackageIsShipped()
    {
        return DB_KEY_PACKAGE_IS_SHIPPED;
    }

    /**
     * @param dbKeyPackageIsShipped
     *            the dbKeyPackageIsShipped to set
     */
    public static void setDbKeyPackageIsShipped(String dbKeyPackageIsShipped)
    {
        DB_KEY_PACKAGE_IS_SHIPPED = dbKeyPackageIsShipped;
    }

    /**
     * @return the dbKeyPackageIsDelivered
     */
    public static String getDbKeyPackageIsDelivered()
    {
        return DB_KEY_PACKAGE_IS_DELIVERED;
    }

    /**
     * @param dbKeyPackageIsDelivered
     *            the dbKeyPackageIsDelivered to set
     */
    public static void setDbKeyPackageIsDelivered(String dbKeyPackageIsDelivered)
    {
        DB_KEY_PACKAGE_IS_DELIVERED = dbKeyPackageIsDelivered;
    }

    /**
     * @return the dbKeyPackageNote
     */
    public static String getDbKeyPackageNote()
    {
        return DB_KEY_PACKAGE_NOTE;
    }

    /**
     * @param dbKeyPackageNote
     *            the dbKeyPackageNote to set
     */
    public static void setDbKeyPackageNote(String dbKeyPackageNote)
    {
        DB_KEY_PACKAGE_NOTE = dbKeyPackageNote;
    }

    /**
     * @return the packageId
     */
    public String getPackageId()
    {
        return mPackageId;
    }

    /**
     * @param packageId
     *            the packageId to set
     */
    public void setPackageId(String packageId)
    {
        mPackageId = packageId;
    }

    /**
     * @return the packgeProxyName
     */
    public String getPackgeProxyName()
    {
        return mPackgeProxyName;
    }

    /**
     * @param packgeProxyName
     *            the packgeProxyName to set
     */
    public void setPackgeProxyName(String packgeProxyName)
    {
        mPackgeProxyName = packgeProxyName;
    }

    /**
     * @return the packageTrackingNumber
     */
    public String getPackageTrackingNumber()
    {
        return mPackageTrackingNumber;
    }

    /**
     * @param packageTrackingNumber
     *            the packageTrackingNumber to set
     */
    public void setPackageTrackingNumber(String packageTrackingNumber)
    {
        mPackageTrackingNumber = packageTrackingNumber;
    }

    /**
     * @return the packageReceiver
     */
    public String getPackageReceiver()
    {
        return mPackageReceiver;
    }

    /**
     * @param packageReceiver
     *            the packageReceiver to set
     */
    public void setPackageReceiver(String packageReceiver)
    {
        mPackageReceiver = packageReceiver;
    }

    /**
     * @return the packageDestination
     */
    public String getPackageDestination()
    {
        return mPackageDestination;
    }

    /**
     * @param packageDestination
     *            the packageDestination to set
     */
    public void setPackageDestination(String packageDestination)
    {
        mPackageDestination = packageDestination;
    }

    /**
     * @return the packageDate
     */
    public String getPackageDate()
    {
        return mPackageDate;
    }

    /**
     * @param packageDate
     *            the packageDate to set
     */
    public void setPackageDate(String packageDate)
    {
        mPackageDate = packageDate;
    }

    /**
     * @return the packageWeight
     */
    public int getPackageWeight()
    {
        return mPackageWeight;
    }

    /**
     * @param packageWeight
     *            the packageWeight to set
     */
    public void setPackageWeight(int packageWeight)
    {
        mPackageWeight = packageWeight;
    }

    /**
     * @return the packgeShippingFee
     */
    public int getPackgeShippingFee()
    {
        return mPackgeShippingFee;
    }

    /**
     * @param packgeShippingFee
     *            the packgeShippingFee to set
     */
    public void setPackgeShippingFee(int packgeShippingFee)
    {
        mPackgeShippingFee = packgeShippingFee;
    }

    /**
     * @return the packageAdditionalCustomTaxFee
     */
    public int getPackageAdditionalCustomTaxFee()
    {
        return mPackageAdditionalCustomTaxFee;
    }

    /**
     * @param packageAdditionalCustomTaxFee
     *            the packageAdditionalCustomTaxFee to set
     */
    public void setPackageAdditionalCustomTaxFee(int packageAdditionalCustomTaxFee)
    {
        mPackageAdditionalCustomTaxFee = packageAdditionalCustomTaxFee;
    }

    /**
     * @return the packageAdditionalInsuranceFee
     */
    public int getPackageAdditionalInsuranceFee()
    {
        return mPackageAdditionalInsuranceFee;
    }

    /**
     * @param packageAdditionalInsuranceFee
     *            the packageAdditionalInsuranceFee to set
     */
    public void setPackageAdditionalInsuranceFee(int packageAdditionalInsuranceFee)
    {
        mPackageAdditionalInsuranceFee = packageAdditionalInsuranceFee;
    }

    /**
     * @return the packageIsShipped
     */
    public boolean isPackageIsShipped()
    {
        return mPackageIsShipped;
    }

    /**
     * @param packageIsShipped
     *            the packageIsShipped to set
     */
    public void setPackageIsShipped(boolean packageIsShipped)
    {
        mPackageIsShipped = packageIsShipped;
    }

    /**
     * @return the packageIsDelivered
     */
    public boolean isPackageIsDelivered()
    {
        return mPackageIsDelivered;
    }

    /**
     * @param packageIsDelivered
     *            the packageIsDelivered to set
     */
    public void setPackageIsDelivered(boolean packageIsDelivered)
    {
        mPackageIsDelivered = packageIsDelivered;
    }

    /**
     * @return the packageNote
     */
    public String getPackageNote()
    {
        return mPackageNote;
    }

    /**
     * @param packageNote
     *            the packageNote to set
     */
    public void setPackageNote(String packageNote)
    {
        mPackageNote = packageNote;
    }

}
