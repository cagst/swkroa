package com.cagst.swkroa.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	/**
	 * Handles and retrieves the Report List page.
	 * 
	 * @return The name of the page.
	 */
	@RequestMapping(value = "/listing", method = RequestMethod.GET)
	public String getReportListingPage() {
		logger.info("Received request to show report home page.");

		return "report/listing";
	}

}
