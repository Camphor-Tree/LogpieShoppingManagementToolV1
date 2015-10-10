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
public class ClientController extends LogpieBaseController
{
    @RequestMapping(value = "/client_management", method = RequestMethod.GET)
    public Object showAllClients(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        Object object = logpieControllerImplementation.showAllClients(request, httpResponse);
        super.injectCurrentActiveTab(object, "client_management");
        return object;
    }

    @RequestMapping(value = "/client/edit", method = RequestMethod.GET)
    public Object showModifyclientPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String clientId,
            final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showModifyClientPage(request, httpResponse, clientId,
                redirectAttrs);
    }

    @RequestMapping(value = "/client/edit", method = RequestMethod.POST)
    public Object modifyclient(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.modifyClient(request, httpResponse, redirectAttrs);
    }

}
