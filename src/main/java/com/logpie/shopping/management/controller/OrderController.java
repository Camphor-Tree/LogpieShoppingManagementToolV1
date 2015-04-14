// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author zhoyilei
 *
 */
@Controller
public class OrderController
{
    private static final Logger LOG = Logger.getLogger(OrderController.class);

    @RequestMapping(value = "/order_management", method = RequestMethod.GET)
    public Object showOrderManagementPage(
            final HttpServletRequest request,
            final HttpServletResponse httpResponse,
            final RedirectAttributes redirectAttrs,
            @RequestParam(value = "admin", required = false) final String adminId,
            @RequestParam(value = "buyer", required = false) final String buyerName,
            @RequestParam(value = "packageId", required = false) final String packageId,
            @RequestParam(value = "showAll", required = false) final Boolean showAll,
            @RequestParam(value = "orderByBuyerName", required = false) final Boolean orderByBuyerName,
            @RequestParam(value = "orderByPackage", required = false) final Boolean orderByPackage)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showOrderManagementPage(request, httpResponse,
                redirectAttrs, adminId, buyerName, packageId, showAll, orderByBuyerName,
                orderByPackage);
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public Object createNewOrder(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.createNewOrder(request, httpResponse, redirectAttrs);
    }

    /**
     * Show singple order information
     * 
     * @param request
     * @param packageId
     * @return
     */
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public Object showOrderDetailPage(final HttpServletRequest request,
            @RequestParam("id") String orderId)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showOrderDetailPage(request, orderId);
    }

    @RequestMapping(value = "/order/edit", method = RequestMethod.GET)
    public Object showModifyOrderPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String orderId,
            @RequestParam("ru") String redirectUrl,
            @RequestParam(value = "anchor", required = false) String anchor,
            final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showModifyOrderPage(request, httpResponse, orderId,
                redirectUrl, anchor, redirectAttrs);
    }

    @RequestMapping(value = "/order/edit", method = RequestMethod.POST)
    public Object modifyOrder(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.modifyOrder(request, httpResponse, redirectAttrs);
    }

    // 订单结算界面
    @RequestMapping(value = "/order/settledown", method = RequestMethod.GET)
    public Object showOrderSettleDownPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("adminId") String adminId,
            final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showOrderSettleDownPage(request, httpResponse,
                adminId, redirectAttrs);
    }

    // 订单结算处理
    @RequestMapping(value = "/order/settledown", method = RequestMethod.POST)
    public Object handleOrderSettleDown(
            final HttpServletRequest request,
            final HttpServletResponse httpResponse,
            @RequestParam("adminId") String adminId,
            @RequestParam(value = "SettleDownOrders", required = false) List<String> settleDownOrders,
            final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.handleOrderSettleDown(request, httpResponse, adminId,
                settleDownOrders, redirectAttrs);
    }
}
