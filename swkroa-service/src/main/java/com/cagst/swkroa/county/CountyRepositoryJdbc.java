package com.cagst.swkroa.county;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC Template implementation of the {@link CountyRepository} interface.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@Named("countyRepository")
/* package */ final class CountyRepositoryJdbc extends BaseRepositoryJdbc implements CountyRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(CountyRepositoryJdbc.class);

  private static final String GET_ACTIVE_COUNTIES = "GET_ACTIVE_COUNTIES";
  private static final String GET_COUNTIES_FOR_STATE = "GET_COUNTIES_FOR_STATE";
  private static final String GET_COUNTY_BY_UID = "GET_COUNTY_BY_UID";
  private static final String GET_COUNTY_BY_STATE_CODE = "GET_COUNTY_BY_STATE_CODE";

  /**
   * Primary Constructor used to create an instance of <i>CountryRepositoryJdbc</i>.
   *
   * @param dataSource
   *     The {@link DataSource} used to retrieve / persist data objects.
   */
  @Inject
  public CountyRepositoryJdbc(final DataSource dataSource) {
    super(dataSource);
  }

  @Override
  @Cacheable("countyLists")
  public Collection<County> getActiveCounties() {
    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    LOGGER.info("Calling getActiveCountries.");

    return getJdbcTemplate().getJdbcOperations().query(stmtLoader.load(GET_ACTIVE_COUNTIES), new CountyMapper());
  }

  @Override
  public Collection<County> getCountiesForState(final String stateCode) {
    Assert.hasText(stateCode, "Assertion Failed - argument [stateCode] cannot be null");

    LOGGER.info("Calling getCountiesForState [{}]", stateCode);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    Map<String, String> params = new HashMap<String, String>(1);
    params.put("state_code", stateCode);

    return getJdbcTemplate().query(stmtLoader.load(GET_COUNTIES_FOR_STATE), params, new CountyMapper());
  }

  @Override
  @Cacheable("counties")
  public County getCountyByUID(final long uid) {
    Assert.isTrue(uid > 0L, "Assertion Failed - argument [uid] must be greater than zero (0)");

    LOGGER.info("Calling getCountyByUID [{}]", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    Map<String, Long> params = new HashMap<String, Long>(1);
    params.put("county_id", uid);

    List<County> counties = getJdbcTemplate().query(stmtLoader.load(GET_COUNTY_BY_UID), params, new CountyMapper());
    if (counties.size() == 1) {
      return counties.get(0);
    } else if (counties.size() == 0) {
      LOGGER.warn("No county with UID of [{}] was found!", uid);
      return null;
    } else {
      LOGGER.warn("More than 1 county with uid of [{}] was found!", uid);
      return null;
    }
  }

  @Override
  @Cacheable("counties")
  public County getCountyByStateAndCode(final String state, final String code) {
    Assert.hasText(state, "Assertion Failed - argument [state] cannot be null or empty.");
    Assert.hasText(code, "Assertion Failed - argument [code] cannot be null or empty.");

    LOGGER.info("Calling getCountyByStateAndCode for state [{}] and code", state, code);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    Map<String, String> params = new HashMap<String, String>(2);
    params.put("state", state);
    params.put("county", code);

    List<County> counties = getJdbcTemplate().query(stmtLoader.load(GET_COUNTY_BY_STATE_CODE), params,
        new CountyMapper());
    if (counties.size() == 1) {
      return counties.get(0);
    } else if (counties.size() == 0) {
      LOGGER.warn("No county with state of [{}] and county code of [{}] was found!", state, code);
      return null;
    } else {
      LOGGER.warn("More than 1 county with state of [{}] and county code of [{}] was found!", state, code);
      return null;
    }
  }
}
