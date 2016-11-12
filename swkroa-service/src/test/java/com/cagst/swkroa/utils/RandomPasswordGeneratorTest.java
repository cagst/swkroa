package com.cagst.swkroa.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for the {@link RandomPasswordGenerator} class.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class RandomPasswordGeneratorTest {
  @Test(expected = IllegalArgumentException.class)
  public void testGenerateRandomPassword_InvalidSize() {
    RandomPasswordGenerator.generateRandomPassword(1);
  }

  @Test
  public void testGenerateRandomPassword() {
    String tmpPwd1 = RandomPasswordGenerator.generateRandomPassword(7);

    assertNotNull("Ensure we have a temporary password.", tmpPwd1);
    assertEquals("Ensure the temporary password is the correct size.", 7, tmpPwd1.length());

    String tmpPwd2 = RandomPasswordGenerator.generateRandomPassword(10);
    assertNotNull("Ensure we have a temporary password.", tmpPwd2);
    assertEquals("Ensure the temporary password is the correct size.", 10, tmpPwd2.length());
  }
}
