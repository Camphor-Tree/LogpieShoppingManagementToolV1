package com.logpie.shopping.management.controller;

import org.springframework.web.servlet.ModelAndView;

/**
 * Used to inject some common info to the view
 * 
 * @author yilei
 *
 */
public class LogpieBaseController
{
    private static final String ACTIVE_TAB = "ActiveTab";

    protected void injectCurrentActiveTab(Object object, String activeTab)
    {
        if (object instanceof ModelAndView)
        {
            ModelAndView view = (ModelAndView) object;
            view.addObject(ACTIVE_TAB, activeTab);
        }
    }

}
