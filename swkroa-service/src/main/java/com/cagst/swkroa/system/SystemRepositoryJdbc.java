package com.cagst.swkroa.system;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;

import com.cagst.swkroa.internal.BaseRepositoryJdbc;
import com.cagst.swkroa.internal.StatementLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A JDBC implementation of the {@link SystemRepository} interface.
 *
 * @author Craig GAskill
 */
@Named("systemRepository")
/* packaged */ final class SystemRepositoryJdbc extends BaseRepositoryJdbc implements SystemRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(SystemRepositoryJdbc.class);

  private static final String GET_SYSTEM_SETTINGS = "GET_SYSTEM_SETTINGS";

  /**
   * Primary Constructor used to create an instance of the <i>BaseRepositoryJDBC</i> class.
   *
   * @param dataSource
   *     The {@link DataSource} used to retrieve / persist data objects.
   */
  @Inject
  protected SystemRepositoryJdbc(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public List<SystemSetting> getSystemSettings() {
    LOGGER.info("Calling getSystemSettings");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(
        stmtLoader.load(GET_SYSTEM_SETTINGS),
        new SystemSettingMapper()
    );
  }
}
