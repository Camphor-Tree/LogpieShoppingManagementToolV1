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
public class ImageController
{
    @RequestMapping(value = "/image_management", method = RequestMethod.GET)
    public Object showAllCategories(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showAllImages(request, httpResponse);
    }

    @RequestMapping(value = "/image/edit", method = RequestMethod.GET)
    public Object showModifyimagePage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String imageId,
            final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showModifyImagePage(request, httpResponse, imageId,
                redirectAttrs);
    }

    @RequestMapping(value = "/image/edit", method = RequestMethod.POST)
    public Object modifyimage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.modifyImage(request, httpResponse, redirectAttrs);
    }

}
