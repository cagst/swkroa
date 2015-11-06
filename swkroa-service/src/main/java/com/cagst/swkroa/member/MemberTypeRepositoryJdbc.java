package com.cagst.swkroa.member;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.user.User;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

/**
 * JDBC Template implementation of the {@link MemberTypeRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("memberTypeRepository")
/* package */class MemberTypeRepositoryJdbc extends BaseRepositoryJdbc implements MemberTypeRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(MemberTypeRepositoryJdbc.class);

  private static final String GET_MEMBERTYPE_BY_UID           = "GET_MEMBERTYPE_BY_UID";
  private static final String GET_MEMBERTYPE_BY_MEANING       = "GET_MEMBERTYPE_BY_MEANING";
  private static final String GET_MEMBERTYPES_ACTIVE          = "GET_MEMBERTYPES_ACTIVE";
  private static final String GET_MEMBERTYPES_FOR_MEMBERTYPES = "GET_MEMBERTYPES_FOR_MEMBERTYPES";

  private static final String INSERT_MEMBERTYPE = "INSERT_MEMBERTYPE";
  private static final String UPDATE_MEMBERTYPE = "UPDATE_MEMBERTYPE";

  /**
   * Primary constructor used to create an instance of the MemberTypeRepositoryJdbc.
   *
   * @param dataSource
   *     The {@link DataSource} to use to retrieve / persist data objects.
   */
  @Inject
  public MemberTypeRepositoryJdbc(final DataSource dataSource) {
    super(dataSource);
  }

  @Override
  @Cacheable(value = "memberTypes")
  public MemberType getMemberTypeByUID(final long id)
      throws EmptyResultDataAccessException, IncorrectResultSizeDataAccessException {

    LOGGER.info("Calling getMemberTypeByID for [{}].", id);

    Map<String, Object> params = new HashMap<>();
    params.put("member_type_id", id);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<MemberType> types = getJdbcTemplate().query(
        stmtLoader.load(GET_MEMBERTYPE_BY_UID),
        params,
        new MemberTypeMapper()
    );

    if (types.size() == 1) {
      return types.get(0);
    } else if (types.size() == 0) {
      LOGGER.warn("MemberType with ID of [{}] was not found.", id);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.error("More than one MemberType with ID of [{}] was found.", id);
      throw new IncorrectResultSizeDataAccessException(1, types.size());
    }
  }

  @Override
  @Cacheable(value = "memberTypes")
  public MemberType getMemberTypeByMeaning(final String meaning)
      throws EmptyResultDataAccessException, IncorrectResultSizeDataAccessException {

    LOGGER.info("Calling getMemberTypeByMeaning for [{}].", meaning);

    return getMemberTypeByMeaningAsOf(meaning, DateTime.now());
  }

  @Override
  @Cacheable(value = "memberTypes")
  public MemberType getMemberTypeByMeaningAsOf(final String meaning, final DateTime effectiveDateTime)
      throws EmptyResultDataAccessException, IncorrectResultSizeDataAccessException {

    Assert.hasText(meaning, "Assertion Failed - argument [meaning] cannot be null or empty");
    Assert.notNull(effectiveDateTime, "Assertion Failed - argument [effectiveDateTime] cannot be null");

    LOGGER.info("Calling getMemberTypeByMeaningAsOf for [{}] as of [{}]", meaning, effectiveDateTime);

    Map<String, Object> params = new HashMap<String, Object>(2);
    params.put("member_type_meaning", meaning);
    params.put("eff_dt", effectiveDateTime.toDate());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<MemberType> types = getJdbcTemplate().query(
        stmtLoader.load(GET_MEMBERTYPE_BY_MEANING),
        params,
        new MemberTypeMapper()
    );

    if (types.size() == 1) {
      return types.get(0);
    } else if (types.size() == 0) {
      LOGGER.warn("MemberType with Meaning of [{}] was not found for [{}].", meaning, effectiveDateTime);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.error("More than one MemberType with Meaning of [{}] was found for [{}].", meaning, effectiveDateTime);
      throw new IncorrectResultSizeDataAccessException(1, types.size());
    }
  }

  @Override
  @Cacheable(value = "memberTypeList")
  public List<MemberType> getActiveMemberTypes() {
    LOGGER.info("Calling getActiveMemberTypes.");

    return getActiveMemberTypesAsOf(DateTime.now());
  }

  @Override
  @Cacheable(value = "memberTypeList")
  public List<MemberType> getActiveMemberTypesAsOf(final DateTime effectiveDateTime) {
    Assert.notNull(effectiveDateTime, "Assertion Failed - argument [effectiveDateTime] cannot be null");

    LOGGER.info("Calling getActiveMemberTypesAsOf [{}]", effectiveDateTime);

    Map<String, Object> params = new HashMap<String, Object>(1);
    params.put("eff_dt", effectiveDateTime.toDate());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(stmtLoader.load(GET_MEMBERTYPES_ACTIVE), params, new MemberTypeMapper());
  }

  @Override
  @Cacheable(value = "memberTypeList")
  public List<MemberType> getActiveMemberTypesForMemberType(final long memberTypeId) {
    Assert.isTrue(memberTypeId > 0, "Assertion Failed - argument [memberTypeId] must be a positive value");

    LOGGER.info("Calling getActiveMemberTypesForMemberType [{}]", memberTypeId);

    Map<String, Object> params = new HashMap<String, Object>(1);
    params.put("member_type_id", memberTypeId);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(stmtLoader.load(GET_MEMBERTYPES_FOR_MEMBERTYPES), params, new MemberTypeMapper());
  }

  @Override
  @CacheEvict(value = {"memberTypes", "memberTypeList"}, allEntries = true)
  public MemberType saveMemberType(final MemberType memberType, final User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, DataAccessException {

    Assert.notNull(memberType, "Assertion failed - argument memberType cannot be null.");

    LOGGER.info("Calling saveMemberType for [{}]", memberType.getMemberTypeMeaning());

    if (memberType.getMemberTypeUID() == 0L) {
      return insertMemberType(memberType, user);
    } else {
      return updateMemberType(memberType, user);
    }
  }

  private MemberType insertMemberType(final MemberType memberType, final User user)
      throws IncorrectResultSizeDataAccessException, DataAccessException {

    LOGGER.info("Inserting new MemberType [{}]", memberType.getMemberTypeMeaning());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_MEMBERTYPE),
        MemberTypeMapper.mapInsertStatement(memberType, user), keyHolder);
    if (cnt == 1) {
      memberType.setMemberTypeUID(keyHolder.getKey().longValue());
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return memberType;
  }

  private MemberType updateMemberType(final MemberType memberType, final User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, DataAccessException {

    LOGGER.info("Updating MemberType [{}]", memberType.getMemberTypeMeaning());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_MEMBERTYPE),
        MemberTypeMapper.mapUpdateStatement(memberType, user));
    if (cnt == 1) {
      memberType.setMemberTypeUpdateCount(memberType.getMemberTypeUpdateCount() + 1);
    } else if (cnt == 0) {
      throw new OptimisticLockingFailureException("invalid update count of [" + cnt + "] possible update count mismatch");
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return memberType;
  }
}
