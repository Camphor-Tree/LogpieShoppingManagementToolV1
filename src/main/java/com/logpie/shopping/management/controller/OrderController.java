// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.logpie.shopping.management.auth.logic.AuthenticationHelper;

/**
 * @author zhoyilei
 *
 */
@Controller
public class OrderController
{
    private static final Logger LOG = Logger.getLogger(OrderController.class);

    @RequestMapping(value = "/order_management", method = RequestMethod.GET)
    public Object showSignInPage(HttpServletRequest request, HttpServletResponse httpResponse)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to order manage page.");
            final ModelAndView orderManagementPage = new ModelAndView("order_management");
            return orderManagementPage;
        }
        return "redirect:/signin";

    }

}
