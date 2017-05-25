package com.cagst.swkroa.member;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;
import java.util.Set;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.internal.BaseRepositoryJdbc;
import com.cagst.swkroa.internal.StatementLoader;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.utils.SwkroaStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
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
 * JDBC Template implementation of the {@link MembershipRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("membershipRepo")
/* package */ final class MembershipRepositoryJdbc extends BaseRepositoryJdbc implements MembershipRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(MembershipRepositoryJdbc.class);

  private static final String GET_MEMBERSHIP_BY_UID         = "GET_MEMBERSHIP_BY_UID";
  private static final String GET_MEMBERSHIPS               = "GET_MEMBERSHIPS";
  private static final String GET_MEMBERSHIPS_BY_NAME       = "GET_MEMBERSHIPS_BY_NAME";
  private static final String GET_MEMBERSHIPS_DUE_IN_X_DAYS = "GET_MEMBERSHIPS_DUE_IN_X_DAYS";

  private static final String INSERT_MEMBERSHIP    = "INSERT_MEMBERSHIP";
  private static final String UPDATE_MEMBERSHIP    = "UPDATE_MEMBERSHIP";
  private static final String CLOSE_MEMBERSHIPS    = "CLOSE_MEMBERSHIPS";
  private static final String UPDATE_NEXT_DUE_DATE = "UPDATE_NEXT_DUE_DATE";

  private final MemberRepository memberRepo;
  private final CodeValueRepository codeValueRepo;
  private final MemberTypeRepository memberTypeRepo;

  /**
   * Primary Constructor used to create an instance of <i>PersonRepositoryJdbc</i>.
   *
   * @param dataSource
   *     The {@link DataSource} used to retrieve / persist data objects.
   * @param memberRepo
   *     The {@link MemberRepository} to use to retrieve member information.
   * @param codeValueRepo
   *     The {@link CodeValueRepository} to use to retrieve codified information.
   * @param memberTypeRepo
   *     The {@link MemberTypeRepository} to use to retrieve member type information.
   */
  @Inject
  /* package */ MembershipRepositoryJdbc(DataSource dataSource,
                                         MemberRepository memberRepo,
                                         CodeValueRepository codeValueRepo,
                                         MemberTypeRepository memberTypeRepo) {
    super(dataSource);

    this.memberRepo = memberRepo;
    this.codeValueRepo = codeValueRepo;
    this.memberTypeRepo = memberTypeRepo;
  }

  @Override
//  @Cacheable(value = "memberships")
  public Membership getMembershipByUID(long uid)
      throws IncorrectResultSizeDataAccessException {

    LOGGER.info("Calling getMembershipByUID for [{}].", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<Membership> memberships = getJdbcTemplate().query(
        stmtLoader.load(GET_MEMBERSHIP_BY_UID),
        new MapSqlParameterSource("membership_id", uid),
        new MembershipMapper(codeValueRepo, memberTypeRepo)
    );

    if (memberships.size() == 1) {
      return memberships.get(0);
    } else if (memberships.size() == 0) {
      LOGGER.warn("Membership with UID of [{}] was not found.", uid);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.error("More than one Membership with UID of [{}] was found.", uid);
      throw new IncorrectResultSizeDataAccessException(1, memberships.size());
    }
  }

  @Override
  public List<Membership> getMemberships(Status status, MembershipBalance balance) {
    LOGGER.info("Calling getMemberships with status [{}] and balance [{}]", status, balance);

    Assert.notNull(status, "Argument [status] cannot be null");
    Assert.notNull(balance, "Argument [balance] cannot be null");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("status", status.toString());
    params.addValue("balance", balance.toString());

    return getJdbcTemplate().query(stmtLoader.load(GET_MEMBERSHIPS), params, new MembershipMapper(codeValueRepo, memberTypeRepo));
  }

  @Override
  public List<Membership> getMembershipsByName(String name, Status status, MembershipBalance balance) {
    LOGGER.info("Calling getMembershipsByName for [{}].", name);

    Assert.hasText(name, "Argument [name] cannot be null or empty");
    Assert.notNull(status, "Argument [status] cannot be null");
    Assert.notNull(balance, "Argument [balance] cannot be null");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("name", SwkroaStringUtils.normalizeToKey(name) + "%");
    params.addValue("status", status.toString());
    params.addValue("balance", balance.toString());

    return getJdbcTemplate().query(stmtLoader.load(GET_MEMBERSHIPS_BY_NAME), params, new MembershipMapper(codeValueRepo, memberTypeRepo));
  }

  @Override
  public List<Membership> getMembershipsDueInXDays(int days) {
    LOGGER.info("Calling getMembershipsDueInXDays for [{}]", days);

    Assert.isTrue(days >= 0, "Argument [days] must be greater than or equal to zero");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(
        stmtLoader.load(GET_MEMBERSHIPS_DUE_IN_X_DAYS),
        new MapSqlParameterSource("nextDueDate", DateTime.now().plusDays(days).toDate()),
        new MembershipMapper(codeValueRepo, memberTypeRepo));
  }

  @Override
  @Transactional
  public Membership saveMembership(Membership membership, User user)
      throws DataAccessException {

    Assert.notNull(membership, "Argument [membership] cannot be null");
    Assert.notNull(user, "Argument [user] cannot be null");

    LOGGER.info("Saving Membership for [{}]", membership.getMembershipUID());

    // if the Membership is an Individual, ensure the primary member company name field is cleared
    if (StringUtils.equals("ENTITY_INDIVIDUAL", membership.getEntityType().getMeaning())) {
      Member primaryMember = membership.getPrimaryMember();
      if (primaryMember != null) {
        primaryMember.setCompanyName(null);
      }
    }

    if (membership.getMembershipUID() == 0L) {
      insertMembership(membership, user);
    } else {
      updateMembership(membership, user);
    }

    // save MembershipCounty(ies)
    for (MembershipCounty county : membership.getMembershipCounties()) {
      memberRepo.saveMembershipCounty(county, membership, user);
    }

    // save Members
    for (Member member : membership.getMembers()) {
      memberRepo.saveMember(member, membership, user);
    }

    return membership;
  }

  @Override
  @Transactional
  public int closeMemberships(Set<Long> membershipIds, CodeValue closeReason, String closeText, User user)
      throws DataAccessException {

    Assert.notEmpty(membershipIds, "Argument [membershipIds] cannot be null or empty");
    Assert.notNull(closeReason, "Argument [closeReason] cannot be null");
    Assert.notNull(user, "Argument [user] cannot be null");

    LOGGER.info("Closing Memberships");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("close_reason_id", closeReason.getCodeValueUID());
    params.addValue("close_reason_txt", StringUtils.trimToNull(closeText));
    params.addValue("memberships", membershipIds);
    params.addValue("updt_id", user.getUserUID());

    return getJdbcTemplate().update(stmtLoader.load(CLOSE_MEMBERSHIPS), params);
  }

  @Override
  @Transactional
//  @CacheEvict(value = "memberships", allEntries = true)
  public int updateNextDueDate(long membershipId, User user) throws DataAccessException {
    Assert.notNull(user, "Argument [user] cannot be null");

    LOGGER.info("Billing Memberships");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("membershipId", membershipId);
    params.addValue("updt_id", user.getUserUID());

    return getJdbcTemplate().update(stmtLoader.load(UPDATE_NEXT_DUE_DATE), params);
  }

  private Membership insertMembership(Membership membership, User user)
      throws DataAccessException {

    LOGGER.info("Inserting new Membership [{}]", membership.getMembershipUID());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_MEMBERSHIP),
        MembershipMapper.mapInsertStatement(membership, user), keyHolder);
    if (cnt == 1) {
      membership.setMembershipUID(keyHolder.getKey().longValue());
    } else {
      throw new IncorrectResultSizeDataAccessException("Failed to insert Membership: expected 1, actual " + cnt, 1, cnt);
    }

    return membership;
  }

  private Membership updateMembership(Membership membership, User user)
      throws DataAccessException {

    LOGGER.info("Updating Membership [{}]", membership.getMembershipUID());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_MEMBERSHIP),
        MembershipMapper.mapUpdateStatement(membership, user));
    if (cnt == 1) {
      membership.setMembershipUpdateCount(membership.getMembershipUpdateCount() + 1);
    } else if (cnt == 0) {
      throw new OptimisticLockingFailureException("invalid update count of [" + cnt
          + "] possible update count mismatch");
    } else {
      throw new IncorrectResultSizeDataAccessException("Failed to update Membership: expected 1, actual " + cnt, 1, cnt);
    }

    return membership;
  }
}
