package com.cagst.swkroa.member;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cagst.swkroa.user.User;
import com.cagst.swkroa.util.DateTimeConverter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Used to marshal/unmarshal a {@link MemberType} to/from the database.
 *
 * @author Craig Gaskill
 */
/* package */class MemberTypeMapper implements RowMapper<MemberType> {
  private static final String MEMBER_TYPE_UID      = "member_type_id";
  private static final String MEMBER_TYPE_PREV_UID = "prev_member_type_id";
  private static final String MEMBER_TYPE_DISPLAY  = "member_type_display";
  private static final String MEMBER_TYPE_MEANING  = "member_type_meaning";
  private static final String DUES_AMOUNT          = "dues_amount";
  private static final String PRIMARY_IND          = "primary_ind";
  private static final String ALLOW_SPOUSE_IND     = "allow_spouse_ind";
  private static final String ALLOW_MEMBER_IND     = "allow_member_ind";
  private static final String BEG_EFF_DT           = "beg_eff_dt";
  private static final String END_EFF_DT           = "end_eff_dt";

  // meta-data
  private static final String ACTIVE_IND = "active_ind";
  private static final String CREATE_ID  = "create_id";
  private static final String UPDT_ID    = "updt_id";
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
    type.setPrimary(rs.getBoolean(PRIMARY_IND));
    type.setAllowSpouse(rs.getBoolean(ALLOW_SPOUSE_IND));
    type.setAllowMember(rs.getBoolean(ALLOW_MEMBER_IND));
    type.setBeginEffectiveDate(DateTimeConverter.convert(rs.getTimestamp(BEG_EFF_DT)));
    type.setEndEffectiveDate(DateTimeConverter.convert(rs.getTimestamp(END_EFF_DT)));
    type.setActive(rs.getBoolean(ACTIVE_IND));
    type.setMemberTypeUpdateCount(rs.getLong(MEMBER_TYPE_UPDT_CNT));

    return type;
  }

  public static MapSqlParameterSource mapInsertStatement(final MemberType memberType, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();

    params.addValue(MEMBER_TYPE_PREV_UID, memberType.getPreviousMemberTypeUID());
    params.addValue(MEMBER_TYPE_DISPLAY, memberType.getMemberTypeDisplay());
    params.addValue(MEMBER_TYPE_MEANING, memberType.getMemberTypeMeaning());
    params.addValue(DUES_AMOUNT, memberType.getDuesAmount());
    params.addValue(PRIMARY_IND, memberType.isPrimary());
    params.addValue(ALLOW_SPOUSE_IND, memberType.isAllowSpouse());
    params.addValue(ALLOW_MEMBER_IND, memberType.isAllowMember());
    params.addValue(BEG_EFF_DT, memberType.getBeginEffectiveDate().toDate());
    params.addValue(END_EFF_DT, memberType.getEndEffectiveDate() != null ? memberType.getEndEffectiveDate().toDate() : null);

    params.addValue(ACTIVE_IND, memberType.isActive());
    params.addValue(CREATE_ID, user.getUserUID());
    params.addValue(UPDT_ID, user.getUserUID());

    return params;
  }

  public static MapSqlParameterSource mapUpdateStatement(final MemberType memberType, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();

    params.addValue(MEMBER_TYPE_DISPLAY, memberType.getMemberTypeDisplay());
    params.addValue(MEMBER_TYPE_MEANING, memberType.getMemberTypeMeaning());
    params.addValue(DUES_AMOUNT, memberType.getDuesAmount());
    params.addValue(PRIMARY_IND, memberType.isPrimary());
    params.addValue(ALLOW_SPOUSE_IND, memberType.isAllowSpouse());
    params.addValue(ALLOW_MEMBER_IND, memberType.isAllowMember());
    params.addValue(BEG_EFF_DT, memberType.getBeginEffectiveDate().toDate());
    params.addValue(END_EFF_DT, memberType.getEndEffectiveDate() != null ? memberType.getEndEffectiveDate().toDate() : null);
    params.addValue(ACTIVE_IND, memberType.isActive());
    params.addValue(UPDT_ID, user.getUserUID());

    params.addValue(MEMBER_TYPE_UID, memberType.getMemberTypeUID());
    params.addValue(MEMBER_TYPE_UPDT_CNT, memberType.getMemberTypeUpdateCount());

    return params;
  }
}
