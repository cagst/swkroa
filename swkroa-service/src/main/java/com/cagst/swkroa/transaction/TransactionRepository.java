package com.cagst.swkroa.transaction;

import java.util.List;

import com.cagst.swkroa.deposit.Deposit;
import com.cagst.swkroa.deposit.DepositTransaction;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Definition of a repository that retrieves and persists {@link Transaction} objects.
 *
 * @author Craig Gaskill
 */
public interface TransactionRepository {
  /**
   * Retrieves the {@link Transaction} associated with the specified unique identifier.
   *
   * @param uid
   *     A {@link long} that uniquely identifies the {@link Transaction} to retrieve.
   *
   * @return The {@link Transaction} associated with the specified uid.
   *
   * @throws EmptyResultDataAccessException
   *     if no {@link Transaction} was found.
   * @throws IncorrectResultSizeDataAccessException
   *     if more than 1 {@link Transaction} was found.
   */
  Transaction getTransactionByUID(final long uid) throws IncorrectResultSizeDataAccessException;

  /**
   * Retrieves a {@link List} of {@link Transaction Transactions} defined withing the system for the specified
   * {@link Membership}.
   *
   * @param membership
   *     The {@link Membership} to retrieve transactions for.
   *
   * @return A {@link List} of {@link Transaction Transactions} associated with the specified Membership.
   */
  List<Transaction> getTransactionsForMembership(final Membership membership);

  /**
   * @return The number of {@link TransactionGroup} defined in the system for the specified {@link TransactionType}.
   */
  long getCountOfTransactionGroupsForType(final TransactionType type);

  /**
   * Retrieves a {@link List} of {@link TransactionGroup TransactionGroups} representing a group of transactions
   * for a given types defined within the system.
   *
   * @param type
   *      The {@link TransactionType} to retrieve {@link TransactionGroup TransactionGroups} for.
   * @param start
   *      An {@link int} that defines the first element to retrieve.
   * @param limit
   *      An {@link int} that defines the number of elements to retrieve.
   *
   * @return A {@link List} of {@link TransactionGroup} representing a group of invoices created in the system.
   */
  List<TransactionGroup> getTransactionGroupsForType(final TransactionType type, final int start, final int limit);

  /**
   * Retrieves a {@link List} of {@link DepositTransaction Transactions} defined within the system for the specified
   * {@link Deposit}.
   *
   * @param deposit
   *    The {@link Deposit} to retrieve transactions for.
   *
   * @return A {@link List} of {@link DepositTransaction Transactions} associated with the specified Deposit.
   */
  List<DepositTransaction> getTransactionsForDeposit(final Deposit deposit);

  /**
   * Retrieves a {@link List} of {@link Transaction Transactions} that have not been paid in full.
   *
   * @return A {@link List} of {@link Transaction Transactions} that have not been paid in full.
   */
  List<UnpaidInvoice> getUnpaidInvoices();

  /**
   * Persists the specified {@link Transaction}.
   *
   * @param transaction
   *     The {@link Transaction} to persist.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link Transaction} after it has been persisted.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws DataAccessException
   *     if the query fails
   */
  Transaction saveTransaction(final Transaction transaction, final User user) throws DataAccessException;
}
