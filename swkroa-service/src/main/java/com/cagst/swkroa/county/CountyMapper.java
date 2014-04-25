package com.cagst.swkroa.county;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a row in the resultset into a {@link County} object. Used to marshal / unmarshal a
 * {@link County} to / from the database.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
/* package */final class CountyMapper implements RowMapper<County> {
	private static final String COUNTY_ID = "county_id";
	private static final String STATE_CODE = "state_code";
	private static final String COUNTY_CODE = "county_code";
	private static final String COUNTY_NAME = "county_name";
	private static final String SWKROA_COUNTY_IND = "swkroa_county_ind";
	private static final String ACTIVE_IND = "active_ind";
	private static final String UPDT_CNT = "county_updt_cnt";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public County mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		County county = new County();
		county.setCountyUID(rs.getLong(COUNTY_ID));
		county.setState(rs.getString(STATE_CODE));
		county.setCountyCode(rs.getString(COUNTY_CODE));
		county.setCountyName(rs.getString(COUNTY_NAME));
		county.setSwkroaCounty(rs.getBoolean(SWKROA_COUNTY_IND));
		county.setActive(rs.getBoolean(ACTIVE_IND));
		county.setCountyUpdateCount(rs.getLong(UPDT_CNT));

		return county;
	}
}
