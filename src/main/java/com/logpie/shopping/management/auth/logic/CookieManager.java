// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.auth.logic;

import javax.servlet.http.Cookie;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zhoyilei
 * 
 *         This class is responsible for generate cookie.
 */
public class CookieManager
{
    private static final String AUTH_COOKIE_NAME = "logpie.auth_id";
    // auth cookie expires in 1 hour
    private static final int AUTH_COOKIE_EXPIRAION = 60 * 60;
    private final EncryptionManager mEncryptor;

    public CookieManager()
    {
        mEncryptor = new EncryptionManager();
    }

    public Cookie setupAuthCookie(Admin admin)
    {
        Cookie authCookie = new Cookie(AUTH_COOKIE_NAME, buildAuthenticationCookie(admin));
        authCookie.setMaxAge(AUTH_COOKIE_EXPIRAION);
        authCookie.setPath("/");
        return authCookie;
    }

    private String buildAuthenticationCookie(Admin admin)
    {
        final JSONObject cookieJSON = new JSONObject();
        try
        {
            cookieJSON.put("adminId", admin.getAdminId());
            cookieJSON.put("email", admin.getAdminEmail());
            cookieJSON.put("name", admin.getAdminName());
            cookieJSON.put("pass", admin.getPassVersion());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mEncryptor.encrypt(cookieJSON.toString());

    }

}
