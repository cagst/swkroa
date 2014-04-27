package com.cagst.swkroa.transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.member.MemberRepository;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.user.User;

/**
 * JDBC Template implementation of the {@link TransactionRepository} interface.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
@Repository("transactionRepo")
/* package */final class TransactionRepositoryJdbc extends BaseRepositoryJdbc implements TransactionRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRepositoryJdbc.class);

	private static final String GET_TRANSACTION_BY_UID = "GET_TRANSACTION_BY_UID";
	private static final String GET_TRANSACTIONS_FOR_MEMBERSHIP = "GET_TRANSACTIONS_FOR_MEMBERSHIP";

	private static final String INSERT_TRANSACTION = "INSERT_TRANSACTION";
	private static final String UPDATE_TRANSACTION = "UPDATE_TRANSACTION";

	private final CodeValueRepository codeValueRepo;
	private final MemberRepository memberRepo;

	/**
	 * Primary Constructor used to create an instance of the TransactionRepositoryJdbc.
	 * 
	 * @param dataSource
	 *          The {@link DataSource} to used to retrieve / persists data object.
	 * @param codeValueRepo
	 *          The {@link CodeValueRepository} to use to retrieve additional attributes.
	 * @param memberRepo
	 *          The {@link MemberRepository} to us to retrieve Member information.
	 */
	public TransactionRepositoryJdbc(final DataSource dataSource, final CodeValueRepository codeValueRepo,
			final MemberRepository memberRepo) {
		super(dataSource);

		this.codeValueRepo = codeValueRepo;
		this.memberRepo = memberRepo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.transaction.TransactionRepository#getTransactionByUID(long)
	 */
	@Override
	public Transaction getTransactionByUID(final long uid) throws EmptyResultDataAccessException,
			IncorrectResultSizeDataAccessException {

		LOGGER.info("Calling getTransactionByUID for [{}]", uid);

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("transaction_id", uid);

		List<Transaction> trans = getJdbcTemplate().query(stmtLoader.load(GET_TRANSACTION_BY_UID), params,
				new TransactionMapper(codeValueRepo, memberRepo));

		if (trans.size() == 1) {
			return trans.get(0);
		} else if (trans.size() == 0) {
			LOGGER.warn("Transaction with UID of [{}] was not found.", uid);
			throw new EmptyResultDataAccessException(1);
		} else {
			LOGGER.warn("More than one Transaction with UID of [{}] was found.", uid);
			throw new IncorrectResultSizeDataAccessException(1, trans.size());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cagst.swkroa.transaction.TransactionRepository#getTransactionsForMembership(com.cagst.swkroa.member.Membership)
	 */
	@Override
	public List<Transaction> getTransactionsForMembership(final Membership membership) {
		Assert.notNull(membership, "Assertion Failed - argument [membership] cannot be null.");

		LOGGER.info("Calling getTransactionsForMembership [{}].", membership.getMembershipUID());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		Map<String, Long> params = new HashMap<String, Long>();
		params.put("membership_id", membership.getMembershipUID());

		return getJdbcTemplate().query(stmtLoader.load(GET_TRANSACTIONS_FOR_MEMBERSHIP), params,
				new TransactionMapper(codeValueRepo, memberRepo));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.transaction.TransactionRepository#saveTransaction(com.cagst.swkroa.transaction.Transaction,
	 * com.cagst.swkroa.user.User)
	 */
	@Override
	public Transaction saveTransaction(final Transaction transaction, final User user)
			throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, DataAccessException {

		Assert.notNull(transaction, "Assertion Failed - argument [transaction] cannot be null");
		Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

		LOGGER.info("Saving Transaction for Membership [{}]", transaction.getMembershipUID());

		if (transaction.getTransactionUID() == 0L) {
			return insertTransaction(transaction, user);
		} else {
			return updateTransaction(transaction, user);
		}
	}

	private Transaction insertTransaction(final Transaction transaction, final User user) {
		LOGGER.info("Inserting Transaction for Membership [{}]", transaction.getMembershipUID());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		KeyHolder keyHolder = new GeneratedKeyHolder();

		int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_TRANSACTION),
				TransactionMapper.mapInsertStatement(transaction, user), keyHolder);

		if (cnt == 1) {
			transaction.setTransactionUID(keyHolder.getKey().longValue());
		} else {
			throw new IncorrectResultSizeDataAccessException(1, cnt);
		}

		return transaction;
	}

	private Transaction updateTransaction(final Transaction transaction, final User user) {
		LOGGER.info("Updating Transaction for Membership [{}]", transaction.getMembershipUID());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

		int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_TRANSACTION),
				TransactionMapper.mapUpdateStatement(transaction, user));

		if (cnt == 1) {
			transaction.setTransactionUpdateCount(transaction.getTransactionUpdateCount() + 1);
		} else if (cnt == 0) {
			throw new OptimisticLockingFailureException("invalid update count of [" + cnt
					+ "] possible update count mismatch");
		} else {
			throw new IncorrectResultSizeDataAccessException(1, cnt);
		}

		return transaction;
	}
}