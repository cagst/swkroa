package com.cagst.swkroa.member;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cagst.common.db.StatementLoader;
import com.cagst.common.util.CGTStringUtils;
import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.contact.Address;
import com.cagst.swkroa.contact.ContactRepository;
import com.cagst.swkroa.contact.EmailAddress;
import com.cagst.swkroa.contact.PhoneNumber;
import com.cagst.swkroa.county.CountyRepository;
import com.cagst.swkroa.person.PersonRepository;
import com.cagst.swkroa.person.PersonRepositoryJdbc;
import com.cagst.swkroa.user.User;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * JDBC Template implementation of the {@link MemberRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("memberRepo")
/* package */ final class MemberRepositoryJdbc extends PersonRepositoryJdbc implements MemberRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(MemberRepositoryJdbc.class);

  private static final String GET_MEMBERS_FOR_MEMBERSHIP = "GET_MEMBERS_FOR_MEMBERSHIP";
  private static final String GET_MEMBERS_BY_NAME        = "GET_MEMBERS_BY_NAME";
  private static final String GET_MEMBER_BY_UID          = "GET_MEMBER_BY_UID";

  private static final String GET_MEMBERSHIP_COUNTIES_FOR_MEMBERSHIP = "GET_MEMBERSHIP_COUNTIES_FOR_MEMBERSHIP";
  private static final String GET_MEMBERSHIP_COUNTY_BY_UID           = "GET_MEMBERSHIP_COUNTY_BY_UID";

  private static final String GET_PARTIAL_OWNERID_COUNT = "GET_PARTIAL_OWNERID_COUNT";

  private static final String INSERT_MEMBER = "INSERT_MEMBER";
  private static final String UPDATE_MEMBER = "UPDATE_MEMBER";
  private static final String CLOSE_MEMBER  = "CLOSE_MEMBER";

  private static final String INSERT_MEMBERSHIP_COUNTY = "INSERT_MEMBERSHIP_COUNTY";
  private static final String UPDATE_MEMBERSHIP_COUNTY = "UPDATE_MEMBERSHIP_COUNTY";

  private final PersonRepository personRepo;
  private final MemberTypeRepository memberTypeRepo;
  private final CountyRepository countyRepo;

  /**
   * Primary constructor used to create an instance of the MemberRepositoryJdbc.
   *
   * @param dataSource
   *     The {@link DataSource} to use to retrieve / persist data objects.
   * @param personRepo
   *     The {@link PersonRepository} to use to retrieve the Person attributes.
   * @param memberTypeRepo
   *     The {@link MemberTypeRepository} to use to populate the {@link MemberType} objects.
   * @param countyRepo
   *     The {@link CountyRepository} to use to populate the MembershipCounty objects.
   * @param contactRepo
   *     The {@link ContactRepository} to use to populate contact objects.
   */
  @Inject
  public MemberRepositoryJdbc(final DataSource dataSource, final PersonRepository personRepo,
                              final MemberTypeRepository memberTypeRepo,
                              final CountyRepository countyRepo, final ContactRepository contactRepo) {

    super(dataSource, contactRepo);

    this.personRepo = personRepo;
    this.memberTypeRepo = memberTypeRepo;
    this.countyRepo = countyRepo;
  }

  @Override
//  @Cacheable(value = "membersList", key = "#membership.getMembershipUID()")
  public List<Member> getMembersForMembership(final Membership membership) {
    Assert.notNull(membership, "Assertion Failed - argument [membership] cannot be null");

    LOGGER.info("Calling getMembersForMembership for [{}].", membership.getMembershipUID());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    Map<String, Long> params = new HashMap<String, Long>(1);
    params.put("membership_id", membership.getMembershipUID());

    return getJdbcTemplate().query(stmtLoader.load(GET_MEMBERS_FOR_MEMBERSHIP), params, new MemberMapper(personRepo, memberTypeRepo));
  }

  @Override
  public List<Member> getMembersByName(final String name, final MembershipStatus status) {
    LOGGER.info("Calling getMembersByName for [{}].", name);

    Assert.hasText(name, "Assertion Failture - argument [name] cannot be null or empty");
    Assert.notNull(status, "Assertion Failure - argument [status] cannot be null");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, String> params = new HashMap<String, String>(2);
    params.put("name", CGTStringUtils.normalizeToKey(name) + "%");
    params.put("status", status.toString());

    return getJdbcTemplate().query(stmtLoader.load(GET_MEMBERS_BY_NAME), params, new MemberMapper(personRepo, memberTypeRepo));

  }

  @Override
  public Member getMemberByUID(final long uid) throws IncorrectResultSizeDataAccessException {

    LOGGER.info("Calling getMemberByUID for [{}].", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<String, Long>(1);
    params.put("member_id", uid);

    List<Member> members = getJdbcTemplate().query(stmtLoader.load(GET_MEMBER_BY_UID), params,
        new MemberMapper(personRepo, memberTypeRepo));

    if (members.size() == 1) {
      return members.get(0);
    } else if (members.size() == 0) {
      LOGGER.warn("Member with UID of [{}] was not found.", uid);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.error("More than one Member with UID of [{}] was found.", uid);
      throw new IncorrectResultSizeDataAccessException(1, members.size());
    }
  }

  @Override
//  @Cacheable(value = "membershipCountiesList", key = "#membership.getMembershipUID()")
  public List<MembershipCounty> getMembershipCountiesForMembership(final Membership membership) {
    Assert.notNull(membership, "Assertion Failed - argument [membership] cannot be null");

    LOGGER.info("Calling getMembershipCountiesForMembership for [{}].", membership.getMembershipUID());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    Map<String, Long> params = new HashMap<String, Long>(1);
    params.put("membership_id", membership.getMembershipUID());

    return getJdbcTemplate().query(stmtLoader.load(GET_MEMBERSHIP_COUNTIES_FOR_MEMBERSHIP), params,
        new MembershipCountyMapper(countyRepo));
  }

  @Override
  public MembershipCounty getMembershipCountyByUID(final long uid) throws IncorrectResultSizeDataAccessException {

    LOGGER.info("Calling getMembershipCountyByUID for [{}].", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<String, Long>(1);
    params.put("membership_county_id", uid);

    List<MembershipCounty> counties = getJdbcTemplate().query(stmtLoader.load(GET_MEMBERSHIP_COUNTY_BY_UID), params,
        new MembershipCountyMapper(countyRepo));

    if (counties.size() == 1) {
      return counties.get(0);
    } else if (counties.size() == 0) {
      LOGGER.warn("MembershipCounty with UID of [{}] was not found.", uid);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.error("More than one MembershipCounty with UID of [{}] was found.", uid);
      throw new IncorrectResultSizeDataAccessException(1, counties.size());
    }
  }

  @Override
  public String generateOwnerId(final String firstName, final String lastName) {
    Assert.hasText(firstName, "Assertion Failure - argument [firstName] cannot be null or empty.");
    Assert.isTrue(firstName.length() > 2, "Assertion Failure - argument [firstName] must have at least 3 characters.");
    Assert.hasText(lastName, "Assertion Failure - argument [lastName] cannot be null or empty.");
    Assert.isTrue(lastName.length() > 2, "Assertion Failure - argument [lastName] must have at least 3 characters.");

    LOGGER.info("Calling generateOwnerId for [{}, {}].", lastName, firstName);

    String partial = StringUtils.left(lastName, 3).toUpperCase() + StringUtils.left(firstName, 3).toUpperCase();

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, String> params = new HashMap<String, String>();
    params.put("ownerIdent", partial + '%');

    int cnt = getJdbcTemplate().queryForObject(stmtLoader.load(GET_PARTIAL_OWNERID_COUNT), params, Integer.class);
    return (partial + cnt);
  }

  @Override
  @Transactional
//  @CacheEvict(value = "membersList", key = "#membership.getMembershipUID()")
  public Member saveMember(final Member member, final Membership membership, final User user)
      throws DataAccessException {

    Assert.notNull(member, "Assertion Failed - argument [member] cannot be null");
    Assert.notNull(membership, "Assertion Failed - argument [membership] cannot be null");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Saving Member [{}]", member.getEffectiveMemberName());

    // save the Person portion of the Member
    if (member.getPerson() != null) {
      personRepo.savePerson(member.getPerson(), user);
    }

    Member savedMember;
    if (member.getMemberUID() == 0L) {
      savedMember = insertMember(member, membership, user);
    } else {
      savedMember = updateMember(member, membership, user);
    }

    for (Address address : member.getAddresses()) {
      address.setParentEntityUID(savedMember.getMemberUID());
      address.setParentEntityName(ContactRepository.ENTITY_MEMBER);
      getContactRepository().saveAddress(address, user);
    }

    for (PhoneNumber phone : member.getPhoneNumbers()) {
      phone.setParentEntityUID(savedMember.getMemberUID());
      phone.setParentEntityName(ContactRepository.ENTITY_MEMBER);
      getContactRepository().savePhoneNumber(phone, user);
    }

    for (EmailAddress email : member.getEmailAddresses()) {
      email.setParentEntityUID(savedMember.getMemberUID());
      email.setParentEntityName(ContactRepository.ENTITY_MEMBER);
      getContactRepository().saveEmailAddress(email, user);
    }

    return savedMember;
  }

  @Override
  @Transactional
//  @CacheEvict(value = "membershipCountiesList", key = "#membership.getMembershipUID()")
  public MembershipCounty saveMembershipCounty(final MembershipCounty county,
                                               final Membership membership,
                                               final User user)
      throws DataAccessException {

    Assert.notNull(county, "Assertion Failed - argument [builder] cannot be null");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Saving MembershipCounty [{}]", county.getCounty().getCountyName());

    if (county.getMembershipCountyUID() == 0L) {
      return insertMembershipCounty(county, membership, user);
    } else {
      return updateMembershipCounty(county, membership, user);
    }
  }

  @Override
  @Transactional
//  @CacheEvict(value = "membersList", key = "#membership.getMembershipUID()")
  public Member closeMember(final Member member, final CodeValue closeReason, final String closeText, final User user)
      throws IncorrectResultSizeDataAccessException {

    Assert.notNull(closeReason, "Assertion Failure - argument [closeReason] cannot be null");
    Assert.notNull(member, "Assertion Failure - argument [member] cannot be null");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Closing Member [{}]", member.getMemberUID());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("close_reason_id", closeReason.getCodeValueUID());
    params.addValue("close_reason_txt", StringUtils.trimToNull(closeText));
    params.addValue("member_id", Lists.newArrayList(member.getMemberUID()));
    params.addValue("updt_id", user.getUserUID());

    int cnt = getJdbcTemplate().update(stmtLoader.load(CLOSE_MEMBER), params);
    if (cnt == 1) {
      member.setActive(false);
      member.setCloseReasonUID(closeReason.getCodeValueUID());
      member.setCloseReasonText(closeText);
      member.setCloseDate(new DateTime());

      return member;
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }
  }

  private Member insertMember(final Member member, final Membership membership, final User user)
      throws DataAccessException {

    LOGGER.info("Inserting new Member [{}]", member.getEffectiveMemberName());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_MEMBER),
        MemberMapper.mapInsertStatement(member, membership, user), keyHolder);

    if (cnt == 1) {
      member.setMemberUID(keyHolder.getKey().longValue());
    } else {
      throw new IncorrectResultSizeDataAccessException("Failed to insert Member: expected 1, actual " + cnt, 1, cnt);
    }

    return member;
  }

  private Member updateMember(final Member member, final Membership membership, final User user)
      throws DataAccessException {

    LOGGER.info("Updating Member [{}]", member.getEffectiveMemberName());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_MEMBER),
        MemberMapper.mapUpdateStatement(member, membership, user));

    if (cnt == 1) {
      member.setMemberUpdateCount(member.getMemberUpdateCount() + 1);
    } else if (cnt == 0) {
      throw new OptimisticLockingFailureException("invalid update count of [" + cnt
          + "] possible update count mismatch");
    } else {
      throw new IncorrectResultSizeDataAccessException("Failed to update Member: expected 1, actual " + cnt, 1, cnt);
    }

    return member;
  }

  private MembershipCounty insertMembershipCounty(final MembershipCounty county,
                                                  final Membership membership,
                                                  final User user)
      throws DataAccessException {

    LOGGER.info("Inserting new MembershipCounty [{}]", county.getCounty().getCountyName());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_MEMBERSHIP_COUNTY),
        MembershipCountyMapper.mapInsertStatement(county, membership, user), keyHolder);

    if (cnt == 1) {
      county.setMembershipCountyUID(keyHolder.getKey().longValue());
    } else {
      throw new IncorrectResultSizeDataAccessException("Failed to insert MembershipCounty: expected 1, actual " + cnt, 1, cnt);
    }

    return county;
  }

  private MembershipCounty updateMembershipCounty(final MembershipCounty county,
                                                  final Membership membership,
                                                  final User user)
      throws DataAccessException {

    LOGGER.info("Updating MembershipCounty [{}]", county.getCounty().getCountyName());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_MEMBERSHIP_COUNTY),
        MembershipCountyMapper.mapUpdateStatement(county, membership, user));

    if (cnt == 1) {
      county.setMembershipCountyUpdateCount(county.getMembershipCountyUpdateCount() + 1);
    } else if (cnt == 0) {
      throw new OptimisticLockingFailureException("invalid update count of [" + cnt
          + "] possible update count mismatch");
    } else {
      throw new IncorrectResultSizeDataAccessException("Failed to update MembershipCounty: expected 1, actual " + cnt, 1, cnt);
    }

    return county;
  }
}
