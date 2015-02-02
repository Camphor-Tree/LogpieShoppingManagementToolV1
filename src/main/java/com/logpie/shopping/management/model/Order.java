// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.logpie.shopping.management.auth.logic.Admin;

/**
 * @author zhoyilei
 *
 */
public class Order implements RowMapper<Order>
{
    public static final String DB_KEY_ORDER_ID = "OrderId";
    public static final String DB_KEY_ORDER_DATE = "OrderDate";
    public static final String DB_KEY_PRODUCT_ID = "ProductId";
    public static final String DB_KEY_PRODUCT_COUNT = "ProductCount";
    public static final String DB_KEY_ORDER_BUYER_NAME = "OrderBuyerName";
    public static final String DB_KEY_ORDER_PROXY = "OrderProxy";
    public static final String DB_KEY_ORDER_PROXY_PROFIT_PERCENTAGE = "OrderProxyProfitPercentage";
    public static final String DB_KEY_CURRENCY_RATE = "CurrencyRate";
    public static final String DB_KEY_PACKAGE_ID = "PackageId";
    public static final String DB_KEY_ESTIMATED_SHIPPING_FEE = "EstimatedShippingFee";
    public static final String DB_KEY_ACTUAL_SHIPPING_FEE = "ActualShippingFee";
    public static final String DB_KEY_SELLING_PRICE = "SellingPrice";
    public static final String DB_KEY_CUSTOMER_PAID_MONEY = "CustomerPaidMoney";
    public static final String DB_KEY_FINAL_PROFIT = "FinalProfit";
    public static final String DB_KEY_IS_PROFIT_PAID = "IsProfitPaid";
    public static final String DB_KEY_ORDER_NOTES = "OrderNotes";

    private String mOrderId;
    private String mOrderDate;
    private Product mProduct;
    private int mProductCount;
    private String mOrderBuyerName;
    private Admin mOrderProxy; // may be null
    private float mProxyProfitPercentage;
    private float mCurrencyRate;
    private Package mPackage;// may be null
    private int mEstimatedShippingFee;
    private int mActualShippingFee;// may be null
    private int mSellingPrice;
    private int mCustomerPaidMoney;// may be null
    private int mFinalProfit;// may be null
    private boolean mIsProfitPaid;
    private String mOrderNote;

    /**
     * @param orderId
     * @param orderDate
     * @param product
     * @param productCount
     * @param orderBuyerName
     * @param orderProxy
     * @param proxyProfitPercentage
     * @param currencyRate
     * @param package1
     * @param estimatedShippingFee
     * @param actualShippingFee
     * @param sellingPrice
     * @param customerPaidMoney
     * @param finalProfit
     * @param isProfitPaid
     * @param orderNote
     */
    public Order(String orderId, String orderDate, Product product, int productCount,
            String orderBuyerName, Admin orderProxy, float proxyProfitPercentage,
            float currencyRate, Package package1, int estimatedShippingFee, int actualShippingFee,
            int sellingPrice, int customerPaidMoney, int finalProfit, boolean isProfitPaid,
            String orderNote)
    {
        mOrderId = orderId;
        mOrderDate = orderDate;
        mProduct = product;
        mProductCount = productCount;
        mOrderBuyerName = orderBuyerName;
        mOrderProxy = orderProxy;
        mProxyProfitPercentage = proxyProfitPercentage;
        mCurrencyRate = currencyRate;
        mPackage = package1;
        mEstimatedShippingFee = estimatedShippingFee;
        mActualShippingFee = actualShippingFee;
        mSellingPrice = sellingPrice;
        mCustomerPaidMoney = customerPaidMoney;
        mFinalProfit = finalProfit;
        mIsProfitPaid = isProfitPaid;
        mOrderNote = orderNote;
    }

    @Override
    public Order mapRow(final ResultSet rs, final int row) throws SQLException
    {
        return getOrderByResultSet(rs, row);
    }

    public static Order getOrderByResultSet(final ResultSet rs, final int row) throws SQLException
    {
        if (rs == null)
        {
            return null;
        }

        final String orderId = String.valueOf(rs.getInt(DB_KEY_ORDER_ID));
        final Date orderDate = rs.getTimestamp(DB_KEY_ORDER_DATE);
        final String orderDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(orderDate);
        final Product product = Product.getProductByResultSet(rs, row);
        final int productCount = rs.getInt(DB_KEY_PRODUCT_COUNT);
        final String orderBuyerName = rs.getString(DB_KEY_ORDER_BUYER_NAME);
        final Admin orderProxy = Admin.getAdminByResultSet(rs, row);
        final float proxyProfitPercentage = rs.getFloat(DB_KEY_ORDER_PROXY_PROFIT_PERCENTAGE);
        final float currencyRate = rs.getFloat(DB_KEY_CURRENCY_RATE);
        // TODO
        final Package package1 = null;
        final int estimatedShippingFee = rs.getInt(DB_KEY_ESTIMATED_SHIPPING_FEE);
        final int actualShippingFee = rs.getInt(DB_KEY_ACTUAL_SHIPPING_FEE);
        final int sellingPrice = rs.getInt(DB_KEY_SELLING_PRICE);
        final int customerPaidMoney = rs.getInt(DB_KEY_CUSTOMER_PAID_MONEY);
        final int finalProfit = rs.getInt(DB_KEY_FINAL_PROFIT);
        final boolean isProfitPaid = rs.getBoolean(DB_KEY_IS_PROFIT_PAID);
        final String orderNote = rs.getString(DB_KEY_ORDER_NOTES);

        return new Order(orderId, orderDateString, product, productCount, orderBuyerName,
                orderProxy, proxyProfitPercentage, currencyRate, package1, estimatedShippingFee,
                actualShippingFee, sellingPrice, customerPaidMoney, finalProfit, isProfitPaid,
                orderNote);
    }
}
