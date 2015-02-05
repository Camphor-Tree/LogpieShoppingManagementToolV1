// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Brand;
import com.logpie.shopping.management.model.Image;
import com.logpie.shopping.management.model.LogpieModel;
import com.logpie.shopping.management.model.Product;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * @author zhoyilei
 *
 */
public class ProductDAO extends LogpieBaseDAO<Product>
{
    private static final Logger LOG = Logger.getLogger(ProductDAO.class);
    public static final String sProductTableName = "Products";

    /**
     * For adding a new product into the database
     * 
     * @param product
     * @return true if adding product successfully. false if adding product
     *         fails
     */
    public boolean addProduct(final Product product)
    {
        final LogpieDataInsert<Product> addProductInsert = new AddProductInsert(product);
        return super.insertData(addProductInsert);
    }

    /**
     * For getting all existing categories
     * 
     * @return All existing categories
     */
    public List<Product> getAllProduct()
    {
        GetAllProductQuery getAllProductQuery = new GetAllProductQuery();
        return super.queryResult(getAllProductQuery);
    }

    /**
     * For querying specific Product by ProductId
     * 
     * @param productId
     * @return Product corresponding to the ProductId
     */
    public Product getProductById(final String productId)
    {
        GetProductByIdQuery getProductByIdQuery = new GetProductByIdQuery(productId);
        List<Product> productList = super.queryResult(getProductByIdQuery);
        if (CollectionUtils.isEmpty(productList) || productList.size() > 1)
        {
            LOG.error("The product cannot be found by this id:" + productId);
            return null;
        }
        return productList.get(0);
    }

    /**
     * Update the product profile
     * 
     * @param product
     * @return
     */
    public boolean updateProductProfile(final Product product)
    {
        final UpdateProductUpdate updateProductUpdate = new UpdateProductUpdate(product,
                sProductTableName);
        return super.updateData(updateProductUpdate);
    }

    private class AddProductInsert implements LogpieDataInsert<Product>
    {
        private Product mProduct;

        AddProductInsert(final Product product)
        {
            mProduct = product;
        }

        @Override
        public String getInsertTable()
        {
            return sProductTableName;
        }

        @Override
        public Map<String, Object> getInsertValues()
        {
            return mProduct.getModelMap();
        }
    }

    private class GetAllProductQuery extends LogpieBaseQueryAllTemplateQuery<Product>
    {
        GetAllProductQuery()
        {
            super(new Product(), ProductDAO.sProductTableName);
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

    private class GetProductByIdQuery extends LogpieBaseQuerySingleRecordByIdTemplateQuery<Product>
    {
        GetProductByIdQuery(final String productId)
        {
            super(new Product(), ProductDAO.sProductTableName, Product.DB_KEY_PRODUCT_ID, productId);
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

    private class UpdateProductUpdate extends LogpieBaseUpdateRecordTemplateUpdate<Product>
    {
        /**
         * @param model
         * @param tableName
         */
        public UpdateProductUpdate(LogpieModel model, String tableName)
        {
            super(model, tableName);
        }
    }

    private static Set<String> getForeignKeyConnectionConditions()
    {
        final Set<String> conditions = new HashSet<String>();
        conditions.add(String.format("%s = %s", Product.DB_KEY_PRODUCT_BRAND_ID,
                Brand.DB_KEY_BRAND_ID));
        conditions.add(String.format("%s = %s", Product.DB_KEY_PRODUCT_IMAGE_ID,
                Image.DB_KEY_IMAGE_ID));
        return null;
    }

    private static Map<String, String> getForeignKeyConnectionTables()
    {
        final Map<String, String> tableMap = new HashMap<String, String>();
        tableMap.put(sNonAliasPrefix + sProductTableName, sProductTableName);
        // alias for multiple foreign key connection
        tableMap.put(sNonAliasPrefix + BrandDAO.sBrandTableName, sProductTableName);
        tableMap.put(sNonAliasPrefix + ImageDAO.sImageTableName, sProductTableName);
        return tableMap;
    }
}
