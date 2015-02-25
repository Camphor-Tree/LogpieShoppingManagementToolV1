// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.Brand;
import com.logpie.shopping.management.model.Category;
import com.logpie.shopping.management.model.Image;
import com.logpie.shopping.management.model.LogpieModel;
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
    public static final String sOrderTableName = "Orders";

    /**
     * For adding a new order into the database
     * 
     * @param order
     * @return true if adding order successfully. false if adding order fails
     */
    public boolean addOrder(final Order order)
    {
        final LogpieDataInsert<Order> addPackageInsert = new AddOrderInsert(order);
        return super.insertData(addPackageInsert);
    }

    /**
     * For getting all existing orders
     * 
     * @return All existing orders
     */
    public List<Order> getAllOrders()
    {
        final GetAllOrdersQuery getAllPackageQuery = new GetAllOrdersQuery();
        return super.queryResult(getAllPackageQuery);
    }

    /**
     * For getting all orders in specific year-month
     * 
     * @return orders
     */
    public List<Order> getOrdersByMonth(final String year, final String month)
    {
        final GetAllOrdersInMonthQuery getAllOrdersInMonthQuery = new GetAllOrdersInMonthQuery(
                year, month);
        return super.queryResult(getAllOrdersInMonthQuery);
    }

    /**
     * For querying specific Package by PackageId
     * 
     * @param orderId
     * @return Order corresponding to the orderId
     */
    public Order getOrderById(final String orderId)
    {
        GetOrderByIdQuery getPackageByIdQuery = new GetOrderByIdQuery(orderId);
        List<Order> orderList = super.queryResult(getPackageByIdQuery);
        if (CollectionUtils.isEmpty(orderList) || orderList.size() > 1)
        {
            LOG.error("The order cannot be found by this id:" + orderId);
            return null;
        }
        return orderList.get(0);
    }

    /**
     * Update the order profile
     * 
     * @param order
     * @return
     */
    public boolean updateOrderProfile(final Order order)
    {
        final ModifyOrderUpdate updateOrderUpdate = new ModifyOrderUpdate(order, sOrderTableName,
                order.getOrderId());
        return super.updateData(updateOrderUpdate);
    }

    private class AddOrderInsert implements LogpieDataInsert<Order>
    {
        private Order mOrder;

        AddOrderInsert(final Order order)
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
            return mOrder.getModelMap();
        }
    }

    private class GetAllOrdersQuery extends LogpieBaseQueryAllTemplateQuery<Order>
    {
        GetAllOrdersQuery()
        {
            super(new Order(), OrderDAO.sOrderTableName);
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

    private class GetAllOrdersInMonthQuery extends LogpieBaseQueryAllTemplateQuery<Order>
    {
        private String mYear;
        private String mMonth;

        GetAllOrdersInMonthQuery(final String year, final String month)
        {
            super(new Order(), OrderDAO.sOrderTableName);
            mYear = year;
            mMonth = month;
        }

        @Override
        public Set<String> getQueryConditions()
        {
            // get foreign key connection
            final Set<String> conditions = getForeignKeyConnectionConditions();
            // add year month conditions
            conditions.add(String.format("MONTH(%s) = MONTH(\'%s-%s-01\')",
                    Order.DB_KEY_ORDER_DATE, mYear, mMonth));
            return conditions;
        }

        @Override
        public Map<String, String> getQueryTables()
        {
            return getForeignKeyConnectionTables();
        }
    }

    private class GetOrderByIdQuery extends LogpieBaseQuerySingleRecordByIdTemplateQuery<Order>
    {
        GetOrderByIdQuery(final String orderId)
        {
            super(new Order(), OrderDAO.sOrderTableName, Order.DB_KEY_ORDER_ID, orderId);
        }

        // foreign key connection
        @Override
        public Set<String> getQueryConditions()
        {
            final Set<String> conditions = getForeignKeyConnectionConditions();
            conditions.add(super.mKeyForId + "=\"" + super.mValueForId + "\"");
            return conditions;
        }

        @Override
        public Map<String, String> getQueryTables()
        {
            return getForeignKeyConnectionTables();
        }
    }

    private class ModifyOrderUpdate extends LogpieBaseUpdateRecordTemplateUpdate<Order>
    {
        /**
         * @param model
         * @param tableName
         */
        public ModifyOrderUpdate(LogpieModel model, String tableName, String orderId)
        {
            super(model, tableName, Order.DB_KEY_ORDER_ID, orderId);
        }
    }

    private static Set<String> getForeignKeyConnectionConditions()
    {
        final Set<String> conditions = new HashSet<String>();
        conditions
                .add(String.format("%s = %s", Order.DB_KEY_ORDER_PROXY_ID, Admin.DB_KEY_ADMIN_ID));

        // conditions.add(String.format("%s = %s",
        // Order.DB_KEY_ORDER_PACKAGE_ID,
        // LogpiePackage.DB_KEY_PACKAGE_ID));

        conditions.add(String.format("%s = %s", Order.DB_KEY_ORDER_PRODUCT_ID,
                Product.DB_KEY_PRODUCT_ID));
        conditions.add(String.format("%s = %s.%s", Product.DB_KEY_PRODUCT_IMAGE_ID,
                ProductDAO.sProductImageTableAlias, Image.DB_KEY_IMAGE_ID));
        // Brand need to connect to Image
        conditions.add(String.format("%s = %s.%s", Brand.DB_KEY_BRAND_IMAGE_ID,
                BrandDAO.sBrandImageTableAlias, Image.DB_KEY_IMAGE_ID));
        // conditions.add(String.format("%s = %s.%s",
        // Brand.DB_KEY_BRAND_SIZE_CHART_ID,
        // BrandDAO.sBrandSizeChartImageAlias, Image.DB_KEY_IMAGE_ID));
        conditions.add(String.format("%s = %s", Brand.DB_KEY_BRAND_CATEGORY_ID,
                Category.DB_KEY_CATEGORY_ID));
        conditions.add(String.format("%s = %s", Product.DB_KEY_PRODUCT_BRAND_ID,
                Brand.DB_KEY_BRAND_ID));
        return conditions;
    }

    private static Map<String, String> getForeignKeyConnectionTables()
    {
        final Map<String, String> tableMap = new HashMap<String, String>();
        tableMap.put(sNonAliasPrefix + sOrderTableName, sOrderTableName);
        // alias for multiple foreign key connection
        tableMap.put(sNonAliasPrefix + AdminDAO.sAdminTableName, AdminDAO.sAdminTableName);

        // tableMap.put(sNonAliasPrefix + LogpiePackageDAO.sPackageTableName,
        // LogpiePackageDAO.sPackageTableName);

        tableMap.put(sNonAliasPrefix + ProductDAO.sProductTableName, ProductDAO.sProductTableName);
        tableMap.put(sNonAliasPrefix + BrandDAO.sBrandTableName, BrandDAO.sBrandTableName);
        tableMap.put(ProductDAO.sProductImageTableAlias, ImageDAO.sImageTableName);
        tableMap.put(BrandDAO.sBrandImageTableAlias, ImageDAO.sImageTableName);
        // tableMap.put(BrandDAO.sBrandSizeChartImageAlias,
        // ImageDAO.sImageTableName);
        tableMap.put(sNonAliasPrefix + CategoryDAO.sCategoryTableName,
                CategoryDAO.sCategoryTableName);
        return tableMap;
    }
}
