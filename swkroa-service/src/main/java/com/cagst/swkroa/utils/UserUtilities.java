package com.cagst.swkroa.utils;

/**
 * Defines some utilities that can act upon a {@link User}.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public interface UserUtilities {
  /**
   * This utility method will create a user with the specified attributes. This is intended to be
   * used to create the initial set of users only.
   * <p/>
   * NOTE: All users created with this utility will have their password set as temporary, meaning
   * they will be forced to change their password once they sign in.
   *
   * @param username
   *     The username for the {@link User}.
   * @param password
   *     The free-text password for the {@link User}.
   * @param firstName
   *     The first name to associate with the {@link User}.
   * @param lastName
   *     The last name to associate with the {@link User}.
   */
  public void createUser(final String username, final String password, final String firstName, final String lastName);
}
