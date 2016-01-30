// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * @author zhoyilei
 *
 */
public class SettleDownRecord implements RowMapper<SettleDownRecord>, LogpieModel
{
    private static final Logger LOG = Logger.getLogger(SettleDownRecord.class);

    public static final String DB_KEY_SETTLE_DOWN_RECORD_ID = "SettleDownRecordId";
    public static final String DB_KEY_SETTLE_DOWN_RECORD_ADMIN = "SettleDownRecordAdmin";
    public static final String DB_KEY_SETTLE_DOWN_RECORD_DATE = "SettleDownRecordDate";
    public static final String DB_KEY_SETTLE_DOWN_RECORD_INFO = "SettleDownRecordInfo";

    private String mSettleDownRecordId;
    private Admin mSettleDownRecordAdmin;
    private String mSettleDownRecordDate;
    private String mSettleDownRecordInfo;
    private String mSettleDownRecordInfoReadable;

    /**
     * @return the settleDownRecordInfoReadable
     */
    public String getSettleDownRecordInfoReadable()
    {
        return mSettleDownRecordInfoReadable;
    }

    /**
     * @param settleDownRecordInfoReadable
     *            the settleDownRecordInfoReadable to set
     */
    public void setSettleDownRecordInfoReadable(String settleDownRecordInfoReadable)
    {
        mSettleDownRecordInfoReadable = settleDownRecordInfoReadable;
    }

    public SettleDownRecord()
    {

    }

    /**
     * @param settleDownRecordId
     * @param settleDownRecordDate
     * @param settleDownRecordInfo
     */
    public SettleDownRecord(String settleDownRecordId, Admin settleDownRecordAdmin,
            String settleDownRecordDate, String settleDownRecordInfo)
    {
        super();
        mSettleDownRecordId = settleDownRecordId;
        mSettleDownRecordAdmin = settleDownRecordAdmin;
        mSettleDownRecordDate = settleDownRecordDate;
        mSettleDownRecordInfo = settleDownRecordInfo;

        JSONObject settleDownRecordInfoJSON;
        try
        {
            settleDownRecordInfoJSON = new JSONObject(settleDownRecordInfo);
            final StringBuilder settleDownRecordInfoBuilder = new StringBuilder();
            settleDownRecordInfoBuilder.append("本次结算订单号：");
            settleDownRecordInfoBuilder
                    .append(settleDownRecordInfoJSON.getString("settleDownOrders"));
            settleDownRecordInfoBuilder.append(". 代理已付公司:");
            settleDownRecordInfoBuilder
                    .append(settleDownRecordInfoJSON.getString("proxyOweCompanyMoney"));
            settleDownRecordInfoBuilder.append(". 公司利润:");
            settleDownRecordInfoBuilder.append(settleDownRecordInfoJSON.getString("companyProfit"));
            settleDownRecordInfoBuilder.append(". 代理利润:");
            settleDownRecordInfoBuilder.append(settleDownRecordInfoJSON.getString("proxyProfit"));
            this.mSettleDownRecordInfoReadable = settleDownRecordInfoBuilder.toString();
        } catch (JSONException e)
        {
            LOG.error("JSONException when building readable settle down record");
            this.mSettleDownRecordInfoReadable = "JSONException when building readable settle down record";
        }

    }

    /**
     * For creating a new settle down record
     * 
     * @param settleDownRecordInfo
     */
    public SettleDownRecord(String settleDownRecordInfo, Admin settleDownRecordAdmin)
    {
        super();
        mSettleDownRecordId = null;
        mSettleDownRecordDate = null;
        mSettleDownRecordAdmin = settleDownRecordAdmin;
        mSettleDownRecordInfo = settleDownRecordInfo;
    }

    @Override
    public Map<String, Object> getModelMap()
    {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put(SettleDownRecord.DB_KEY_SETTLE_DOWN_RECORD_INFO, mSettleDownRecordInfo);
        modelMap.put(SettleDownRecord.DB_KEY_SETTLE_DOWN_RECORD_ADMIN,
                mSettleDownRecordAdmin.getAdminId());
        return modelMap;
    }

    @Override
    public String getPrimaryKey()
    {
        return DB_KEY_SETTLE_DOWN_RECORD_ID;
    }

    @Override
    public boolean compareTo(Object object)
    {
        if (object instanceof SettleDownRecord)
        {
            final SettleDownRecord compareToSettleDownRecord = (SettleDownRecord) object;
            if (compareToSettleDownRecord.mSettleDownRecordId.equals(mSettleDownRecordId)
                    && compareToSettleDownRecord.mSettleDownRecordAdmin
                            .compareTo(mSettleDownRecordAdmin)
                    && compareToSettleDownRecord.mSettleDownRecordDate.equals(mSettleDownRecordDate)
                    && compareToSettleDownRecord.mSettleDownRecordInfo
                            .equals(mSettleDownRecordInfo))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public SettleDownRecord mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        return getSettleDownRecordByResultSet(rs, rowNum);
    }

    public static final SettleDownRecord getSettleDownRecordByResultSet(ResultSet rs, int rowNum)
            throws SQLException
    {
        if (rs == null)
        {
            return null;
        }
        final String settleDownRecordId = String.valueOf(rs.getInt(DB_KEY_SETTLE_DOWN_RECORD_ID));
        final Admin settleDownRecordAdmin = Admin.getAdminByResultSet(rs, rowNum);
        final Date settleDownRecordDate = rs.getTimestamp(DB_KEY_SETTLE_DOWN_RECORD_DATE);
        final String settleDownRecordDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(settleDownRecordDate);
        final String settleDownRecordInfo = rs.getString(DB_KEY_SETTLE_DOWN_RECORD_INFO);
        String settleDownRecordInfoJSONString = null;
        try
        {
            settleDownRecordInfoJSONString = new String(Base64.decode(settleDownRecordInfo),
                    "UTF-8");
        } catch (UnsupportedEncodingException | Base64DecodingException e)
        {
            LOG.error("Exception happens when trying to build settle down record info json string.",
                    e);
            settleDownRecordInfoJSONString = "Error when building settle down record info";
        }
        return new SettleDownRecord(settleDownRecordId, settleDownRecordAdmin,
                settleDownRecordDateString, settleDownRecordInfoJSONString);
    }

    /**
     * @return the settleDownRecordId
     */
    public String getSettleDownRecordId()
    {
        return mSettleDownRecordId;
    }

    /**
     * @param settleDownRecordId
     *            the settleDownRecordId to set
     */
    public void setSettleDownRecordId(String settleDownRecordId)
    {
        mSettleDownRecordId = settleDownRecordId;
    }

    /**
     * @return the settleDownRecordDate
     */
    public String getSettleDownRecordDate()
    {
        return mSettleDownRecordDate;
    }

    /**
     * @param settleDownRecordDate
     *            the settleDownRecordDate to set
     */
    public void setSettleDownRecordDate(String settleDownRecordDate)
    {
        mSettleDownRecordDate = settleDownRecordDate;
    }

    /**
     * @return the settleDownRecordInfo
     */
    public String getSettleDownRecordInfo()
    {
        return mSettleDownRecordInfo;
    }

    /**
     * @param settleDownRecordInfo
     *            the settleDownRecordInfo to set
     */
    public void setSettleDownRecordInfo(String settleDownRecordInfo)
    {
        mSettleDownRecordInfo = settleDownRecordInfo;
    }

    /**
     * @return the settleDownRecordAdmin
     */
    public Admin getSettleDownRecordAdmin()
    {
        return mSettleDownRecordAdmin;
    }

    /**
     * @param settleDownRecordAdmin
     *            the settleDownRecordAdmin to set
     */
    public void setSettleDownRecordAdmin(Admin settleDownRecordAdmin)
    {
        mSettleDownRecordAdmin = settleDownRecordAdmin;
    }

}
