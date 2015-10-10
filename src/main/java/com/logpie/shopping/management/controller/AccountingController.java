// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
// TODO move to LogpieControllerImplementation. Don't forget to inject current
// admin to DAOs
public class AccountingController extends LogpieBaseController
{
    @RequestMapping(value = "/accounting", method = RequestMethod.GET)
    public Object showAccountingPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        Object object = logpieControllerImplementation.showAccountingPage(request, httpResponse,
                redirectAttrs);
        super.injectCurrentActiveTab(object, "accounting");
        return object;
    }

    /**
     * PieChart showing each category's order number It will show the all the
     * orders. If you specify a year and month, it will also show a specific
     * month's result
     */
    @RequestMapping(value = "/accounting/piechart", method = RequestMethod.GET)
    public Object showPieChartAccounting(final HttpServletRequest request,
            final HttpServletResponse httpResponse,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "year_month", required = false) String yearMonth)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showPieChartAccounting(request, httpResponse, type,
                yearMonth);
    }

    /**
     * LineChart showing the numbers of orders, profits Logpie makes in daily or
     * monthly mode.
     */
    @RequestMapping(value = "/accounting/linechart", method = RequestMethod.GET)
    public Object showLineChartAccounting(final HttpServletRequest request,
            final HttpServletResponse httpResponse,
            @RequestParam(value = "type", required = true) String type)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showLineChartAccounting(request, httpResponse, type);
    }
}
