package com.cagst.swkroa.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles and retrieves the CodeSet pages depending on the URI template.
 *
 * @author Craig Gaskill
 */
@Controller
@RequestMapping(value = "/maintain")
public final class MaintenanceController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MaintenanceController.class);

  /**
   * Handles and retrieves the CodeSet page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "codeset", method = RequestMethod.GET)
  public String getMaintainCodeSetsPage() {
    LOGGER.info("Received request to show codesets page.");

    return "maintain/codeset";
  }

  /**
   * Handles and retrieves the Member Types page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "membertype", method = RequestMethod.GET)
  public String getMaintainMemberTypesPage() {
    LOGGER.info("Received request to show member types page.");

    return "maintain/membertype";
  }

  /**
   * Handles and retrieves the User Home / Listing page.
   *
   * @return The location and name of the page template.
   */
  @RequestMapping(value = "users", method = RequestMethod.GET)
  public String getUsersPage() {
    LOGGER.info("Received request to show users listing page");

    return "maintain/user";
  }
}
