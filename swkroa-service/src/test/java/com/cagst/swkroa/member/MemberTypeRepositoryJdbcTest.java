package com.cagst.swkroa.member;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import com.cagst.swkroa.internal.StatementDialect;
import com.cagst.swkroa.test.BaseTestRepository;
import com.cagst.swkroa.user.User;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Test class for the MemberTypeRepositoryJdbc class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class MemberTypeRepositoryJdbcTest extends BaseTestRepository {
  private MemberTypeRepositoryJdbc repo;

  private User user;

  @Before
  public void setUp() throws Exception {
    user = new User();
    user.setUserUID(1L);

    repo = new MemberTypeRepositoryJdbc(createTestDataSource());
    repo.setStatementDialect(StatementDialect.HSQLDB);
  }

  /**
   * Test the getMemberTypeByUID method and not finding the MemberType.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetMemberTypeByUID_NotFound() {
    repo.getMemberTypeByUID(999L);
  }

  /**
   * Test the getMemberTypeByUID method and finding the MemberType.
   */
  @Test
  public void testGetMemberTypeByUID_Found() {
    MemberType type = repo.getMemberTypeByUID(3L);
    assertNotNull("Ensure we found a MemberType.", type);
    assertEquals("Ensure we found the correct MemberType.", 3L, type.getMemberTypeUID());
    assertTrue("Ensure we found the correct MemberType.", type.getDuesAmount().compareTo(new BigDecimal(85)) == 0);
  }

  /**
   * Test the getMemberTypeMeaning method and finding too many MemberTypes.
   */
  @Test(expected = IncorrectResultSizeDataAccessException.class)
  public void testGetMemberTypeByMeaning_FoundTooMany() {
    repo.getMemberTypeByMeaning(MemberType.FAMILY_MEMBER);
  }

  /**
   * Test the getMemberTypeByMeaning method and finding the 1 effective MemberType.
   */
  @Test
  public void testGetMemberTypeByMeaning_FoundOne() {
    MemberType type = repo.getMemberTypeByMeaning(MemberType.FAMILY_HEAD);
    assertNotNull("Ensure we found a MemberType.", type);
    assertEquals("Ensure we found the correct MemberType.", MemberType.FAMILY_HEAD, type.getMemberTypeMeaning());
  }

  /**
   * Test the getMemberTypeByMeaningAsOf method and not finding a MemberType.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetMemberTypeByMeaningAsOf_NotFound() {
    repo.getMemberTypeByMeaning("NOT_FOUND");
  }

  /**
   * Test the getMemberTypeByMeaningAsOf method and finding too many MemberTypes.
   */
  @Test(expected = IncorrectResultSizeDataAccessException.class)
  public void testGetMemberTypeByMeaningAsOf_FoundTooMany() {
    DateTime dt = new DateTime(2005, 4, 19, 13, 15);

    repo.getMemberTypeByMeaningAsOf(MemberType.FAMILY_MEMBER, dt);
  }

  /**
   * Test the getMemberTypeByMeaningAsOf method and finding 1 MemberType.
   */
  @Test
  public void testGetMemberTypeByMeaningAsOf_FoundOne() {
    DateTime dt = new DateTime(2005, 4, 19, 13, 15);

    MemberType type = repo.getMemberTypeByMeaningAsOf(MemberType.ASSOCIATE, dt);
    assertNotNull("Ensure we found a MemberType.", type);
    assertEquals("Ensure we found the correct MemberType.", MemberType.ASSOCIATE, type.getMemberTypeMeaning());
  }

  /**
   * Test the getActiveMemberTypes method.
   */
  @Test
  public void testGetActiveMemberTypes() {
    Collection<MemberType> types = repo.getActiveMemberTypes();

    assertNotNull("Ensure we have a collection of MemberType.", types);
    assertFalse("Ensure our collection is populated.", types.isEmpty());
    assertEquals("Ensure we found the correct number of MemberTypes.", 6, types.size());
  }

  /**
   * Test the getActiveMemberTypesAsOf method.
   */
  @Test
  public void testGetActiveMemberTypesAsOf() {
    DateTime dt = new DateTime(2012, 4, 19, 13, 15, 0);

    Collection<MemberType> types = repo.getActiveMemberTypesAsOf(dt);

    assertNotNull("Ensure we have a collection of MemberTypes.", types);
    assertFalse("Ensure our collection is populated.", types.isEmpty());
    assertEquals("Ensure we found the correct number of MemberTypes.", 7, types.size());
  }

  /**
   * Test the getActiveMemberTypesForMemberType method.
   */
  @Test
  public void testGetActiveMemberTypesForMemberType() {
    MemberType type = repo.getMemberTypeByMeaning(MemberType.FAMILY_HEAD);

    List<MemberType> types = repo.getActiveMemberTypesForMemberType(type.getMemberTypeUID());

    assertNotNull("Ensure we have a collection of MemberTypes.", types);
    assertFalse("Ensure our collection is populated.", types.isEmpty());
    assertEquals("Ensure we found the correct number of MemberTypes.", 2, types.size());
  }

  /**
   * Test the saveMemberType method by inserting a MemberType.
   */
  @Test
  public void testSaveMemberType_Insert() {
    DateTime now = new DateTime();

    MemberType type = new MemberType();
    type.setMemberTypeDisplay("New Member Type");
    type.setMemberTypeMeaning("NEW_MEMBER_TYPE");
    type.setDuesAmount(BigDecimal.valueOf(123.00));
    type.setPrimary(true);
    type.setAllowSpouse(false);
    type.setAllowMember(true);
    type.setBeginEffectiveDate(now);

    MemberType newType = repo.saveMemberType(type, user);
    assertNotNull("Ensure a MemberType is returned", newType);
    assertTrue("Ensure the MemberType has an ID", newType.getMemberTypeUID() > 0L);
    assertEquals("Ensure the description is correct", "New Member Type", newType.getMemberTypeDisplay());
    assertEquals("Ensure the meaning is correct", "NEW_MEMBER_TYPE", newType.getMemberTypeMeaning());

    MemberType retrievedType = repo.getMemberTypeByMeaning("NEW_MEMBER_TYPE");
    assertNotNull("Ensure a MemberType is returned", retrievedType);
    assertTrue("Ensure the MemberType has an ID", retrievedType.getMemberTypeUID() > 0L);
    assertEquals("Ensure the description is correct", "New Member Type", retrievedType.getMemberTypeDisplay());
    assertEquals("Ensure the meaning is correct", "NEW_MEMBER_TYPE", retrievedType.getMemberTypeMeaning());
  }

  /**
   * Test the saveMemberType method by updating a MemberType.
   */
  @Test
  public void testSaveMemberType_Update() {
    MemberType type = repo.getMemberTypeByUID(2L);

    String newDisplay = type.getMemberTypeDisplay() + " - Updated";
    BigDecimal newDuesAmount = type.getDuesAmount().multiply(BigDecimal.valueOf(0.25)).setScale(2, BigDecimal.ROUND_HALF_UP);

    type.setMemberTypeDisplay(newDisplay);
    type.setDuesAmount(newDuesAmount);

    MemberType savedMemberType = repo.saveMemberType(type, user);
    assertNotNull("Ensure a MemberType is returned", savedMemberType);
    assertEquals("Ensure the MemberType update count was updated", 1, savedMemberType.getMemberTypeUpdateCount());
    assertEquals("Ensure the description was updated", newDisplay, savedMemberType.getMemberTypeDisplay());
    assertEquals("Ensure the dues amount was updated", newDuesAmount, savedMemberType.getDuesAmount());

    MemberType retrievedType = repo.getMemberTypeByUID(2L);
    assertNotNull("Ensure a MemberType is returned", retrievedType);
    assertEquals("Ensure the MemberType update count was updated", 1, retrievedType.getMemberTypeUpdateCount());
    assertEquals("Ensure the description was updated", newDisplay, retrievedType.getMemberTypeDisplay());
    assertEquals("Ensure the dues amount was updated", newDuesAmount, retrievedType.getDuesAmount());
  }

  /**
   * Test the saveMemberType method by updating a MemberType but failing (due to an invalid update count).
   */
  @Test(expected = OptimisticLockingFailureException.class)
  public void testSaveMemberType_Update_Failed() {
    MemberType type = repo.getMemberTypeByUID(2L);

    String newDisplay = type.getMemberTypeDisplay() + " - Updated";
    BigDecimal newDuesAmount = type.getDuesAmount().multiply(BigDecimal.valueOf(0.25)).setScale(2);

    type.setMemberTypeDisplay(newDisplay);
    type.setDuesAmount(newDuesAmount);
    type.setMemberTypeUpdateCount(type.getMemberTypeUpdateCount() + 1);

    repo.saveMemberType(type, user);
  }
}
