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
public class SettleDownRecordController
{
    @RequestMapping(value = "/settle_down_history", method = RequestMethod.GET)
    public Object showSettleDownRecordPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showSettleDownRecordPage(request, httpResponse);
    }
}
