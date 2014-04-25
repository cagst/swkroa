package com.cagst.swkroa.role;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a row in the resultset into a {@link Role} object. Used to marshal / unmarshal a
 * {@link Role} to / from the database.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
/* package */final class RoleMapper implements RowMapper<Role> {
	private static final String ROLE_ID = "role_id";
	private static final String ROLE_KEY = "role_key";
	private static final String ROLE_NAME = "role_name";
	private static final String ROLE_UPDT_CNT = "role_updt_cnt";
	private static final String ACTIVE_IND = "active_ind";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public Role mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		Role role = new Role();
		role.setRoleUID(rs.getLong(ROLE_ID));
		role.setRoleKey(rs.getString(ROLE_KEY));
		role.setRoleName(rs.getString(ROLE_NAME));
		role.setRoleUpdateCount(rs.getLong(ROLE_UPDT_CNT));
		role.setActive(rs.getBoolean(ACTIVE_IND));

		return role;
	}

}
