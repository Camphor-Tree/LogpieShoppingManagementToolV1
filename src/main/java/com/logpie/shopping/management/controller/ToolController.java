// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.logpie.shopping.management.util.CurrencyRateUtils;

/**
 * @author zhoyilei
 *
 */
@Controller
public class ToolController extends LogpieBaseController
{
    private static final Logger LOG = Logger.getLogger(ToolController.class);

    // Show the price calculator
    @RequestMapping(value = "/calculator", method = RequestMethod.GET)
    public Object showCreateCategoryPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView calculatorPage = new ModelAndView("calculator");
        // inject the current currency rate into the page
        final float currencyRate = CurrencyRateUtils.getUScurrencyRate();
        calculatorPage.addObject("CurrencyRate", currencyRate);
        super.injectCurrentActiveTab(calculatorPage, "calculator");
        return calculatorPage;
    }

}
