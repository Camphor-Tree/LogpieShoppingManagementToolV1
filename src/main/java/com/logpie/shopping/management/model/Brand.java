// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Brand implements RowMapper<Brand>
{
    public static final String DB_KEY_BRAND_ID = "BrandId";
    public static final String DB_KEY_BRAND_IMAGE_ID = "BrandImageId";
    public static final String DB_KEY_BRAND_ENGLISH_NAME = "BrandEnglishName";
    public static final String DB_KEY_BRAND_CHINESE_NAME = "BrandChineseName";
    public static final String DB_KEY_BRAND_IS_ACTIVATED = "BrandIsActivated";
    public static final String DB_KEY_BRAND_SIZE_CHART_URL = "BrandSizeChartId";
    public static final String DB_KEY_BRAND_CATEGORY_ID = "BrandCategoryId";

    private String mBrandId;
    private Image mBrandImage;
    private String mBrandEnglishName;
    private String mBrandChineseName;
    private Image mBrandSizeChartImage;
    private Category mBrandCategory;
    private boolean mBrandIsActivated;

    public Brand(final String brandId, final Image brandImage, final String brandEnglishName,
            final String brandChineseName, final Image brandSizeChartImage,
            final Category brandCategory, final boolean isActivated)
    {
        this.mBrandId = brandId;
        this.mBrandImage = brandImage;
        this.mBrandEnglishName = brandEnglishName;
        this.mBrandChineseName = brandChineseName;
        this.mBrandSizeChartImage = brandSizeChartImage;
        this.mBrandCategory = brandCategory;
        this.mBrandIsActivated = isActivated;
    }

    @Override
    public Brand mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        return getBrandByResultSet(rs, rowNum);
    }

    public static Brand getBrandByResultSet(final ResultSet rs, final int rowNum)
            throws SQLException
    {
        if (rs == null)
        {
            return null;
        }
        final String brandId = rs.getString(DB_KEY_BRAND_ID);
        final Image brandImage = Image.getImageByResultSet(rs, rowNum, "BrandImage");
        final String brandNameEN = rs.getString(DB_KEY_BRAND_ENGLISH_NAME);
        final String brandNameCN = rs.getString(DB_KEY_BRAND_CHINESE_NAME);
        // TODO: Here will produce a bug since there are two foreign keys in the
        // result
        final Image brandSizeChartImage = Image.getImageByResultSet(rs, rowNum,
                "BrandSizeChartImage");
        final Category brandCategory = Category.getCategoryByResultSet(rs, rowNum);
        final boolean brandIsActivated = rs.getBoolean(DB_KEY_BRAND_IS_ACTIVATED);

        return new Brand(brandId, brandImage, brandNameEN, brandNameCN, brandSizeChartImage,
                brandCategory, brandIsActivated);
    }

    /**
     * @return the brandId
     */
    public String getBrandId()
    {
        return mBrandId;
    }

    /**
     * @param brandId
     *            the brandId to set
     */
    public void setBrandId(String brandId)
    {
        mBrandId = brandId;
    }

    /**
     * @return the brandImage
     */
    public Image getBrandImage()
    {
        return mBrandImage;
    }

    /**
     * @param brandImage
     *            the brandImage to set
     */
    public void setBrandImage(Image brandImage)
    {
        mBrandImage = brandImage;
    }

    /**
     * @return the brandEnglishName
     */
    public String getBrandEnglishName()
    {
        return mBrandEnglishName;
    }

    /**
     * @param brandEnglishName
     *            the brandEnglishName to set
     */
    public void setBrandEnglishName(String brandEnglishName)
    {
        mBrandEnglishName = brandEnglishName;
    }

    /**
     * @return the brandChineseName
     */
    public String getBrandChineseName()
    {
        return mBrandChineseName;
    }

    /**
     * @param brandChineseName
     *            the brandChineseName to set
     */
    public void setBrandChineseName(String brandChineseName)
    {
        mBrandChineseName = brandChineseName;
    }

    /**
     * @return the brandSizeChartImage
     */
    public Image getBrandSizeChartImage()
    {
        return mBrandSizeChartImage;
    }

    /**
     * @param brandSizeChartImage
     *            the brandSizeChartImage to set
     */
    public void setBrandSizeChartImage(Image brandSizeChartImage)
    {
        mBrandSizeChartImage = brandSizeChartImage;
    }

    /**
     * @return the brandCategoryId
     */
    public Category getBrandCategory()
    {
        return mBrandCategory;
    }

    /**
     * @param brandCategoryId
     *            the brandCategoryId to set
     */
    public void setBrandCategory(Category brandCategory)
    {
        mBrandCategory = brandCategory;
    }

    /**
     * @return the brandIsActivated
     */
    public boolean isBrandIsActivated()
    {
        return mBrandIsActivated;
    }

    /**
     * @param brandIsActivated
     *            the brandIsActivated to set
     */
    public void setBrandIsActivated(boolean brandIsActivated)
    {
        mBrandIsActivated = brandIsActivated;
    }

}