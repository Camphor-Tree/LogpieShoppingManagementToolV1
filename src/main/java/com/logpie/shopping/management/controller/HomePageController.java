package com.logpie.shopping.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomePageController {
	
	@RequestMapping(value = "/signin", produces = "text/plain; charset=utf-8")
	public ModelAndView showHomePage()
	{
		ModelAndView homePage = new ModelAndView("sign_in");
		homePage.addObject("hello","test hello");
		return homePage;
	}
}
