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
public class ProductController
{

    @RequestMapping(value = "/product_management", method = RequestMethod.GET)
    public Object showProductManagementPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showProductManagementPage(request, httpResponse,
                redirectAttrs);
    }

    @RequestMapping(value = "/product/edit", method = RequestMethod.GET)
    public Object showModifyproductPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String productId,
            final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showModifyproductPage(request, httpResponse,
                productId, redirectAttrs);
    }

    @RequestMapping(value = "/product/edit", method = RequestMethod.POST)
    public Object modifyProduct(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.modifyProduct(request, httpResponse, redirectAttrs);
    }
}
