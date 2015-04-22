// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.coupon;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.thymeleaf.util.StringUtils;

/**
 * @author zhoyilei
 *
 */
public class CouponCodeGenerator
{
    private static Map<String, String> sCouponTypeMapper;
    private static Map<String, String> sCouponValueMapper;
    /*
     * HMAC hash for verification of the data.
     */
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String HMAC_SALT = "HMAC";

    static
    {
        sCouponTypeMapper = new HashMap<String, String>();
        sCouponTypeMapper.put("5", "A");
        sCouponTypeMapper.put("10", "B");
        sCouponTypeMapper.put("15", "C");
        sCouponTypeMapper.put("20", "D");
        sCouponValueMapper = new HashMap<String, String>();
        sCouponValueMapper.put("A", "5");
        sCouponValueMapper.put("B", "10");
        sCouponValueMapper.put("C", "15");
        sCouponValueMapper.put("D", "20");
    }

    private static String generateTimeString()
    {
        final String timeString = String.valueOf(System.currentTimeMillis() / 1000);
        return timeString;
    }

    public static String validateCouponCode(final String couponCode)
    {
        if (couponCode.length() < 5)
        {
            return null;
        }
        final String plainCouponCode = couponCode.substring(1, couponCode.length() - 2);
        try
        {
            final String hmac = getHmac(plainCouponCode.getBytes("UTF-8"));
            final String couponCodeHmac = couponCode.substring(0, 1)
                    + couponCode.substring(couponCode.length() - 2);
            if (StringUtils.equals(hmac, couponCodeHmac))
            {
                final String couponType = plainCouponCode.substring(plainCouponCode.length() - 1);
                return sCouponValueMapper.get(couponType);
            }
        } catch (Exception e)
        {
        }
        return null;
    }

    public static String getCouponCodeByPrice(final int price)
    {
        final StringBuilder couponCodeBuilder = new StringBuilder();
        String couponPrice;
        if (price <= 100)
        {
            couponPrice = "5";
        }
        else if (price <= 500)
        {
            couponPrice = "10";
        }
        else if (price <= 1000)
        {
            couponPrice = "15";
        }
        else
        {
            couponPrice = "20";
        }

        final String couponType = sCouponTypeMapper.get(couponPrice);
        final String timeString = generateTimeString();
        final String plainCouponCode = timeString + couponType;
        try
        {
            final String hmac = getHmac(plainCouponCode.getBytes("UTF-8"));
            couponCodeBuilder.append(hmac.substring(0, 1));
            couponCodeBuilder.append(plainCouponCode);
            couponCodeBuilder.append(hmac.substring(1, 3));
            return couponCodeBuilder.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static String getHmac(final byte[] plainBytes) throws NoSuchAlgorithmException,
            InvalidKeyException, UnsupportedEncodingException, InvalidKeySpecException
    {
        final Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(generateKey());
        mac.update(generateIVForEncryption());
        mac.update(plainBytes);
        final byte[] hmac = mac.doFinal();
        // Get first 3 character as the Hmac string
        return Base64.encodeBase64String(hmac).substring(0, 3);
    }

    // Currently we just derive the encryption from a specific String
    // TODO: In the future, we should just randomly generate the key, and store
    // the key in the file.
    private static Key generateKey() throws NoSuchAlgorithmException, UnsupportedEncodingException,
            InvalidKeySpecException
    {
        final int iterationCount = 1000;
        final int keyLength = 128; // AES-128
        final String password = "LogpieCouponCode";

        final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        final KeySpec keySpec = new PBEKeySpec(password.toCharArray(), HMAC_SALT.getBytes("UTF-8"),
                iterationCount, keyLength);

        final SecretKey secrectKey = factory.generateSecret(keySpec);
        // specify the key is used for AES algorithm
        final Key encryptionKey = new SecretKeySpec(secrectKey.getEncoded(), "HmacSHA1");
        return encryptionKey;
    }

    private static byte[] generateIVForEncryption()
    {
        final byte[] iv = new byte[16];
        return iv;
    }
}
