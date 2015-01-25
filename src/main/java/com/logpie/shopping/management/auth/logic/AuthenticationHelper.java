// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.auth.logic;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.logpie.shopping.management.storage.AdminDAO;

/**
 * @author zhoyilei
 *
 */
public class AuthenticationHelper
{
    private static final Logger LOG = Logger.getLogger(AuthenticationHelper.class);
    private static final CookieManager sCookieManager = new CookieManager();

    public static boolean handleAuthentication(final HttpServletRequest request)
    {
        if (request == null)
        {
            return false;
        }

        final Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0)
        {
            LOG.debug("no cookies are set, authentication fail");
            return false;
        }

        for (final Cookie cookie : cookies)
        {
            final String cookieName = cookie.getName();
            // Only need to verify the authentication cookie
            if (cookieName != null && cookieName.equals(CookieManager.AUTH_COOKIE_NAME))
            {
                return verifyAuthCookie(cookie);
            }
        }
        return false;
    }

    private static boolean verifyAuthCookie(final Cookie cookie)
    {
        final String cookieValue = cookie.getValue();
        return sCookieManager.validateCookie(cookieValue);
    }

    public static Admin getAdminFromCookie(final HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }

        final Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0)
        {
            LOG.debug("no cookies are set, something must be wrong");
            return null;
        }

        for (final Cookie cookie : cookies)
        {
            final String cookieName = cookie.getName();
            // Only need to verify the authentication cookie
            if (cookieName != null && cookieName.equals(CookieManager.AUTH_COOKIE_NAME))
            {
                final String adminId = sCookieManager.getAdminIdFromCookie(cookie.getValue());
                AdminDAO adminDAO = new AdminDAO();
                return adminDAO.queryAccountByAdminId(adminId);
            }
        }
        return null;
    }
}
