// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.accounting.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.Category;
import com.logpie.shopping.management.model.Order;
import com.logpie.shopping.management.model.Product;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * @author zhoyilei
 *
 */
public class AccountingLogic
{
    public static Map<String, Integer> getOrdersInCategory(final List<Order> orderList)
    {
        if (CollectionUtils.isEmpty(orderList))
        {
            return null;
        }
        final Map<String, Integer> orderInCategoryMap = new HashMap<String, Integer>();
        for (final Order order : orderList)
        {
            final Product orderProduct = order.getOrderProduct();
            if (orderProduct != null)
            {
                final Category category = orderProduct.getProductBrand().getBrandCategory();
                final String categoryName = category.getCategoryName();
                if (!orderInCategoryMap.containsKey(categoryName))
                {
                    orderInCategoryMap.put(categoryName, 1);
                }
                else
                {
                    int count = orderInCategoryMap.get(categoryName);
                    orderInCategoryMap.put(categoryName, count + 1);
                }
            }
        }
        return orderInCategoryMap;
    }

    public static Map<String, Integer> getOrdersInBrand(final List<Order> orderList)
    {
        if (CollectionUtils.isEmpty(orderList))
        {
            return null;
        }
        final Map<String, Integer> orderInBrandMap = new HashMap<String, Integer>();
        for (final Order order : orderList)
        {
            final Product orderProduct = order.getOrderProduct();
            if (orderProduct != null)
            {
                final String brandName = orderProduct.getProductBrand().getBrandChineseName();
                if (!orderInBrandMap.containsKey(brandName))
                {
                    orderInBrandMap.put(brandName, 1);
                }
                else
                {
                    int count = orderInBrandMap.get(brandName);
                    orderInBrandMap.put(brandName, count + 1);
                }
            }
        }
        return orderInBrandMap;
    }

    public static Map<String, Integer> getOrdersInAdmin(final List<Order> orderList)
    {
        if (CollectionUtils.isEmpty(orderList))
        {
            return null;
        }
        final Map<String, Integer> orderInAdminMap = new HashMap<String, Integer>();
        for (final Order order : orderList)
        {
            final Admin orderProxyAdmin = order.getOrderProxy();
            if (orderProxyAdmin != null)
            {
                final String proxyAdminName = orderProxyAdmin.getAdminName();
                if (!orderInAdminMap.containsKey(proxyAdminName))
                {
                    orderInAdminMap.put(proxyAdminName, 1);
                }
                else
                {
                    int count = orderInAdminMap.get(proxyAdminName);
                    orderInAdminMap.put(proxyAdminName, count + 1);
                }
            }
        }
        return orderInAdminMap;
    }
}
