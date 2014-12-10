package com.cagst.swkroa.audit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.Collection;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * JDBC Template implementation of the {@link AuditEventRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("auditRepository")
/* package */ final class AuditEventRepositoryJdbc extends BaseRepositoryJdbc implements AuditEventRepository {
  private static final Logger logger = LoggerFactory.getLogger(AuditEventRepositoryJdbc.class);

  private static final String GET_ALL_AUDIT_EVENTS = "GET_ALL_AUDIT_EVENTS";
  private static final String INSERT_AUDIT_EVENT = "INSERT_AUDIT_EVENT";

  /**
   * Primary constructor used to create an instance of the AuditEventRepositoryJdbc.
   *
   * @param dataSource
   *     The {@link DataSource} used to retrieve / persist data objects.
   */
  @Inject
  public AuditEventRepositoryJdbc(final DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public Collection<AuditEvent> getAllAuditEvents() {
    logger.info("Calling getAllAuditEvents.");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().getJdbcOperations().query(stmtLoader.load(GET_ALL_AUDIT_EVENTS), new AuditEventMapper());
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void save(final AuditEvent auditEvent) {
    Assert.notNull(auditEvent, "Assertion Failed - auditEvent is required; it must not be null");

    logger.info("Calling save for AuditEvent [{}].", auditEvent);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_AUDIT_EVENT),
        AuditEventMapper.mapInsertStatement(auditEvent));
    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException("Incorrect result size: expected 1, actual " + cnt, 1, cnt);
    }
  }
}
