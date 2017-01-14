package com.cagst.swkroa.country;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a row in the result set into a {@link State} object. Used to marshal / unmarshal a {@link State}
 * to / from the database.
 *
 * @author Craig Gaskill
 */
/* package */ class StateMapper implements RowMapper<State> {
  private static final String COUNTRY_CODE = "country_code";
  private static final String STATE_CODE = "state_code";
  private static final String STATE_FIPS = "state_fips";
  private static final String STATE_NAME = "state_name";
  private static final String ACTIVE_IND = "active_ind";
  private static final String UPDT_CNT = "updt_cnt";

  @Override
  public State mapRow(ResultSet rs, int rowNum) throws SQLException {
    return State.builder()
        .setCountryCode(rs.getString(COUNTRY_CODE))
        .setStateCode(rs.getString(STATE_CODE))
        .setFipsCode(rs.getString(STATE_FIPS))
        .setStateName(rs.getString(STATE_NAME))
        .setActive(rs.getBoolean(ACTIVE_IND))
        .setStateUpdateCount(rs.getLong(UPDT_CNT))
        .build();
  }
}
