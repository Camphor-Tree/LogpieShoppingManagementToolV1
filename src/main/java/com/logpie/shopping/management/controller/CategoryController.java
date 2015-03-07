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
import com.logpie.shopping.management.model.Category;
import com.logpie.shopping.management.storage.CategoryDAO;

/**
 * @author zhoyilei
 *
 */
@Controller
public class CategoryController
{
    @RequestMapping(value = "/category_management", method = RequestMethod.GET)
    public Object showAllCategories(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView createCategoryPage = new ModelAndView("category_management");
        final CategoryDAO categoryDAO = new CategoryDAO();
        final List<Category> categoryList = categoryDAO.getAllCategory();
        createCategoryPage.addObject("categoryList", categoryList);

        return createCategoryPage;
    }

    @RequestMapping(value = "/category/edit", method = RequestMethod.GET)
    public Object showModifyCategoryPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String categoryId,
            final RedirectAttributes redirectAttrs)
    {
        final ModelAndView modifyCategoryPage = new ModelAndView("category_edit");

        final CategoryDAO categoryDAO = new CategoryDAO();
        final Category category = categoryDAO.getCategoryById(categoryId);
        modifyCategoryPage.addObject("category", category);

        return modifyCategoryPage;
    }

    @RequestMapping(value = "/category/edit", method = RequestMethod.POST)
    public Object modifyCategory(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final Category modifiedCateogry = Category.readModifiedCategoryFromRequest(request);
        boolean updatCateogrySuccess = false;
        if (modifiedCateogry != null)
        {
            final CategoryDAO categoryDAO = new CategoryDAO();
            updatCateogrySuccess = categoryDAO.updateCategoryProfile(modifiedCateogry);
        }

        if (updatCateogrySuccess)
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    "更新类别:" + modifiedCateogry.mCategoryName + " 信息，成功!");
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL, "更新类别:"
                    + modifiedCateogry.mCategoryName + " 信息，失败!");
        }
        return "redirect:/category_management";
    }

}
