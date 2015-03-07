// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.business.logic;

import java.util.List;

import com.logpie.shopping.management.model.Order;
import com.logpie.shopping.management.util.CollectionUtils;

/**
 * This class is used to calculate logpie profits.
 * 
 * @author zhoyilei
 *
 */
public class LogpieProfitCalculator
{
    private List<Order> mOrderList;
    private Float mEstimatedProfitsForAllOrders;
    private Float mActualProfitsForAllOrders;
    private Float mNetEstimatedProfitsForAllOrders;
    private Float mNetActualProfitsForAllOrders;

    /**
     * @param orderList
     */
    public LogpieProfitCalculator(List<Order> orderList)
    {
        super();
        mOrderList = orderList;
        mEstimatedProfitsForAllOrders = getProfitInEstimationForAllOrders();
        mActualProfitsForAllOrders = getActualProfitForAllShippedOrders();
        mNetEstimatedProfitsForAllOrders = getEstimatedNetCompanyProfitForAllOrders();
        mNetActualProfitsForAllOrders = getActualNetCompanyProfitForShippedOrders();
    }

    /**
     * Calculate the profit in estimation. This calculation is for all the
     * orders even if the order is not shipped yet.
     * 
     * ProfitInTheory = SellingPrice - ActualBuyingCost - EstimatedShippingFee
     */
    public Float getProfitInEstimationForAllOrders()
    {
        if (CollectionUtils.isEmpty(mOrderList))
        {
            return 0.0f;
        }

        float totalProfitInEstimation = 0.0f;
        for (final Order order : mOrderList)
        {
            float singleProfitInEstimation = getEstimatedProfitForOrder(order);
            totalProfitInEstimation += singleProfitInEstimation;
        }
        return Float.valueOf(totalProfitInEstimation);
    }

    /**
     * Calculate the actual theory profit for all shipped package.
     * 
     * ActualTheoryProfit = SellingPrice - ActualBuyingCost - ActualShippingFee
     */
    public Float getActualProfitForAllShippedOrders()
    {
        if (CollectionUtils.isEmpty(mOrderList))
        {
            return 0.0f;
        }

        float totalActualTheoryProfit = 0.0f;
        for (final Order order : mOrderList)
        {
            // Skip all the orders without orderPackage order the actual
            // shipping fee is 0 which is barely impossible
            if (order.getOrderPackage() == null || order.getOrderActualShippingFee() < 0.001f)
            {
                continue;
            }
            float singleActualTheoryProfit = getActualProfitForOrder(order);
            totalActualTheoryProfit += singleActualTheoryProfit;
        }
        return Float.valueOf(totalActualTheoryProfit);
    }

    /**
     * Calculate the profit cutting off proxy's bonus distribution
     * EstimatedNetCompanyProfit = (SellingPrice - ActualBuyingCost -
     * EstimatedShippingFee) * (1.0 - ProxyProfitPercentage)
     * 
     * @return
     */
    public Float getEstimatedNetCompanyProfitForAllOrders()
    {
        if (CollectionUtils.isEmpty(mOrderList))
        {
            return 0.0f;
        }

        float totalEstimatedNetCompanyProfit = 0.0f;
        for (final Order order : mOrderList)
        {
            float singleEstimatedNetCompanyProfit = getEstimatedProfitForOrder(order)
                    * (1.0f - order.getOrderProxyProfitPercentage());
            totalEstimatedNetCompanyProfit += singleEstimatedNetCompanyProfit;
        }
        return Float.valueOf(totalEstimatedNetCompanyProfit);
    }

    /**
     * Calculate the profit cutting off proxy's bonus distribution for orders
     * already shipped (knowing the actual shipping fee.)
     * 
     * ActualNetCompanyProfit = (SellingPrice - ActualBuyingCost -
     * ActualShippingFee) * (1.0 - ProxyProfitPercentage)
     * 
     * @return
     */
    public Float getActualNetCompanyProfitForShippedOrders()
    {
        if (CollectionUtils.isEmpty(mOrderList))
        {
            return 0.0f;
        }

        float totalActualNetCompnayProfit = 0.0f;
        for (final Order order : mOrderList)
        {
            // Skip all the orders without orderPackage order the actual
            // shipping fee is 0 which is barely impossible
            if (order.getOrderPackage() == null || order.getOrderActualShippingFee() < 0.001f)
            {
                continue;
            }
            float singleActualNetCompnayProfit = getActualProfitForOrder(order)
                    * (1.0f - order.getOrderProxyProfitPercentage());
            totalActualNetCompnayProfit += singleActualNetCompnayProfit;
        }
        return Float.valueOf(totalActualNetCompnayProfit);
    }

    /**
     * @param order
     * @return
     */
    private float getActualProfitForOrder(final Order order)
    {
        float singleActualTheoryProfit = order.getOrderSellingPrice() - order.getOrderActualCost()
                - order.getOrderActualShippingFee();
        return singleActualTheoryProfit;
    }

    /**
     * @param order
     * @return
     */
    private float getEstimatedProfitForOrder(final Order order)
    {
        float singleProfitInEstimation = order.getOrderSellingPrice() - order.getOrderActualCost()
                - order.getOrderEstimatedShippingFee();
        return singleProfitInEstimation;
    }

    /**
     * @return the estimatedProfitsForAllOrders
     */
    public Float getEstimatedProfitsForAllOrders()
    {
        return mEstimatedProfitsForAllOrders;
    }

    /**
     * @return the actualProfitsForAllOrders
     */
    public Float getActualProfitsForAllOrders()
    {
        return mActualProfitsForAllOrders;
    }

    /**
     * @return the netEstimatedProfitsForAllOrders
     */
    public Float getNetEstimatedProfitsForAllOrders()
    {
        return mNetEstimatedProfitsForAllOrders;
    }

    /**
     * @return the netActualProfitsForAllOrders
     */
    public Float getNetActualProfitsForAllOrders()
    {
        return mNetActualProfitsForAllOrders;
    }
}