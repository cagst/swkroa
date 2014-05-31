package com.cagst.swkroa.report;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Defines services for the reporting system.
 *
 * @author Craig Gaskill
 *
 * @version 1.0.0
 */
public interface ReportRepository {
	public List<JsonNode> getMembershipListingReport(final String membershipType);

}
