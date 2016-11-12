package com.cagst.swkroa.role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Optional;

import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.test.BaseTestRepository;
import com.cagst.swkroa.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for RoleRepositoryJdbc class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class RoleRepositoryJdbcTest extends BaseTestRepository {
  private RoleRepositoryJdbc repo;

  private User user12;

  @Before
  public void setUp() {
    user12 = new User();
    user12.setUserUID(12L);

    repo = new RoleRepositoryJdbc(createTestDataSource());
    repo.setStatementDialect(StatementLoader.HSQLDB_DIALECT);
  }

  /**
   * Test the getRolesForUser method.
   */
  @Test
  public void testGetRolesForUser() {
    Collection<Role> roles = repo.getRolesForUser(user12);
    assertNotNull("Ensure the roles collection is not null.", roles);
    assertFalse("Ensure the roles collection is not empty.", roles.isEmpty());
    assertEquals("Ensure we received the correct number of roles.", 1, roles.size());
  }

  /**
   * Test the getActiveRoles method.
   */
  @Test
  public void testGetActiveRoles() {
    Collection<Role> roles = repo.getActiveRoles();
    assertNotNull("Ensure the roles collection is not null.", roles);
    assertFalse("Ensure the roles collection is not empty.", roles.isEmpty());
    assertEquals("Ensure we found the correct number of roles.", 2, roles.size());
  }

  /**
   * Test the getRoleByKey method and not finding a role.
   */
  @Test
  public void testGetRoleByKey_NotFound() {
    Optional<Role> checkRole = repo.getRoleByKey("ADMIN");
    assertFalse("Ensure the role was not found.", checkRole.isPresent());
  }

  /**
   * Test the getRoleByKey method and finding a role.
   */
  @Test
  public void testGetRoleByKey_Found() {
    Optional<Role> checkRole = repo.getRoleByKey("STAFF");
    assertTrue("Ensure the role was found.", checkRole.isPresent());
    assertEquals("Ensure the role that was found is correct.", "Staff", checkRole.get().getRoleName());
    assertEquals("Ensure the role that was found is correct.", "STAFF", checkRole.get().getRoleKey());
  }

}
