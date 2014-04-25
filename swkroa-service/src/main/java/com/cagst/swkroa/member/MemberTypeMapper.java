package com.cagst.swkroa.member;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.cagst.common.util.CGTDateTimeUtils;

/**
 * Used to marshal/unmarshal a {@link MemberType} to/from the database.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
/* package */class MemberTypeMapper implements RowMapper<MemberType> {
	private static final String MEMBER_TYPE_UID = "member_type_id";
	private static final String MEMBER_TYPE_PREV_UID = "prev_member_type_id";
	private static final String MEMBER_TYPE_DISPLAY = "member_type_display";
	private static final String MEMBER_TYPE_MEANING = "member_type_meaning";
	private static final String DUES_AMOUNT = "dues_amount";
	private static final String BEG_EFF_DT_TM = "beg_eff_dt_tm";
	private static final String END_EFF_DT_TM = "end_eff_dt_tm";
	private static final String ACTIVE_IND = "active_ind";
	private static final String MEMBER_TYPE_UPDT_CNT = "member_type_updt_cnt";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public MemberType mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		MemberType type = new MemberType();
		type.setMemberTypeUID(rs.getLong(MEMBER_TYPE_UID));
		type.setPreviousMemberTypeUID(rs.getLong(MEMBER_TYPE_PREV_UID));
		type.setMemberTypeDisplay(rs.getString(MEMBER_TYPE_DISPLAY));
		type.setMemberTypeMeaning(rs.getString(MEMBER_TYPE_MEANING));
		type.setDuesAmount(rs.getBigDecimal(DUES_AMOUNT));
		type.setBeginEffectiveDateTime(CGTDateTimeUtils.getUTCDateTime(rs, BEG_EFF_DT_TM));
		type.setEndEffectiveDateTime(CGTDateTimeUtils.getUTCDateTime(rs, END_EFF_DT_TM));
		type.setActive(rs.getBoolean(ACTIVE_IND));
		type.setMemberTypeUpdateCount(rs.getLong(MEMBER_TYPE_UPDT_CNT));

		return type;
	}
}
