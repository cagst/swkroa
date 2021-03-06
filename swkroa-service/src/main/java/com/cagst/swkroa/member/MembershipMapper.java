package com.cagst.swkroa.member;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.util.DateTimeConverter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Used to marshal/unmarshal a {@link Membership} to/from the database.
 *
 * @author Craig Gaskill
 */
/* package */final class MembershipMapper implements RowMapper<Membership> {
  private static final String MEMBERSHIP_ID           = "membership_id";
  private static final String MEMBERSHIP_NAME         = "membership_name";
  private static final String MEMBER_ID               = "member_id";
  private static final String ENTITY_TYPE_CD          = "entity_type_cd";
  private static final String MEMBER_TYPE_ID          = "member_type_id";
  private static final String NEXT_DUE_DT             = "next_due_dt";
  private static final String JOIN_DT                 = "join_dt";
  private static final String CALCULATED_DUES         = "calculated_dues";
  private static final String INCREMENTAL_DUES        = "incremental_dues";
  private static final String BALANCE                 = "balance";
  private static final String LAST_PAYMENT_DT         = "last_payment_dt";
  private static final String CLOSE_REASON_ID         = "close_reason_id";
  private static final String CLOSE_REASON_TXT        = "close_reason_txt";
  private static final String CLOSE_DT_TM             = "close_dt_tm";

  // meta-data
  private static final String ACTIVE_IND          = "active_ind";
  private static final String CREATE_ID           = "create_id";
  private static final String UPDT_ID             = "updt_id";
  private static final String MEMBERSHIP_UPDT_CNT = "membership_updt_cnt";

  private final CodeValueRepository codeValueRepo;
  private final MemberTypeRepository memberTypeRepo;

  /**
   * Primary Constructor used to create an instance of <i>MembershipMapper</i>.
   *
   * @param codeValueRepo
   *    The {@link CodeValueRepository} to use to retrieve additional membership values.
   * @param memberTypeRepo
   *    The {@link MemberTypeRepository} to use to retrieve member type information.
   */
  public MembershipMapper(CodeValueRepository codeValueRepo, MemberTypeRepository memberTypeRepo) {
    this.codeValueRepo = codeValueRepo;
    this.memberTypeRepo = memberTypeRepo;
  }

  @Override
  public Membership mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    Membership membership = new Membership();

    membership.setMembershipUID(rs.getLong(MEMBERSHIP_ID));
    membership.setMembershipName(rs.getString(MEMBERSHIP_NAME));
    membership.setMemberUID(rs.getLong(MEMBER_ID));
    membership.setEntityType(codeValueRepo.getCodeValueByUID(rs.getLong(ENTITY_TYPE_CD)));
    membership.setMemberType(memberTypeRepo.getMemberTypeByUID(rs.getLong(MEMBER_TYPE_ID)));
    membership.setNextDueDate(DateTimeConverter.convert(rs.getTimestamp(NEXT_DUE_DT)));
    membership.setJoinDate(DateTimeConverter.convert(rs.getTimestamp(JOIN_DT)));

    membership.setCalculatedDuesAmount(rs.getBigDecimal(CALCULATED_DUES));
    membership.setIncrementalDues(rs.getBigDecimal(INCREMENTAL_DUES));

    BigDecimal balance = rs.getBigDecimal(BALANCE);
    if (balance != null) {
      membership.setBalance(balance);
    } else {
      membership.setBalance(new BigDecimal(0.0));
    }
    membership.setLastPaymentDate(DateTimeConverter.convert(rs.getTimestamp(LAST_PAYMENT_DT)));
    membership.setCloseReasonUID(rs.getLong(CLOSE_REASON_ID));
    membership.setCloseReasonText(rs.getString(CLOSE_REASON_TXT));
    membership.setCloseDate(DateTimeConverter.convert(rs.getTimestamp(CLOSE_DT_TM)));
    membership.setMembershipUpdateCount(rs.getLong(MEMBERSHIP_UPDT_CNT));
    membership.setActive(rs.getBoolean(ACTIVE_IND));

    return membership;
  }

  /**
   * Will marshal a {@link Membership} into a {@link MapSqlParameterSource} for inserting into the database.
   *
   * @param membership
   *     The {@link Membership} to map into an insert statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapInsertStatement(final Membership membership, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();

    mapCommonProperties(params, membership, user);
    params.addValue(CREATE_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link Membership} into a {@link MapSqlParameterSource} for updating into the database.
   *
   * @param membership
   *     The {@link Membership} to map into an update statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(final Membership membership, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();

    mapCommonProperties(params, membership, user);
    params.addValue(MEMBERSHIP_ID, membership.getMembershipUID());
    params.addValue(MEMBERSHIP_UPDT_CNT, membership.getMembershipUpdateCount());

    return params;
  }

  private static void mapCommonProperties(final MapSqlParameterSource params,
                                          final Membership membership,
                                          final User user) {

    params.addValue(NEXT_DUE_DT, DateTimeConverter.convert(membership.getNextDueDate()));
    params.addValue(ENTITY_TYPE_CD, membership.getEntityType().getCodeValueUID());
    params.addValue(INCREMENTAL_DUES, membership.getIncrementalDues());
    params.addValue(ACTIVE_IND, membership.isActive());
    params.addValue(CLOSE_REASON_ID, membership.getCloseReasonUID() > 0 ? membership.getCloseReasonUID() : null);
    params.addValue(CLOSE_REASON_TXT, membership.getCloseReasonText());
    params.addValue(CLOSE_DT_TM, DateTimeConverter.convert(membership.getCloseDate()));
    params.addValue(UPDT_ID, user.getUserUID());
  }
}
