// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.omg.CORBA.SystemException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.logpie.shopping.management.auth.logic.AuthenticationHelper;
import com.logpie.shopping.management.util.LogpieUrlEnum;

/**
 * This Interceptor is used to intercept all resources accessing. It will
 * validate the auth cookies. If the cookie is invalid, it will redirect the
 * page to sign-in page to re-authenticate.
 * 
 * @author zhoyilei
 *
 */
public class AuthInterceptor extends HandlerInterceptorAdapter
{
    private static Logger LOG = Logger.getLogger(AuthInterceptor.class);

    /**
     * In this case intercept the request BEFORE it reaches the controller
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception
    {
        try
        {
            final String requestURI = request.getRequestURI();
            LOG.info("AuthInterceptor Intercepting: " + requestURI + " URL:"
                    + request.getRequestURL());
            // if the request URI is /signin, then just let it pass through to
            // sign-in page.
            if (requestURI.equals(LogpieUrlEnum.LogpieSigninURL))
            {
                LOG.info("Meeting sign-in url, rendering the sign-in page");
                return true;
            }

            if (requestURI.startsWith(LogpieUrlEnum.LogpieResourcesURL))
            {
                LOG.info("Requesting resources, let it pass through");
                return true;
            }

            // if the request URI is calculator, then just let it pass.
            if (requestURI.equals(LogpieUrlEnum.LogpieCalculatorURL))
            {
                LOG.info("Meeting calculator url, rendering the calculator page");
                return true;
            }

            // if the request URI is wechat integration, then just let it pass.
            if (requestURI.equals(LogpieUrlEnum.LogpieWechatIntegrationURL))
            {
                LOG.info("Meeting wechat integration url, let it pass through");
                return true;
            }

            // String requestUrl = request.getRequestURL().toString();
            // if (!StringUtils.isEmpty(request.getQueryString()))
            // {
            // requestUrl += "?" + request.getQueryString();
            // }
            // PageHistoryHandler.handlePageHistory(request, response,
            // requestUrl);

            final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
            if (authSuccess)
            {
                return true;
            }
            else
            {
                LOG.info("The authcookie is expired or invalid, redirecting to sign_in page");
                response.sendRedirect("/LogpieShopping/signin");
                return false;
            }
        } catch (SystemException e)
        {
            LOG.error("request update failed", e);
            return false;
        }
    }
}
