package com.cagst.swkroa.integration;

import java.util.Set;

import com.cagst.swkroa.member.MembershipService;
import com.cagst.swkroa.user.User;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test class for the {@link com.cagst.swkroa.member.MembershipServiceImpl} class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class MembershipServiceImplTest {
  private User user;

  private MembershipService membershipService;

  @Before
  public void setUp() {
    user = new User();
    user.setUserUID(1L);

    ApplicationContext appCtx = new ClassPathXmlApplicationContext("classpath*:/appCtx/**/*.xml");

    membershipService = (MembershipService) appCtx.getBean("membershipService");
  }

  /**
   * Test the createBillingInvoicesForMemberships method.
   */
  @Test
  public void testCreateBillingInvoicesForMemberships() {
    DateTime transDate = new DateTime(2015, 3, 1, 0, 0);
    String transDesc = "2015 - 2016 Dues";
    String transMemo = "Due upon receipt";

    Set<Long> membershipIds = Sets.newHashSet(1L, 2L);

    membershipService.billMemberships(transDate, transDesc, transMemo, membershipIds, user);
  }
}
