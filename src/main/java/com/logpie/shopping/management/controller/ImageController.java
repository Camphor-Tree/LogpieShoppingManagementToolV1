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
import com.logpie.shopping.management.model.Image;
import com.logpie.shopping.management.storage.ImageDAO;

/**
 * @author zhoyilei
 *
 */
@Controller
public class ImageController
{
    @RequestMapping(value = "/image_management", method = RequestMethod.GET)
    public Object showAllCategories(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView createimagePage = new ModelAndView("image_management");
        final ImageDAO ImageDAO = new ImageDAO();
        final List<Image> imageList = ImageDAO.getAllImage();
        createimagePage.addObject("imageList", imageList);

        return createimagePage;
    }

    @RequestMapping(value = "/image/edit", method = RequestMethod.GET)
    public Object showModifyimagePage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String imageId,
            final RedirectAttributes redirectAttrs)
    {
        final ModelAndView modifyimagePage = new ModelAndView("image_edit");

        final ImageDAO ImageDAO = new ImageDAO();
        final Image image = ImageDAO.getImageById(imageId);
        modifyimagePage.addObject("image", image);

        return modifyimagePage;
    }

    @RequestMapping(value = "/image/edit", method = RequestMethod.POST)
    public Object modifyimage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final Image modifiedImage = Image.readModifiedImageFromRequest(request);
        boolean updatCateogrySuccess = false;
        if (modifiedImage != null)
        {
            final ImageDAO ImageDAO = new ImageDAO();
            updatCateogrySuccess = ImageDAO.updateImageProfile(modifiedImage);
        }

        if (updatCateogrySuccess)
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    "更新图片:" + modifiedImage.getImageDescription() + " 信息，成功!");
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL, "更新图片:"
                    + modifiedImage.getImageDescription() + " 信息，失败!");
        }
        return "redirect:/image_management";
    }

}
