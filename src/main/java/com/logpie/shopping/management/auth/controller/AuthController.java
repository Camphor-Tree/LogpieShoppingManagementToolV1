package com.logpie.shopping.management.auth.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.logpie.shopping.management.auth.logic.Admin;
import com.logpie.shopping.management.auth.logic.CookieManager;
import com.logpie.shopping.management.storage.AdminDAO;

@Controller
public class AuthController
{
    private static final Logger LOG = Logger.getLogger(AuthController.class);

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ModelAndView showSignInPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView signinPage = new ModelAndView("signin");
        return signinPage;
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public Object verifyPassword(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        LOG.debug("receiving post signin request");
        final String email = request.getParameter("email");
        final String password = request.getParameter("password");
        final AdminDAO adminDAO = new AdminDAO();
        final Admin admin = adminDAO.verifyAccount(email, password);
        if (admin == null)
        {
            return "redirect:/signin";
        }
        else
        {
            final CookieManager cookieManager = new CookieManager();
            final Cookie cookie = cookieManager.setupAuthCookie(admin);
            httpResponse.addCookie(cookie);
            return "redirect:/home";
        }
    }

    @RequestMapping(value = "/logout")
    public Object logout(final HttpServletRequest request, final HttpServletResponse httpResponse)
    {
        LOG.debug("receiving logout request");
        final CookieManager cookieManager = new CookieManager();
        final Cookie emptyAuthCookie = cookieManager.getEmptyAuthCookie();
        httpResponse.addCookie(emptyAuthCookie);
        return "redirect:/signin";
    }
}
