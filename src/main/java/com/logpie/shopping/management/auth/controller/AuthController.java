package com.logpie.shopping.management.auth.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.logpie.shopping.management.auth.logic.AuthenticationHelper;
import com.logpie.shopping.management.auth.logic.CookieManager;
import com.logpie.shopping.management.auth.logic.LogpiePageAlertMessage;
import com.logpie.shopping.management.model.Admin;
import com.logpie.shopping.management.storage.AdminDAO;

@Controller
public class AuthController
{
    private static final Logger LOG = Logger.getLogger(AuthController.class);

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public ModelAndView showSignInPage(final HttpServletRequest request,
            final HttpServletResponse httpResponse)
    {
        final ModelAndView signinPage = new ModelAndView("signin");
        return signinPage;
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public Object signin(final HttpServletRequest request, final HttpServletResponse httpResponse)
    {
        LOG.debug("receiving post signin request");
        final String email = request.getParameter("email");
        final String password = request.getParameter("password");
        final AdminDAO adminDAO = new AdminDAO();
        final Admin admin = adminDAO.verifyAccount(email, password);
        if (admin == null)
        {
            return "redirect:/signin";
        }
        else
        {
            final CookieManager cookieManager = new CookieManager();
            final Cookie cookie = cookieManager.setupAuthCookie(admin);
            httpResponse.addCookie(cookie);
            return "redirect:/home";
        }
    }

    @RequestMapping(value = "/logout")
    public Object logout(final HttpServletRequest request, final HttpServletResponse httpResponse)
    {
        LOG.debug("receiving logout request");
        final CookieManager cookieManager = new CookieManager();
        final Cookie emptyAuthCookie = cookieManager.getEmptyAuthCookie();
        httpResponse.addCookie(emptyAuthCookie);
        return "redirect:/signin";
    }

    @RequestMapping(value = "/account_management", method = RequestMethod.GET)
    public Object showModifyAdminProfilePage(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to show modify admin profile page.");
        final Admin admin = AuthenticationHelper.getAdminFromCookie(request);

        final ModelAndView modifyAdminProfilePage = new ModelAndView("admin_profile");
        modifyAdminProfilePage.addObject("admin", admin);
        return modifyAdminProfilePage;
    }

    @RequestMapping(value = "/admin/edit", method = RequestMethod.POST)
    public Object modifyAdminProfile(final HttpServletRequest request,
            final HttpServletResponse httpResponse, final RedirectAttributes redirectAttrs)
    {
        LOG.debug("Authenticate cookie is valid. Going to modify the admin information.");
        final Admin admin = AuthenticationHelper.getAdminFromCookie(request);
        final Admin modifiedAdmin = Admin.readModifiedAdminFromRequest(request);

        if (admin.isSuperAdmin() || admin.getAdminId().equals(modifiedAdmin.getAdminId()))
        {
            boolean updateAdminSuccess = false;
            if (modifiedAdmin != null)
            {
                final AdminDAO adminDAO = new AdminDAO();
                updateAdminSuccess = adminDAO.updateAdminProfile(modifiedAdmin);
            }

            if (updateAdminSuccess)
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_SUCCESS,
                        "更新个人信息，成功!");
            }
            else
            {
                redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                        "更新个人信息信息，失败!");
            }
        }
        else
        {
            redirectAttrs.addFlashAttribute(LogpiePageAlertMessage.KEY_ACTION_MESSAGE_FAIL,
                    "你不可以修改他人的个人信息!");
        }
        return "redirect:/order_management";
    }
}
