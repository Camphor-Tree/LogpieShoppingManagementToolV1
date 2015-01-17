// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.auth.logic;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author zhoyilei This class is the model class for Admin table
 */
public class Admin implements RowMapper<Admin> {
	public static final String DB_KEY_ADMIN_ID = "AdminId";
	public static final String DB_KEY_ADMIN_NAME = "AdminName";
	public static final String DB_KEY_ADMIN_PASSWORD = "AdminPassword";
	public static final String DB_KEY_ADMIN_EMAIL = "AdminEmail";
	public static final String DB_KEY_ADMIN_QQ = "AdminQQ";
	public static final String DB_KEY_ADMIN_WECHAT = "AdminWechat";
	public static final String DB_KEY_ADMIN_PHONE = "AdminIdPhone";
	public static final String DB_KEY_ADMIN_IDENTITY_NUMBER = "AdminIdentityNumber";
	public static final String DB_KEY_ADMIN_PASS_VERSION = "AdminPassVersion";

	private final String mAdminId;
	private final String mAdminName;
	private final String mAdminEmail;
	private final String mAdminQQ;
	private final String mAdminWechat;
	private final String mAdminPhone;
	// Chinese Id number or passport number
	private final String mAdminIdentityNumber;
	// This is the control bit to make sure changing the password can invalidate
	// the cookie
	private final String mAdminPassVersion;

	public Admin(final String adminId, final String name, final String email,
			final String qq, final String wechat, final String phone,
			final String identityNumber, final String passVersion) {
		mAdminId = adminId;
		mAdminName = name;
		mAdminEmail = email;
		mAdminQQ = qq;
		mAdminWechat = wechat;
		mAdminPhone = phone;
		mAdminIdentityNumber = identityNumber;
		mAdminPassVersion = passVersion;
	}

	/**
	 * @return the adminId
	 */
	public String getAdminId() {
		return mAdminId;
	}

	/**
	 * @return the adminName
	 */
	public String getAdminName() {
		return mAdminName;
	}

	/**
	 * @return the adminEmail
	 */
	public String getAdminEmail() {
		return mAdminEmail;
	}

	/**
	 * @return the adminQQ
	 */
	public String getAdminQQ() {
		return mAdminQQ;
	}

	/**
	 * @return the adminWechat
	 */
	public String getAdminWechat() {
		return mAdminWechat;
	}

	/**
	 * @return the adminPhone
	 */
	public String getAdminPhone() {
		return mAdminPhone;
	}

	/**
	 * @return the adminIdentityNumber
	 */
	public String getAdminIdentityNumber() {
		return mAdminIdentityNumber;
	}

	/**
	 * @return the adminIdentityNumber
	 */
	public String getPassVersion() {
		return mAdminPassVersion;
	}

	/*
	 * Map the database row into Admin object
	 * 
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
	 * int)
	 */
	@Override
	public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
		if (rs == null || rowNum == 0) {
			return null;
		}
		final String adminId = rs.getString(DB_KEY_ADMIN_ID);
		final String adminName = rs.getString(DB_KEY_ADMIN_NAME);
		final String adminEmail = rs.getString(DB_KEY_ADMIN_EMAIL);
		final String adminQQ = rs.getString(DB_KEY_ADMIN_QQ);
		final String adminWechat = rs.getString(DB_KEY_ADMIN_WECHAT);
		final String adminPhone = rs.getString(DB_KEY_ADMIN_PHONE);
		final String adminIdentityNumber = rs
				.getString(DB_KEY_ADMIN_IDENTITY_NUMBER);
		final String adminPassVersion = rs.getString(DB_KEY_ADMIN_PASS_VERSION);

		return new Admin(adminId, adminName, adminEmail, adminQQ, adminWechat,
				adminPhone, adminIdentityNumber, adminPassVersion);

	}
}
