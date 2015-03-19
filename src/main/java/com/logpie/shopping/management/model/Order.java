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
import org.springframework.util.StringUtils;

import com.logpie.shopping.management.storage.AdminDAO;
import com.logpie.shopping.management.storage.LogpiePackageDAO;
import com.logpie.shopping.management.storage.ProductDAO;
import com.logpie.shopping.management.util.NumberUtils;

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
    public static final String DB_KEY_ORDER_WEIGHT = "OrderWeight";
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
    // public static final String DB_KEY_ORDER_FINAL_PROFIT =
    // "OrderFinalProfit";
    public static final String DB_KEY_ORDER_COMPANY_RECEIVED_MONEY = "OrderCompanyReceivedMoney";
    public static final String DB_KEY_ORDER_IS_PROFIT_PAID = "OrderIsProfitPaid";
    public static final String DB_KEY_ORDER_NOTE = "OrderNote";

    private String mOrderId;
    private String mOrderDate;
    private Product mOrderProduct;
    private Integer mOrderProductCount;
    private Float mOrderWeight;
    private String mOrderBuyerName;
    private Admin mOrderProxy;
    private Float mOrderProxyProfitPercentage;
    private Float mOrderActualCost; // may be null
    private Float mOrderCurrencyRate;
    private LogpiePackage mOrderPackage;// may be null
    private Float mOrderEstimatedShippingFee;
    private Float mOrderActualShippingFee;// may be null
    private Float mOrderSellingPrice;
    private Float mOrderCustomerPaidMoney;// default to 0
    private Float mOrderFinalProfit;// may be null
    private Float mOrderFinalActualCost;
    private Float mOrderCompanyReceivedMoney; // may be null
    private Boolean mOrderIsProfitPaid; // default false
    private String mOrderNote;// may be null

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
    public Order(Product product, Integer productCount, Float orderWeight, String orderBuyerName,
            Admin orderProxy, Float proxyProfitPercentage, Float orderActualCost,
            Float currencyRate, LogpiePackage package1, Float estimatedShippingFee,
            Float actualShippingFee, Float sellingPrice, Float customerPaidMoney,
            Float orderCompanyReceivedMoney, Boolean isProfitPaid, String orderNote)
    {
        // OrderData is auto generated
        // mOrderDate = orderDate;
        mOrderProduct = product;
        mOrderProductCount = productCount;
        mOrderWeight = orderWeight;
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
        // mOrderFinalProfit = finalProfit;
        mOrderCompanyReceivedMoney = orderCompanyReceivedMoney;
        mOrderIsProfitPaid = isProfitPaid;
        mOrderNote = orderNote;

        if (mOrderCustomerPaidMoney != null && mOrderCurrencyRate != null
                && mOrderActualCost != null)
        {
            final String profitString = String.format(
                    "%.2f",
                    Float.valueOf(mOrderCustomerPaidMoney.intValue()
                            - mOrderCurrencyRate.floatValue() * mOrderActualCost.floatValue()
                            - mOrderActualShippingFee));
            mOrderFinalProfit = Float.valueOf(profitString);
        }
        mOrderFinalActualCost = mOrderCurrencyRate * mOrderActualCost + mOrderActualShippingFee;
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
            Float orderWeight, String orderBuyerName, Admin orderProxy,
            Float proxyProfitPercentage, Float orderActualCost, Float currencyRate,
            LogpiePackage package1, Float estimatedShippingFee, Float actualShippingFee,
            Float sellingPrice, Float customerPaidMoney, Float orderCompanyReceivedMoney,
            Boolean isProfitPaid, String orderNote)
    {
        mOrderId = orderId;
        mOrderDate = orderDate;
        mOrderProduct = product;
        mOrderProductCount = productCount;
        mOrderWeight = orderWeight;
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
        mOrderCompanyReceivedMoney = orderCompanyReceivedMoney;
        mOrderIsProfitPaid = isProfitPaid;
        mOrderNote = orderNote;

        if (mOrderCustomerPaidMoney != null && mOrderCurrencyRate != null
                && mOrderActualCost != null)
        {
            mOrderFinalProfit = NumberUtils.keepTwoDigitsDecimalForFloat(Float
                    .valueOf(mOrderCustomerPaidMoney.intValue() - mOrderCurrencyRate.floatValue()
                            * mOrderActualCost.floatValue() - mOrderActualShippingFee));

        }
        mOrderFinalActualCost = NumberUtils.keepTwoDigitsDecimalForFloat(mOrderCurrencyRate
                * mOrderActualCost + mOrderActualShippingFee);
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
        final Float orderWeight = rs.getFloat(DB_KEY_ORDER_WEIGHT);
        final String orderBuyerName = rs.getString(DB_KEY_ORDER_BUYER_NAME);
        final Admin orderProxy = Admin.getAdminByResultSet(rs, row);
        final Float proxyProfitPercentage = rs.getFloat(DB_KEY_ORDER_PROXY_PROFIT_PERCENTAGE);
        final Float orderActualCost = rs.getFloat(DB_KEY_ORDER_ACTUAL_COST);
        final Float currencyRate = rs.getFloat(DB_KEY_ORDER_CURRENCY_RATE);
        final Integer packageId = rs.getInt(DB_KEY_ORDER_PACKAGE_ID);
        LogpiePackage package1 = null;
        if (packageId != null)
        {
            final LogpiePackageDAO packageDAO = new LogpiePackageDAO();
            package1 = packageDAO.getPackageById(String.valueOf(packageId));
        }
        final Float estimatedShippingFee = rs.getFloat(DB_KEY_ORDER_ESTIMATED_SHIPPING_FEE);
        final Float actualShippingFee = rs.getFloat(DB_KEY_ORDER_ACTUAL_SHIPPING_FEE);
        final Float sellingPrice = rs.getFloat(DB_KEY_ORDER_SELLING_PRICE);
        final Float customerPaidMoney = rs.getFloat(DB_KEY_ORDER_CUSTOMER_PAID_MONEY);
        // final Integer finalProfit = rs.getInt(DB_KEY_ORDER_FINAL_PROFIT);
        final Float orderCompanyReceivedMoney = rs.getFloat(DB_KEY_ORDER_COMPANY_RECEIVED_MONEY);
        final Boolean isProfitPaid = rs.getBoolean(DB_KEY_ORDER_IS_PROFIT_PAID);
        final String orderNote = rs.getString(DB_KEY_ORDER_NOTE);

        return new Order(orderId, orderDateString, product, productCount, orderWeight,
                orderBuyerName, orderProxy, proxyProfitPercentage, orderActualCost, currencyRate,
                package1, estimatedShippingFee, actualShippingFee, sellingPrice, customerPaidMoney,
                orderCompanyReceivedMoney, isProfitPaid, orderNote);
    }

    @Override
    public Map<String, Object> getModelMap()
    {
        final Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put(Order.DB_KEY_ORDER_DATE, mOrderDate);
        modelMap.put(Order.DB_KEY_ORDER_PRODUCT_ID, mOrderProduct.getProductId());
        modelMap.put(Order.DB_KEY_ORDER_PRODUCT_COUNT, mOrderProductCount);
        modelMap.put(Order.DB_KEY_ORDER_WEIGHT, mOrderWeight);
        modelMap.put(Order.DB_KEY_ORDER_BUYER_NAME, mOrderBuyerName);
        modelMap.put(Order.DB_KEY_ORDER_PROXY_ID, mOrderProxy.getAdminId());
        modelMap.put(Order.DB_KEY_ORDER_PROXY_PROFIT_PERCENTAGE, mOrderProxyProfitPercentage);
        // if (mOrderActualCost != null)
        // {
        modelMap.put(Order.DB_KEY_ORDER_ACTUAL_COST, mOrderActualCost);
        // }
        modelMap.put(Order.DB_KEY_ORDER_CURRENCY_RATE, mOrderCurrencyRate);
        if (mOrderPackage != null)
        {
            modelMap.put(Order.DB_KEY_ORDER_PACKAGE_ID, mOrderPackage.getPackageId());
        }
        else
        {
            modelMap.put(Order.DB_KEY_ORDER_PACKAGE_ID, null);
        }
        modelMap.put(Order.DB_KEY_ORDER_ESTIMATED_SHIPPING_FEE, mOrderEstimatedShippingFee);
        // if (mOrderActualShippingFee != null)
        // {
        modelMap.put(Order.DB_KEY_ORDER_ACTUAL_SHIPPING_FEE, mOrderActualShippingFee);
        // }
        modelMap.put(Order.DB_KEY_ORDER_SELLING_PRICE, mOrderSellingPrice);
        modelMap.put(Order.DB_KEY_ORDER_CUSTOMER_PAID_MONEY, mOrderCustomerPaidMoney);
        // modelMap.put(Order.DB_KEY_ORDER_FINAL_PROFIT, mOrderFinalProfit);
        modelMap.put(Order.DB_KEY_ORDER_COMPANY_RECEIVED_MONEY, mOrderCompanyReceivedMoney);
        modelMap.put(Order.DB_KEY_ORDER_IS_PROFIT_PAID, mOrderIsProfitPaid);
        // if (mOrderNote != null)
        // {
        modelMap.put(Order.DB_KEY_ORDER_NOTE, mOrderNote);
        // }
        return modelMap;
    }

    public static Order readNewOrderFromRequest(final HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }
        // OrderData is auto generated
        // final String orderDate = request.getParameter("OrderDate");
        final String orderProductId = request.getParameter("OrderProductId");
        final ProductDAO productDAO = new ProductDAO();
        final Product orderProduct = productDAO.getProductById(orderProductId);
        final Integer orderProductCount = Integer.parseInt(request
                .getParameter("OrderProductCount"));
        final Float orderWeight = Float.parseFloat(request.getParameter("OrderWeight"));
        final String orderBuyerName = request.getParameter("OrderBuyerName");
        final String orderProxyId = request.getParameter("OrderProxyId");
        final AdminDAO adminDAO = new AdminDAO();
        final Admin orderProxy = adminDAO.queryAccountByAdminId(orderProxyId);
        final Float orderProxyProfitPercentage = Float.parseFloat(request
                .getParameter("OrderProxyProfitPercentage"));
        // OrderActualCost may be null
        Float orderActualCost = null;
        final String orderActualCostString = request.getParameter("OrderActualCost");
        if (!StringUtils.isEmpty(orderActualCostString))
        {
            orderActualCost = Float.parseFloat(request.getParameter("OrderActualCost"));
        }
        final Float orderCurrencyRate = Float.parseFloat(request.getParameter("OrderCurrencyRate"));

        // PackageId may be null
        LogpiePackage orderPackage = null;
        final String orderPackageId = request.getParameter("OrderPackageId");
        if (!StringUtils.isEmpty(orderPackageId))
        {
            final LogpiePackageDAO packageDAO = new LogpiePackageDAO();
            orderPackage = packageDAO.getPackageById(orderPackageId);
        }
        final Float orderEstimatedShippingFee = Float.parseFloat(request
                .getParameter("OrderEstimatedShippingFee"));

        // ActualShipping Fee may be null
        Float orderActualShippingFee = null;
        final String orderActualShippingFeeString = request.getParameter("OrderActualShippingFee");
        if (!StringUtils.isEmpty(orderActualShippingFeeString))
        {
            orderActualShippingFee = Float.parseFloat(request
                    .getParameter("OrderActualShippingFee"));
        }
        else
        {
            // default to 0
            orderActualShippingFee = 0.0f;
        }
        final Float orderSellingPrice = Float.parseFloat(request.getParameter("OrderSellingPrice"));
        final Float orderCustomerPaidMoney = Float.parseFloat(request
                .getParameter("OrderCustomerPaidMoney"));
        // final Integer orderFinalProfit =
        // Integer.parseInt(request.getParameter("OrderFinalProfit"));
        final Float orderCompanyReceivedMoney = Float.parseFloat(request
                .getParameter("OrderCompanyReceivedMoney"));
        final Boolean orderIsProfitPaid = Boolean.parseBoolean(request
                .getParameter("OrderIsProfitPaid"));

        // orderNote may be null
        final String orderNote = request.getParameter("OrderNote");

        return new Order(orderProduct, orderProductCount, orderWeight, orderBuyerName, orderProxy,
                orderProxyProfitPercentage, orderActualCost, orderCurrencyRate, orderPackage,
                orderEstimatedShippingFee, orderActualShippingFee, orderSellingPrice,
                orderCustomerPaidMoney, orderCompanyReceivedMoney, orderIsProfitPaid, orderNote);
    }

    public static Order readModifiedOrderFromRequest(final HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }
        final String orderId = request.getParameter("OrderId");
        final String orderDate = request.getParameter("OrderDate");
        // OrderData is auto generated
        // final String orderDate = request.getParameter("OrderDate");
        final String orderProductId = request.getParameter("OrderProductId");
        final ProductDAO productDAO = new ProductDAO();
        final Product orderProduct = productDAO.getProductById(orderProductId);
        final Integer orderProductCount = Integer.parseInt(request
                .getParameter("OrderProductCount"));
        final Float orderWeight = Float.parseFloat(request.getParameter("OrderWeight"));
        final String orderBuyerName = request.getParameter("OrderBuyerName");
        final String orderProxyId = request.getParameter("OrderProxyId");
        final AdminDAO adminDAO = new AdminDAO();
        final Admin orderProxy = adminDAO.queryAccountByAdminId(orderProxyId);
        final Float orderProxyProfitPercentage = Float.parseFloat(request
                .getParameter("OrderProxyProfitPercentage"));
        // OrderActualCost may be null
        Float orderActualCost = null;
        final String orderActualCostString = request.getParameter("OrderActualCost");
        if (!StringUtils.isEmpty(orderActualCostString))
        {
            orderActualCost = Float.parseFloat(request.getParameter("OrderActualCost"));
        }
        final Float orderCurrencyRate = Float.parseFloat(request.getParameter("OrderCurrencyRate"));
        final String orderPackageId = request.getParameter("OrderPackageId");
        final LogpiePackageDAO packageDAO = new LogpiePackageDAO();
        final LogpiePackage orderPackage = packageDAO.getPackageById(orderPackageId);
        final Float orderEstimatedShippingFee = Float.parseFloat(request
                .getParameter("OrderEstimatedShippingFee"));
        // ActualShipping Fee may be null
        Float orderActualShippingFee = null;
        final String orderActualShippingFeeString = request.getParameter("OrderActualShippingFee");
        if (!StringUtils.isEmpty(orderActualShippingFeeString))
        {
            orderActualShippingFee = Float.parseFloat(request
                    .getParameter("OrderActualShippingFee"));
        }
        final Float orderSellingPrice = Float.parseFloat(request.getParameter("OrderSellingPrice"));
        final Float orderCustomerPaidMoney = Float.parseFloat(request
                .getParameter("OrderCustomerPaidMoney"));
        // final Integer orderFinalProfit =
        // Integer.parseInt(request.getParameter("OrderFinalProfit"));
        final Float orderCompanyReceivedMoney = Float.parseFloat(request
                .getParameter("OrderCompanyReceivedMoney"));
        final Boolean orderIsProfitPaid = Boolean.parseBoolean(request
                .getParameter("OrderIsProfitPaid"));
        // orderNote may be null
        final String orderNote = request.getParameter("OrderNote");

        return new Order(orderId, orderDate, orderProduct, orderProductCount, orderWeight,
                orderBuyerName, orderProxy, orderProxyProfitPercentage, orderActualCost,
                orderCurrencyRate, orderPackage, orderEstimatedShippingFee, orderActualShippingFee,
                orderSellingPrice, orderCustomerPaidMoney, orderCompanyReceivedMoney,
                orderIsProfitPaid, orderNote);
    }

    // 订单结算
    public void settleDown()
    {
        this.mOrderCustomerPaidMoney = this.mOrderSellingPrice;
        this.mOrderCompanyReceivedMoney = this.mOrderSellingPrice;
        this.mOrderIsProfitPaid = true;
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
     * @return the orderWeight
     */
    public Float getOrderWeight()
    {
        return mOrderWeight;
    }

    /**
     * @param orderWeight
     *            the orderWeight to set
     */
    public void setOrderWeight(Float orderWeight)
    {
        mOrderWeight = orderWeight;
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
    public void setOrderActualCost(Float actualCost)
    {
        mOrderActualCost = actualCost;
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
    public Float getOrderEstimatedShippingFee()
    {
        return mOrderEstimatedShippingFee;
    }

    /**
     * @param orderEstimatedShippingFee
     *            the orderEstimatedShippingFee to set
     */
    public void setOrderEstimatedShippingFee(Float orderEstimatedShippingFee)
    {
        mOrderEstimatedShippingFee = orderEstimatedShippingFee;
    }

    /**
     * @return the orderActualShippingFee
     */
    public Float getOrderActualShippingFee()
    {
        return mOrderActualShippingFee;
    }

    /**
     * @param orderActualShippingFee
     *            the orderActualShippingFee to set
     */
    public void setOrderActualShippingFee(Float orderActualShippingFee)
    {
        mOrderActualShippingFee = orderActualShippingFee;
    }

    /**
     * @return the orderSellingPrice
     */
    public Float getOrderSellingPrice()
    {
        return mOrderSellingPrice;
    }

    /**
     * @param orderSellingPrice
     *            the orderSellingPrice to set
     */
    public void setOrderSellingPrice(Float orderSellingPrice)
    {
        mOrderSellingPrice = orderSellingPrice;
    }

    /**
     * @return the orderCustomerPaidMoney
     */
    public Float getOrderCustomerPaidMoney()
    {
        return mOrderCustomerPaidMoney;
    }

    /**
     * @param orderCustomerPaidMoney
     *            the orderCustomerPaidMoney to set
     */
    public void setOrderCustomerPaidMoney(Float orderCustomerPaidMoney)
    {
        mOrderCustomerPaidMoney = orderCustomerPaidMoney;
    }

    /**
     * @return the orderFinalProfit
     */
    public Float getOrderFinalProfit()
    {
        return mOrderFinalProfit;
    }

    /**
     * @param orderFinalProfit
     *            the orderFinalProfit to set
     */
    public void setOrderFinalProfit(Float orderFinalProfit)
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

    /**
     * @return the orderCompanyReceivedMoney
     */
    public Float getOrderCompanyReceivedMoney()
    {
        return mOrderCompanyReceivedMoney;
    }

    /**
     * @param orderCompanyReceivedMoney
     *            the orderCompanyReceivedMoney to set
     */
    public void setOrderCompanyReceivedMoney(Float orderCompanyReceivedMoney)
    {
        mOrderCompanyReceivedMoney = orderCompanyReceivedMoney;
    }

    @Override
    public String getPrimaryKey()
    {
        return DB_KEY_ORDER_ID;
    }

    /**
     * @return the orderFinalCost
     */
    public Float getOrderFinalActualCost()
    {
        return mOrderFinalActualCost;
    }

    /**
     * @param orderFinalCost
     *            the orderFinalCost to set
     */
    public void setOrderFinalActualCost(Float orderFinalActualCost)
    {
        mOrderFinalActualCost = orderFinalActualCost;
    }

}
