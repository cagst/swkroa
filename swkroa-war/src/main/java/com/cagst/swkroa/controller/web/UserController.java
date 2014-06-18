package com.cagst.swkroa.controller.web;

import com.cagst.swkroa.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles and retrieves the membership page(s) depending on the URI template.
 *
 * @author Craig Gaskill
 *
 * @version 1.0.0
 *
 */
@Controller
public final class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	/**
	 * Handles and retrieves the User Home / Listing page.
	 *
	 * @return The location and name of the page template.
	 */
	@RequestMapping(value = "/user/home", method = RequestMethod.GET)
	public ModelAndView getUsersPage() {
		LOGGER.info("Received request to show Users Page");

		ModelAndView mav = new ModelAndView("user/index");

		return mav;
	}
}
