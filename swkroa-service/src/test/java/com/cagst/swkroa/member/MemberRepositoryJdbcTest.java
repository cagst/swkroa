package com.cagst.swkroa.member;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.cagst.swkroa.contact.ContactRepository;
import com.cagst.swkroa.county.County;
import com.cagst.swkroa.county.CountyRepository;
import com.cagst.swkroa.internal.StatementDialect;
import com.cagst.swkroa.person.Person;
import com.cagst.swkroa.person.PersonRepository;
import com.cagst.swkroa.test.BaseTestRepository;
import com.cagst.swkroa.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Test class for the MemberRepositoryJdbc class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class MemberRepositoryJdbcTest extends BaseTestRepository {
  private MemberRepositoryJdbc repo;

  private PersonRepository personRepo;
  private MemberTypeRepository memberTypeRepo;

  private User user;

  private County svCounty;

  @Before
  public void setUp() throws Exception {
    user = new User();
    user.setUserUID(1L);

    personRepo = mock(PersonRepository.class);

    CountyRepository countyRepo = mock(CountyRepository.class);
    ContactRepository contactRepo = mock(ContactRepository.class);

    memberTypeRepo = mock(MemberTypeRepository.class);

    MemberType regularMember = new MemberType();
    regularMember.setMemberTypeUID(1L);
    regularMember.setMemberTypeMeaning(MemberType.REGULAR);
    regularMember.setMemberTypeDisplay("Regular");

    MemberType familyHead = new MemberType();
    familyHead.setMemberTypeUID(2L);
    familyHead.setMemberTypeMeaning(MemberType.FAMILY_HEAD);
    familyHead.setMemberTypeDisplay("Family Head");

    when(memberTypeRepo.getMemberTypeByUID(1L)).thenReturn(regularMember);
    when(memberTypeRepo.getMemberTypeByUID(2L)).thenReturn(familyHead);

    when(memberTypeRepo.getMemberTypeByMeaning(MemberType.REGULAR)).thenReturn(regularMember);
    when(memberTypeRepo.getMemberTypeByMeaning(MemberType.FAMILY_HEAD)).thenReturn(familyHead);

    svCounty = new County();
    svCounty.setCountyUID(1L);
    svCounty.setState("KS");
    svCounty.setCountyCode("SV");
    svCounty.setCountyName("Stevens");

    when(countyRepo.getCountyByUID(1L)).thenReturn(svCounty);

    repo = new MemberRepositoryJdbc(createTestDataSource(), personRepo, memberTypeRepo, countyRepo, contactRepo);
    repo.setStatementDialect(StatementDialect.HSQLDB);
  }

  /**
   * Test the getMembersForMembership method and not finding any members.
   */
  @Test
  public void testGetMembersForMembership_NoneFound() {
    Membership membership = new Membership();
    membership.setMembershipUID(6L);

    List<Member> members = repo.getMembersForMembership(membership);
    assertNotNull("Ensure the members collection is not null.", members);
    assertTrue("Ensure the members collection is empty.", members.isEmpty());
  }

  /**
   * Test the getMembersForMembership method and finding members.
   */
  @Test
  public void testGetMembersForMembership_Found() {
    Membership membership = new Membership();
    membership.setMembershipUID(2L);

    List<Member> members = repo.getMembersForMembership(membership);
    assertNotNull("Ensure the members collection is not null.", members);
    assertFalse("Ensure the members collection is not empty.", members.isEmpty());
    assertEquals("Ensure we found the correct number of members.", 2, members.size());
  }

  /**
   * Test the getMembersByName method and not finding any.
   */
  @Test
  public void testGetMembersByName_NoneFound() {
    List<Member> memberships = repo.getMembersByName("zzz", Status.ACTIVE, 0, 5);

    assertNotNull("Ensure the memberships collection is not null.", memberships);
    assertTrue("Ensure the memberships collection is empty.", memberships.isEmpty());
  }

  /**
   * Test the getMembersByName method and finding Memberships.
   */
  @Test
  public void testGetMembersByName_Found() {
    List<Member> memberships1 = repo.getMembersByName("dori", Status.ACTIVE, 0, 5);

    assertNotNull("Ensure the memberships collection is not null.", memberships1);
    assertFalse("Ensure the memberships collection is not empty.", memberships1.isEmpty());
    assertEquals("Ensure we found the correct number of memberships.", 3, memberships1.size());

    List<Member> memberships2 = repo.getMembersByName("reg", Status.ACTIVE, 0, 5);

    assertNotNull("Ensure the memberships collection is not null.", memberships2);
    assertFalse("Ensure the memberships collection is not empty.", memberships2.isEmpty());
    assertEquals("Ensure we found the correct number of memberships.", 1, memberships2.size());
  }

  /**
   * Test the getMemberByUID method and not finding the member.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetMemberByUID_NotFound() {
    repo.getMemberByUID(999L);
  }

  /**
   * Test the getMemberByUID method and finding the member.
   */
  @Test
  public void testGetMemberByUID_Found() {
    Member member = repo.getMemberByUID(2L);

    assertNotNull("Ensure the member is not NULL!", member);
    assertEquals("Ensure we found the correct member!", 2, member.getMemberUID());
    assertNotNull("Ensure we have a valid MemberType.", member.getMemberType());
    assertEquals("Ensure it is the correct MemberType.", MemberType.REGULAR, member.getMemberType()
        .getMemberTypeMeaning());
  }

  /**
   * Test the getMemberByPersonUID method and not finding the member.
   */
  @Test
  public void testGetMemberByPersonUID_NotFound() {
    Optional<Member> check = repo.getMemberByPersonUID(999L);
    assertFalse("Ensure the Member was not found.", check.isPresent());
  }

  /**
   * Test the getMemberByUID method and finding the member.
   */
  @Test
  public void testGetMemberByPersonUID_Found() {
    Optional<Member> check = repo.getMemberByPersonUID(11L);

    assertTrue("Ensure the member was found.", check.isPresent());

    Member member = check.get();
    assertEquals("Ensure we found the correct member!", 2, member.getMemberUID());
    assertNotNull("Ensure we have a valid MemberType.", member.getMemberType());
    assertEquals("Ensure it is the correct MemberType.", MemberType.REGULAR, member.getMemberType()
        .getMemberTypeMeaning());
  }

  /**
   * Test the getMemberByOwnerId method and not finding the member.
   */
  @Test
  public void testGetMemberByOwnerId_NotFound() {
    Optional<Member> checkMember = repo.getMemberByOwnerId("BADOWNERID");

    assertNotNull("Ensure a valid optional is returned", checkMember);
    assertFalse("Ensure a member wasn't found", checkMember.isPresent());
  }

  /**
   * Test the getMemberByOwnerId method and finding a member.
   */
  @Test
  public void testGetMemberByOwnerId_Found() {
    Optional<Member> checkMember = repo.getMemberByOwnerId("DORDUD0");

    assertNotNull("Ensure a valid optional is returned", checkMember);
    assertTrue("Ensure a member was found", checkMember.isPresent());

    Member member = checkMember.get();
    assertEquals("Ensure we found the correct member!", 2, member.getMemberUID());
    assertNotNull("Ensure we have a valid MemberType.", member.getMemberType());
    assertEquals("Ensure it is the correct MemberType.", MemberType.REGULAR, member.getMemberType()
        .getMemberTypeMeaning());

  }

  /**
   * Test the getMembershipCountiesForMembership and not finding any membership counties.
   */
  @Test
  public void testGetMembershipCountiesForMembership_NoneFound() {
    Membership membership = new Membership();
    membership.setMembershipUID(4L);

    List<MembershipCounty> counties = repo.getMembershipCountiesForMembership(membership);
    assertNotNull("Ensure the membership counties collection is not null.", counties);
    assertTrue("Ensure the membership counties collection is empty.", counties.isEmpty());
  }

  /**
   * Test the getMembershipCountiesForMembership and finding membership counties.
   */
  @Test
  public void testGetMembershipCountiesForMembership_Found() {
    Membership membership = new Membership();
    membership.setMembershipUID(1L);

    List<MembershipCounty> counties = repo.getMembershipCountiesForMembership(membership);
    assertNotNull("Ensure the membership counties collection is not null.", counties);
    assertFalse("Ensure the membership counties collection is not empty.", counties.isEmpty());
    assertEquals("Ensure we found the correct number of membership counties.", 2, counties.size());
  }

  /**
   * Test the getMembershipCountyByUID and not finding the membership county.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetMembershipCountyByUID_NotFound() {
    repo.getMembershipCountyByUID(999L);
  }

  /**
   * Test the getMembershipCountyByUID and finding the membership county.
   */
  @Test
  public void testGetMembershipCountyByUID_Found() {
    MembershipCounty county = repo.getMembershipCountyByUID(3L);
    assertNotNull("Ensure we found the membership county.", county);
  }

  /**
   * Test the generateOwnerId method and failing.
   */
  @Test
  public void testGenerateOwnerId_Failed() {
    try {
      repo.generateOwnerId(null, "");
      fail("Should have generated an exception.");
    } catch (IllegalArgumentException ex) {
      assertNotNull("Ensure an exception was thrown.", ex);
    }

    try {
      repo.generateOwnerId("1", "12");
      fail("Should have generated an exception.");
    } catch (IllegalArgumentException ex) {
      assertNotNull("Ensure an exception was thrown.", ex);
    }

    try {
      repo.generateOwnerId("12", "12");
      fail("Should have generated an exception.");
    } catch (IllegalArgumentException ex) {
      assertNotNull("Ensure an exception was thrown.", ex);
    }
  }

  /**
   * Test the generateOwnerId method and succeeding.
   */
  @Test
  public void testGenerateOwnerId_Success() {
    String ownerId1 = repo.generateOwnerId("Craig", "Gaskill");
    assertNotNull("Ensure an OwnerId was created.", ownerId1);
    assertEquals("Ensure it was created correctly.", "GASCRA0", ownerId1);

    String ownerId2 = repo.generateOwnerId("Michael", "Mouse");
    assertNotNull("Ensure an OwnerId was created.", ownerId1);
    assertEquals("Ensure it was created correctly.", "MOUMIC1", ownerId2);
  }

  /**
   * Test the saveMember method and inserting a member that is a Person.
   */
  @Test
  public void testSaveMember_Insert_Person() {
    Membership membership = new Membership();
    membership.setMembershipUID(2L);

    List<Member> members1 = repo.getMembersForMembership(membership);
    assertNotNull("Ensure the members collection is not null.", members1);
    assertFalse("Ensure the members collection is not empty.", members1.isEmpty());
    assertEquals("Ensure we found the correct number of members.", 2, members1.size());

    MemberType type = memberTypeRepo.getMemberTypeByMeaning(MemberType.REGULAR);

    Person person = new Person();
    person.setFirstName("FNAME");
    person.setLastName("LNAME");

    Member builder = new Member();
    builder.setPerson(person);
    builder.setOwnerIdent("LNAFNA0");
    builder.setJoinDate(LocalDate.now());
    builder.setMemberType(type);

    Member member = repo.saveMember(builder, membership, user);
    assertNotNull("Ensure we have a member.", member);
    assertTrue("Ensure the member has a valid uid.", member.getMemberUID() > 0L);

    List<Member> members2 = repo.getMembersForMembership(membership);
    assertNotNull("Ensure the members collection is not null.", members2);
    assertFalse("Ensure the members collection is not empty.", members2.isEmpty());
    assertEquals("Ensure we found the correct number of members.", 3, members2.size());
  }

  /**
   * Test the saveMember method and inserting a member that is a Company.
   */
  @Test
  public void testSaveMember_Insert_Company() {
    Person person = new Person();
    person.setPersonUID(1L);

    when(personRepo.savePerson(any(), any())).thenReturn(person);

    Membership membership = new Membership();
    membership.setMembershipUID(2L);

    List<Member> members1 = repo.getMembersForMembership(membership);
    assertNotNull("Ensure the members collection is not null.", members1);
    assertFalse("Ensure the members collection is not empty.", members1.isEmpty());
    assertEquals("Ensure we found the correct number of members.", 2, members1.size());

    MemberType type = memberTypeRepo.getMemberTypeByMeaning(MemberType.REGULAR);

    Member builder = new Member();
    builder.setOwnerIdent("LNAFNA1");
    builder.setJoinDate(LocalDate.now());
    builder.setMemberType(type);
    builder.setPerson(person);

    Member member = repo.saveMember(builder, membership, user);
    assertNotNull("Ensure we have a member.", member);
    assertTrue("Ensure the member has a valid uid.", member.getMemberUID() > 0L);

    List<Member> members2 = repo.getMembersForMembership(membership);
    assertNotNull("Ensure the members collection is not null.", members2);
    assertFalse("Ensure the members collection is not empty.", members2.isEmpty());
    assertEquals("Ensure we found the correct number of members.", 3, members2.size());
  }

  /**
   * Test the saveMember method and updating a member.
   */
  @Test
  public void testSaveMember_Update() {
    Person person = new Person();
    person.setPersonUID(1L);

    when(personRepo.savePerson(any(), any())).thenReturn(person);

    Membership membership = new Membership();
    membership.setMembershipUID(2L);

    List<Member> members1 = repo.getMembersForMembership(membership);
    assertNotNull("Ensure the members collection is not null.", members1);
    assertFalse("Ensure the members collection is not empty.", members1.isEmpty());
    assertEquals("Ensure we found the correct number of members.", 2, members1.size());

    Member member1 = members1.get(0);
    assertNotNull("Ensure the member is not NULL!", member1);

    String ownerId = member1.getOwnerIdent() + "-EDITED";

    member1.setEmailNewsletter(true);
    member1.setOwnerIdent(ownerId);
    member1.setPerson(person);

    Member member2 = repo.saveMember(member1, membership, user);
    assertNotNull("Ensure the member is not null.", member2);
    assertEquals("Ensure it is the same member.", member1.getMemberUID(), member2.getMemberUID());
    assertEquals("Ensure it has been updated.", ownerId, member2.getOwnerIdent());
    assertEquals("Ensure it has been updated.", 1, member2.getMemberUpdateCount());
  }

  /**
   * Test the saveMember method and updating a member but failing (due to an invalid update count).
   */
  @Test(expected = OptimisticLockingFailureException.class)
  public void testSaveMember_Update_Failed() {
    Membership membership = new Membership();
    membership.setMembershipUID(2L);

    List<Member> members1 = repo.getMembersForMembership(membership);
    assertNotNull("Ensure the members collection is not null.", members1);
    assertFalse("Ensure the members collection is not empty.", members1.isEmpty());
    assertEquals("Ensure we found the correct number of members.", 2, members1.size());

    Member member1 = members1.get(0);
    assertNotNull("Ensure the member is not NULL!", member1);

    String ownerId = member1.getOwnerIdent() + "-EDITED";

    member1.setEmailNewsletter(true);
    member1.setOwnerIdent(ownerId);
    member1.setMemberUpdateCount(1L);

    repo.saveMember(member1, membership, user);
  }

  /**
   * Test the saveMembershipCounty and inserting a membership county.
   */
  @Test
  public void testSaveMembershipCounty_Insert() {
    Membership membership = new Membership();
    membership.setMembershipUID(1L);

    List<MembershipCounty> counties1 = repo.getMembershipCountiesForMembership(membership);
    assertNotNull("Ensure the membership counties collection is not null.", counties1);
    assertFalse("Ensure the membership counties collection is not empty.", counties1.isEmpty());
    assertEquals("Ensure we found the correct number of membership counties.", 2, counties1.size());

    MembershipCounty newCounty = new MembershipCounty();
    newCounty.setCounty(svCounty);
    newCounty.setNetMineralAcres(640);
    newCounty.setSurfaceAcres(640);

    MembershipCounty county = repo.saveMembershipCounty(newCounty, membership, user);
    assertNotNull("Ensure the membership county was created.", county);
    assertTrue("Ensure the membership county has a valid uid.", county.getMembershipCountyUID() > 0L);

    List<MembershipCounty> counties2 = repo.getMembershipCountiesForMembership(membership);
    assertNotNull("Ensure the membership counties collection is not null.", counties2);
    assertFalse("Ensure the membership counties collection is not empty.", counties2.isEmpty());
    assertEquals("Ensure we found the correct number of membership counties.", 3, counties2.size());
  }

  /**
   * Test the saveMembershipCounty and updating a membership county.
   */
  @Test
  public void testSaveMembershipCounty_Update() {
    Membership membership = new Membership();
    membership.setMembershipUID(1L);

    List<MembershipCounty> counties1 = repo.getMembershipCountiesForMembership(membership);
    assertNotNull("Ensure the membership counties collection is not null.", counties1);
    assertFalse("Ensure the membership counties collection is not empty.", counties1.isEmpty());
    assertEquals("Ensure we found the correct number of membership counties.", 2, counties1.size());

    MembershipCounty county1 = counties1.get(0);
    county1.setNetMineralAcres(1024);

    MembershipCounty county2 = repo.saveMembershipCounty(county1, membership, user);
    assertNotNull("Ensure the membership county is not null.", county2);
    assertEquals("Ensure it is has been updated.", 1024, county2.getNetMineralAcres());
    assertEquals("Ensure it has been updated.", 1, county2.getMembershipCountyUpdateCount());
  }

  /**
   * Test the saveMembershipCounty and updating a membership county but failing (due to an invalid update count).
   */
  @Test(expected = OptimisticLockingFailureException.class)
  public void testSaveMembershipCounty_Update_Failed() {
    Membership membership = new Membership();
    membership.setMembershipUID(1L);

    List<MembershipCounty> counties1 = repo.getMembershipCountiesForMembership(membership);
    assertNotNull("Ensure the membership counties collection is not null.", counties1);
    assertFalse("Ensure the membership counties collection is not empty.", counties1.isEmpty());
    assertEquals("Ensure we found the correct number of membership counties.", 2, counties1.size());

    MembershipCounty county = counties1.get(0);
    county.setNetMineralAcres(1024);
    county.setMembershipCountyUpdateCount(1L);

    repo.saveMembershipCounty(county, membership, user);
  }
}
