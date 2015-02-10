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

import com.logpie.shopping.management.auth.logic.AuthenticationHelper;
import com.logpie.shopping.management.auth.logic.LogpiePageAlertMessage;
import com.logpie.shopping.management.model.Category;
import com.logpie.shopping.management.model.Order;
import com.logpie.shopping.management.storage.CategoryDAO;
import com.logpie.shopping.management.storage.OrderDAO;

/**
 * @author zhoyilei
 *
 */
@Controller
public class OrderController
{
    private static final Logger LOG = Logger.getLogger(OrderController.class);

    @RequestMapping(value = "/order_management", method = RequestMethod.GET)
    public Object showOrderManagementPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to order manage page.");
            final ModelAndView orderManagementPage = new ModelAndView("order_management");
            if (redirectAttrs.containsAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE))
            {
                final String message = (String) redirectAttrs.getFlashAttributes().get(
                        LogpiePageAlertMessage.KEY_ACTION_MESSAGE);
                orderManagementPage.addObject(LogpiePageAlertMessage.KEY_ACTION_MESSAGE, message);
            }
            final OrderDAO orderDAO = new OrderDAO();
            final List<Order> orderList = orderDAO.getAllOrders();
            orderManagementPage.addObject("orderList", orderList);

            final CategoryDAO categoryDAO = new CategoryDAO();
            final List<Category> categoryList = categoryDAO.getAllCategory();
            orderManagementPage.addObject("categoryList", categoryList);

            return orderManagementPage;
        }
        return "redirect:/signin";
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public Object createNewOrder(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to create a new order.");
            final Order newOrder = Order.readNewOrderFromRequest(request);
            boolean createOrderSuccess = false;
            if (newOrder != null)
            {
                final OrderDAO orderDAO = new OrderDAO();
                createOrderSuccess = orderDAO.addOrder(newOrder);
            }

            if (createOrderSuccess)
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE,
                        "create new order for " + newOrder.getOrderBuyerName() + " successfully!");
            }
            else
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE,
                        "create new order for" + newOrder.getOrderBuyerName() + " fail!");
            }

            return "redirect:/order_management";
        }
        return "redirect:/signin";
    }

    /**
     * Show singple order information
     * 
     * @param request
     * @param packageId
     * @return
     */
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public Object showOrderManagementPage(final HttpServletRequest request,
            @RequestParam("id") String orderId)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to order page.");
            final ModelAndView packageDetailPage = new ModelAndView("order_detail");
            final OrderDAO orderDAO = new OrderDAO();
            final Order order = orderDAO.getOrderById(orderId);
            if (order != null)
            {
                packageDetailPage.addObject("order", order);
            }
            return packageDetailPage;
        }
        return "redirect:/signin";
    }

}
