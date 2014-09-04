package com.cagst.swkroa.controller.web;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  @RequestMapping(value = "/membership_listing", method = RequestMethod.GET)
  public ModelAndView getMembershipListingReport() {
    LOGGER.info("Received request to show Membership Listing report options.");

    List<CodeValue> types = codeValueRepo.getCodeValuesForCodeSetByMeaning(CodeValueRepository.MEMBERSHIP_TYPE);
    Collections.sort(types);

    ModelAndView mav = new ModelAndView("report/membership_listing");
    mav.addObject("membershipTypes", types);

    return mav;
  }

  @RequestMapping(value = "/membership_listing", method = RequestMethod.POST)
  public ModelAndView runMembershipListingReport() {
    LOGGER.info("Received request to run Membership Listing report.");

    return new ModelAndView("membershipListingReport");
  }
}
