// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Category implements RowMapper<Category>
{
    public static final String DB_KEY_CATEGORY_ID = "CategoryId";
    public static final String DB_KEY_CATEGORY_NAME = "CategoryName";

    private String mCategoryId;
    private String mCategoryName;

    // For RowMapper
    public Category()
    {
    }

    // For creating a new category
    public Category(String categoryName)
    {
        this.mCategoryName = categoryName;
    }

    public Category(String categoryId, String categoryName)
    {
        this.mCategoryId = categoryId;
        this.mCategoryName = categoryName;
    }

    @Override
    public Category mapRow(final ResultSet rs, final int rowNum) throws SQLException
    {
        return getCategoryByResultSet(rs, rowNum);
    }

    public static Category getCategoryByResultSet(final ResultSet rs, final int row)
            throws SQLException
    {
        if (rs == null)
        {
            return null;
        }
        final String categoryId = rs.getString(DB_KEY_CATEGORY_ID);
        final String categoryName = rs.getString(DB_KEY_CATEGORY_NAME);
        return new Category(categoryId, categoryName);
    }

    public String getCategoryId()
    {
        return mCategoryId;
    }

    public String getCategoryName()
    {
        return mCategoryName;
    }
}
