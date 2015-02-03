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
public final class BillingController {
  private static final Logger LOGGER = LoggerFactory.getLogger(BillingController.class);

  /**
   * Handles and retrieves the Billing page.
   *
   * @return The location and name fo the page template.
   */
  @RequestMapping(value = "/accounting/billing", method = RequestMethod.GET)
  public String getBillingPage() {
    LOGGER.info("Received request to show billing page");

    return "accounting/billing";
  }
}
