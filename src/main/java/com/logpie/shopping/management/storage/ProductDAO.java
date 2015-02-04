// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Brand;
import com.logpie.shopping.management.model.Image;
import com.logpie.shopping.management.model.Product;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * @author zhoyilei
 *
 */
public class ProductDAO extends LogpieBaseDAO<Product>
{
    private static final Logger LOG = Logger.getLogger(ProductDAO.class);
    private static final String sProductTableName = "Products";

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
            final String productName = mProduct.getProductName();
            final String productDescription = mProduct.getProductDescription();
            final String productLink = mProduct.getProductLink();
            final Image productImage = mProduct.getProductImage();
            final String productImageId = productImage.getImageId();
            final Integer productWeight = mProduct.getProductWeight();
            final Boolean productIsActivated = mProduct.getProductIsActivated();
            final String productPostDate = mProduct.getProductPostDate();
            final Brand productBrand = mProduct.getProductBrand();
            final String productBrandId = productBrand.getBrandId();

            final Map<String, Object> insertValues = new HashMap<String, Object>();
            insertValues.put(Product.DB_KEY_PRODUCT_NAME, productName);
            insertValues.put(Product.DB_KEY_PRODUCT_DESCRIPTION, productDescription);
            insertValues.put(Product.DB_KEY_PRODUCT_LINK, productLink);
            insertValues.put(Product.DB_KEY_PRODUCT_IMAGE_ID, productImageId);
            insertValues.put(Product.DB_KEY_PRODUCT_WEIGHT, productWeight);
            insertValues.put(Product.DB_KEY_PRODUCT_IS_ACTIVATED, productIsActivated);
            insertValues.put(Product.DB_KEY_PRODUCT_POST_DATE, productPostDate);
            insertValues.put(Product.DB_KEY_PRODUCT_BRAND_ID, productBrandId);
            return insertValues;
        }
    }

    private class GetAllProductQuery extends LogpieBaseQueryAllTemplateQuery<Product>
    {
        GetAllProductQuery()
        {
            super(new Product(), ProductDAO.sProductTableName);
        }
    }

    private class GetProductByIdQuery extends LogpieBaseQuerySingleRecordByIdTemplateQuery<Product>
    {
        GetProductByIdQuery(final String productId)
        {
            super(new Product(), ProductDAO.sProductTableName, Product.DB_KEY_PRODUCT_ID, productId);
        }
    }

}
