package com.cagst.swkroa.controller.web;

import com.cagst.swkroa.report.ReportGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles and retrieves the Report pages depending on the URI template.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
@Controller
@RequestMapping(value = "/report")
public final class ReportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

//	@Autowired
//	private ReportGenerator reportGenerator;

	/**
	 * Handles and retrieves the Membership Listing report page.
	 *
	 * @return The name of the page.
	 */
	@RequestMapping(value = "/membership_listing", method = RequestMethod.GET)
	public String getMembershipListingReport(final ModelMap modelMap) {
		LOGGER.info("Received request to show Membership Listing report.");

//		modelMap.addAttribute("format", "html");
//		modelMap.addAttribute("report", "MembershipListingReport.jasper");

//		reportGenerator.generateMembershipListingReport();
//		return "report/membership_listing";
		return "htmlMembershipListingReport";
	}
}
