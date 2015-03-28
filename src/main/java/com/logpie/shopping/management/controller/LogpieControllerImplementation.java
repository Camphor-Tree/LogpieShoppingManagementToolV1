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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.logpie.shopping.management.accounting.logic.AccountingLogic;
import com.logpie.shopping.management.accounting.logic.GoogleChartHelper;
import com.logpie.shopping.management.accounting.logic.GoogleChartHelper.KeyValue;
import com.logpie.shopping.management.accounting.logic.LogpieLineChart;
import com.logpie.shopping.management.accounting.logic.LogpiePieChart;
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
        final OrderDAO orderDAO = new OrderDAO(mCurrentAdmin);
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

        final CategoryDAO categoryDAO = new CategoryDAO(mCurrentAdmin);
        final List<Category> categoryList = categoryDAO.getAllCategory();
        orderManagementPage.addObject("categoryList", categoryList);

        final ImageDAO imageDAO = new ImageDAO(mCurrentAdmin);
        final List<Image> imageList = imageDAO.getAllImage();
        orderManagementPage.addObject("imageList", imageList);

        final BrandDAO brandDAO = new BrandDAO(mCurrentAdmin);
        final List<Brand> brandList = brandDAO.getAllBrand();
        orderManagementPage.addObject("brandList", brandList);

        if (mCurrentAdmin.isSuperAdmin())
        {
            final AdminDAO adminDAO = new AdminDAO(mCurrentAdmin);
            final List<Admin> adminList = adminDAO.getAllAdmins();
            orderManagementPage.addObject("adminList", adminList);
        }

        final List<LogpiePackage> packageList = getPackageListFromOrderList(orderList);
        orderManagementPage.addObject("packageList", packageList);

        final ProductDAO productDAO = new ProductDAO(mCurrentAdmin);
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
            final HttpServletResponse httpResponse, @RequestParam("id") String orderId,
            final RedirectAttributes redirectAttrs)
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
     * Used to show log page
     * 
     * @return
     */
    public abstract Object showLogPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse);

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

        return "redirect:/order_management";
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

        return "redirect:/order_management";
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

        return "redirect:/order_management";
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

        return "redirect:/order_management";
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

        return "redirect:/order_management";
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
        final LogpieLineChart orderInCategoryPieChart1 = new LogpieLineChart("Logpie订单量 最近12天 走势图",
                "日期", "订单数量");
        List<Order> orderListWithinNdays = orderDAO.getOrdersWithinNdays(12);
        if (!mCurrentAdmin.isSuperAdmin())
        {
            orderListWithinNdays = filterOutOrdersNotBelongToAdmin(orderListWithinNdays,
                    mCurrentAdmin);
        }
        final Map<String, Integer> orderWithinNdaysMap = AccountingLogic.getOrderNumbers(true, 12,
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
        final LogpieLineChart orderInCategoryPieChart1 = new LogpieLineChart("Logpie 利润 最近12天 走势图",
                "日期", "订单数量");
        List<Order> orderListWithinNdays = orderDAO.getOrdersWithinNdays(12);
        if (!mCurrentAdmin.isSuperAdmin())
        {
            orderListWithinNdays = filterOutOrdersNotBelongToAdmin(orderListWithinNdays,
                    mCurrentAdmin);
        }
        final Map<String, Double> orderWithinNdaysMap = AccountingLogic.getOrderProfits(true, 12,
                orderListWithinNdays);
        final List<KeyValue> lineDataList1 = GoogleChartHelper
                .getLineChartDataListFromStringDoubleMap(orderWithinNdaysMap, false);
        orderNumberLineChartPage.addObject(LINE_CHART_DATA_LIST_1, lineDataList1);
        orderNumberLineChartPage.addObject(LINE_CHART_1, orderInCategoryPieChart1);

        final LogpieLineChart orderInCategoryPieChart2 = new LogpieLineChart("Logpie利润 最近12个月 走势图",
                "日期", "订单数量");
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
        List<Order> allOrderList = orderDAO.getAllOrders();
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
        List<Order> allOrderList = orderDAO.getAllOrders();
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

    private Object handleOrderProfitInCategory(final String yearMonth)
    {
        final ModelAndView accountingOrderInAdminPieChartPage = new ModelAndView(
                "accounting_piechart");
        final LogpiePieChart orderInCategoryPieChart1 = new LogpiePieChart("各类别 所有订单利润分布图", "订单类别",
                "订单利润");

        final OrderDAO orderDAO = new OrderDAO(null);
        List<Order> allOrderList = orderDAO.getAllOrders();
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
        final LogpiePieChart orderInCategoryPieChart1 = new LogpiePieChart("各品牌 所有订单利润分布图", "订单品牌",
                "订单利润");

        final OrderDAO orderDAO = new OrderDAO(null);
        List<Order> allOrderList = orderDAO.getAllOrders();
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

}
