package com.cagst.swkroa.audit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.test.BaseTestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for AuditEventRepositoryJdbc class.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class AuditEventRepositoryJdbcTest extends BaseTestRepository {
  private AuditEventRepositoryJdbc repo;

  @Before
  public void setUp() {
    repo = new AuditEventRepositoryJdbc(createTestDataSource());
    repo.setStatementDialect(StatementLoader.HSQLDB_DIALECT);
  }

  /**
   * Test the getAllAuditEvents method.
   */
  @Test
  public void testGetAllAuditEvents() {
    Collection<AuditEvent> auditEvents = repo.getAllAuditEvents();

    assertNotNull("Ensure the auditEvents collection is not null.", auditEvents);
    assertFalse("Ensure the auditEvents collection is not empty.", auditEvents.isEmpty());
    assertEquals("Ensure we found the correct number of AuditEvents.", 2, auditEvents.size());
  }

  /**
   * Test the save method.
   */
  @Test
  public void testSave() {
    Collection<AuditEvent> auditEvents0 = repo.getAllAuditEvents();

    assertNotNull("Ensure the auditEvents collection is not null.", auditEvents0);
    assertFalse("Ensure the auditEvents collection is not empty.", auditEvents0.isEmpty());
    assertEquals("Ensure we found the correct number of AuditEvents.", 2, auditEvents0.size());

    AuditEvent auditEvent1 = new AuditEvent(AuditEventType.SECURITY, "ACTION", "INSTIGATOR", null);
    repo.save(auditEvent1);

    Collection<AuditEvent> auditEvents1 = repo.getAllAuditEvents();

    assertNotNull("Ensure the auditEvents collection is not null.", auditEvents1);
    assertFalse("Ensure the auditEvents collection is not empty.", auditEvents1.isEmpty());
    assertEquals("Ensure we found the correct number of AuditEvents.", 3, auditEvents1.size());

    AuditEvent auditEvent2 = new AuditEvent(AuditEventType.SECURITY, "ACTION", "INSTIGATOR", "MESSAGE");
    repo.save(auditEvent2);

    Collection<AuditEvent> auditEvents2 = repo.getAllAuditEvents();

    assertNotNull("Ensure the auditEvents collection is not null.", auditEvents2);
    assertFalse("Ensure the auditEvents collection is not empty.", auditEvents2.isEmpty());
    assertEquals("Ensure we found the correct number of AuditEvents.", 4, auditEvents2.size());
  }
}
