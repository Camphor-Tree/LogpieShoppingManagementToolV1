// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import com.logpie.shopping.management.storage.BrandDAO;
import com.logpie.shopping.management.storage.CategoryDAO;
import com.logpie.shopping.management.storage.ImageDAO;

public class Brand implements RowMapper<Brand>, LogpieModel
{
    public static final String DB_KEY_BRAND_ID = "BrandId";
    public static final String DB_KEY_BRAND_IMAGE_ID = "BrandImageId";
    public static final String DB_KEY_BRAND_ENGLISH_NAME = "BrandEnglishName";
    public static final String DB_KEY_BRAND_CHINESE_NAME = "BrandChineseName";
    public static final String DB_KEY_BRAND_IS_ACTIVATED = "BrandIsActivated";
    public static final String DB_KEY_BRAND_SIZE_CHART_ID = "BrandSizeChartId";
    public static final String DB_KEY_BRAND_CATEGORY_ID = "BrandCategoryId";

    private static Logger LOG = Logger.getLogger(Brand.class);

    private String mBrandId;
    private Image mBrandImage;
    private String mBrandEnglishName;
    private String mBrandChineseName;
    private Image mBrandSizeChartImage;// may be null
    private Category mBrandCategory;
    private Boolean mBrandIsActivated;

    // For RowMapper
    public Brand()
    {

    }

    // For create a new brand
    public Brand(final Image brandImage, final String brandEnglishName,
            final String brandChineseName, final Image brandSizeChartImage,
            final Category brandCategory, final Boolean brandIsActivated)
    {
        this.mBrandImage = brandImage;
        this.mBrandEnglishName = brandEnglishName;
        this.mBrandChineseName = brandChineseName;
        this.mBrandSizeChartImage = brandSizeChartImage;
        this.mBrandCategory = brandCategory;
        this.mBrandIsActivated = brandIsActivated;
    }

    public Brand(final String brandId, final Image brandImage, final String brandEnglishName,
            final String brandChineseName, final Image brandSizeChartImage,
            final Category brandCategory, final Boolean brandIsActivated)
    {
        this.mBrandId = brandId;
        this.mBrandImage = brandImage;
        this.mBrandEnglishName = brandEnglishName;
        this.mBrandChineseName = brandChineseName;
        this.mBrandSizeChartImage = brandSizeChartImage;
        this.mBrandCategory = brandCategory;
        this.mBrandIsActivated = brandIsActivated;
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
        final Image brandImage = Image.getImageByResultSet(rs, rowNum,
                BrandDAO.sBrandImageTableAlias);
        final String brandNameEN = rs.getString(DB_KEY_BRAND_ENGLISH_NAME);
        final String brandNameCN = rs.getString(DB_KEY_BRAND_CHINESE_NAME);
        final Integer sizeChartImageId = rs.getInt(DB_KEY_BRAND_SIZE_CHART_ID);
        Image brandSizeChartImage = null;
        if (sizeChartImageId != null)
        {
            final ImageDAO imageDAO = new ImageDAO();
            brandSizeChartImage = imageDAO.getImageById(String.valueOf(sizeChartImageId));
        }
        final Category brandCategory = Category.getCategoryByResultSet(rs, rowNum);
        final boolean brandIsActivated = rs.getBoolean(DB_KEY_BRAND_IS_ACTIVATED);

        return new Brand(brandId, brandImage, brandNameEN, brandNameCN, brandSizeChartImage,
                brandCategory, brandIsActivated);
    }

    @Override
    public Map<String, Object> getModelMap()
    {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put(Brand.DB_KEY_BRAND_ENGLISH_NAME, mBrandEnglishName);
        modelMap.put(Brand.DB_KEY_BRAND_CHINESE_NAME, mBrandChineseName);
        modelMap.put(Brand.DB_KEY_BRAND_IMAGE_ID, mBrandImage.getImageId());
        if (mBrandSizeChartImage != null)
        {
            modelMap.put(Brand.DB_KEY_BRAND_SIZE_CHART_ID, mBrandSizeChartImage.getImageId());
        }
        modelMap.put(Brand.DB_KEY_BRAND_CATEGORY_ID, mBrandCategory.getCategoryId());
        modelMap.put(Brand.DB_KEY_BRAND_IS_ACTIVATED, mBrandIsActivated);
        return modelMap;
    }

    public static Brand readNewBrandFromRequest(final HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }
        final ImageDAO imageDAO = new ImageDAO();
        final CategoryDAO categoryDAO = new CategoryDAO();

        final String brandImageId = request.getParameter("BrandImageId");
        final Image brandImage = imageDAO.getImageById(brandImageId);
        final String brandEnglishName = request.getParameter("BrandEnglishName");
        final String brandChineseName = request.getParameter("BrandChineseName");
        final String brandSizeChartImageId = request.getParameter("BrandSizeChartImageId");
        Image brandSizeChartImage = null;
        if (!StringUtils.isEmpty(brandSizeChartImageId))
        {
            brandSizeChartImage = imageDAO.getImageById(brandSizeChartImageId);
        }
        final Category brandCategory = categoryDAO.getCategoryById(request
                .getParameter("BrandCategoryId"));
        final Boolean brandIsActivated = Boolean.parseBoolean(request
                .getParameter("BrandIsActivated"));

        return new Brand(brandImage, brandEnglishName, brandChineseName, brandSizeChartImage,
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
     * @return the brandCategory
     */
    public Category getBrandCategory()
    {
        return mBrandCategory;
    }

    /**
     * @param brandCategory
     *            the brandCategory to set
     */
    public void setBrandCategory(Category brandCategory)
    {
        mBrandCategory = brandCategory;
    }

    /**
     * @return the brandIsActivated
     */
    public Boolean getBrandIsActivated()
    {
        return mBrandIsActivated;
    }

    /**
     * @param brandIsActivated
     *            the brandIsActivated to set
     */
    public void setBrandIsActivated(Boolean brandIsActivated)
    {
        mBrandIsActivated = brandIsActivated;
    }

}
