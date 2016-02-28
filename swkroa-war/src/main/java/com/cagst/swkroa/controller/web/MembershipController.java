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
 */
@Controller
public final class MembershipController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MembershipController.class);

  /**
   * Handles and retrieves the Membership Home / Listing page.
   *
   * @return The location and name of the page template.
   */
  @RequestMapping(value = "/memberships", method = RequestMethod.GET)
  public String getMembershipListingPage() {
    LOGGER.info("Received request to show membership listing page.");

    return "membership/home";
  }
}
