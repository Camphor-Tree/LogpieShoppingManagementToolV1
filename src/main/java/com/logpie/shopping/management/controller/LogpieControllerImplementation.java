// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.controller;

import java.io.UnsupportedEncodingException;
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

import com.logpie.shopping.management.auth.controller.PageHistoryHandler;
import com.logpie.shopping.management.auth.logic.LogpiePageAlertMessage;
import com.logpie.shopping.management.business.logic.LogpieProfitCalculator;
import com.logpie.shopping.management.business.logic.LogpieSettleDownOrderLogic;
import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.model.Brand;
import com.logpie.shopping.management.model.Category;
import com.logpie.shopping.management.model.Image;
import com.logpie.shopping.management.model.LogpiePackage;
import com.logpie.shopping.management.model.Order;
import com.logpie.shopping.management.model.Product;
import com.logpie.shopping.management.storage.AdminDAO;
import com.logpie.shopping.management.storage.BrandDAO;
import com.logpie.shopping.management.storage.CategoryDAO;
import com.logpie.shopping.management.storage.ImageDAO;
import com.logpie.shopping.management.storage.LogpiePackageDAO;
import com.logpie.shopping.management.storage.OrderDAO;
import com.logpie.shopping.management.storage.ProductDAO;
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
            final String adminId, final String buyerName, final String packageId,
            final Boolean showAll)
    {
        LOG.debug("Authenticate cookie is valid. Going to order manage page.");
        final ModelAndView orderManagementPage = new ModelAndView("order_management");
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
        final OrderDAO orderDAO = new OrderDAO();
        List<Order> orderList;

        if (adminId != null)
        {
            if (mCurrentAdmin.isSuperAdmin() || mCurrentAdmin.getAdminId().equals(adminId))
            {
                orderList = orderDAO.getOrdersForProxy(adminId);
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
                orderList = injectOrderManagementOrderList();
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
            orderList = injectOrderManagementOrderList();
        }

        if (showAll == null || showAll == false)
        {
            orderList = filterOutOrdersAlreadySettledDown(orderList);
            orderManagementPage.addObject("showAll", false);
        }
        else
        {
            orderManagementPage.addObject("showAll", true);
        }

        orderManagementPage.addObject("orderList", orderList);

        // Use all the orders list to generate the buyers list.
        final List<String> orderBuyersList = getBuyerList(orderList);
        orderManagementPage.addObject("orderBuyersList", orderBuyersList);

        final CategoryDAO categoryDAO = new CategoryDAO();
        final List<Category> categoryList = categoryDAO.getAllCategory();
        orderManagementPage.addObject("categoryList", categoryList);

        final ImageDAO imageDAO = new ImageDAO();
        final List<Image> imageList = imageDAO.getAllImage();
        orderManagementPage.addObject("imageList", imageList);

        final BrandDAO brandDAO = new BrandDAO();
        final List<Brand> brandList = brandDAO.getAllBrand();
        orderManagementPage.addObject("brandList", brandList);

        if (mCurrentAdmin.isSuperAdmin())
        {
            final AdminDAO adminDAO = new AdminDAO();
            final List<Admin> adminList = adminDAO.getAllAdmins();
            orderManagementPage.addObject("adminList", adminList);
        }

        final List<LogpiePackage> packageList = getPackageListFromOrderList(orderList);
        orderManagementPage.addObject("packageList", packageList);

        final ProductDAO productDAO = new ProductDAO();
        final List<Product> productList = productDAO.getAllProduct();
        orderManagementPage.addObject("productList", productList);

        final LogpieProfitCalculator profitCalculator = new LogpieProfitCalculator(orderList);
        orderManagementPage.addObject("profitCalculator", profitCalculator);

        // inject the current admin into the page
        orderManagementPage.addObject("admin", mCurrentAdmin);

        // inject the current currency rate into the page
        final float currencyRate = CurrencyRateUtils.getUScurrencyRate();
        orderManagementPage.addObject("CurrencyRate", currencyRate);

        return orderManagementPage;
    }

    /**
     * Used to inject the order management page's order list. For super admin,
     * should inject all the orders. For normal admin, should just inject the
     * orders we are as the proxy.
     * 
     * @return
     */
    public abstract List<Order> injectOrderManagementOrderList();

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
            final OrderDAO orderDAO = new OrderDAO();
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

        return "redirect:/order_management";
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
        final OrderDAO orderDAO = new OrderDAO();
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
            final HttpServletResponse httpResponse, @RequestParam("id") String orderId,
            final RedirectAttributes redirectAttrs)
    {
        final ModelAndView modifyOrderPage = new ModelAndView("order_edit");
        final OrderDAO orderDAO = new OrderDAO();
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

            final AdminDAO adminDAO = new AdminDAO();
            final List<Admin> adminList = adminDAO.getAllAdmins();
            modifyOrderPage.addObject("adminList", adminList);

            final LogpiePackageDAO packageDAO = new LogpiePackageDAO();
            final List<LogpiePackage> packageList = packageDAO.getAllPackage();
            modifyOrderPage.addObject("packageList", packageList);

            final ProductDAO productDAO = new ProductDAO();
            final List<Product> productList = productDAO.getAllProduct();
            modifyOrderPage.addObject("productList", productList);

            // inject the current admin into the page
            modifyOrderPage.addObject("admin", mCurrentAdmin);
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
            final OrderDAO orderDAO = new OrderDAO();
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

        final String previous1Url = PageHistoryHandler.getPrevious1Url(request);
        if (previous1Url != null)
        {
            return "redirect:" + previous1Url;
        }

        return "redirect:/order_management";
    }

    /**
     * Used to show the order settle down page, only super admin can see that
     * page.
     * 
     * @return
     */
    public abstract Object showOrderSettleDownPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final String adminId,
            final RedirectAttributes redirectAttrs);

    /**
     * Used to handle the order settle down logic. Only super admin can operate
     * this.
     * 
     * @return
     */
    public abstract Object handleOrderSettleDown(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final String adminId,
            final List<String> settleDownOrders, final RedirectAttributes redirectAttrs);

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
     * The package info should only be viewed and modified by super admin.
     */
    abstract Object showPackageManagementPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs,
            final Boolean showAll);

    public Object showPackageDetailPage(final HttpServletRequest request,
            @RequestParam("id") String packageId)
    {
        LOG.debug("Authenticate cookie is valid. Going to package page.");
        final ModelAndView packageDetailPage = new ModelAndView("package_detail");
        final LogpiePackageDAO packageDAO = new LogpiePackageDAO();
        final LogpiePackage logpiePackage = packageDAO.getPackageById(packageId);
        if (logpiePackage != null)
        {
            packageDetailPage.addObject("logpiePackage", logpiePackage);
            final OrderDAO orderDAO = new OrderDAO();
            final List<Order> orderList = orderDAO.getOrdersForPackage(packageId);
            packageDetailPage.addObject("orderList", orderList);
            // Calculate the total weight
            packageDetailPage.addObject("packageTotalWeight",
                    String.valueOf(calculateTotalWeight(orderList)));

            // inject the current admin into the page
            packageDetailPage.addObject("admin", mCurrentAdmin);
        }
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

    /**
     * For Brand Controller.
     * 
     * All then handle should be same for all admins.
     */
    public Object showAllBrands(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView createbrandPage = new ModelAndView("brand_management");
        final BrandDAO BrandDAO = new BrandDAO();
        final List<Brand> brandList = BrandDAO.getAllBrand();
        createbrandPage.addObject("brandList", brandList);
        return createbrandPage;
    }

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

    /**
     * For Category Controller. All the logic handle should be same for both
     * super admin and normal admin
     */
    public Object showAllCategories(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView createCategoryPage = new ModelAndView("category_management");
        final CategoryDAO categoryDAO = new CategoryDAO();
        final List<Category> categoryList = categoryDAO.getAllCategory();
        createCategoryPage.addObject("categoryList", categoryList);

        return createCategoryPage;
    }

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

    /**
     * For image controller All the logic handle should be same for both super
     * admin and normal admin
     */
    public Object showAllImages(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView createimagePage = new ModelAndView("image_management");
        final ImageDAO ImageDAO = new ImageDAO();
        final List<Image> imageList = ImageDAO.getAllImage();
        createimagePage.addObject("imageList", imageList);

        return createimagePage;
    }

    public Object showModifyImagePage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, @RequestParam("id") String imageId,
            final RedirectAttributes redirectAttrs)
    {
        final ModelAndView modifyimagePage = new ModelAndView("image_edit");

        final ImageDAO ImageDAO = new ImageDAO();
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

    /**
     * For Product controller All the logic handle should be same for both super
     * admin and normal admin
     */
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

    /**
     * @return
     */
    protected Object showNoPermissionPage()
    {
        ModelAndView noPermissionPage = new ModelAndView("no_permission");
        return noPermissionPage;
    }

    private List<Order> filterOutOrdersNotBelongToAdmin(final List<Order> orderList,
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

    private List<Order> filterOutOrdersAlreadySettledDown(final List<Order> orderList)
    {
        final LogpieSettleDownOrderLogic settleDownLogic = new LogpieSettleDownOrderLogic();
        final List<Order> orderAfterFilter = new ArrayList<Order>();
        for (final Order order : orderList)
        {
            // If haven't settle down, then add to the list
            if (settleDownLogic.isOrderNeedSettleDown(order))
            {
                orderAfterFilter.add(order);
            }
        }
        return orderAfterFilter;
    }
}
