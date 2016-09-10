package com.cagst.swkroa.utils;

import javax.inject.Inject;
import javax.inject.Named;

import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserRepository;

/**
 * This is a utility class that will perform actions for / on a {@link User}.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@Named("userUtilities")
public final class UserUtilitiesImpl implements UserUtilities {
  private final UserRepository userRepo;

  /**
   * Primary Constructor used to create an instance of <i>UserUtilities</i>.
   *
   * @param userRepo
   *     The {@link UserRepository} to use to perform actions for / on a {@link User}.
   */
  @Inject
  public UserUtilitiesImpl(final UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public void createUser(final String username, final String password, final String firstName, final String lastName) {
    User user = new User();
    user.setUserUID(1L);

    User newUser = new User();
    newUser.setUsername(username);
    newUser.setPassword(password);
    newUser.setFirstName(firstName);
    newUser.setLastName(lastName);
    newUser.setPasswordTemporary(true);

    userRepo.saveUser(newUser, user);
  }
}
