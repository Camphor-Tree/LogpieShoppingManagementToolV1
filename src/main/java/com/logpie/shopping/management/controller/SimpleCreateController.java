// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author zhoyilei
 *
 */
@Controller
public class SimpleCreateController
{
    private static final Logger LOG = Logger.getLogger(SimpleCreateController.class);

    @RequestMapping(value = "/category/create", method = RequestMethod.GET)
    public Object showCreateCategoryPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showCreateCategoryPage(request, httpResponse);
    }

    @RequestMapping(value = "/category/create", method = RequestMethod.POST)
    public Object createCategory(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.createCategory(request, httpResponse, redirectAttrs);
    }

    /**
     * Only super admin can create admin account
     * 
     * @param request
     * @param httpResponse
     * @return
     */
    @RequestMapping(value = "/admin/create", method = RequestMethod.POST)
    public Object createAdmin(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.createAdmin(request, httpResponse, redirectAttrs);
    }

    @RequestMapping(value = "/image/create", method = RequestMethod.POST)
    public Object createImage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.createImage(request, httpResponse, redirectAttrs);
    }

    @RequestMapping(value = "/brand/create", method = RequestMethod.POST)
    public Object createBrand(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.createBrand(request, httpResponse, redirectAttrs);
    }

    @RequestMapping(value = "/product/create", method = RequestMethod.POST)
    public Object createProduct(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.createProduct(request, httpResponse, redirectAttrs);
    }

    @RequestMapping(value = "/client/create", method = RequestMethod.POST)
    public Object createClient(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.createClient(request, httpResponse, redirectAttrs);
    }
}
