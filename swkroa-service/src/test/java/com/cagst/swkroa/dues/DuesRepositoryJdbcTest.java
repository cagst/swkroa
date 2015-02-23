package com.cagst.swkroa.dues;

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
 * Test class for {@link DuesRepositoryJdbc} class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class DuesRepositoryJdbcTest extends BaseTestRepository {
  private DuesRepositoryJdbc repo;

  @Before
  public void setUp() {
    repo = new DuesRepositoryJdbc(createTestDataSource());
    repo.setStatementDialect(StatementLoader.HSQLDB_DIALECT);
  }

  /**
   * Test the getDuesRuns method.
   */
  @Test
  public void testGetDuesRuns() {
    List<DuesRun> runs = repo.getDuesRuns();

    assertNotNull("Ensure billing runs were found.", runs);
    assertEquals("Ensure we found the correct number of runs.", 1L, runs.size());
  }
}
