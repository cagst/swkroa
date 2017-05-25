package com.cagst.swkroa.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Extension class for the {@link StringUtils} class that adds some additional common operations.
 *
 * @author Craig Gaskill
 */
public class SwkroaStringUtils extends StringUtils {
  /**
   * Normalizes the specified name into a valid key that can be searched.
   *
   * @param name
   *     The {@link String} to normalized into a key.
   *
   * @return A {@link String} that represents a key value of the specified name.
   */
  public static String normalizeToKey(final String name) {
    String key = remove(name, '.');
    key = remove(key, ',');

    return upperCase(stripAccents(replaceChars(normalizeSpace(key), ' ', '_')));
  }
}
