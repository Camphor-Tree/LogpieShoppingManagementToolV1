// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author zhoyilei
 *
 */
@Controller
public class DBLogController
{
    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public Object showLogPage(HttpServletRequest request, HttpServletResponse httpResponse)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showLogPage(request, httpResponse);
    }

}
