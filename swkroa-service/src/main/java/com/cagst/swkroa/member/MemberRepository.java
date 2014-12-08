package com.cagst.swkroa.member;

import java.util.List;

import com.cagst.swkroa.person.PersonRepository;
import com.cagst.swkroa.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Definition of a repository that retrieves and persists {@link Member} and {@link Membership}
 * objects.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public interface MemberRepository extends PersonRepository {
  /**
   * Retrieves a {@link List} of all {@link Member Members} associated with the specified
   * Membership.
   *
   * @param membership
   *     The {@link Membership} to retrieve members for.
   *
   * @return A {@link List} of {@link Member Members} associated with the specified Membership.
   */
  public List<Member> getMembersForMembership(final Membership membership);

  /**
   * Retrieves a {@link Member} by its unique identifier.
   *
   * @param uid
   *     A {@link long} that uniquely identifies the member to retrieve.
   *
   * @return The {@link Member} that is associated with the specified uid.
   *
   * @throws EmptyResultDataAccessException
   *     if no Member was found.
   * @throws IncorrectResultSizeDataAccessException
   *     if more than 1 Member was found.
   */
  public Member getMemberByUID(final long uid) throws EmptyResultDataAccessException,
      IncorrectResultSizeDataAccessException;

  /**
   * Retrieves a {@link List} of all {@link MembershipCounty Counties} associated with the specified
   * Membership.
   *
   * @param membership
   *     The {@link Membership} to retrieve counties for.
   *
   * @return A {@link List} of {@link MembershipCounty Counties} associated with the specified
   * Membership.
   */
  public List<MembershipCounty> getMembershipCountiesForMembership(final Membership membership);

  /**
   * Retrieves a {@link MembershipCounty} by its unique identifier.
   *
   * @param uid
   *     A {@link long} that uniquely identifies the member to retrieve
   *
   * @return The {@link MembershipCounty} that is associated with the specified uid.
   *
   * @throws EmptyResultDataAccessException
   *     if no MembershipCounty was found.
   * @throws IncorrectResultSizeDataAccessException
   *     if more than 1 MembershipCounty was found.
   */
  public MembershipCounty getMembershipCountyByUID(final long uid) throws EmptyResultDataAccessException,
      IncorrectResultSizeDataAccessException;

  /**
   * Generates an Owner Id based upon the specified first-name and last-name.
   *
   * @param firstName
   *     The first-name to use to generate a new Owner Id.
   * @param lastName
   *     The last-name to use to generate a new Owner Id.
   *
   * @return A {@link String} that represents a unique Owner Id based upon the specified first /
   * last names.
   */
  public String generateOwnerId(final String firstName, final String lastName);

  /**
   * Commits the specified {@link Member Member} to persistent storage.
   *
   * @param member
   *     The {@link Member} to persist.
   * @param The
   *     {@link Membership} to associate this Member to.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link Member} once it has been committed to persistent storage.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws DataAccessException
   *     if the query fails
   */
  public Member saveMember(final Member member, final Membership membership, final User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, DataAccessException;

  /**
   * Commits the specified {@link MembershipCounty MembershipCounty} to persistent storage.
   *
   * @param builder
   *     The {@link MembershipCounty} to persist.
   * @param The
   *     {@link Membership} to associate this Member to.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MembershipCounty} once it has been committed to persistent storage.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws DataAccessException
   *     if the query fails
   */
  public MembershipCounty saveMembershipCounty(final MembershipCounty builder, final Membership membership,
                                               final User user) throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException,
      DataAccessException;
}
