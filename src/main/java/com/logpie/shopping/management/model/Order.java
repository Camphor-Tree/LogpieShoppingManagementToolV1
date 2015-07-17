// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import com.logpie.shopping.management.storage.AdminDAO;
import com.logpie.shopping.management.storage.ClientDAO;
import com.logpie.shopping.management.storage.LogpiePackageDAO;
import com.logpie.shopping.management.storage.ProductDAO;
import com.logpie.shopping.management.util.NumberUtils;
import com.logpie.shopping.settings.SettingManager;
import com.logpie.shopping.settings.SettingManager.SystemSettingKeys;

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
    public static final String DB_KEY_ORDER_DOMESTIC_SHIPPING_FEE = "OrderDomesticShippingFee";
    public static final String DB_KEY_ORDER_CUSTOMER_PAID_DOMESTIC_SHIPPING_FEE = "OrderCustomerPaidDomesticShippingFee";
    public static final String DB_KEY_ORDER_SELLING_PRICE = "OrderSellingPrice";
    public static final String DB_KEY_ORDER_CUSTOMER_PAID_MONEY = "OrderCustomerPaidMoney";
    // public static final String DB_KEY_ORDER_FINAL_PROFIT =
    // "OrderFinalProfit";
    public static final String DB_KEY_ORDER_COMPANY_RECEIVED_MONEY = "OrderCompanyReceivedMoney";
    public static final String DB_KEY_ORDER_IS_PROFIT_PAID = "OrderIsProfitPaid";
    public static final String DB_KEY_ORDER_SENT_TO_USER = "OrderSentToUser";
    public static final String DB_KEY_ORDER_CLIENT_ID = "OrderClientId";
    public static final String DB_KEY_ORDER_NOTE = "OrderNote";

    private static final Logger LOG = Logger.getLogger(Order.class);

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
    private Float mOrderDomesticShippingFee;
    private Float mOrderCustomerPaidDomesticShippingFee;
    private Float mOrderActualShippingFee;// may be null
    private Float mOrderSellingPrice;
    private Float mOrderCustomerPaidMoney;// default to 0
    private Float mOrderFinalProfit;// may be null
    private Float mOrderFinalActualCost;
    private Float mOrderCompanyReceivedMoney; // may be null
    private Boolean mOrderIsProfitPaid; // default false
    private Boolean mOrderSentToUser; // default false
    private Client mOrderClient;
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
     * @ppram domesticShippingFee
     * @param customerPaidDomesticShippingFee
     * @param sellingPrice
     * @param customerPaidMoney
     * @param finalProfit
     * @param isProfitPaid
     * @param orderSentToUser
     * @param orderClient
     * @param orderNote
     */
    public Order(Product product, Integer productCount, Float orderWeight, String orderBuyerName,
            Admin orderProxy, Float proxyProfitPercentage, Float orderActualCost,
            Float currencyRate, LogpiePackage package1, Float estimatedShippingFee,
            Float actualShippingFee, Float orderDomesticShippingFee,
            Float orderCustomerPaidDomesticShippingFee, Float sellingPrice, Float customerPaidMoney,
            Float orderCompanyReceivedMoney, Boolean isProfitPaid, Boolean orderSentToUser,
            Client orderClient, String orderNote)
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
        mOrderDomesticShippingFee = orderDomesticShippingFee;
        mOrderCustomerPaidDomesticShippingFee = orderCustomerPaidDomesticShippingFee;
        mOrderSellingPrice = sellingPrice;
        mOrderCustomerPaidMoney = customerPaidMoney;
        // mOrderFinalProfit = finalProfit;
        mOrderCompanyReceivedMoney = orderCompanyReceivedMoney;
        mOrderIsProfitPaid = isProfitPaid;
        mOrderSentToUser = orderSentToUser;
        mOrderClient = orderClient;
        mOrderNote = orderNote;

        refreshOrderFinalProfit();
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
            Float orderWeight, String orderBuyerName, Admin orderProxy, Float proxyProfitPercentage,
            Float orderActualCost, Float currencyRate, LogpiePackage package1,
            Float estimatedShippingFee, Float actualShippingFee, Float orderDomesticShippingFee,
            Float orderCustomerPaidDomesticShippingFee, Float sellingPrice, Float customerPaidMoney,
            Float orderCompanyReceivedMoney, Boolean isProfitPaid, Boolean orderSentToUser,
            Client orderClient, String orderNote)
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
        mOrderDomesticShippingFee = orderDomesticShippingFee;
        mOrderCustomerPaidDomesticShippingFee = orderCustomerPaidDomesticShippingFee;
        mOrderSellingPrice = sellingPrice;
        mOrderCustomerPaidMoney = customerPaidMoney;
        mOrderCompanyReceivedMoney = orderCompanyReceivedMoney;
        mOrderIsProfitPaid = isProfitPaid;
        mOrderSentToUser = orderSentToUser;
        mOrderClient = orderClient;
        mOrderNote = orderNote;

        refreshOrderFinalProfit();
    }

    /**
     * This is to refresh the final actual profit. Since it is a calculation
     * member variable. Every time
     * mOrderCurrencyRate,mOrderActualCost,mOrderActualShippingFee
     * ,mOrderDomesticShippingFee changes, should call this method to
     * recalculate the final actual cost. This call internally calls
     * refreshOrderFinalActualCost();
     */
    private void refreshOrderFinalProfit()
    {
        refreshOrderFinalActualCost();
        if (mOrderCustomerPaidMoney != null && mOrderCurrencyRate != null
                && mOrderActualCost != null)
        {
            mOrderFinalProfit = NumberUtils
                    .keepTwoDigitsDecimalForFloat(mOrderCustomerPaidMoney - mOrderFinalActualCost);
        }
    }

    /**
     * This is to refresh the final actual cost. Since it is a calculation
     * member variable. Every time
     * mOrderCurrencyRate,mOrderActualCost,mOrderActualShippingFee
     * ,mOrderDomesticShippingFee changes, should call this method to
     * recalculate the final actual cost.
     */
    private void refreshOrderFinalActualCost()
    {
        mOrderFinalActualCost = NumberUtils
                .keepTwoDigitsDecimalForFloat(mOrderCurrencyRate * mOrderActualCost
                        + mOrderActualShippingFee + mOrderDomesticShippingFee);
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
        final LogpiePackage package1 = LogpiePackage.getLogpiePackageByResultSet(rs, row);
        final Float estimatedShippingFee = rs.getFloat(DB_KEY_ORDER_ESTIMATED_SHIPPING_FEE);
        final Float actualShippingFee = rs.getFloat(DB_KEY_ORDER_ACTUAL_SHIPPING_FEE);
        final Float orderDomesticShippingFee = rs.getFloat(DB_KEY_ORDER_DOMESTIC_SHIPPING_FEE);
        final Float orderCustomerPaidDomesticShippingFee = rs
                .getFloat(DB_KEY_ORDER_CUSTOMER_PAID_DOMESTIC_SHIPPING_FEE);
        final Float sellingPrice = rs.getFloat(DB_KEY_ORDER_SELLING_PRICE);
        final Float customerPaidMoney = rs.getFloat(DB_KEY_ORDER_CUSTOMER_PAID_MONEY);
        // final Integer finalProfit = rs.getInt(DB_KEY_ORDER_FINAL_PROFIT);
        final Float orderCompanyReceivedMoney = rs.getFloat(DB_KEY_ORDER_COMPANY_RECEIVED_MONEY);
        final Boolean isProfitPaid = rs.getBoolean(DB_KEY_ORDER_IS_PROFIT_PAID);
        final Boolean orderSentToUser = rs.getBoolean(DB_KEY_ORDER_SENT_TO_USER);
        final Client orderClient = Client.getClientByResultSet(rs, row);
        final String orderNote = rs.getString(DB_KEY_ORDER_NOTE);

        return new Order(orderId, orderDateString, product, productCount, orderWeight,
                orderBuyerName, orderProxy, proxyProfitPercentage, orderActualCost, currencyRate,
                package1, estimatedShippingFee, actualShippingFee, orderDomesticShippingFee,
                orderCustomerPaidDomesticShippingFee, sellingPrice, customerPaidMoney,
                orderCompanyReceivedMoney, isProfitPaid, orderSentToUser, orderClient, orderNote);
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
        modelMap.put(Order.DB_KEY_ORDER_ACTUAL_SHIPPING_FEE, mOrderActualShippingFee);
        modelMap.put(Order.DB_KEY_ORDER_DOMESTIC_SHIPPING_FEE, mOrderDomesticShippingFee);
        modelMap.put(Order.DB_KEY_ORDER_CUSTOMER_PAID_DOMESTIC_SHIPPING_FEE,
                mOrderCustomerPaidDomesticShippingFee);
        modelMap.put(Order.DB_KEY_ORDER_SELLING_PRICE, mOrderSellingPrice);
        modelMap.put(Order.DB_KEY_ORDER_CUSTOMER_PAID_MONEY, mOrderCustomerPaidMoney);
        // modelMap.put(Order.DB_KEY_ORDER_FINAL_PROFIT, mOrderFinalProfit);
        modelMap.put(Order.DB_KEY_ORDER_COMPANY_RECEIVED_MONEY, mOrderCompanyReceivedMoney);
        modelMap.put(Order.DB_KEY_ORDER_IS_PROFIT_PAID, mOrderIsProfitPaid);
        modelMap.put(Order.DB_KEY_ORDER_SENT_TO_USER, mOrderSentToUser);
        if (mOrderClient != null)
        {
            modelMap.put(Order.DB_KEY_ORDER_CLIENT_ID, mOrderClient.getClientId());
        }
        else
        {
            modelMap.put(Order.DB_KEY_ORDER_CLIENT_ID, null);
        }
        modelMap.put(Order.DB_KEY_ORDER_NOTE, mOrderNote);
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
        final ProductDAO productDAO = new ProductDAO(null);
        final Product orderProduct = productDAO.getProductById(orderProductId);
        final Integer orderProductCount = Integer
                .parseInt(request.getParameter("OrderProductCount"));
        final Float orderWeight = Float.parseFloat(request.getParameter("OrderWeight"));
        final String orderBuyerName = request.getParameter("OrderBuyerName");
        final String orderProxyId = request.getParameter("OrderProxyId");
        final AdminDAO adminDAO = new AdminDAO(null);
        final Admin orderProxy = adminDAO.queryAccountByAdminId(orderProxyId);
        final Float orderProxyProfitPercentage;
        // only super admin's orderProxyProfitPercentage is 0, normal admin will
        // use default system settings' profit percentage
        if (orderProxy.isSuperAdmin())
        {
            orderProxyProfitPercentage = 0.0f;
        }
        else
        {
            // 从设置中读出默认的分红百分比。
            orderProxyProfitPercentage = Float.parseFloat(SettingManager.getInstance()
                    .getSystemSetting(SystemSettingKeys.SYSTEM_DEFAULT_PROXY_PROFIT_PERCENTAGE));
        }
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
            final LogpiePackageDAO packageDAO = new LogpiePackageDAO(null);
            orderPackage = packageDAO.getPackageById(orderPackageId);
        }
        final Float orderEstimatedShippingFee = Float
                .parseFloat(request.getParameter("OrderEstimatedShippingFee"));

        // ActualShipping Fee may be null
        Float orderActualShippingFee = null;
        final String orderActualShippingFeeString = request.getParameter("OrderActualShippingFee");
        if (!StringUtils.isEmpty(orderActualShippingFeeString))
        {
            orderActualShippingFee = Float
                    .parseFloat(request.getParameter("OrderActualShippingFee"));
        }
        else
        {
            // default to 0
            orderActualShippingFee = 0.0f;
        }

        final Float orderDomesticShippingFee = Float
                .parseFloat(request.getParameter("OrderDomesticShippingFee"));
        final Float orderCustomerPaidDomesticShippingFee = Float
                .parseFloat(request.getParameter("OrderCustomerPaidDomesticShippingFee"));

        final Float orderSellingPrice = Float.parseFloat(request.getParameter("OrderSellingPrice"));
        final Float orderCustomerPaidMoney = Float
                .parseFloat(request.getParameter("OrderCustomerPaidMoney"));
        // final Integer orderFinalProfit =
        // Integer.parseInt(request.getParameter("OrderFinalProfit"));
        final Float orderCompanyReceivedMoney = Float
                .parseFloat(request.getParameter("OrderCompanyReceivedMoney"));
        final Boolean orderIsProfitPaid = Boolean
                .parseBoolean(request.getParameter("OrderIsProfitPaid"));
        final Boolean orderSentToUser = Boolean
                .parseBoolean(request.getParameter("OrderSentToUser"));

        // ClientId may be null
        Client orderClient = null;
        final String orderClientId = request.getParameter("OrderClientId");
        if (!StringUtils.isEmpty(orderClientId))
        {
            final ClientDAO clientDAO = new ClientDAO(null);
            orderClient = clientDAO.getClientById(orderClientId);
        }
        // orderNote may be null
        final String orderNote = request.getParameter("OrderNote");

        return new Order(orderProduct, orderProductCount, orderWeight, orderBuyerName, orderProxy,
                orderProxyProfitPercentage, orderActualCost, orderCurrencyRate, orderPackage,
                orderEstimatedShippingFee, orderActualShippingFee, orderDomesticShippingFee,
                orderCustomerPaidDomesticShippingFee, orderSellingPrice, orderCustomerPaidMoney,
                orderCompanyReceivedMoney, orderIsProfitPaid, orderSentToUser, orderClient,
                orderNote);
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
        final ProductDAO productDAO = new ProductDAO(null);
        final Product orderProduct = productDAO.getProductById(orderProductId);
        final Integer orderProductCount = Integer
                .parseInt(request.getParameter("OrderProductCount"));
        final Float orderWeight = Float.parseFloat(request.getParameter("OrderWeight"));
        final String orderBuyerName = request.getParameter("OrderBuyerName");
        final String orderProxyId = request.getParameter("OrderProxyId");
        final AdminDAO adminDAO = new AdminDAO(null);
        final Admin orderProxy = adminDAO.queryAccountByAdminId(orderProxyId);
        final Float orderProxyProfitPercentage;
        // only super admin's orderProxyProfitPercentage is 0, normal admin will
        // use default system settings profit percentage
        if (orderProxy.isSuperAdmin())
        {
            orderProxyProfitPercentage = 0.0f;
        }
        else
        {
            // 从设置中读出默认的分红百分比。
            orderProxyProfitPercentage = Float.parseFloat(SettingManager.getInstance()
                    .getSystemSetting(SystemSettingKeys.SYSTEM_DEFAULT_PROXY_PROFIT_PERCENTAGE));
        }
        // OrderActualCost may be null
        Float orderActualCost = null;
        final String orderActualCostString = request.getParameter("OrderActualCost");
        if (!StringUtils.isEmpty(orderActualCostString))
        {
            orderActualCost = Float.parseFloat(request.getParameter("OrderActualCost"));
        }
        final Float orderCurrencyRate = Float.parseFloat(request.getParameter("OrderCurrencyRate"));
        final String orderPackageId = request.getParameter("OrderPackageId");
        final LogpiePackageDAO packageDAO = new LogpiePackageDAO(null);
        final LogpiePackage orderPackage = packageDAO.getPackageById(orderPackageId);
        final Float orderEstimatedShippingFee = Float
                .parseFloat(request.getParameter("OrderEstimatedShippingFee"));
        // ActualShipping Fee may be null
        Float orderActualShippingFee = null;
        final String orderActualShippingFeeString = request.getParameter("OrderActualShippingFee");
        if (!StringUtils.isEmpty(orderActualShippingFeeString))
        {
            orderActualShippingFee = Float
                    .parseFloat(request.getParameter("OrderActualShippingFee"));
        }
        final Float orderDomesticShippingFee = Float
                .parseFloat(request.getParameter("OrderDomesticShippingFee"));
        final Float orderCustomerPaidDomesticShippingFee = Float
                .parseFloat(request.getParameter("OrderCustomerPaidDomesticShippingFee"));
        final Float orderSellingPrice = Float.parseFloat(request.getParameter("OrderSellingPrice"));
        final Float orderCustomerPaidMoney = Float
                .parseFloat(request.getParameter("OrderCustomerPaidMoney"));
        // final Integer orderFinalProfit =
        // Integer.parseInt(request.getParameter("OrderFinalProfit"));
        final Float orderCompanyReceivedMoney = Float
                .parseFloat(request.getParameter("OrderCompanyReceivedMoney"));
        final Boolean orderIsProfitPaid = Boolean
                .parseBoolean(request.getParameter("OrderIsProfitPaid"));
        final Boolean orderSentToUser = Boolean
                .parseBoolean(request.getParameter("OrderSentToUser"));

        // orderClient may be null
        final String orderClientId = request.getParameter("OrderClientId");
        final ClientDAO clientDAO = new ClientDAO(null);
        final Client orderClient = clientDAO.getClientById(orderClientId);
        // orderNote may be null
        final String orderNote = request.getParameter("OrderNote");

        return new Order(orderId, orderDate, orderProduct, orderProductCount, orderWeight,
                orderBuyerName, orderProxy, orderProxyProfitPercentage, orderActualCost,
                orderCurrencyRate, orderPackage, orderEstimatedShippingFee, orderActualShippingFee,
                orderDomesticShippingFee, orderCustomerPaidDomesticShippingFee, orderSellingPrice,
                orderCustomerPaidMoney, orderCompanyReceivedMoney, orderIsProfitPaid,
                orderSentToUser, orderClient, orderNote);
    }

    // Get the problem reason string
    public String getOrderProblemReason()
    {
        final StringBuilder problemReasonBuilder = new StringBuilder();
        // 所有可能的问题
        // 1. 总成本（购买成本＋国际运费＋国内运费）小于0
        if (NumberUtils.floatLessThan(mOrderFinalActualCost, 0.0f))
        {
            problemReasonBuilder.append("总成本（购买成本＋国际运费＋国内运费）小于0。");
        }

        // 2. 重量小于0.
        if (NumberUtils.floatLessThan(this.mOrderWeight, 0.0f))
        {
            problemReasonBuilder.append("重量小于0。");
        }

        // 3. 买家付款 不等于 商品卖价＋用户已付国内邮费
        if (!NumberUtils.floatEquals(this.mOrderCustomerPaidMoney,
                this.mOrderSellingPrice + this.mOrderCustomerPaidDomesticShippingFee))
        {
            problemReasonBuilder.append("买家付款 不等于 商品卖价＋用户已付国内邮费。");
        }

        // 4. 数量小于1
        if (this.mOrderProductCount < 1)
        {
            problemReasonBuilder.append("数量小于1。");
        }

        // 5. 超级管理员 订单分红百分比不为0
        if (this.mOrderProxy.isSuperAdmin() && this.mOrderProxyProfitPercentage != 0)
        {
            problemReasonBuilder.append("超级管理员 订单分红百分比不为0。");
        }

        // 6. 普通管理员 订单分红百分比为0
        if (!this.mOrderProxy.isSuperAdmin()
                && !NumberUtils.floatEquals(this.mOrderProxyProfitPercentage, 0.0f))
        {
            problemReasonBuilder.append("普通管理员 订单分红百分比为0。");
        }

        return problemReasonBuilder.toString();
    }

    // Triage whether the order has some problem (calculation problem or user
    // input typo)
    public Boolean getOrderHasProblem()
    {
        // 所有可能的问题
        // 1. 总成本（购买成本＋国际运费＋国内运费）小于0
        if (NumberUtils.floatLessThan(mOrderFinalActualCost, 0.0f))
        {
            return true;
        }

        // 2. 重量小于0.
        if (NumberUtils.floatLessThan(this.mOrderWeight, 0.0f))
        {
            return true;
        }

        // 3. 买家付款 不等于 商品卖价＋用户已付国内邮费
        if (!NumberUtils.floatEquals(this.mOrderCustomerPaidMoney,
                this.mOrderSellingPrice + this.mOrderCustomerPaidDomesticShippingFee))
        {
            return true;
        }

        // 4. 数量小于1
        if (this.mOrderProductCount < 1)
        {
            return true;
        }

        // 5. 超级管理员 订单分红百分比不为0
        if (this.mOrderProxy.isSuperAdmin() && this.mOrderProxyProfitPercentage != 0)
        {
            return true;
        }

        // 6. 普通管理员 订单分红百分比为0
        if (!this.mOrderProxy.isSuperAdmin()
                && !NumberUtils.floatEquals(this.mOrderProxyProfitPercentage, 0.0f))
        {
            return true;
        }

        return false;
    }

    // 订单结算
    public void settleDown()
    {
        // 公司收款应等于用户付的钱减去代理垫付的钱(国内运费)
        this.mOrderCompanyReceivedMoney = this.mOrderCustomerPaidMoney
                - this.mOrderDomesticShippingFee;
        this.mOrderIsProfitPaid = true;
    }

    public String getDeltaChange(final Order compareToOrder)
    {
        if (compareToOrder == null || !compareToOrder.getOrderId().equals(mOrderId))
        {
            return null;
        }

        if (compareTo(compareToOrder))
        {
            // Order doesn't change.
            return null;
        }
        final StringBuilder changeStringBuilder = new StringBuilder();

        changeStringBuilder.append("改动订单:" + mOrderId + " ");

        if (!compareToOrder.mOrderIsProfitPaid.equals(mOrderIsProfitPaid))
        {
            changeStringBuilder.append("利润已付给代理：" + compareToOrder.mOrderIsProfitPaid + "->"
                    + mOrderIsProfitPaid + " ");
        }
        if (!compareToOrder.mOrderActualCost.equals(mOrderActualCost))
        {
            changeStringBuilder.append(
                    "购买成本：" + compareToOrder.mOrderActualCost + "->" + mOrderActualCost + " ");
        }
        if (!compareToOrder.mOrderActualShippingFee.equals(mOrderActualShippingFee))
        {
            changeStringBuilder.append("订单国际运费：" + compareToOrder.mOrderActualShippingFee + "->"
                    + mOrderActualShippingFee + " ");
        }
        if (!compareToOrder.mOrderBuyerName.equals(mOrderBuyerName))
        {
            changeStringBuilder.append(
                    "订单购买者：" + compareToOrder.mOrderBuyerName + "->" + mOrderBuyerName + " ");
        }
        if (!compareToOrder.mOrderCompanyReceivedMoney.equals(mOrderCompanyReceivedMoney))
        {
            changeStringBuilder.append("公司已收汇款：" + compareToOrder.mOrderCompanyReceivedMoney + "->"
                    + mOrderCompanyReceivedMoney + " ");
        }
        if (!compareToOrder.mOrderCurrencyRate.equals(mOrderCurrencyRate))
        {
            changeStringBuilder.append(
                    "汇率：" + compareToOrder.mOrderCurrencyRate + "->" + mOrderCurrencyRate + " ");
        }
        if (!compareToOrder.mOrderCustomerPaidMoney.equals(mOrderCustomerPaidMoney))
        {
            changeStringBuilder.append("客户已付款：" + compareToOrder.mOrderCustomerPaidMoney + "->"
                    + mOrderCustomerPaidMoney + " ");
        }
        if (!compareToOrder.mOrderDate.equals(mOrderDate))
        {
            changeStringBuilder
                    .append("订单日期：" + compareToOrder.mOrderDate + "->" + mOrderDate + " ");
        }
        if (!compareToOrder.mOrderEstimatedShippingFee.equals(mOrderEstimatedShippingFee))
        {
            changeStringBuilder.append("估计国际运费：" + compareToOrder.mOrderEstimatedShippingFee + "->"
                    + mOrderEstimatedShippingFee + " ");
        }
        if (!compareToOrder.mOrderDomesticShippingFee.equals(mOrderDomesticShippingFee))
        {
            changeStringBuilder.append("国内运费：" + compareToOrder.mOrderDomesticShippingFee + "->"
                    + mOrderDomesticShippingFee + " ");
        }
        if (!compareToOrder.mOrderCustomerPaidDomesticShippingFee
                .equals(mOrderCustomerPaidDomesticShippingFee))
        {
            changeStringBuilder
                    .append("用户已付国内运费：" + compareToOrder.mOrderCustomerPaidDomesticShippingFee
                            + "->" + mOrderCustomerPaidDomesticShippingFee + " ");
        }
        if (!compareToOrder.mOrderFinalActualCost.equals(mOrderFinalActualCost))
        {
            changeStringBuilder.append("最终成本：" + compareToOrder.mOrderFinalActualCost + "->"
                    + mOrderFinalActualCost + " ");
        }
        if (!compareToOrder.mOrderFinalProfit.equals(mOrderFinalProfit))
        {
            changeStringBuilder.append(
                    "最终利润：" + compareToOrder.mOrderFinalProfit + "->" + mOrderFinalProfit + " ");
        }
        if (!compareToOrder.mOrderSentToUser.equals(mOrderSentToUser))
        {
            changeStringBuilder.append(
                    "订单已向用户发货：" + compareToOrder.mOrderSentToUser + "->" + mOrderSentToUser + " ");
        }
        if (!compareToOrder.mOrderNote.equals(mOrderNote))
        {
            changeStringBuilder.append("备注：" + compareToOrder.mOrderNote + "->" + mOrderNote + " ");
        }
        if (compareToOrder.mOrderPackage != null && mOrderPackage != null
                && !compareToOrder.mOrderPackage.compareTo(mOrderPackage))
        {
            changeStringBuilder.append("OrderPackage：" + compareToOrder.mOrderPackage.getPackageId()
                    + "->" + mOrderPackage.getPackageId() + " ");
        }
        if (compareToOrder.mOrderPackage == null && mOrderPackage != null)
        {
            changeStringBuilder
                    .append("OrderPackage：null" + "->" + mOrderPackage.getPackageId() + " ");
        }
        if (compareToOrder.mOrderPackage != null && mOrderPackage == null)
        {
            changeStringBuilder.append(
                    "OrderPackage：" + compareToOrder.mOrderPackage.getPackageId() + "-> null");
        }

        if (compareToOrder.mOrderClient != null && mOrderClient != null
                && !compareToOrder.mOrderClient.compareTo(mOrderClient))
        {
            changeStringBuilder.append("OrderClient：" + compareToOrder.mOrderClient.getClientId()
                    + "->" + mOrderClient.getClientId() + " ");
        }
        if (compareToOrder.mOrderClient == null && mOrderClient != null)
        {
            changeStringBuilder
                    .append("OrderClient：null" + "->" + mOrderClient.getClientId() + " ");
        }
        if (compareToOrder.mOrderClient != null && mOrderClient == null)
        {
            changeStringBuilder
                    .append("OrderClient：" + compareToOrder.mOrderClient.getClientId() + "-> null");
        }

        if (!compareToOrder.mOrderProduct.compareTo(mOrderProduct))
        {
            changeStringBuilder
                    .append("OrderProduct：" + compareToOrder.mOrderProduct.getProductName() + "->"
                            + mOrderProduct.getProductName() + " ");
        }
        if (!compareToOrder.mOrderProductCount.equals(mOrderProductCount))
        {
            changeStringBuilder.append("OrderProductCount：" + compareToOrder.mOrderProductCount
                    + "->" + mOrderProductCount + " ");
        }
        if (!compareToOrder.mOrderProxy.compareTo(mOrderProxy))
        {
            changeStringBuilder.append("OrderProxy：" + compareToOrder.mOrderProxy.getAdminName()
                    + "->" + mOrderProxy.getAdminName() + " ");
        }
        if (!compareToOrder.mOrderProxyProfitPercentage.equals(mOrderProxyProfitPercentage))
        {
            changeStringBuilder.append(
                    "OrderProxyProfitPercentage：" + compareToOrder.mOrderProxyProfitPercentage
                            + "->" + mOrderProxyProfitPercentage + " ");
        }
        if (!compareToOrder.mOrderSellingPrice.equals(mOrderSellingPrice))
        {
            changeStringBuilder.append("OrderSellingPrice：" + compareToOrder.mOrderSellingPrice
                    + "->" + mOrderSellingPrice + " ");
        }
        if (!compareToOrder.mOrderWeight.equals(mOrderWeight))
        {
            changeStringBuilder.append(
                    "OrderWeight：" + compareToOrder.mOrderWeight + "->" + mOrderWeight + " ");
        }
        return changeStringBuilder.toString();
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
        refreshOrderFinalProfit();
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
        refreshOrderFinalProfit();
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
        refreshOrderFinalProfit();
    }

    /**
     * @return the orderDomesticShippingFee
     */
    public Float getOrderDomesticShippingFee()
    {
        return mOrderDomesticShippingFee;
    }

    /**
     * @param orderDomesticShippingFee
     *            the orderDomesticShippingFee to set
     */
    public void setOrderDomesticShippingFee(Float orderDomesticShippingFee)
    {
        mOrderDomesticShippingFee = orderDomesticShippingFee;
        refreshOrderFinalProfit();
    }

    /**
     * @return the orderCustomerPaidDomesticShippingFee
     */
    public Float getOrderCustomerPaidDomesticShippingFee()
    {
        return mOrderCustomerPaidDomesticShippingFee;
    }

    /**
     * @param orderCustomerPaidDomesticShippingFee
     *            the orderCustomerPaidDomesticShippingFee to set
     */
    public void setOrderCustomerPaidDomesticShippingFee(Float orderCustomerPaidDomesticShippingFee)
    {
        mOrderCustomerPaidDomesticShippingFee = orderCustomerPaidDomesticShippingFee;
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
        refreshOrderFinalProfit();
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
     * @return the orderClient
     */
    public Client getOrderClient()
    {
        return mOrderClient;
    }

    /**
     * @param orderClient
     *            the orderClient to set
     */
    public void setOrderClient(Client orderClient)
    {
        mOrderClient = orderClient;
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
     * @return the orderSentToUser
     */
    public Boolean getOrderSentToUser()
    {
        return mOrderSentToUser;
    }

    /**
     * @param orderSentToUser
     *            the orderSentToUser to set
     */
    public void setOrderSentToUser(Boolean orderSentToUser)
    {
        mOrderSentToUser = orderSentToUser;
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

    @Override
    public boolean compareTo(Object object)
    {
        if (object instanceof Order)
        {
            final Order compareToOrder = (Order) object;
            if (compareToOrder.mOrderId.equals(mOrderId)
                    && compareToOrder.mOrderIsProfitPaid.equals(mOrderIsProfitPaid)
                    && compareToOrder.mOrderActualCost.equals(mOrderActualCost)
                    && compareToOrder.mOrderActualShippingFee.equals(mOrderActualShippingFee)
                    && compareToOrder.mOrderBuyerName.equals(mOrderBuyerName)
                    && compareToOrder.mOrderCompanyReceivedMoney.equals(mOrderCompanyReceivedMoney)
                    && compareToOrder.mOrderCurrencyRate.equals(mOrderCurrencyRate)
                    && compareToOrder.mOrderCustomerPaidMoney.equals(mOrderCustomerPaidMoney)
                    && compareToOrder.mOrderDate.equals(mOrderDate)
                    && compareToOrder.mOrderEstimatedShippingFee.equals(mOrderEstimatedShippingFee)
                    && compareToOrder.mOrderDomesticShippingFee.equals(mOrderDomesticShippingFee)
                    && compareToOrder.mOrderCustomerPaidDomesticShippingFee
                            .equals(mOrderCustomerPaidDomesticShippingFee)
                    && compareToOrder.mOrderFinalActualCost.equals(mOrderFinalActualCost)
                    && compareToOrder.mOrderFinalProfit.equals(mOrderFinalProfit)
                    && compareToOrder.mOrderNote.equals(mOrderNote)
                    && ((compareToOrder.mOrderPackage == null && mOrderPackage == null)
                            || (compareToOrder.mOrderPackage != null
                                    && compareToOrder.mOrderPackage.compareTo(mOrderPackage)))
                    && compareToOrder.mOrderProduct.compareTo(mOrderProduct)
                    && compareToOrder.mOrderProductCount.equals(mOrderProductCount)
                    && compareToOrder.mOrderProxy.compareTo(mOrderProxy)
                    && compareToOrder.mOrderProxyProfitPercentage
                            .equals(mOrderProxyProfitPercentage)
                    && compareToOrder.mOrderSellingPrice.equals(mOrderSellingPrice)
                    && compareToOrder.mOrderWeight.equals(mOrderWeight)
                    && compareToOrder.mOrderSentToUser.equals(mOrderSentToUser)
                    && ((compareToOrder.mOrderClient == null && mOrderClient == null)
                            || (compareToOrder.mOrderClient != null
                                    && compareToOrder.mOrderClient.compareTo(mOrderClient))))
            {
                return true;
            }
        }
        return false;
    }

}
