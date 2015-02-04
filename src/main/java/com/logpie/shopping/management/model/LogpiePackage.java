// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author zhoyilei
 *
 */
public class LogpiePackage implements RowMapper<LogpiePackage>
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
    private Integer mPackageWeight;
    private Integer mPackgeShippingFee;
    private Integer mPackageAdditionalCustomTaxFee;
    private Integer mPackageAdditionalInsuranceFee;
    private Boolean mPackageIsShipped;
    private Boolean mPackageIsDelivered;
    private String mPackageNote;

    // For RowMapper
    public LogpiePackage()
    {

    }

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
    public LogpiePackage(String packageId, String packageProxyName, String packageTrackingNumber,
            String packageReceiver, String packageDestination, String packageDate,
            Integer packageWeight, Integer packgeShippingFee,
            Integer packageAdditionalCustomTaxFee, Integer packageAdditionalInsuranceFee,
            Boolean packageIsShipped, Boolean packageIsDelivered, String packageNote)
    {
        super();
        mPackageId = packageId;
        mPackgeProxyName = packageProxyName;
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
    public LogpiePackage mapRow(final ResultSet rs, final int row) throws SQLException
    {
        // TODO Auto-generated method stub
        return getLogpiePackageByResultSet(rs, row);
    }

    public static LogpiePackage getLogpiePackageByResultSet(final ResultSet rs, final int row)
            throws SQLException
    {
        if (rs == null)
        {
            return null;
        }

        final String packageId = rs.getString(DB_KEY_PACKAGE_ID);
        final String packageProxyName = rs.getString(DB_KEY_PACKAGE_PROXY_NAME);
        final String packageTrackingNumber = rs.getString(DB_KEY_PACKAGE_TRACKING_NUMBER);
        final String packageReceiver = rs.getString(DB_KEY_PACKAGE_RECEIVER);
        final String packageDestination = rs.getString(DB_KEY_PACKAGE_DESTINATION);
        final Date packageDate = rs.getTimestamp(DB_KEY_PACKAGE_DATE);
        final String packageDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(packageDate);
        final Integer packageWeight = rs.getInt(DB_KEY_PACKAGE_WEIGHT);
        final Integer packgeShippingFee = rs.getInt(DB_KEY_PACKAGE_SHIPPING_FEE);
        final Integer packageAdditionalCustomTaxFee = rs
                .getInt(DB_KEY_PACKAGE_ADDITIONAL_CUSTOM_TAX_FEE);
        final Integer packageAdditionalInsuranceFee = rs
                .getInt(DB_KEY_PACKAGE_ADDITIONAL_INSURANCE_FEE);
        final Boolean packageIsShipped = rs.getBoolean(DB_KEY_PACKAGE_IS_SHIPPED);
        final Boolean packageIsDelivered = rs.getBoolean(DB_KEY_PACKAGE_IS_DELIVERED);
        final String packageNote = rs.getString(DB_KEY_PACKAGE_NOTE);

        return new LogpiePackage(packageId, packageProxyName, packageTrackingNumber,
                packageReceiver, packageDestination, packageDateString, packageWeight,
                packgeShippingFee, packageAdditionalCustomTaxFee, packageAdditionalInsuranceFee,
                packageIsShipped, packageIsDelivered, packageNote);
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
    public Integer getPackageWeight()
    {
        return mPackageWeight;
    }

    /**
     * @param packageWeight
     *            the packageWeight to set
     */
    public void setPackageWeight(Integer packageWeight)
    {
        mPackageWeight = packageWeight;
    }

    /**
     * @return the packgeShippingFee
     */
    public Integer getPackgeShippingFee()
    {
        return mPackgeShippingFee;
    }

    /**
     * @param packgeShippingFee
     *            the packgeShippingFee to set
     */
    public void setPackgeShippingFee(Integer packgeShippingFee)
    {
        mPackgeShippingFee = packgeShippingFee;
    }

    /**
     * @return the packageAdditionalCustomTaxFee
     */
    public Integer getPackageAdditionalCustomTaxFee()
    {
        return mPackageAdditionalCustomTaxFee;
    }

    /**
     * @param packageAdditionalCustomTaxFee
     *            the packageAdditionalCustomTaxFee to set
     */
    public void setPackageAdditionalCustomTaxFee(Integer packageAdditionalCustomTaxFee)
    {
        mPackageAdditionalCustomTaxFee = packageAdditionalCustomTaxFee;
    }

    /**
     * @return the packageAdditionalInsuranceFee
     */
    public Integer getPackageAdditionalInsuranceFee()
    {
        return mPackageAdditionalInsuranceFee;
    }

    /**
     * @param packageAdditionalInsuranceFee
     *            the packageAdditionalInsuranceFee to set
     */
    public void setPackageAdditionalInsuranceFee(Integer packageAdditionalInsuranceFee)
    {
        mPackageAdditionalInsuranceFee = packageAdditionalInsuranceFee;
    }

    /**
     * @return the packageIsShipped
     */
    public Boolean getPackageIsShipped()
    {
        return mPackageIsShipped;
    }

    /**
     * @param packageIsShipped
     *            the packageIsShipped to set
     */
    public void setPackageIsShipped(Boolean packageIsShipped)
    {
        mPackageIsShipped = packageIsShipped;
    }

    /**
     * @return the packageIsDelivered
     */
    public Boolean getPackageIsDelivered()
    {
        return mPackageIsDelivered;
    }

    /**
     * @param packageIsDelivered
     *            the packageIsDelivered to set
     */
    public void setPackageIsDelivered(Boolean packageIsDelivered)
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