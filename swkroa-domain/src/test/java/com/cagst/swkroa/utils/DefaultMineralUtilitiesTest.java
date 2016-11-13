package com.cagst.swkroa.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cagst.swkroa.member.MembershipCounty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for DefaultMineralUtilities class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class DefaultMineralUtilitiesTest {
  private final DefaultMineralUtilities utils = new DefaultMineralUtilities();

  /**
   * Tests the calculateFeesForMembershipCounty method.
   */
  @Test
  public void testCalculateFeesForMembershipCounty() {
    MembershipCounty county = new MembershipCounty();

    county.setNetMineralAcres(480);
    BigDecimal fee = utils.calculateFeesForMembershipCounty(county);
    assertNotNull("Ensure an object was returned.", fee);
    assertEquals("Ensure it returned the correct fee.", 10d, fee.doubleValue(), 0);

    county.setNetMineralAcres(160);
    fee = utils.calculateFeesForMembershipCounty(county);
    assertNotNull("Ensure an object was returned.", fee);
    assertEquals("Ensure it returned the correct fee.", 0d, fee.doubleValue(), 0);

    county.setNetMineralAcres(0);
    fee = utils.calculateFeesForMembershipCounty(county);
    assertNotNull("Ensure an object was returned.", fee);
    assertEquals("Ensure it returned the correct fee.", 0d, fee.doubleValue(), 0);

    county.setNetMineralAcres(100);
    fee = utils.calculateFeesForMembershipCounty(county);
    assertNotNull("Ensure an object was returned.", fee);
    assertEquals("Ensure it returned the correct fee.", 0d, fee.doubleValue(), 0);

    county.setNetMineralAcres(300);
    fee = utils.calculateFeesForMembershipCounty(county);
    assertNotNull("Ensure an object was returned.", fee);
    assertEquals("Ensure it returned the correct fee.", 5d, fee.doubleValue(), 0);

    county.setNetMineralAcres(800);
    fee = utils.calculateFeesForMembershipCounty(county);
    assertNotNull("Ensure an object was returned.", fee);
    assertEquals("Ensure it returned the correct fee.", 20d, fee.doubleValue(), 0);
  }

  /**
   * Tests the calculateFeesForMembershipCounties method.
   */
  @Test
  public void testCalculateFeesForMembershipCounties() {
    MembershipCounty county1 = new MembershipCounty();
    county1.setNetMineralAcres(480);

    MembershipCounty county2 = new MembershipCounty();
    county2.setNetMineralAcres(150);

    MembershipCounty county3 = new MembershipCounty();
    county3.setNetMineralAcres(800);

    List<MembershipCounty> counties = new ArrayList<>();
    counties.add(county1);
    counties.add(county2);
    counties.add(county3);

    BigDecimal fees = utils.calculateFeesForMembershipCounties(counties);
    assertNotNull("Ensure an object was returned.", fees);
    assertEquals("Ensure it returned the correct fee.", 30d, fees.doubleValue(), 0);

  }
}
