package com.cagst.swkroa.deposit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.test.BaseTestRepository;
import com.cagst.swkroa.transaction.TransactionRepositoryJdbc;
import com.cagst.swkroa.transaction.UnpaidInvoice;
import com.cagst.swkroa.user.User;
import jdk.nashorn.internal.ir.annotations.Ignore;
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
  private TransactionRepositoryJdbc transactionRepo;

  private User user;

  private CodeValue typeDues = CodeValue.builder()
      .setCodeValueUID(1L)
      .setDisplay("Annual Dues")
      .setMeaning("ANNUAL DUES")
      .build();

  private CodeValue typePayment = CodeValue.builder()
      .setCodeValueUID(2L)
      .setDisplay("Payment")
      .setMeaning("PAYMENT")
      .build();

  private CodeValue typeSpecial = CodeValue.builder()
      .setCodeValueUID(3L)
      .setDisplay("Special Funds")
      .setMeaning("SPECIAL FUNDS")
      .build();

  @Before
  public void setUp() {
    DataSource dataSource = createTestDataSource();

    CodeValueRepository codeValueRepo = mock(CodeValueRepository.class);

    when(codeValueRepo.getCodeValueByUID(1L)).thenReturn(typeDues);
    when(codeValueRepo.getCodeValueByUID(2L)).thenReturn(typePayment);
    when(codeValueRepo.getCodeValueByUID(3L)).thenReturn(typeSpecial);

    transactionRepo = new TransactionRepositoryJdbc(dataSource, codeValueRepo);
    repo = new DepositRepositoryJdbc(dataSource, transactionRepo);

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

    LocalDate depositDate = LocalDate.of(2016, 1, 23);

    Deposit newDeposit = new Deposit();
    newDeposit.setDepositDate(depositDate);
    newDeposit.setDepositAmount(BigDecimal.valueOf(123.45));
    newDeposit.setDepositNumber("new_deposit");

    List<UnpaidInvoice> unpaidInvoices1 = transactionRepo.getUnpaidInvoices();
    assertEquals("Ensure we have 2 unpaid transactions", 2, unpaidInvoices1.size());

    List<DepositTransaction> depositTransactions = unpaidInvoices1.stream()
        .map(t -> {
          DepositTransaction dt = new DepositTransaction();
          dt.setTransactionEntries(t.getTransactionEntries());

          return dt;
        })
        .collect(Collectors.toList());

    newDeposit.setTransactions(depositTransactions);

    Deposit savedDeposit = repo.saveDeposit(newDeposit, user);
    assertNotNull("Ensure the deposit is not null.", savedDeposit);
    assertTrue("Ensure the saved deposit has an id", savedDeposit.getDepositUID() > 0L);

    List<Deposit> deposits2 = repo.getDeposits();

    assertNotNull("Ensure the deposits is not null.", deposits2);
    assertEquals("Ensure we have the correct number of Deposits.", 3, deposits2.size());

    List<UnpaidInvoice> unpaidInvoices2 = transactionRepo.getUnpaidInvoices();
    assertEquals("Ensure we have 0 unpaid transactions", 2, unpaidInvoices2.size());
  }

  @Test
  @Ignore
  public void testSaveDeposit_Update() {
    Deposit deposit1 = repo.getDeposit(1L);

    assertNotNull("Ensure the deposit is not null.", deposit1);
    assertEquals("Ensure we found the correct deposit.", "REF1", deposit1.getDepositNumber());

    deposit1.setDepositAmount(deposit1.getDepositAmount().add(BigDecimal.valueOf(100)));

    List<DepositTransaction> transactions = transactionRepo.getTransactionsForDeposit(deposit1);
    deposit1.setTransactions(transactions);

    Deposit savedDeposit = repo.saveDeposit(deposit1, user);
    assertNotNull("Ensure the deposit is not null.", savedDeposit);

    Deposit deposit2 = repo.getDeposit(1L);

    assertNotNull("Ensure the deposit is not null.", deposit2);
    assertEquals("Ensure we found the correct deposit.", "REF1", deposit2.getDepositNumber());
    assertEquals("Ensure the amount as updated", savedDeposit.getDepositAmount(), deposit2.getDepositAmount());
    assertEquals("Ensure the update count was updated", savedDeposit.getDepositUpdateCount(), deposit2.getDepositUpdateCount());
  }
}
