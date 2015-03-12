// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

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
    public Object showOrderManagementPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs,
            @RequestParam(value = "admin", required = false) final String adminId,
            @RequestParam(value = "buyer", required = false) final String buyerName,
            @RequestParam(value = "packageId", required = false) final String packageId)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showOrderManagementPage(request, httpResponse,
                redirectAttrs, adminId, buyerName, packageId);
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
            final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showModifyOrderPage(request, httpResponse, orderId,
                redirectAttrs);
    }

    @RequestMapping(value = "/order/edit", method = RequestMethod.POST)
    public Object modifyOrder(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.modifyOrder(request, httpResponse, redirectAttrs);
    }
}
