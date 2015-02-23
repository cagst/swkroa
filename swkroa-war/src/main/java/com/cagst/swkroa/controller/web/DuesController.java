package com.cagst.swkroa.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles and retrieves the Billing page(s) depending on the URI template.
 *
 * @author Craig Gaskill
 */
@Controller
public final class DuesController {
  private static final Logger LOGGER = LoggerFactory.getLogger(DuesController.class);

  /**
   * Handles and retrieves the Dues page.
   *
   * @return The location and name of the page template.
   */
  @RequestMapping(value = "/accounting/dues", method = RequestMethod.GET)
  public String getDuesPage() {
    LOGGER.info("Received request to show dues page");

    return "accounting/dues";
  }

  /**
   * Handles and retrieves the Renew Dues page.
   *
   * @return The location and name of the page template.
   */
  @RequestMapping(value = "/accounting/dues/renew", method = RequestMethod.GET)
  public String getRenewDuesPage() {
    LOGGER.info("Received request to show dues renew page");

    return "accounting/dues_renew";
  }
}
