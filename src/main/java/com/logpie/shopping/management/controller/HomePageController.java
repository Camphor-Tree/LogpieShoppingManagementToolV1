package com.logpie.shopping.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomePageController {
	
	@RequestMapping("/LogpieManagement")
	public ModelAndView showHomePage()
	{
		ModelAndView homePage = new ModelAndView("LogpieManagementHome");
		homePage.addObject("hello","test hello");
		return homePage;
	}
}
