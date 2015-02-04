// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author zhoyilei This class is the model class for Admin table
 */
public class Admin implements RowMapper<Admin>, LogpieModel
{
    public static final String DB_KEY_ADMIN_ID = "AdminId";
    public static final String DB_KEY_ADMIN_NAME = "AdminName";
    public static final String DB_KEY_ADMIN_PASSWORD = "AdminPassword";
    public static final String DB_KEY_ADMIN_EMAIL = "AdminEmail";
    public static final String DB_KEY_ADMIN_QQ = "AdminQQ";
    public static final String DB_KEY_ADMIN_WECHAT = "AdminWechat";
    public static final String DB_KEY_ADMIN_PHONE = "AdminPhone";
    public static final String DB_KEY_ADMIN_IDENTITY_NUMBER = "AdminIdentityNumber";
    // public static final String DB_KEY_ADMIN_PASS_VERSION =
    // "AdminPassVersion";

    private static final Logger LOG = Logger.getLogger(Admin.class);

    private String mAdminId;
    private String mAdminName;
    private String mAdminEmail;
    private String mAdminQQ;
    private String mAdminWechat;
    private String mAdminPhone;
    // Chinese Id number or passport number
    private String mAdminIdentityNumber;
    // This is the control bit to make sure changing the password can invalidate
    // the cookie
    private String mAdminPassVersion;

    private String mAdminPassword;

    public Admin()
    {

    }

    public Admin(final String adminId, final String name, final String email, final String qq,
            final String wechat, final String phone, final String identityNumber,
            final String passVersion, final String password)
    {
        mAdminId = adminId;
        mAdminName = name;
        mAdminEmail = email;
        mAdminQQ = qq;
        mAdminWechat = wechat;
        mAdminPhone = phone;
        mAdminIdentityNumber = identityNumber;
        mAdminPassVersion = passVersion;
        mAdminPassword = password;
    }

    @Override
    public Map<String, Object> getModelMap()
    {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put(Admin.DB_KEY_ADMIN_NAME, mAdminName);
        modelMap.put(Admin.DB_KEY_ADMIN_EMAIL, mAdminEmail);
        modelMap.put(Admin.DB_KEY_ADMIN_QQ, mAdminQQ);
        modelMap.put(Admin.DB_KEY_ADMIN_WECHAT, mAdminWechat);
        modelMap.put(Admin.DB_KEY_ADMIN_PHONE, mAdminPhone);
        modelMap.put(Admin.DB_KEY_ADMIN_IDENTITY_NUMBER, mAdminIdentityNumber);
        modelMap.put(Admin.DB_KEY_ADMIN_PASSWORD, mAdminPassword);
        return modelMap;
    }

    /**
     * @return the adminId
     */
    public String getAdminId()
    {
        return mAdminId;
    }

    /**
     * @return the adminName
     */
    public String getAdminName()
    {
        return mAdminName;
    }

    /**
     * @return the adminEmail
     */
    public String getAdminEmail()
    {
        return mAdminEmail;
    }

    /**
     * @return the adminQQ
     */
    public String getAdminQQ()
    {
        return mAdminQQ;
    }

    /**
     * @return the adminWechat
     */
    public String getAdminWechat()
    {
        return mAdminWechat;
    }

    /**
     * @return the adminPhone
     */
    public String getAdminPhone()
    {
        return mAdminPhone;
    }

    /**
     * @return the adminIdentityNumber
     */
    public String getAdminIdentityNumber()
    {
        return mAdminIdentityNumber;
    }

    /**
     * @return the adminIdentityNumber
     */
    public String getPassVersion()
    {
        return mAdminPassVersion;
    }

    /*
     * Map the database row into Admin object
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
     * int)
     */
    @Override
    public Admin mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        return getAdminByResultSet(rs, rowNum);
    }

    public static Admin getAdminByResultSet(final ResultSet rs, int row) throws SQLException
    {
        if (rs == null)
        {
            return null;
        }
        LOG.debug("Mapping the result set to admin object");
        final String adminId = rs.getString(DB_KEY_ADMIN_ID);
        final String adminName = rs.getString(DB_KEY_ADMIN_NAME);
        final String adminEmail = rs.getString(DB_KEY_ADMIN_EMAIL);
        final String adminQQ = String.valueOf(rs.getInt(DB_KEY_ADMIN_QQ));
        final String adminWechat = rs.getString(DB_KEY_ADMIN_WECHAT);
        final String adminPhone = String.valueOf(rs.getInt(DB_KEY_ADMIN_PHONE));
        final String adminIdentityNumber = String.valueOf(rs.getInt(DB_KEY_ADMIN_IDENTITY_NUMBER));
        // final String adminPassVersion =
        // rs.getString(DB_KEY_ADMIN_PASS_VERSION);
        LOG.debug("Find values:" + adminId + ":" + adminName);
        final String adminPassVersion = "1";
        return new Admin(adminId, adminName, adminEmail, adminQQ, adminWechat, adminPhone,
                adminIdentityNumber, adminPassVersion, "fakePassword");
    }
}
