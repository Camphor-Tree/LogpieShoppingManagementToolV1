package com.logpie.shopping.management.auth.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.logpie.shopping.management.auth.logic.Admin;
import com.logpie.shopping.management.auth.logic.CookieManager;

@Controller
public class AuthController
{

    @RequestMapping(value = "/signin", produces = "text/plain; charset=utf-8")
    public ModelAndView showHomePage(HttpServletResponse httpResponse)
    {
        ModelAndView homePage = new ModelAndView("signin");
        CookieManager cookieManager = new CookieManager();
        Cookie cookie = cookieManager.setupAuthCookie(new Admin("id", "name", "email", "qq",
                "wechat", "phone", "passversion", "idnumber"));
        httpResponse.addCookie(cookie);
        return homePage;
    }
}
