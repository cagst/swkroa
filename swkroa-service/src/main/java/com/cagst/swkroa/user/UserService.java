package com.cagst.swkroa.user;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Definitions of a service that retrieves and persists {@link User} objects.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public interface UserService extends UserDetailsService {
  public static final String SYSTEM_USER = "system";

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
  public User signinSuccessful(final User user, final String ipAddress) throws IllegalArgumentException;

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
  public void signinFailure(final String username, final String message) throws IllegalArgumentException;

  /**
   * Clears out the session when the user signs out of the system.
   *
   * @param user
   *     The {@link User} that has signed out of the system.
   */
  public void signedOut(final User user);

  /**
   * Clears out the session when the user is timed out of the system due to inactivity.
   *
   * @param user
   *     The {@link User} that has timed out of the system.
   */
  public void timedOut(final User user);

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
  public User changePassword(final User user, final String oldPassword, final String newPassword,
                             final String confirmPassword) throws BadCredentialsException;

  /**
   * Reset the user's password to a random temporary password.
   *
   * @param user
   *     The {@link User} to reset the password for.
   * @param instigator
   *     The {@link User} that instigated (performed) the reset of the password for the user.
   *
   * @return A {@link User} that has been updated accordingly.
   *
   */
  public User resetPassword(final User user, final User instigator);

  /**
   * Updates the user's record by clearing the locked date (unlocking) on the user's account.
   *
   * @param unlockUserUID
   *     The unique identifier of the user to unlock.
   * @param user
   *     The {@link User} that is performing the unlock.
   *
   * @return A {@link User} that has been updated accordingly.
   */
  public User unlockAccount(final long unlockUserUID, final User user);

  /**
   * Enables the user's account.
   *
   * @param enableUserUID
   *      The unique identifier of the user to enable.
   * @param user
   *      The {@link User} that is performing the action.
   *
   * @return The {@link User} that has been updated accordingly.
   */
  public User enableAccount(final long enableUserUID, final User user);

  /**
   * Disables the user's account.
   *
   * @param disableUserUID
   *      The unique identifier of the user to disable.
   * @param user
   *      The {@link User} that is performing the action.
   *
   * @return The {@link User} that has been updated accordingly.
   */
  public User disableAccount(final long disableUserUID, final User user);

  public boolean doesUsernameExist(final String username);

  /**
   * Commits the specified {@link User User} to persistent storage.
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
  public User saveUser(final User builder, final User user);

  /**
   * Retrieves a {@link List} of all {@link User Users} that are in the system.
   *
   * @return A {@link List} of {@link User Users} defined in the system.
   */
  public List<User> getAllUsers();

  /**
   * Retrieves a {@link User} that is associated with the specified id.
   *
   * @param uid
   *     A {@link long} that uniquely identifies the {@link User} to retrieve.
   *
   * @return The {@link User} associated with the specified id.
   */
  public User getUserByUID(final long uid);
}
