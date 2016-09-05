package com.cagst.swkroa.deposit;

import com.cagst.swkroa.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.List;

/**
 * Definition of a repository that retrieves and persists {@link Deposit} objects.
 *
 * @author Craig Gaskill
 */
public interface DepositRepository {
  /**
   * Retrieves a {@link List} of all {@link Deposit Deposits} within the system.
   *
   * @return A {@link List} of {@link Deposit Deposits} within the system.
   */
  List<Deposit> getDeposits();

  /**
   * Retrieves a {@link Deposit} by its unique identifier.
   *
   * @param uid
   *      A {@link long} that uniquely identifies the {@link Deposit} to retrieve.
   *
   * @return A {@link Deposit} that is identified by the specified uid.
   *
   * @throws EmptyResultDataAccessException
   *     if no Member was found.
   * @throws IncorrectResultSizeDataAccessException
   *     if more than 1 Member was found.
   */
  Deposit getDeposit(final long uid) throws IncorrectResultSizeDataAccessException;

  /**
   * Persists the specified {@link Deposit}.
   *
   * @param deposit
   *    The {@link Deposit} to persist.
   * @param user
   *    The {@link User} that performed the changes.
   *
   * @return A {@link Deposit} after is has been persisted.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws DataAccessException
   *     if the query fails
   */
  Deposit saveDeposit(final Deposit deposit, final User user) throws DataAccessException;
}
