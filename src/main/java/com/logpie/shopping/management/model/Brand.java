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
    public static final String DB_KEY_BRAND_IS_ACTIVATED = "IsActivated";
    public static final String DB_KEY_BRAND_SIZE_CHART_URL = "BrandSizeChartId";
    public static final String DB_KEY_BRAND_CATEGORY_ID = "CategoryId";

    private final String mBrandId;
    private final Image mBrandImage;
    private final String mBrandEnglishName;
    private final String mBrandChineseName;
    private final Image mBrandSizeChartImage;
    private final String mBrandCategoryId;
    private final boolean mBrandIsActivated;

    public Brand(final String brandId, final Image brandImage, final String brandEnglishName,
            final String brandChineseName, final Image brandSizeChartImage,
            final String brandCategoryId, final boolean isActivated)
    {
        this.mBrandId = brandId;
        this.mBrandImage = brandImage;
        this.mBrandEnglishName = brandEnglishName;
        this.mBrandChineseName = brandChineseName;
        this.mBrandSizeChartImage = brandSizeChartImage;
        this.mBrandCategoryId = brandCategoryId;
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
        final Image brandImage = Image.getImageByResultSet(rs, rowNum);
        final String brandNameEN = rs.getString(DB_KEY_BRAND_ENGLISH_NAME);
        final String brandNameCN = rs.getString(DB_KEY_BRAND_CHINESE_NAME);
        // TODO: Here will produce a bug since there are two foreign keys in the
        // result
        final Image brandSizeChartImage = Image.getImageByResultSet(rs, rowNum);
        final String brandCategoryId = rs.getString(DB_KEY_BRAND_CATEGORY_ID);
        final boolean brandIsActivated = rs.getBoolean(DB_KEY_BRAND_IS_ACTIVATED);

        return new Brand(brandId, brandImage, brandNameEN, brandNameCN, brandSizeChartImage,
                brandCategoryId, brandIsActivated);

    }

}
