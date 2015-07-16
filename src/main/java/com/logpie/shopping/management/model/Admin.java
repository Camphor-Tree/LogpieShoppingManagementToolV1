// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

    public Admin(final String name, final String email, final String qq, final String wechat,
            final String phone, final String identityNumber, final String passVersion,
            final String password)
    {
        mAdminName = name;
        mAdminEmail = email;
        mAdminQQ = qq;
        mAdminWechat = wechat;
        mAdminPhone = phone;
        mAdminIdentityNumber = identityNumber;
        mAdminPassVersion = passVersion;
        mAdminPassword = password;
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

    public static Admin buildSystemSuperAdmin()
    {
        return new Admin("1");
    }

    private Admin(final String adminId)
    {
        mAdminId = adminId;
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

    // TODO refactor the logic better
    public boolean isSuperAdmin()
    {
        if (mAdminId != null && mAdminId.equals("1"))
        {
            return true;
        }
        return false;
    }

    // for jstl to get the value
    public boolean getIsSuperAdmin()
    {
        return isSuperAdmin();
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
        final String adminId = rs.getString(DB_KEY_ADMIN_ID);
        final String adminName = rs.getString(DB_KEY_ADMIN_NAME);
        final String adminEmail = rs.getString(DB_KEY_ADMIN_EMAIL);
        final String adminQQ = rs.getString(DB_KEY_ADMIN_QQ);
        final String adminWechat = rs.getString(DB_KEY_ADMIN_WECHAT);
        final String adminPhone = rs.getString(DB_KEY_ADMIN_PHONE);
        final String adminIdentityNumber = rs.getString(DB_KEY_ADMIN_IDENTITY_NUMBER);
        final String adminPassword = rs.getString(DB_KEY_ADMIN_PASSWORD);
        // final String adminPassVersion =
        // rs.getString(DB_KEY_ADMIN_PASS_VERSION);
        final String adminPassVersion = "1";
        return new Admin(adminId, adminName, adminEmail, adminQQ, adminWechat, adminPhone,
                adminIdentityNumber, adminPassVersion, adminPassword);
    }

    public static Admin readNewAdminFromRequest(final HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }
        final String adminName = request.getParameter("AdminName");
        final String adminEmail = request.getParameter("AdminEmail");
        final String adminQQ = request.getParameter("AdminQQ");
        final String adminWechat = request.getParameter("AdminWechat");
        final String adminPhone = request.getParameter("AdminPhone");
        final String adminIdentityNumber = request.getParameter("AdminIdentityNumber");
        final String adminPassVersion = "1";
        final String adminPassword = request.getParameter("AdminPassword");
        return new Admin(adminName, adminEmail, adminQQ, adminWechat, adminPhone,
                adminIdentityNumber, adminPassVersion, adminPassword);
    }

    public static Admin readModifiedAdminFromRequest(final HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }
        final String adminId = request.getParameter("AdminId");
        final String adminName = request.getParameter("AdminName");
        final String adminEmail = request.getParameter("AdminEmail");
        final String adminQQ = request.getParameter("AdminQQ");
        final String adminWechat = request.getParameter("AdminWechat");
        final String adminPhone = request.getParameter("AdminPhone");
        final String adminIdentityNumber = request.getParameter("AdminIdentityNumber");
        final String adminPassVersion = "1";
        final String adminPassword = request.getParameter("AdminPassword");
        return new Admin(adminId, adminName, adminEmail, adminQQ, adminWechat, adminPhone,
                adminIdentityNumber, adminPassVersion, adminPassword);
    }

    @Override
    public String getPrimaryKey()
    {
        return DB_KEY_ADMIN_ID;
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
     * @return the adminPassword
     */
    public String getAdminPassword()
    {
        return mAdminPassword;
    }

    /**
     * @return the adminIdentityNumber
     */
    public String getPassVersion()
    {
        return mAdminPassVersion;
    }

    @Override
    public boolean compareTo(Object object)
    {
        if (object instanceof Admin)
        {
            final Admin compareToAdmin = (Admin) object;
            if (compareToAdmin.mAdminId.equals(mAdminId)
                    && compareToAdmin.mAdminEmail.equals(mAdminEmail)
                    && compareToAdmin.mAdminIdentityNumber.equals(mAdminIdentityNumber)
                    && compareToAdmin.mAdminName.equals(mAdminName)
                    && compareToAdmin.mAdminPassword.equals(mAdminPassword)
                    && compareToAdmin.mAdminPhone.equals(mAdminPhone)
                    && compareToAdmin.mAdminQQ.equals(mAdminQQ)
                    && compareToAdmin.mAdminWechat.equals(mAdminWechat))
            {
                return true;
            }
        }
        return false;
    }
}
