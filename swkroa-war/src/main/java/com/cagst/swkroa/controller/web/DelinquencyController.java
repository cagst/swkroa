package com.cagst.swkroa.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles and retrieves the Delinquency page(s) depending on the URI template.
 *
 * @author Craig Gaskill
 */
@Controller
public final class DelinquencyController {
  private static final Logger LOGGER = LoggerFactory.getLogger(DelinquencyController.class);

  /**
   * Handles and retrieves the Delinquencies page.
   *
   * @return The location and name fo the page template.
   */
  @RequestMapping(value = "/accounting/delinquencies", method = RequestMethod.GET)
  public String getDelinquenciesPage() {
    LOGGER.info("Received request to show delinquencies page");

    return "accounting/delinquency";
  }
}
