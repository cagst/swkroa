package com.cagst.swkroa.deposit;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

/**
 * Definition of a repository that retrieves and persists {@link Deposit} objects.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public interface DepositRepository {
  /**
   * Retrieves a {@link List} of all {@link Deposit Deposits} within the system.
   *
   * @return A {@link List} of {@link Deposit Deposits} within the system.
   */
  public List<Deposit> getDeposits();

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
  public Deposit getDeposit(final long uid) throws EmptyResultDataAccessException, IncorrectResultSizeDataAccessException;
}
