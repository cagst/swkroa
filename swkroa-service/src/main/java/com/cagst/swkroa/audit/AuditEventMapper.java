package com.cagst.swkroa.audit;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.cagst.common.util.CGTDateTimeUtils;

/**
 * Used to marshal/unmarshal a {@link AuditEvent} to/from the database.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
/* package */final class AuditEventMapper implements RowMapper<AuditEvent> {
	private static final String AUDIT_EVENT_TYPE = "audit_event_type";
	private static final String AUDIT_ACTION = "audit_action";
	private static final String AUDIT_INSTIGATOR = "audit_instigator";
	private static final String AUDIT_MESSAGE = "audit_message";
	private static final String CREATE_DT_TM = "create_dt_tm";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public AuditEvent mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		AuditEventType eventType = AuditEventType.values()[rs.getInt(AUDIT_EVENT_TYPE)];
		String action = rs.getString(AUDIT_ACTION);
		String instigator = rs.getString(AUDIT_INSTIGATOR);
		String message = rs.getString(AUDIT_MESSAGE);
		DateTime createDtTm = CGTDateTimeUtils.getUTCDateTime(rs, CREATE_DT_TM);

		return new AuditEvent(eventType, action, instigator, message, createDtTm);
	}

	/**
	 * Will marshal a {@link AuditEvent} into a {@link MapSqlParameterSource} for inserting into the
	 * database.
	 * 
	 * @param auditEvent
	 *          The {@link AuditEvent} to marshal.
	 * 
	 * @return A {@link MapSqlParameterSource} that can be used in a <code>jdbcTemplate.update</code>
	 *         statement.
	 */
	public static MapSqlParameterSource mapInsertStatement(final AuditEvent auditEvent) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(AUDIT_EVENT_TYPE, auditEvent.getAuditEventType().ordinal());
		params.addValue(AUDIT_ACTION, auditEvent.getAuditAction());
		params.addValue(AUDIT_INSTIGATOR, auditEvent.getAuditInstigator());
		params.addValue(AUDIT_MESSAGE, StringUtils.left(auditEvent.getAuditMessage(), 250));

		return params;
	}
}
