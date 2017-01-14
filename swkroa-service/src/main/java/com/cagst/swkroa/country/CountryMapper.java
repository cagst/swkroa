package com.cagst.swkroa.country;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a row in the result set into a {@link Country} object. Used to marshal / un-marshal a {@link Country}
 * to / from the database.
 *
 * @author Craig Gaskill
 */
/* package */ class CountryMapper implements RowMapper<Country> {
  private static final String COUNTRY_CODE = "country_code";
  private static final String COUNTRY_NAME = "country_name";
  private static final String ACTIVE_IND = "active_ind";
  private static final String UPDT_CNT = "updt_cnt";

  @Override
  public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
    return Country.builder()
        .setCountryCode(rs.getString(COUNTRY_CODE))
        .setCountryName(rs.getString(COUNTRY_NAME))
        .setActive(rs.getBoolean(ACTIVE_IND))
        .setCountryUpdateCount(rs.getLong(UPDT_CNT))
        .build();
  }
}
