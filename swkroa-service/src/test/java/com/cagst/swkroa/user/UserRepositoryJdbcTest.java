package com.cagst.swkroa.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.cagst.swkroa.contact.ContactRepository;
import com.cagst.swkroa.internal.StatementDialect;
import com.cagst.swkroa.member.MemberRepository;
import com.cagst.swkroa.person.Person;
import com.cagst.swkroa.person.PersonRepository;
import com.cagst.swkroa.test.BaseTestRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Test class for UserRepositoryJdbc class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class UserRepositoryJdbcTest extends BaseTestRepository {
  private UserRepositoryJdbc repo;

  private PersonRepository personRepo;

  @Before
  public void setUp() {
    personRepo = mock(PersonRepository.class);

    ContactRepository contactRepo = mock(ContactRepository.class);
    MemberRepository memberRepo = mock(MemberRepository.class);

    when(memberRepo.getMemberByPersonUID(anyLong())).thenReturn(Optional.empty());

    repo = new UserRepositoryJdbc(createTestDataSource(), personRepo, memberRepo, contactRepo);
    repo.setStatementDialect(StatementDialect.HSQLDB);
  }

  /**
   * Tests the getUserByUsername and not finding any User.
   */
  @Test
  public void testGetUserByUsername_NotFound() {
    Optional<User> checkUser = repo.getUserByUsername("billybob");
    assertFalse("Ensure user was not found.", checkUser.isPresent());
  }

  /**
   * Tests the getUserByUsername and finding the User.
   */
  @Test
  public void testGetUserByUsername_Found() {
    Optional<User> checkUser = repo.getUserByUsername("cgaskill");

    assertTrue("Ensure user was found.", checkUser.isPresent());

    User user = checkUser.get();

    assertEquals("Ensure we found the correct user (check username).", "cgaskill", user.getUsername());
    assertEquals("Ensure we found the correct user (check firstname).", "Craig", user.getFirstName());
    assertEquals("Ensure we found the correct user (check lastname).", "Gaskill", user.getLastName());

    assertNull("Ensure we don't have a last sign-in date.", user.getLastSigninDate());
    assertNull("Ensure we don't have a last sign-in ip.", user.getLastSigninIp());
  }

  /**
   * Tests the getUserByUID and not finding the user.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetUserByUID_NotFound() {
    repo.getUserByUID(999L);
  }

  /**
   * Tests the getUserByUID and finding the user.
   */
  @Test
  public void testGetUserByUID_Found() {
    User user = repo.getUserByUID(11L);
    assertNotNull("Ensure user was found.", user);
    assertEquals("Ensure we found the correct user (check username).", "cgaskill", user.getUsername());
    assertEquals("Ensure we found the correct user (check firstname).", "Craig", user.getFirstName());
    assertEquals("Ensure we found the correct user (check lastname).", "Gaskill", user.getLastName());
  }

  /**
   * Tests the getUserByPersonId method and not finding the user.
   */
  @Test
  public void testGetUserByPersonId_NotFound() {
    Optional<User> check = repo.getUserByPersonId(999L);
    assertFalse("Ensure a user was not found.", check.isPresent());
  }

  /**
   * Tests the getUserByPersonId method and finding the user.
   */
  @Test
  public void testGetUserByPersonId_Found() {
    Optional<User> check = repo.getUserByPersonId(1L);
    assertTrue("Ensure a user as found.", check.isPresent());

    User user = check.get();
    assertEquals("Ensure we found the correct user (check username).", "cgaskill", user.getUsername());
    assertEquals("Ensure we found the correct user (check firstname).", "Craig", user.getFirstName());
    assertEquals("Ensure we found the correct user (check lastname).", "Gaskill", user.getLastName());
  }

  /**
   * Tests the getUserByUsername where account will expire but hasn't expired yet.
   */
  @Test
  public void testGetUserByUsername_WillExpired() {
    Optional<User> checkUser = repo.getUserByUsername("temp");
    assertTrue("Ensure user was found.", checkUser.isPresent());

    User user = checkUser.get();
    assertEquals("Ensure we found the correct user (check username).", "temp", user.getUsername());

    assertNotNull("Ensure the user account has an expiration date.", user.getAccountExpiredDate());
    assertTrue("Ensure the user account has not yet expired.", user.isAccountNonExpired());
  }

  /**
   * Tests the getUserByUsername where account has expired.
   */
  @Test
  public void testGetUserByUsername_Expired() {
    Optional<User> checkUser = repo.getUserByUsername("expire");
    assertTrue("Ensure user was found.", checkUser.isPresent());

    User user = checkUser.get();
    assertEquals("Ensure we found the correct user (check username).", "expire", user.getUsername());

    assertNotNull("Ensure the user account has an expiration date.", user.getAccountExpiredDate());
    assertFalse("Ensure the user accounnt has expired.", user.isAccountNonExpired());
  }

  /**
   * Tests the getUserByUsername where account has been locked.
   */
  @Test
  public void testGetUserByUsername_Locked() {
    Optional<User> checkUser = repo.getUserByUsername("locked");
    assertTrue("Ensure user was found.", checkUser.isPresent());

    User user = checkUser.get();
    assertEquals("Ensure we found the correct user (check username).", "locked", user.getUsername());
    assertEquals("Ensure we found the correct user (check sign-in attempts).", 5, user.getSigninAttempts());

    assertNotNull("Ensure the user account has a locked date.", user.getAccountLockedDate());
    assertFalse("Ensure the user account has been lockec.", user.isAccountNonLocked());
  }

  /**
   * Tests the signinAttempt method.
   */
  @Test
  public void testSigninAttempt() {
    Optional<User> checkUser1 = repo.getUserByUsername("cgaskill");
    assertTrue("Ensure user was found.", checkUser1.isPresent());

    User user1 = checkUser1.get();
    assertEquals("Ensure we found the correct user (check username).", "cgaskill", user1.getUsername());
    assertEquals("Ensure we found the correct user (check sign-in attempts).", 0, user1.getSigninAttempts());
    assertEquals("Ensure we found the correct user (check update count).", 0, user1.getUserUpdateCount());

    User user = repo.signinAttempt(user1);
    assertNotNull("Ensure user is still valid.", user);
    assertEquals("Ensure it is the same user.", "cgaskill", user.getUsername());
    assertEquals("Ensure the user's sign-in attempts has been incremented.", 1, user.getSigninAttempts());

    Optional<User> checkUser2 = repo.getUserByUsername("cgaskill");
    assertTrue("Ensure user was found.", checkUser2.isPresent());

    User user2 = checkUser2.get();
    assertEquals("Ensure we found the correct user (check username).", "cgaskill", user2.getUsername());
    assertEquals("Ensure the user's sign-in attempts is correct.", 1, user2.getSigninAttempts());
  }

  /**
   * Tests the lockUserAccount method.
   */
  @Test
  public void testLockUserAccount() throws Exception {
    Optional<User> checkUser1 = repo.getUserByUsername("cgaskill");
    assertTrue("Ensure user was found.", checkUser1.isPresent());

    User user1 = checkUser1.get();
    assertTrue("Ensure user account is not locked.", user1.isAccountNonLocked());
    assertEquals("Ensure the user account has not been updated.", 0, user1.getUserUpdateCount());

    User user2 = repo.lockUserAccount(user1, null, user1);
    assertNotNull("Ensure user is valid.", user2);
    assertFalse("Ensure user account is locked.", user2.isAccountNonLocked());

    Optional<User> checkUser3 = repo.getUserByUsername("cgaskill");
    assertTrue("Ensure user was found.", checkUser3.isPresent());

    User user3 = checkUser3.get();
    assertFalse("Ensure user account is locked.", user3.isAccountNonLocked());
  }

  /**
   * Tests the unlockUserAccount method.
   */
  @Test
  public void testUnlockUserAccount() throws Exception {
    Optional<User> checkUser1 = repo.getUserByUsername("locked");
    assertTrue("Ensure user was found.", checkUser1.isPresent());

    User user1 = checkUser1.get();
    assertFalse("Ensure user account is locked.", user1.isAccountNonLocked());

    User user2 = repo.unlockUserAccount(user1, "Manually unlocked", user1);
    assertNotNull("Ensure user was found.", user2);
    assertTrue("Ensure user account is not locked.", user2.isAccountNonLocked());

    Optional<User> checkUser3 = repo.getUserByUsername("locked");
    assertTrue("Ensure user was found.", checkUser3.isPresent());

    User user3 = checkUser3.get();
    assertTrue("Ensure user account is still not locked.", user3.isAccountNonLocked());
  }

  /**
   * Tests the enableUserAccount method.
   */
  @Test
  public void testEnableUserAccount() {
    Optional<User> checkUser1 = repo.getUserByUsername("disabled");
    assertTrue("Ensure user was found.", checkUser1.isPresent());

    User user1 = checkUser1.get();
    assertFalse("Ensure user account is disabled.", user1.isActive());
    assertFalse("Ensure user account is disabled.", user1.isEnabled());

    User user2 = repo.enableUserAccount(user1, "Enable account", user1);
    assertNotNull("Ensure user was found.", user2);
    assertTrue("Ensure user account is enabled.", user2.isActive());
    assertTrue("Ensure user account is enabled.", user2.isEnabled());

    Optional<User> checkUser3 = repo.getUserByUsername("disabled");
    assertTrue("Ensure user was found.", checkUser3.isPresent());

    User user3 = checkUser3.get();
    assertTrue("Ensure user account is enabled.", user3.isActive());
    assertTrue("Ensure user account is enabled.", user3.isEnabled());
  }

  /**
   * Tests the disableUserAccount method.
   */
  @Test
  public void testDisableUserAccount() {
    Optional<User> checkUser1 = repo.getUserByUsername("enabled");
    assertTrue("Ensure user was found.", checkUser1.isPresent());

    User user1 = checkUser1.get();
    assertTrue("Ensure user account is enabled.", user1.isActive());
    assertTrue("Ensure user account is enabled.", user1.isEnabled());

    User user2 = repo.disableUserAccount(user1, "Enable account", user1);
    assertNotNull("Ensure user was found.", user2);
    assertFalse("Ensure user account is disabled.", user2.isActive());
    assertFalse("Ensure user account is disabled.", user2.isEnabled());

    Optional<User> checkUser3 = repo.getUserByUsername("enabled");
    assertTrue("Ensure user was found.", checkUser3.isPresent());

    User user3 = checkUser3.get();
    assertFalse("Ensure user account is disabled.", user3.isActive());
    assertFalse("Ensure user account is disabled.", user3.isEnabled());
  }

  /**
   * Tests the signinSuccessful method.
   */
  @Test
  public void testSigninSuccessful() {
    String ipAddress = "127.0.0.1";

    Optional<User> checkUser1 = repo.getUserByUsername("temp");
    assertTrue("Ensure user was found.", checkUser1.isPresent());

    User user1 = checkUser1.get();
    assertEquals("Ensure the correct user was found (check username).", "temp", user1.getUsername());
    assertNull("Ensure user has not signed in (no last sign-in date).", user1.getLastSigninDate());
    assertNull("Ensure user has not signed in (no last sign-in ip).", user1.getLastSigninIp());
    assertEquals("Ensure the user has not been updated.", 0, user1.getUserUpdateCount());

    User user = repo.signinSuccessful(user1, ipAddress);
    assertNotNull("Ensure user is valid.", user);
    assertEquals("Ensure it is the correct user (check username).", "temp", user.getUsername());

    Optional<User> checkUser2 = repo.getUserByUsername("temp");
    assertTrue("Ensure user was found.", checkUser2.isPresent());

    User user2 = checkUser2.get();
    assertEquals("Ensure we found the correct user (check username).", "temp", user2.getUsername());
    assertNotNull("Ensure the user has a last sign-in date.", user2.getLastSigninDate());
    assertNotNull("Ensure the user has a last sign-in ip.", user2.getLastSigninIp());
    assertEquals("Ensure the user's last sign-in ip is correct.", ipAddress, user2.getLastSigninIp());
  }

  /**
   * Tests the changeUserPassword method.
   */
  @Test
  public void testChangeUserPassword() {
    Optional<User> checkUser1 = repo.getUserByUsername("temp");
    assertTrue("Ensure user was found.", checkUser1.isPresent());

    User user1 = checkUser1.get();
    assertEquals("Ensure we found the correct user (check username).", "temp", user1.getUsername());
    assertEquals("Ensure we found the correct user (check password).", "password1", user1.getPassword());
    assertTrue("Ensure the user account is temporary.", user1.isPasswordTemporary());
    assertEquals("Ensure the user account has not been updated.", 0, user1.getUserUpdateCount());

    User user2 = repo.changeUserPassword(user1, "newPassword", "message");
    assertNotNull("Ensure user is valid.", user2);
    assertEquals("Ensure it is the correct user (check username).", "temp", user2.getUsername());
    assertFalse("Ensure the user account is no longer temporary.", user2.isPasswordTemporary());
  }

  /**
   * Tests the changeUserPassword method with a null password.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testChangeUserPassword_NullPassword() {
    Optional<User> checkUser1 = repo.getUserByUsername("temp");
    assertTrue("Ensure user was found.", checkUser1.isPresent());

    User user1 = checkUser1.get();
    assertEquals("Ensure we found the correct user (check username).", "temp", user1.getUsername());
    assertEquals("Ensure we found the correct user (check password).", "password1", user1.getPassword());

    repo.changeUserPassword(user1, null, null);
  }

  /**
   * Tests the changeUserPassword method with an empty password
   */
  @Test(expected = IllegalArgumentException.class)
  public void testChangeUserPassword_EmptyPassword() {
    Optional<User> checkUser1 = repo.getUserByUsername("temp");
    assertTrue("Ensure user was found.", checkUser1.isPresent());

    User user1 = checkUser1.get();
    assertEquals("Ensure we found the correct user (check username).", "temp", user1.getUsername());
    assertEquals("Ensure we found the correct user (check password).", "password1", user1.getPassword());

    repo.changeUserPassword(user1, StringUtils.EMPTY, null);
  }

  /**
   * Tests the resetUserPassword method.
   */
  @Test
  public void testResetUserPassword() {
    User user1 = repo.getUserByUID(11);
    assertNotNull("Ensure user was found.", user1);
    assertEquals("Ensure we found the correct user (check username).", "cgaskill", user1.getUsername());
    assertEquals("Ensure we found the correct user (check password).", "password1", user1.getPassword());
    assertFalse("Ensure the user account is not temporary.", user1.isPasswordTemporary());
    assertEquals("Ensure the user account has not been updated.", 0, user1.getUserUpdateCount());

    User user2 = repo.resetUserPassword(user1, "tempPassword", "message", user1);
    assertNotNull("Ensure user is valid.", user2);
    assertEquals("Ensure it is the correct user (check username).", "cgaskill", user2.getUsername());
    assertTrue("Ensure the user account is now temporary.", user2.isPasswordTemporary());
  }

  /**
   * Tests the resetUserPassword method with a null password.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testResetUserPassword_NullPassword() {
    User user1 = repo.getUserByUID(11);
    assertNotNull("Ensure user was found.", user1);
    assertEquals("Ensure we found the correct user (check username).", "cgaskill", user1.getUsername());
    assertEquals("Ensure we found the correct user (check password).", "password1", user1.getPassword());

    repo.resetUserPassword(user1, null, null, user1);
  }

  /**
   * Tests the resetUserPassword method with an empty password
   */
  @Test(expected = IllegalArgumentException.class)
  public void testResetUserPassword_EmptyPassword() {
    User user1 = repo.getUserByUID(11);
    assertNotNull("Ensure user was found.", user1);
    assertEquals("Ensure we found the correct user (check username).", "cgaskill", user1.getUsername());
    assertEquals("Ensure we found the correct user (check password).", "password1", user1.getPassword());

    repo.resetUserPassword(user1, StringUtils.EMPTY, null, user1);
  }

  /**
   * Tests the doesUsernameExist method for a new user.
   */
  @Test
  public void testDoesUsernameExist_New() {
    boolean used1 = repo.doesUsernameExist("cgaskill");
    assertTrue("Ensure the username exists (is being used).", used1);

    boolean used2 = repo.doesUsernameExist("temp");
    assertTrue("Ensure the username exists (is being used).", used2);

    boolean notused = repo.doesUsernameExist("billybob");
    assertFalse("Ensure the username doesn't exist (is not being used).", notused);
  }

  /**
   * Tests the doesUsernameExist method for an existing user (in case we want to allow the user to
   * change their username).
   */
  @Test
  public void testDoesUsernameExist_Existing() {
    User user = repo.getUserByUsername("cgaskill").get();

    boolean used1 = repo.doesUsernameExist("cgaskill", user);
    assertFalse("Ensure the username is not being used (it is the username for the specified user).", used1);

    boolean used2 = repo.doesUsernameExist("temp", user);
    assertTrue("Ensure the username is being used.", used2);

    boolean notused = repo.doesUsernameExist("billybob", user);
    assertFalse("Ensure the username doesn't exist (is not being used).", notused);
  }

  /**
   * Test the saveUser method by inserting a new User.
   */
  @Test
  public void testSaveUser_Insert() {
    User editingUser = new User();
    editingUser.setUserUID(1L);

    User newUser = new User();
    newUser.setLastName("Last");
    newUser.setFirstName("First");
    newUser.setUsername("uname");
    newUser.setPassword("myPwd");

    Person person = new Person();
    person.setPersonUID(1L);

    when(personRepo.savePerson(any(), any())).thenReturn(person);

    User user = repo.saveUser(newUser, editingUser);
    assertNotNull("Ensure we have a new User", user);
    assertTrue("Ensure our new user has an Id", user.getUserUID() > 0L);
    assertTrue("Ensure our new user has a Person Id", user.getPersonUID() > 0L);
  }

  /**
   * Test the saveUser method by updating an existing User.
   */
  @Test
  public void testSaveUser_Update() {
    User editingUser = new User();
    editingUser.setUserUID(1L);

    Person person = new Person();
    person.setPersonUID(1L);

    when(personRepo.savePerson(any(), any())).thenReturn(person);

    User existingUser = new User();
    existingUser.setUserUID(11L);
    existingUser.setPersonUID(person.getPersonUID());
    existingUser.setLastName("Last");
    existingUser.setFirstName("First");
    existingUser.setUsername("uname");
    existingUser.setPassword("myPwd");

    User user = repo.saveUser(existingUser, editingUser);
    assertNotNull("Ensure we have a valid User", user);
    assertEquals("Ensure the update count has been updated", 1, user.getUserUpdateCount());
  }

  /**
   * Test the saveUser method by updating an existing User but failing due to update count mismatch.
   */
  @Test(expected = OptimisticLockingFailureException.class)
  public void testSaveUser_Update_Failed() {
    User editingUser = new User();
    editingUser.setUserUID(1L);

    User existingUser = new User();
    existingUser.setUserUID(11L);
    existingUser.setPersonUID(1L);
    existingUser.setLastName("Last");
    existingUser.setFirstName("First");
    existingUser.setUsername("uname");
    existingUser.setPassword("myPwd");

    Person person = new Person();
    person.setPersonUID(1L);

    when(personRepo.savePerson(any(), any())).thenReturn(person);

    // force failure due to updt_cnt mis-match
    existingUser.setUserUpdateCount(existingUser.getUserUpdateCount() + 1);

    repo.saveUser(existingUser, editingUser);
  }

  /**
   * Test the getAllUsers method.
   */
  @Test
  public void testGetAllUsers() {
    List<User> users = repo.getAllUsers();

    assertNotNull("Ensure we have a valid non-null user list", users);
    assertFalse("Ensure we have users", users.isEmpty());
    assertEquals("Ensure we found the correct number of users", 7, users.size());
  }
}
