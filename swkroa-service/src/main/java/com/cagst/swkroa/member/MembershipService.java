package com.cagst.swkroa.member;

import java.util.List;

import com.cagst.swkroa.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Definitions of a service that retrieves and persists {@link Membership} objects.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public interface MembershipService {
  /**
   * Retrieves the active {@link Membership Memberships} in the system.
   *
   * @return A {@link List} of {@link Membership Memberships} in the system.
   */
  public List<Membership> getActiveMemberships();

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
   *
   * @return A {@link List} of {@link Membership Memberships} that contain the specified name.
   */
  public List<Membership> getMembershipsForName(final String name);

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
}
