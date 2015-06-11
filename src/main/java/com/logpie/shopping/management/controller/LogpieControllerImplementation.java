// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.logpie.shopping.management.accounting.logic.AccountingLogic;
import com.logpie.shopping.management.accounting.logic.GoogleChartHelper;
import com.logpie.shopping.management.accounting.logic.GoogleChartHelper.KeyValue;
import com.logpie.shopping.management.accounting.logic.LogpieLineChart;
import com.logpie.shopping.management.accounting.logic.LogpiePieChart;
import com.logpie.shopping.management.auth.logic.LogpiePageAlertMessage;
import com.logpie.shopping.management.backup.LogpieBackupManager;
import com.logpie.shopping.management.business.logic.LogpieProfitCalculator;
import com.logpie.shopping.management.business.logic.LogpieSettleDownOrderLogic;
import com.logpie.shopping.management.coupon.CouponCodeGenerator;
import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.Brand;
import com.logpie.shopping.management.model.Category;
import com.logpie.shopping.management.model.Client;
import com.logpie.shopping.management.model.Coupon;
import com.logpie.shopping.management.model.Image;
import com.logpie.shopping.management.model.LogpiePackage;
import com.logpie.shopping.management.model.Order;
import com.logpie.shopping.management.model.Product;
import com.logpie.shopping.management.model.SettleDownRecord;
import com.logpie.shopping.management.storage.AdminDAO;
import com.logpie.shopping.management.storage.BrandDAO;
import com.logpie.shopping.management.storage.CategoryDAO;
import com.logpie.shopping.management.storage.ClientDAO;
import com.logpie.shopping.management.storage.CouponDAO;
import com.logpie.shopping.management.storage.ImageDAO;
import com.logpie.shopping.management.storage.LogpiePackageDAO;
import com.logpie.shopping.management.storage.OrderDAO;
import com.logpie.shopping.management.storage.ProductDAO;
import com.logpie.shopping.management.storage.SettleDownRecordDAO;
import com.logpie.shopping.management.util.CurrencyRateUtils;

/**
 * Define what are the flows controller need to implement. Also the common
 * implementations for both super admin and normal admin should put them here.
 * 
 * @author zhoyilei
 *
 */
public abstract class LogpieControllerImplementation
{
    private static final Logger LOG = Logger.getLogger(LogpieControllerImplementation.class);

    protected Admin mCurrentAdmin;

    LogpieControllerImplementation(final Admin admin)
    {
        mCurrentAdmin = admin;
    }

    /**
     * For Order Controller
     */
    /*
     * Order management page should be visible for all admins. Just the order
     * list should be different.
     */
    public Object showOrderManagementPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs,
            final String adminId, final String buyerNameEscape, final String packageId,
            final Boolean showAll, final String orderBy)
    {
        final String buyerName = HtmlUtils.htmlUnescape(buyerNameEscape);
        long time1 = System.currentTimeMillis();
        LOG.debug("Authenticate cookie is valid. Going to order manage page.");
        final ModelAndView orderManagementPage = new ModelAndView("order_management");
        long time2 = System.currentTimeMillis();
        if (redirectAttrs.containsAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS))
        {
            final String message = (String) redirectAttrs.getFlashAttributes().get(
                    LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS);
            orderManagementPage.addObject(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    message);
        }
        if (redirectAttrs.containsAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL))
        {
            final String message = (String) redirectAttrs.getFlashAttributes().get(
                    LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL);
            orderManagementPage.addObject(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL, message);
        }
        long time3 = System.currentTimeMillis();
        final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
        List<Order> orderList = null;

        if (adminId != null)
        {
            if (mCurrentAdmin.isSuperAdmin())
            {
                if (!StringUtils.isEmpty(orderBy))
                {
                    if (orderBy.equals("orderId"))
                    {
                        orderList = orderDAO.getOrdersForProxy(adminId, null);
                    }
                    else if (orderBy.equals("package"))
                    {
                        // Make the null value in front, then order by package
                        // id by desc.
                        // http://stackoverflow.com/questions/9307613/mysql-order-by-null-first-and-desc-after
                        orderList = orderDAO.getOrdersForProxy(adminId,
                                Order.DB_KEY_ORDER_PACKAGE_ID + " IS NULL DESC, "
                                        + Order.DB_KEY_ORDER_PACKAGE_ID + " DESC");
                    }
                    else if (orderBy.equals("buyerName"))
                    {
                        orderList = orderDAO.getOrdersForProxy(adminId,
                                Order.DB_KEY_ORDER_BUYER_NAME);
                    }
                    else
                    {
                        orderList = orderDAO.getOrdersForProxy(adminId, null);
                    }
                    orderManagementPage.addObject("orderBy", orderBy);
                }
                else
                {
                    orderList = orderDAO.getOrdersForProxy(adminId, null);
                }
                orderManagementPage.addObject("filterByAdmin", "admin=" + adminId);
            }
            else
            {
                return showNoPermissionPage();
            }
        }
        else if (buyerName != null)
        {
            try
            {
                final String decodedBuyerName = new String(buyerName.getBytes("iso-8859-1"),
                        "UTF-8");
                orderList = orderDAO.getOrdersForBuyerName(decodedBuyerName);
                if (!mCurrentAdmin.isSuperAdmin())
                {
                    orderList = filterOutOrdersNotBelongToAdmin(orderList, mCurrentAdmin);
                }
            } catch (UnsupportedEncodingException e)
            {
                orderList = injectOrderManagementOrderList(null);
            }
        }
        else if (packageId != null)
        {
            orderList = orderDAO.getOrdersForPackage(packageId);
            if (!mCurrentAdmin.isSuperAdmin())
            {
                orderList = filterOutOrdersNotBelongToAdmin(orderList, mCurrentAdmin);
            }
        }
        else
        {
            if (!StringUtils.isEmpty(orderBy))
            {
                if (orderBy.equals("orderId"))
                {
                    // it will default order by order id
                    orderList = injectOrderManagementOrderList(null);
                }
                else if (orderBy.equals("package"))
                {
                    // Make the null value in front, then order by package
                    // id by desc.
                    // http://stackoverflow.com/questions/9307613/mysql-order-by-null-first-and-desc-after
                    orderList = injectOrderManagementOrderList(Order.DB_KEY_ORDER_PACKAGE_ID
                            + " IS NULL DESC, " + Order.DB_KEY_ORDER_PACKAGE_ID + " DESC");
                }
                else if (orderBy.equals("buyerName"))
                {
                    orderList = injectOrderManagementOrderList(Order.DB_KEY_ORDER_BUYER_NAME);
                }
                else
                {
                    orderList = orderDAO.getOrdersForProxy(adminId, null);
                }
                orderManagementPage.addObject("orderBy", orderBy);
            }
            else
            {
                return "redirect:/order_management?orderBy=orderId";
            }
        }
        long time4 = System.currentTimeMillis();

        if (showAll == null || showAll == false)
        {
            orderList = filterOutOrdersAlreadySettleDown(orderList);
            orderManagementPage.addObject("showAll", false);
        }
        else
        {
            orderManagementPage.addObject("showAll", true);
        }

        long time5 = System.currentTimeMillis();

        orderManagementPage.addObject("orderList", orderList);

        // Use all the orders list to generate the buyers list.
        final List<String> orderBuyersList = getBuyerList(orderList);
        orderManagementPage.addObject("orderBuyersList", orderBuyersList);

        long time6 = System.currentTimeMillis();

        final CategoryDAO categoryDAO = new CategoryDAO(mCurrentAdmin);
        final List<Category> categoryList = categoryDAO.getAllCategory();
        orderManagementPage.addObject("categoryList", categoryList);
        long time7 = System.currentTimeMillis();

        final ImageDAO imageDAO = new ImageDAO(mCurrentAdmin);
        final List<Image> imageList = imageDAO.getAllImage();
        orderManagementPage.addObject("imageList", imageList);

        long time8 = System.currentTimeMillis();

        final BrandDAO brandDAO = new BrandDAO(mCurrentAdmin);
        final List<Brand> brandList = brandDAO.getAllBrand();
        orderManagementPage.addObject("brandList", brandList);

        long time9 = System.currentTimeMillis();
        if (mCurrentAdmin.isSuperAdmin())
        {
            final AdminDAO adminDAO = new AdminDAO(mCurrentAdmin);
            final List<Admin> adminList = adminDAO.getAllAdmins();
            orderManagementPage.addObject("adminList", adminList);
        }
        long time10 = System.currentTimeMillis();
        // Used for filter
        final List<LogpiePackage> packageList = getPackageListFromOrderList(orderList);
        orderManagementPage.addObject("packageList", packageList);
        // Used for create new order to assign package
        final LogpiePackageDAO packageDAO = new LogpiePackageDAO(mCurrentAdmin);
        final List<LogpiePackage> allPackageList = packageDAO.getAllPackage();
        orderManagementPage.addObject("allPackageList", allPackageList);
        long time11 = System.currentTimeMillis();

        final ProductDAO productDAO = new ProductDAO(mCurrentAdmin);
        final List<Product> productList = productDAO.getAllProduct();
        orderManagementPage.addObject("productList", productList);
        long time12 = System.currentTimeMillis();

        final LogpieProfitCalculator profitCalculator = new LogpieProfitCalculator(orderList);
        orderManagementPage.addObject("profitCalculator", profitCalculator);
        long time13 = System.currentTimeMillis();
        // inject the current admin into the page
        orderManagementPage.addObject("admin", mCurrentAdmin);
        long time14 = System.currentTimeMillis();

        // inject the current currency rate into the page
        final float currencyRate = CurrencyRateUtils.getUScurrencyRate();
        orderManagementPage.addObject("CurrencyRate", currencyRate);

        long time15 = System.currentTimeMillis();

        injectCurrentUrl(request, orderManagementPage);

        long time16 = System.currentTimeMillis();

        long totaltime = time16 - time1;

        final StringBuilder metricBuilder = new StringBuilder();
        metricBuilder.append("总时间:" + totaltime + "<br/>");
        metricBuilder.append("创建view:" + (time2 - time1) + " 耗时百分比:"
                + ((double) (time2 - time1) / totaltime) + "<br/>");
        metricBuilder.append("alertmessage:" + (time3 - time2) + " 耗时百分比:"
                + ((double) (time3 - time2) / totaltime) + "<br/>");
        metricBuilder.append("查询所有订单:" + (time4 - time3) + " 耗时百分比:"
                + ((double) (time4 - time3) / totaltime) + "<br/>");
        metricBuilder.append("去除已经清算的订单:" + (time5 - time4) + " 耗时百分比:"
                + ((double) (time5 - time4) / totaltime) + "<br/>");
        metricBuilder.append("获取购买者列表:" + (time6 - time5) + " 耗时百分比:"
                + ((double) (time6 - time5) / totaltime) + "<br/>");
        metricBuilder.append("获取分类列表:" + (time7 - time6) + " 耗时百分比:"
                + ((double) (time7 - time6) / totaltime) + "<br/>");
        metricBuilder.append("获取图片列表" + (time8 - time7) + " 耗时百分比:"
                + ((double) (time8 - time7) / totaltime) + "<br/>");
        metricBuilder.append("获取品牌列表:" + (time9 - time8) + " 耗时百分比:"
                + ((double) (time9 - time8) / totaltime) + "<br/>");
        metricBuilder.append("获取管理员列表:" + (time10 - time9) + " 耗时百分比:"
                + ((double) (time10 - time9) / totaltime) + "<br/>");
        metricBuilder.append("获取包裹列表:" + (time11 - time10) + " 耗时百分比:"
                + ((double) (time11 - time10) / totaltime) + "<br/>");
        metricBuilder.append("获取产品列表:" + (time12 - time11) + " 耗时百分比:"
                + ((double) (time12 - time11) / totaltime) + "<br/>");
        metricBuilder.append("利润计算:" + (time13 - time12) + " 耗时百分比:"
                + ((double) (time13 - time12) / totaltime) + "<br/>");
        metricBuilder.append("插入管理员:" + (time14 - time13) + " 耗时百分比:"
                + ((double) (time14 - time13) / totaltime) + "<br/>");
        metricBuilder.append("获取汇率:" + (time15 - time14) + " 耗时百分比:"
                + ((double) (time15 - time14) / totaltime) + "<br/>");
        metricBuilder.append("插入当前url:" + (time16 - time15) + " 耗时百分比:"
                + ((double) (time16 - time15) / totaltime) + "<br/>");
        orderManagementPage.addObject("metric", metricBuilder.toString());

        final OrderFilterLogic filterLogic = new OrderFilterLogic(adminId, buyerName, orderBy,
                showAll);
        orderManagementPage.addObject("filterLogic", filterLogic);
        return orderManagementPage;
    }

    /**
     * @param request
     * @param orderManagementPage
     */
    protected void injectCurrentUrl(final HttpServletRequest request,
            final ModelAndView orderManagementPage)
    {
        try
        {
            String requestUrl = request.getRequestURL().toString();
            if (!StringUtils.isEmpty(request.getQueryString()))
            {
                requestUrl += "?" + request.getQueryString();
            }
            orderManagementPage.addObject("CurrentUrl",
                    Base64.encodeBase64String(requestUrl.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e)
        {
            LOG.error("no support for UTF-8", e);
        }
    }

    /**
     * Used to inject the order management page's order list. For super admin,
     * should inject all the orders. For normal admin, should just inject the
     * orders we are as the proxy.
     * 
     * @return
     */
    public abstract List<Order> injectOrderManagementOrderList(final String orderByAttributes);

    /**
     * All admin can create order.
     * 
     * @param request
     * @param httpResponse
     * @param redirectAttrs
     * @return
     */
    public Object createNewOrder(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to create a new order.");
        final Order newOrder = Order.readNewOrderFromRequest(request);
        boolean createOrderSuccess = false;
        if (newOrder != null)
        {
            final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
            createOrderSuccess = orderDAO.addOrder(newOrder);
        }

        if (createOrderSuccess)
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    "创建一个新的订单 给购买者:" + newOrder.getOrderBuyerName() + " 成功!");
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                    "创建一个新的订单 给购买者:" + newOrder.getOrderBuyerName() + " 失败");
        }

        return "redirect:/order_management?orderBy=orderId";
    }

    /**
     * Show singple order information
     * 
     * @param request
     * @param packageId
     * @return
     */
    public Object showOrderDetailPage(final HttpServletRequest request,
            @RequestParam("id") String orderId)
    {
        LOG.debug("Authenticate cookie is valid. Going to order page.");
        final ModelAndView orderDetailPage = new ModelAndView("order_detail");
        final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
        final Order order = orderDAO.getOrderById(orderId);
        if (order != null)
        {
            // If just normal admin, cannot view the detail of the order doesn't
            // belong to him/her.
            if (!mCurrentAdmin.isSuperAdmin()
                    && !order.getOrderProxy().getAdminId().equals(mCurrentAdmin.getAdminId()))
            {
                return showNoPermissionPage();
            }
            orderDetailPage.addObject("order", order);
        }
        return orderDetailPage;
    }

    public Object showModifyOrderPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final String orderId, final String redirectUrl,
            final String anchor, final RedirectAttributes redirectAttrs)
    {
        final ModelAndView modifyOrderPage = new ModelAndView("order_edit");
        final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
        final Order order = orderDAO.getOrderById(orderId);

        if (order != null)
        {
            // If just normal admin, cannot modify the detail of the order
            // doesn't belong to him/her.
            if (!mCurrentAdmin.isSuperAdmin()
                    && !order.getOrderProxy().getAdminId().equals(mCurrentAdmin.getAdminId()))
            {
                return showNoPermissionPage();
            }
            modifyOrderPage.addObject("order", order);

            final AdminDAO adminDAO = new AdminDAO(mCurrentAdmin);
            final List<Admin> adminList = adminDAO.getAllAdmins();
            modifyOrderPage.addObject("adminList", adminList);

            final LogpiePackageDAO packageDAO = new LogpiePackageDAO(mCurrentAdmin);
            final List<LogpiePackage> packageList = packageDAO.getAllPackage();
            modifyOrderPage.addObject("packageList", packageList);

            final ProductDAO productDAO = new ProductDAO(mCurrentAdmin);
            final List<Product> productList = productDAO.getAllProduct();
            modifyOrderPage.addObject("productList", productList);

            // inject the current admin into the page
            modifyOrderPage.addObject("admin", mCurrentAdmin);
            try
            {
                modifyOrderPage.addObject("RedirectUrl",
                        new String(Base64.decodeBase64(redirectUrl), "UTF-8"));
                if (anchor != null)
                {
                    modifyOrderPage.addObject("Anchor", anchor);
                }
            } catch (UnsupportedEncodingException e)
            {
                LOG.error("UTF-8 is not supported", e);
            }
        }

        return modifyOrderPage;
    }

    public Object modifyOrder(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to create a new order.");
        final Order modifiedOrder = Order.readModifiedOrderFromRequest(request);
        boolean updateOrderSuccess = false;
        if (modifiedOrder != null)
        {
            // If just normal admin, cannot modify the detail of the order
            // doesn't belong to him/her.
            if (!mCurrentAdmin.isSuperAdmin()
                    && !modifiedOrder.getOrderProxy().getAdminId()
                            .equals(mCurrentAdmin.getAdminId()))
            {
                return showNoPermissionPage();
            }
            final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
            updateOrderSuccess = orderDAO.updateOrderProfile(modifiedOrder);
        }

        if (updateOrderSuccess)
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    "更新订单 购买者:" + modifiedOrder.getOrderBuyerName() + " 成功");
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                    "更新订单 购买者:" + modifiedOrder.getOrderBuyerName() + " 失败");
        }

        String redirectUrl = request.getParameter("RedirectUrl");
        redirectUrl = HtmlUtils.htmlUnescape(redirectUrl);
        final String anchor = request.getParameter("Anchor");
        if (anchor != null)
        {
            redirectUrl += "#" + anchor;
        }
        return "redirect:" + redirectUrl;
    }

    /**
     * Used to show the order settle down page.
     * 
     * @return
     */
    public Object showOrderSettleDownPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final String adminId,
            final RedirectAttributes redirectAttrs)
    {
        if (!mCurrentAdmin.isSuperAdmin() && !adminId.equals(mCurrentAdmin.getAdminId()))
        {
            return this.showNoPermissionPage();
        }
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
            orderSettleDownPage.addObject("settleDownAdmin", currentAdminToSettleDown);
            final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
            List<Order> orderList = orderDAO.getOrdersForProxy(adminId, null);
            // get all the orders need to be settled down.
            orderList = getOrderNeedToSettleDown(orderList);
            orderSettleDownPage.addObject("orderList", orderList);
        }
        injectCurrentUrl(request, orderSettleDownPage);

        orderSettleDownPage.addObject("admin", mCurrentAdmin);
        return orderSettleDownPage;
    }

    /**
     * Used to handle the order settle down logic. Only super admin can operate
     * this.
     * 
     * @return
     */
    public abstract Object handleOrderSettleDown(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final String adminId,
            final List<String> settleDownOrders, final String proxyOweCompanyMoney,
            final String proxyProfit, final String companyProfit,
            final RedirectAttributes redirectAttrs);

    private List<LogpiePackage> getPackageListFromOrderList(final List<Order> orderList)
    {
        final Set<String> packageSet = new HashSet<String>();
        final List<LogpiePackage> packageList = new ArrayList<LogpiePackage>();
        if (orderList != null)
        {
            for (final Order order : orderList)
            {
                final LogpiePackage logpiePackage = order.getOrderPackage();
                if (logpiePackage == null)
                {
                    continue;
                }
                final String packageId = logpiePackage.getPackageId();
                if (!packageSet.contains(packageId))
                {
                    packageSet.add(packageId);
                    packageList.add(logpiePackage);
                }
            }
        }
        return packageList;
    }

    private List<String> getBuyerList(final List<Order> orderList)
    {
        final Set<String> buyerSet = new HashSet<String>();
        final List<String> buyerList = new ArrayList<String>();
        if (orderList != null)
        {
            for (final Order order : orderList)
            {
                final String buyerName = order.getOrderBuyerName();
                if (!buyerSet.contains(buyerName))
                {
                    buyerSet.add(buyerName);
                    buyerList.add(buyerName);
                }
            }
        }
        return buyerList;
    }

    /**
     * For Pacakge Controller
     * 
     * Normal admins can view the packages have order belong to them
     */
    public Object showPackageManagementPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs,
            final Boolean showAll, final Boolean showAllDelivered)
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
        final LogpiePackageDAO packageDAO = new LogpiePackageDAO(mCurrentAdmin);
        List<LogpiePackage> packageList = packageDAO.getAllPackage();

        if (showAll == null || showAll == false)
        {
            if (showAllDelivered != null && showAllDelivered == true)
            {
                packageList = getPackageAlreadyDeliveredList(packageList);
            }
            else
            {
                packageList = filterOutPackageAlreadyReceived(packageList);
            }
            packageManagementPage.addObject("showAll", false);
        }
        else
        {
            packageManagementPage.addObject("showAll", true);
        }

        // If normal admin, only can view packages have order belong to him,
        if (!mCurrentAdmin.isSuperAdmin())
        {
            packageList = filterOutPackageNotBelongToCurrentAdmin(packageList);
        }
        packageManagementPage.addObject("packageList", packageList);
        packageManagementPage.addObject("admin", mCurrentAdmin);

        return packageManagementPage;
    }

    public Object showPackageDetailPage(final HttpServletRequest request,
            @RequestParam("id") String packageId)
    {
        LOG.debug("Authenticate cookie is valid. Going to package page.");
        final ModelAndView packageDetailPage = new ModelAndView("package_detail");
        final LogpiePackageDAO packageDAO = new LogpiePackageDAO(mCurrentAdmin);
        final LogpiePackage logpiePackage = packageDAO.getPackageById(packageId);
        if (logpiePackage != null)
        {
            packageDetailPage.addObject("logpiePackage", logpiePackage);
            final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
            final List<Order> orderList = orderDAO.getOrdersForPackage(packageId);
            packageDetailPage.addObject("orderList", orderList);
            // Calculate the total weight
            packageDetailPage.addObject("packageTotalWeight",
                    String.valueOf(calculateTotalWeight(orderList)));

            // inject the current admin into the page
            packageDetailPage.addObject("admin", mCurrentAdmin);
        }
        injectCurrentUrl(request, packageDetailPage);
        return packageDetailPage;
    }

    protected Float calculateTotalWeight(final List<Order> orderList)
    {
        if (orderList == null || orderList.size() == 0)
        {
            return 0.0f;
        }
        Float totalWeight = 0.0f;
        for (Order order : orderList)
        {
            totalWeight += order.getOrderWeight();
        }
        return totalWeight;
    }

    abstract Object quickCalculateShippingFeeDistribution(final HttpServletRequest request,
            @RequestParam("id") String packageId);

    abstract Object createPackage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs);

    abstract public Object showModifyPackagePage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String packageId,
            final RedirectAttributes redirectAttrs);

    abstract public Object modifyPackage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs);

    abstract public Object markPackageDelivered(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String packageId,
            final RedirectAttributes redirectAttrs);

    /**
     * For Brand Controller.
     * 
     * All then handle should be same for all admins.
     */
    public Object showAllBrands(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView createbrandPage = new ModelAndView("brand_management");
        final BrandDAO BrandDAO = new BrandDAO(mCurrentAdmin);
        final List<Brand> brandList = BrandDAO.getAllBrand();
        createbrandPage.addObject("brandList", brandList);
        return createbrandPage;
    }

    public Object showModifybrandPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String brandId,
            final RedirectAttributes redirectAttrs)
    {
        final ModelAndView modifybrandPage = new ModelAndView("brand_edit");

        final BrandDAO BrandDAO = new BrandDAO(mCurrentAdmin);
        final Brand brand = BrandDAO.getBrandById(brandId);
        modifybrandPage.addObject("brand", brand);

        final CategoryDAO categoryDAO = new CategoryDAO(mCurrentAdmin);
        final List<Category> categoryList = categoryDAO.getAllCategory();
        modifybrandPage.addObject("categoryList", categoryList);

        final ImageDAO imageDAO = new ImageDAO(mCurrentAdmin);
        final List<Image> imageList = imageDAO.getAllImage();
        modifybrandPage.addObject("imageList", imageList);

        return modifybrandPage;
    }

    public Object modifyBrand(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final Brand modifiedBrand = Brand.readModifiedBrandFromRequest(request);
        boolean updatCateogrySuccess = false;
        if (modifiedBrand != null)
        {
            final BrandDAO BrandDAO = new BrandDAO(mCurrentAdmin);
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

    /**
     * For Category Controller. All the logic handle should be same for both
     * super admin and normal admin
     */
    public Object showAllCategories(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView createCategoryPage = new ModelAndView("category_management");
        final CategoryDAO categoryDAO = new CategoryDAO(mCurrentAdmin);
        final List<Category> categoryList = categoryDAO.getAllCategory();
        createCategoryPage.addObject("categoryList", categoryList);

        return createCategoryPage;
    }

    public Object showModifyCategoryPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String categoryId,
            final RedirectAttributes redirectAttrs)
    {
        final ModelAndView modifyCategoryPage = new ModelAndView("category_edit");

        final CategoryDAO categoryDAO = new CategoryDAO(mCurrentAdmin);
        final Category category = categoryDAO.getCategoryById(categoryId);
        modifyCategoryPage.addObject("category", category);

        return modifyCategoryPage;
    }

    public Object modifyCategory(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final Category modifiedCateogry = Category.readModifiedCategoryFromRequest(request);
        boolean updatCateogrySuccess = false;
        if (modifiedCateogry != null)
        {
            final CategoryDAO categoryDAO = new CategoryDAO(mCurrentAdmin);
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

    /**
     * For image controller All the logic handle should be same for both super
     * admin and normal admin
     */
    public Object showAllImages(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView createimagePage = new ModelAndView("image_management");
        final ImageDAO ImageDAO = new ImageDAO(mCurrentAdmin);
        final List<Image> imageList = ImageDAO.getAllImage();
        createimagePage.addObject("imageList", imageList);

        return createimagePage;
    }

    public Object showModifyImagePage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String imageId,
            final RedirectAttributes redirectAttrs)
    {
        final ModelAndView modifyimagePage = new ModelAndView("image_edit");

        final ImageDAO ImageDAO = new ImageDAO(mCurrentAdmin);
        final Image image = ImageDAO.getImageById(imageId);
        modifyimagePage.addObject("image", image);

        return modifyimagePage;
    }

    public Object modifyImage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final Image modifiedImage = Image.readModifiedImageFromRequest(request);
        boolean updatCateogrySuccess = false;
        if (modifiedImage != null)
        {
            final ImageDAO ImageDAO = new ImageDAO(mCurrentAdmin);
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

    /**
     * For Product controller All the logic handle should be same for both super
     * admin and normal admin
     */
    public Object showProductManagementPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to product manage page.");
        final ModelAndView productManagementPage = new ModelAndView("product_management");
        final ProductDAO ProductDAO = new ProductDAO(mCurrentAdmin);
        final List<Product> productList = ProductDAO.getAllProduct();
        productManagementPage.addObject("productList", productList);
        return productManagementPage;
    }

    public Object showModifyproductPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String productId,
            final RedirectAttributes redirectAttrs)
    {
        final ModelAndView modifyproductPage = new ModelAndView("product_edit");

        final ProductDAO ProductDAO = new ProductDAO(mCurrentAdmin);
        final Product product = ProductDAO.getProductById(productId);
        modifyproductPage.addObject("product", product);

        final BrandDAO brandDAO = new BrandDAO(mCurrentAdmin);
        final List<Brand> brandList = brandDAO.getAllBrand();
        modifyproductPage.addObject("brandList", brandList);

        final ImageDAO imageDAO = new ImageDAO(mCurrentAdmin);
        final List<Image> imageList = imageDAO.getAllImage();
        modifyproductPage.addObject("imageList", imageList);

        return modifyproductPage;
    }

    public Object modifyProduct(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final Product modifiedProduct = Product.readModifiedProductFromRequest(request);
        boolean updatCateogrySuccess = false;
        if (modifiedProduct != null)
        {
            final ProductDAO ProductDAO = new ProductDAO(mCurrentAdmin);
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

    /**
     * For client controller All the logic handle should be same for both super
     * admin and normal admin
     */
    public Object showAllClients(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView createclientPage = new ModelAndView("client_management");
        final ClientDAO ClientDAO = new ClientDAO(mCurrentAdmin);
        final List<Client> clientList = ClientDAO.getAllClient();
        createclientPage.addObject("clientList", clientList);

        return createclientPage;
    }

    public Object showModifyClientPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String clientId,
            final RedirectAttributes redirectAttrs)
    {
        final ModelAndView modifyclientPage = new ModelAndView("client_edit");

        final ClientDAO ClientDAO = new ClientDAO(mCurrentAdmin);
        final Client client = ClientDAO.getClientById(clientId);
        modifyclientPage.addObject("client", client);

        return modifyclientPage;
    }

    public Object modifyClient(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final Client modifiedClient = Client.readModifiedClientFromRequest(request);
        boolean updatCateogrySuccess = false;
        if (modifiedClient != null)
        {
            final ClientDAO ClientDAO = new ClientDAO(mCurrentAdmin);
            updatCateogrySuccess = ClientDAO.updateClientProfile(modifiedClient);
        }

        if (updatCateogrySuccess)
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    "更新图片:" + modifiedClient.getClientShowName() + " 信息，成功!");
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL, "更新图片:"
                    + modifiedClient.getClientShowName() + " 信息，失败!");
        }
        return "redirect:/client_management";
    }

    /**
     * Used to show coupon management page
     * 
     * @return
     */
    public abstract Object showLogPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse);

    public Object showCouponManagementPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        ModelAndView couponManagementPage = new ModelAndView("coupon_management");
        // inject the current admin into the page
        couponManagementPage.addObject("admin", mCurrentAdmin);
        if (redirectAttrs.containsAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS))
        {
            final String message = (String) redirectAttrs.getFlashAttributes().get(
                    LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS);
            couponManagementPage.addObject(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    message);
        }
        if (redirectAttrs.containsAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL))
        {
            final String message = (String) redirectAttrs.getFlashAttributes().get(
                    LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL);
            couponManagementPage.addObject(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL, message);
        }
        return couponManagementPage;
    }

    public Object generateCoupon(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final String productPrice,
            final String couponValue)
    {

        if (StringUtils.isEmpty(productPrice))
        {
            return "redirect:/coupon_management";
        }
        ModelAndView couponManagementPage = new ModelAndView("coupon_management");
        // inject the current admin into the page
        couponManagementPage.addObject("admin", mCurrentAdmin);
        try
        {
            final String couponCode = CouponCodeGenerator.getCouponCodeByPrice(Integer
                    .parseInt(productPrice));
            couponManagementPage.addObject("CouponCode", couponCode);
        } catch (Exception e)
        {
            return "redirect:/coupon_management";
        }

        return couponManagementPage;
    }

    public Object validateCoupon(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final String couponCode)
    {

        if (StringUtils.isEmpty(couponCode))
        {
            return "redirect:/coupon_management";
        }
        ModelAndView couponManagementPage = new ModelAndView("coupon_management");
        // inject the current admin into the page
        couponManagementPage.addObject("admin", mCurrentAdmin);
        try
        {
            final String couponValue = CouponCodeGenerator.validateCouponCode(couponCode);
            if (couponValue != null)
            {
                couponManagementPage.addObject("ValidateCouponCode", couponCode);
                couponManagementPage.addObject("ValidateCouponValue", couponValue);
            }
            else
            {
                couponManagementPage.addObject("ValidateCouponCode", couponCode);
                couponManagementPage.addObject("ValidateCouponValue", "无效");
            }
        } catch (Exception e)
        {
            return "redirect:/coupon_management";
        }

        return couponManagementPage;
    }

    public Object useCoupon(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final String couponCode,
            final RedirectAttributes redirectAttrs)
    {

        if (StringUtils.isEmpty(couponCode))
        {
            return "redirect:/coupon_management";
        }
        ModelAndView couponManagementPage = new ModelAndView("coupon_management");
        // inject the current admin into the page
        couponManagementPage.addObject("admin", mCurrentAdmin);
        try
        {
            final String couponValue = CouponCodeGenerator.validateCouponCode(couponCode);
            if (couponValue != null)
            {
                final CouponDAO couponDAO = new CouponDAO(mCurrentAdmin);
                boolean isCouponAlreadyUsed = couponDAO.isCouponAlreadyUsed(new Coupon(null,
                        couponCode));

                if (isCouponAlreadyUsed)
                {
                    redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                            "优惠券:" + couponCode + "，已使用过");
                    return "redirect:/coupon_management";
                }
                final boolean useCouponSuccess = couponDAO.addCoupon(new Coupon(null, couponCode));

                if (useCouponSuccess)
                {
                    redirectAttrs.addFlashAttribute(
                            LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS, "使用优惠券:"
                                    + couponCode + "，成功!");
                }
                else
                {
                    redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                            "使用优惠券:" + couponCode + "，失败!");
                }
            }
            else
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                        "优惠券:" + couponCode + "，无效");
            }
        } catch (Exception e)
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                    "使用优惠券服务器错误!");
        }
        return "redirect:/coupon_management";
    }

    /**
     * @return
     */
    protected Object showNoPermissionPage()
    {
        ModelAndView noPermissionPage = new ModelAndView("no_permission");
        return noPermissionPage;
    }

    /*
     * For Simple Create Controller
     */
    public Object showCreateCategoryPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView createCategoryPage = new ModelAndView("create_category");
        return createCategoryPage;
    }

    public Object createCategory(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to create a new category.");
        final Category newCategory = Category.readNewCategoryFromRequest(request);
        boolean createCategorySuccess = false;
        if (newCategory != null)
        {
            final CategoryDAO categoryDAO = new CategoryDAO(mCurrentAdmin);
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

        return "redirect:/order_management?orderBy=orderId";
    }

    /**
     * Only super admin can create admin account
     * 
     * @param request
     * @param httpResponse
     * @return
     */
    public Object createAdmin(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final Admin newAdmin = Admin.readNewAdminFromRequest(request);
        boolean createAdminSuccess = false;
        if (newAdmin != null)
        {
            final AdminDAO adminDAO = new AdminDAO(mCurrentAdmin);
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

        return "redirect:/order_management?orderBy=orderId";
    }

    public Object createImage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to create a new image.");
        final Image newImage = Image.readNewImageFromRequest(request);
        boolean createImageSuccess = false;
        if (newImage != null)
        {
            final ImageDAO imageDAO = new ImageDAO(mCurrentAdmin);
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

        return "redirect:/order_management?orderBy=orderId";
    }

    public Object createBrand(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to create a new logpiePackage.");
        final Brand newBrand = Brand.readNewBrandFromRequest(request);
        boolean createBrandSuccess = false;
        if (newBrand != null)
        {
            final BrandDAO brandDAO = new BrandDAO(mCurrentAdmin);
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

        return "redirect:/order_management?orderBy=orderId";
    }

    public Object createProduct(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to create a new logpiePackage.");
        final Product newProduct = Product.readNewProductFromRequest(request);
        boolean createNewProductSuccess = false;
        if (newProduct != null)
        {
            final ProductDAO productDAO = new ProductDAO(mCurrentAdmin);
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

        return "redirect:/order_management?orderBy=orderId";
    }

    public Object createClient(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to create a new logpiePackage.");
        final Client newClient = Client.readNewClientFromRequest(request);
        boolean createNewClientSuccess = false;
        if (newClient != null)
        {
            final ClientDAO productDAO = new ClientDAO(mCurrentAdmin);
            createNewClientSuccess = productDAO.addClient(newClient);
        }

        if (createNewClientSuccess)
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    "创建新的用户:" + newClient.getClientShowName() + " 成功!");
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                    "创建新的用户:" + newClient.getClientShowName() + " 失败!");
        }

        return "redirect:/order_management?orderBy=orderId";
    }

    public Object searchResult(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs,
            final String searchString)
    {
        long timeBeforeSearch = System.currentTimeMillis();
        final ModelAndView view = new ModelAndView("search_result");
        final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
        final LogpiePackageDAO packageDAO = new LogpiePackageDAO(mCurrentAdmin);
        final ClientDAO clientDAO = new ClientDAO(mCurrentAdmin);
        String searchStringOriginal = searchString;
        List<Order> orderList = new ArrayList<Order>();
        List<LogpiePackage> packageList = new ArrayList<LogpiePackage>();
        List<Client> clientList = new ArrayList<Client>();
        try
        {
            searchStringOriginal = new String(HtmlUtils.htmlUnescape(searchString).getBytes(
                    "iso-8859-1"));
            orderList = orderDAO.searchOrders(searchStringOriginal);
            view.addObject("orderList", orderList);

            packageList = packageDAO.searchPackage(searchStringOriginal);
            if (!mCurrentAdmin.isSuperAdmin())
            {
                packageList = filterOutPackageNotBelongToCurrentAdmin(packageList);
            }
            view.addObject("packageList", packageList);

            clientList = clientDAO.searchClient(searchStringOriginal);
            view.addObject("clientList", clientList);

        } catch (UnsupportedEncodingException e)
        {
            LOG.error("UnsupportedEncodingException when trying to parse search string", e);
            view.addObject("orderList", null);
        }
        long timeAfterSearch = System.currentTimeMillis();
        view.addObject("SearchPerformance", timeAfterSearch - timeBeforeSearch);

        view.addObject("SearchString", searchStringOriginal);
        view.addObject("SearchResultsCount",
                orderList.size() + packageList.size() + clientList.size());
        view.addObject("OrdersCount", orderList.size());
        view.addObject("PackagesCount", packageList.size());
        view.addObject("ClientsCount", clientList.size());
        view.addObject("admin", mCurrentAdmin);

        return view;
    }

    protected List<Order> filterOutOrdersNotBelongToAdmin(final List<Order> orderList,
            final Admin admin)
    {
        final List<Order> orderAfterFilter = new ArrayList<Order>();
        for (final Order order : orderList)
        {
            if (order.getOrderProxy().getAdminId().equals(admin.getAdminId()))
            {
                orderAfterFilter.add(order);
            }
        }
        return orderAfterFilter;

    }

    protected List<Order> filterOutOrdersAlreadySettleDown(final List<Order> orderList)
    {
        final LogpieSettleDownOrderLogic settleDownLogic = new LogpieSettleDownOrderLogic();
        final List<Order> orderAfterFilter = new ArrayList<Order>();
        for (final Order order : orderList)
        {
            // If haven't settle down, then add to the list
            if (!settleDownLogic.isOrderAlreadySettleDown(order))
            {
                orderAfterFilter.add(order);
            }
        }
        return orderAfterFilter;
    }

    protected List<Order> getOrderNeedToSettleDown(final List<Order> orderList)
    {
        final LogpieSettleDownOrderLogic settleDownLogic = new LogpieSettleDownOrderLogic();
        final List<Order> orderNeedToSettleDown = new ArrayList<Order>();
        for (final Order order : orderList)
        {
            if (settleDownLogic.isOrderNeedSettleDown(order))
            {
                orderNeedToSettleDown.add(order);
            }
        }
        return orderNeedToSettleDown;
    }

    /**
     * For Accounting controller
     */

    private static final String PIE_CHART_DATA_LIST_1 = "PieChartDataList1";
    private static final String PIE_CHART_1 = "PieChart1";
    private static final String PIE_CHART_DATA_LIST_2 = "PieChartDataList2";
    private static final String PIE_CHART_2 = "PieChart2";

    private static final String LINE_CHART_DATA_LIST_1 = "LineChartDataList1";
    private static final String LINE_CHART_1 = "LineChart1";
    private static final String LINE_CHART_DATA_LIST_2 = "LineChartDataList2";
    private static final String LINE_CHART_2 = "LineChart2";

    public Object showAccountingPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to accounting page.");
        final ModelAndView accountingHomePage = new ModelAndView("accounting");
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        accountingHomePage.addObject("Today", dateFormat.format(calendar.getTime()));
        accountingHomePage.addObject("admin", mCurrentAdmin);
        return accountingHomePage;
    }

    /**
     * PieChart showing each category's order number It will show the all the
     * orders. If you specify a year and month, it will also show a specific
     * month's result
     */
    public Object showPieChartAccounting(final HttpServletRequest request,
            final HttpServletResponse httpResponse,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "year_month", required = false) String yearMonth)
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
        else if (type.equals("OrderProfitInCategory"))
        {
            return handleOrderProfitInCategory(yearMonth);
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

    /**
     * LineChart showing the numbers of orders, profits Logpie makes in daily or
     * monthly mode.
     */
    public Object showLineChartAccounting(final HttpServletRequest request,
            final HttpServletResponse httpResponse,
            @RequestParam(value = "type", required = true) String type)
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

    private Object handleOrderNumbers()
    {
        final ModelAndView orderNumberLineChartPage = new ModelAndView("accounting_linechart");
        final OrderDAO orderDAO = new OrderDAO(null);
        final LogpieLineChart orderInCategoryPieChart1 = new LogpieLineChart("Logpie订单量 最近30天 走势图",
                "日期", "订单数量");
        List<Order> orderListWithinNdays = orderDAO.getOrdersWithinNdays(30);
        if (!mCurrentAdmin.isSuperAdmin())
        {
            orderListWithinNdays = filterOutOrdersNotBelongToAdmin(orderListWithinNdays,
                    mCurrentAdmin);
        }
        final Map<String, Integer> orderWithinNdaysMap = AccountingLogic.getOrderNumbers(true, 30,
                orderListWithinNdays);
        final List<KeyValue> lineDataList1 = GoogleChartHelper
                .getLineChartDataListFromStringIntegerMap(orderWithinNdaysMap);
        orderNumberLineChartPage.addObject(LINE_CHART_DATA_LIST_1, lineDataList1);
        orderNumberLineChartPage.addObject(LINE_CHART_1, orderInCategoryPieChart1);

        final LogpieLineChart orderInCategoryPieChart2 = new LogpieLineChart(
                "Logpie订单量 最近12个月 走势图", "日期", "订单数量");
        List<Order> orderListWithinNmonths = orderDAO.getOrdersWithinNmonths(12);
        if (!mCurrentAdmin.isSuperAdmin())
        {
            orderListWithinNmonths = filterOutOrdersNotBelongToAdmin(orderListWithinNmonths,
                    mCurrentAdmin);
        }
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
        final OrderDAO orderDAO = new OrderDAO(null);
        final LogpieLineChart orderInCategoryPieChart1 = new LogpieLineChart("Logpie 利润 最近30天 走势图",
                "日期", "订单利润");
        List<Order> orderListWithinNdays = orderDAO.getOrdersWithinNdays(30);
        if (!mCurrentAdmin.isSuperAdmin())
        {
            orderListWithinNdays = filterOutOrdersNotBelongToAdmin(orderListWithinNdays,
                    mCurrentAdmin);
        }
        final Map<String, Double> orderWithinNdaysMap = AccountingLogic.getOrderProfits(true, 30,
                orderListWithinNdays);
        final List<KeyValue> lineDataList1 = GoogleChartHelper
                .getLineChartDataListFromStringDoubleMap(orderWithinNdaysMap, false);
        orderNumberLineChartPage.addObject(LINE_CHART_DATA_LIST_1, lineDataList1);
        orderNumberLineChartPage.addObject(LINE_CHART_1, orderInCategoryPieChart1);

        final LogpieLineChart orderInCategoryPieChart2 = new LogpieLineChart("Logpie利润 最近12个月 走势图",
                "日期", "订单利润");
        List<Order> orderListWithinNmonths = orderDAO.getOrdersWithinNmonths(12);
        if (!mCurrentAdmin.isSuperAdmin())
        {
            orderListWithinNmonths = filterOutOrdersNotBelongToAdmin(orderListWithinNmonths,
                    mCurrentAdmin);
        }
        final Map<String, Double> orderWithinNmonthsMap = AccountingLogic.getOrderProfits(false,
                12, orderListWithinNmonths);
        final List<KeyValue> lineDataList2 = GoogleChartHelper
                .getLineChartDataListFromStringDoubleMap(orderWithinNmonthsMap, true);
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

        final OrderDAO orderDAO = new OrderDAO(null);
        List<Order> allOrderList = orderDAO.getAllOrders(null);
        if (!mCurrentAdmin.isSuperAdmin())
        {
            allOrderList = filterOutOrdersNotBelongToAdmin(allOrderList, mCurrentAdmin);
        }
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
            List<Order> orderInMonthList = orderDAO.getOrdersByMonth(year, month);
            if (!mCurrentAdmin.isSuperAdmin())
            {
                orderInMonthList = filterOutOrdersNotBelongToAdmin(orderInMonthList, mCurrentAdmin);
            }

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

        final OrderDAO orderDAO = new OrderDAO(null);
        List<Order> allOrderList = orderDAO.getAllOrders(null);
        if (!mCurrentAdmin.isSuperAdmin())
        {
            allOrderList = filterOutOrdersNotBelongToAdmin(allOrderList, mCurrentAdmin);
        }
        final Map<String, Integer> orderInBrandMap = AccountingLogic.getOrdersInBrand(allOrderList);
        final List<KeyValue> pieDataList = GoogleChartHelper.getPieDataListFromMap(orderInBrandMap);
        accountingOrderInBrandPieChartPage.addObject(PIE_CHART_DATA_LIST_1, pieDataList);
        accountingOrderInBrandPieChartPage.addObject(PIE_CHART_1, orderInCategoryPieChart1);

        if (yearMonth != null)
        {
            final String year = yearMonth.substring(0, 4);
            final String month = yearMonth.substring(5, 7);
            List<Order> orderInMonthList = orderDAO.getOrdersByMonth(year, month);
            if (!mCurrentAdmin.isSuperAdmin())
            {
                orderInMonthList = filterOutOrdersNotBelongToAdmin(orderInMonthList, mCurrentAdmin);
            }
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
        if (!mCurrentAdmin.isSuperAdmin())
        {
            return showNoPermissionPage();
        }
        final ModelAndView accountingOrderInAdminPieChartPage = new ModelAndView(
                "accounting_piechart");
        final LogpiePieChart orderInCategoryPieChart1 = new LogpiePieChart("各代理 所有订单数量分布图", "订单类别",
                "订单数量");

        final OrderDAO orderDAO = new OrderDAO(null);
        final List<Order> allOrderList = orderDAO.getAllOrders(null);

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

    private Object handleOrderProfitInCategory(final String yearMonth)
    {
        final ModelAndView accountingOrderInAdminPieChartPage = new ModelAndView(
                "accounting_piechart");
        final LogpiePieChart orderInCategoryPieChart1 = new LogpiePieChart("各类别 所有订单利润分布图", "订单类别",
                "订单利润");

        final OrderDAO orderDAO = new OrderDAO(null);
        List<Order> allOrderList = orderDAO.getAllOrders(null);
        if (!mCurrentAdmin.isSuperAdmin())
        {
            allOrderList = filterOutOrdersNotBelongToAdmin(allOrderList, mCurrentAdmin);
        }
        final Map<String, Double> orderProfitInBrandMap = AccountingLogic
                .getOrderProfitsInCategory(allOrderList);
        final List<KeyValue> pieDataList = GoogleChartHelper
                .getPieDataListFromMap(orderProfitInBrandMap);
        accountingOrderInAdminPieChartPage.addObject(PIE_CHART_DATA_LIST_1, pieDataList);
        accountingOrderInAdminPieChartPage.addObject(PIE_CHART_1, orderInCategoryPieChart1);

        if (yearMonth != null)
        {
            final String year = yearMonth.substring(0, 4);
            final String month = yearMonth.substring(5, 7);
            List<Order> orderInMonthList = orderDAO.getOrdersByMonth(year, month);
            if (!mCurrentAdmin.isSuperAdmin())
            {
                orderInMonthList = filterOutOrdersNotBelongToAdmin(orderInMonthList, mCurrentAdmin);
            }
            if (orderInMonthList != null && !orderInMonthList.isEmpty())
            {
                final String chartLabel = String.format("各类别 %s年%s月 订单利润分布图", year, month);
                final LogpiePieChart orderInCategoryPieChart2 = new LogpiePieChart(chartLabel,
                        "订单类别", "订单利润");
                final Map<String, Double> orderProfitInBrandMap2 = AccountingLogic
                        .getOrderProfitsInCategory(orderInMonthList);
                final List<KeyValue> pieDataList2 = GoogleChartHelper
                        .getPieDataListFromMap(orderProfitInBrandMap2);
                accountingOrderInAdminPieChartPage.addObject(PIE_CHART_DATA_LIST_2, pieDataList2);
                accountingOrderInAdminPieChartPage.addObject(PIE_CHART_2, orderInCategoryPieChart2);
            }
        }

        return accountingOrderInAdminPieChartPage;
    }

    private Object handleOrderProfitInAdmin(final String yearMonth)
    {
        if (!mCurrentAdmin.isSuperAdmin())
        {
            return showNoPermissionPage();
        }
        final ModelAndView accountingOrderInAdminPieChartPage = new ModelAndView(
                "accounting_piechart");
        final LogpiePieChart orderInCategoryPieChart1 = new LogpiePieChart("各代理 所有订单利润分布图", "订单代理",
                "订单利润");

        final OrderDAO orderDAO = new OrderDAO(null);
        final List<Order> allOrderList = orderDAO.getAllOrders(null);
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
        final LogpiePieChart orderInCategoryPieChart1 = new LogpiePieChart("各品牌 所有订单利润分布图", "订单品牌",
                "订单利润");

        final OrderDAO orderDAO = new OrderDAO(null);
        List<Order> allOrderList = orderDAO.getAllOrders(null);
        if (!mCurrentAdmin.isSuperAdmin())
        {
            allOrderList = filterOutOrdersNotBelongToAdmin(allOrderList, mCurrentAdmin);
        }
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
            List<Order> orderInMonthList = orderDAO.getOrdersByMonth(year, month);
            if (!mCurrentAdmin.isSuperAdmin())
            {
                orderInMonthList = filterOutOrdersNotBelongToAdmin(orderInMonthList, mCurrentAdmin);
            }
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

    private List<LogpiePackage> getPackageAlreadyDeliveredList(final List<LogpiePackage> packageList)
    {
        final List<LogpiePackage> packagesAfterFilter = new ArrayList<LogpiePackage>();
        for (final LogpiePackage logpiePackage : packageList)
        {
            // If already delivered, then add to the list
            if (logpiePackage.getPackageIsDelivered())
            {
                packagesAfterFilter.add(logpiePackage);
            }
        }
        return packagesAfterFilter;
    }

    private List<LogpiePackage> filterOutPackageAlreadyReceived(
            final List<LogpiePackage> packageList)
    {
        final List<LogpiePackage> packagesAfterFilter = new ArrayList<LogpiePackage>();
        for (final LogpiePackage logpiePackage : packageList)
        {
            // If haven't received, then add to the list
            if (!logpiePackage.getPackageIsDelivered())
            {
                packagesAfterFilter.add(logpiePackage);
            }
        }
        return packagesAfterFilter;
    }

    private List<LogpiePackage> filterOutPackageNotBelongToCurrentAdmin(
            final List<LogpiePackage> packageList)
    {
        final List<LogpiePackage> packagesAfterFilter = new ArrayList<LogpiePackage>();
        for (final LogpiePackage logpiePackage : packageList)
        {
            final String packageId = logpiePackage.getPackageId();
            final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
            final List<Order> orderList = orderDAO.getOrdersForPackage(packageId);
            boolean belongToCurrentAdmin = false;
            for (final Order order : orderList)
            {
                if (order.getOrderProxy().getAdminId().equals(mCurrentAdmin.getAdminId()))
                {
                    belongToCurrentAdmin = true;
                    break;
                }
            }

            if (belongToCurrentAdmin)
            {
                packagesAfterFilter.add(logpiePackage);
            }
        }
        return packagesAfterFilter;
    }

    /**
     * Show system backup page.
     * 
     * @param request
     * @param httpResponse
     * @return
     */
    public Object showSystemBackupPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final ModelAndView systemBackupPage = new ModelAndView("system_backup");
        final LogpieBackupManager backupManager = new LogpieBackupManager();
        final List<String> backupHistory = backupManager.getBackupHistory();
        systemBackupPage.addObject("BackupHistory", backupHistory);
        injectAlertActionMessage(redirectAttrs, systemBackupPage);
        return systemBackupPage;
    }

    /**
     * @param redirectAttrs
     * @param page
     */
    protected void injectAlertActionMessage(final RedirectAttributes redirectAttrs,
            final ModelAndView page)
    {
        if (redirectAttrs.containsAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS))
        {
            final String message = (String) redirectAttrs.getFlashAttributes().get(
                    LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS);
            page.addObject(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS, message);
        }
        if (redirectAttrs.containsAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL))
        {
            final String message = (String) redirectAttrs.getFlashAttributes().get(
                    LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL);
            page.addObject(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL, message);
        }
    }

    public Object backupDatabase(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        final LogpieBackupManager backupManager = new LogpieBackupManager();
        final boolean backupSuccess = backupManager.backupDatabase();
        if (backupSuccess)
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                    "备份数据库成功!");
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                    "备份数据库失败!");
        }

        return "redirect:/system_backup";
    }

    public Object showSettleDownRecordPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView settleDownHistoryPage = new ModelAndView("settle_down_history");

        List<SettleDownRecord> settleDownRecordList;
        final SettleDownRecordDAO settleDownRecordDAO = new SettleDownRecordDAO(mCurrentAdmin);
        if (mCurrentAdmin.isSuperAdmin())
        {
            settleDownRecordList = settleDownRecordDAO.getAllSettleDownRecords();
        }
        else
        {
            settleDownRecordList = settleDownRecordDAO.getSettleDownRecordsByAdmin(mCurrentAdmin);
        }
        settleDownHistoryPage.addObject("SettleDownRecords", settleDownRecordList);
        injectHistoryTotalInformation(settleDownRecordList, settleDownHistoryPage);
        return settleDownHistoryPage;
    }

    private void injectHistoryTotalInformation(final List<SettleDownRecord> settleDownRecordList,
            final ModelAndView view)
    {
        float totalProxyPaidCompanyMoney = 0.0f;
        float totalCompanyProfit = 0.0f;
        float totalProxyProfit = 0.0f;
        try
        {
            for (final SettleDownRecord record : settleDownRecordList)
            {
                final String recordInfo = record.getSettleDownRecordInfo();

                final JSONObject settleDownRecordInfoJSON = new JSONObject(recordInfo);
                totalProxyPaidCompanyMoney += Float.parseFloat(settleDownRecordInfoJSON
                        .getString("proxyOweCompanyMoney"));
                totalCompanyProfit += Float.parseFloat(settleDownRecordInfoJSON
                        .getString("companyProfit"));
                totalProxyProfit += Float.parseFloat(settleDownRecordInfoJSON
                        .getString("proxyProfit"));
            }
            view.addObject("totalProxyPaidCompanyMoney", String.valueOf(totalProxyPaidCompanyMoney));
            view.addObject("totalCompanyProfit", String.valueOf(totalCompanyProfit));
            view.addObject("totalProxyProfit", String.valueOf(totalProxyProfit));

        } catch (JSONException e)
        {
        }

    }
}
