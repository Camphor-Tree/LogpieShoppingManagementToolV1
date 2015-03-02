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
import com.logpie.shopping.management.model.Brand;
import com.logpie.shopping.management.model.Category;
import com.logpie.shopping.management.model.Image;
import com.logpie.shopping.management.model.Product;
import com.logpie.shopping.management.storage.AdminDAO;
import com.logpie.shopping.management.storage.BrandDAO;
import com.logpie.shopping.management.storage.CategoryDAO;
import com.logpie.shopping.management.storage.ImageDAO;
import com.logpie.shopping.management.storage.ProductDAO;

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
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                        "创建新的分类:" + newCategory.getCategoryName() + " 成功!");
            }
            else
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                        "创建新的分类:" + newCategory.getCategoryName() + " 失败!");
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
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                        "创建新的管理员:" + newAdmin.getAdminName() + " 成功!");
            }
            else
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                        "创建新的管理员:" + newAdmin.getAdminName() + " 失败!");
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
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                        "创建新的图片:" + newImage.getImageDescription() + " 成功!");
            }
            else
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                        "创建新的图片:" + newImage.getImageDescription() + " 失败!");
            }

            return "redirect:/order_management";
        }
        return "redirect:/signin";
    }

    @RequestMapping(value = "/brand/create", method = RequestMethod.POST)
    public Object createBrand(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to create a new logpiePackage.");
            final Brand newBrand = Brand.readNewBrandFromRequest(request);
            boolean createBrandSuccess = false;
            if (newBrand != null)
            {
                final BrandDAO brandDAO = new BrandDAO();
                createBrandSuccess = brandDAO.addBrand(newBrand);
            }

            if (createBrandSuccess)
            {
                redirectAttrs.addFlashAttribute(
                        LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                        "创建新的品牌:" + newBrand.getBrandEnglishName() + "/"
                                + newBrand.getBrandChineseName() + " 成功!");
            }
            else
            {
                redirectAttrs.addFlashAttribute(
                        LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                        "创建新的品牌:" + newBrand.getBrandEnglishName() + "/"
                                + newBrand.getBrandChineseName() + " 失败!");
            }

            return "redirect:/order_management";
        }
        return "redirect:/signin";
    }

    @RequestMapping(value = "/product/create", method = RequestMethod.POST)
    public Object createProduct(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to create a new logpiePackage.");
            final Product newProduct = Product.readNewProductFromRequest(request);
            boolean createNewProductSuccess = false;
            if (newProduct != null)
            {
                final ProductDAO productDAO = new ProductDAO();
                createNewProductSuccess = productDAO.addProduct(newProduct);
            }

            if (createNewProductSuccess)
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                        "创建新的产品:" + newProduct.getProductName() + " 成功!");
            }
            else
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                        "创建新的产品:" + newProduct.getProductName() + " 失败!");
            }

            return "redirect:/order_management";
        }
        return "redirect:/signin";
    }
}
