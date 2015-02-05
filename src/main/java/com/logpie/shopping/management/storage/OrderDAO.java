// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.LogpieModel;
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
    public static final String sOrderTableName = "Orders";

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

    /**
     * Update the order profile
     * 
     * @param order
     * @return
     */
    public boolean updateOrderProfile(final Order order)
    {
        final UpdateOrderUpdate updateOrderUpdate = new UpdateOrderUpdate(order, sOrderTableName);
        return super.updateData(updateOrderUpdate);
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
            return mOrder.getModelMap();
        }
    }

    private class GetAllPackageQuery extends LogpieBaseQueryAllTemplateQuery<Order>
    {
        GetAllPackageQuery()
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

    private class GetPackageByIdQuery extends LogpieBaseQuerySingleRecordByIdTemplateQuery<Order>
    {
        GetPackageByIdQuery(final String orderId)
        {
            super(new Order(), OrderDAO.sOrderTableName, Order.DB_KEY_ORDER_PACKAGE_ID, orderId);
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

    private class UpdateOrderUpdate extends LogpieBaseUpdateRecordTemplateUpdate<Order>
    {
        /**
         * @param model
         * @param tableName
         */
        public UpdateOrderUpdate(LogpieModel model, String tableName)
        {
            super(model, tableName);
        }
    }

    private static Set<String> getForeignKeyConnectionConditions()
    {
        final Set<String> conditions = new HashSet<String>();
        conditions
                .add(String.format("%s = %s", Order.DB_KEY_ORDER_PROXY_ID, Admin.DB_KEY_ADMIN_ID));
        conditions.add(String.format("%s = %s", Order.DB_KEY_ORDER_PACKAGE_ID,
                LogpiePackage.DB_KEY_PACKAGE_ID));
        conditions.add(String.format("%s = %s", Order.DB_KEY_ORDER_PRODUCT_ID,
                Product.DB_KEY_PRODUCT_ID));
        return null;
    }

    private static Map<String, String> getForeignKeyConnectionTables()
    {
        final Map<String, String> tableMap = new HashMap<String, String>();
        tableMap.put(sNonAliasPrefix + sOrderTableName, sOrderTableName);
        // alias for multiple foreign key connection
        tableMap.put(sNonAliasPrefix + AdminDAO.sAdminTableName, AdminDAO.sAdminTableName);
        tableMap.put(sNonAliasPrefix + LogpiePackageDAO.sPackageTableName,
                LogpiePackageDAO.sPackageTableName);
        tableMap.put(sNonAliasPrefix + ProductDAO.sProductTableName, ProductDAO.sProductTableName);
        return tableMap;
    }
}
