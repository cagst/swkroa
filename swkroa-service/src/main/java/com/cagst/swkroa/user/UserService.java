package com.cagst.swkroa.user;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Definitions of a service that retrieves and persists {@link User} objects.
 *
 * @author Craig Gaskill
 */
public interface UserService extends UserDetailsService {
  String SYSTEM_USER = "system";

  /**
   * Updates the {@link User User's} record for a successful signing in.
   *
   * @param user
   *     The {@link User} to update.
   * @param ipAddress
   *     The ipAddress of where the user signed in from.
   *
   * @return A {@link User} that has been updated accordingly.
   *
   * @throws IllegalArgumentException
   *     if the <code>user</code> is <code>null</code>.
   */
  User signinSuccessful(User user, String ipAddress) throws IllegalArgumentException;

  /**
   * Updates the {@link User User's} record for a failed sign in.
   *
   * @param username
   *     The username used trying to sign in.
   * @param message
   *     A descriptive message of why the sign in failed.
   *
   * @throws IllegalArgumentException
   *     if the <code>username</code> is <code>null</code> or empty.
   */
  void signinFailure(String username, String message) throws IllegalArgumentException;

  /**
   * Clears out the session when the user signs out of the system.
   *
   * @param user
   *     The {@link User} that has signed out of the system.
   */
  void signedOut(User user);

  /**
   * Clears out the session when the user is timed out of the system due to inactivity.
   *
   * @param user
   *     The {@link User} that has timed out of the system.
   */
  void timedOut(User user);

  /**
   * Modify the current user's password. This should change the user's password in the persistent
   * user repository.
   *
   * @param user
   *     The {@link User} to change the password for.
   * @param oldPassword
   *     The old / current password (for re-authentication if required).
   * @param newPassword
   *     The password to change to.
   * @param confirmPassword
   *     The re-entered password to change to.
   *
   * @return A {@link User} that has been updated accordingly.
   *
   * @throws BadCredentialsException
   *     if the oldPassword isn't valid, if the newPassword is the same as the new password,
   *     or if the newPassword is the same as the username
   */
  User changePassword(User user, String oldPassword, String newPassword, String confirmPassword)
      throws BadCredentialsException;

  /**
   * Reset the user's password to a random temporary password.
   *
   * @param user
   *     The {@link User} to reset the password for.
   * @param instigator
   *     The {@link User} that instigated (performed) the reset of the password for the user.
   *
   * @return A {@link User} that has been updated accordingly.
   */
  User resetPassword(User user, User instigator);

  /**
   * Updates the user's record by clearing the locked date (unlocking) on the user's account.
   *
   * @param user
   *     The {@link User} to unlock.
   * @param instigator
   *     The {@link User} that is performing the unlock.
   *
   * @return A {@link User} that has been updated accordingly.
   */
  User unlockAccount(User user, User instigator);

  /**
   * Enables the user's account.
   *
   * @param user
   *     The {@link User} to enable.
   * @param instigator
   *     The {@link User} that is performing the action.
   *
   * @return The {@link User} that has been updated accordingly.
   */
  User enableAccount(User user, User instigator);

  /**
   * Disables the user's account.
   *
   * @param user
   *     The {@link User} to disable.
   * @param instigator
   *     The {@link User} that is performing the action.
   *
   * @return The {@link User} that has been updated accordingly.
   */
  User disableAccount(User user, User instigator);

  boolean doesUsernameExist(String username);

  /**
   * Commits the specified {@link User} to persistent storage.
   *
   * @param builder
   *     The {@link User} to persist.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link User} once it has been committed to persistent storage.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws UsernameTakenException
   *     if the username associated to the {@code builder} is already being used by another
   *     user
   * @throws DataAccessException
   *     if the query fails
   */
  User saveUser(User builder, User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, UsernameTakenException;

  /**
   * Registers a {@link User}. The associated Person object must already exist.
   *
   * @param builder
   *     The {@link User} to persist.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link User} once it has been committed to persistent storage.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws UsernameTakenException
   *     if the username associated to the {@code builder} is already being used by another
   *     user
   * @throws DataAccessException
   *     if the query fails
   */
  User registerUser(User builder, User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, UsernameTakenException;

  /**
   * Retrieves a {@link List} of all {@link User Users} that are in the system.
   *
   * @return A {@link List} of {@link User Users} defined in the system.
   */
  List<User> getAllUsers();

  /**
   * Retrieves a {@link User} that is associated with the specified id.
   *
   * @param uid
   *     A {@link long} that uniquely identifies the {@link User} to retrieve.
   *
   * @return The {@link User} associated with the specified id.
   *
   * @throws EmptyResultDataAccessException
   *     when no user was found with the specified uid.
   * @throws IncorrectResultSizeDataAccessException
   *     when more than 1 user was found with the specified uid.
   */
  User getUserByUID(long uid) throws IncorrectResultSizeDataAccessException;

  /**
   * Retrieves a {@link User} based upon the unique identifier for the Person associated to the User record.
   *
   * @param personId
   *     A {@link long} that identifies the Person to retrieve the User for.
   *
   * @return The {@link User} that is associated with the specified personId.
   *
   * @throws IncorrectResultSizeDataAccessException
   *     when more than 1 user was found with the specified personId.
   */
  Optional<User> getUserByPersonId(long personId) throws IncorrectResultSizeDataAccessException;

}
