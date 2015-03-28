package com.cagst.swkroa.member;

import java.util.List;
import java.util.Set;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.user.User;
import org.joda.time.DateTime;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Definitions of a service that retrieves and persists {@link Membership} objects.
 *
 * @author Craig Gaskill
 */
public interface MembershipService {
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
  public Membership getMembershipByUID(final long uid);

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
  public List<Membership> getMemberships(final Status status, final MembershipBalance balance);

  /**
   * Retrieves a {@link List} of {@link Membership Memberships} that contain the specified <i>name</i> in one of
   * the following fields:
   * <ul>
   * <li>first name</li>
   * <li>last name</li>
   * <li>company name</li>
   * <li>ownerid </li>
   * </ul>
   *
   * @param name
   *     The name to search for.
   * @param status
   *      The status of the Memberships to search for; 'all', 'active' or 'inactive'.
   * @param balance
   *      The balance group of the Memberships to search for; 'all', 'delinquent', 'paid', or 'credit'.
   *
   * @return A {@link List} of {@link Membership Memberships} that contain the specified name.
   */
  public List<Membership> getMembershipsForName(final String name, final Status status, final MembershipBalance balance);

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
  public Membership saveMembership(final Membership membership, final User user);

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
  public int closeMemberships(final Set<Long> membershipIds,
                              final CodeValue closeReason,
                              final String closeText,
                              final User user)
      throws DataAccessException;

  /**
   * Creates invoice transactions for the specified memberships and updated the next due date for the membership.
   *
   * @param transactionDate
   *        A {@link DateTime} that represents the date of the transaction.
   * @param transactionDescription
   *        A {@link String} that describes the transaction.
   * @param transactionMemo
   *        A {@link String} that provides additional information for the transaction.
   * @param membershipIds
   *        A {@link Set} of {@link Long Longs} that uniquely identify the membership to bill.
   * @param user
   *     The {@link User} that performed the changes.
   */
  public void billMemberships(final DateTime transactionDate,
                              final String transactionDescription,
                              final String transactionMemo,
                              final Set<Long> membershipIds,
                              final User user)
  throws DataAccessException;
}
