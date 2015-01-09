// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.auth.logic;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zhoyilei
 * 
 *         This class is responsible for generate cookie.
 */
public class CookieManager
{
    private static final Logger LOG = Logger.getLogger(CookieManager.class);

    private static final String AUTH_COOKIE_NAME = "logpie.auth_id";
    // auth cookie expires in 1 hour
    private static final int AUTH_COOKIE_DURATION = 60 * 60;
    private static final long AUTH_COOKIE_EXPIRATION_MILLIS = AUTH_COOKIE_DURATION * 1000;
    private final EncryptionManager mEncryptor;

    public class AuthException extends Exception
    {
        private static final long serialVersionUID = -8773762794736798391L;

        /**
         * Thrown when cookie expires
         */
        public class CookieExpireException extends AuthException
        {
            private static final long serialVersionUID = 9146604621694359076L;
        }

        /**
         * Thrown when cookie is not valid (cannot be decrypted or the
         * information is not correct)
         */
        public class CookieInvalidException extends AuthException
        {
            private static final long serialVersionUID = 4513536645744726722L;
        }

    }

    public CookieManager()
    {
        mEncryptor = new EncryptionManager();
    }

    public Cookie setupAuthCookie(Admin admin)
    {
        Cookie authCookie = new Cookie(AUTH_COOKIE_NAME, buildAuthenticationCookie(admin));
        authCookie.setMaxAge(AUTH_COOKIE_DURATION);
        authCookie.setPath("/");
        return authCookie;
    }

    private String buildAuthenticationCookie(Admin admin)
    {
        final JSONObject cookieJSON = new JSONObject();
        long currentTime = System.currentTimeMillis();
        try
        {
            cookieJSON.put("adminId", admin.getAdminId());
            cookieJSON.put("email", admin.getAdminEmail());
            cookieJSON.put("name", admin.getAdminName());
            cookieJSON.put("passVersion", admin.getPassVersion());
            cookieJSON.put("expires_in",
                    String.valueOf(currentTime + AUTH_COOKIE_EXPIRATION_MILLIS));
        } catch (JSONException e)
        {
            LOG.error("JSONException when building authentication cookie", e);
        }
        return mEncryptor.encrypt(cookieJSON.toString());

    }

}
