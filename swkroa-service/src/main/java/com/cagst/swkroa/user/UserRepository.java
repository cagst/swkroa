package com.cagst.swkroa.user;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.cagst.swkroa.security.SecurityPolicy;

/**
 * Definition of a repository that retrieves and persists {@link User} objects.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 */
public interface UserRepository {
	/**
	 * Retrieves a {@link User} based upon the specified username.
	 * 
	 * @param username
	 *          The {@link String} username that identifies the {@link User} to retrieve.
	 * 
	 * @return The {@link User} associated to the specified username. <code>null</code> if no
	 *         {@link User} is associated to this username.
	 * 
	 * @throws IllegalArgumentException
	 *           if username is <code>null</code> or is empty.
	 */
	public User getUserByUsername(final String username) throws IllegalArgumentException;

	/**
	 * Retrieves a {@link User} based upon the unique identifier.
	 * 
	 * @param uid
	 *          A {@link long} that uniquely identifies the User to retrieve.
	 * 
	 * @return The {@link User} that is associated with the specified uid.
	 */
	public User getUserByUID(final long uid);

	/**
	 * Updates the {@link User} account for a signin attempt.
	 * 
	 * @param user
	 *          The {@link User} attempting to signin.
	 * 
	 * @return The {@link User} that has been updated accordingly.
	 * 
	 * @throws IllegalArgumentException
	 *           if <code>user</code> is null
	 */
	public User signinAttempt(final User user) throws IllegalArgumentException;

	/**
	 * Updates the {@link User} account for a successful sign-in.
	 * 
	 * @param user
	 *          The {@link User} to update.
	 * @param ipAddress
	 *          The ipAddress where the user was when they successfully signed in.
	 * 
	 * @return The {@link User} that has been updated accordingly.
	 * 
	 * @throws IllegalArgumentException
	 *           if <code>user</code> is null
	 */
	public User signinSuccessful(final User user, final String ipAddress) throws IllegalArgumentException;

	/**
	 * Locks the user account as of NOW. Used primarily when the user has exceeded their sign-in
	 * attempts.
	 * 
	 * @param user
	 *          The {@link User} who's account is to be locked.
	 * @param message
	 *          A descriptive message describing why the account as locked.
	 * 
	 * @return The {@link User} that has been locked and updated accordingly.
	 * 
	 * @throws IllegalArgumentException
	 *           if {@code user} is null
	 */
	public User lockUserAccount(final User user, final String message) throws IllegalArgumentException;

	/**
	 * Unlocks the user account as of NOW. Used by the system to automatically unlock a user account
	 * when they have gone past the Account Locked Days according to the {@link SecurityPolicy}
	 * defined for the user or by another user to manually unlock a user account.
	 * 
	 * @param unlockUser
	 *          The {@link User} who's account is to be unlocked.
	 * @param message
	 *          A descriptive message describing why the account as unlocked.
	 * @param user
	 *          The {@link User} that is performing the unlock.
	 * 
	 * @return The {@link User} that has been successfully unlocked and updated accordingly.
	 * 
	 * @throws IllegalArgumentException
	 *           if {@code user} is null
	 */
	public User unlockUserAccount(final User unlockUser, final String message, final User user)
			throws IllegalArgumentException;

	/**
	 * Changes the specified {@link User User} password.
	 * <p>
	 * <b>NOTE:</b> No checks are performed on the password to ensure they meet the security policy
	 * for the user.
	 * 
	 * @param user
	 *          The {@link User} who's password we are changing.
	 * @param newPassword
	 *          The {@link String Password} to change to.
	 * @param message
	 *          A descriptive message of the change password event.
	 * 
	 * @return A new {@link User} with it's password changed.
	 * 
	 * @throws IllegalArgumentException
	 *           if <code>user</code> is null or <code>password</code> is null or empty
	 */
	public User changeUserPassword(final User user, String newPassword, final String message)
			throws IllegalArgumentException;

	/**
	 * Checks to see if the specified {@code username} is currently being used.
	 * 
	 * @param username
	 *          The {@link String} username to check.
	 * 
	 * @return {@code true} if the username is already being used, {@code false} if not.
	 */
	public boolean doesUsernameExist(final String username);

	/**
	 * Checks to see if the specified {@code username} is currently being used.
	 * 
	 * @param username
	 *          The {@link String} username to check.
	 * @param user
	 *          The {@link User} to possibly exclude from the check.
	 * 
	 * @return {@code true} if the username is already being used, {@code false} if not.
	 */
	public boolean doesUsernameExist(final String username, final User user);

	/**
	 * Commits the specified {@link User User} to persistent storage.
	 * 
	 * @param builder
	 *          The {@link User} to persist.
	 * @param user
	 *          The {@link User} that performed the changes.
	 * 
	 * @return A {@link User} once it has been committed to persistent storage.
	 * 
	 * @throws OptimisticLockingFailureException
	 *           if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
	 * @throws IncorrectResultSizeDataAccessException
	 *           if the number of rows inserted / updated exceeded the expected number
	 * @throws UsernameTakenException
	 *           if the username associated to the {@code builder} is already being used by another
	 *           user
	 * @throws DataAccessException
	 *           if the query fails
	 */
	public User saveUser(final User builder, final User user) throws OptimisticLockingFailureException,
			IncorrectResultSizeDataAccessException, UsernameTakenException, DataAccessException;
}
