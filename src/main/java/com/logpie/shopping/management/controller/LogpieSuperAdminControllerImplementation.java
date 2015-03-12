// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.logpie.shopping.management.auth.logic.LogpiePageAlertMessage;
import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.LogpiePackage;
import com.logpie.shopping.management.model.Order;
import com.logpie.shopping.management.storage.LogpiePackageDAO;
import com.logpie.shopping.management.storage.OrderDAO;

/**
 * @author zhoyilei
 */
public class LogpieSuperAdminControllerImplementation extends LogpieControllerImplementation
{
    private static final Logger LOG = Logger
            .getLogger(LogpieSuperAdminControllerImplementation.class);

    LogpieSuperAdminControllerImplementation(final Admin admin)
    {
        super(admin);
    }

    @Override
    public Object showPackageManagementPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to package management page.");

        final ModelAndView packageManagementPage = new ModelAndView("package_management");
        if (redirectAttrs.containsAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS))
        {
            final String message = (String) redirectAttrs.getFlashAttributes().get(
                    LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS);
            packageManagementPage.addObject(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    message);
        }
        if (redirectAttrs.containsAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL))
        {
            final String message = (String) redirectAttrs.getFlashAttributes().get(
                    LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL);
            packageManagementPage
                    .addObject(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL, message);
        }
        final LogpiePackageDAO packageDAO = new LogpiePackageDAO();
        final List<LogpiePackage> packageList = packageDAO.getAllPackage();
        packageManagementPage.addObject("packageList", packageList);

        return packageManagementPage;
    }

    /**
     * Quick calculate the distribution of the shipping fee in one package.
     * 
     * @param request
     * @param packageId
     * @return
     */
    @Override
    public Object quickCalculateShippingFeeDistribution(final HttpServletRequest request,
            @RequestParam("id") String packageId)
    {
        LOG.debug("Authenticate cookie is valid. Going to package page.");
        final LogpiePackageDAO packageDAO = new LogpiePackageDAO();
        final LogpiePackage logpiePackage = packageDAO.getPackageById(packageId);
        if (logpiePackage != null)
        {
            final OrderDAO orderDAO = new OrderDAO();
            final List<Order> orderList = orderDAO.getOrdersForPackage(packageId);
            final Float totalWeight = calculateTotalWeight(orderList);
            for (final Order order : orderList)
            {
                final Float shippingFeeDistribution = order.getOrderWeight() / totalWeight
                        * logpiePackage.getPackgeShippingFee();
                order.setOrderActualShippingFee(Float.valueOf(String.format("%.2f",
                        shippingFeeDistribution)));
                orderDAO.updateOrderProfile(order);
            }
        }

        return "redirect:/package?id=" + packageId;
    }

    @Override
    public Object createPackage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
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
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    "创建寄给:" + newLogpiePackage.getPackageDestination() + "的包裹 成功!");
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL, "创建寄给:"
                    + newLogpiePackage.getPackageDestination() + "的包裹 失败!");
        }

        return "redirect:/order_management";
    }

    @Override
    public Object showModifyPackagePage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String packageId,
            final RedirectAttributes redirectAttrs)
    {
        final ModelAndView modifyOrderPage = new ModelAndView("package_edit");

        final LogpiePackageDAO packageDAO = new LogpiePackageDAO();
        final LogpiePackage logpiePackage = packageDAO.getPackageById(packageId);
        modifyOrderPage.addObject("logpiePackage", logpiePackage);

        return modifyOrderPage;
    }

    @Override
    public Object modifyPackage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to modify the package information.");
        final LogpiePackage modifiedPackage = LogpiePackage
                .readModifiedLogpiePackageFromRequest(request);
        boolean updatePackageSuccess = false;
        if (modifiedPackage != null)
        {
            final LogpiePackageDAO packageDAO = new LogpiePackageDAO();
            updatePackageSuccess = packageDAO.updateLogpiePackageProfile(modifiedPackage);
        }

        if (updatePackageSuccess)
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    "更新包裹:" + modifiedPackage.getPackageId() + " 信息，成功!");
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL, "更新包裹:"
                    + modifiedPackage.getPackageId() + " 信息，失败!");
        }
        return "redirect:/package_management";
    }

    @Override
    public List<Order> injectOrderManagementOrderList()
    {
        final OrderDAO orderDAO = new OrderDAO();
        return orderDAO.getAllOrders();
    }

}
