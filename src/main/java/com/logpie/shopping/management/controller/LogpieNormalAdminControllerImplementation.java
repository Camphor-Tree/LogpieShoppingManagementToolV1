// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.DBLog;
import com.logpie.shopping.management.model.Order;
import com.logpie.shopping.management.storage.DBLogDAO;
import com.logpie.shopping.management.storage.OrderDAO;

/**
 * @author zhoyilei
 *
 */
public class LogpieNormalAdminControllerImplementation extends LogpieControllerImplementation
{
    private static final Logger LOG = Logger
            .getLogger(LogpieNormalAdminControllerImplementation.class);

    LogpieNormalAdminControllerImplementation(final Admin admin)
    {
        super(admin);
    }

    @Override
    Object quickCalculateShippingFeeDistribution(HttpServletRequest request, String packageId)
    {
        return showNoPermissionPage();
    }

    @Override
    Object createPackage(HttpServletRequest request, HttpServletResponse httpResponse,
            RedirectAttributes redirectAttrs)
    {
        return showNoPermissionPage();
    }

    @Override
    Object quickCreatePackage(HttpServletRequest request, HttpServletResponse httpResponse,
            String adminId, RedirectAttributes redirectAttrs)
    {
        return showNoPermissionPage();
    }

    @Override
    public Object showModifyPackagePage(HttpServletRequest request,
            HttpServletResponse httpResponse, String packageId, RedirectAttributes redirectAttrs)
    {
        return showNoPermissionPage();
    }

    @Override
    public Object modifyPackage(HttpServletRequest request, HttpServletResponse httpResponse,
            RedirectAttributes redirectAttrs)
    {
        return showNoPermissionPage();
    }

    @Override
    public Object markPackageDelivered(HttpServletRequest request, HttpServletResponse httpResponse,
            String packageId, RedirectAttributes redirectAttrs)
    {
        return showNoPermissionPage();
    }

    @Override
    public List<Order> injectOrderManagementOrderList(final String orderByAttributes)
    {
        final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
        return orderDAO.getOrdersForProxy(mCurrentAdmin.getAdminId(), orderByAttributes);
    }

    @Override
    public Object handleOrderSettleDown(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final String adminId,
            final List<String> settleDownOrders, final String proxyOweCompanyMoney,
            final String proxyProfit, final String companyProfit,
            final RedirectAttributes redirectAttrs)
    {
        return showNoPermissionPage();
    }

    @Override
    public Object showLogPage(HttpServletRequest request, HttpServletResponse httpResponse)
    {
        final ModelAndView logPage = new ModelAndView("log");

        final DBLogDAO dbLogDAO = new DBLogDAO(mCurrentAdmin);
        final List<DBLog> dbLogList = dbLogDAO.getDBLogByAdmin(mCurrentAdmin);
        logPage.addObject("dbLogList", dbLogList);

        return logPage;
    }

    @Override
    public Object showWechatSubscriptionManagementPage(HttpServletRequest request,
            HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        return showNoPermissionPage();
    }

    @Override
    public Object createTextAutoReplyRule(HttpServletRequest request,
            HttpServletResponse httpResponse, RedirectAttributes redirectAttrs)
    {
        return showNoPermissionPage();
    }

    @Override
    public Object showEditTextAutoReplyPage(HttpServletRequest request,
            HttpServletResponse httpResponse, final String textAutoReplyRuleId,
            RedirectAttributes redirectAttrs)
    {
        return showNoPermissionPage();
    }

    @Override
    public Object modifyTextAutoReplyRule(HttpServletRequest request,
            HttpServletResponse httpResponse, RedirectAttributes redirectAttrs)
    {
        return showNoPermissionPage();
    }

}
