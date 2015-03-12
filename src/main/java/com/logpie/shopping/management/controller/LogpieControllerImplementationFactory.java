// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import javax.servlet.http.HttpServletRequest;

import com.logpie.shopping.management.auth.logic.AuthenticationHelper;
import com.logpie.shopping.management.model.Admin;

/**
 * Switch the implementation based whether the admin is super admin or not.
 * 
 * @author zhoyilei
 *
 */
public class LogpieControllerImplementationFactory
{
    public static LogpieControllerImplementation getControllerImplementationBasedForAdmin(
            final HttpServletRequest request)
    {
        final Admin admin = AuthenticationHelper.getAdminFromCookie(request);
        if (admin == null)
        {
            return null;
        }
        if (admin.isSuperAdmin())
        {
            return new LogpieSuperAdminControllerImplementation(admin);
        }
        return new LogpieNormalAdminControllerImplementation(admin);
    }
}
