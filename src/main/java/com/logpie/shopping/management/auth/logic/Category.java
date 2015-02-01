package com.logpie.shopping.management.auth.logic;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Category implements RowMapper<Category> {
	public static final String DB_KEY_CATEGORY_ID = "";
	public static final String DB_KEY_CATEGORY_URL = "";
	public static final String DB_KEY_CATEGORY_DESCRIPTION = "";

	private final String mCategoryId;
	private final String mCategoryUrl;
	private final String mCategoryDescription;

	public Category(String categoryId, String categoryUrl,
			String categoryDescription) {
		this.mCategoryId = categoryId;
		this.mCategoryUrl = categoryUrl;
		this.mCategoryDescription = categoryDescription;
	}

	public String getCategoryId() {
		return mCategoryId;
	}

	public String getCategoryUrl() {
		return mCategoryUrl;
	}

	public String getCategoryDescription() {
		return mCategoryDescription;
	}

	/*
	 * Map the database row into Category object
	 * 
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
	 * int)
	 */
	@Override
	public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
		if (rs == null || rowNum == 0) {
			return null;
		}
		final String categoryId = rs.getString(DB_KEY_CATEGORY_ID);
		final String categoryUrl = rs.getString(DB_KEY_CATEGORY_URL);
		final String categoryDescription = rs
				.getString(DB_KEY_CATEGORY_DESCRIPTION);

		return new Category(categoryId, categoryUrl, categoryDescription);
	}
}
