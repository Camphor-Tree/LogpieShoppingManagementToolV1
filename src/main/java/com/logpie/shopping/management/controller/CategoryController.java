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
public class CategoryController
{
    @RequestMapping(value = "/category_management", method = RequestMethod.GET)
    public Object showAllCategories(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showAllCategories(request, httpResponse);
    }

    @RequestMapping(value = "/category/edit", method = RequestMethod.GET)
    public Object showModifyCategoryPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String categoryId,
            final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showModifyCategoryPage(request, httpResponse,
                categoryId, redirectAttrs);
    }

    @RequestMapping(value = "/category/edit", method = RequestMethod.POST)
    public Object modifyCategory(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.modifyCategory(request, httpResponse, redirectAttrs);
    }
}
