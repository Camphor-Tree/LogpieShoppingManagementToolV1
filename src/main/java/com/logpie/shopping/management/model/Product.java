// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.RowMapper;

import com.logpie.shopping.management.storage.BrandDAO;
import com.logpie.shopping.management.storage.ImageDAO;

/**
 * @author zhoyilei
 *
 */
public class Product implements RowMapper<Product>, LogpieModel
{
    public static final String DB_KEY_PRODUCT_ID = "ProductId";
    public static final String DB_KEY_PRODUCT_NAME = "ProductName";
    public static final String DB_KEY_PRODUCT_DESCRIPTION = "ProductDescription";
    public static final String DB_KEY_PRODUCT_LINK = "ProductLink";
    public static final String DB_KEY_PRODUCT_WEIGHT = "ProductWeight";
    public static final String DB_KEY_PRODUCT_IMAGE_ID = "ProductImageId";
    public static final String DB_KEY_PRODUCT_IS_ACTIVATED = "ProductIsActivated";
    public static final String DB_KEY_PRODUCT_POST_DATE = "ProductPostDate";
    public static final String DB_KEY_PRODUCT_BRAND_ID = "ProductBrandId";

    private String mProductId;
    private String mProductName;
    private String mProductDescription;
    private String mProductLink;
    private Image mProductImage;
    private Integer mProductWeight;
    private Boolean mProductIsActivated;
    private String mProductPostDate;
    private Brand mProductBrand;

    // For RowMapper
    public Product()
    {

    }

    // For create a new product
    // Product id and postdate are all created by database
    public Product(final String productName, final String productDescription,
            final String productLink, final Image productImage, final Integer productWeight,
            final Boolean productIsActivated, final Brand productBrand)
    {
        mProductName = productName;
        mProductDescription = productDescription;
        mProductLink = productLink;
        mProductImage = productImage;
        mProductWeight = productWeight;
        mProductIsActivated = productIsActivated;
        mProductBrand = productBrand;
    }

    public Product(final String productId, final String productName,
            final String productDescription, final String productLink, final Image productImage,
            final Integer productWeight, final Boolean productIsActivated,
            final String productPostDate, final Brand productBrand)
    {
        mProductId = productId;
        mProductName = productName;
        mProductDescription = productDescription;
        mProductLink = productLink;
        mProductImage = productImage;
        mProductWeight = productWeight;
        mProductIsActivated = productIsActivated;
        mProductPostDate = productPostDate;
        mProductBrand = productBrand;
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
        // final ProductDAO productDAO = new ProductDAO();
        // return productDAO.getProductById(productId);
        final String productName = rs.getString(DB_KEY_PRODUCT_NAME);
        final String productDescription = rs.getString(DB_KEY_PRODUCT_DESCRIPTION);
        final String productLink = rs.getString(DB_KEY_PRODUCT_LINK);
        final int productWeight = rs.getInt(DB_KEY_PRODUCT_WEIGHT);
        final Image productImage = Image.getImageByResultSet(rs, row, null);
        final boolean isActivated = rs.getBoolean(DB_KEY_PRODUCT_IS_ACTIVATED);
        final Date postDate = rs.getTimestamp(DB_KEY_PRODUCT_POST_DATE);
        final String postDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(postDate);
        final Brand brand = Brand.getBrandByResultSet(rs, row);

        return new Product(productId, productName, productDescription, productLink, productImage,
                productWeight, isActivated, postDateString, brand);
    }

    @Override
    public Map<String, Object> getModelMap()
    {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put(Product.DB_KEY_PRODUCT_NAME, mProductName);
        modelMap.put(Product.DB_KEY_PRODUCT_DESCRIPTION, mProductDescription);
        modelMap.put(Product.DB_KEY_PRODUCT_LINK, mProductLink);
        modelMap.put(Product.DB_KEY_PRODUCT_IMAGE_ID, mProductImage.getImageId());
        modelMap.put(Product.DB_KEY_PRODUCT_WEIGHT, mProductWeight);
        modelMap.put(Product.DB_KEY_PRODUCT_IS_ACTIVATED, mProductIsActivated);
        // It will default to current date
        // modelMap.put(Product.DB_KEY_PRODUCT_POST_DATE, mProductPostDate);
        modelMap.put(Product.DB_KEY_PRODUCT_BRAND_ID, mProductBrand.getBrandId());
        return modelMap;
    }

    public static Product readNewProductFromRequest(final HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }
        final ImageDAO imageDAO = new ImageDAO();
        final BrandDAO brandDAO = new BrandDAO();

        final String productName = request.getParameter("ProductName");
        final String productDescription = request.getParameter("ProductDescription");
        final String productLink = request.getParameter("ProductLink");
        final String productImageId = request.getParameter("ProductImageId");
        final Image productImage = imageDAO.getImageById(productImageId);
        final Integer productWeight = Integer.parseInt(request.getParameter("ProductWeight"));
        final Boolean productIsActivated = Boolean.parseBoolean(request
                .getParameter("ProductIsActivated"));
        // final String productPostDate =
        // request.getParameter("ProductPostDate");
        final Brand productBrand = brandDAO.getBrandById(request.getParameter("ProductBrandId"));

        return new Product(productName, productDescription, productLink, productImage,
                productWeight, productIsActivated, productBrand);
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
    public Image getProductImage()
    {
        return mProductImage;
    }

    /**
     * @param productImageId
     *            the productImageId to set
     */
    public void setProductImageId(Image productImage)
    {
        mProductImage = productImage;
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
    public Boolean getProductIsActivated()
    {
        return mProductIsActivated;
    }

    /**
     * @param isActivated
     *            the isActivated to set
     */
    public void setProductIsActivated(boolean isActivated)
    {
        mProductIsActivated = isActivated;
    }

    /**
     * @return the postDate
     */
    public String getProductPostDate()
    {
        return mProductPostDate;
    }

    /**
     * @param postDate
     *            the postDate to set
     */
    public void setProductPostDate(String postDate)
    {
        mProductPostDate = postDate;
    }

    /**
     * @return the brand
     */
    public Brand getProductBrand()
    {
        return mProductBrand;
    }

    /**
     * @param brand
     *            the brand to set
     */
    public void setProductBrand(Brand brand)
    {
        mProductBrand = brand;
    }

    @Override
    public String getPrimaryKey()
    {
        return DB_KEY_PRODUCT_ID;
    }

}
