package com.cagst.swkroa.member;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Collection;

import com.cagst.common.db.DataSourceFactory;
import com.cagst.common.db.StatementLoader;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

/**
 * Test class for the MemberTypeRepositoryJdbc class.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class MemberTypeRepositoryJdbcTest {
  private MemberTypeRepositoryJdbc repo;

  @Before
  public void setUp() throws Exception {
    repo = new MemberTypeRepositoryJdbc(createTestDataSource());
    repo.setStatementDialect(StatementLoader.HSQLDB_DIALECT);
  }

  /**
   * Test the getMemberTypeByID method and not finding the MemberType.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetMemberTypeByID_NotFound() {
    repo.getMemberTypeByID(999L);
  }

  /**
   * Test the getMemberTypeByID method and finding the MemberType.
   */
  @Test
  public void testGetMemberTypeByID_Found() {
    MemberType type = repo.getMemberTypeByID(2L);
    assertNotNull("Ensure we found a MemberType.", type);
    assertEquals("Ensure we found the correct MemberType.", 2L, type.getPreviousMemberTypeUID());
    assertTrue("Ensure we found the correct MemberType.", type.getDuesAmount().compareTo(new BigDecimal(85)) == 0);
  }

  /**
   * Test the getMemberTypeByIDAsOf method and not finding the MemberType.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetMemberTypeByIDAsOf_NotFound() {
    DateTime dt = new DateTime(1999, 10, 01, 13, 15);

    repo.getMemberTypeByIDAsOf(2L, dt);
  }

  /**
   * Test the getMemberTypeByIDAsOf method and finding the MemberType.
   */
  @Test
  public void testGetMemberTypeByIDAsOf_Found() {
    DateTime dt = new DateTime(2005, 04, 19, 13, 15);

    MemberType type = repo.getMemberTypeByIDAsOf(2L, dt);
    assertNotNull("Ensure we found a MemberType.", type);
    assertEquals("Ensure we found the correct MemberType.", 2L, type.getPreviousMemberTypeUID());
    assertTrue("Ensure we found the correct MemberType.", type.getDuesAmount().compareTo(new BigDecimal(70)) == 0);
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
    DateTime dt = new DateTime(2005, 04, 19, 13, 15);

    repo.getMemberTypeByMeaningAsOf(MemberType.FAMILY_MEMBER, dt);
  }

  /**
   * Test the getMemberTypeByMeaningAsOf method and finding 1 MemberType.
   */
  @Test
  public void testGetMemberTypeByMeaningAsOf_FoundOne() {
    DateTime dt = new DateTime(2005, 04, 19, 13, 15);

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
    assertEquals("Ensure we found the correct number of MemberTypes.", 4, types.size());
  }

  /**
   * Test the getActiveMemberTypesAsOf method.
   */
  @Test
  public void testGetActiveMemberTypesAsOf() {
    DateTime dt = new DateTime(2012, 04, 19, 13, 15, 00);

    Collection<MemberType> types = repo.getActiveMemberTypesAsOf(dt);

    assertNotNull("Ensure we have a collection of MemberType.", types);
    assertFalse("Ensure our collection is populated.", types.isEmpty());
    assertEquals("Ensure we found the correct number of MemberTypes.", 5, types.size());
  }

  private DataSource createTestDataSource() {
    Resource schemaLocation = new ClassPathResource("/testDb/schema.sql");
    Resource testDataLocation = new ClassPathResource("/testDb/test_data.sql");

    DataSourceFactory dsFactory = new DataSourceFactory("swkroadb", schemaLocation, testDataLocation);
    return dsFactory.getDataSource();
  }
}
