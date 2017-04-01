package com.cagst.swkroa.system;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a row in the result set into a {@link SystemSetting} object. Used to marshal / un-marshal a
 * {@link SystemSetting} to / from the database.
 */
/* package */ final class SystemSettingMapper implements RowMapper<SystemSetting> {
  private static final String SYSTEM_KEY   = "system_key";
  private static final String SYSTEM_NAME  = "system_name";
  private static final String SYSTEM_VALUE = "system_value";

  @Override
  public SystemSetting mapRow(ResultSet rs, int rowNum) throws SQLException {
    return SystemSetting.builder()
        .setSystemKey(rs.getString(SYSTEM_KEY))
        .setSystemName(rs.getString(SYSTEM_NAME))
        .setSystemValue(rs.getString(SYSTEM_VALUE))
        .build();
  }
}
