// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.logpie.shopping.management.auth.logic.AuthenticationHelper;
import com.logpie.shopping.management.model.Category;
import com.logpie.shopping.management.storage.CategoryDAO;

/**
 * @author zhoyilei
 *
 */
@Controller
public class SimpleCreateController
{
    private static final Logger LOG = Logger.getLogger(OrderController.class);

    @RequestMapping(value = "/category/create", method = RequestMethod.GET)
    public Object showCreateCategoryPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            final ModelAndView createCategoryPage = new ModelAndView("create_category");
            return createCategoryPage;
        }
        return "redirect:/signin";
    }

    @RequestMapping(value = "/category/create", method = RequestMethod.POST)
    public Object createCategory(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to create a new category.");
            final Category newCategory = Category.readNewCategoryFromRequest(request);
            boolean createCategorySuccess = false;
            if (newCategory != null)
            {
                final CategoryDAO categoryDAO = new CategoryDAO();
                createCategorySuccess = categoryDAO.addCategory(newCategory);
            }
            final ModelAndView orderManagementPage = new ModelAndView("order_management");
            orderManagementPage.addObject("action_message",
                    "create new category:" + newCategory.getCategoryName() + " successfully!");
            return orderManagementPage;
        }
        return "redirect:/signin";
    }

    @RequestMapping(value = "/admin/create", method = RequestMethod.POST)
    public Object createAdmin(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to order manage page.");
            final ModelAndView orderManagementPage = new ModelAndView("order_management");
            return orderManagementPage;
        }
        return "redirect:/signin";
    }

    @RequestMapping(value = "/image/create", method = RequestMethod.POST)
    public Object createImage(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to order manage page.");
            final ModelAndView orderManagementPage = new ModelAndView("order_management");
            return orderManagementPage;
        }
        return "redirect:/signin";
    }

    @RequestMapping(value = "/package/create", method = RequestMethod.POST)
    public Object createPackage(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to order manage page.");
            final ModelAndView orderManagementPage = new ModelAndView("order_management");
            return orderManagementPage;
        }
        return "redirect:/signin";
    }

}
