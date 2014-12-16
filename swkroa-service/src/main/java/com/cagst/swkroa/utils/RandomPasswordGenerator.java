package com.cagst.swkroa.utils;

import java.util.Random;

import org.springframework.util.Assert;

/**
 * This class can be used to generate a random password of a specified size.
 * <br/>
 * The password will be AlphaNumeric.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public class RandomPasswordGenerator {
  private static final String CHAR_ARRAY = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final Random random = new Random();

  /**
   * Private default constructor so class can't be instantiated.
   */
  private RandomPasswordGenerator() {
  }

  public static String generateRandomPassword(final int length) {
    Assert.isTrue(length > 1, "Assertion Failure - argument [length] must be greater than 1");

    char[] buffer = new char[length];
    for (int idx = 0; idx < length; idx++) {
      buffer[idx] = CHAR_ARRAY.charAt(random.nextInt(CHAR_ARRAY.length()));
    }

    return new String(buffer);
  }
}
