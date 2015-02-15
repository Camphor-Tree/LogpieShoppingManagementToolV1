// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.auth.logic;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.storage.AdminDAO;

/**
 * @author zhoyilei
 * 
 *         This class is responsible for generate cookie.
 */
public class CookieManager
{
    private static final Logger LOG = Logger.getLogger(CookieManager.class);

    public static final String AUTH_COOKIE_NAME = "logpie.auth_id";
    // auth cookie expires in 1 week
    private static final int AUTH_COOKIE_DURATION = 7 * 24 * 60 * 60;
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

    public Cookie setupAuthCookie(final Admin admin)
    {
        Cookie authCookie = new Cookie(AUTH_COOKIE_NAME, buildAuthenticationCookie(admin));
        authCookie.setMaxAge(AUTH_COOKIE_DURATION);
        authCookie.setPath("/");
        return authCookie;
    }

    public Cookie getEmptyAuthCookie()
    {
        Cookie emptyAuthCookie = new Cookie(AUTH_COOKIE_NAME, "");
        emptyAuthCookie.setMaxAge(0);
        emptyAuthCookie.setPath("/");
        return emptyAuthCookie;
    }

    private String buildAuthenticationCookie(final Admin admin)
    {
        final JSONObject cookieJSON = new JSONObject();
        long currentTime = System.currentTimeMillis();
        try
        {
            cookieJSON.put("adminId", admin.getAdminId());
            cookieJSON.put("email", admin.getAdminEmail());
            cookieJSON.put("name", admin.getAdminName());
            // cookieJSON.put("passVersion", admin.getPassVersion());
            cookieJSON.put("expires_in",
                    String.valueOf(currentTime + AUTH_COOKIE_EXPIRATION_MILLIS));
        } catch (JSONException e)
        {
            LOG.error("JSONException when building authentication cookie", e);
        }
        return mEncryptor.encrypt(cookieJSON.toString());
    }

    public boolean validateCookie(final String cookieValue)
    {
        try
        {
            final String cookie = mEncryptor.decrypt(cookieValue);
            final JSONObject cookieJSON = new JSONObject(cookie);

            final String adminId = cookieJSON.getString("adminId");
            final String adminEmail = cookieJSON.getString("email");
            final String adminName = cookieJSON.getString("name");
            // TODO: we have implemented passVersion
            // final String adminPassVersion =
            // cookieJSON.getString("passVersion");
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.queryAccountByAdminId(adminId);
            if (admin == null)
            {
                LOG.error("No such admin id!");
                return false;
            }
            final String expectedEmail = admin.getAdminEmail();
            final String expectedName = admin.getAdminName();
            // final String expectedPassVersion = admin.getPassVersion();

            if (!adminEmail.equals(expectedEmail))
            {
                LOG.error("Email changed or doesn't match!");
                return false;
            }
            if (!adminName.equals(expectedName))
            {
                LOG.error("Name changed or doesn't match!");
                return false;
            }
            final String cookieExpiration = cookieJSON.getString("expires_in");

            return checkExpiration(cookieExpiration);
        } catch (Exception e)
        {
            LOG.error("Exception happened when try to parse the cookie", e);
            return false;
        }
    }

    public String getAdminIdFromCookie(final String cookieValue)
    {
        try
        {
            final String cookie = mEncryptor.decrypt(cookieValue);
            final JSONObject cookieJSON = new JSONObject(cookie);
            final String adminId = cookieJSON.getString("adminId");
            return adminId;
        } catch (Exception e)
        {
            LOG.error("Exception happened when trying to get admin id from cookie", e);
        }
        return null;
    }

    private boolean checkExpiration(final String cookieExpiration)
    {
        long cookieExpirationTime = Long.parseLong(cookieExpiration);
        if (cookieExpirationTime > System.currentTimeMillis())
        {
            return true;
        }
        return false;
    }
}
