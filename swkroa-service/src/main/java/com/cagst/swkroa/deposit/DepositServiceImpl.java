package com.cagst.swkroa.deposit;

import javax.inject.Inject;
import java.util.List;

import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.transaction.TransactionRepository;
import com.cagst.swkroa.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link DepositService} interface.
 *
 * @author Craig Gaskill
 */
@Service("depositService")
public class DepositServiceImpl implements DepositService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DepositServiceImpl.class);

  private final DepositRepository depositRepo;
  private final TransactionRepository transactionRepo;

  /**
   * Primary Constructor used to create an instance of <i>DepositService</i>.
   *
   * @param depositRepo
   *      The {@link DepositRepository} to use to retrieve information about {@link Deposit Deposits}.
   * @param transactionRepo
   *      The {@link TransactionRepository} to use to retrieve information about {@link Transaction Transactions} that
   *      are related to Deposits.
   */
  @Inject
  public DepositServiceImpl(DepositRepository depositRepo, TransactionRepository transactionRepo) {
    this.depositRepo = depositRepo;
    this.transactionRepo = transactionRepo;
  }

  @Override
  public List<Deposit> getDeposits() {
    LOGGER.info("Calling getDeposits");

    return depositRepo.getDeposits();
  }

  @Override
  public Deposit getDeposit(long uid) {
    LOGGER.info("Calling getDeposit for [{}].", uid);

    Deposit deposit = depositRepo.getDeposit(uid);
    deposit.setTransactions(transactionRepo.getTransactionsForDeposit(deposit));

    return deposit;
  }

  @Override
  @Transactional
  public Deposit saveDeposit(Deposit deposit, User user) {
    LOGGER.info("Calling saveDeposit for [{}]", deposit.getDepositNumber());

    return depositRepo.saveDeposit(deposit, user);
  }
}
