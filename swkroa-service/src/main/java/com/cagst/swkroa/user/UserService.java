package com.cagst.swkroa.user;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Definitions of a service that retrieves and persists {@link User} objects.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public interface UserService extends UserDetailsService {
	/**
	 * Updates the {@link User User's} record for a successful signing in.
	 * 
	 * @param user
	 *          The {@link User} to update.
	 * @param ipAddress
	 *          The ipAddress of where the user signed in from.
	 * 
	 * @return A {@link User} that has been updated accordingly.
	 * 
	 * @throws IllegalArgumentException
	 *           if the <code>user</code> is <code>null</code>.
	 */
	public User signinSuccessful(final User user, final String ipAddress) throws IllegalArgumentException;

	/**
	 * Updates the {@link User User's} record for a failed sign in.
	 * 
	 * @param username
	 *          The username used trying to sign in.
	 * @param message
	 *          A descriptive message of why the sign in failed.
	 * 
	 * @throws IllegalArgumentException
	 *           if the <code>username</code> is <code>null</code> or empty.
	 */
	public void signinFailure(final String username, final String message) throws IllegalArgumentException;

	/**
	 * Clears out the session when the user signs out of the system.
	 * 
	 * @param user
	 *          The {@link User} that has signed out of the system.
	 */
	public void signedOut(final User user);

	/**
	 * Clears out the session when the user is timed out of the system due to inactivity.
	 * 
	 * @param user
	 *          The {@link User} that has timed out of the system.
	 */
	public void timedOut(final User user);

	/**
	 * Modify the current user's password. This should change the user's password in the persistent
	 * user repository.
	 * 
	 * @param user
	 *          The {@link User} to change the password for.
	 * @param oldPassword
	 *          The old / current password (for re-authentication if required).
	 * @param newPassword
	 *          The password to change to.
	 * @param confirmPassword
	 *          The re-entered password to change to.
	 * 
	 * @return A {@link User} that has been updated accordingly.
	 * 
	 * @throws BadCredentialsException
	 *           if the oldPassword isn't valid, if the newPassword is the same as the new password,
	 *           or if the newPassword is the same as the username
	 */
	public User changePassword(final User user, final String oldPassword, final String newPassword,
			final String confirmPassword) throws BadCredentialsException;

	/**
	 * Updates the user's record by clearing the locked date (unlocking) on the user's account.
	 * 
	 * @param unlockUser
	 *          The {@link User} to unlock.
	 * @param user
	 *          The {@link User} that is performing the unlock.
	 * 
	 * @return A {@link User} that has been updated accordingly.
	 */
	public User unlockAccount(final User unlockUser, final User user);

	public boolean doesUsernameExist(final String username);

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
	public User saveUser(final User builder, final User user);
}
