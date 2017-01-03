package com.cagst.swkroa.transaction;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.Comparator;
import java.util.List;

import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.deposit.Deposit;
import com.cagst.swkroa.deposit.DepositTransaction;
import com.cagst.swkroa.internal.BaseRepositoryJdbc;
import com.cagst.swkroa.internal.StatementLoader;
import com.cagst.swkroa.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * JDBC Template implementation of the {@link TransactionRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("transactionRepo")
public final class TransactionRepositoryJdbc extends BaseRepositoryJdbc implements TransactionRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRepositoryJdbc.class);

  private static final String GET_TRANSACTION_BY_UID             = "GET_TRANSACTION_BY_UID";
  private static final String GET_TRANSACTIONS_FOR_MEMBERSHIP    = "GET_TRANSACTIONS_FOR_MEMBERSHIP";
  private static final String GET_TRANSACTIONS_FOR_DEPOSIT       = "GET_TRANSACTIONS_FOR_DEPOSIT";
  private static final String GET_UNPAID_INVOICES                = "GET_UNPAID_INVOICES";

  private static final String GET_COUNT_OF_TRANSACTIONGROUPS_FOR_TYPE = "GET_COUNT_OF_TRANSACTIONGROUPS_FOR_TYPE";
  private static final String GET_TRANACTIONLGROUPS_FOR_TYPE          = "GET_TRANACTIONLGROUPS_FOR_TYPE";


  private static final String INSERT_TRANSACTION = "INSERT_TRANSACTION";
  private static final String UPDATE_TRANSACTION = "UPDATE_TRANSACTION";

  private static final String INSERT_TRANSACTION_ENTRY = "INSERT_TRANSACTION_ENTRY";
  private static final String UPDATE_TRANSACTION_ENTRY = "UPDATE_TRANSACTION_ENTRY";

  private final CodeValueRepository codeValueRepo;

  /**
   * Primary Constructor used to create an instance of the TransactionRepositoryJdbc.
   *
   * @param dataSource
   *     The {@link DataSource} to used to retrieve / persists data object.
   * @param codeValueRepo
   *     The {@link CodeValueRepository} to use to retrieve additional attributes.
   */
  @Inject
  public TransactionRepositoryJdbc(DataSource dataSource, CodeValueRepository codeValueRepo) {
    super(dataSource);

    this.codeValueRepo = codeValueRepo;
  }

  @Override
  public Transaction getTransactionByUID(long uid)
      throws IncorrectResultSizeDataAccessException {

    LOGGER.info("Calling getTransactionByUID for [{}]", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<Transaction> trans = getJdbcTemplate().query(
        stmtLoader.load(GET_TRANSACTION_BY_UID),
        new MapSqlParameterSource("transaction_id", uid),
        new TransactionListExtractor(codeValueRepo)
    );

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

  @Override
  public List<Transaction> getTransactionsForMembership(long membershipUID) {
    LOGGER.info("Calling getTransactionsForMembership [{}].", membershipUID);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<Transaction> results = getJdbcTemplate().query(
        stmtLoader.load(GET_TRANSACTIONS_FOR_MEMBERSHIP),
        new MapSqlParameterSource("membership_id", membershipUID),
        new TransactionListExtractor(codeValueRepo));

    results.sort(Comparator.comparing(Transaction::getTransactionDate));
    return results;
  }

  @Override
  public long getCountOfTransactionGroupsForType(TransactionType type) {
    LOGGER.info("Calling getCountOfTransactionGroupsForInvoices");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().queryForObject(
        stmtLoader.load(GET_COUNT_OF_TRANSACTIONGROUPS_FOR_TYPE),
        new MapSqlParameterSource("transaction_type", type.ordinal()),
        Long.class
    );
  }

  @Override
  public List<TransactionGroup> getTransactionGroupsForType(TransactionType type, int start, int limit) {
    LOGGER.info("Calling getTransactionListForInvoices");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("transaction_type", type.ordinal());
    params.addValue("start", start);
    params.addValue("limit", limit);

    return getJdbcTemplate().query(
        stmtLoader.load(GET_TRANACTIONLGROUPS_FOR_TYPE),
        params,
        new TransactionGroupMapper()
    );
  }

  @Override
  public List<DepositTransaction> getTransactionsForDeposit(Deposit deposit) {
    Assert.notNull(deposit, "Assertion Failed - argument [deposit] cannot be null.");

    LOGGER.info("Calling getTransactionsForDeposit [{}].", deposit.getDepositNumber());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(
        stmtLoader.load(GET_TRANSACTIONS_FOR_DEPOSIT),
        new MapSqlParameterSource("deposit_id", deposit.getDepositUID()),
        new DepositTransactionListExtractor(codeValueRepo)
    );
  }

  @Override
  public List<UnpaidInvoice> getUnpaidInvoices() {
    LOGGER.info("Calling getUnpaidInvoices");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().query(stmtLoader.load(GET_UNPAID_INVOICES), new UnpaidInvoiceListExtractor(codeValueRepo));
  }

  @Override
  public Transaction saveTransaction(Transaction transaction, User user) throws DataAccessException {
    Assert.notNull(transaction, "Argument [transaction] cannot be null");
    Assert.notNull(user, "Argument [user] cannot be null");

    LOGGER.info("Saving Transaction for Membership [{}]", transaction.getMembershipUID());

    if (CollectionUtils.isEmpty(transaction.getTransactionEntries())) {
      throw new IncorrectResultSizeDataAccessException(1, 0);
    }

    Transaction trans;
    if (transaction.getTransactionUID() == 0L) {
      trans = insertTransaction(transaction, user);
    } else {
      trans = updateTransaction(transaction, user);
    }

    // save the entries related to this transaction
    for (TransactionEntry entry : transaction.getTransactionEntries()) {
      // ensure the entry is associated with the transaction
      entry.setTransaction(trans);

      // ensure the entry has the appropriate polarity (invoices are negative / payments are positive)
      if (TransactionType.INVOICE.equals(trans.getTransactionType())) {
        entry.setTransactionEntryAmount(entry.getTransactionEntryAmount().abs().negate());
      } else {
        entry.setTransactionEntryAmount(entry.getTransactionEntryAmount().abs());
      }

      saveTransactionEntry(entry, user);
    }

    return trans;
  }

  private Transaction insertTransaction(Transaction transaction, User user) {
    LOGGER.info("Inserting Transaction for Membership [{}]", transaction.getMembershipUID());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_TRANSACTION),
        TransactionMapper.mapInsertStatement(transaction, user), keyHolder);

    if (cnt == 1) {
      transaction.setTransactionUID(keyHolder.getKey().longValue());
    } else {
      throw new IncorrectResultSizeDataAccessException("Failed to insert Transaction: expected 1, actual " + cnt, 1, cnt);
    }

    return transaction;
  }

  private Transaction updateTransaction(Transaction transaction, User user) {
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
      throw new IncorrectResultSizeDataAccessException("Failed to update Transaction: expected 1, actual " + cnt, 1, cnt);
    }

    return transaction;
  }

  private TransactionEntry saveTransactionEntry(TransactionEntry entry, User user) {
    if (entry.getTransactionEntryUID() == 0L) {
      return insertTransactionEntry(entry, user);
    } else {
      return updateTransactionEntry(entry, user);
    }
  }

  private TransactionEntry insertTransactionEntry(TransactionEntry entry, User user) {
    LOGGER.info("Inserting TransactionEntry for Transaction [{}]", entry.getTransactionEntryUID());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_TRANSACTION_ENTRY),
        TransactionEntryMapper.mapInsertStatement(entry, user), keyHolder);

    if (cnt == 1) {
      entry.setTransactionEntryUID(keyHolder.getKey().longValue());
    } else {
      throw new IncorrectResultSizeDataAccessException("Failed to insert TransactionEntry: expected 1, actual " + cnt, 1, cnt);
    }

    return entry;
  }

  private TransactionEntry updateTransactionEntry(TransactionEntry entry, User user) {
    LOGGER.info("Updating TransactionEntry for Transaction [{}]", entry.getTransactionEntryUID());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_TRANSACTION_ENTRY),
        TransactionEntryMapper.mapUpdateStatement(entry, user));

    if (cnt == 1) {
      entry.setTransactionEntryUpdateCount(entry.getTransactionEntryUpdateCount() + 1);
    } else if (cnt == 0) {
      throw new OptimisticLockingFailureException("invalid update count of [" + cnt
          + "] possible update count mismatch");
    } else {
      throw new IncorrectResultSizeDataAccessException("Failed to update TransactionEntry: expected 1, actual " + cnt, 1, cnt);
    }

    return entry;
  }
}
