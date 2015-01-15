package com.logpie.shopping.management.home.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.logpie.shopping.management.auth.logic.Admin;
import com.logpie.shopping.management.auth.logic.AuthenticationHelper;

@Controller
public class HomeController
{
    private static final Logger LOG = Logger.getLogger(HomeController.class);

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public Object showSignInPage(HttpServletRequest request, HttpServletResponse httpResponse)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            Admin admin = AuthenticationHelper.getAdminFromCookie(request);

            final ModelAndView signinPage = new ModelAndView("home");
            signinPage.addObject("AdminName", admin.getAdminName());
            return signinPage;
        }
        return "redirect:/signin";

    }
}
