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

import com.logpie.shopping.management.storage.AdminDAO;
import com.logpie.shopping.management.storage.LogpiePackageDAO;
import com.logpie.shopping.management.storage.ProductDAO;

/**
 * @author zhoyilei
 *
 */
public class Order implements RowMapper<Order>, LogpieModel
{
    public static final String DB_KEY_ORDER_ID = "OrderId";
    public static final String DB_KEY_ORDER_DATE = "OrderDate";
    public static final String DB_KEY_ORDER_PRODUCT_ID = "OrderProductId";
    public static final String DB_KEY_ORDER_PRODUCT_COUNT = "OrderProductCount";
    public static final String DB_KEY_ORDER_BUYER_NAME = "OrderBuyerName";
    public static final String DB_KEY_ORDER_PROXY_ID = "OrderProxyId";
    public static final String DB_KEY_ORDER_PROXY_PROFIT_PERCENTAGE = "OrderProxyProfitPercentage";
    public static final String DB_KEY_ORDER_ACTUAL_COST = "OrderActualCost";
    public static final String DB_KEY_ORDER_CURRENCY_RATE = "OrderCurrencyRate";
    public static final String DB_KEY_ORDER_PACKAGE_ID = "OrderPackageId";
    public static final String DB_KEY_ORDER_ESTIMATED_SHIPPING_FEE = "OrderEstimatedShippingFee";
    public static final String DB_KEY_ORDER_ACTUAL_SHIPPING_FEE = "OrderActualShippingFee";
    public static final String DB_KEY_ORDER_SELLING_PRICE = "OrderSellingPrice";
    public static final String DB_KEY_ORDER_CUSTOMER_PAID_MONEY = "OrderCustomerPaidMoney";
    public static final String DB_KEY_ORDER_FINAL_PROFIT = "OrderFinalProfit";
    public static final String DB_KEY_ORDER_IS_PROFIT_PAID = "OrderIsProfitPaid";
    public static final String DB_KEY_ORDER_NOTE = "OrderNote";

    private String mOrderId;
    private String mOrderDate;
    private Product mOrderProduct;
    private Integer mOrderProductCount;
    private String mOrderBuyerName;
    private Admin mOrderProxy; // may be null
    private Float mOrderProxyProfitPercentage;
    private Float mOrderActualCost;
    private Float mOrderCurrencyRate;
    private LogpiePackage mOrderPackage;// may be null
    private Integer mOrderEstimatedShippingFee;
    private Integer mOrderActualShippingFee;// may be null
    private Integer mOrderSellingPrice;
    private Integer mOrderCustomerPaidMoney;// may be null
    private Integer mOrderFinalProfit;// may be null
    private Boolean mOrderIsProfitPaid;
    private String mOrderNote;

    // For RowMapper
    public Order()
    {

    }

    /**
     * @param orderDate
     * @param product
     * @param productCount
     * @param orderBuyerName
     * @param orderProxy
     * @param proxyProfitPercentage
     * @param orderActualCost
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
    public Order(String orderDate, Product product, Integer productCount, String orderBuyerName,
            Admin orderProxy, Float proxyProfitPercentage, Float orderActualCost,
            Float currencyRate, LogpiePackage package1, Integer estimatedShippingFee,
            Integer actualShippingFee, Integer sellingPrice, Integer customerPaidMoney,
            Integer finalProfit, Boolean isProfitPaid, String orderNote)
    {
        mOrderDate = orderDate;
        mOrderProduct = product;
        mOrderProductCount = productCount;
        mOrderBuyerName = orderBuyerName;
        mOrderProxy = orderProxy;
        mOrderProxyProfitPercentage = proxyProfitPercentage;
        mOrderActualCost = orderActualCost;
        mOrderCurrencyRate = currencyRate;
        mOrderPackage = package1;
        mOrderEstimatedShippingFee = estimatedShippingFee;
        mOrderActualShippingFee = actualShippingFee;
        mOrderSellingPrice = sellingPrice;
        mOrderCustomerPaidMoney = customerPaidMoney;
        mOrderFinalProfit = finalProfit;
        mOrderIsProfitPaid = isProfitPaid;
        mOrderNote = orderNote;
    }

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
    public Order(String orderId, String orderDate, Product product, Integer productCount,
            String orderBuyerName, Admin orderProxy, Float proxyProfitPercentage,
            Float orderActualCost, Float currencyRate, LogpiePackage package1,
            Integer estimatedShippingFee, Integer actualShippingFee, Integer sellingPrice,
            Integer customerPaidMoney, Integer finalProfit, Boolean isProfitPaid, String orderNote)
    {
        mOrderId = orderId;
        mOrderDate = orderDate;
        mOrderProduct = product;
        mOrderProductCount = productCount;
        mOrderBuyerName = orderBuyerName;
        mOrderProxy = orderProxy;
        mOrderProxyProfitPercentage = proxyProfitPercentage;
        mOrderActualCost = orderActualCost;
        mOrderCurrencyRate = currencyRate;
        mOrderPackage = package1;
        mOrderEstimatedShippingFee = estimatedShippingFee;
        mOrderActualShippingFee = actualShippingFee;
        mOrderSellingPrice = sellingPrice;
        mOrderCustomerPaidMoney = customerPaidMoney;
        mOrderFinalProfit = finalProfit;
        mOrderIsProfitPaid = isProfitPaid;
        mOrderNote = orderNote;
    }

    @Override
    public Order mapRow(final ResultSet rs, final int row) throws SQLException
    {
        return getOrderByResultSet(rs, row);
    }

    public static Order getOrderByResultSet(final ResultSet rs, final Integer row)
            throws SQLException
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
        final Integer productCount = rs.getInt(DB_KEY_ORDER_PRODUCT_COUNT);
        final String orderBuyerName = rs.getString(DB_KEY_ORDER_BUYER_NAME);
        final Admin orderProxy = Admin.getAdminByResultSet(rs, row);
        final Float proxyProfitPercentage = rs.getFloat(DB_KEY_ORDER_PROXY_PROFIT_PERCENTAGE);
        final Float orderActualCost = rs.getFloat(DB_KEY_ORDER_ACTUAL_COST);
        final Float currencyRate = rs.getFloat(DB_KEY_ORDER_CURRENCY_RATE);
        final LogpiePackage package1 = LogpiePackage.getLogpiePackageByResultSet(rs, row);
        final Integer estimatedShippingFee = rs.getInt(DB_KEY_ORDER_ESTIMATED_SHIPPING_FEE);
        final Integer actualShippingFee = rs.getInt(DB_KEY_ORDER_ACTUAL_SHIPPING_FEE);
        final Integer sellingPrice = rs.getInt(DB_KEY_ORDER_SELLING_PRICE);
        final Integer customerPaidMoney = rs.getInt(DB_KEY_ORDER_CUSTOMER_PAID_MONEY);
        final Integer finalProfit = rs.getInt(DB_KEY_ORDER_FINAL_PROFIT);
        final Boolean isProfitPaid = rs.getBoolean(DB_KEY_ORDER_IS_PROFIT_PAID);
        final String orderNote = rs.getString(DB_KEY_ORDER_NOTE);

        return new Order(orderId, orderDateString, product, productCount, orderBuyerName,
                orderProxy, proxyProfitPercentage, orderActualCost, currencyRate, package1,
                estimatedShippingFee, actualShippingFee, sellingPrice, customerPaidMoney,
                finalProfit, isProfitPaid, orderNote);
    }

    @Override
    public Map<String, Object> getModelMap()
    {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put(Order.DB_KEY_ORDER_DATE, mOrderDate);
        modelMap.put(Order.DB_KEY_ORDER_PRODUCT_ID, mOrderProduct.getProductId());
        modelMap.put(Order.DB_KEY_ORDER_PRODUCT_COUNT, mOrderProductCount);
        modelMap.put(Order.DB_KEY_ORDER_BUYER_NAME, mOrderBuyerName);
        modelMap.put(Order.DB_KEY_ORDER_PROXY_ID, mOrderProxy.getAdminId());
        modelMap.put(Order.DB_KEY_ORDER_PROXY_PROFIT_PERCENTAGE, mOrderProxyProfitPercentage);
        modelMap.put(Order.DB_KEY_ORDER_CURRENCY_RATE, mOrderCurrencyRate);
        modelMap.put(Order.DB_KEY_ORDER_PACKAGE_ID, mOrderPackage.getPackageId());
        modelMap.put(Order.DB_KEY_ORDER_ESTIMATED_SHIPPING_FEE, mOrderEstimatedShippingFee);
        modelMap.put(Order.DB_KEY_ORDER_ACTUAL_SHIPPING_FEE, mOrderActualShippingFee);
        modelMap.put(Order.DB_KEY_ORDER_SELLING_PRICE, mOrderSellingPrice);
        modelMap.put(Order.DB_KEY_ORDER_CUSTOMER_PAID_MONEY, mOrderCustomerPaidMoney);
        modelMap.put(Order.DB_KEY_ORDER_FINAL_PROFIT, mOrderFinalProfit);
        modelMap.put(Order.DB_KEY_ORDER_IS_PROFIT_PAID, mOrderIsProfitPaid);
        modelMap.put(Order.DB_KEY_ORDER_NOTE, mOrderNote);
        return modelMap;
    }

    public static Order readNewOrderFromRequest(final HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }
        final String orderDate = request.getParameter("OrderDate");
        final String orderProductId = request.getParameter("OrderProductId");
        final ProductDAO productDAO = new ProductDAO();
        final Product orderProduct = productDAO.getProductById(orderProductId);
        final Integer orderProductCount = Integer.parseInt(request
                .getParameter("OrderProductCount"));
        final String orderBuyerName = request.getParameter("OrderBuyerName");
        final String orderProxyId = request.getParameter("OrderProxyId");
        final AdminDAO adminDAO = new AdminDAO();
        final Admin orderProxy = adminDAO.queryAccountByAdminId(orderProxyId);
        final Float orderProxyProfitPercentage = Float.parseFloat(request
                .getParameter("OrderProxyProfitPercentage"));
        final Float orderActualCost = Float.parseFloat(request.getParameter("OrderActualCost"));
        final Float orderCurrencyRate = Float.parseFloat(request.getParameter("OrderCurrencyRate"));
        final String orderPackageId = request.getParameter("OrderPackageId");
        final LogpiePackageDAO packageDAO = new LogpiePackageDAO();
        final LogpiePackage orderPackage = packageDAO.getPackageById(orderPackageId);
        final Integer orderEstimatedShippingFee = Integer.parseInt(request
                .getParameter("OrderEstimatedShippingFee"));
        final Integer orderActualShippingFee = Integer.parseInt(request
                .getParameter("OrderActualShippingFee"));
        final Integer orderSellingPrice = Integer.parseInt(request
                .getParameter("OrderSellingPrice"));
        final Integer orderCustomerPaidMoney = Integer.parseInt(request
                .getParameter("OrderCustomerPaidMoney"));
        final Integer orderFinalProfit = Integer.parseInt(request.getParameter("OrderFinalProfit"));
        final Boolean orderIsProfitPaid = Boolean.parseBoolean(request
                .getParameter("OrderIsProfitPaid"));
        final String orderNote = request.getParameter("OrderNote");

        return new Order(orderDate, orderProduct, orderProductCount, orderBuyerName, orderProxy,
                orderProxyProfitPercentage, orderActualCost, orderCurrencyRate, orderPackage,
                orderEstimatedShippingFee, orderActualShippingFee, orderSellingPrice,
                orderCustomerPaidMoney, orderFinalProfit, orderIsProfitPaid, orderNote);
    }

    /**
     * @return the orderId
     */
    public String getOrderId()
    {
        return mOrderId;
    }

    /**
     * @param orderId
     *            the orderId to set
     */
    public void setOrderId(String orderId)
    {
        mOrderId = orderId;
    }

    /**
     * @return the orderDate
     */
    public String getOrderDate()
    {
        return mOrderDate;
    }

    /**
     * @param orderDate
     *            the orderDate to set
     */
    public void setOrderDate(String orderDate)
    {
        mOrderDate = orderDate;
    }

    /**
     * @return the orderProduct
     */
    public Product getOrderProduct()
    {
        return mOrderProduct;
    }

    /**
     * @param orderProduct
     *            the orderProduct to set
     */
    public void setOrderProduct(Product orderProduct)
    {
        mOrderProduct = orderProduct;
    }

    /**
     * @return the orderProductCount
     */
    public Integer getOrderProductCount()
    {
        return mOrderProductCount;
    }

    /**
     * @param orderProductCount
     *            the orderProductCount to set
     */
    public void setOrderProductCount(Integer orderProductCount)
    {
        mOrderProductCount = orderProductCount;
    }

    /**
     * @return the orderBuyerName
     */
    public String getOrderBuyerName()
    {
        return mOrderBuyerName;
    }

    /**
     * @param orderBuyerName
     *            the orderBuyerName to set
     */
    public void setOrderBuyerName(String orderBuyerName)
    {
        mOrderBuyerName = orderBuyerName;
    }

    /**
     * @return the orderProxy
     */
    public Admin getOrderProxy()
    {
        return mOrderProxy;
    }

    /**
     * @param orderProxy
     *            the orderProxy to set
     */
    public void setOrderProxy(Admin orderProxy)
    {
        mOrderProxy = orderProxy;
    }

    /**
     * @return the orderProxyProfitPercentage
     */
    public Float getOrderProxyProfitPercentage()
    {
        return mOrderProxyProfitPercentage;
    }

    /**
     * @param orderProxyProfitPercentage
     *            the orderProxyProfitPercentage to set
     */
    public void setOrderProxyProfitPercentage(Float orderProxyProfitPercentage)
    {
        mOrderProxyProfitPercentage = orderProxyProfitPercentage;
    }

    /**
     * @return the orderActualCost
     */
    public Float getOrderActualCost()
    {
        return mOrderActualCost;
    }

    /**
     * @param orderActualCost
     *            the orderActualCost to set
     */
    public void setOrderActualCost(Float orderActualCost)
    {
        mOrderActualCost = orderActualCost;
    }

    /**
     * @return the orderCurrencyRate
     */
    public Float getOrderCurrencyRate()
    {
        return mOrderCurrencyRate;
    }

    /**
     * @param orderCurrencyRate
     *            the orderCurrencyRate to set
     */
    public void setOrderCurrencyRate(Float orderCurrencyRate)
    {
        mOrderCurrencyRate = orderCurrencyRate;
    }

    /**
     * @return the orderPackage
     */
    public LogpiePackage getOrderPackage()
    {
        return mOrderPackage;
    }

    /**
     * @param orderPackage
     *            the orderPackage to set
     */
    public void setOrderPackage(LogpiePackage orderPackage)
    {
        mOrderPackage = orderPackage;
    }

    /**
     * @return the orderEstimatedShippingFee
     */
    public Integer getOrderEstimatedShippingFee()
    {
        return mOrderEstimatedShippingFee;
    }

    /**
     * @param orderEstimatedShippingFee
     *            the orderEstimatedShippingFee to set
     */
    public void setOrderEstimatedShippingFee(Integer orderEstimatedShippingFee)
    {
        mOrderEstimatedShippingFee = orderEstimatedShippingFee;
    }

    /**
     * @return the orderActualShippingFee
     */
    public Integer getOrderActualShippingFee()
    {
        return mOrderActualShippingFee;
    }

    /**
     * @param orderActualShippingFee
     *            the orderActualShippingFee to set
     */
    public void setOrderActualShippingFee(Integer orderActualShippingFee)
    {
        mOrderActualShippingFee = orderActualShippingFee;
    }

    /**
     * @return the orderSellingPrice
     */
    public Integer getOrderSellingPrice()
    {
        return mOrderSellingPrice;
    }

    /**
     * @param orderSellingPrice
     *            the orderSellingPrice to set
     */
    public void setOrderSellingPrice(Integer orderSellingPrice)
    {
        mOrderSellingPrice = orderSellingPrice;
    }

    /**
     * @return the orderCustomerPaidMoney
     */
    public Integer getOrderCustomerPaidMoney()
    {
        return mOrderCustomerPaidMoney;
    }

    /**
     * @param orderCustomerPaidMoney
     *            the orderCustomerPaidMoney to set
     */
    public void setOrderCustomerPaidMoney(Integer orderCustomerPaidMoney)
    {
        mOrderCustomerPaidMoney = orderCustomerPaidMoney;
    }

    /**
     * @return the orderFinalProfit
     */
    public Integer getOrderFinalProfit()
    {
        return mOrderFinalProfit;
    }

    /**
     * @param orderFinalProfit
     *            the orderFinalProfit to set
     */
    public void setOrderFinalProfit(Integer orderFinalProfit)
    {
        mOrderFinalProfit = orderFinalProfit;
    }

    /**
     * @return the orderIsProfitPaid
     */
    public Boolean getOrderIsProfitPaid()
    {
        return mOrderIsProfitPaid;
    }

    /**
     * @param orderIsProfitPaid
     *            the orderIsProfitPaid to set
     */
    public void setOrderIsProfitPaid(Boolean orderIsProfitPaid)
    {
        mOrderIsProfitPaid = orderIsProfitPaid;
    }

    /**
     * @return the orderNote
     */
    public String getOrderNote()
    {
        return mOrderNote;
    }

    /**
     * @param orderNote
     *            the orderNote to set
     */
    public void setOrderNote(String orderNote)
    {
        mOrderNote = orderNote;
    }

}
