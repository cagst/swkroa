package com.cagst.swkroa.codevalue;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;

import com.cagst.swkroa.internal.BaseRepositoryJdbc;
import com.cagst.swkroa.internal.StatementLoader;
import com.cagst.swkroa.user.User;
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
import org.springframework.util.Assert;

/**
 * JDBC Template implementation of the {@link CodeValueRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("codeValueRepo")
/* package */class CodeValueRepositoryJdbc extends BaseRepositoryJdbc implements CodeValueRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(CodeValueRepositoryJdbc.class);

  private static final String GET_CODESET_BY_UID                    = "GET_CODESET_BY_UID";
  private static final String GET_ACTIVE_CODESETS                   = "GET_ACTIVE_CODESETS";
  private static final String GET_CODEVALUES_FOR_CODESET            = "GET_CODEVALUES_FOR_CODESET";
  private static final String GET_CODEVALUES_FOR_CODESET_BY_MEANING = "GET_CODEVALUES_FOR_CODESET_BY_MEANING";
  private static final String GET_CODEVALUE_BY_UID                  = "GET_CODEVALUE_BY_UID";
  private static final String GET_CODEVALUE_BY_MEANING              = "GET_CODEVALUE_BY_MEANING";

  private static final String INSERT_CODEVALUE = "INSERT_CODEVALUE";
  private static final String UPDATE_CODEVALUE = "UPDATE_CODEVALUE";

  /**
   * Primary Constructor used to create an instance of the CodeValueRepositoryJdbc.
   *
   * @param dataSource
   *     The {@link DataSource} used to retrieve / persist data objects.
   */
  @Inject
  public CodeValueRepositoryJdbc(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public CodeSet getCodeSetByUID(long uid) {
    LOGGER.info("Calling CodeSetByUID for [{}].", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<CodeSet> codesets = getJdbcTemplate().query(
        stmtLoader.load(GET_CODESET_BY_UID),
        new MapSqlParameterSource("codeset_id", uid),
        new CodeSetMapper());

    if (codesets.size() == 1) {
      return codesets.get(0);
    } else if (codesets.size() == 0) {
      LOGGER.warn("No codeset with UID of [{}] was found!", uid);
      return null;
    } else {
      LOGGER.warn("More than 1 codeset with uid of [{}] was found!", uid);
      return null;
    }
  }

  @Override
  public List<CodeSet> getActiveCodeSets() {
    LOGGER.info("Calling getActiveCodeSets.");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().getJdbcOperations().query(stmtLoader.load(GET_ACTIVE_CODESETS), new CodeSetMapper());
  }

  @Override
  @Cacheable(value = "codeValueLists")
  public List<CodeValue> getCodeValuesForCodeSet(CodeSet codeset) {
    Assert.notNull(codeset, "Argument [codeset] cannot be null.");

    LOGGER.info("Calling CodeValuesForCodeSet for [{}].", codeset.getDisplay());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(
        stmtLoader.load(GET_CODEVALUES_FOR_CODESET),
        new MapSqlParameterSource("codeset_id", codeset.getCodeSetUID()),
        new CodeValueMapper());
  }

  @Override
  @Cacheable(value = "codeValueLists")
  public List<CodeValue> getCodeValuesForCodeSetByType(CodeSetType codeSetType) {
    Assert.notNull(codeSetType, "Argument [codeSetType] cannot be null.");

    LOGGER.info("Calling getCodeValuesForCodeSetByType for [{}].", codeSetType);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(
        stmtLoader.load(GET_CODEVALUES_FOR_CODESET_BY_MEANING),
        new MapSqlParameterSource("codeset_meaning", codeSetType.name()),
        new CodeValueMapper());
  }

  @Override
  @Cacheable(value = "codeValues")
  public CodeValue getCodeValueByUID(long uid) {
    if (uid == 0L) {
      return null;
    }

    LOGGER.info("Calling getCodeValueByUID for [{}].", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<CodeValue> codevalues = getJdbcTemplate().query(
        stmtLoader.load(GET_CODEVALUE_BY_UID),
        new MapSqlParameterSource("codevalue_id", uid),
        new CodeValueMapper());

    if (codevalues.size() == 1) {
      return codevalues.get(0);
    } else if (codevalues.size() == 0) {
      LOGGER.warn("No codevalues with uid of [{}] were found!", uid);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.warn("More than 1 codevalue with uid of [{}] were found!", uid);
      throw new IncorrectResultSizeDataAccessException(1, codevalues.size());
    }
  }

  @Override
  @Cacheable(value = "codeValues")
  public CodeValue getCodeValueByMeaning(CodeSetType codeSetType, String meaning) {
    Assert.notNull(codeSetType, "Argument [codeSetType] cannot be null");
    Assert.hasText(meaning, "Argument [meaning] cannot be null or empty.");

    LOGGER.info("Calling getCodeValueByMeaning for [{}].", meaning);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("codeset_meaning", codeSetType.name());
    params.addValue("meaning", meaning);

    List<CodeValue> codevalues = getJdbcTemplate().query(stmtLoader.load(GET_CODEVALUE_BY_MEANING), params,
        new CodeValueMapper());

    if (codevalues.size() == 1) {
      return codevalues.get(0);
    } else if (codevalues.size() == 0) {
      LOGGER.warn("No codevalues with meaning of [{}] were found!", meaning);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.warn("More than 1 codevalue with meaning of [{}] was found!", meaning);
      throw new IncorrectResultSizeDataAccessException(1, codevalues.size());
    }
  }

  @Override
  @CacheEvict(value = "codeValueLists", allEntries = true)
  public CodeValue saveCodeValueForCodeSet(CodeValue codeValue, User user)
      throws DataAccessException {

    Assert.notNull(codeValue, "Argument codeValue cannot be null.");
    Assert.notNull(user, "Argument user cannot be null.");

    LOGGER.info("Saving CodeValue [{}].", codeValue.getDisplay());

    if (codeValue.getCodeValueUID() == 0L) {
      return insertCodeValueForCodeSet(codeValue, user);
    } else {
      return updateCodeValueForCodeSet(codeValue, user);
    }
  }

  private CodeValue insertCodeValueForCodeSet(CodeValue codeValue, User user)
      throws DataAccessException {

    LOGGER.info("Inserting new CodeValue [{}].", codeValue.getDisplay());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(
        stmtLoader.load(INSERT_CODEVALUE),
        CodeValueMapper.mapInsertStatement(codeValue, user),
        keyHolder);

    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return codeValue.toBuilder().setCodeValueUID(keyHolder.getKey().longValue()).build();
  }

  private CodeValue updateCodeValueForCodeSet(CodeValue codeValue, User user)
      throws DataAccessException {

    LOGGER.info("Updating CodeValue [{}].", codeValue.getDisplay());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(
        stmtLoader.load(UPDATE_CODEVALUE),
        CodeValueMapper.mapUpdateStatement(codeValue, user));

    if (cnt == 1) {
      return codeValue.toBuilder().setCodeValueUpdateCount(codeValue.getCodeValueUpdateCount() + 1).build();
    } else if (cnt == 0) {
      throw new OptimisticLockingFailureException("invalid update count of [" + cnt
          + "] possible update count mismatch");
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }
  }
}
