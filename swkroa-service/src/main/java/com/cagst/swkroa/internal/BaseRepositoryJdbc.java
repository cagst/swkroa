package com.cagst.swkroa.internal;

import javax.sql.DataSource;

import java.time.Clock;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Abstract class that implements common functionality for all JDBC repositories.
 *
 * @author Craig Gaskill
 */
public abstract class BaseRepositoryJdbc {
  private final NamedParameterJdbcTemplate jdbcTemplate;

  private StatementDialect dialect = StatementDialect.MYSQL;
  private Clock clock = Clock.systemDefaultZone();

  /**
   * Primary Constructor used to create an instance of the <i>BaseRepositoryJDBC</i> class.
   *
   * @param dataSource
   *     The {@link DataSource} used to retrieve / persist data objects.
   */
  protected BaseRepositoryJdbc(DataSource dataSource) {
    jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  /**
   * @return The {@link NamedParameterJdbcTemplate} JDBC Template associated to this repository.
   */
  protected NamedParameterJdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  /**
   * @return The current {@link StatementDialect} being used to retrieve SQL resource statements.
   */
  protected StatementDialect getStatementDialect() {
    return dialect;
  }

  /**
   * Sets the dialect to use to retrieve SQL resource statements.
   *
   * @param dialect
   *     The new {@link StatementDialect} to use to retrieve SQL resource statements, "oracle" is the default.
   */
  public void setStatementDialect(StatementDialect dialect) {
    this.dialect = dialect;
  }

  /**
   * @return The {@link Clock} that is currently being used to retrieve the current instant.
   */
  public Clock getClock() {
    return clock;
  }

  /**
   * Sets the {@link Clock} to use to retrieve the current instant.
   *
   * @param clock
   *     The {@link Clock} to use to retrieve the current instant.
   */
  public void setClock(Clock clock) {
    this.clock = clock;
  }
}
