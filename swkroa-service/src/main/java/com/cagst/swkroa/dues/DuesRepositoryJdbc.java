package com.cagst.swkroa.dues;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A JDBC Implementation of the {@link DuesRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("duesRepository")
public class DuesRepositoryJdbc extends BaseRepositoryJdbc implements DuesRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(DuesRepositoryJdbc.class);

  private static final String GET_DUES_RUNS = "GET_DUES_RUNS";

  /**
   * Primary Constructor used to create an instance of <i>DuesRepositoryJdbc</i>.
   *
   * @param dataSource
   *     The {@link DataSource} used to retrieve / persist data objects.
   */
  @Inject
  public DuesRepositoryJdbc(final DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public List<DuesRun> getDuesRuns() {
    LOGGER.info("Calling getDuesRuns.");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(stmtLoader.load(GET_DUES_RUNS), new DuesRunMapper());
  }
}
