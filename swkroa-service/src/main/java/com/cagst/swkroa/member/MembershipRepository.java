package com.cagst.swkroa.member;

import java.util.List;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Definition of a repository that retrieves and persists {@link Membership} objects.
 *
 * @author Craig Gaskill
 */
public interface MembershipRepository {
  /**
   * Retrieves a {@link Membership} by its unique identifier.
   *
   * @param uid
   *     A {@link long} that uniquely identifies the membership to retrieve.
   *
   * @return The {@link Membership} that is associated with the specified uid.
   *
   * @throws EmptyResultDataAccessException
   *     if no Membership was found.
   * @throws IncorrectResultSizeDataAccessException
   *     if more than 1 Membership was found.
   */
  public Membership getMembershipByUID(final long uid) throws IncorrectResultSizeDataAccessException;

  /**
   * Retrieves the active {@link Membership Memberships} in the system.
   *
   * @param status
   *      The status of the Memberships to search for; 'all', 'active' or 'inactive'.
   * @param balance
   *      The balance group of the Memberships to search for; 'all', 'delinquent', 'paid', or 'credit'.
   *
   * @return A {@link List} of {@link Membership Memberships} in the system.
   */
  public List<Membership> getMemberships(final MembershipStatus status, final MembershipBalance balance);

  /**
   * Retrieves all active {@link Membership Memberships} in the system that has the name in one of the following fields:
   * <ul>
   * <li>OwnerIdent</li>
   * <li>Company Name</li>
   * <li>First Name</li>
   * <li>Last Name</li>
   * </ul>
   *
   * @param name
   *      The name to search for.
   * @param status
   *      The status of the Memberships to search for; 'all', 'active' or 'inactive'.
   * @param balance
   *      The balance group of the Memberships to search for; 'all', 'delinquent', 'paid', or 'credit'.
   *
   * @return A {@link List} of {@link Membership Memberships} in the system that starts with the specified name.
   */
  public List<Membership> getMembershipsByName(final String name, final MembershipStatus status, final MembershipBalance balance);

  /**
   * Retrieves all {@link Membership Memberships} that will be due in the following days.
   *
   * @param days
   *      The number of days to look ahead for memberships that will be due.
   *
   * @return A {@link List} of {@link Membership Memberships} that will be due in the following days.
   */
  public List<Membership> getMembershipsDueInXDays(final int days);

  /**
   * Commits the specified {@link Membership Membership} to persistent storage.
   *
   * @param membership
   *     The {@link Membership} to persist.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link Membership} once it has been committed to persistent storage.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws DataAccessException
   *     if the query fails
   */
  public Membership saveMembership(final Membership membership, final User user)
      throws DataAccessException;

  /**
   * Closes the memberships identified by their unique identifier for the specified reason.
   *
   * @param membershipIds
   *      A {@link List} of {@link Long} that uniquely identifies the memberships to close.
   * @param closeReason
   *      A {@link CodeValue} that specifies the reason for closure, if {@code null} then a closeText must be specified.
   * @param closeText
   *      A {@link String} that specifies the reason for closing if a closeReason is not specified.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return The number of memberships closed (modified)
   */
  public int closeMemberships(final List<Long> membershipIds, final CodeValue closeReason, final String closeText, final User user)
      throws DataAccessException;

}
