package com.cagst.swkroa.controller.web;

import com.cagst.swkroa.codevalue.CodeValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles and retrieves the Report pages depending on the URI template.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "/report")
public final class ReportController {
  private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

  @Autowired
  private CodeValueRepository codeValueRepo;

  /**
   * Handles and retrieves the Membership Listing report page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "/membership/listing", method = RequestMethod.GET)
  public ModelAndView getMembershipListingReport() {
    LOGGER.info("Received request to show Membership Listing report options.");

    ModelAndView mav = new ModelAndView("report/membership/listing");

    return mav;
  }

  /**
   * Handles the request to run/generate the Membership Listing report page.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/membership/listing", method = RequestMethod.POST)
  public ModelAndView runMembershipListingReport() {
    LOGGER.info("Received request to run Membership Listing report.");

    return new ModelAndView("membershipListingReport");
  }

  /**
   * Handles and retrieves the Membershp Past Due report page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "/membership/pastdue", method = RequestMethod.GET)
  public ModelAndView getMembershipPastDueReport() {
    LOGGER.info("Received request to show Membership Past Due report options.");

    ModelAndView mav = new ModelAndView("report/membership/pastdue");

    return mav;
  }

  /**
   * Handles the request to run/generate the Membership Past Due report page.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/membership/pastdue", method = RequestMethod.POST)
  public ModelAndView runMembershipPastDueReport() {
    LOGGER.info("Received request to run Membership Listing report.");

    return new ModelAndView("membershipPastDueReport");
  }
}
