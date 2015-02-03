package com.cagst.swkroa.billing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.test.BaseTestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for {@link BillingRepositoryJdbc} class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class BillingRepositoryJdbcTest extends BaseTestRepository {
  private BillingRepositoryJdbc repo;

  @Before
  public void setUp() {
    repo = new BillingRepositoryJdbc(createTestDataSource());
    repo.setStatementDialect(StatementLoader.HSQLDB_DIALECT);
  }

  /**
   * Test the getBillingRuns method.
   */
  @Test
  public void testGetBillingRuns() {
    List<BillingRun> runs = repo.getBillingRuns();

    assertNotNull("Ensure billing runs were found.", runs);
    assertEquals("Ensure we found the correct number of runs.", 1L, runs.size());
  }
}
