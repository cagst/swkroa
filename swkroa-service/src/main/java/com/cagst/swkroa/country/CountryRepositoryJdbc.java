package com.cagst.swkroa.country;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;

import com.cagst.swkroa.internal.BaseRepositoryJdbc;
import com.cagst.swkroa.internal.StatementLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.Assert;

/**
 * JDBC Template implementation of the {@link CountryRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("countyRepository")
/* package */ final class CountryRepositoryJdbc extends BaseRepositoryJdbc implements CountryRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(CountryRepositoryJdbc.class);

  private static final String GET_ACTIVE_COUNTRIES = "GET_ACTIVE_COUNTRIES";

  private static final String GET_ACTIVE_STATES = "GET_ACTIVE_STATES";
  private static final String GET_ACTIVE_STATES_FOR_COUNTRY = "GET_ACTIVE_STATES_FOR_COUNTRY";

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
  public CountryRepositoryJdbc(final DataSource dataSource) {
    super(dataSource);
  }

  @Override
  @Cacheable("countryLists")
  public List<Country> getActiveCountries() {
    StatementLoader statementLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    LOGGER.info("Calling getActiveCountries");

    return getJdbcTemplate().query(statementLoader.load(GET_ACTIVE_COUNTRIES), new CountryMapper());
  }

  @Override
  @Cacheable("stateLists")
  public List<State> getActiveStates() {
    StatementLoader statementLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    LOGGER.info("Calling getActiveStates");

    return getJdbcTemplate().query(statementLoader.load(GET_ACTIVE_STATES), new StateMapper());
  }

  @Override
  @Cacheable("stateLists")
  public List<State> getActiveStatesForCountry(String countryCode) {
    Assert.hasText(countryCode, "Argument [countryCode] cannot be null");

    StatementLoader statementLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    LOGGER.info("Calling getActiveStatesForCountry for [{}]", countryCode);

    return getJdbcTemplate().query(
        statementLoader.load(GET_ACTIVE_STATES_FOR_COUNTRY),
        new MapSqlParameterSource("country_code", countryCode),
        new StateMapper()
    );
  }

  @Override
  @Cacheable("countyLists")
  public List<County> getActiveCounties() {
    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    LOGGER.info("Calling getActiveCountries.");

    return getJdbcTemplate().query(stmtLoader.load(GET_ACTIVE_COUNTIES), new CountyMapper());
  }

  @Override
  public List<County> getCountiesForState(String stateCode) {
    Assert.hasText(stateCode, "Argument [stateCode] cannot be null");

    LOGGER.info("Calling getCountiesForState [{}]", stateCode);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(
        stmtLoader.load(GET_COUNTIES_FOR_STATE),
        new MapSqlParameterSource("state_code", stateCode),
        new CountyMapper()
    );
  }

  @Override
  @Cacheable("counties")
  public County getCountyByUID(final long uid) {
    Assert.isTrue(uid > 0L, "Argument [uid] must be greater than zero (0)");

    LOGGER.info("Calling getCountyByUID [{}]", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<County> counties = getJdbcTemplate().query(
        stmtLoader.load(GET_COUNTY_BY_UID),
        new MapSqlParameterSource("county_id", uid),
        new CountyMapper());

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
    Assert.hasText(state, "Argument [state] cannot be null or empty.");
    Assert.hasText(code, "Argument [code] cannot be null or empty.");

    LOGGER.info("Calling getCountyByStateAndCode for state [{}] and code", state, code);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("state", state);
    params.addValue("county", code);

    List<County> counties = getJdbcTemplate().query(stmtLoader.load(GET_COUNTY_BY_STATE_CODE), params, new CountyMapper());

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
