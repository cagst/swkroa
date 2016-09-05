package com.cagst.swkroa.integration;

import com.cagst.swkroa.security.SecurityPolicy;
import com.cagst.swkroa.security.SecurityService;
import com.cagst.swkroa.security.SecurityServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for the {@link SecurityServiceImpl} class.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class SecurityServiceImplTest {
  private SecurityService securityService;

  /**
   * Test the getSecurityPolicy method and getting the default security policy with no environment
   * properties set.
   */
  @Test
  public void testGetSecurityPolicy_Default_NoEnvironment() {
    securityService = new SecurityServiceImpl();

    SecurityPolicy securityPolicy = securityService.getSecurityPolicy(null);
    assertNotNull(securityPolicy);
    assertEquals(5, securityPolicy.getMaxSigninAttempts());
    assertEquals(15, securityPolicy.getTimeoutPeriodInMinutes());
    assertEquals(90, securityPolicy.getPasswordExpiryInDays());
    assertEquals(7, securityPolicy.getAccountLockedDays());
  }

  /**
   * Test the getSecurityPolicy method and getting the default security policy with environment
   * properties set.
   */
  @Test
  public void testGetSecurityPolicy_Default_Environment() {
    ApplicationContext appCtx = new ClassPathXmlApplicationContext("classpath*:/appCtx/**/*.xml");
    securityService = (SecurityService) appCtx.getBean("securityService");

    SecurityPolicy securityPolicy = securityService.getSecurityPolicy(null);
    assertNotNull(securityPolicy);
    assertEquals(3, securityPolicy.getMaxSigninAttempts());
    assertEquals(5, securityPolicy.getTimeoutPeriodInMinutes());
    assertEquals(30, securityPolicy.getPasswordExpiryInDays());
    assertEquals(2, securityPolicy.getAccountLockedDays());
  }
}
