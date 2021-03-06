package com.cagst.swkroa.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.deposit.Deposit;
import com.cagst.swkroa.deposit.DepositTransaction;
import com.cagst.swkroa.test.BaseTestRepository;
import com.cagst.swkroa.user.User;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Test class for the TransactionRepositoryJdbc class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class TransactionRepositoryJdbcTest extends BaseTestRepository {
  private TransactionRepositoryJdbc repo;

  private final CodeValue typeDues = CodeValue.builder()
      .setCodeValueUID(1L)
      .setDisplay("Annual Dues")
      .setMeaning("ANNUAL DUES")
      .build();

  private final CodeValue typePayment = CodeValue.builder()
      .setCodeValueUID(2L)
      .setDisplay("Payment")
      .setMeaning("PAYMENT")
      .build();

  private final CodeValue typeSpecial = CodeValue.builder()
      .setCodeValueUID(3L)
      .setDisplay("Special Funds")
      .setMeaning("SPECIAL FUNDS")
      .build();

  @Before
  public void setUp() {
    CodeValueRepository codeValueRepo = mock(CodeValueRepository.class);

    when(codeValueRepo.getCodeValueByUID(1L)).thenReturn(typeDues);
    when(codeValueRepo.getCodeValueByUID(2L)).thenReturn(typePayment);
    when(codeValueRepo.getCodeValueByUID(3L)).thenReturn(typeSpecial);

    repo = new TransactionRepositoryJdbc(createTestDataSource(), codeValueRepo);
  }

  /**
   * Test the getTransactionByUID method and not finding the Transaction
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetTransactionByUID_NotFound() {
    repo.getTransactionByUID(999L);
  }

  /**
   * Test the getTransactionByUID method and finding the Transaction
   */
  @Test
  public void testGetTransactionByUID_Found() {
    Transaction trans = repo.getTransactionByUID(1L);

    assertNotNull("Ensure we found a transaction.", trans);
    assertEquals("Ensure it is the correct transaction (amount).", -90.00, trans.getTransactionAmount().doubleValue(), 0.001);
  }

  /**
   * Test the getTransactionsForMember method and not finding any Transactions
   */
  @Test
  public void testGetTransactionsForMembership_NoneFound() {
    List<Transaction> transactions = repo.getTransactionsForMembership(99L);
    assertNotNull("Ensure the transaction list exists.", transactions);
    assertTrue("Ensure the transaction list is empty.", transactions.isEmpty());
  }

  /**
   * Test the getTransactionsForMember method and finding Transactions
   */
  @Test
  public void testGetTransactionsForMembership_Found() {
    List<Transaction> transactions = repo.getTransactionsForMembership(2L);
    assertNotNull("Ensure the transaction list exists.", transactions);
    assertFalse("Ensure the transaction list is not empty.", transactions.isEmpty());
    assertEquals("Ensure we found the correct number of transactions.", 2, transactions.size());

    for (Transaction trans : transactions) {
      assertFalse("Ensure the transaction entries are not empty", trans.getTransactionEntries().isEmpty());
      assertEquals("Ensure each transaction has 2 entries", 2, trans.getTransactionEntries().size());
    }
  }

  /**
   * Test the getTransactionGroupsForType method and finding TransactionGroups for invoices.
   */
  @Test
  public void testGetTransactionGroupsForType() {
    List<TransactionGroup> groups = repo.getTransactionGroupsForType(TransactionType.INVOICE, 0, 20);

    assertNotNull("Ensure invoice transaction groups were found.", groups);
    assertEquals("Ensure we found the correct number of groups.", 1L, groups.size());
  }

  /**
   * Test the getTransactionsForDeposit method and not finding any Transactions.
   */
  @Test
  public void testGetTransactionsForDeposit_NoneFound() {
    Deposit deposit = new Deposit();
    deposit.setDepositUID(3L);

    List<DepositTransaction> transactions = repo.getTransactionsForDeposit(deposit);
    assertNotNull("Ensure the transaction list exists.", transactions);
    assertTrue("Ensure the transaction list is empty.", transactions.isEmpty());
  }

  /**
   * Test the getTransactionsForDeposit method and finding Transactions.
   */
  @Test
  public void testGetTransactionsForDeposit_Found() {
    Deposit deposit = new Deposit();
    deposit.setDepositUID(1L);

    List<DepositTransaction> transactions = repo.getTransactionsForDeposit(deposit);
    assertNotNull("Ensure the transaction list exists.", transactions);
    assertFalse("Ensure the transaction list is not empty.", transactions.isEmpty());
    assertEquals("Ensure we found the correct number of transactions.", 2, transactions.size());
  }

  /**
   * Test the getTransactionsForMember method and finding Transactions
   */
  @Test
  public void testGetUnpaidInvoices_Found() {
    List<UnpaidInvoice> transactions = repo.getUnpaidInvoices();
    assertNotNull("Ensure the transaction list exists.", transactions);
    assertFalse("Ensure the transaction list is not empty.", transactions.isEmpty());
    assertEquals("Ensure we found the correct number of transactions.", 2, transactions.size());
  }

  /**
   * Test the saveTransaction method by inserting a Transaction with no entries.
   */
  @Test(expected = IncorrectResultSizeDataAccessException.class)
  public void testSaveTransaction_Insert_NoEntries() {
    User user = new User();
    user.setUserUID(1L);

    List<Transaction> transactions1 = repo.getTransactionsForMembership(2L);
    assertNotNull("Ensure the transaction list exists.", transactions1);
    assertFalse("Ensure the transaction list is empty.", transactions1.isEmpty());
    assertEquals("Ensure we found the correct number of transactions.", 2, transactions1.size());

    Transaction newTrans = new Transaction();
    newTrans.setTransactionDate(new DateTime());
    newTrans.setTransactionType(TransactionType.INVOICE);
    newTrans.setMembershipUID(2L);

    repo.saveTransaction(newTrans, user);
  }

  /**
   * Test the saveTransaction method by inserting a Transaction
   */
  @Test
  public void testSaveTransaction_Insert() {
    User user = new User();
    user.setUserUID(1L);

    List<Transaction> transactions1 = repo.getTransactionsForMembership(2L);
    assertNotNull("Ensure the transaction list exists.", transactions1);
    assertFalse("Ensure the transaction list is empty.", transactions1.isEmpty());
    assertEquals("Ensure we found the correct number of transactions.", 2, transactions1.size());

    Transaction newTrans = new Transaction();
    newTrans.setTransactionDate(new DateTime());
    newTrans.setTransactionType(TransactionType.INVOICE);
    newTrans.setMembershipUID(2L);

    TransactionEntry newEntry = new TransactionEntry();
    newEntry.setTransactionEntryAmount(new BigDecimal(-25));
    newEntry.setTransactionEntryType(typeDues);
    newEntry.setTransaction(newTrans);

    newTrans.addEntry(newEntry);

    Transaction insertedTrans = repo.saveTransaction(newTrans, user);
    assertNotNull("Ensure we have a valid transaction", insertedTrans);
    assertTrue("Ensure the transaction has a valid ID.", insertedTrans.getTransactionUID() > 0L);
    assertEquals("Ensure it is the same transaction (amount).", newTrans.getTransactionAmount(),
        insertedTrans.getTransactionAmount());

    List<Transaction> transactions2 = repo.getTransactionsForMembership(2L);
    assertNotNull("Ensure the transaction list exists.", transactions2);
    assertFalse("Ensure the transaction list is empty.", transactions2.isEmpty());
    assertEquals("Ensure we found the correct number of transactions.", 3, transactions2.size());
  }

  /**
   * Test the saveTransaction method by updating a Transaction
   */
  @Test
  public void testSaveTransaction_Update() {
    User user = new User();
    user.setUserUID(1L);

    Transaction trans = repo.getTransactionByUID(2L);

    assertNotNull("Ensure we found a transaction.", trans);
    assertEquals("Ensure it is the correct transaction (amount).", -90.00, trans.getTransactionAmount().doubleValue(), 0.001);

    for (TransactionEntry entry : trans.getTransactionEntries()) {
      if (entry.getTransactionEntryAmount().compareTo(BigDecimal.valueOf(-20.00)) == 0) {
        entry.setTransactionEntryAmount(new BigDecimal(-35));
      }
    }

    Transaction updatedTrans = repo.saveTransaction(trans, user);
    assertNotNull("Ensure we have a valid transaction", updatedTrans);
    assertEquals("Ensure it has been updated.", 1, updatedTrans.getTransactionUpdateCount());
    assertEquals("Ensure it is still updated.", -105.00, updatedTrans.getTransactionAmount().doubleValue(), 0.001);
  }

  /**
   * Test the saveTransaction method by updating a Transaction but failing due to updt_cnt.
   */
  @Test(expected = OptimisticLockingFailureException.class)
  public void testSaveTransaction_Update_Failed() {
    User user = new User();
    user.setUserUID(1L);

    Transaction trans = repo.getTransactionByUID(2L);

    assertNotNull("Ensure we found a transaction.", trans);
    assertEquals("Ensure it is the correct transaction (amount).", -90.00, trans.getTransactionAmount().doubleValue(), 0.001);

    for (TransactionEntry entry : trans.getTransactionEntries()) {
      if (entry.getTransactionEntryAmount().compareTo(BigDecimal.valueOf(-20.00)) == 0) {
        entry.setTransactionEntryAmount(new BigDecimal(-35));
        entry.setTransactionEntryUpdateCount(entry.getTransactionEntryUpdateCount() + 1);
      }
    }

    repo.saveTransaction(trans, user);
  }
}
