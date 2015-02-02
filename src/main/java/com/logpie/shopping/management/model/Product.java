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
public class Product implements RowMapper<Product>
{
    public static final String DB_KEY_PRODUCT_ID = "ProductId";
    public static final String DB_KEY_PRODUCT_NAME = "ProductName";
    public static final String DB_KEY_PRODUCT_DESCRIPTION = "ProductDescription";
    public static final String DB_KEY_PRODUCT_LINK = "ProductLink";
    public static final String DB_KEY_PRODUCT_WEIGHT = "ProductWeight";
    public static final String DB_KEY_PRODUCT_IMAGE_ID = "ProductImageId";
    public static final String DB_KEY_IS_ACTIVATED = "IsActivated";
    public static final String DB_KEY_POST_DATE = "PostDate";
    public static final String DB_KEY_BAND_ID = "BrandId";

    private String mProductId;
    private String mProductName;
    private String mProductDescription;
    private String mProductLink;
    private String mProductImageId;
    private int mProductWeight;
    private boolean mIsActivated;
    private String mPostDate;
    private Brand mBrand;

    public Product(final String productId, final String productName,
            final String productDescription, final String productLink, final String productImageId,
            final int productWeight, final boolean isActivated, final String postDate,
            final Brand brand)
    {
        mProductId = productId;
        mProductName = productName;
        mProductDescription = productDescription;
        mProductLink = productLink;
        mProductImageId = productImageId;
        mProductWeight = productWeight;
        mIsActivated = isActivated;
        mPostDate = postDate;
        mBrand = brand;
    }

    @Override
    public Product mapRow(ResultSet rs, int row) throws SQLException
    {
        return getProductByResultSet(rs, row);
    }

    public static Product getProductByResultSet(final ResultSet rs, final int row)
            throws SQLException
    {
        if (rs == null)
        {
            return null;
        }
        final String productId = rs.getString(DB_KEY_PRODUCT_ID);
        final String productName = rs.getString(DB_KEY_PRODUCT_NAME);
        final String productDescription = rs.getString(DB_KEY_PRODUCT_DESCRIPTION);
        final String productLink = rs.getString(DB_KEY_PRODUCT_LINK);
        final int productWeight = rs.getInt(DB_KEY_PRODUCT_WEIGHT);
        final String productImageId = rs.getString(DB_KEY_PRODUCT_IMAGE_ID);
        final boolean isActivated = rs.getBoolean(DB_KEY_IS_ACTIVATED);
        final Date postDate = rs.getTimestamp(DB_KEY_POST_DATE);
        final String postDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(postDate);
        final Brand brand = Brand.getBrandByResultSet(rs, row);

        return new Product(productId, productName, productDescription, productLink, productImageId,
                productWeight, isActivated, postDateString, brand);
    }

    /**
     * @return the productId
     */
    public String getProductId()
    {
        return mProductId;
    }

    /**
     * @param productId
     *            the productId to set
     */
    public void setProductId(String productId)
    {
        mProductId = productId;
    }

    /**
     * @return the productName
     */
    public String getProductName()
    {
        return mProductName;
    }

    /**
     * @param productName
     *            the productName to set
     */
    public void setProductName(String productName)
    {
        mProductName = productName;
    }

    /**
     * @return the productDescription
     */
    public String getProductDescription()
    {
        return mProductDescription;
    }

    /**
     * @param productDescription
     *            the productDescription to set
     */
    public void setProductDescription(String productDescription)
    {
        mProductDescription = productDescription;
    }

    /**
     * @return the productLink
     */
    public String getProductLink()
    {
        return mProductLink;
    }

    /**
     * @param productLink
     *            the productLink to set
     */
    public void setProductLink(String productLink)
    {
        mProductLink = productLink;
    }

    /**
     * @return the productImageId
     */
    public String getProductImageId()
    {
        return mProductImageId;
    }

    /**
     * @param productImageId
     *            the productImageId to set
     */
    public void setProductImageId(String productImageId)
    {
        mProductImageId = productImageId;
    }

    /**
     * @return the productWeight
     */
    public int getProductWeight()
    {
        return mProductWeight;
    }

    /**
     * @param productWeight
     *            the productWeight to set
     */
    public void setProductWeight(int productWeight)
    {
        mProductWeight = productWeight;
    }

    /**
     * @return the isActivated
     */
    public boolean isIsActivated()
    {
        return mIsActivated;
    }

    /**
     * @param isActivated
     *            the isActivated to set
     */
    public void setIsActivated(boolean isActivated)
    {
        mIsActivated = isActivated;
    }

    /**
     * @return the postDate
     */
    public String getPostDate()
    {
        return mPostDate;
    }

    /**
     * @param postDate
     *            the postDate to set
     */
    public void setPostDate(String postDate)
    {
        mPostDate = postDate;
    }

    /**
     * @return the brand
     */
    public Brand getBrand()
    {
        return mBrand;
    }

    /**
     * @param brand
     *            the brand to set
     */
    public void setBrand(Brand brand)
    {
        mBrand = brand;
    }

}
