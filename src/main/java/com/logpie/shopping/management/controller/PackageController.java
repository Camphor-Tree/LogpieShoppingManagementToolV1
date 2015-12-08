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
public class PackageController extends LogpieBaseController
{
    @RequestMapping(value = "/package_management", method = RequestMethod.GET)
    public Object showPackageManagementPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs,
            @RequestParam(value = "showAll", required = false) final Boolean showAll,
            @RequestParam(value = "showAllDelivered", required = false) final Boolean showAllDelivered)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        Object object = logpieControllerImplementation.showPackageManagementPage(request,
                httpResponse, redirectAttrs, showAll, showAllDelivered);
        super.injectCurrentActiveTab(object, "package_management");
        return object;
    }

    /**
     * Show singple package information
     * 
     * @param request
     * @param packageId
     * @return
     */
    @RequestMapping(value = "/package", method = RequestMethod.GET)
    public Object showPackageDetailPage(final HttpServletRequest request,
            @RequestParam("id") String packageId)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showPackageDetailPage(request, packageId);
    }

    /**
     * Quick calculate the distribution of the shipping fee in one package.
     * 
     * @param request
     * @param packageId
     * @return
     */
    @RequestMapping(value = "/package/quickCalculateShippingFeeDistribution", method = RequestMethod.GET)
    public Object quickCalculateShippingFeeDistribution(final HttpServletRequest request,
            @RequestParam("id") String packageId)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.quickCalculateShippingFeeDistribution(request,
                packageId);
    }

    @RequestMapping(value = "/package/create", method = RequestMethod.POST)
    public Object createPackage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.createPackage(request, httpResponse, redirectAttrs);
    }

    @RequestMapping(value = "/package/quick_create", method = RequestMethod.GET)
    public Object quickCreatePackakge(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("adminId") String adminId,
            final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.quickCreatePackage(request, httpResponse, adminId,
                redirectAttrs);
    }

    @RequestMapping(value = "/package/edit", method = RequestMethod.GET)
    public Object showModifyPackagePage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String packageId,
            final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showModifyPackagePage(request, httpResponse,
                packageId, redirectAttrs);
    }

    @RequestMapping(value = "/package/edit", method = RequestMethod.POST)
    public Object modifyPackage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.modifyPackage(request, httpResponse, redirectAttrs);
    }

    @RequestMapping(value = "/package/delivered", method = RequestMethod.GET)
    public Object markPackageDelivered(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String packageId,
            final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.markPackageDelivered(request, httpResponse, packageId,
                redirectAttrs);
    }
}
