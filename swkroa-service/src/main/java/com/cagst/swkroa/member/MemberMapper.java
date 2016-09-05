package com.cagst.swkroa.member;

import com.cagst.common.util.CGTDateTimeUtils;
import com.cagst.common.util.CGTStringUtils;
import com.cagst.swkroa.person.Person;
import com.cagst.swkroa.person.PersonRepository;
import com.cagst.swkroa.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Used to marshal/un-marshal a {@link Member} to/from the database.
 *
 * @author Craig Gaskill
 */
/* package */final class MemberMapper implements RowMapper<Member> {
  private static final String MEMBER_ID            = "member_id";
  private static final String MEMBER_NAME          = "member_name";
  private static final String MEMBERSHIP_ID        = "membership_id";
  private static final String PERSON_ID            = "person_id";
  private static final String COMPANY_NAME         = "company_name";
  private static final String COMPANY_NAME_KEY     = "company_name_key";
  private static final String OWNER_IDENT          = "owner_ident";
  private static final String MEMBER_TYPE          = "member_type_id";
  private static final String GREETING             = "greeting";
  private static final String IN_CARE_OF           = "in_care_of";
  private static final String JOIN_DT              = "join_dt";
  private static final String MAIL_NEWSLETTER_IND  = "mail_newsletter_ind";
  private static final String EMAIL_NEWSLETTER_IND = "email_newsletter_ind";
  private static final String CLOSE_REASON_ID      = "close_reason_id";
  private static final String CLOSE_REASON_TXT     = "close_reason_txt";
  private static final String CLOSE_DT_TM          = "close_dt_tm";

  // meta-data
  private static final String ACTIVE_IND      = "active_ind";
  private static final String CREATE_ID       = "create_id";
  private static final String UPDT_ID         = "updt_id";
  private static final String MEMBER_UPDT_CNT = "member_updt_cnt";

  private final PersonRepository personRepo;
  private final MemberTypeRepository memberTypeRepo;

  /**
   * Primary Constructor used to create an instance of <i>MemberMapper</i>.
   *
   * @param personRepo
   *     The {@link PersonRepository} to use to retrieve the {@link Person} associated to with this Member.
   * @param memberTypeRepo
   *     The {@link MemberTypeRepository} to use to retrieve the {@link MemberType} associated with the Members.
   */
  public MemberMapper(final PersonRepository personRepo, final MemberTypeRepository memberTypeRepo) {
    this.personRepo = personRepo;
    this.memberTypeRepo = memberTypeRepo;
  }

  @Override
  public Member mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    Member member = new Member();

    long personID = rs.getLong(PERSON_ID);
    if (personID > 0L) {
      member.setPerson(personRepo.getPersonByUID(personID));
    }

    member.setMemberUID(rs.getLong(MEMBER_ID));
    member.setMemberName(rs.getString(MEMBER_NAME));
    member.setMembershipUID(rs.getLong(MEMBERSHIP_ID));
    member.setCompanyName(rs.getString(COMPANY_NAME));
    member.setOwnerIdent(rs.getString(OWNER_IDENT));
    member.setMemberType(memberTypeRepo.getMemberTypeByUID(rs.getLong(MEMBER_TYPE)));
    member.setGreeting(rs.getString(GREETING));
    member.setInCareOf(rs.getString(IN_CARE_OF));
    member.setJoinDate(CGTDateTimeUtils.getDateTime(rs, JOIN_DT));
    member.setMailNewsletter(rs.getBoolean(MAIL_NEWSLETTER_IND));
    member.setEmailNewsletter(rs.getBoolean(EMAIL_NEWSLETTER_IND));
    member.setCloseReasonUID(rs.getLong(CLOSE_REASON_ID));
    member.setCloseReasonText(rs.getString(CLOSE_REASON_TXT));
    member.setCloseDate(CGTDateTimeUtils.getDateTime(rs, CLOSE_REASON_TXT));

    // meta-data
    member.setMemberUpdateCount(rs.getLong(MEMBER_UPDT_CNT));
    member.setActive(rs.getBoolean(ACTIVE_IND));

    return member;
  }

  /**
   * Will marshal a {@link Member} into a {@link MapSqlParameterSource} for inserting into the database.
   *
   * @param member
   *     The {@link Member} to map into an insert statement.
   * @param membership
   *     The {@link Membership} this Member is associated with.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapInsertStatement(final Member member,
                                                         final Membership membership,
                                                         final User user) {

    MapSqlParameterSource params = new MapSqlParameterSource();

    mapCommonProperties(params, member, membership, user);
    params.addValue(CREATE_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link Member} into a {@link MapSqlParameterSource} for updating into the database.
   *
   * @param member
   *     The {@link Member} to map into an update statement.
   * @param membership
   *     The {@link Membership} this Member is associated with.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(final Member member,
                                                         final Membership membership,
                                                         final User user) {

    MapSqlParameterSource params = new MapSqlParameterSource();

    mapCommonProperties(params, member, membership, user);
    params.addValue(MEMBER_ID, member.getMemberUID());
    params.addValue(MEMBER_UPDT_CNT, member.getMemberUpdateCount());

    return params;
  }

  private static void mapCommonProperties(final MapSqlParameterSource params,
                                          final Member member,
                                          final Membership membership,
                                          final User user) {

    params.addValue(PERSON_ID, member.getPerson() != null ? member.getPerson().getPersonUID() : null);
    params.addValue(MEMBERSHIP_ID, membership.getMembershipUID());
    params.addValue(COMPANY_NAME, StringUtils.stripToNull(member.getCompanyName()));
    params.addValue(COMPANY_NAME_KEY, StringUtils.stripToNull(CGTStringUtils.normalizeToKey(member.getCompanyName())));
    params.addValue(MEMBER_TYPE, member.getMemberType().getMemberTypeUID());
    params.addValue(GREETING, StringUtils.stripToNull(member.getGreeting()));
    params.addValue(IN_CARE_OF, StringUtils.stripToNull(member.getInCareOf()));
    params.addValue(OWNER_IDENT, member.getOwnerIdent());
    params.addValue(JOIN_DT, CGTDateTimeUtils.convertDateTimeToTimestamp(member.getJoinDate()));
    params.addValue(MAIL_NEWSLETTER_IND, member.isMailNewsletter());
    params.addValue(EMAIL_NEWSLETTER_IND, member.isEmailNewsletter());
    params.addValue(ACTIVE_IND, member.isActive());
    params.addValue(UPDT_ID, user.getUserUID());
  }
}
