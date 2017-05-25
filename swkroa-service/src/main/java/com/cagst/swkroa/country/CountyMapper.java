package com.cagst.swkroa.country;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a row in the result set into a {@link County} object. Used to marshal / un-marshal a
 * {@link County} to / from the database.
 *
 * @author Craig Gaskill
 */
/* package */ class CountyMapper implements RowMapper<County> {
  private static final String COUNTY_ID = "county_id";
  private static final String STATE_CODE = "state_code";
  private static final String COUNTY_CODE = "county_code";
  private static final String COUNTY_NAME = "county_name";
  private static final String SWKROA_COUNTY_IND = "swkroa_county_ind";
  private static final String ACTIVE_IND = "active_ind";
  private static final String UPDT_CNT = "county_updt_cnt";

  @Override
  public County mapRow(ResultSet rs, int rowNum) throws SQLException {
    return County.builder()
        .setCountyUID(rs.getLong(COUNTY_ID))
        .setStateCode(rs.getString(STATE_CODE))
        .setCountyCode(rs.getString(COUNTY_CODE))
        .setCountyName(rs.getString(COUNTY_NAME))
        .setSwkroaCounty(rs.getBoolean(SWKROA_COUNTY_IND))
        .setActive(rs.getBoolean(ACTIVE_IND))
        .setCountyUpdateCount(rs.getLong(UPDT_CNT))
        .build();
  }
}
