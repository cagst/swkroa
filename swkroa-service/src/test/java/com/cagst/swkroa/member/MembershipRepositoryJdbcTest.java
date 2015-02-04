package com.cagst.swkroa.member;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.test.BaseTestRepository;
import com.cagst.swkroa.user.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Test class for the MemberRepositoryJdbc class.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class MembershipRepositoryJdbcTest extends BaseTestRepository {
  private MembershipRepositoryJdbc repo;

  private CodeValueRepository codeValueRepo;

  private final CodeValue associate = new CodeValue();
  private final CodeValue regular = new CodeValue();
  private final CodeValue family = new CodeValue();
  private final CodeValue closeReason = new CodeValue();

  private User user;

  @Before
  public void setUp() throws Exception {
    user = new User();
    user.setUserUID(1L);

    MemberRepository memberRepo = Mockito.mock(MemberRepository.class);

    codeValueRepo = Mockito.mock(CodeValueRepository.class);

    associate.setCodeValueUID(1L);
    associate.setDisplay("Associate Membership");

    regular.setCodeValueUID(2L);
    regular.setDisplay("Regular Membership");

    family.setCodeValueUID(3L);
    family.setDisplay("Family Membership");

    closeReason.setCodeValueUID(99L);
    closeReason.setDisplay("Close Reason");

    Mockito.when(codeValueRepo.getCodeValueByUID(1L)).thenReturn(associate);
    Mockito.when(codeValueRepo.getCodeValueByUID(2L)).thenReturn(regular);
    Mockito.when(codeValueRepo.getCodeValueByUID(3L)).thenReturn(family);

    repo = new MembershipRepositoryJdbc(createTestDataSource(), memberRepo, codeValueRepo);
    repo.setStatementDialect(StatementLoader.HSQLDB_DIALECT);
  }

  /**
   * Test the getMembershipByUID method and not finding the membership.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetMembershipByUID_NotFound() {
    repo.getMembershipByUID(999L);
  }

  /**
   * Test the getMembershipByUID method and finding a membership.
   */
  @Test
  public void testGetMembershipByUID_Found() {
    Membership membership = repo.getMembershipByUID(1L);
    assertNotNull("Ensure we found the membership.", membership);
  }

  /**
   * Test the getMembershipsByName method and not finding any.
   */
  @Test
  public void testGetMembershipsByName_NoneFound() {
    Collection<Membership> memberships = repo.getMembershipsByName("zzz", MembershipStatus.ACTIVE, MembershipBalance.ALL);

    assertNotNull("Ensure the memberships collection is not null.", memberships);
    assertTrue("Ensure the memberships collection is empty.", memberships.isEmpty());
  }

  /**
   * Test the getMembershipsByName method and finding Memberships.
   */
  @Test
  public void testGetMembershipsByName_Found() {
    Collection<Membership> memberships1 = repo.getMembershipsByName("dori", MembershipStatus.ACTIVE, MembershipBalance.ALL);

    assertNotNull("Ensure the memberships collection is not null.", memberships1);
    assertFalse("Ensure the memberships collection is not empty.", memberships1.isEmpty());
    assertEquals("Ensure we found the correct number of memberships.", 2, memberships1.size());

    Collection<Membership> memberships2 = repo.getMembershipsByName("reg", MembershipStatus.ACTIVE, MembershipBalance.ALL);

    assertNotNull("Ensure the memberships collection is not null.", memberships2);
    assertFalse("Ensure the memberships collection is not empty.", memberships2.isEmpty());
    assertEquals("Ensure we found the correct number of memberships.", 1, memberships2.size());
  }

  /**
   * Test the getMemberships method for Active memberships.
   */
  @Test
  public void testGetMemberships_Active_Found() {
    Collection<Membership> memberships = repo.getMemberships(MembershipStatus.ACTIVE, MembershipBalance.ALL);

    assertNotNull("Ensure the memberships collection is not null!", memberships);
    assertFalse("Ensure the memberships collection is not empty!", memberships.isEmpty());
    assertEquals("Ensure we found the correct number of memberships!", 4, memberships.size());
  }

  /**
   * Test the getDelinquentMemberships method.
   */
  @Test
  public void testGetMemberships_Delinquent_Found() {
    Collection<Membership> memberships = repo.getMemberships(MembershipStatus.ACTIVE, MembershipBalance.DELINQUENT);

    assertNotNull("Ensure the memberships collection is not null!", memberships);
    assertFalse("Ensure the memberships collection is not empty!", memberships.isEmpty());
    assertEquals("Ensure we found the correct number of memberships!", 2, memberships.size());
  }

  /**
   * Test the getMembershipsDueInXDays and not finding any.
   */
  @Test
  public void testGetMemberships_DueInXDays_NoneFound() {
    DateTime currentTime = new DateTime(2013, 2, 15, 0, 0);
    DateTimeUtils.setCurrentMillisFixed(currentTime.getMillis());

    Collection<Membership> memberships = repo.getMembershipsDueInXDays(30);
    assertNotNull("Ensure the memberships collection is not null.", memberships);
    assertTrue("Ensure the memberships collection is empty.", memberships.isEmpty());

    DateTimeUtils.setCurrentMillisSystem();
  }

  /**
   * Test the getMembershipsDueInXDays and finding 1.
   */
  @Test
  public void testGetMemberships_DueInXDays_Found1() {
    DateTime currentTime = new DateTime(2014, 2, 15, 0, 0);
    DateTimeUtils.setCurrentMillisFixed(currentTime.getMillis());

    Collection<Membership> memberships = repo.getMembershipsDueInXDays(30);
    assertNotNull("Ensure the memberships collection is not null.", memberships);
    assertFalse("Ensure the memberships collection is not empty.", memberships.isEmpty());
    assertEquals("Ensure the correct number of memberships are found due.", 1, memberships.size());

    DateTimeUtils.setCurrentMillisSystem();
  }

  /**
   * Test the getMembershipsDueInXDays and finding 2.
   */
  @Test
  public void testGetMemberships_DueInXDays_Found2() {
    DateTime currentTime = new DateTime(2015, 2, 15, 0, 0);
    DateTimeUtils.setCurrentMillisFixed(currentTime.getMillis());

    Collection<Membership> memberships = repo.getMembershipsDueInXDays(30);
    assertNotNull("Ensure the memberships collection is not null.", memberships);
    assertFalse("Ensure the memberships collection is not empty.", memberships.isEmpty());
    assertEquals("Ensure the correct number of memberships are found due.", 2, memberships.size());

    DateTimeUtils.setCurrentMillisSystem();
  }

  /**
   * Test the saveMembership method and inserting a membership.
   */
  @Test
  @Ignore
  public void testSaveMembership_Insert() {
    Collection<Membership> memberships1 = repo.getMemberships(null, null);

    assertNotNull("Ensure the memberships collection is not null!", memberships1);
    assertFalse("Ensure the memberships collection is not empty!", memberships1.isEmpty());
    assertEquals("Ensure we found the correct number of memberships!", 4, memberships1.size());

    DateTime now = new DateTime();

    Membership builder = new Membership();
    builder.setNextDueDate(new DateTime(now.plusYears(1).toDate()));
    builder.setEntityType(codeValueRepo.getCodeValueByUID(1L));

    MemberType type = new MemberType();
    type.setMemberTypeUID(1L);

    Member member = new Member();
    member.setMemberType(type);
    builder.addMember(member);

    Membership membership = repo.saveMembership(builder, user);
    assertNotNull("Ensure the membership was created.", membership);
    assertTrue("Ensure the membership has a valid uid.", membership.getMembershipUID() > 0L);

    Collection<Membership> memberships2 = repo.getMemberships(null, null);

    assertNotNull("Ensure the memberships collection is not null!", memberships2);
    assertFalse("Ensure the memberships collection is not empty!", memberships2.isEmpty());
    assertEquals("Ensure we found the correct number of memberships!", 5, memberships2.size());
  }

  /**
   * Test the saveMembership method and updating a membership.
   */
  @Test
  public void testSaveMembership_Update() {
    Membership membership1 = repo.getMembershipByUID(1L);
    assertNotNull("Ensure we found the membership.", membership1);

    membership1.setEntityType(family);

    Membership membership2 = repo.saveMembership(membership1, user);
    assertNotNull("Ensure we found the membership.", membership1);
    assertEquals("Ensure it is the same membership.", membership1.getMembershipUID(), membership2.getMembershipUID());
    assertEquals("Ensure it has been updated.", family, membership2.getEntityType());
    assertEquals("Ensure it has been updated.", 1, membership2.getMembershipUpdateCount());
  }

  /**
   * Test the saveMembership method and updating a membership but failing (due to an invalid update count).
   */
  @Test(expected = OptimisticLockingFailureException.class)
  public void testSaveMembership_Update_Failed() {
    Membership membership1 = repo.getMembershipByUID(1L);
    assertNotNull("Ensure we found the membership.", membership1);

    membership1.setEntityType(family);
    membership1.setMembershipUpdateCount(1L);

    repo.saveMembership(membership1, user);
  }

  /**
   * Test the closeMemberships method and failing because of no memberships.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCloseMemberships_Failed_NoMemberships() {
    repo.closeMemberships(new ArrayList<Long>(), closeReason, null, user);
  }

  /**
   * Test the closeMemberships method and failing because of no close reason.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCloseMemberships_Failed_NoCloseReason() {
    List<Long> membershipIds = new ArrayList<Long>(2);

    membershipIds.add(1L);
    membershipIds.add(2L);

    repo.closeMemberships(membershipIds, null, null, user);
  }

  /**
   * Test the closeMemberships method and succeeding.
   */
  @Test
  public void testCloseMemberships_Succeeded() {
    List<Long> membershipIds = new ArrayList<Long>(2);

    membershipIds.add(1L);
    membershipIds.add(2L);

    int closedMembers = repo.closeMemberships(membershipIds, closeReason, null, user);

    assertEquals("Ensure the correct number of memberships were closed", 2, closedMembers);
  }
}
