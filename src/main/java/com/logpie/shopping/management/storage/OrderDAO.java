// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.storage;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.model.LogpieModel;
import com.logpie.shopping.management.model.Order;
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

    public boolean updateOrderProfile(final Order admin)
    {
        final UpdateOrderUpdate updateOrderUpdate = new UpdateOrderUpdate(admin, sOrderTableName);
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
    }

    private class GetPackageByIdQuery extends LogpieBaseQuerySingleRecordByIdTemplateQuery<Order>
    {
        GetPackageByIdQuery(final String orderId)
        {
            super(new Order(), OrderDAO.sOrderTableName, Order.DB_KEY_ORDER_PACKAGE_ID, orderId);
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

}
