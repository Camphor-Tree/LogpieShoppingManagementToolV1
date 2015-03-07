// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.logpie.shopping.management.auth.logic.LogpiePageAlertMessage;
import com.logpie.shopping.management.model.Brand;
import com.logpie.shopping.management.model.Category;
import com.logpie.shopping.management.model.Image;
import com.logpie.shopping.management.storage.BrandDAO;
import com.logpie.shopping.management.storage.CategoryDAO;
import com.logpie.shopping.management.storage.ImageDAO;

/**
 * @author zhoyilei
 *
 */
@Controller
public class BrandController
{
    @RequestMapping(value = "/brand_management", method = RequestMethod.GET)
    public Object showAllCategories(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView createbrandPage = new ModelAndView("brand_management");
        final BrandDAO BrandDAO = new BrandDAO();
        final List<Brand> brandList = BrandDAO.getAllBrand();
        createbrandPage.addObject("brandList", brandList);

        return createbrandPage;
    }

    @RequestMapping(value = "/brand/edit", method = RequestMethod.GET)
    public Object showModifybrandPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String brandId,
            final RedirectAttributes redirectAttrs)
    {
        final ModelAndView modifybrandPage = new ModelAndView("brand_edit");

        final BrandDAO BrandDAO = new BrandDAO();
        final Brand brand = BrandDAO.getBrandById(brandId);
        modifybrandPage.addObject("brand", brand);

        final CategoryDAO categoryDAO = new CategoryDAO();
        final List<Category> categoryList = categoryDAO.getAllCategory();
        modifybrandPage.addObject("categoryList", categoryList);

        final ImageDAO imageDAO = new ImageDAO();
        final List<Image> imageList = imageDAO.getAllImage();
        modifybrandPage.addObject("imageList", imageList);

        return modifybrandPage;
    }

    @RequestMapping(value = "/brand/edit", method = RequestMethod.POST)
    public Object modifyBrand(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final Brand modifiedBrand = Brand.readModifiedBrandFromRequest(request);
        boolean updatCateogrySuccess = false;
        if (modifiedBrand != null)
        {
            final BrandDAO BrandDAO = new BrandDAO();
            updatCateogrySuccess = BrandDAO.updateBrandProfile(modifiedBrand);
        }

        if (updatCateogrySuccess)
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    "更新品牌:" + modifiedBrand.getBrandChineseName() + " 信息，成功!");
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL, "更新品牌:"
                    + modifiedBrand.getBrandChineseName() + " 信息，失败!");
        }
        return "redirect:/brand_management";
    }

}
