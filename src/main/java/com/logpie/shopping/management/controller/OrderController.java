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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author zhoyilei
 *
 */
@Controller
public class OrderController extends LogpieBaseController
{
    private static final Logger LOG = Logger.getLogger(OrderController.class);

    @RequestMapping(value = "/order_management", method = RequestMethod.GET)
    public Object showOrderManagementPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs,
            @RequestParam(value = "admin", required = false) final String adminId,
            @RequestParam(value = "buyer", required = false) final String buyerName,
            @RequestParam(value = "packageId", required = false) final String packageId,
            @RequestParam(value = "showAll", required = false) final Boolean showAll,
            @RequestParam(value = "orderBy", required = false) final String orderBy)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        Object object = logpieControllerImplementation.showOrderManagementPage(request,
                httpResponse, redirectAttrs, adminId, buyerName, packageId, showAll, orderBy);

        super.injectCurrentActiveTab(object, "order_management");
        return object;
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

    @RequestMapping(value = "/order/quick_edit/receive_money", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public @ResponseBody String quickEditReceiveMoney(final HttpServletRequest request,
            @RequestParam("id") String orderId,
            @RequestParam("domestic_shipping_fee") String domesticShippingFee)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.quickEditReceiveMoney(orderId, domesticShippingFee);
    }

    @RequestMapping(value = "/order/quick_edit/set_package", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public @ResponseBody String quickSetPackage(final HttpServletRequest request,
            @RequestParam("id") String orderId, @RequestParam("package_id") String packageId)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.quickSetPackage(orderId, packageId);
    }

    @RequestMapping(value = "/order/query", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public @ResponseBody String querySingleOrder(final HttpServletRequest request,
            @RequestParam("id") String orderId)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.querySingleOrder(orderId);
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
    public Object handleOrderSettleDown(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("adminId") String adminId,
            @RequestParam(value = "SettleDownOrders", required = true) List<String> settleDownOrders,
            @RequestParam(value = "ProxyOweCompanyMoney", required = true) String proxyOweCompanyMoney,
            @RequestParam(value = "ProxyProfit", required = true) String proxyProfit,
            @RequestParam(value = "CompanyProfit", required = true) String companyProfit,
            final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.handleOrderSettleDown(request, httpResponse, adminId,
                settleDownOrders, proxyOweCompanyMoney, proxyProfit, companyProfit, redirectAttrs);
    }
}
