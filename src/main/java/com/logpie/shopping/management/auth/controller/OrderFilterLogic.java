// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.auth.controller;

import org.springframework.util.StringUtils;

/**
 * @author zhoyilei
 *
 */
public class OrderFilterLogic
{
    private String mAdminId;
    private String mBuyerName;
    private String mOrderBy;
    private Boolean mShowAll;

    /**
     * @param adminId
     * @param buyerName
     * @param orderBy
     * @param showAll
     */
    public OrderFilterLogic(final String adminId, final String buyerName, final String orderBy,
            final Boolean showAll)
    {
        super();
        mAdminId = adminId;
        mBuyerName = buyerName;
        mOrderBy = orderBy;
        mShowAll = showAll;
    }

    public String getOrderByPackageQueryUrl()
    {
        final String orderBy = mOrderBy;
        mOrderBy = "package";
        final String queryUrl = getCurrentQueryString();
        mOrderBy = orderBy;
        return queryUrl;
    }

    public String getOrderByBuyerNameUrl()
    {
        final String orderBy = mOrderBy;
        mOrderBy = "buyerName";
        final String queryUrl = getCurrentQueryString();
        mOrderBy = orderBy;
        return queryUrl;
    }

    public String getOrderByOrderIdUrl()
    {
        final String orderBy = mOrderBy;
        mOrderBy = "orderId";
        final String queryUrl = getCurrentQueryString();
        mOrderBy = orderBy;
        return queryUrl;
    }

    public String getShowAllQueryUrl()
    {
        final Boolean showAll = mShowAll;
        mShowAll = true;
        final String queryUrl = getCurrentQueryString();
        mShowAll = showAll;
        return queryUrl;
    }

    public String getHideSettleDownOrderQueryUrl()
    {
        final Boolean showAll = mShowAll;
        mShowAll = false;
        final String queryUrl = getCurrentQueryString();
        mShowAll = showAll;
        return queryUrl;
    }

    public String getRemoveFilterQueryUrl()
    {
        final String adminId = mAdminId;
        final String buyerName = mBuyerName;
        mAdminId = null;
        mBuyerName = null;
        final String queryString = getCurrentQueryString();
        mAdminId = adminId;
        mBuyerName = buyerName;
        return queryString;
    }

    public String getCurrentQueryString()
    {
        final StringBuilder queryStringBuilder = new StringBuilder();
        if (mShowAll != null && mShowAll)
        {
            queryStringBuilder.append("showAll=true&");
        }

        if (!StringUtils.isEmpty(mOrderBy))
        {
            queryStringBuilder.append("orderBy=");
            queryStringBuilder.append(mOrderBy);
            queryStringBuilder.append("&");
        }

        if (mAdminId != null)
        {
            queryStringBuilder.append("admin=");
            queryStringBuilder.append(mAdminId);
            queryStringBuilder.append("&");
        }
        else if (!StringUtils.isEmpty(mBuyerName))
        {
            queryStringBuilder.append("buyer=");
            queryStringBuilder.append(mBuyerName);
            queryStringBuilder.append("&");
        }
        final String queryString = queryStringBuilder.toString();
        if (queryString.endsWith("&"))
        {
            return queryString.substring(0, queryString.length() - 1);
        }
        else
        {
            return queryString;
        }

    }
}
