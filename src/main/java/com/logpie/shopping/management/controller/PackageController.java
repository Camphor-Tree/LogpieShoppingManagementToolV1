// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.logpie.shopping.management.auth.logic.AuthenticationHelper;
import com.logpie.shopping.management.model.LogpiePackage;
import com.logpie.shopping.management.storage.LogpiePackageDAO;

/**
 * @author zhoyilei
 *
 */
@Controller
public class PackageController
{
    private static final Logger LOG = Logger.getLogger(PackageController.class);

    /**
     * Show singple package information
     * 
     * @param request
     * @param packageId
     * @return
     */
    @RequestMapping(value = "/package", method = RequestMethod.GET)
    public Object showOrderManagementPage(final HttpServletRequest request,
            @RequestParam("id") String packageId)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to package page.");
            final ModelAndView packageDetailPage = new ModelAndView("package_detail");
            final LogpiePackageDAO packageDAO = new LogpiePackageDAO();
            final LogpiePackage logpiePackage = packageDAO.getPackageById(packageId);
            packageDetailPage.addObject("logpiePackage", logpiePackage);
            return packageDetailPage;
        }
        return "redirect:/signin";
    }
}
