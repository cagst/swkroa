package com.cagst.swkroa.member;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.contact.Address;
import com.cagst.swkroa.contact.ContactRepository;
import com.cagst.swkroa.contact.EmailAddress;
import com.cagst.swkroa.contact.PhoneNumber;
import com.cagst.swkroa.county.CountyRepository;
import com.cagst.swkroa.internal.BaseRepositoryJdbc;
import com.cagst.swkroa.internal.StatementLoader;
import com.cagst.swkroa.person.PersonRepository;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserType;
import com.cagst.swkroa.utils.SwkroaStringUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
/* package */ final class MemberRepositoryJdbc extends BaseRepositoryJdbc implements MemberRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(MemberRepositoryJdbc.class);

  private static final String GET_MEMBERS_FOR_MEMBERSHIP = "GET_MEMBERS_FOR_MEMBERSHIP";
  private static final String GET_MEMBERS_BY_NAME        = "GET_MEMBERS_BY_NAME";
  private static final String GET_MEMBERS_BY_NAME_COUNT  = "GET_MEMBERS_BY_NAME_COUNT";
  private static final String GET_MEMBER_BY_UID          = "GET_MEMBER_BY_UID";
  private static final String GET_MEMBER_BY_PERSON_UID   = "GET_MEMBER_BY_PERSON_UID";
  private static final String GET_MEMBER_BY_OWNER_ID     = "GET_MEMBER_BY_OWNER_ID";

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
  private final ContactRepository contactRepo;

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
  public MemberRepositoryJdbc(DataSource dataSource,
                              PersonRepository personRepo,
                              MemberTypeRepository memberTypeRepo,
                              CountyRepository countyRepo,
                              ContactRepository contactRepo) {

    super(dataSource);

    this.personRepo = personRepo;
    this.memberTypeRepo = memberTypeRepo;
    this.countyRepo = countyRepo;
    this.contactRepo = contactRepo;
  }

  @Override
//  @Cacheable(value = "membersList", key = "#membership.getMembershipUID()")
  public List<Member> getMembersForMembership(Membership membership) {
    Assert.notNull(membership, "Argument [membership] cannot be null");

    LOGGER.info("Calling getMembersForMembership for [{}].", membership.getMembershipUID());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(
        stmtLoader.load(GET_MEMBERS_FOR_MEMBERSHIP),
        new MapSqlParameterSource("membership_id", membership.getMembershipUID()),
        new MemberMapper(personRepo, memberTypeRepo));
  }

  @Override
  public List<Member> getMembersByName(String name, Status status, int start, int limit) {
    LOGGER.info("Calling getMembersByName for [{}].", name);

    Assert.hasText(name, "Argument [name] cannot be null or empty");
    Assert.notNull(status, "Argument [status] cannot be null");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("name", SwkroaStringUtils.normalizeToKey(name) + "%");
    params.addValue("status", status.toString());
    params.addValue("start", start);
    params.addValue("limit", limit);

    return getJdbcTemplate().query(stmtLoader.load(GET_MEMBERS_BY_NAME), params, new MemberMapper(personRepo, memberTypeRepo));
  }

  @Override
  public long getMembersByNameCount(String name, Status status) {
    LOGGER.info("Calling getMembersByName for [{}].", name);

    Assert.hasText(name, "Assertion Failture - argument [name] cannot be null or empty");
    Assert.notNull(status, "Assertion Failure - argument [status] cannot be null");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("name", SwkroaStringUtils.normalizeToKey(name) + "%");
    params.addValue("status", status.toString());

    return getJdbcTemplate().queryForObject(stmtLoader.load(GET_MEMBERS_BY_NAME_COUNT), params, Long.class);
  }

  @Override
  public Member getMemberByUID(long uid) throws IncorrectResultSizeDataAccessException {
    LOGGER.info("Calling getMemberByUID for [{}].", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<Member> members = getJdbcTemplate().query(stmtLoader.load(GET_MEMBER_BY_UID),
        new MapSqlParameterSource("member_id", uid),
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
  public Optional<Member> getMemberByPersonUID(long uid) throws IncorrectResultSizeDataAccessException {
    LOGGER.info("Calling getMemberByPersonUID for [{}]", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<Member> members = getJdbcTemplate().query(stmtLoader.load(GET_MEMBER_BY_PERSON_UID),
        new MapSqlParameterSource("person_id", uid),
        new MemberMapper(personRepo, memberTypeRepo));

    if (members.size() == 1) {
      return Optional.of(members.get(0));
    } else if (members.size() == 0) {
      return Optional.empty();
    } else {
      LOGGER.error("More than one Member with Person ID of [{}] was found.", uid);
      throw new IncorrectResultSizeDataAccessException(1, members.size());
    }
  }

  @Override
  public Optional<Member> getMemberByOwnerId(String ownerId) throws IncorrectResultSizeDataAccessException {
    Objects.requireNonNull(ownerId, "Argument [ownerId] cannot be null");

    LOGGER.info("Calling getMemberByOwnerId for [{}]", ownerId);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<Member> members = getJdbcTemplate().query(stmtLoader.load(GET_MEMBER_BY_OWNER_ID),
        new MapSqlParameterSource("owner_ident", ownerId),
        new MemberMapper(personRepo, memberTypeRepo));

    if (members.size() == 1) {
      return Optional.of(members.get(0));
    } else if (members.size() == 0) {
      LOGGER.info("No Member found for OwnerId of [{}]", ownerId);
      return Optional.empty();
    } else {
      LOGGER.error("More than one Member with Owner Id of [{}] was found.", ownerId);
      throw new IncorrectResultSizeDataAccessException(1, members.size());
    }
  }

  @Override
//  @Cacheable(value = "membershipCountiesList", key = "#membership.getMembershipUID()")
  public List<MembershipCounty> getMembershipCountiesForMembership(Membership membership) {
    Assert.notNull(membership, "Argument [membership] cannot be null");

    LOGGER.info("Calling getMembershipCountiesForMembership for [{}].", membership.getMembershipUID());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(stmtLoader.load(GET_MEMBERSHIP_COUNTIES_FOR_MEMBERSHIP),
        new MapSqlParameterSource("membership_id", membership.getMembershipUID()),
        new MembershipCountyMapper(countyRepo));
  }

  @Override
  public MembershipCounty getMembershipCountyByUID(long uid) throws IncorrectResultSizeDataAccessException {
    LOGGER.info("Calling getMembershipCountyByUID for [{}].", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<MembershipCounty> counties = getJdbcTemplate().query(
        stmtLoader.load(GET_MEMBERSHIP_COUNTY_BY_UID),
        new MapSqlParameterSource("membership_county_id", uid),
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
  public String generateOwnerId(String firstName, String lastName) {
    Assert.hasText(firstName, "Argument [firstName] cannot be null or empty.");
    Assert.isTrue(firstName.length() > 2, "Argument [firstName] must have at least 3 characters.");
    Assert.hasText(lastName, "Argument [lastName] cannot be null or empty.");
    Assert.isTrue(lastName.length() > 2, "Argument [lastName] must have at least 3 characters.");

    LOGGER.info("Calling generateOwnerId for [{}, {}].", lastName, firstName);

    String partial = StringUtils.left(lastName, 3).toUpperCase() + StringUtils.left(firstName, 3).toUpperCase();

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().queryForObject(
        stmtLoader.load(GET_PARTIAL_OWNERID_COUNT),
        new MapSqlParameterSource("ownerIdent", partial + '%'),
        Integer.class);

    return (partial + cnt);
  }

  @Override
  @Transactional
//  @CacheEvict(value = "membersList", key = "#membership.getMembershipUID()")
  public Member saveMember(Member member, Membership membership, User user)
      throws DataAccessException {

    Assert.notNull(member, "Argument [member] cannot be null");
    Assert.notNull(membership, "Argument [membership] cannot be null");
    Assert.notNull(user, "Argument [user] cannot be null");

    LOGGER.info("Saving Member [{}]", member.getMembershipUID());

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

    for (Address address : member.getPerson().getAddresses()) {
      address.setParentEntityUID(member.getMemberUID());
      address.setParentEntityName(UserType.MEMBER.name());

      contactRepo.saveAddress(address, user);
    }

    for (PhoneNumber phone : member.getPerson().getPhoneNumbers()) {
      phone.setParentEntityUID(member.getMemberUID());
      phone.setParentEntityName(UserType.MEMBER.name());

      contactRepo.savePhoneNumber(phone, user);
    }

    for (EmailAddress email : member.getPerson().getEmailAddresses()) {
      email.setParentEntityUID(member.getMemberUID());
      email.setParentEntityName(UserType.MEMBER.name());
      contactRepo.saveEmailAddress(email, user);
    }

    return savedMember;
  }

  @Override
  @Transactional
//  @CacheEvict(value = "membershipCountiesList", key = "#membership.getMembershipUID()")
  public MembershipCounty saveMembershipCounty(MembershipCounty county,
                                               Membership membership,
                                               User user)
      throws DataAccessException {

    Assert.notNull(county, "Argument [builder] cannot be null");
    Assert.notNull(user, "Argument [user] cannot be null");

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
  public Member closeMember(Member member, CodeValue closeReason, String closeText, User user)
      throws IncorrectResultSizeDataAccessException {

    Assert.notNull(closeReason, "Argument [closeReason] cannot be null");
    Assert.notNull(member, "Argument [member] cannot be null");
    Assert.notNull(user, "Argument [user] cannot be null");

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
      member.setCloseDate(LocalDateTime.now(getClock()));

      return member;
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }
  }

  private Member insertMember(Member member, Membership membership, User user)
      throws DataAccessException {

    LOGGER.info("Inserting new Member [{}]", member.getMembershipUID());

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

  private Member updateMember(Member member, Membership membership, User user)
      throws DataAccessException {

    LOGGER.info("Updating Member [{}]", member.getMembershipUID());

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

  private MembershipCounty insertMembershipCounty(MembershipCounty county,
                                                  Membership membership,
                                                  User user)
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

  private MembershipCounty updateMembershipCounty(MembershipCounty county,
                                                  Membership membership,
                                                  User user)
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
