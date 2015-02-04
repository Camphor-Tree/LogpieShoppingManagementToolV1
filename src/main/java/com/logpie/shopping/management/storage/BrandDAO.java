// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Brand;
import com.logpie.shopping.management.model.Category;
import com.logpie.shopping.management.model.Image;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * @author zhoyilei
 *
 */
public class BrandDAO extends LogpieBaseDAO<Brand>
{
    private static final Logger LOG = Logger.getLogger(BrandDAO.class);
    private static final String sBrandTableName = "Brands";

    /**
     * For adding a new brand into the database
     * 
     * @param brand
     * @return true if adding brand successfully. false if adding brand fails
     */
    public boolean addBrand(final Brand brand)
    {
        final LogpieDataInsert<Brand> addBrandInsert = new AddBrandInsert(brand);
        return super.insertData(addBrandInsert);
    }

    /**
     * For getting all existing categories
     * 
     * @return All existing categories
     */
    public List<Brand> getAllBrand()
    {
        GetAllBrandQuery getAllBrandQuery = new GetAllBrandQuery();
        return super.queryResult(getAllBrandQuery);
    }

    /**
     * For querying specific Brand by BrandId
     * 
     * @param brandId
     * @return Brand corresponding to the BrandId
     */
    public Brand getBrandById(final String brandId)
    {
        GetBrandByIdQuery getBrandByIdQuery = new GetBrandByIdQuery(brandId);
        List<Brand> brandList = super.queryResult(getBrandByIdQuery);
        if (CollectionUtils.isEmpty(brandList) || brandList.size() > 1)
        {
            LOG.error("The brand cannot be found by this id:" + brandId);
            return null;
        }
        return brandList.get(0);

    }

    private class AddBrandInsert implements LogpieDataInsert<Brand>
    {
        private Brand mBrand;

        AddBrandInsert(final Brand brand)
        {
            mBrand = brand;
        }

        @Override
        public String getInsertTable()
        {
            return sBrandTableName;
        }

        @Override
        public Map<String, Object> getInsertValues()
        {
            final String brandEnglishName = mBrand.getBrandEnglishName();
            final String brandChineseName = mBrand.getBrandChineseName();
            final Image brandImage = mBrand.getBrandImage();
            final String brandImageId = brandImage.getImageId();
            final Image brandSizeChartImage = mBrand.getBrandSizeChartImage();
            final String brandSizeChartImageId = brandSizeChartImage.getImageId();
            final Category brandCategory = mBrand.getBrandCategory();
            final String brandCategoryId = brandCategory.getCategoryId();
            final Boolean brandIsActivated = mBrand.getBrandIsActivated();

            final Map<String, Object> insertValues = new HashMap<String, Object>();
            insertValues.put(Brand.DB_KEY_BRAND_ENGLISH_NAME, brandEnglishName);
            insertValues.put(Brand.DB_KEY_BRAND_CHINESE_NAME, brandChineseName);
            insertValues.put(Brand.DB_KEY_BRAND_IMAGE_ID, brandImageId);
            insertValues.put(Brand.DB_KEY_BRAND_SIZE_CHART_ID, brandSizeChartImageId);
            insertValues.put(Brand.DB_KEY_BRAND_CATEGORY_ID, brandCategoryId);
            insertValues.put(Brand.DB_KEY_BRAND_IS_ACTIVATED, brandIsActivated);
            return insertValues;
        }
    }

    private class GetAllBrandQuery extends LogpieBaseQueryAllTemplateQuery<Brand>
    {
        GetAllBrandQuery()
        {
            super(new Brand(), BrandDAO.sBrandTableName);
        }
    }

    private class GetBrandByIdQuery extends LogpieBaseQuerySingleRecordByIdTemplateQuery<Brand>
    {
        GetBrandByIdQuery(final String brandId)
        {
            super(new Brand(), BrandDAO.sBrandTableName, Brand.DB_KEY_BRAND_ID, brandId);
        }
    }

}
