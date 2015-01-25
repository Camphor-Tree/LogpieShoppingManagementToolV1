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
    public ModelAndView showSignInPage(HttpServletRequest request, HttpServletResponse httpResponse)
    {
        final ModelAndView signinPage = new ModelAndView("signin");
        return signinPage;
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public Object verifyPassword(HttpServletRequest request, HttpServletResponse httpResponse)
    {
        LOG.debug("receiving post signin request");
        final String email = request.getParameter("email");
        final String password = request.getParameter("password");
        AdminDAO adminDAO = new AdminDAO();
        Admin admin = adminDAO.verifyAccount(email, password);
        if (admin == null)
        {
            return "redirect:/signin";
        }
        else
        {
            final CookieManager cookieManager = new CookieManager();
            Cookie cookie = cookieManager.setupAuthCookie(admin);
            httpResponse.addCookie(cookie);
            return "redirect:/home";
        }

    }
}
