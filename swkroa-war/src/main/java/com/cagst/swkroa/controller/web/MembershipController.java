package com.cagst.swkroa.controller.web;

import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.member.MembershipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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

//    return "membership/home";
    return "membership2/listing";
  }

  /**
   * Handles and retrieves the Add Membership page.
   *
   * @return The location and name of the page template.
   */
//  @RequestMapping(value = "/memberships/add", method = RequestMethod.GET)
//  public ModelAndView getMembershipAddPage() {
//    LOGGER.info("Received request to show add membership page.");
//
//    ModelAndView mav = new ModelAndView("membership/modify");
//    mav.addObject("membershipId", 0L);
//
//    return mav;
//  }

  /**
   * Handles and retrieves the Edit Membership page.
   *
   * @param membershipId
   *     A {@link long} that uniquely identifies the {@link Membership} to retrieve.
   *
   * @return The location and name of the page template.
   */
//  @RequestMapping(value = "/memberships/edit/{membershipId}", method = RequestMethod.GET)
//  public ModelAndView getMembershipEditPage(final @PathVariable long membershipId) {
//    LOGGER.info("Received request to show edit membership page.");
//
//    ModelAndView mav = new ModelAndView("membership/modify");
//    mav.addObject("membershipId", membershipId);
//
//    return mav;
//  }

  /**
   * Handles and retrieves the Membership Details page.
   *
   * @param membershipId
   *     A {@link long} that uniquely identifies the {@link Membership} to retrieve.
   *
   * @return The location and name of the page template.
   */
  @RequestMapping(value = "/memberships/{membershipId}", method = RequestMethod.GET)
  public ModelAndView getMembershipDetailsPage(final @PathVariable long membershipId) {
    LOGGER.info("Received request to show edit membership page.");

    ModelAndView mav = new ModelAndView("membership2/details");
    mav.addObject("membershipId", membershipId);

    return mav;
  }
}
