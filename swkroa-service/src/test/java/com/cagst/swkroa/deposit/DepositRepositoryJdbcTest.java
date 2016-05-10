package com.cagst.swkroa.deposit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.List;

import com.cagst.swkroa.test.BaseTestRepository;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.transaction.TransactionRepository;
import com.cagst.swkroa.user.User;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * Test class for the {@link DepositRepositoryJdbc} class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class DepositRepositoryJdbcTest extends BaseTestRepository {
  private DepositRepositoryJdbc repo;

  private User user;

  @Before
  public void setUp() {
    TransactionRepository transactionRepo = mock(TransactionRepository.class);

    repo = new DepositRepositoryJdbc(createTestDataSource(), transactionRepo);

    user = new User();
    user.setUserUID(1L);
  }

  /**
   * Test the getDeposits method.
   */
  @Test
  public void testGetDeposits() {
    List<Deposit> deposits = repo.getDeposits();

    assertNotNull("Ensure the deposits is not null.", deposits);
    assertEquals("Ensure we have the correct number of Deposits.", 2, deposits.size());
  }

  /**
   * Test the getDeposit method and not finding any deposit.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetDeposit_NotFound() {
    repo.getDeposit(999L);
  }

  /**
   * Test the getDeposit method and finding a deposit.
   */
  @Test
  public void testGetDeposit_Found() {
    Deposit deposit = repo.getDeposit(1L);

    assertNotNull("Ensure the deposit is not null.", deposit);
    assertEquals("Ensure we found the correct deposit.", "REF1", deposit.getDepositNumber());
  }

  /**
   * Test the saveDeposit method by inserting a new deposit.
   */
  @Test
  public void testSaveDeposit_Insert() {
    List<Deposit> deposits1 = repo.getDeposits();

    assertNotNull("Ensure the deposits is not null.", deposits1);
    assertEquals("Ensure we have the correct number of Deposits.", 2, deposits1.size());

    DateTime depositDate = new DateTime(2016, 1, 23, 0, 0, 0);

    Deposit newDeposit = new Deposit();
    newDeposit.setDepositDate(depositDate);
    newDeposit.setDepositAmount(BigDecimal.valueOf(123.45));
    newDeposit.setDepositNumber("new_deposit");

    Transaction transaction1 = new Transaction();
    Transaction transaction2 = new Transaction();

    newDeposit.setTransactions(Lists.newArrayList(transaction1, transaction2));

    Deposit savedDeposit = repo.saveDeposit(newDeposit, user);
    assertNotNull("Ensure the deposit is not null.", savedDeposit);
    assertTrue("Ensure the saved deposit has an id", savedDeposit.getDepositUID() > 0L);

    List<Deposit> deposits2 = repo.getDeposits();

    assertNotNull("Ensure the deposits is not null.", deposits2);
    assertEquals("Ensure we have the correct number of Deposits.", 3, deposits2.size());
  }

  @Test
  public void testSaveDeposit_Update() {
    Deposit deposit1 = repo.getDeposit(1L);

    assertNotNull("Ensure the deposit is not null.", deposit1);
    assertEquals("Ensure we found the correct deposit.", "REF1", deposit1.getDepositNumber());

    deposit1.setDepositAmount(deposit1.getDepositAmount().add(BigDecimal.valueOf(100)));

    Transaction transaction1 = new Transaction();
    Transaction transaction2 = new Transaction();

    deposit1.setTransactions(Lists.newArrayList(transaction1, transaction2));

    Deposit savedDeposit = repo.saveDeposit(deposit1, user);
    assertNotNull("Ensure the deposit is not null.", savedDeposit);

    Deposit deposit2 = repo.getDeposit(1L);

    assertNotNull("Ensure the deposit is not null.", deposit2);
    assertEquals("Ensure we found the correct deposit.", "REF1", deposit2.getDepositNumber());
    assertEquals("Ensure the amount as updated", savedDeposit.getDepositAmount(), deposit2.getDepositAmount());
    assertEquals("Ensure the update count was updated", savedDeposit.getDepositUpdateCount(), deposit2.getDepositUpdateCount());
  }
}
