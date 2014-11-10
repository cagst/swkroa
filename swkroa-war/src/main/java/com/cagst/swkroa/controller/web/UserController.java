package com.cagst.swkroa.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles and retrieves the membership page(s) depending on the URI template.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@Controller
public final class UserController {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

  /**
   * Handles and retrieves the User Home / Listing page.
   *
   * @return The location and name of the page template.
   */
  @RequestMapping(value = "/maintain/users", method = RequestMethod.GET)
  public String getUsersPage() {
    LOGGER.info("Received request to show users listing page");

    return "maintain/user";
  }
}
