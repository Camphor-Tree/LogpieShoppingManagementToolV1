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
public class BrandController
{
    @RequestMapping(value = "/brand_management", method = RequestMethod.GET)
    public Object showAllCategories(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showAllBrands(request, httpResponse);
    }

    @RequestMapping(value = "/brand/edit", method = RequestMethod.GET)
    public Object showModifybrandPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String brandId,
            final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showModifybrandPage(request, httpResponse, brandId,
                redirectAttrs);
    }

    @RequestMapping(value = "/brand/edit", method = RequestMethod.POST)
    public Object modifyBrand(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.modifyBrand(request, httpResponse, redirectAttrs);
    }

}
