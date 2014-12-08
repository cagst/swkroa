package com.cagst.swkroa.role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

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
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class RoleRepositoryJdbcTest extends BaseTestRepository {
  private RoleRepositoryJdbc repo;

  private User user12;
  private User user13;

  private Role role1;
  private Role role2;
  private Role role3;
  private Role role4;

  @Before
  public void setUp() {
    user12 = new User();
    user12.setUserUID(12L);

    user13 = new User();
    user13.setUserUID(13L);

    role1 = new Role();
    role1.setRoleUID(1L);

    role2 = new Role();
    role2.setRoleUID(2L);

    role3 = new Role();
    role3.setRoleUID(3L);

    role4 = new Role();
    role4.setRoleUID(4L);

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
    assertEquals("Ensure we received the correct number of roles.", 2, roles.size());
  }

  /**
   * Test the getActiveRoles method.
   */
  @Test
  public void testGetActiveRoles() {
    Collection<Role> roles = repo.getActiveRoles();
    assertNotNull("Ensure the roles collection is not null.", roles);
    assertFalse("Ensure the roles collection is not empty.", roles.isEmpty());
    assertEquals("Ensure we found the correct number of roles.", 4, roles.size());
  }

  /**
   * Test the getActivePrivileges method.
   */
  @Test
  public void testGetActivePrivileges() {
    Collection<Privilege> privs = repo.getActivePrivileges();
    assertNotNull("Ensure the privilege collection is not null.", privs);
    assertFalse("Ensure the privilege collection is not empty.", privs.isEmpty());
    assertEquals("Ensure we found the correct number of privileges.", 7, privs.size());
  }

  /**
   * Test the getPrivilegesForRole and not finding any.
   */
  @Test
  public void testGetPrivilegesForRole_NonFound() {
    Collection<Privilege> privs = repo.getPrivilegesForRole(role4);
    assertNotNull("Ensure the privilege collection is not null.", privs);
    assertTrue("Ensure the privilege collection is empty.", privs.isEmpty());
  }

  /**
   * Test the getPrivilegesForRole and finding privileges.
   */
  @Test
  public void testGetPrivilegesForRole_Found() {
    Collection<Privilege> privs1 = repo.getPrivilegesForRole(role1);
    assertNotNull("Ensure the privilege collection is not null.", privs1);
    assertFalse("Ensure the privilege collection is not empty.", privs1.isEmpty());
    assertEquals("Ensure we found the correct number of privileges.", 7, privs1.size());

    Collection<Privilege> privs2 = repo.getPrivilegesForRole(role2);
    assertNotNull("Ensure the privilege collection is not null.", privs2);
    assertFalse("Ensure the privilege collection is not empty.", privs2.isEmpty());
    assertEquals("Ensure we found the correct number of privileges.", 4, privs2.size());
  }

  /**
   * Test the getPrivilegesForUser and not finding any.
   */
  @Test
  public void testGetPrivilegesForUser_NonFound() {
    Collection<Privilege> privs1 = repo.getPrivilegesForUser(user13);
    assertNotNull("Ensure the privilege collection is not null.", privs1);
    assertTrue("Ensure the privilege collection is empty.", privs1.isEmpty());
  }

  /**
   * Test the getPrivilegesForUser and finding privileges.
   */
  @Test
  public void testGetPrivilegesForUser_Found() {
    Collection<Privilege> privs1 = repo.getPrivilegesForUser(user12);
    assertNotNull("Ensure the privilege collection is not null.", privs1);
    assertFalse("Ensure the privilege collection is not empty.", privs1.isEmpty());
    assertEquals("Ensure we have the correct number of privileges.", 5, privs1.size());
  }
}
