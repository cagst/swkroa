package com.cagst.swkroa.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import com.cagst.swkroa.test.BaseTestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for the {@link SystemRepositoryJdbc} class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class SystemRepositoryJdbcTest extends BaseTestRepository {
  private SystemRepositoryJdbc repo;

  @Before
  public void setUp() {
    repo = new SystemRepositoryJdbc(createTestDataSource());
  }

  /**
   * Test the getSystemSettings method.
   */
  @Test
  public void testGetSystemSettings() {
    Collection<SystemSetting> settings = repo.getSystemSettings();

    assertNotNull("Ensure the settings collection is not null.", settings);
    assertFalse("ENsure the settings collection is not empty.", settings.isEmpty());
    assertEquals("Ensure we received the correct number of settings.", 7, settings.size());
  }
}
