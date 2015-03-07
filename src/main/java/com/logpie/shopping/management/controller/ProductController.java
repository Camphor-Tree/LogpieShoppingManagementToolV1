// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.logpie.shopping.management.auth.logic.LogpiePageAlertMessage;
import com.logpie.shopping.management.model.Brand;
import com.logpie.shopping.management.model.Image;
import com.logpie.shopping.management.model.Product;
import com.logpie.shopping.management.storage.BrandDAO;
import com.logpie.shopping.management.storage.ImageDAO;
import com.logpie.shopping.management.storage.ProductDAO;

/**
 * @author zhoyilei
 *
 */
@Controller
public class ProductController
{
    private static final Logger LOG = Logger.getLogger(ProductController.class);

    @RequestMapping(value = "/product_management", method = RequestMethod.GET)
    public Object showProductManagementPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to product manage page.");
        final ModelAndView productManagementPage = new ModelAndView("product_management");
        final ProductDAO ProductDAO = new ProductDAO();
        final List<Product> productList = ProductDAO.getAllProduct();
        productManagementPage.addObject("productList", productList);
        return productManagementPage;
    }

    @RequestMapping(value = "/product/edit", method = RequestMethod.GET)
    public Object showModifyproductPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String productId,
            final RedirectAttributes redirectAttrs)
    {
        final ModelAndView modifyproductPage = new ModelAndView("product_edit");

        final ProductDAO ProductDAO = new ProductDAO();
        final Product product = ProductDAO.getProductById(productId);
        modifyproductPage.addObject("product", product);

        final BrandDAO brandDAO = new BrandDAO();
        final List<Brand> brandList = brandDAO.getAllBrand();
        modifyproductPage.addObject("brandList", brandList);

        final ImageDAO imageDAO = new ImageDAO();
        final List<Image> imageList = imageDAO.getAllImage();
        modifyproductPage.addObject("imageList", imageList);

        return modifyproductPage;
    }

    @RequestMapping(value = "/product/edit", method = RequestMethod.POST)
    public Object modifyProduct(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final Product modifiedProduct = Product.readModifiedProductFromRequest(request);
        boolean updatCateogrySuccess = false;
        if (modifiedProduct != null)
        {
            final ProductDAO ProductDAO = new ProductDAO();
            updatCateogrySuccess = ProductDAO.updateProductProfile(modifiedProduct);
        }

        if (updatCateogrySuccess)
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    "更新商品:" + modifiedProduct.getProductName() + " 信息，成功!");
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL, "更新商品:"
                    + modifiedProduct.getProductName() + " 信息，失败!");
        }
        return "redirect:/product_management";
    }
}
