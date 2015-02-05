// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Brand;
import com.logpie.shopping.management.model.Category;
import com.logpie.shopping.management.model.Image;
import com.logpie.shopping.management.model.LogpieModel;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * @author zhoyilei
 *
 */
public class BrandDAO extends LogpieBaseDAO<Brand>
{
    private static final Logger LOG = Logger.getLogger(BrandDAO.class);
    public static final String sBrandTableName = "Brands";
    public static final String sBrandImageTableAlias = "BrandImage";
    public static final String sBrandSizeChartImageAlias = "BrandSizeChartImage";

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

        // foreign key connection
        @Override
        public Set<String> getQueryConditions()
        {
            return getForeignKeyConnectionConditions();
        }

        @Override
        public Map<String, String> getQueryTables()
        {
            return getForeignKeyConnectionTables();
        }
    }

    private class GetBrandByIdQuery extends LogpieBaseQuerySingleRecordByIdTemplateQuery<Brand>
    {
        GetBrandByIdQuery(final String brandId)
        {
            super(new Brand(), BrandDAO.sBrandTableName, Brand.DB_KEY_BRAND_ID, brandId);
        }

        // foreign key connection
        @Override
        public Set<String> getQueryConditions()
        {
            return getForeignKeyConnectionConditions();
        }

        @Override
        public Map<String, String> getQueryTables()
        {
            return getForeignKeyConnectionTables();
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

    private static Set<String> getForeignKeyConnectionConditions()
    {
        final Set<String> conditions = new HashSet<String>();
        conditions.add(String.format("%s = %s.%s", Brand.DB_KEY_BRAND_IMAGE_ID,
                sBrandImageTableAlias, Image.DB_KEY_IMAGE_ID));
        conditions.add(String.format("%s = %s.%s", Brand.DB_KEY_BRAND_SIZE_CHART_ID,
                sBrandSizeChartImageAlias, Image.DB_KEY_IMAGE_ID));
        conditions.add(String.format("%s = %s", Brand.DB_KEY_BRAND_CATEGORY_ID,
                Category.DB_KEY_CATEGORY_ID));
        return null;
    }

    private static Map<String, String> getForeignKeyConnectionTables()
    {
        final Map<String, String> tableMap = new HashMap<String, String>();
        tableMap.put(sNonAliasPrefix + sBrandTableName, sBrandTableName);
        // alias for multiple foreign key connection
        tableMap.put(sBrandImageTableAlias, ImageDAO.sImageTableName);
        tableMap.put(sBrandSizeChartImageAlias, ImageDAO.sImageTableName);
        tableMap.put(sNonAliasPrefix + CategoryDAO.sCategoryTableName,
                CategoryDAO.sCategoryTableName);
        return tableMap;
    }

}
