// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.auth.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zhoyilei
 *
 */
public class PageHistoryHandler
{
    private static final Logger LOG = Logger.getLogger(PageHistoryHandler.class);
    private static final String sPageHistoryCookieName = "logpie.page_history";
    private static final String sPrevious1Url = "prev_1_url";
    private static final String sPrevious2Url = "prev_2_url";
    private static final String sCurrentUrl = "cur_url";

    public static void handlePageHistory(final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse, final String currentUrl)
    {
        final Cookie[] cookies = httpServletRequest.getCookies();
        final String method = httpServletRequest.getMethod();
        // We don't need to track the post request in history. Because the post
        // url may not be legal to directly load.
        if (method.equals("POST"))
        {
            return;
        }
        if (cookies == null || cookies.length == 0)
        {
            return;
        }

        for (final Cookie cookie : cookies)
        {
            final String cookieName = cookie.getName();
            // Only need to verify the authentication cookie
            if (cookieName != null && cookieName.equals(sPageHistoryCookieName))
            {
                final JSONObject cookieJSON = getPageHistoryCookieJSON(cookie);
                buildPageHistoryCookie(cookieJSON, currentUrl);
                setupPageHistoryCookie(httpServletResponse, cookieJSON);
                return;
            }
        }
        LOG.debug("no page history cookies are set, create a new one");
        final JSONObject cookieJSON = getNewPageHistoryCookieJSON(currentUrl);
        setupPageHistoryCookie(httpServletResponse, cookieJSON);
    }

    public static String getPrevious1Url(final HttpServletRequest httpServletRequest)
    {
        return getPreviousUrl(httpServletRequest, sPrevious1Url);
    }

    public static String getPrevious2Url(final HttpServletRequest httpServletRequest)
    {
        return getPreviousUrl(httpServletRequest, sPrevious2Url);
    }

    public static String getPreviousUrl(final HttpServletRequest httpServletRequest,
            final String key)
    {
        final Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null || cookies.length == 0)
        {
            LOG.error("no page history cookies are set");
            return null;
        }

        for (final Cookie cookie : cookies)
        {
            final String cookieName = cookie.getName();
            // Only need to verify the authentication cookie
            if (cookieName != null && cookieName.equals(sPageHistoryCookieName))
            {
                final JSONObject cookieJSON = getPageHistoryCookieJSON(cookie);
                try
                {
                    final String previousUrl = cookieJSON.getString(key);
                    return previousUrl;
                } catch (JSONException e)
                {
                    LOG.error("no previous 2 url set in page history cookie");
                    return null;
                }
            }
        }
        LOG.error("no history cookie found");
        return null;
    }

    private static Cookie setupPageHistoryCookie(final HttpServletResponse httpServletResponse,
            final JSONObject cookieJSON)
    {
        try
        {
            final Cookie pageHistoryCookie = new Cookie(sPageHistoryCookieName,
                    Base64.encodeBase64String(cookieJSON.toString().getBytes("UTF-8")));
            pageHistoryCookie.setPath("/");
            // Set domain to .logpie.com so that it can work for both
            // tool.logpie.com and t.logpie.com
            // But it won't work on local debug mode. So currently just set it
            // default full domain
            // authCookie.setDomain(".logpie.com");
            httpServletResponse.addCookie(pageHistoryCookie);
        } catch (UnsupportedEncodingException e)
        {
            LOG.debug("UTF-8 is not supported, which is not possible.");
        }
        return null;
    }

    private static JSONObject getNewPageHistoryCookieJSON(final String currentUrl)
    {
        final JSONObject cookieJSON = new JSONObject();
        try
        {
            cookieJSON.put(sPrevious1Url, "");
            cookieJSON.put(sPrevious2Url, "");
            cookieJSON.put(sCurrentUrl, currentUrl);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return cookieJSON;
    }

    private static void buildPageHistoryCookie(final JSONObject currentPageHistoryCookieJSON,
            final String newCurrentUrl)
    {
        try
        {
            final String previous1url = currentPageHistoryCookieJSON.getString(sPrevious1Url);
            final String currentUrl = currentPageHistoryCookieJSON.getString(sCurrentUrl);
            currentPageHistoryCookieJSON.put(sPrevious1Url, currentUrl);
            currentPageHistoryCookieJSON.put(sPrevious2Url, previous1url);
            currentPageHistoryCookieJSON.put(sCurrentUrl, newCurrentUrl);
        } catch (JSONException e)
        {
            LOG.error("JSONException happened when try to build new page history cookie", e);
        }

    }

    private static JSONObject getPageHistoryCookieJSON(final Cookie pageHistoryCookie)
    {
        try
        {
            final String base64EncodedCookie = pageHistoryCookie.getValue();
            final JSONObject cookieJSON = new JSONObject(new String(
                    Base64.decodeBase64(base64EncodedCookie), "UTF-8"));
            return cookieJSON;
        } catch (UnsupportedEncodingException | JSONException e)
        {

        }
        return null;
    }
}
