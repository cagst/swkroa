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
 * 
 * @version 1.0.0
 * 
 */
@Controller
public final class CodeSetController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CodeSetController.class);

	/**
	 * Handles and retrieves the CodeSet Listing JSP page.
	 * 
	 * @return The name of the page.
	 */
	@RequestMapping(value = "/maintain/codesets", method = RequestMethod.GET)
	public String getMaintainCodeSetListingPage() {
		LOGGER.info("Received request to show codeset listing page.");

		return "maintain/codeset";
	}
}
