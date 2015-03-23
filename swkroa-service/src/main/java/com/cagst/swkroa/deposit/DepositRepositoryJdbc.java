package com.cagst.swkroa.deposit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;

/**
 * JDBC Template implementation of the {@link DepositRepository} interface.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@Repository("depositRepository")
/* package */ final class DepositRepositoryJdbc extends BaseRepositoryJdbc implements DepositRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(DepositRepositoryJdbc.class);

  private static final String GET_DEPOSITS = "GET_DEPOSITS";
  private static final String GET_DEPOSIT  = "GET_DEPOSIT";

  /**
   * Primary Constructor used to create an instance of <i>DepositRepositoryJdbc</i>.
   *
   * @param dataSource
   *     The {@link DataSource} to use to retrieve / persist data objects.
   */
  @Inject
  public DepositRepositoryJdbc(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public List<Deposit> getDeposits() {
    LOGGER.info("Calling getDeposits");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    return getJdbcTemplate().query(stmtLoader.load(GET_DEPOSITS), new DepositMapper());
  }

  @Override
  public Deposit getDeposit(final long uid) {
    LOGGER.info("Calling getDeposit for [{}]", uid);

    Map<String, Long> params = new HashMap<String, Long>(1);
    params.put("deposit_id", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<Deposit> deposits = getJdbcTemplate().query(stmtLoader.load(GET_DEPOSIT), params, new DepositMapper());
    if (deposits.size() == 1) {
      return deposits.get(0);
    } else if (deposits.size() == 0) {
      LOGGER.warn("Deposit with UID of [{}] was not found.", uid);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.warn("More than one Deposit with UID of [{}] was found.", uid);
      throw new IncorrectResultSizeDataAccessException(1, deposits.size());
    }
  }
}
