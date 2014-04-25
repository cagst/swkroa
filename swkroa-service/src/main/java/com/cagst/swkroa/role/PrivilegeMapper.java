package com.cagst.swkroa.role;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a row in the resultset into a {@link Privilege} object. Used to marshal / unmarshall a
 * {@link Privilege} to / from the database.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
/* package */class PrivilegeMapper implements RowMapper<Privilege> {
	private static final String PRIVILEGE_ID = "privilege_id";
	private static final String PRIVILEGE_KEY = "privilege_key";
	private static final String PRIVILEGE_NAME = "privilege_name";
	private static final String PRIVILEGE_UPDT_CNT = "privilege_updt_cnt";
	private static final String ACTIVE_IND = "active_ind";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public Privilege mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		Privilege priv = new Privilege();
		priv.setPrivilegeUID(rs.getLong(PRIVILEGE_ID));
		priv.setPrivilegeKey(rs.getString(PRIVILEGE_KEY));
		priv.setPrivilegeName(rs.getString(PRIVILEGE_NAME));
		priv.setPrivilegeUpdateCount(rs.getLong(PRIVILEGE_UPDT_CNT));
		priv.setActive(rs.getBoolean(ACTIVE_IND));

		return priv;
	}
}
