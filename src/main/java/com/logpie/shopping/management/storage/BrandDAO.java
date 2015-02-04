// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Brand;
import com.logpie.shopping.management.model.LogpieModel;
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

    /**
     * Update the brand profile
     * 
     * @param brand
     * @return
     */
    public boolean updateBrandProfile(final Brand brand)
    {
        final UpdateBrandUpdate updateBrandUpdate = new UpdateBrandUpdate(brand, sBrandTableName);
        return super.updateData(updateBrandUpdate);
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
            return mBrand.getModelMap();
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

    private class UpdateBrandUpdate extends LogpieBaseUpdateRecordTemplateUpdate<Brand>
    {
        /**
         * @param model
         * @param tableName
         */
        public UpdateBrandUpdate(LogpieModel model, String tableName)
        {
            super(model, tableName);
        }
    }

}
