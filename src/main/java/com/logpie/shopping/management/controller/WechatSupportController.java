// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhoyilei
 *
 */
@Controller
public class WechatSupportController
{
    private static Logger LOG = Logger.getLogger(WechatSupportController.class);
    private static final String WECHAT_INTEGRATION_TOKEN = "LogpieToken";

    // For wechat developer integration
    @RequestMapping(value = "/wechat_integration", method = RequestMethod.GET)
    public Object showOrderManagementPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse,
            @RequestParam(value = "signature", required = true) final String signature,
            @RequestParam(value = "timestamp", required = true) final String timestamp,
            @RequestParam(value = "nonce", required = true) final String nonce,
            @RequestParam(value = "echostr", required = true) final String echostr)
    {

        PrintWriter out = null;
        try
        {
            out = httpResponse.getWriter();
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败
            if (checkSignature(signature, timestamp, nonce))
            {
                LOG.debug("Wechat signature matches, gonna return echo string");
                out.print(echostr);
            }
            else
            {
                LOG.debug("Wechat signature doesn't matches");
            }
        } catch (IOException e)
        {
            LOG.error("IOException when trying to write response", e);
        } finally
        {
            out.close();
            out = null;
        }
        return null;
    }

    private boolean checkSignature(final String signature, final String timestamp,
            final String nonce)
    {
        LOG.debug("Checking signature:" + signature);
        final String[] components = new String[3];
        components[0] = timestamp;
        components[1] = nonce;
        components[2] = WECHAT_INTEGRATION_TOKEN;

        Arrays.sort(components);

        final StringBuilder targetStringBuilder = new StringBuilder();
        for (String component : components)
        {
            targetStringBuilder.append(component);
        }
        final String targetString = targetStringBuilder.toString();

        try
        {
            final MessageDigest digest = MessageDigest.getInstance("SHA-1");
            final byte[] signatureBytes = digest.digest(targetString.getBytes());
            final StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < signatureBytes.length; i++)
            {
                // http://stackoverflow.com/questions/332079/in-java-how-do-i-convert-a-byte-array-to-a-string-of-hex-digits-while-keeping-l
                String hex = Integer.toHexString(0xFF & signatureBytes[i]);
                if (hex.length() == 1)
                {
                    // could use a for loop, but we're only dealing with a
                    // single byte
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            final String logpieComputeSignature = hexString.toString();
            LOG.debug("Logpie computed signature:" + logpieComputeSignature);
            return signature.equals(logpieComputeSignature);
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
