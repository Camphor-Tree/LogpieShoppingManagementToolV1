// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.logpie.shopping.management.auth.logic.LogpiePageAlertMessage;
import com.logpie.shopping.management.wechat.logic.LogpieWechatAutoReplyEngine;

/**
 * @author zhoyilei
 *
 */
@Controller
public class WechatSupportController extends LogpieBaseController
{
    private static Logger LOG = Logger.getLogger(WechatSupportController.class);
    private static final String WECHAT_INTEGRATION_TOKEN = "LogpieToken";
    private static final String UTF_8 = "UTF-8";

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

    // For wechat auto reply
    @RequestMapping(value = "/wechat_integration", method = RequestMethod.POST)
    public void processWechatMessage(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        try
        {
            request.setCharacterEncoding(UTF_8);
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        httpResponse.setCharacterEncoding(UTF_8);

        LOG.info("LogpieWechatSubscription receive message!");
        // 调用核心业务类接收消息、处理消息
        final String respMessage = LogpieWechatAutoReplyEngine.getInstance()
                .processCommingMessage(request);

        LOG.info("LogpieWechatSubscription response message:" + respMessage);

        // 响应消息
        PrintWriter out = null;
        try
        {
            out = httpResponse.getWriter();
            out.print(respMessage);
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (out != null)
            {
                out.close();
                out = null;
            }
        }
    }

    // 微信订阅号管理
    @RequestMapping(value = "/text_auto_reply/test", method = RequestMethod.POST)
    public Object testTextAutoReply(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        try
        {
            request.setCharacterEncoding(UTF_8);
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        httpResponse.setCharacterEncoding(UTF_8);
        final String content = request.getParameter("TestAutoReplyRequest");

        // 调用核心业务类接收消息、处理消息
        long startTime = System.currentTimeMillis();
        final String respMessage = LogpieWechatAutoReplyEngine.getInstance()
                .processCommingMessage(content);
        long endTime = System.currentTimeMillis();
        long latency = endTime - startTime;
        if (respMessage.equals(LogpieWechatAutoReplyEngine.getInstance().getNoSupportString()))
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                    "回复文本:" + respMessage + " 当前延迟:" + latency);
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    "回复文本:" + respMessage + " 当前延迟:" + latency);
        }

        return "redirect:/wechat_subscription";
    }

    // 微信订阅号管理界面
    @RequestMapping(value = "/wechat_subscription", method = RequestMethod.GET)
    public Object showWechatSubscriptionManagementPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        final Object view = logpieControllerImplementation
                .showWechatSubscriptionManagementPage(request, httpResponse, redirectAttrs);
        super.injectCurrentActiveTab(view, "wechat_subscription");
        return view;
    }

    // 微信订阅号管理
    @RequestMapping(value = "/text_auto_reply/create", method = RequestMethod.POST)
    public Object createTextAutoReply(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.createTextAutoReplyRule(request, httpResponse,
                redirectAttrs);
    }

    // 文本自动回复规则编辑
    @RequestMapping(value = "/text_auto_reply_rule/edit", method = RequestMethod.GET)
    public Object showEditTextAutoReplyPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String textAutoReplyRuleId,
            final RedirectAttributes redirectAttrs)

    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.showEditTextAutoReplyPage(request, httpResponse,
                textAutoReplyRuleId, redirectAttrs);
    }

    // 文本自动回复规则编辑
    @RequestMapping(value = "/text_auto_reply_rule/edit", method = RequestMethod.POST)
    public Object editTextAutoReplyRule(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)

    {
        final LogpieControllerImplementation logpieControllerImplementation = LogpieControllerImplementationFactory
                .getControllerImplementationBasedForAdmin(request);
        return logpieControllerImplementation.modifyTextAutoReplyRule(request, httpResponse,
                redirectAttrs);
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
