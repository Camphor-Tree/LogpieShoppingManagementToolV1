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
    public boolean isOrderAlreadyCleared(final Order order)
    {
        if (order == null)
        {
            return false;
        }
        // 订单的售价。也是应收账款。
        final float sellingPrice = order.getOrderSellingPrice();
        // 用户已付款。 可能付给代理也可能付给公司。 也是实收账款。
        final float customerPaidMoney = order.getOrderCustomerPaidMoney();
        // 公司已收到的钱
        final float companyReceivedMoney = order.getOrderCompanyReceivedMoney();
        // 利润是否已结算
        final boolean isProfitPaidToProxy = order.getOrderIsProfitPaid();

        // 如果 应收账款等于实收账款 等于公司已收到的钱。并且利润已结算给代理。 那么改订单已结算。
        if (floatEquals(sellingPrice, customerPaidMoney)
                && floatEquals(sellingPrice, companyReceivedMoney) && isProfitPaidToProxy)
        {
            return true;
        }
        return false;
    }

    public boolean isOrderNeedClearing(final Order order)
    {
        return !isOrderNeedClearing(order);
    }

    private boolean floatEquals(final float numberA, final float numberB)
    {
        if (numberA - numberB < 0.01f && numberA - numberB > -0.01f)
        {
            return true;
        }
        return false;

    }
}
