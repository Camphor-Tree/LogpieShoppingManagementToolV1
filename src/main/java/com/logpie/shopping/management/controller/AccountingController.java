// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.logpie.shopping.management.accounting.logic.AccountingLogic;
import com.logpie.shopping.management.accounting.logic.GoogleChartHelper;
import com.logpie.shopping.management.accounting.logic.GoogleChartHelper.KeyValue;
import com.logpie.shopping.management.accounting.logic.LogpieLineChart;
import com.logpie.shopping.management.accounting.logic.LogpiePieChart;
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

    private static final String PIE_CHART_DATA_LIST_1 = "PieChartDataList1";
    private static final String PIE_CHART_1 = "PieChart1";
    private static final String PIE_CHART_DATA_LIST_2 = "PieChartDataList2";
    private static final String PIE_CHART_2 = "PieChart2";

    private static final String LINE_CHART_DATA_LIST_1 = "LineChartDataList1";
    private static final String LINE_CHART_1 = "LineChart1";
    private static final String LINE_CHART_DATA_LIST_2 = "LineChartDataList2";
    private static final String LINE_CHART_2 = "LineChart2";

    @RequestMapping(value = "/accounting", method = RequestMethod.GET)
    public Object showAccountingPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to accounting page.");
            final ModelAndView accountingHomePage = new ModelAndView("accounting");
            final Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            accountingHomePage.addObject("Today", dateFormat.format(calendar.getTime()));
            return accountingHomePage;
        }
        return "redirect:/signin";
    }

    /**
     * PieChart showing each category's order number It will show the all the
     * orders. If you specify a year and month, it will also show a specific
     * month's result
     */
    @RequestMapping(value = "/accounting/piechart", method = RequestMethod.GET)
    public Object showPieChartAccounting(final HttpServletRequest request,
            final HttpServletResponse httpResponse,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "year_month", required = false) String yearMonth)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to piechart page.");
            if (type.equals("OrderInCategory"))
            {
                return handleOrderInCategory(yearMonth);
            }
            else if (type.equals("OrderInBrand"))
            {
                return handleOrderInBrand(yearMonth);
            }
            else if (type.equals("OrderInAdmin"))
            {
                return handleOrderInAdmin(yearMonth);
            }
            else if (type.equals("OrderProfitInBrand"))
            {
                return handleOrderProfitInBrand(yearMonth);
            }
            else if (type.equals("OrderProfitInAdmin"))
            {
                return handleOrderProfitInAdmin(yearMonth);
            }
            else
            {
                return "redirect:/accounting";
            }
        }
        return "redirect:/signin";
    }

    /**
     * LineChart showing the numbers of orders, profits Logpie makes in daily or
     * monthly mode.
     */
    @RequestMapping(value = "/accounting/linechart", method = RequestMethod.GET)
    public Object showLineChartAccounting(final HttpServletRequest request,
            final HttpServletResponse httpResponse,
            @RequestParam(value = "type", required = true) String type)
    {
        final boolean authSuccess = AuthenticationHelper.handleAuthentication(request);
        if (authSuccess)
        {
            LOG.debug("Authenticate cookie is valid. Going to piechart page.");
            if (type.equals("OrderNumbers"))
            {
                return handleOrderNumbers();
            }
            else if (type.equals("OrderProfits"))
            {
                return handleOrderProfits();
            }
            else
            {
                return "redirect:/accounting";
            }
        }
        return "redirect:/signin";
    }

    private Object handleOrderNumbers()
    {
        final ModelAndView orderNumberLineChartPage = new ModelAndView("accounting_linechart");
        final OrderDAO orderDAO = new OrderDAO();
        final LogpieLineChart orderInCategoryPieChart1 = new LogpieLineChart("Logpie订单量 最近12天 走势图",
                "日期", "订单数量");
        final List<Order> orderListWithinNdays = orderDAO.getOrdersWithinNdays(12);
        final Map<String, Integer> orderWithinNdaysMap = AccountingLogic.getOrderNumbers(true, 12,
                orderListWithinNdays);
        final List<KeyValue> lineDataList1 = GoogleChartHelper
                .getLineChartDataListFromStringIntegerMap(orderWithinNdaysMap);
        orderNumberLineChartPage.addObject(LINE_CHART_DATA_LIST_1, lineDataList1);
        orderNumberLineChartPage.addObject(LINE_CHART_1, orderInCategoryPieChart1);

        final LogpieLineChart orderInCategoryPieChart2 = new LogpieLineChart(
                "Logpie订单量 最近12个月 走势图", "日期", "订单数量");
        final List<Order> orderListWithinNmonths = orderDAO.getOrdersWithinNmonths(12);
        final Map<String, Integer> orderWithinNmonthsMap = AccountingLogic.getOrderNumbers(false,
                12, orderListWithinNmonths);
        final List<KeyValue> lineDataList2 = GoogleChartHelper
                .getLineChartDataListFromStringIntegerMap(orderWithinNmonthsMap);
        orderNumberLineChartPage.addObject(LINE_CHART_DATA_LIST_2, lineDataList2);
        orderNumberLineChartPage.addObject(LINE_CHART_2, orderInCategoryPieChart2);

        return orderNumberLineChartPage;
    }

    private Object handleOrderProfits()
    {
        final ModelAndView orderNumberLineChartPage = new ModelAndView("accounting_linechart");
        final OrderDAO orderDAO = new OrderDAO();
        final LogpieLineChart orderInCategoryPieChart1 = new LogpieLineChart("Logpie 利润 最近12天 走势图",
                "日期", "订单数量");
        final List<Order> orderListWithinNdays = orderDAO.getOrdersWithinNdays(12);
        final Map<String, Double> orderWithinNdaysMap = AccountingLogic.getOrderProfits(true, 12,
                orderListWithinNdays);
        final List<KeyValue> lineDataList1 = GoogleChartHelper
                .getLineChartDataListFromStringDoubleMap(orderWithinNdaysMap);
        orderNumberLineChartPage.addObject(LINE_CHART_DATA_LIST_1, lineDataList1);
        orderNumberLineChartPage.addObject(LINE_CHART_1, orderInCategoryPieChart1);

        final LogpieLineChart orderInCategoryPieChart2 = new LogpieLineChart("Logpie利润 最近12个月 走势图",
                "日期", "订单数量");
        final List<Order> orderListWithinNmonths = orderDAO.getOrdersWithinNmonths(12);
        final Map<String, Double> orderWithinNmonthsMap = AccountingLogic.getOrderProfits(false,
                12, orderListWithinNmonths);
        final List<KeyValue> lineDataList2 = GoogleChartHelper
                .getLineChartDataListFromStringDoubleMap(orderWithinNmonthsMap);
        orderNumberLineChartPage.addObject(LINE_CHART_DATA_LIST_2, lineDataList2);
        orderNumberLineChartPage.addObject(LINE_CHART_2, orderInCategoryPieChart2);

        return orderNumberLineChartPage;
    }

    private Object handleOrderInCategory(final String yearMonth)
    {
        final ModelAndView accountingOrderInCategoryPieChartPage = new ModelAndView(
                "accounting_piechart");
        final LogpiePieChart orderInCategoryPieChart1 = new LogpiePieChart("各类别 所有订单数量分布图", "订单类别",
                "订单数量");

        final OrderDAO orderDAO = new OrderDAO();
        final List<Order> allOrderList = orderDAO.getAllOrders();
        final Map<String, Integer> orderInCategoryMap = AccountingLogic
                .getOrdersInCategory(allOrderList);
        final List<KeyValue> pieDataList = GoogleChartHelper
                .getPieDataListFromMap(orderInCategoryMap);
        accountingOrderInCategoryPieChartPage.addObject(PIE_CHART_DATA_LIST_1, pieDataList);
        accountingOrderInCategoryPieChartPage.addObject(PIE_CHART_1, orderInCategoryPieChart1);

        if (yearMonth != null)
        {
            final String year = yearMonth.substring(0, 4);
            final String month = yearMonth.substring(5, 7);
            final List<Order> orderInMonthList = orderDAO.getOrdersByMonth(year, month);
            if (orderInMonthList != null && !orderInMonthList.isEmpty())
            {
                final String chartLabel = String.format("各类别 %s年%s月 订单数量分布图", year, month);
                final LogpiePieChart orderInCategoryPieChart2 = new LogpiePieChart(chartLabel,
                        "订单类别", "订单数量");
                final Map<String, Integer> orderInMonthCategoryMap = AccountingLogic
                        .getOrdersInCategory(orderInMonthList);
                final List<KeyValue> pieDataList2 = GoogleChartHelper
                        .getPieDataListFromMap(orderInMonthCategoryMap);
                accountingOrderInCategoryPieChartPage
                        .addObject(PIE_CHART_DATA_LIST_2, pieDataList2);
                accountingOrderInCategoryPieChartPage.addObject(PIE_CHART_2,
                        orderInCategoryPieChart2);
            }
        }

        return accountingOrderInCategoryPieChartPage;
    }

    private Object handleOrderInBrand(final String yearMonth)
    {
        final ModelAndView accountingOrderInBrandPieChartPage = new ModelAndView(
                "accounting_piechart");
        final LogpiePieChart orderInCategoryPieChart1 = new LogpiePieChart("各品牌 所有订单数量分布图", "订单品牌",
                "订单数量");

        final OrderDAO orderDAO = new OrderDAO();
        final List<Order> allOrderList = orderDAO.getAllOrders();
        final Map<String, Integer> orderInBrandMap = AccountingLogic.getOrdersInBrand(allOrderList);
        final List<KeyValue> pieDataList = GoogleChartHelper.getPieDataListFromMap(orderInBrandMap);
        accountingOrderInBrandPieChartPage.addObject(PIE_CHART_DATA_LIST_1, pieDataList);
        accountingOrderInBrandPieChartPage.addObject(PIE_CHART_1, orderInCategoryPieChart1);

        if (yearMonth != null)
        {
            final String year = yearMonth.substring(0, 4);
            final String month = yearMonth.substring(5, 7);
            final List<Order> orderInMonthList = orderDAO.getOrdersByMonth(year, month);
            if (orderInMonthList != null && !orderInMonthList.isEmpty())
            {
                final String chartLabel = String.format("各品牌 %s年%s月 订单数量分布图", year, month);
                final LogpiePieChart orderInCategoryPieChart2 = new LogpiePieChart(chartLabel,
                        "订单品牌", "订单数量");
                final Map<String, Integer> orderInMonthBrandMap = AccountingLogic
                        .getOrdersInBrand(orderInMonthList);
                final List<KeyValue> pieDataList2 = GoogleChartHelper
                        .getPieDataListFromMap(orderInMonthBrandMap);
                accountingOrderInBrandPieChartPage.addObject(PIE_CHART_DATA_LIST_2, pieDataList2);
                accountingOrderInBrandPieChartPage.addObject(PIE_CHART_2, orderInCategoryPieChart2);
            }
        }
        return accountingOrderInBrandPieChartPage;
    }

    private Object handleOrderInAdmin(final String yearMonth)
    {
        final ModelAndView accountingOrderInAdminPieChartPage = new ModelAndView(
                "accounting_piechart");
        final LogpiePieChart orderInCategoryPieChart1 = new LogpiePieChart("各代理 所有订单数量分布图", "订单类别",
                "订单数量");

        final OrderDAO orderDAO = new OrderDAO();
        final List<Order> allOrderList = orderDAO.getAllOrders();
        final Map<String, Integer> orderInAdminMap = AccountingLogic.getOrdersInAdmin(allOrderList);
        final List<KeyValue> pieDataList = GoogleChartHelper.getPieDataListFromMap(orderInAdminMap);
        accountingOrderInAdminPieChartPage.addObject(PIE_CHART_DATA_LIST_1, pieDataList);
        accountingOrderInAdminPieChartPage.addObject(PIE_CHART_1, orderInCategoryPieChart1);

        if (yearMonth != null)
        {
            final String year = yearMonth.substring(0, 4);
            final String month = yearMonth.substring(5, 7);
            final List<Order> orderInMonthList = orderDAO.getOrdersByMonth(year, month);
            if (orderInMonthList != null && !orderInMonthList.isEmpty())
            {
                final String chartLabel = String.format("各代理 %s年%s月 订单数量分布图", year, month);
                final LogpiePieChart orderInCategoryPieChart2 = new LogpiePieChart(chartLabel,
                        "订单代理", "订单数量");
                final Map<String, Integer> orderInMonthAdminMap2 = AccountingLogic
                        .getOrdersInAdmin(orderInMonthList);
                final List<KeyValue> pieDataList2 = GoogleChartHelper
                        .getPieDataListFromMap(orderInMonthAdminMap2);
                accountingOrderInAdminPieChartPage.addObject(PIE_CHART_DATA_LIST_2, pieDataList2);
                accountingOrderInAdminPieChartPage.addObject(PIE_CHART_2, orderInCategoryPieChart2);
            }
        }

        return accountingOrderInAdminPieChartPage;
    }

    private Object handleOrderProfitInAdmin(final String yearMonth)
    {
        final ModelAndView accountingOrderInAdminPieChartPage = new ModelAndView(
                "accounting_piechart");
        final LogpiePieChart orderInCategoryPieChart1 = new LogpiePieChart("各代理 所有订单利润分布图", "订单类别",
                "订单利润");

        final OrderDAO orderDAO = new OrderDAO();
        final List<Order> allOrderList = orderDAO.getAllOrders();
        final Map<String, Double> orderInAdminMap = AccountingLogic
                .getOrderProfitsInAdmin(allOrderList);
        final List<KeyValue> pieDataList = GoogleChartHelper.getPieDataListFromMap(orderInAdminMap);
        accountingOrderInAdminPieChartPage.addObject(PIE_CHART_DATA_LIST_1, pieDataList);
        accountingOrderInAdminPieChartPage.addObject(PIE_CHART_1, orderInCategoryPieChart1);

        if (yearMonth != null)
        {
            final String year = yearMonth.substring(0, 4);
            final String month = yearMonth.substring(5, 7);
            final List<Order> orderInMonthList = orderDAO.getOrdersByMonth(year, month);
            if (orderInMonthList != null && !orderInMonthList.isEmpty())
            {
                final String chartLabel = String.format("各代理 %s年%s月 订单利润分布图", year, month);
                final LogpiePieChart orderInCategoryPieChart2 = new LogpiePieChart(chartLabel,
                        "订单代理", "订单利润");
                final Map<String, Double> orderInMonthAdminMap2 = AccountingLogic
                        .getOrderProfitsInAdmin(orderInMonthList);
                final List<KeyValue> pieDataList2 = GoogleChartHelper
                        .getPieDataListFromMap(orderInMonthAdminMap2);
                accountingOrderInAdminPieChartPage.addObject(PIE_CHART_DATA_LIST_2, pieDataList2);
                accountingOrderInAdminPieChartPage.addObject(PIE_CHART_2, orderInCategoryPieChart2);
            }
        }

        return accountingOrderInAdminPieChartPage;
    }

    private Object handleOrderProfitInBrand(final String yearMonth)
    {
        final ModelAndView accountingOrderInAdminPieChartPage = new ModelAndView(
                "accounting_piechart");
        final LogpiePieChart orderInCategoryPieChart1 = new LogpiePieChart("各品牌 所有订单利润分布图", "订单类别",
                "订单利润");

        final OrderDAO orderDAO = new OrderDAO();
        final List<Order> allOrderList = orderDAO.getAllOrders();
        final Map<String, Double> orderProfitInBrandMap = AccountingLogic
                .getOrderProfitsInBrand(allOrderList);
        final List<KeyValue> pieDataList = GoogleChartHelper
                .getPieDataListFromMap(orderProfitInBrandMap);
        accountingOrderInAdminPieChartPage.addObject(PIE_CHART_DATA_LIST_1, pieDataList);
        accountingOrderInAdminPieChartPage.addObject(PIE_CHART_1, orderInCategoryPieChart1);

        if (yearMonth != null)
        {
            final String year = yearMonth.substring(0, 4);
            final String month = yearMonth.substring(5, 7);
            final List<Order> orderInMonthList = orderDAO.getOrdersByMonth(year, month);
            if (orderInMonthList != null && !orderInMonthList.isEmpty())
            {
                final String chartLabel = String.format("各品牌 %s年%s月 订单利润分布图", year, month);
                final LogpiePieChart orderInCategoryPieChart2 = new LogpiePieChart(chartLabel,
                        "订单品牌", "订单利润");
                final Map<String, Double> orderProfitInBrandMap2 = AccountingLogic
                        .getOrderProfitsInBrand(orderInMonthList);
                final List<KeyValue> pieDataList2 = GoogleChartHelper
                        .getPieDataListFromMap(orderProfitInBrandMap2);
                accountingOrderInAdminPieChartPage.addObject(PIE_CHART_DATA_LIST_2, pieDataList2);
                accountingOrderInAdminPieChartPage.addObject(PIE_CHART_2, orderInCategoryPieChart2);
            }
        }

        return accountingOrderInAdminPieChartPage;
    }
}
