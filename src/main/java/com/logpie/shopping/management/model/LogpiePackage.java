// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author zhoyilei
 *
 */
public class LogpiePackage implements RowMapper<LogpiePackage>, LogpieModel
{
    public static String DB_KEY_PACKAGE_ID = "PackageId";
    public static String DB_KEY_PACKAGE_PROXY_NAME = "PackageProxyName";
    public static String DB_KEY_PACKAGE_TRACKING_NUMBER = "PackageTrackingNumber";
    public static String DB_KEY_PACKAGE_RECEIVER = "PackageReceiver";
    public static String DB_KEY_PACKAGE_DESTINATION = "PackageDestination";
    public static String DB_KEY_PACKAGE_DATE = "PackageDate";
    public static String DB_KEY_PACKAGE_WEIGHT = "PackageWeight";
    public static String DB_KEY_PACKAGE_SHIPPING_FEE = "PackageShippingFee";
    public static String DB_KEY_PACKAGE_ADDITIONAL_CUSTOM_TAX_FEE = "PackageAdditionalCustomTaxFee";
    public static String DB_KEY_PACKAGE_ADDITIONAL_INSURANCE_FEE = "PackageAdditionalInsuranceFee";
    public static String DB_KEY_PACKAGE_IS_SHIPPED = "PackageIsShipped";
    public static String DB_KEY_PACKAGE_IS_DELIVERED = "PackageIsDelivered";
    public static String DB_KEY_PACKAGE_NOTE = "PackageNote";

    private String mPackageId;
    private String mPackageProxyName;
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

    private static final Logger LOG = Logger.getLogger(LogpiePackage.class);

    // For RowMapper
    public LogpiePackage()
    {

    }

    /**
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
    public LogpiePackage(String packageProxyName, String packageTrackingNumber,
            String packageReceiver, String packageDestination, Integer packageWeight,
            Integer packgeShippingFee, Integer packageAdditionalCustomTaxFee,
            Integer packageAdditionalInsuranceFee, Boolean packageIsShipped,
            Boolean packageIsDelivered, String packageNote)
    {
        mPackageProxyName = packageProxyName;
        mPackageTrackingNumber = packageTrackingNumber;
        mPackageReceiver = packageReceiver;
        mPackageDestination = packageDestination;
        // PackageDate is automated to current time
        // mPackageDate = packageDate;
        mPackageWeight = packageWeight;
        mPackgeShippingFee = packgeShippingFee;
        mPackageAdditionalCustomTaxFee = packageAdditionalCustomTaxFee;
        mPackageAdditionalInsuranceFee = packageAdditionalInsuranceFee;
        mPackageIsShipped = packageIsShipped;
        mPackageIsDelivered = packageIsDelivered;
        mPackageNote = packageNote;
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
            Integer packageWeight, Integer packgeShippingFee, Integer packageAdditionalCustomTaxFee,
            Integer packageAdditionalInsuranceFee, Boolean packageIsShipped,
            Boolean packageIsDelivered, String packageNote)
    {
        mPackageId = packageId;
        mPackageProxyName = packageProxyName;
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
        if (packageId == null)
        {
            return null;
        }
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

    @Override
    public Map<String, Object> getModelMap()
    {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put(LogpiePackage.DB_KEY_PACKAGE_PROXY_NAME, mPackageProxyName);
        modelMap.put(LogpiePackage.DB_KEY_PACKAGE_TRACKING_NUMBER, mPackageTrackingNumber);
        modelMap.put(LogpiePackage.DB_KEY_PACKAGE_RECEIVER, mPackageReceiver);
        modelMap.put(LogpiePackage.DB_KEY_PACKAGE_DESTINATION, mPackageDestination);
        modelMap.put(LogpiePackage.DB_KEY_PACKAGE_DATE, mPackageDate);
        modelMap.put(LogpiePackage.DB_KEY_PACKAGE_WEIGHT, mPackageWeight);
        modelMap.put(LogpiePackage.DB_KEY_PACKAGE_SHIPPING_FEE, mPackgeShippingFee);
        modelMap.put(LogpiePackage.DB_KEY_PACKAGE_ADDITIONAL_CUSTOM_TAX_FEE,
                mPackageAdditionalCustomTaxFee);
        modelMap.put(LogpiePackage.DB_KEY_PACKAGE_ADDITIONAL_INSURANCE_FEE,
                mPackageAdditionalInsuranceFee);
        modelMap.put(LogpiePackage.DB_KEY_PACKAGE_IS_SHIPPED, mPackageIsShipped);
        modelMap.put(LogpiePackage.DB_KEY_PACKAGE_IS_DELIVERED, mPackageIsDelivered);
        modelMap.put(LogpiePackage.DB_KEY_PACKAGE_NOTE, mPackageNote);
        return modelMap;
    }

    public static LogpiePackage readNewLogpiePackageFromRequest(final HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }
        final String packageProxyName = request.getParameter("PackageProxyName");
        final String packageTrackingNumber = request.getParameter("PackageTrackingNumber");
        final String packageReceiver = request.getParameter("PackageReceiver");
        final String packageDestination = request.getParameter("PackageDestination");
        // PackageDate is automated to current time
        // final String packageDate = request.getParameter("PackageDate");
        final Integer packageWeight = Integer.parseInt(request.getParameter("PackageWeight"));
        final Integer packgeShippingFee = Integer
                .parseInt(request.getParameter("PackgeShippingFee"));
        final Integer packageAdditionalCustomTaxFee = Integer
                .parseInt(request.getParameter("PackageAdditionalCustomTaxFee"));
        final Integer packageAdditionalInsuranceFee = Integer
                .parseInt(request.getParameter("PackageAdditionalInsuranceFee"));
        final Boolean packageIsShipped = Boolean
                .parseBoolean(request.getParameter("PackageIsShipped"));
        final Boolean packageIsDelivered = Boolean
                .parseBoolean(request.getParameter("PackageIsDelivered"));
        final String packageNote = request.getParameter("PackageNote");
        return new LogpiePackage(packageProxyName, packageTrackingNumber, packageReceiver,
                packageDestination, packageWeight, packgeShippingFee, packageAdditionalCustomTaxFee,
                packageAdditionalInsuranceFee, packageIsShipped, packageIsDelivered, packageNote);
    }

    public static LogpiePackage readModifiedLogpiePackageFromRequest(
            final HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }
        final String packageId = request.getParameter("PackageId");
        final String packageProxyName = request.getParameter("PackageProxyName");
        final String packageTrackingNumber = request.getParameter("PackageTrackingNumber");
        final String packageReceiver = request.getParameter("PackageReceiver");
        final String packageDestination = request.getParameter("PackageDestination");
        final String packageDate = request.getParameter("PackageDate");
        final Integer packageWeight = Integer.parseInt(request.getParameter("PackageWeight"));
        final Integer packgeShippingFee = Integer
                .parseInt(request.getParameter("PackgeShippingFee"));
        final Integer packageAdditionalCustomTaxFee = Integer
                .parseInt(request.getParameter("PackageAdditionalCustomTaxFee"));
        final Integer packageAdditionalInsuranceFee = Integer
                .parseInt(request.getParameter("PackageAdditionalInsuranceFee"));
        final Boolean packageIsShipped = Boolean
                .parseBoolean(request.getParameter("PackageIsShipped"));
        final Boolean packageIsDelivered = Boolean
                .parseBoolean(request.getParameter("PackageIsDelivered"));
        final String packageNote = request.getParameter("PackageNote");
        return new LogpiePackage(packageId, packageProxyName, packageTrackingNumber,
                packageReceiver, packageDestination, packageDate, packageWeight, packgeShippingFee,
                packageAdditionalCustomTaxFee, packageAdditionalInsuranceFee, packageIsShipped,
                packageIsDelivered, packageNote);
    }

    public String getDeltaChange(final LogpiePackage compareToLogpiePackage)
    {
        if (compareToLogpiePackage == null
                || !compareToLogpiePackage.getPackageId().equals(mPackageId))
        {
            return null;
        }

        if (compareTo(compareToLogpiePackage))
        {
            // Package doesn't change.
            return null;
        }
        final StringBuilder changeStringBuilder = new StringBuilder();

        changeStringBuilder.append("改动包裹:" + mPackageId + " ");

        if (!compareToLogpiePackage.mPackageIsDelivered.equals(mPackageIsDelivered))
        {
            changeStringBuilder.append("包裹是否已送达：" + compareToLogpiePackage.mPackageIsDelivered
                    + "->" + mPackageIsDelivered);
        }
        if (!compareToLogpiePackage.mPackageIsShipped.equals(mPackageIsShipped))
        {
            changeStringBuilder.append("包裹是否基础：" + compareToLogpiePackage.mPackageIsShipped + "->"
                    + mPackageIsShipped);
        }
        if (!compareToLogpiePackage.mPackageAdditionalCustomTaxFee
                .equals(mPackageAdditionalCustomTaxFee))
        {
            changeStringBuilder
                    .append("包裹额外的海关费用：" + compareToLogpiePackage.mPackageAdditionalCustomTaxFee
                            + "->" + mPackageAdditionalCustomTaxFee);
        }
        if (!compareToLogpiePackage.mPackageAdditionalInsuranceFee
                .equals(mPackageAdditionalInsuranceFee))
        {
            changeStringBuilder
                    .append("包裹额外的保险费：" + compareToLogpiePackage.mPackageAdditionalInsuranceFee
                            + "->" + mPackageAdditionalInsuranceFee);
        }
        if (!compareToLogpiePackage.mPackageDate.equals(mPackageDate))
        {
            changeStringBuilder
                    .append("包裹日期：" + compareToLogpiePackage.mPackageDate + "->" + mPackageDate);
        }
        if (!compareToLogpiePackage.mPackageDestination.equals(mPackageDestination))
        {
            changeStringBuilder.append("包裹目的地：" + compareToLogpiePackage.mPackageDestination + "->"
                    + mPackageDestination);
        }
        if (!compareToLogpiePackage.mPackageNote.equals(mPackageNote))
        {
            changeStringBuilder
                    .append("包裹备注：" + compareToLogpiePackage.mPackageNote + "->" + mPackageNote);
        }
        if (!compareToLogpiePackage.mPackageProxyName.equals(mPackageProxyName))
        {
            changeStringBuilder.append("包裹物流代理：" + compareToLogpiePackage.mPackageProxyName + "->"
                    + mPackageProxyName);
        }
        if (!compareToLogpiePackage.mPackageReceiver.equals(mPackageReceiver))
        {
            changeStringBuilder.append(
                    "包裹收件人：" + compareToLogpiePackage.mPackageReceiver + "->" + mPackageReceiver);
        }
        if (!compareToLogpiePackage.mPackageTrackingNumber.equals(mPackageTrackingNumber))
        {
            changeStringBuilder.append("包裹追踪号：" + compareToLogpiePackage.mPackageTrackingNumber
                    + "->" + mPackageTrackingNumber);
        }
        if (!compareToLogpiePackage.mPackageWeight.equals(mPackageWeight))
        {
            changeStringBuilder.append(
                    "包裹重量：" + compareToLogpiePackage.mPackageWeight + "->" + mPackageWeight);
        }
        if (!compareToLogpiePackage.mPackgeShippingFee.equals(mPackgeShippingFee))
        {
            changeStringBuilder.append("包裹运费：" + compareToLogpiePackage.mPackgeShippingFee + "->"
                    + mPackgeShippingFee);
        }

        return changeStringBuilder.toString();
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
     * @return the packageProxyName
     */
    public String getPackageProxyName()
    {
        return mPackageProxyName;
    }

    /**
     * @param packageProxyName
     *            the packageProxyName to set
     */
    public void setPackageProxyName(String packageProxyName)
    {
        mPackageProxyName = packageProxyName;
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

    @Override
    public String getPrimaryKey()
    {
        return DB_KEY_PACKAGE_ID;
    }

    @Override
    public boolean compareTo(Object object)
    {
        if (object instanceof LogpiePackage)
        {
            final LogpiePackage compareToLogpiePackage = (LogpiePackage) object;
            if (compareToLogpiePackage.mPackageId.equals(mPackageId)
                    && compareToLogpiePackage.mPackageIsDelivered.equals(mPackageIsDelivered)
                    && compareToLogpiePackage.mPackageIsShipped.equals(mPackageIsShipped)
                    && compareToLogpiePackage.mPackageAdditionalCustomTaxFee
                            .equals(mPackageAdditionalCustomTaxFee)
                    && compareToLogpiePackage.mPackageAdditionalInsuranceFee
                            .equals(mPackageAdditionalInsuranceFee)
                    && compareToLogpiePackage.mPackageDate.equals(mPackageDate)
                    && compareToLogpiePackage.mPackageDestination.equals(mPackageDestination)
                    && compareToLogpiePackage.mPackageNote.equals(mPackageNote)
                    && compareToLogpiePackage.mPackageProxyName.equals(mPackageProxyName)
                    && compareToLogpiePackage.mPackageReceiver.equals(mPackageReceiver)
                    && compareToLogpiePackage.mPackageTrackingNumber.equals(mPackageTrackingNumber)
                    && compareToLogpiePackage.mPackageWeight.equals(mPackageWeight)
                    && compareToLogpiePackage.mPackgeShippingFee.equals(mPackgeShippingFee))
            {
                return true;
            }
        }
        return false;
    }

    public JSONObject getJSON()
    {
        final JSONObject packageJSON = new JSONObject();
        try
        {
            packageJSON.put(DB_KEY_PACKAGE_ID, this.mPackageId);
            packageJSON.put(DB_KEY_PACKAGE_PROXY_NAME, this.mPackageProxyName);
            packageJSON.put(DB_KEY_PACKAGE_TRACKING_NUMBER, this.mPackageTrackingNumber);
            packageJSON.put(DB_KEY_PACKAGE_RECEIVER, this.mPackageReceiver);
            packageJSON.put(DB_KEY_PACKAGE_DESTINATION, this.mPackageDestination);
            packageJSON.put(DB_KEY_PACKAGE_DATE, this.mPackageDate);
            packageJSON.put(DB_KEY_PACKAGE_WEIGHT, String.valueOf(this.mPackageWeight));
            packageJSON.put(DB_KEY_PACKAGE_SHIPPING_FEE, String.valueOf(this.mPackgeShippingFee));
            packageJSON.put(DB_KEY_PACKAGE_ADDITIONAL_CUSTOM_TAX_FEE,
                    String.valueOf(this.mPackageAdditionalCustomTaxFee));
            packageJSON.put(DB_KEY_PACKAGE_ADDITIONAL_INSURANCE_FEE,
                    String.valueOf(this.mPackageAdditionalInsuranceFee));
            packageJSON.put(DB_KEY_PACKAGE_IS_SHIPPED, String.valueOf(this.mPackageIsShipped));
            packageJSON.put(DB_KEY_PACKAGE_IS_DELIVERED, String.valueOf(this.mPackageIsDelivered));
            packageJSON.put(DB_KEY_PACKAGE_NOTE, this.mPackageNote);

            // 包裹显示在订单管理界面的简介信息
            // 包裹${order.orderPackage.packageId}
            // ${order.orderPackage.packageReceiver}
            // ${order.orderPackage.packageProxyName}
            // ${fn:substring(order.orderPackage.packageDate,5,10)}
            // ${order.orderPackage.packageTrackingNumber}
            packageJSON.put("PackageDescription",
                    String.format("包裹%s %s %s %s %s", mPackageId, mPackageReceiver,
                            mPackageProxyName, mPackageDate.substring(5, 10),
                            mPackageTrackingNumber));

        } catch (JSONException e)
        {
            LOG.error("JSONException when building package json", e);
        }

        return packageJSON;

    }
}
