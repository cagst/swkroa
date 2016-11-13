package com.cagst.swkroa.user;

import java.util.List;
import java.util.Optional;

import com.cagst.swkroa.security.SecurityPolicy;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Definition of a repository that retrieves and persists {@link User} objects.
 *
 * @author Craig Gaskill
 */
public interface UserRepository {
  /**
   * Retrieves a {@link User} based upon the specified username.
   *
   * @param username
   *     The {@link String} username that identifies the {@link User} to retrieve.
   *
   * @return An {@link Optional} that may contain the {@link User} associated to the specified username.
   *
   * @throws IllegalArgumentException
   *     if username is <code>null</code> or is empty.
   */
  Optional<User> getUserByUsername(String username) throws IllegalArgumentException;

  /**
   * Retrieves a {@link User} based upon the unique identifier.
   *
   * @param uid
   *     A {@link long} that uniquely identifies the User to retrieve.
   *
   * @return The {@link User} that is associated with the specified uid.
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
  Optional<User> getUserByPersonId(long personId);

  /**
   * Updates the {@link User} account for a signin attempt.
   *
   * @param user
   *     The {@link User} attempting to signin.
   *
   * @return The {@link User} that has been updated accordingly.
   *
   * @throws IllegalArgumentException
   *     if <code>user</code> is null
   */
  User signinAttempt(User user) throws IllegalArgumentException;

  /**
   * Updates the {@link User} account for a successful sign-in.
   *
   * @param user
   *     The {@link User} to update.
   * @param ipAddress
   *     The ipAddress where the user was when they successfully signed in.
   *
   * @return The {@link User} that has been updated accordingly.
   *
   * @throws IllegalArgumentException
   *     if <code>user</code> is null
   */
  User signinSuccessful(User user, String ipAddress) throws IllegalArgumentException;

  /**
   * Locks the user account as of NOW. Used primarily when the user has exceeded their sign-in
   * attempts.
   *
   * @param user
   *     The {@link User} who's account is to be locked.
   * @param message
   *     A descriptive message describing why the account as locked.
   * @param instigator
   *     The {@link User} that is performing the lock.
   *
   * @return The {@link User} that has been locked and updated accordingly.
   *
   * @throws IllegalArgumentException
   *     if {@code user} is null
   */
  User lockUserAccount(User user, String message, User instigator) throws IllegalArgumentException;

  /**
   * Unlocks the user account as of NOW. Used by the system to automatically unlock a user account
   * when they have gone past the Account Locked Days according to the {@link SecurityPolicy}
   * defined for the user or by another user to manually unlock a user account.
   *
   * @param user
   *     The {@link User} who's account is to be unlocked.
   * @param message
   *     A descriptive message describing why the account was unlocked.
   * @param instigator
   *     The {@link User} that is performing the unlock.
   *
   * @return The {@link User} that has been successfully unlocked and updated accordingly.
   *
   * @throws IllegalArgumentException if {@code user} is null
   */
  User unlockUserAccount(User user, String message, User instigator) throws IllegalArgumentException;

  /**
   * Enables the specified {@link User} account.
   *
   * @param user
   *     The {@link User} who's account is to be enabled.
   * @param message
   *     A descriptive message describing why the account was enabled.
   * @param instigator
   *     The {@link User} that is performing the enabling of the account.
   *
   * @return The {@link User} that has been successfully enabled and updated accordingly.
   *
   * @throws IllegalArgumentException if {@code user} is null
   */
  User enableUserAccount(User user, String message, User instigator) throws IllegalArgumentException;

  /**
   * Disables the specified {@link User} account.
   *
   * @param user
   *     The {@link User} who's account is to be disabled.
   * @param message
   *     A descriptive message describing why the account was disabled.
   * @param instigator
   *     The {@link User} that is performing the disabling of the account.
   *
   * @return The {@link User} that has been successfully disabled and updated accordingly.
   *
   * @throws IllegalArgumentException if {@code user} is null
   */
  User disableUserAccount(User user, String message, User instigator) throws IllegalArgumentException;

  /**
   * Changes the specified {@link User User} password.
   *
   * <b>NOTE:</b> No checks are performed on the password to ensure they meet the security policy
   * for the user.
   *
   * @param user
   *     The {@link User} who's password we are changing.
   * @param newPassword
   *     The {@link String Password} to change to.
   * @param message
   *     A descriptive message of the change password event.
   *
   * @return A new {@link User} with it's password changed.
   *
   * @throws IllegalArgumentException
   *     if <code>user</code> is null or <code>password</code> is null or empty
   */
  User changeUserPassword(User user, String newPassword, String message)
      throws IllegalArgumentException;

  /**
   * Resets the specified {@link User User} password. The password will be temporary
   * and the user will be required to change it the next time they sign-in.
   *
   * @param user
   *     The {@link User} to reset the password for.
   * @param tempPassword
   *     The {@link String Password} to reset to.
   * @param message
   *     A descriptive message of the reset password event.
   * @param instigator
   *     The {@link User} that instigated (performed) this action.
   *
   * @return A new {@link User} with it's password changed.
   *
   * @throws IllegalArgumentException
   *     if <code>user</code> is null or <code>password</code> is null or empty
   */
  User resetUserPassword(User user, String tempPassword, String message, User instigator)
      throws IllegalArgumentException;

  /**
   * Checks to see if the specified {@code username} is currently being used.
   *
   * @param username
   *     The {@link String} username to check.
   *
   * @return {@code true} if the username is already being used, {@code false} if not.
   */
  boolean doesUsernameExist(String username);

  /**
   * Checks to see if the specified {@code username} is currently being used.
   *
   * @param username
   *     The {@link String} username to check.
   * @param user
   *     The {@link User} to possibly exclude from the check.
   *
   * @return {@code true} if the username is already being used, {@code false} if not.
   */
  boolean doesUsernameExist(String username, User user);

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
   * Retieves a {@link List} of {@link UserQuestion} for the specified {@link User}.
   *
   * @param user
   *    The {@link User} to retrieve the list of security questions for.
   *
   * @return The {@link List} of {@link UserQuestion} associated with the specified {@link User}.
   */
  List<UserQuestion> getSecurityQuestionsForUser(User user);
}
