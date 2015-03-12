// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.Order;
import com.logpie.shopping.management.storage.OrderDAO;

/**
 * @author zhoyilei
 *
 */
public class LogpieNormalAdminControllerImplementation extends LogpieControllerImplementation
{
    LogpieNormalAdminControllerImplementation(final Admin admin)
    {
        super(admin);
    }

    /**
     * For Package Controller
     * 
     * Normal admin is not allowed to access package management.
     */
    @Override
    Object showPackageManagementPage(HttpServletRequest request, HttpServletResponse httpResponse,
            RedirectAttributes redirectAttrs)
    {
        return showNoPermissionPage();
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
    public List<Order> injectOrderManagementOrderList()
    {
        final OrderDAO orderDAO = new OrderDAO();
        return orderDAO.getOrdersForProxy(mCurrentAdmin.getAdminId());
    }

}
