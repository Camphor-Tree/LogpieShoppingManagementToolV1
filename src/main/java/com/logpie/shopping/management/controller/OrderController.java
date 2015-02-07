// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.logpie.shopping.management.auth.logic.AuthenticationHelper;
import com.logpie.shopping.management.auth.logic.LogpiePageAlertMessage;

/**
 * @author zhoyilei
 *
 */
@Controller
public class OrderController
{
    private static final Logger LOG = Logger.getLogger(OrderController.class);

    @RequestMapping(value = "/order_management", method = RequestMethod.GET)
    public Object showSignInPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to order manage page.");
            final ModelAndView orderManagementPage = new ModelAndView("order_management");
            if (redirectAttrs.containsAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE))
            {
                final String message = (String) redirectAttrs.getFlashAttributes().get(
                        LogpiePageAlertMessage.KEY_ACTION_MESSAGE);
                orderManagementPage.addObject(LogpiePageAlertMessage.KEY_ACTION_MESSAGE, message);
            }
            return orderManagementPage;
        }
        return "redirect:/signin";

    }
}
