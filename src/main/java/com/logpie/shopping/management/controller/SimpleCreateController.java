// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.logpie.shopping.management.auth.logic.AuthenticationHelper;
import com.logpie.shopping.management.auth.logic.LogpiePageAlertMessage;
import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.Category;
import com.logpie.shopping.management.model.Image;
import com.logpie.shopping.management.model.LogpiePackage;
import com.logpie.shopping.management.storage.AdminDAO;
import com.logpie.shopping.management.storage.CategoryDAO;
import com.logpie.shopping.management.storage.ImageDAO;
import com.logpie.shopping.management.storage.LogpiePackageDAO;

/**
 * @author zhoyilei
 *
 */
@Controller
public class SimpleCreateController
{
    private static final Logger LOG = Logger.getLogger(OrderController.class);

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public Object showAllCategories(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            final ModelAndView createCategoryPage = new ModelAndView("show_all_categories");
            final CategoryDAO categoryDAO = new CategoryDAO();
            final List<Category> categoryList = categoryDAO.getAllCategory();
            // final Map<String, Object> modelMap = new HashMap<String,
            // Object>();
            // modelMap.put("categories", categoryList);
            createCategoryPage.addObject("categoryList", categoryList);

            return createCategoryPage;
        }
        return "redirect:/signin";
    }

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
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
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

            if (createCategorySuccess)
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE,
                        "create new category:" + newCategory.getCategoryName() + " successfully!");
            }
            else
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE,
                        "create new category:" + newCategory.getCategoryName() + " fail!");
            }

            return "redirect:/order_management";
        }
        return "redirect:/signin";
    }

    /**
     * Only super admin can create admin account
     * 
     * @param request
     * @param httpResponse
     * @return
     */
    @RequestMapping(value = "/admin/create", method = RequestMethod.POST)
    public Object createAdmin(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            // TODO: check whether it is super admin
            LOG.debug("Authenticate cookie is valid. Going to create a new admin.");
            final Admin newAdmin = Admin.readNewAdminFromRequest(request);
            boolean createAdminSuccess = false;
            if (newAdmin != null)
            {
                final AdminDAO adminDAO = new AdminDAO();
                createAdminSuccess = adminDAO.addAdmin(newAdmin);
            }

            if (createAdminSuccess)
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE,
                        "create new admin:" + newAdmin.getAdminName() + " successfully!");
            }
            else
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE,
                        "create new admin:" + newAdmin.getAdminName() + " fail!");
            }

            return "redirect:/order_management";
        }
        return "redirect:/signin";
    }

    @RequestMapping(value = "/image/create", method = RequestMethod.POST)
    public Object createImage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to create a new image.");
            final Image newImage = Image.readNewImageFromRequest(request);
            boolean createImageSuccess = false;
            if (newImage != null)
            {
                final ImageDAO imageDAO = new ImageDAO();
                createImageSuccess = imageDAO.addImage(newImage);
            }

            if (createImageSuccess)
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE,
                        "create new image:" + newImage.getImageDescription() + " successfully!");
            }
            else
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE,
                        "create new image:" + newImage.getImageDescription() + " fail!");
            }

            return "redirect:/order_management";
        }
        return "redirect:/signin";
    }

    @RequestMapping(value = "/package/create", method = RequestMethod.POST)
    public Object createPackage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to create a new logpiePackage.");
            final LogpiePackage newLogpiePackage = LogpiePackage
                    .readNewLogpiePackageFromRequest(request);
            boolean createLogpiePackageSuccess = false;
            if (newLogpiePackage != null)
            {
                final LogpiePackageDAO logpiePackageDAO = new LogpiePackageDAO();
                createLogpiePackageSuccess = logpiePackageDAO.addPackage(newLogpiePackage);
            }

            if (createLogpiePackageSuccess)
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE,
                        "create new logpiePackage to " + newLogpiePackage.getPackageDestination()
                                + " successfully!");
            }
            else
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE,
                        "create new logpiePackage to " + newLogpiePackage.getPackageDestination()
                                + " fail!");
            }

            return "redirect:/order_management";
        }
        return "redirect:/signin";
    }

}
