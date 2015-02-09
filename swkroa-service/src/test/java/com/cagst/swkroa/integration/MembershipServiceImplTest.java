package com.cagst.swkroa.integration;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyObject;

import java.util.Set;

import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.member.MemberRepository;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.member.MembershipRepository;
import com.cagst.swkroa.member.MembershipService;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.transaction.TransactionRepository;
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
  private CodeValueRepository codeValueRepo;
  private MembershipRepository membershipRepo;
  private MemberRepository memberRepo;
  private TransactionRepository transactionRepo;

  @Before
  public void setUp() {
    user = new User();
    user.setUserUID(1L);

    ApplicationContext appCtx = new ClassPathXmlApplicationContext("classpath*:/appCtx/**/*.xml");

    membershipService = (MembershipService) appCtx.getBean("membershipService");
    codeValueRepo = (CodeValueRepository) appCtx.getBean("codeValueRepo");
    membershipRepo = (MembershipRepository) appCtx.getBean("membershipRepo");
    memberRepo = (MemberRepository) appCtx.getBean("memberRepo");
    transactionRepo = (TransactionRepository) appCtx.getBean("transactionRepo");
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

    membershipService.createBillingInvoicesForMemberships(transDate, transDesc, transMemo, membershipIds, user);

    verify(codeValueRepo.getCodeValueByMeaning(anyString()), times(3));
    verify(membershipRepo.getMembershipByUID(anyLong()), times(2));
    verify(memberRepo.getMembersForMembership((Membership)anyObject()), times(2));
    verify(transactionRepo.saveTransaction((Transaction)anyObject(), user), times(2));
    verify(membershipRepo.saveMembership((Membership) anyObject(), user), times(2));
  }
}
