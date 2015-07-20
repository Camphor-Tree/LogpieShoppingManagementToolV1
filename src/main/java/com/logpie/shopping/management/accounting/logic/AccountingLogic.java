// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.accounting.logic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.logpie.shopping.management.business.logic.LogpieProfitCalculator;
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
    public enum ProfitType
    {
        CompanyProfit, ProxyProfit, SuperAdminProfit;
    }

    /**
     * 
     * @param isDaily
     *            whether it is daily or monthly. If daily pass true
     * @param period
     *            How long the period should be.
     * @param orderList
     * @return
     */
    public static Map<String, Double> getOrderProfits(final boolean isDaily, final int period,
            final List<Order> orderList, final ProfitType profitType)
    {
        final Map<String, Double> orderProfitMap = new HashMap<String, Double>();
        final Map<String, List<Order>> ordersInPeriodMap = new HashMap<String, List<Order>>();
        // Add keys. (Date/month)
        for (int i = 0; i < period; i++)
        {
            final Calendar cal = Calendar.getInstance();
            final String dateKey;
            if (isDaily)
            {
                cal.add(Calendar.DATE, -1 * i);
                final Date dateBeforeIdays = cal.getTime();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
                dateKey = dateFormat.format(dateBeforeIdays);

            }
            else
            {
                cal.add(Calendar.MONTH, -1 * i);
                final Date dateBeforeImonths = cal.getTime();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
                dateKey = dateFormat.format(dateBeforeImonths);
            }
            orderProfitMap.put(dateKey, 0.0);
            ordersInPeriodMap.put(dateKey, new ArrayList<Order>());
        }

        if (CollectionUtils.isEmpty(orderList))
        {
            return orderProfitMap;
        }

        // Insert orders into all periods
        for (final Order order : orderList)
        {
            final String orderDate = order.getOrderDate();
            if (orderDate != null)
            {
                final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date;
                try
                {
                    date = formatter.parse(orderDate);
                } catch (ParseException e)
                {
                    continue;
                }
                final SimpleDateFormat dateFormat;
                final String dateKey;
                if (isDaily)
                {
                    dateFormat = new SimpleDateFormat("MM-dd");
                }
                else
                {
                    dateFormat = new SimpleDateFormat("yyyy-MM");
                }
                dateKey = dateFormat.format(date);

                if (ordersInPeriodMap.containsKey(dateKey))
                {
                    final List<Order> ordersListInPeriod = ordersInPeriodMap.get(dateKey);
                    ordersListInPeriod.add(order);
                }
            }
        }

        // Use LogpieProfitCalculator to calculate each period's orders' profit
        for (final Entry<String, Double> profitEntry : orderProfitMap.entrySet())
        {
            final List<Order> ordersListInPeriod = ordersInPeriodMap.get(profitEntry.getKey());
            final LogpieProfitCalculator profitCalculator = new LogpieProfitCalculator(
                    ordersListInPeriod);
            Float periodProfit = null;
            if (profitType.equals(ProfitType.ProxyProfit))
            {
                periodProfit = profitCalculator.getProxyEstimatedProfitsForAllOrders();
            }
            else if (profitType.equals(ProfitType.CompanyProfit))
            {
                periodProfit = profitCalculator.getEstimatedProfitsForAllOrders();
            }
            else if (profitType.equals(ProfitType.SuperAdminProfit))
            {
                periodProfit = profitCalculator.getNetEstimatedProfitsForAllOrders();
            }
            orderProfitMap.put(profitEntry.getKey(), Double.valueOf(Float.toString(periodProfit)));
        }
        return orderProfitMap;
    }

    /**
     * 
     * @param isDaily
     *            whether it is daily or monthly. If daily pass true
     * @param period
     *            How long the period should be.
     * @param orderList
     * @return
     */
    public static Map<String, Integer> getOrderNumbers(final boolean isDaily, final int period,
            final List<Order> orderList)
    {
        final Map<String, Integer> orderNumberMap = new HashMap<String, Integer>();
        // Add keys. (Date/month)
        for (int i = 0; i < period; i++)
        {
            final Calendar cal = Calendar.getInstance();
            if (isDaily)
            {
                cal.add(Calendar.DATE, -1 * i);
                final Date dateBeforeIdays = cal.getTime();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
                final String dateKey = dateFormat.format(dateBeforeIdays);
                orderNumberMap.put(dateKey, 0);
            }
            else
            {
                cal.add(Calendar.MONTH, -1 * i);
                final Date dateBeforeImonths = cal.getTime();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
                final String dateKey = dateFormat.format(dateBeforeImonths);
                orderNumberMap.put(dateKey, 0);
            }
        }

        if (CollectionUtils.isEmpty(orderList))
        {
            return orderNumberMap;
        }

        for (final Order order : orderList)
        {
            final String orderDate = order.getOrderDate();
            if (orderDate != null)
            {
                final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date;
                try
                {
                    date = formatter.parse(orderDate);
                } catch (ParseException e)
                {
                    continue;
                }
                SimpleDateFormat dateFormat;
                String dateKey;
                if (isDaily)
                {
                    dateFormat = new SimpleDateFormat("MM-dd");
                    dateKey = dateFormat.format(date);
                }
                else
                {
                    dateFormat = new SimpleDateFormat("yyyy-MM");
                    dateKey = dateFormat.format(date);
                }

                if (orderNumberMap.containsKey(dateKey))
                {
                    int count = orderNumberMap.get(dateKey);
                    orderNumberMap.put(dateKey, count + 1);
                }
            }
        }
        return orderNumberMap;
    }

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

    public static Map<String, Double> getOrderProfitsInAdmin(final List<Order> orderList)
    {
        if (CollectionUtils.isEmpty(orderList))
        {
            return null;
        }
        final Map<String, Double> orderProfitInAdminMap = new HashMap<String, Double>();
        for (final Order order : orderList)
        {
            final Admin orderProxyAdmin = order.getOrderProxy();

            final Float orderProfit = order.getOrderFinalProfit();
            // We do this to prevent it being a negative number,
            // since Pie chart doesn't accept negative number.
            if (orderProfit < 0.001f)
            {
                continue;
            }

            if (orderProxyAdmin != null)
            {
                final String proxyAdminName = orderProxyAdmin.getAdminName();
                if (!orderProfitInAdminMap.containsKey(proxyAdminName))
                {
                    orderProfitInAdminMap.put(proxyAdminName,
                            Double.parseDouble(orderProfit.toString()));
                }
                else
                {
                    double profit = orderProfitInAdminMap.get(proxyAdminName);
                    profit = profit + orderProfit;
                    orderProfitInAdminMap.put(proxyAdminName, profit);
                }
            }
        }
        return orderProfitInAdminMap;
    }

    public static Map<String, Double> getOrderProfitsInBrand(final List<Order> orderList)
    {
        if (CollectionUtils.isEmpty(orderList))
        {
            return null;
        }
        final Map<String, Double> orderProfitInBrandMap = new HashMap<String, Double>();
        for (final Order order : orderList)
        {
            final String brandName = order.getOrderProduct().getProductBrand()
                    .getBrandChineseName();
            final Float orderProfit = order.getOrderFinalProfit();
            // We do this to prevent it being a negative number,
            // since Pie chart doesn't accept negative number.
            if (orderProfit < 0.001f)
            {
                continue;
            }

            if (brandName != null)
            {
                if (!orderProfitInBrandMap.containsKey(brandName))
                {
                    orderProfitInBrandMap.put(brandName,
                            Double.parseDouble(orderProfit.toString()));
                }
                else
                {
                    double profit = orderProfitInBrandMap.get(brandName);
                    profit = profit + orderProfit;
                    orderProfitInBrandMap.put(brandName, profit);
                }
            }
        }
        return orderProfitInBrandMap;
    }

    public static Map<String, Double> getOrderProfitsInCategory(final List<Order> orderList)
    {
        if (CollectionUtils.isEmpty(orderList))
        {
            return null;
        }
        final Map<String, Double> orderProfitInCategoryMap = new HashMap<String, Double>();
        for (final Order order : orderList)
        {
            final String categoryName = order.getOrderProduct().getProductBrand().getBrandCategory()
                    .getCategoryName();
            final Float orderProfit = order.getOrderFinalProfit();
            // We do this to prevent it being a negative number,
            // since Pie chart doesn't accept negative number.
            if (orderProfit < 0.001f)
            {
                continue;
            }
            if (categoryName != null)
            {
                if (!orderProfitInCategoryMap.containsKey(categoryName))
                {
                    orderProfitInCategoryMap.put(categoryName,
                            Double.parseDouble(orderProfit.toString()));
                }
                else
                {
                    double profit = orderProfitInCategoryMap.get(categoryName);
                    profit = profit + orderProfit;
                    orderProfitInCategoryMap.put(categoryName, profit);
                }
            }
        }
        return orderProfitInCategoryMap;
    }
}
