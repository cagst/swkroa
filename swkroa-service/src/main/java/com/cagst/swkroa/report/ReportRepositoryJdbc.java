package com.cagst.swkroa.report;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC Template implementation of the {@link ReportRepository} interface.
 *
 * @author Craig Gaskill
 *
 * @version 1.0.0
 *
 */
@Repository("reportRepo")
/* package */ class ReportRepositoryJdbc extends BaseRepositoryJdbc implements ReportRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportRepositoryJdbc.class);

	private static final String MEMBERSHIP_LISTING_ALL  = "MEMBERSHIP_LISTING_ALL";
	private static final String MEMBERSHIP_LISTING_TYPE = "MEMBERSHIP_LISTING_TYPE";

	/**
	 * Primary Constructor used to create an instance of <i>ReportServiceJdbc</i>.
	 *
	 * @param dataSource The {@link DataSource} to use to retrieve information for the various reports.
	 */
	public ReportRepositoryJdbc(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public List<JsonNode> getMembershipListingReport(final String membershipType) {
		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

		if (StringUtils.equals(membershipType, "ALL")) {
			return getJdbcTemplate().getJdbcOperations().query(stmtLoader.load(MEMBERSHIP_LISTING_ALL), new JsonNodeRowMapper());
		} else {
			Map<String, String> params = new HashMap<String, String>();
			params.put("membership_type", membershipType);

			return getJdbcTemplate().query(stmtLoader.load(MEMBERSHIP_LISTING_TYPE), params, new JsonNodeRowMapper());
		}
	}
}
