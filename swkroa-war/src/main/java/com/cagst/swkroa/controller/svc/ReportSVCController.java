package com.cagst.swkroa.controller.svc;

import com.cagst.swkroa.report.ReportRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles and retrieves JSON objects depending on the URI template.
 *
 * @author Craig Gaskill
 *
 * @version 1.0.0
 */
@Controller
public class ReportSVCController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportSVCController.class);

	@Autowired
	private ReportRepository reportService;

	/**
	 * Handles the request and retrieves a JSON string representing the Membership Listing report.
	 *
	 * @return A JSON representation of the Membership Listing report.
	 */
	@RequestMapping(value = "/svc/report/membershiplisting/{membershipType}", method = RequestMethod.GET)
	@ResponseBody
	public List<JsonNode> getMembershipListingReport(final @PathVariable String membershipType) {
		LOGGER.info("Received request to retrieve the Membership Listing report for [{}]", membershipType);

		return reportService.getMembershipListingReport(membershipType);
	}
}
