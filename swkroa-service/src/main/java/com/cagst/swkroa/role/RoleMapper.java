package com.cagst.swkroa.role;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a row in the result set into a {@link Role} object. Used to marshal / un-marshal a
 * {@link Role} to / from the database.
 *
 * @author Craig Gaskill
 */
/* package */final class RoleMapper implements RowMapper<Role> {
  private static final String ROLE_ID       = "role_id";
  private static final String ROLE_NAME     = "role_name";
  private static final String ROLE_KEY      = "role_key";
  private static final String ROLE_UPDT_CNT = "role_updt_cnt";
  private static final String ACTIVE_IND    = "active_ind";

  @Override
  public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
    return Role.builder()
        .setRoleUID(rs.getLong(ROLE_ID))
        .setRoleName(rs.getString(ROLE_NAME))
        .setRoleKey(rs.getString(ROLE_KEY))
        .setActive(rs.getBoolean(ACTIVE_IND))
        .setRoleUpdateCount(rs.getLong(ROLE_UPDT_CNT))
        .build();
  }

}
