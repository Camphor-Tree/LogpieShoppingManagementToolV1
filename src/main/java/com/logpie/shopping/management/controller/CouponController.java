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
public class CouponController
{
    @RequestMapping(value = "/coupon_management", method = RequestMethod.GET)
    public Object showCouponManagementPage(HttpServletRequest request,
            HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showCouponManagementPage(request, httpResponse,
                redirectAttrs);
    }

    @RequestMapping(value = "/coupon/generate", method = RequestMethod.GET)
    public Object generateCoupon(HttpServletRequest request, HttpServletResponse httpResponse,
            @RequestParam(value = "productPrice", required = false) final String productPrice,
            @RequestParam(value = "couponValue", required = false) final String couponValue)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.generateCoupon(request, httpResponse, productPrice,
                couponValue);
    }

    @RequestMapping(value = "/coupon/validate", method = RequestMethod.GET)
    public Object validateCoupon(HttpServletRequest request, HttpServletResponse httpResponse,
            @RequestParam(value = "couponCode", required = false) final String couponCode)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.validateCoupon(request, httpResponse, couponCode);
    }

    @RequestMapping(value = "/coupon/use", method = RequestMethod.POST)
    public Object useCoupon(HttpServletRequest request, HttpServletResponse httpResponse,
            @RequestParam(value = "couponCode", required = false) final String couponCode,
            final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.useCoupon(request, httpResponse, couponCode,
                redirectAttrs);
    }

}
