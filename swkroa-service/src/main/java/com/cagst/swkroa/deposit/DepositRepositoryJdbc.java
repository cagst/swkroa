package com.cagst.swkroa.deposit;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.transaction.TransactionEntry;
import com.cagst.swkroa.transaction.TransactionRepository;
import com.cagst.swkroa.transaction.TransactionType;
import com.cagst.swkroa.user.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC Template implementation of the {@link DepositRepository} interface.
 *
 * @author Craig Gaskill
 */
@Repository("depositRepository")
/* package */ final class DepositRepositoryJdbc extends BaseRepositoryJdbc implements DepositRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(DepositRepositoryJdbc.class);

  private static final String GET_DEPOSITS = "GET_DEPOSITS";
  private static final String GET_DEPOSIT  = "GET_DEPOSIT";

  private static final String INSERT_DEPOSIT = "INSERT_DEPOSIT";
  private static final String UPDATE_DEPOSIT = "UPDATE_DEPOSIT";

  private static final String INSERT_DEPOSIT_TRANSACTION = "INSERT_DEPOSIT_TRANSACTION";
  private static final String UPDATE_DEPOSIT_TRANSACTION = "UPDATE_DEPOSIT_TRANSACTION";

  private final TransactionRepository transactionRepo;

  /**
   * Primary Constructor used to create an instance of <i>DepositRepositoryJdbc</i>.
   *
   * @param dataSource
   *     The {@link DataSource} to use to retrieve / persist data objects.
   */
  @Inject
  public DepositRepositoryJdbc(final DataSource dataSource, final TransactionRepository transactionRepo) {
    super(dataSource);

    this.transactionRepo = transactionRepo;
  }

  @Override
  public List<Deposit> getDeposits() {
    LOGGER.info("Calling getDeposits");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    return getJdbcTemplate().query(stmtLoader.load(GET_DEPOSITS), new DepositMapper());
  }

  @Override
  public Deposit getDeposit(final long uid) {
    LOGGER.info("Calling getDeposit for [{}]", uid);

    Map<String, Long> params = new HashMap<>(1);
    params.put("deposit_id", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<Deposit> deposits = getJdbcTemplate().query(stmtLoader.load(GET_DEPOSIT), params, new DepositMapper());
    if (deposits.size() == 1) {
      return deposits.get(0);
    } else if (deposits.size() == 0) {
      LOGGER.warn("Deposit with UID of [{}] was not found.", uid);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.warn("More than one Deposit with UID of [{}] was found.", uid);
      throw new IncorrectResultSizeDataAccessException(1, deposits.size());
    }
  }

  @Override
  public Deposit saveDeposit(Deposit deposit, User user) throws DataAccessException {
    Assert.notNull(deposit, "Assertion Failed - argument [deposit] cannot be null");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Saving Deposit for [{}]", deposit.getDepositNumber());

    if (CollectionUtils.isEmpty(deposit.getTransactions())) {
      throw new IncorrectResultSizeDataAccessException(1, 0);
    }

    Deposit dep;
    if (deposit.getDepositUID() == 0L) {
      dep = insertDeposit(deposit, user);
    } else {
      dep = updateDeposit(deposit, user);
    }

    // save the transactions related to this deposit
    for (DepositTransaction trans : deposit.getTransactions()) {
      if (trans.getDepositTransactionUID() == 0L) {
        // Insert new Transaction

        // if the transaction is an INVOICE we need to generate a PAYMENT
        if (trans.getTransactionType() == TransactionType.INVOICE) {
          DepositTransaction depositTransaction = new DepositTransaction();
          depositTransaction.setMembershipUID(trans.getMembershipUID());
          depositTransaction.setTransactionType(TransactionType.PAYMENT);
          depositTransaction.setTransactionDate(deposit.getDepositDate());
          depositTransaction.setTransactionDescription(StringUtils.replace(trans.getTransactionDescription(), "Invoice", "Payment"));

          for (TransactionEntry entry : trans.getTransactionEntries()) {
            if (entry.getTransactionEntryAmount().compareTo(BigDecimal.ZERO) == 1) {
              TransactionEntry paymentEntry = new TransactionEntry();
              paymentEntry.setTransaction(depositTransaction);
              paymentEntry.setTransactionEntryType(entry.getTransactionEntryType());
              paymentEntry.setTransactionEntryAmount(entry.getTransactionEntryAmount().negate());
              paymentEntry.setRelatedTransaction(trans);

              depositTransaction.addEntry(paymentEntry);
            }
          }

          Transaction savedTransaction = transactionRepo.saveTransaction(depositTransaction, user);

          // save the associations of the transaction to the deposit
          long depositTransactionUID = associateTransactionToDeposit(savedTransaction, deposit, user);
          depositTransaction.setDepositTransactionUID(depositTransactionUID);
        }
      } else if (!trans.isDepositTransactionActive() || !trans.isActive()) {
        // Delete (disassociate) existing Transaction (Relationship)
        trans.setActive(false);
        transactionRepo.saveTransaction(trans, user);

        trans.setDepositTransactionActive(false);
        disassociateDepositTransaction(trans, user);
      }
    }

    return dep;
  }

  private Deposit insertDeposit(final Deposit deposit, final User user) {
    LOGGER.info("Inserting Deposit [{}]", deposit.getDepositNumber());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_DEPOSIT),
        DepositMapper.mapInsertStatement(deposit, user), keyHolder);

    if (cnt == 1) {
      deposit.setDepositUID(keyHolder.getKey().longValue());
    } else {
      throw new IncorrectResultSizeDataAccessException("Failed to insert Deposit: expected 1, actual " + cnt, 1, cnt);
    }

    return deposit;
  }

  private Deposit updateDeposit(final Deposit deposit, final User user) {
    LOGGER.info("Updating Deposit [{}]", deposit.getDepositNumber());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_DEPOSIT),
        DepositMapper.mapUpdateStatement(deposit, user));

    if (cnt == 1) {
      deposit.setDepositUpdateCount(deposit.getDepositUpdateCount() + 1);
    } else if (cnt == 0) {
      throw new OptimisticLockingFailureException("invalid update count of [" + cnt + "] possible update count mismatch");
    } else {
      throw new IncorrectResultSizeDataAccessException("Failed to update Deposit: expected 1, actual " + cnt, 1, cnt);
    }

    return deposit;
  }

  private long associateTransactionToDeposit(final Transaction transaction, final Deposit deposit, final User user) {
    LOGGER.info("Calling associateTransactionToDeposit.");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("deposit_id", deposit.getDepositUID());
    params.addValue("transaction_id", transaction.getTransactionUID());
    params.addValue("active_ind", true);
    params.addValue("create_id", user.getUserUID());
    params.addValue("updt_id", user.getUserUID());

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_DEPOSIT_TRANSACTION), params, keyHolder);
    if (cnt == 1) {
      return keyHolder.getKey().longValue();
    } else {
      return 0L;
    }
  }

  private void disassociateDepositTransaction(final DepositTransaction transaction, final User user) {
    LOGGER.info("Calling disassociateDepositTransaction.");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("active_ind", true);
    params.addValue("updt_id", user.getUserUID());
    params.addValue("deposit_transaction_id", transaction.getDepositTransactionUID());

    getJdbcTemplate().update(stmtLoader.load(UPDATE_DEPOSIT_TRANSACTION), params);
  }
}
