package com.cagst.swkroa.integration;

import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for UserServiceImpl class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class UserServiceImplTest {
  private UserService userService;

  @Before
  public void setUp() {
    ApplicationContext appCtx = new ClassPathXmlApplicationContext("classpath*:/appCtx/**/*.xml");

    userService = (UserService) appCtx.getBean("userService");
  }

  /**
   * Test the loadUserByUsername method and not finding the user.
   */
  @Test(expected = UsernameNotFoundException.class)
  public void testLoadUserByUsername_NotFound() {
    userService.loadUserByUsername("bobbyjoe");
  }

  /**
   * Test the loadUserByUsername method and finding the user.
   */
  @Test
  public void testLoadUserByUsername_Found() {
    UserDetails user = userService.loadUserByUsername("cgaskill");

    assertNotNull("Ensure the user was found.", user);
    assertEquals("Ensure it is the correct user.", "cgaskill", user.getUsername());
    assertTrue("Ensure the account is not locked.", user.isAccountNonLocked());
  }

  /**
   * Test the loadUserByUsername method and locking the user.
   */
  @Test
  public void testLoadUserByUsername_LockUser() {
    UserDetails user1 = userService.loadUserByUsername("cgaskill");
    assertNotNull("Ensure the user was found.", user1);
    assertEquals("Ensure it is the correct user.", "cgaskill", user1.getUsername());
    assertTrue("Ensure the account is not locked.", user1.isAccountNonLocked());

    UserDetails user2 = userService.loadUserByUsername("cgaskill");
    assertNotNull("Ensure the user was found.", user2);
    assertEquals("Ensure it is the correct user.", "cgaskill", user2.getUsername());
    assertTrue("Ensure the account is not locked.", user2.isAccountNonLocked());

    UserDetails user3 = userService.loadUserByUsername("cgaskill");
    assertNotNull("Ensure the user was found.", user3);
    assertEquals("Ensure it is the correct user.", "cgaskill", user3.getUsername());
    assertTrue("Ensure the account is not locked.", user3.isAccountNonLocked());

    UserDetails user4 = userService.loadUserByUsername("cgaskill");
    assertNotNull("Ensure the user was found.", user4);
    assertEquals("Ensure it is the correct user.", "cgaskill", user4.getUsername());
    assertFalse("Ensure the account is not locked.", user4.isAccountNonLocked());
  }

  /**
   * Test the loadUserByUsername method and automatically unlocking a locked user after account
   * locked days has passed.
   */
  @Test
  public void testLoadUserByUsername_AutoUnlockUser() {
    UserDetails user1 = userService.loadUserByUsername("locked");
    assertNotNull("Ensure the user was found.", user1);
    assertEquals("Ensure it is the correct user.", "locked", user1.getUsername());
    assertTrue("Ensure the account is not locked.", user1.isAccountNonLocked());
  }

  /**
   * Test the signinSuccessful method.
   */
  @Test
  public void testSigninSuccessful() {
    User user1 = (User) userService.loadUserByUsername("cgaskill");
    assertNotNull("Ensure the user was found.", user1);
    assertEquals("Ensure it is the correct user.", "cgaskill", user1.getUsername());
    assertEquals("Ensure the signin attempts have been incremented.", 1, user1.getSigninAttempts());

    User user2 = userService.signinSuccessful(user1, "127.0.0.1");
    assertNotNull("Ensure the user was found.", user2);
    assertEquals("Ensure it is the correct user.", "cgaskill", user2.getUsername());
    assertEquals("Ensure the signin attempts have been reset.", 0, user2.getSigninAttempts());
  }

  /**
   * Test the changePassword method but failing because we sent the wrong original password.
   */
  @Test(expected = BadCredentialsException.class)
  public void testChangePassword_WrongOldPassword() {
    User user1 = (User) userService.loadUserByUsername("cgaskill");
    userService.changePassword(user1, "notcorrect", "newpassword", "newpassword");
  }

  /**
   * Test the changePassword method but failing because the new password and confirmation password
   * do not match.
   */
  @Test(expected = BadCredentialsException.class)
  public void testChangePassword_ConfirmationPasswordMismatch() {
    User user1 = (User) userService.loadUserByUsername("cgaskill");
    userService.changePassword(user1, "password1", "newPassword1", "newPassword2");
  }

  /**
   * Test the changePassword method but failing because the new password is the same as the old
   * password.
   */
  @Test(expected = BadCredentialsException.class)
  public void testChangePassword_SameAsOldPassword() {
    User user1 = (User) userService.loadUserByUsername("cgaskill");
    userService.changePassword(user1, "password1", "password1", "password1");
  }

  /**
   * Test the changePassword method but failing because the new password is the same as the
   * username.
   */
  @Test(expected = BadCredentialsException.class)
  public void testChangePassword_SameAsUsername() {
    User user1 = (User) userService.loadUserByUsername("cgaskill");
    userService.changePassword(user1, "password1", "cgaskill", "cgaskill");
  }

  /**
   * Test the changePassword method and succeeding.
   */
  @Test
  public void testChangePassword_Changed() {
    User user1 = (User) userService.loadUserByUsername("cgaskill");
    assertNull("Ensure the changed password date is null (hasn't changed).", user1.getPasswordChangedDate());

    User user2 = userService.changePassword(user1, "password1", "newpassword", "newpassword");

    assertNotNull("Ensure the user is not null.", user2);
    assertEquals("Ensure it is the same user.", "cgaskill", user2.getUsername());
    assertTrue("Ensure it is the same user.", user1.equals(user2));
    assertNotNull("Ensure the changed password date is not null (has changed).", user2.getPasswordChangedDate());
  }

  /**
   * Test the unlockAccount method.
   */
  @Test
  public void testUnlockAccount() {
    User user1 = (User) userService.loadUserByUsername("newlylocked");
    assertNotNull("Ensure the user is not null.", user1);
    assertFalse("Ensure the user account is locked.", user1.isAccountNonLocked());
    assertNotNull("Ensure the user account is locked (has a locked date).", user1.getAccountLockedDate());

    User user2 = userService.unlockAccount(user1, user1);
    assertNotNull("Ensure the user is not null.", user2);
    assertTrue("Ensure the user account is not locked.", user2.isAccountNonLocked());
    assertNull("Ensure the user account is not locked (does not have a locked date).", user2.getAccountLockedDate());
  }

  /**
   * Test the doesUsernameExist method.
   */
  @Test
  public void testDoesUsernameExist() {
    assertTrue("Ensure username does exists.", userService.doesUsernameExist("cgaskill"));
    assertFalse("Ensure username doesn't exist.", userService.doesUsernameExist("billybob"));
  }
}
