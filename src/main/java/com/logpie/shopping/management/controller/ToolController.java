// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author zhoyilei
 *
 */
@Controller
public class ToolController
{
    private static final Logger LOG = Logger.getLogger(ToolController.class);

    // Show the price calculator
    @RequestMapping(value = "/calculator", method = RequestMethod.GET)
    public Object showCreateCategoryPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView createCategoryPage = new ModelAndView("calculator");
        return createCategoryPage;
    }

}
