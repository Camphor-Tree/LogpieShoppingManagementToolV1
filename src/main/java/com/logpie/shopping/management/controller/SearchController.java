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
 * @author Yilei Zhong
 *
 */
@Controller
public class SearchController
{
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Object showSearchResultPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs,
            @RequestParam(value = "key", required = false) final String searchString)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.searchResult(request, httpResponse, redirectAttrs,
                searchString);
    }
}
