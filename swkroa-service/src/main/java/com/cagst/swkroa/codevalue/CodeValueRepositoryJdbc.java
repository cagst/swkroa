package com.cagst.swkroa.codevalue;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.user.User;
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
 * JDBC Template implementation of the {@link CodeValueRepository} interface.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
/* package */class CodeValueRepositoryJdbc extends BaseRepositoryJdbc implements CodeValueRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(CodeValueRepositoryJdbc.class);

  private static final String GET_CODESET_BY_UID = "GET_CODESET_BY_UID";
  private static final String GET_ACTIVE_CODESETS = "GET_ACTIVE_CODESETS";
  private static final String GET_CODEVALUES_FOR_CODESET = "GET_CODEVALUES_FOR_CODESET";
  private static final String GET_CODEVALUES_FOR_CODESET_BY_MEANING = "GET_CODEVALUES_FOR_CODESET_BY_MEANING";
  private static final String GET_CODEVALUE_BY_UID = "GET_CODEVALUE_BY_UID";
  private static final String GET_CODEVALUE_BY_MEANING = "GET_CODEVALUE_BY_MEANING";

  private static final String INSERT_CODEVALUE = "INSERT_CODEVALUE";
  private static final String UPDATE_CODEVALUE = "UPDATE_CODEVALUE";

  /**
   * Primary Constructor used to create an instance of the CodeValueRepositoryJdbc.
   *
   * @param dataSource
   *     The {@link DataSource} used to retrieve / persist data objects.
   */
  public CodeValueRepositoryJdbc(final DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public CodeSet getCodeSetByUID(final long uid) {
    LOGGER.info("Calling CodeSetByUID for [{}].", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<String, Long>(1);
    params.put("codeset_id", uid);

    List<CodeSet> codesets = getJdbcTemplate().query(stmtLoader.load(GET_CODESET_BY_UID), params, new CodeSetMapper());
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
  public List<CodeValue> getCodeValuesForCodeSet(final CodeSet codeset) {
    Assert.notNull(codeset, "Assertion Failed - argument [codeset] cannot be null.");

    LOGGER.info("Calling CodeValuesForCodeSet for [{}].", codeset.getDisplay());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<String, Long>(1);
    params.put("codeset_id", codeset.getCodeSetUID());

    return getJdbcTemplate().query(stmtLoader.load(GET_CODEVALUES_FOR_CODESET), params, new CodeValueMapper());
  }

  @Override
  @Cacheable(value = "codeValueLists")
  public List<CodeValue> getCodeValuesForCodeSetByMeaning(final String codeSetMeaning) {
    Assert.hasText(codeSetMeaning, "Assertion Failed - argument [codeSetMeaning] cannot be null or empty.");

    LOGGER.info("Calling getCodeValuesForCodeSetByMeaning for [{}].", codeSetMeaning);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, String> params = new HashMap<String, String>(1);
    params.put("codeset_meaning", codeSetMeaning);

    return getJdbcTemplate().query(stmtLoader.load(GET_CODEVALUES_FOR_CODESET_BY_MEANING), params,
        new CodeValueMapper());
  }

  @Override
  @Cacheable(value = "codeValues")
  public CodeValue getCodeValueByUID(final long uid) {
    if (uid == 0L) {
      return null;
    }

    LOGGER.info("Calling getCodeValueByUID for [{}].", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<String, Long>(1);
    params.put("codevalue_id", uid);

    List<CodeValue> codevalues = getJdbcTemplate().query(stmtLoader.load(GET_CODEVALUE_BY_UID), params,
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
  public CodeValue getCodeValueByMeaning(final String meaning) {
    Assert.hasText(meaning, "Assertion Failed - argument [meaning] cannot be null or empty.");

    LOGGER.info("Calling getCodeValueByMeaning for [{}].", meaning);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, String> params = new HashMap<String, String>(1);
    params.put("meaning", meaning);

    List<CodeValue> codevalues = getJdbcTemplate().query(stmtLoader.load(GET_CODEVALUE_BY_MEANING), params,
        new CodeValueMapper());

    if (codevalues.size() == 1) {
      return codevalues.get(0);
    } else if (codevalues.size() == 0) {
      LOGGER.warn("No codevalues with meaning of [{}] were found!", meaning);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.warn("More and 1 codevalue with meaning of [{}] was found!", meaning);
      throw new IncorrectResultSizeDataAccessException(1, codevalues.size());
    }
  }

  @Override
  @CacheEvict(value = "codeValueLists", allEntries = true)
  public CodeValue saveCodeValueForCodeSet(final CodeValue codeValue, final User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, DataAccessException {

    Assert.notNull(codeValue, "[Assertion failed] - argument codeValue cannot be null.");
    Assert.notNull(user, "[Assertion failed] - argument user cannot be null.");

    LOGGER.info("Saving CodeValue [{}].", codeValue.getDisplay());

    if (codeValue.getCodeValueUID() == 0L) {
      return insertCodeValueForCodeSet(codeValue, user);
    } else {
      return updateCodeValueForCodeSet(codeValue, user);
    }
  }

  private CodeValue insertCodeValueForCodeSet(final CodeValue codeValue, final User user)
      throws IncorrectResultSizeDataAccessException, DataAccessException {

    LOGGER.info("Inserting new CodeValue [{}].", codeValue.getDisplay());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_CODEVALUE),
        CodeValueMapper.mapInsertStatement(codeValue, user), keyHolder);
    if (cnt == 1) {
      codeValue.setCodeValueUID(keyHolder.getKey().longValue());
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return codeValue;
  }

  private CodeValue updateCodeValueForCodeSet(final CodeValue codeValue, final User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, DataAccessException {

    LOGGER.info("Updating CodeValue [{}].", codeValue.getDisplay());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_CODEVALUE),
        CodeValueMapper.mapUpdateStatement(codeValue, user));
    if (cnt == 1) {
      codeValue.setCodeValueUpdateCount(codeValue.getCodeValueUpdateCount() + 1);
    } else if (cnt == 0) {
      throw new OptimisticLockingFailureException("invalid update count of [" + cnt
          + "] possible update count mismatch");
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return codeValue;
  }
}
