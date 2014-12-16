package com.cagst.swkroa.user;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.cagst.swkroa.security.SecurityPolicy;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for User class.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class UserTest {
  private SecurityPolicy securityPolicy30;
  private SecurityPolicy securityPolicy0;

  @Before
  public void setUp() {
    securityPolicy30 = new SecurityPolicy();
    securityPolicy30.setPasswordExpiryInDays(30);

    securityPolicy0 = new SecurityPolicy();
    securityPolicy0.setPasswordExpiryInDays(0);
  }

  /**
   * Test the isAccountNonLocked method.
   */
  @Test
  public void testIsAccountNonLocked() {
    User user1 = new User();
    user1.setUserUID(10L);
    assertTrue("Ensure the user account is not locked!", user1.isAccountNonLocked());

    User user2 = new User();
    user2.setAccountedLockedDate(new DateTime(2010, 01, 15, 13, 30));
    assertFalse("Ensure the user account is locked!", user2.isAccountNonLocked());

    User user3 = new User();
    user3.setAccountedLockedDate(new DateTime(2020, 02, 25, 14, 45));
    assertTrue("Ensure the user account is not locked due to a future locked date!", user3.isAccountNonLocked());
  }

  /**
   * Test the isAccountNonExpired method.
   */
  @Test
  public void testIsAccountNonExpired() {
    User user1 = new User();
    user1.setUserUID(10L);
    assertTrue("Ensure the user account has not expired!", user1.isAccountNonExpired());

    User user2 = new User();
    user2.setAccountExpiredDate(new DateTime(2010, 01, 15, 13, 30));
    assertFalse("Ensure the user account has expired!", user2.isAccountNonExpired());

    User user3 = new User();
    user3.setAccountExpiredDate(new DateTime(2020, 02, 25, 14, 45));
    assertTrue("Ensure the user account has not expired due to a future expiry date!", user3.isAccountNonExpired());
  }

  /**
   * Test the isCredentialsNonExpired method with password expiry days.
   */
  @Test
  public void testIsCredentialsNonExpired_PasswordExpiryDays() {
    User user1 = new User();
    user1.setUserUID(10L);
    user1.setSecurityPolicy(securityPolicy30);
    assertTrue("Ensure the user credentials has expired!", user1.isPasswordExpired());

    User user2 = new User();
    user2.setUserUID(20L);
    user2.setSecurityPolicy(securityPolicy30);
    user2.setPasswordChangedDate(new DateTime(2013, 04, 15, 13, 30));
    assertTrue("Ensure the user credentials has expired!", user2.isPasswordExpired());

    User user3 = new User();
    user3.setUserUID(30L);
    user3.setSecurityPolicy(securityPolicy30);
    user3.setPasswordChangedDate(new DateTime(2020, 02, 25, 14, 45));
    assertFalse("Ensure the user credentials has not expired due to a future changed password date!",
        user3.isPasswordExpired());
  }

  /**
   * Test the isCredentialsNonExpired method with no password expiry days.
   */
  @Test
  public void testIsCredentialsNonExpired_NoPasswordExpiryDays() {
    User user1 = new User();
    user1.setUserUID(10L);
    user1.setSecurityPolicy(securityPolicy0);
    assertTrue("Ensure the user credentials has not expired!", user1.isCredentialsNonExpired());

    User user2 = new User();
    user2.setPasswordChangedDate(new DateTime(2013, 04, 15, 13, 30));
    assertTrue("Ensure the user credentials has not expired!", user2.isCredentialsNonExpired());

    User user3 = new User();
    user3.setPasswordChangedDate(new DateTime(2020, 02, 25, 14, 45));
    assertTrue("Ensure the user credentials has not expired due to a future changed password date!",
        user3.isCredentialsNonExpired());
  }
}
