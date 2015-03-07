// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.RowMapper;

public class Category implements RowMapper<Category>, LogpieModel
{
    public static final String DB_KEY_CATEGORY_ID = "CategoryId";
    public static final String DB_KEY_CATEGORY_NAME = "CategoryName";

    private String mCategoryId;
    public String mCategoryName;

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

    @Override
    public Map<String, Object> getModelMap()
    {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put(Category.DB_KEY_CATEGORY_NAME, mCategoryName);
        return modelMap;
    }

    public static Category readModifiedCategoryFromRequest(final HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }
        final String categoryId = request.getParameter("CategoryId");
        final String categoryName = request.getParameter("CategoryName");
        return new Category(categoryId, categoryName);
    }

    public static Category readNewCategoryFromRequest(final HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }
        final String categoryName = request.getParameter("CategoryName");
        return new Category(categoryName);
    }

    /**
     * @param categoryId
     *            the categoryId to set
     */
    public void setCategoryId(String categoryId)
    {
        mCategoryId = categoryId;
    }

    /**
     * @param categoryName
     *            the categoryName to set
     */
    public void setCategoryName(String categoryName)
    {
        mCategoryName = categoryName;
    }

    @Override
    public String getPrimaryKey()
    {
        return DB_KEY_CATEGORY_ID;
    }
}
