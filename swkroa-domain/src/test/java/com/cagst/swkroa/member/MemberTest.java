package com.cagst.swkroa.member;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for Membership class.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class MemberTest {
  /**
   * Tests the getEffectiveDuesAmount method.
   */
  @Test
  public void testGetEffectiveDuesAmount() {
    Membership membership1 = new Membership();
    assertNull("Ensure a new Membership has no dues amount defined.", membership1.getFixedDuesAmount());
    assertEquals("Ensure a new Membership has nothing (0) due.", 0d, membership1.getEffectiveDuesAmount().doubleValue(), 0d);

    // the view doesn't handle incremental dues (from Membership Counties) yet.
//    Membership membership2 = new Membership();
//    MembershipCounty county1 = new MembershipCounty();
//    county1.setNetMineralAcres(320);
//    membership2.addCounty(county1);
//
//    assertNull("Ensure the Membership has no dues amount defined.", membership2.getFixedDuesAmount());
//    assertEquals("Ensure the Membership has 5 due.", 5d, membership2.getEffectiveDuesAmount().doubleValue(), 0d);

    Membership membership3 = new Membership();
    membership3.setFixedDuesAmount(new BigDecimal(21d));
    assertNotNull("Ensure the Membership has no dues amount defined.", membership3.getFixedDuesAmount());
    assertEquals("Ensure the Membership has 21 due.", 21d, membership3.getEffectiveDuesAmount().doubleValue(), 0d);

//    MembershipCounty county2 = new MembershipCounty();
//    county2.setNetMineralAcres(480);
//
//    Membership membership4 = new Membership();
//    membership4.addCounty(county1);
//    membership4.addCounty(county2);
//    membership4.setFixedDuesAmount(null);
//    assertNull("Ensure the Membership has no dues amount defined.", membership4.getFixedDuesAmount());
//    assertEquals("Ensure the Membership has 15 due.", 15d, membership4.getEffectiveDuesAmount().doubleValue(), 0d);

    MemberType type1 = new MemberType();
    type1.setDuesAmount(new BigDecimal(70d));

    MemberType type2 = new MemberType();
    type2.setDuesAmount(new BigDecimal(20d));

    Member member1 = new Member();
    member1.setMemberType(type1);

    Member member2 = new Member();
    member2.setMemberType(type2);

//    Membership membership5 = new Membership();
//    membership5.addCounty(county1);
//    membership5.addCounty(county2);
//    membership5.addMember(member1);
//    assertNull("Ensure the Membership has no dues amount defined.", membership5.getFixedDuesAmount());
//    assertEquals("Ensure the Membership has 85 due.", 85d, membership5.getEffectiveDuesAmount().doubleValue(), 0d);
//
//    Membership membership6 = new Membership();
//    membership6.addCounty(county1);
//    membership6.addCounty(county2);
//    membership6.addMember(member1);
//    membership6.addMember(member2);
//    assertNull("Ensure the Membership has no dues amount defined.", membership6.getFixedDuesAmount());
//    assertEquals("Ensure the Membership has 105 due.", 105d, membership6.getEffectiveDuesAmount().doubleValue(), 0d);

    Membership membership7 = new Membership();
    membership7.setFixedDuesAmount(new BigDecimal(32d));
    assertNotNull("Ensure the Membership has no dues amount defined.", membership7.getFixedDuesAmount());
    assertEquals("Ensure the Membership has 32 due.", 32d, membership7.getEffectiveDuesAmount().doubleValue(), 0d);
  }
}
