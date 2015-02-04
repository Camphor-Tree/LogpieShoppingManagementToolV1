// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.LogpiePackage;
import com.logpie.shopping.management.model.Order;
import com.logpie.shopping.management.model.Product;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * @author zhoyilei
 *
 */
public class OrderDAO extends LogpieBaseDAO<Order>
{
    private static final Logger LOG = Logger.getLogger(OrderDAO.class);
    private static final String sOrderTableName = "Orders";

    /**
     * For adding a new order into the database
     * 
     * @param order
     * @return true if adding order successfully. false if adding order fails
     */
    public boolean addPackage(final Order order)
    {
        final LogpieDataInsert<Order> addPackageInsert = new AddPackageInsert(order);
        return super.insertData(addPackageInsert);
    }

    /**
     * For getting all existing categories
     * 
     * @return All existing categories
     */
    public List<Order> getAllPackage()
    {
        GetAllPackageQuery getAllPackageQuery = new GetAllPackageQuery();
        return super.queryResult(getAllPackageQuery);
    }

    /**
     * For querying specific Package by PackageId
     * 
     * @param orderId
     * @return Package corresponding to the PackageId
     */
    public Order getPackageById(final String orderId)
    {
        GetPackageByIdQuery getPackageByIdQuery = new GetPackageByIdQuery(orderId);
        List<Order> orderList = super.queryResult(getPackageByIdQuery);
        if (CollectionUtils.isEmpty(orderList) || orderList.size() > 1)
        {
            LOG.error("The order cannot be found by this id:" + orderId);
            return null;
        }
        return orderList.get(0);

    }

    private class AddPackageInsert implements LogpieDataInsert<Order>
    {
        private Order mOrder;

        AddPackageInsert(final Order order)
        {
            mOrder = order;
        }

        @Override
        public String getInsertTable()
        {
            return sOrderTableName;
        }

        @Override
        public Map<String, Object> getInsertValues()
        {
            final String orderDate = mOrder.getOrderDate();
            final Product orderProduct = mOrder.getOrderProduct();
            final String orderProductId = orderProduct.getProductId();
            final Integer orderProductCount = mOrder.getOrderProductCount();
            final String orderBuyerName = mOrder.getOrderBuyerName();
            final Admin orderProxy = mOrder.getOrderProxy();
            final String orderProxyId = orderProxy.getAdminId();
            final Float orderProxyProfitPercentage = mOrder.getOrderProxyProfitPercentage();
            final Float orderCurrencyRate = mOrder.getOrderCurrencyRate();
            final LogpiePackage orderPackage = mOrder.getOrderPackage();
            final String orderPackageId = orderPackage.getPackageId();
            final Integer orderEstimatedShippingFee = mOrder.getOrderEstimatedShippingFee();
            final Integer orderActualShippingFee = mOrder.getOrderActualShippingFee();
            final Integer orderSellingPrice = mOrder.getOrderSellingPrice();
            final Integer orderCustomerPaidMoney = mOrder.getOrderCustomerPaidMoney();
            final Integer orderFinalProfit = mOrder.getOrderFinalProfit();
            final Boolean orderIsProfitPaid = mOrder.getOrderIsProfitPaid();
            final String orderNote = mOrder.getOrderNote();

            final Map<String, Object> insertValues = new HashMap<String, Object>();
            insertValues.put(Order.DB_KEY_ORDER_DATE, orderDate);
            insertValues.put(Order.DB_KEY_PRODUCT_ID, orderProductId);
            insertValues.put(Order.DB_KEY_PRODUCT_COUNT, orderProductCount);
            insertValues.put(Order.DB_KEY_ORDER_BUYER_NAME, orderBuyerName);
            insertValues.put(Order.DB_KEY_ORDER_PROXY_ID, orderProxyId);
            insertValues
                    .put(Order.DB_KEY_ORDER_PROXY_PROFIT_PERCENTAGE, orderProxyProfitPercentage);
            insertValues.put(Order.DB_KEY_ORDER_CURRENCY_RATE, orderCurrencyRate);
            insertValues.put(Order.DB_KEY_ORDER_PACKAGE_ID, orderPackageId);
            insertValues.put(Order.DB_KEY_ORDER_ESTIMATED_SHIPPING_FEE, orderEstimatedShippingFee);
            insertValues.put(Order.DB_KEY_ORDER_ACTUAL_SHIPPING_FEE, orderActualShippingFee);
            insertValues.put(Order.DB_KEY_ORDER_SELLING_PRICE, orderSellingPrice);
            insertValues.put(Order.DB_KEY_ORDER_CUSTOMER_PAID_MONEY, orderCustomerPaidMoney);
            insertValues.put(Order.DB_KEY_ORDER_FINAL_PROFIT, orderFinalProfit);
            insertValues.put(Order.DB_KEY_ORDER_IS_PROFIT_PAID, orderIsProfitPaid);
            insertValues.put(Order.DB_KEY_ORDER_NOTE, orderNote);
            return insertValues;
        }
    }

    private class GetAllPackageQuery extends LogpieBaseQueryAllTemplateQuery<Order>
    {
        GetAllPackageQuery()
        {
            super(new Order(), OrderDAO.sOrderTableName);
        }
    }

    private class GetPackageByIdQuery extends LogpieBaseQuerySingleRecordByIdTemplateQuery<Order>
    {
        GetPackageByIdQuery(final String orderId)
        {
            super(new Order(), OrderDAO.sOrderTableName, Order.DB_KEY_ORDER_PACKAGE_ID, orderId);
        }
    }

}
