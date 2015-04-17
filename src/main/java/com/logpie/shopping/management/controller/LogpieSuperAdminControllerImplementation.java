// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.logpie.shopping.management.auth.logic.LogpiePageAlertMessage;
import com.logpie.shopping.management.business.logic.LogpieSettleDownOrderLogic;
import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.DBLog;
import com.logpie.shopping.management.model.LogpiePackage;
import com.logpie.shopping.management.model.Order;
import com.logpie.shopping.management.storage.AdminDAO;
import com.logpie.shopping.management.storage.DBLogDAO;
import com.logpie.shopping.management.storage.LogpiePackageDAO;
import com.logpie.shopping.management.storage.OrderDAO;
import com.logpie.shopping.management.util.CollectionUtils;

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
        final LogpiePackageDAO packageDAO = new LogpiePackageDAO(mCurrentAdmin);
        final LogpiePackage logpiePackage = packageDAO.getPackageById(packageId);
        if (logpiePackage != null)
        {
            final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
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
            final LogpiePackageDAO logpiePackageDAO = new LogpiePackageDAO(mCurrentAdmin);
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

        return "redirect:/order_management?orderBy=orderId";
    }

    @Override
    public Object showModifyPackagePage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final String packageId,
            final RedirectAttributes redirectAttrs)
    {
        final ModelAndView modifyOrderPage = new ModelAndView("package_edit");

        final LogpiePackageDAO packageDAO = new LogpiePackageDAO(mCurrentAdmin);
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
            final LogpiePackageDAO packageDAO = new LogpiePackageDAO(mCurrentAdmin);
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
    public Object markPackageDelivered(HttpServletRequest request,
            HttpServletResponse httpResponse, String packageId, RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to modify the package as delivered.");

        boolean updatePackageSuccess = false;
        if (packageId != null)
        {
            final LogpiePackageDAO packageDAO = new LogpiePackageDAO(mCurrentAdmin);
            final LogpiePackage logpiePackage = packageDAO.getPackageById(packageId);
            if (logpiePackage == null)
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                        "该包裹Id无效");
                return "redirect:/package_management";
            }
            logpiePackage.setPackageIsDelivered(true);
            updatePackageSuccess = packageDAO.updateLogpiePackageProfile(logpiePackage);
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                    "该包裹Id无效");
            return "redirect:/package_management";
        }

        if (updatePackageSuccess)
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    "标记包裹:" + packageId + " 已签收送达，成功!");
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL, "标记包裹:"
                    + packageId + " 已签收送达，失败!");
        }
        return "redirect:/package?id=" + packageId;
    }

    @Override
    public List<Order> injectOrderManagementOrderList(final String orderByAttributes)
    {
        final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
        return orderDAO.getAllOrders(orderByAttributes);
    }

    @Override
    public Object showOrderSettleDownPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final String adminId,
            final RedirectAttributes redirectAttrs)
    {
        final ModelAndView orderSettleDownPage = new ModelAndView("order_settledown");
        if (redirectAttrs.containsAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS))
        {
            final String message = (String) redirectAttrs.getFlashAttributes().get(
                    LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS);
            orderSettleDownPage.addObject(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    message);
        }
        if (redirectAttrs.containsAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL))
        {
            final String message = (String) redirectAttrs.getFlashAttributes().get(
                    LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL);
            orderSettleDownPage.addObject(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL, message);
        }

        final AdminDAO adminDAO = new AdminDAO(mCurrentAdmin);
        final Admin currentAdminToSettleDown = adminDAO.queryAccountByAdminId(adminId);

        if (currentAdminToSettleDown != null)
        {
            // inject the current admin to be settle down
            orderSettleDownPage.addObject("admin", currentAdminToSettleDown);
            final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
            List<Order> orderList = orderDAO.getOrdersForProxy(adminId, null);
            // filter out all the orders already settled down.
            orderList = super.filterOutOrdersAlreadySettledDown(orderList);
            orderSettleDownPage.addObject("orderList", orderList);
        }
        injectCurrentUrl(request, orderSettleDownPage);
        return orderSettleDownPage;
    }

    public List<Order> filterOutOrdersAlreadySettleDown(final List<Order> orderList)
    {
        final LogpieSettleDownOrderLogic logpieSettlwDownOrderLogic = new LogpieSettleDownOrderLogic();
        final List<Order> orderListAfterFilter = new ArrayList<Order>();
        for (final Order order : orderList)
        {
            if (!logpieSettlwDownOrderLogic.isOrderAlreadyCleared(order))
            {
                orderListAfterFilter.add(order);
            }
        }
        return orderListAfterFilter;
    }

    @Override
    public Object handleOrderSettleDown(HttpServletRequest request,
            HttpServletResponse httpResponse, final String adminId, List<String> settleDownOrders,
            RedirectAttributes redirectAttrs)
    {
        if (CollectionUtils.isEmpty(settleDownOrders))
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                    "未检测到结算包裹，请选中你想要结算的包裹 再点快捷清算");
            return "redirect:/order/settledown?adminId=" + adminId;
        }
        final StringBuilder messageBuilder = new StringBuilder();
        final Set<String> successSet = new HashSet<String>();
        final Set<String> errorSet = new HashSet<String>();

        final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
        for (final String orderId : settleDownOrders)
        {
            final Order order = orderDAO.getOrderById(orderId);
            order.settleDown();
            final boolean updateSuccess = orderDAO.updateOrderProfile(order);
            if (updateSuccess)
            {
                successSet.add(orderId);
            }
            else
            {
                errorSet.add(orderId);
            }
        }
        int i = 0;
        for (final String orderId : successSet)
        {
            i++;
            messageBuilder.append(orderId);
            if (i < successSet.size())
            {
                messageBuilder.append("号,");
            }
            else
            {
                messageBuilder.append("号");
            }
        }
        if (errorSet.size() == 0)
        {
            if (successSet.size() == 1)
            {
                messageBuilder.append("包裹 结算成功！");
            }
            else
            {
                messageBuilder.append("包裹 全部结算成功！");
            }
        }
        else
        {
            messageBuilder.append("包裹 结算成功！");
            i = 0;
            for (final String orderId : errorSet)
            {
                i++;
                messageBuilder.append(orderId);
                if (i < successSet.size())
                {
                    messageBuilder.append("号,");
                }
                else
                {
                    messageBuilder.append("号");
                }
            }
            messageBuilder.append("包裹 结算失败！");
        }
        redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                messageBuilder.toString());
        return "redirect:/order/settledown?adminId=" + adminId;
    }

    @Override
    public Object showLogPage(HttpServletRequest request, HttpServletResponse httpResponse)
    {
        final ModelAndView logPage = new ModelAndView("log");

        final DBLogDAO dbLogDAO = new DBLogDAO(mCurrentAdmin);
        final List<DBLog> dbLogList = dbLogDAO.getAllDBLog();
        logPage.addObject("dbLogList", dbLogList);

        return logPage;
    }
}
