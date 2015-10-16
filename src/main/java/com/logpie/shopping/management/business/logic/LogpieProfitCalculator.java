// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.business.logic;

import java.util.List;

import com.logpie.shopping.management.model.Order;
import com.logpie.shopping.management.util.CollectionUtils;
import com.logpie.shopping.management.util.NumberUtils;

/**
 * This class is used to calculate logpie profits.
 * 
 * @author zhoyilei
 *
 */
public class LogpieProfitCalculator
{
    private final List<Order> mOrderList;
    private final Float mEstimatedProfitsForAllOrders;
    private final Float mActualProfitsForAllOrders;
    private final Float mNetEstimatedProfitsForAllOrders;
    private final Float mNetActualProfitsForAllOrders;
    private final Float mProxyEstimatedProfitsForAllOrders;
    private final Float mProxyActualProfitsForAllOrders;

    // 总营业额
    private final Float mTotalTurnOver;
    private final Float mTotalMoneyInFly;

    /**
     * @param orderList
     */
    public LogpieProfitCalculator(List<Order> orderList)
    {
        super();
        mOrderList = orderList;
        mEstimatedProfitsForAllOrders = NumberUtils
                .keepTwoDigitsDecimalForFloat(getProfitInEstimationForAllOrders());
        mActualProfitsForAllOrders = NumberUtils
                .keepTwoDigitsDecimalForFloat(getActualProfitForAllShippedOrders());
        mNetEstimatedProfitsForAllOrders = NumberUtils
                .keepTwoDigitsDecimalForFloat(getEstimatedNetCompanyProfitForAllOrders());
        mNetActualProfitsForAllOrders = NumberUtils
                .keepTwoDigitsDecimalForFloat(getActualNetCompanyProfitForShippedOrders());
        mProxyEstimatedProfitsForAllOrders = NumberUtils.keepTwoDigitsDecimalForFloat(
                mEstimatedProfitsForAllOrders - mNetEstimatedProfitsForAllOrders);
        mProxyActualProfitsForAllOrders = NumberUtils.keepTwoDigitsDecimalForFloat(
                mActualProfitsForAllOrders - mNetActualProfitsForAllOrders);
        mTotalTurnOver = calculateTotalTurnOver();
        mTotalMoneyInFly = calculatTotalMoneyInFly();
    }

    /**
     * Calculate the profit in estimation. This calculation is for all the
     * orders even if the order is not shipped yet.
     * 
     * ProfitInTheory = SellingPrice - ActualBuyingCost - EstimatedShippingFee
     */
    private Float getProfitInEstimationForAllOrders()
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
    private Float getEstimatedNetCompanyProfitForAllOrders()
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
        return order.getOrderFinalProfit();
    }

    /**
     * @param order
     * @return
     */
    private float getEstimatedProfitForOrder(final Order order)
    {
        // 如果实际运费不为0 那么就用实际运费来估计
        if (NumberUtils.floatEquals(order.getOrderActualShippingFee(), 0.0f))
        {
            return order.getOrderSellingPrice()
                    - order.getOrderActualCost() * order.getOrderCurrencyRate()
                    - order.getOrderEstimatedShippingFee();
        }
        else
        {
            return order.getOrderSellingPrice()
                    - order.getOrderActualCost() * order.getOrderCurrencyRate()
                    - order.getOrderActualShippingFee();
        }
    }

    /**
     * 计算总营业额
     * 
     * @return
     */
    private Float calculateTotalTurnOver()
    {
        if (CollectionUtils.isEmpty(mOrderList))
        {
            return 0.0f;
        }

        float totalTurnOver = 0.0f;
        for (final Order order : mOrderList)
        {
            float singleActualNetCompnayProfit = order.getOrderSellingPrice();
            totalTurnOver += singleActualNetCompnayProfit;
        }
        return Float.valueOf(totalTurnOver);
    }

    /**
     * 计算总共在路上的钱 （用户还未付款 或代理还未付款）
     * 
     * @return
     */
    private Float calculatTotalMoneyInFly()
    {
        if (CollectionUtils.isEmpty(mOrderList))
        {
            return 0.0f;
        }

        float totalMoneyInFly = 0.0f;
        for (final Order order : mOrderList)
        {
            // 购买成本
            float buyingCost = order.getOrderActualCost();
            // 国际运费， 若为0 则用估计运费
            float shippingFee = NumberUtils.floatEquals(order.getOrderActualShippingFee(), 0.0f)
                    ? order.getOrderEstimatedShippingFee() : order.getOrderActualShippingFee();
            // 售价
            float sellingPrice = order.getOrderSellingPrice();
            // 代理分红百分比
            float proxyProfitPercentage = order.getOrderProxyProfitPercentage();
            // 公司已收款
            float companyReceivedMoney = order.getOrderCompanyReceivedMoney();

            // 公司在路上的钱＝（成本＋公司利润）－公司已收的钱 ＝ （购买成本＋国际运费）＋（售价－购买成本－国际运费）＊（1-代理分红百分比）
            // － 公司已收的钱
            // 这里忽略了运费可能没有如实付国内运费的情况。
            float singleMoneyInFly = (buyingCost + shippingFee)
                    + (sellingPrice - buyingCost - shippingFee) * (1 - proxyProfitPercentage)
                    - companyReceivedMoney;
            totalMoneyInFly += singleMoneyInFly;
        }
        return Float.valueOf(totalMoneyInFly);
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

    /**
     * @return the proxyEstimatedProfitsForAllOrders
     */
    public Float getProxyEstimatedProfitsForAllOrders()
    {
        return mProxyEstimatedProfitsForAllOrders;
    }

    /**
     * @return the proxyActualProfitsForAllOrders
     */
    public Float getProxyActualProfitsForAllOrders()
    {
        return mProxyActualProfitsForAllOrders;
    }

    /**
     * @return the total turn over
     */
    public Float getTotalTurnOver()
    {
        return mTotalTurnOver;
    }

    public Float getTotalMoneyInFly()
    {
        return mTotalMoneyInFly;
    }
}
