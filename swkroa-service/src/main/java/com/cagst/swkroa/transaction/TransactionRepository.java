package com.cagst.swkroa.transaction;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.user.User;

/**
 * Definition of a repository that retrieves and persists {@link Transaction} objects.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public interface TransactionRepository {
	/**
	 * Retrieves the {@link Transaction} associated with the specified unique identifier.
	 * 
	 * @param uid
	 *          A {@link long} that uniquely identifies the {@link Transaction} to retrieve.
	 * 
	 * @return The {@link Transaction} associated with the specified uid.
	 * 
	 * @throws EmptyResultDataAccessException
	 *           if no {@link Transaction} was found.
	 * @throws IncorrectResultSizeDataAccessException
	 *           if more than 1 {@link Transaction} was found.
	 */
	public Transaction getTransactionByUID(final long uid) throws EmptyResultDataAccessException,
			IncorrectResultSizeDataAccessException;

	/**
	 * Retrieves a {@link List} of {@link Transaction Transactions} defined withing the system for the specified
	 * {@link Membership}.
	 * 
	 * @param membership
	 *          The {@link Membership} to retrieve transactions for.
	 * 
	 * @return A {@link List} of {@link Transaction Transactions} associated with the specified Membership.
	 */
	public List<Transaction> getTransactionsForMembership(final Membership membership);

  /**
   * Retrieves a {@link List} of {@link Transaction Transactions} that have not been paid in full for the specified
   * Membership that is identified by the specified id.
   *
   * @param id
   *          A {@link long} that uniquely identifies the Membership to retrieve the unpaid invoices for.
   *
   * @return A {@link List} of {@link Transaction Transactions} that have not been paid in null for the specified membership.
   */
  public List<UnpaidInvoice> getUnpaidInvoicesForMembership(final long id);

	/**
	 * Persists the specified {@link Transaction}.
	 * 
	 * @param transaction
	 *          The {@link Transaction} to persist.
	 * @param user
	 *          The {@link User} that performed the changes.
	 * 
	 * @return A {@link Transaction} after it has been persisted.
	 * 
	 * @throws OptimisticLockingFailureException
	 *           if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
	 * @throws IncorrectResultSizeDataAccessException
	 *           if the number of rows inserted / updated exceeded the expected number
	 * @throws DataAccessException
	 *           if the query fails
	 */
	public Transaction saveTransaction(final Transaction transaction, final User user)
			throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, DataAccessException;
}
