package com.cagst.swkroa.billing;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A JDBC Implementation of the {@link BillingRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("billingRepository")
public class BillingRepositoryJdbc extends BaseRepositoryJdbc implements BillingRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(BillingRepositoryJdbc.class);

  private static final String GET_BILLING_RUNS = "GET_BILLING_RUNS";

  /**
   * Primary Constructor used to create an instance of <i>BillingRepositoryJdbc</i>.
   *
   * @param dataSource
   *     The {@link DataSource} used to retrieve / persist data objects.
   */
  @Inject
  public BillingRepositoryJdbc(final DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public List<BillingRun> getBillingRuns() {
    LOGGER.info("Calling getBillingRuns.");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(stmtLoader.load(GET_BILLING_RUNS), new BillingRunMapper());
  }
}
