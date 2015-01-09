// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.auth.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

/**
 * This class is to handle the encryption for authentication cookie.
 * 
 * @author zhoyilei
 *
 */
public class EncryptionManager
{
    /*
     * HMAC hash for verification of the data.
     */
    private static final String HMAC_SHA256 = "HmacSha256";

    // Internally it will use PKCS7Padding. Because AES's
    private static final String AES_CBC_PKCS7_PADDING = "AES/CBC/PKCS5Padding";
    private static final String AES_SALT = "AES/CBC/PKCS7Padding";
    // This is the cookie version. If we need to upgrade the encryption
    // mechanism, we should change this.
    private static final byte COOKIE_VERSION = 0;
    private static final int HMAC_LENGTH = 8;
    private static final int IV_LENGTH = 16;

    public String encrypt(final String data)
    {
        try
        {
            // GZIP the payload; this saves about 100 bytes
            final ByteArrayOutputStream byteArrayOutputString = new ByteArrayOutputStream();
            final GZIPOutputStream gzipOutputString = new GZIPOutputStream(byteArrayOutputString);
            gzipOutputString.write(data.getBytes("UTF-8"));
            gzipOutputString.close();
            final byte[] dataBytes = byteArrayOutputString.toByteArray();

            // Use AES encryption to encrypt the ziped data.
            final Cipher aesCipher = Cipher.getInstance(AES_CBC_PKCS7_PADDING);
            final Key aesEncryptionKey = generateKey();

            final byte[] iv = generateIVForEncryption();
            aesCipher.init(Cipher.ENCRYPT_MODE, aesEncryptionKey, new IvParameterSpec(iv));
            final byte[] ciphertext = aesCipher.doFinal(dataBytes);

            // Generate the HMAC signature to detect whether the data is
            // changed.
            final Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(generateKey());
            mac.update(iv);
            mac.update(ciphertext);
            final byte[] hmac = mac.doFinal();

            // Format:
            // Bytes Description
            // 0 1-byte version tag (currently version 0)
            // 1-8 First 8 bytes of HmacSha256
            // 9-24 Initialization vector (16 bytes)
            // 25-end Ciphertext
            final byte[] result = new byte[1 + HMAC_LENGTH + IV_LENGTH + ciphertext.length];
            result[0] = COOKIE_VERSION;
            System.arraycopy(hmac, 0, result, 1, HMAC_LENGTH);
            System.arraycopy(iv, 0, result, 1 + HMAC_LENGTH, IV_LENGTH);
            System.arraycopy(ciphertext, 0, result, 1 + HMAC_LENGTH + IV_LENGTH, ciphertext.length);
            return Base64.encodeBase64String(result);
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(final String encrypted) throws Exception
    {
        final byte[] encryptedBytes = Base64.decodeBase64(encrypted);
        if (encryptedBytes.length < (1 + HMAC_LENGTH + IV_LENGTH))
        {
            return null;
        }
        if (encryptedBytes[0] != COOKIE_VERSION)
        {
            return null;
        }

        final byte[] version = new byte[1];
        final byte[] hmac = new byte[8];
        final byte[] iv = new byte[16];
        final byte[] encryptedData = new byte[encryptedBytes.length - 25];
        System.arraycopy(encryptedBytes, 0, version, 0, 1);
        System.arraycopy(encryptedBytes, 1, hmac, 0, 8);
        System.arraycopy(encryptedBytes, 9, iv, 0, 16);
        System.arraycopy(encryptedBytes, 25, encryptedData, 0, encryptedData.length);

        final Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(generateKey());
        mac.update(iv);
        mac.update(encryptedData);
        final byte[] expectedHmac = mac.doFinal();
        for (int i = 0; i < HMAC_LENGTH; i++)
        {
            if (expectedHmac[i] != encryptedBytes[i + 1])
            {
                return null;
            }
        }

        final Cipher cipher = Cipher.getInstance(AES_CBC_PKCS7_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, generateKey(), new IvParameterSpec(iv));

        final byte[] plaintext = cipher.doFinal(encryptedData);

        final ByteArrayInputStream bais = new ByteArrayInputStream(plaintext, 0, plaintext.length);
        final GZIPInputStream gzis = new GZIPInputStream(bais);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int n;
        while ((n = gzis.read(buffer)) > 0)
        {
            baos.write(buffer, 0, n);
        }
        baos.close();

        return new String(baos.toByteArray(), "UTF-8");
    }

    // Currently we just derive the encryption from a specific String
    // TODO: In the future, we should just randomly generate the key, and store
    // the key in the file.
    private Key generateKey()
    {
        final int iterationCount = 1000;
        final int keyLength = 128;
        final String password = "wealllovelogpie";
        try
        {
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            final KeySpec keySpec = new PBEKeySpec(password.toCharArray(),
                    AES_SALT.getBytes("UTF-8"), iterationCount, keyLength);

            final SecretKey secrectKey = factory.generateSecret(keySpec);
            // specify the key is used for AES algorithm
            final Key encryptionKey = new SecretKeySpec(secrectKey.getEncoded(), "AES");
            return encryptionKey;

        } catch (Exception e)
        {
            System.out.println();
        }
        return null;
    }

    private byte[] generateIVForEncryption()
    {
        final SecureRandom random = new SecureRandom();
        final byte[] iv = new byte[16];
        random.nextBytes(iv);
        return iv;
    }
}
