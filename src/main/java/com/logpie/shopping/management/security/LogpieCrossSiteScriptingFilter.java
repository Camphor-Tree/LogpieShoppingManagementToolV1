// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.security;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.log4j.Logger;
import org.springframework.web.util.HtmlUtils;

/**
 * @author zhoyilei
 *
 */
public class LogpieCrossSiteScriptingFilter implements Filter
{
    private static Logger logger = Logger.getLogger(LogpieCrossSiteScriptingFilter.class);
    private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
    }

    public void destroy()
    {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        logger.info("Inlter CrossScriptingFilter  ...............");
        chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
        logger.info("Outlter CrossScriptingFilter ...............");
    }

    public final class RequestWrapper extends HttpServletRequestWrapper
    {
        private final Logger logger = Logger.getLogger(RequestWrapper.class);

        public RequestWrapper(HttpServletRequest servletRequest)
        {
            super(servletRequest);
        }

        public String[] getParameterValues(String parameter)
        {
            logger.info("InarameterValues .. parameter .......");
            String[] values = super.getParameterValues(parameter);
            if (values == null)
            {
                return null;
            }
            int count = values.length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++)
            {
                encodedValues[i] = cleanXSS(values[i]);
            }
            return encodedValues;
        }

        public String getParameter(String parameter)
        {
            logger.info("Inarameter .. parameter .......");
            String value = super.getParameter(parameter);
            if (value == null)
            {
                return null;
            }
            logger.info("Inarameter RequestWrapper ........ value .......");
            return cleanXSS(value);
        }

        public String getHeader(String name)
        {
            logger.info("Ineader .. parameter .......");
            String value = super.getHeader(name);
            if (value == null)
                return null;
            logger.info("Ineader RequestWrapper ........... value ....");
            return cleanXSS(value);
        }

        private String escapeHtml(String value)
        {
            final String previousValue = new String(value);
            final String afterEscape = HtmlUtils.htmlEscape(value);
            if (!previousValue.equals(afterEscape))
            {
                logger.warn("html escape from:" + previousValue + " to: " + afterEscape);
            }
            return afterEscape;
        }

        private String cleanXSS(String value)
        {
            final String previousValue = new String(value);
            if (value != null)
            {
                // NOTE: It's highly recommended to use the ESAPI library and
                // uncomment the following line to
                // avoid encoded attacks.
                // value = ESAPI.encoder().canonicalize(value);
                // Avoid null characters
                value = value.replaceAll("", "");
                // Avoid anything between script tags
                Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>",
                        Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");
                // Avoid anything in a src='...' type of expression
                scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
                        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
                        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                // Remove any lonesome </script> tag
                scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");
                // Remove any lonesome <script ...> tag
                scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE
                        | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                // Avoid eval(...) expressions
                scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE
                        | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                // Avoid expression(...) expressions
                scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE
                        | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                // Avoid javascript:... expressions
                scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");
                // Avoid vbscript:... expressions
                scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");
                // Avoid onload= expressions
                scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE
                        | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
            }
            if (!previousValue.equals(value))
            {
                // TODO add a metric here.
                logger.warn("Logpie is under XSS attack:" + previousValue);
            }
            return escapeHtml(value);
        }
    }

}
