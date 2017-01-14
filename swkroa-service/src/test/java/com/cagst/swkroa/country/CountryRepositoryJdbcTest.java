package com.cagst.swkroa.country;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import com.cagst.swkroa.test.BaseTestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for CountryRepositoryJdbc class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class CountryRepositoryJdbcTest extends BaseTestRepository {
  private CountryRepositoryJdbc repo;

  @Before
  public void setUp() {
    repo = new CountryRepositoryJdbc(createTestDataSource());
  }

  /**
   * Test the getActiveCountries method.
   */
  @Test
  public void testGetActiveCountries() {
    Collection<Country> countries = repo.getActiveCountries();
    assertNotNull("Ensure the countries collection is not null.", countries);
    assertFalse("Ensure the countries collection is not empty.", countries.isEmpty());
    assertEquals("Ensure we found the correct number of countries.", 2, countries.size());
  }

  /**
   * Test the getActiveCounteis method.
   */
  @Test
  public void testGetActiveCounties() {
    Collection<County> counties = repo.getActiveCounties();
    assertNotNull("Ensure the counties collection is not null.", counties);
    assertFalse("Ensure the counties collection is not empty.", counties.isEmpty());
    assertEquals("Ensure we found the correct number of counties.", 6, counties.size());
  }

  /**
   * Test the getActiveStatesForCountry method.
   */
  @Test
  public void testGetActiveStatesForCountry() {
    Collection<State> usStates = repo.getActiveStatesForCountry("US");
    assertNotNull("Ensure the states collection is not null.", usStates);
    assertFalse("Ensure the states collection is not empty.", usStates.isEmpty());
    assertEquals("Ensure we found the correct number of states.", 4, usStates.size());

    Collection<State> caStates = repo.getActiveStatesForCountry("CA");
    assertNotNull("Ensure the states collection is not null.", caStates);
    assertFalse("Ensure the states collection is not empty.", caStates.isEmpty());
    assertEquals("Ensure we found the correct number of states.", 2, caStates.size());
  }

  /**
   * Test the getCountiesForState method.
   */
  @Test
  public void testGetCountiesForState() {
    Collection<County> counties = repo.getCountiesForState("KS");
    assertNotNull("Ensure the counties collection is not null.", counties);
    assertFalse("Ensure the counties collection is not empty.", counties.isEmpty());
    assertEquals("Ensure we found the correct number of counties.", 3, counties.size());
  }

  /**
   * Test the getCountyByUID and not finding a County.
   */
  @Test
  public void testGetCountyByUID_NotFound() {
    County county = repo.getCountyByUID(999L);
    assertNull("Ensure we didn't find the county (it is NULL).", county);
  }

  /**
   * Test the getCountyByUID and finding a County.
   */
  @Test
  public void testGetCountyByUID_Found() {
    County county1 = repo.getCountyByUID(1L);
    assertNotNull("Ensure we found the county.", county1);
    assertEquals("Ensure we found the correct county.", 1L, county1.getCountyUID());
    assertEquals("Ensure we found the correct county.", "JO", county1.getCountyCode());
    assertFalse("Ensure the county is not a SWKROA country.", county1.isSwkroaCounty());

    County county2 = repo.getCountyByUID(2L);
    assertNotNull("Ensure we found the county.", county2);
    assertEquals("Ensure we found the correct county.", 2L, county2.getCountyUID());
    assertEquals("Ensure we found the correct county.", "SV", county2.getCountyCode());
    assertTrue("Ensure the county is not a SWKROA country.", county2.isSwkroaCounty());
  }

  /**
   * Test the getCountyByStateAndCode method and not finding a County.
   */
  @Test
  public void testGetCountyByStateAndCode_NotFound() {
    County county = repo.getCountyByStateAndCode("CO", "SV");
    assertNull("Ensure we did not find the county.", county);
  }

  /**
   * Test the getCountyByStateAndCode method and finding a County.
   */
  @Test
  public void testGetCountyByStateAndCode_Found() {
    County county1 = repo.getCountyByStateAndCode("MO", "JO");
    assertNotNull("Ensure we found the county.", county1);
    assertEquals("Ensure we found the correct county.", "JO", county1.getCountyCode());
    assertFalse("Ensure the county is not a SWKROA country.", county1.isSwkroaCounty());

    County county2 = repo.getCountyByStateAndCode("KS", "JO");
    assertNotNull("Ensure we found the county.", county2);
    assertEquals("Ensure we found the correct county.", "JO", county2.getCountyCode());
    assertFalse("Ensure the county is not a SWKROA country.", county2.isSwkroaCounty());
  }
}
