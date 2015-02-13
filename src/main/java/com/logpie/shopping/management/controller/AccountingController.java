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

/**
 * @author zhoyilei
 *
 */
@Controller
public class AccountingController
{
    private static final Logger LOG = Logger.getLogger(AccountingController.class);

    @RequestMapping(value = "/accounting", method = RequestMethod.GET)
    public Object showAccountingPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to accounting page.");
            final ModelAndView productManagementPage = new ModelAndView("accounting");
            return productManagementPage;
        }
        return "redirect:/signin";
    }
}
