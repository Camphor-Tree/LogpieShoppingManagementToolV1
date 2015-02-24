// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.logpie.shopping.management.accounting.logic.AccountingLogic;
import com.logpie.shopping.management.accounting.logic.GoogleChartHelper;
import com.logpie.shopping.management.accounting.logic.GoogleChartHelper.KeyValue;
import com.logpie.shopping.management.auth.logic.AuthenticationHelper;
import com.logpie.shopping.management.model.Order;
import com.logpie.shopping.management.storage.OrderDAO;

/**
 * @author zhoyilei
 *
 */
@Controller
public class AccountingController
{
    private static final Logger LOG = Logger.getLogger(AccountingController.class);

    @RequestMapping(value = "/accounting", method = RequestMethod.GET)
    public Object showAccountingPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to accounting page.");
            final ModelAndView productManagementPage = new ModelAndView("accounting");
            final OrderDAO orderDAO = new OrderDAO();
            final List<Order> orderList = orderDAO.getAllOrders();
            final Map<String, Integer> orderInCategoryMap = AccountingLogic
                    .getOrdersInCategory(orderList);
            final List<KeyValue> pieDataList = GoogleChartHelper
                    .getPieDataListFromMap(orderInCategoryMap);
            // TODO centralize the constant key
            productManagementPage.addObject("pieDataList", pieDataList);
            return productManagementPage;
        }
        return "redirect:/signin";
    }

}
