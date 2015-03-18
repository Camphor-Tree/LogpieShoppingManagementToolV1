package com.logpie.shopping.management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.logpie.shopping.management.auth.logic.AuthenticationHelper;
import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.util.CurrencyRateUtils;

@Controller
public class HomeController
{
    private static final Logger LOG = Logger.getLogger(HomeController.class);

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public Object showHomePage(HttpServletRequest request, HttpServletResponse httpResponse)
    {
        LOG.debug("Authenticate cookie is valid. Going to home page.");
        Admin admin = AuthenticationHelper.getAdminFromCookie(request);
        final ModelAndView homePage = new ModelAndView("home");
        homePage.addObject("AdminName", admin.getAdminName());
        final float currencyRate = CurrencyRateUtils.getUScurrencyRate();
        homePage.addObject("CurrencyRate", currencyRate);
        return homePage;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object redirectToHomePage(HttpServletRequest request, HttpServletResponse httpResponse)
    {
        return "redirect:/home";
    }
}
