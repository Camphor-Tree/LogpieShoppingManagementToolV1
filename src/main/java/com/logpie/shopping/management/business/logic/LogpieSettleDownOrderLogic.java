// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.business.logic;

import com.logpie.shopping.management.model.Order;

/**
 * This class is used to handle our monthly clearing orders with our proxies and
 * customers.
 * 
 * @author zhoyilei
 *
 */
public class LogpieSettleDownOrderLogic
{
    public boolean isOrderAlreadySettleDown(final Order order)
    {
        if (order == null)
        {
            return false;
        }
        // 订单的售价。（一般包括国际邮费）
        final float sellingPrice = order.getOrderSellingPrice();
        // 用户已付国内邮费
        final float customerPaidDomesticShippingFee = order
                .getOrderCustomerPaidDomesticShippingFee();
        final float domesticShippingFee = order.getOrderDomesticShippingFee();

        // 用户已付款。 可能付给代理也可能付给公司。 也是实收账款。
        final float customerPaidMoney = order.getOrderCustomerPaidMoney();
        // 公司已收到的钱
        final float companyReceivedMoney = order.getOrderCompanyReceivedMoney();
        // 利润是否已结算
        final boolean isProfitPaidToProxy = order.getOrderIsProfitPaid();
        // 已向用户发货
        final boolean sentToUser = order.getOrderSentToUser();

        // A. 如果 用户付款数 等于 卖价+国内用户已付邮费（可能为0， 可能不等于国内运费)
        // B. 如果 公司收款 等于 用户已付钱数减去国内运费 (一般是代理垫付的)
        // C. 如果 利润已结算给代理
        // D. 如果 卖价不为0 那么用户付款数不能为0
        // E. 如果 已向用户发货为true
        // 那么 改订单已完全清算。
        if (floatEquals(sellingPrice + customerPaidDomesticShippingFee, customerPaidMoney)
                && floatEquals(customerPaidMoney - domesticShippingFee, companyReceivedMoney)
                && isProfitPaidToProxy && sentToUser)
        {
            if (!floatEquals(0.0f, sellingPrice) && floatEquals(customerPaidMoney, 0.0f))
            {
                // 如果卖价不为0 而买家付款为0 则该订单有问题 不该设成已结算
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isOrderNeedSettleDown(final Order order)
    {
        // 已向用户发货
        final boolean sentToUser = order.getOrderSentToUser();
        return sentToUser && !isOrderAlreadySettleDown(order);
    }

    private boolean floatEquals(final float numberA, final float numberB)
    {
        if (numberA - numberB <= 0.01f && numberA - numberB >= -0.01f)
        {
            return true;
        }
        return false;

    }
}
