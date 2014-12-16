package com.cagst.swkroa.person;

import com.cagst.swkroa.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Definition of a repository that retrieves and persist contact objects.
 * <ul>
 * <li>Address</li>
 * <li>Phone</li>
 * <li>EmailAddress</li>
 * </ul>
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public interface PersonRepository {
  /**
   * Retrieves a {@link Person} by its unique identifier.
   *
   * @param uid
   *     A {@link long} that uniquely identifies the person to retrieve.
   *
   * @return The {@link Person} that is associated with the specified uid.
   *
   * @throws EmptyResultDataAccessException
   *     if no Member was found.
   * @throws IncorrectResultSizeDataAccessException
   *     if more than 1 Member was found.
   */
  public Person getPersonByUID(final long uid) throws EmptyResultDataAccessException,
      IncorrectResultSizeDataAccessException;

  /**
   * Commits the specified {@link Person Person} to persistent storage.
   *
   * @param person
   *     The {@link Person} to persist.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link Person} once it has been committed to persistent storage.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws DataAccessException
   *     if the query fails
   */
  public Person savePerson(final Person person, final User user) throws OptimisticLockingFailureException,
      IncorrectResultSizeDataAccessException, DataAccessException;
}
