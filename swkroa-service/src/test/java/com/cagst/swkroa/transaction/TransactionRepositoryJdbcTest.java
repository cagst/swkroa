package com.cagst.swkroa.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.cagst.common.db.DataSourceFactory;
import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.member.MemberRepository;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.user.User;

/**
 * Test class for the TransactionRepositoryJdbc class.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
@RunWith(JUnit4.class)
public class TransactionRepositoryJdbcTest {
	private TransactionRepositoryJdbc repo;
	private CodeValueRepository codeValueRepo;

	private Member member;

	@Before
	public void setUp() {
		codeValueRepo = Mockito.mock(CodeValueRepository.class);

		CodeValue typeDues = new CodeValue();
		typeDues.setCodeValueUID(1L);
		typeDues.setDisplay("Annual Dues");
		typeDues.setMeaning("ANNUAL DUES");

		CodeValue typePayment = new CodeValue();
		typePayment.setCodeValueUID(2L);
		typePayment.setDisplay("Payment");
		typePayment.setMeaning("PAYMENT");

		CodeValue typeSpecial = new CodeValue();
		typeSpecial.setCodeValueUID(3L);
		typeSpecial.setDisplay("Special Funds");
		typeSpecial.setMeaning("SPECIAL FUNDS");

		Mockito.when(codeValueRepo.getCodeValueByUID(1L)).thenReturn(typeDues);
		Mockito.when(codeValueRepo.getCodeValueByUID(2L)).thenReturn(typePayment);
		Mockito.when(codeValueRepo.getCodeValueByUID(3L)).thenReturn(typeSpecial);

		MemberRepository memberRepo = Mockito.mock(MemberRepository.class);

		member = new Member();
		member.setMemberUID(2L);

		Mockito.when(memberRepo.getMemberByUID(2L)).thenReturn(member);

		repo = new TransactionRepositoryJdbc(createTestDataSource(), codeValueRepo, memberRepo);
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
		assertEquals("Ensure it is the correct transaction (amount).", 90.00, trans.getTransactionAmount().doubleValue(), 0.001);
	}

	/**
	 * Test the getTransactionsForMember method and not finding any Transactions
	 */
	@Test
	public void testGetTransactionsForMember_NoneFound() {
		Membership membership = new Membership();
		membership.setMembershipUID(99L);

		List<Transaction> transactions = repo.getTransactionsForMembership(membership);
		assertNotNull("Ensure the transaction list exists.", transactions);
		assertTrue("Ensure the transaction list is empty.", transactions.isEmpty());
	}

	/**
	 * Test the getTransactionsForMember method and finding Transactions
	 */
	@Test
	public void testGetTransactionsForMember_Found() {
		Membership membership = new Membership();
		membership.setMembershipUID(2L);

		List<Transaction> transactions = repo.getTransactionsForMembership(membership);
		assertNotNull("Ensure the transaction list exists.", transactions);
		assertFalse("Ensure the transaction list is not empty.", transactions.isEmpty());
		assertEquals("Ensure we found the correct number of transactions.", 2, transactions.size());

		for (Transaction trans : transactions) {
			assertFalse("Ensure the transaction entries are not empty", trans.getTransactionEntries().isEmpty());
			assertEquals("Ensure each transaction has 2 entries", 2, trans.getTransactionEntries().size());
		}
	}

	/**
	 * Test the saveTransaction method by inserting a Transaction with no entries.
	 */
	@Test(expected = IncorrectResultSizeDataAccessException.class)
	public void testSaveTransaction_Insert_NoEntries() {
		User user = new User();
		user.setUserUID(1L);

		Membership membership = new Membership();
		membership.setMembershipUID(2L);

		List<Transaction> transactions1 = repo.getTransactionsForMembership(membership);
		assertNotNull("Ensure the transaction list exists.", transactions1);
		assertFalse("Ensure the transaction list is empty.", transactions1.isEmpty());
		assertEquals("Ensure we found the correct number of transactions.", 3, transactions1.size());

		Transaction newTrans = new Transaction();
		newTrans.setTransactionDate(new DateTime());
		newTrans.setTransactionAmount(new BigDecimal(25));
		newTrans.setTransactionType(TransactionType.INVOICE);
		newTrans.setMembershipUID(membership.getMembershipUID());

		repo.saveTransaction(newTrans, user);
	}

	/**
	 * Test the saveTransaction method by inserting a Transaction
	 */
	@Test
	public void testSaveTransaction_Insert() {
		User user = new User();
		user.setUserUID(1L);

		Membership membership = new Membership();
		membership.setMembershipUID(2L);

		List<Transaction> transactions1 = repo.getTransactionsForMembership(membership);
		assertNotNull("Ensure the transaction list exists.", transactions1);
		assertFalse("Ensure the transaction list is empty.", transactions1.isEmpty());
		assertEquals("Ensure we found the correct number of transactions.", 3, transactions1.size());

		Transaction newTrans = new Transaction();
		newTrans.setTransactionDate(new DateTime());
		newTrans.setTransactionAmount(new BigDecimal(25));
		newTrans.setTransactionType(TransactionType.INVOICE);
		newTrans.setMembershipUID(membership.getMembershipUID());

		Transaction insertedTrans = repo.saveTransaction(newTrans, user);
		assertNotNull("Ensure we have a valid transaction", insertedTrans);
		assertTrue("Ensure the transaction has a valid ID.", insertedTrans.getTransactionUID() > 0L);
		assertEquals("Ensure it is the same transaction (amount).", newTrans.getTransactionAmount(),
				insertedTrans.getTransactionAmount());

		List<Transaction> transactions2 = repo.getTransactionsForMembership(membership);
		assertNotNull("Ensure the transaction list exists.", transactions2);
		assertFalse("Ensure the transaction list is empty.", transactions2.isEmpty());
		assertEquals("Ensure we found the correct number of transactions.", 4, transactions2.size());
	}

	/**
	 * Test the saveTransaction method by updating a Transaction
	 */
	@Test
	public void testSaveTransaction_Update() {
		User user = new User();
		user.setUserUID(1L);

		Transaction trans = repo.getTransactionByUID(1L);

		assertNotNull("Ensure we found a transaction.", trans);
		assertEquals("Ensure it is the correct transaction (amount).", 70.00, trans.getTransactionAmount().doubleValue(),
				0.001);

		trans.setTransactionAmount(new BigDecimal(100));

		Transaction updatedTrans = repo.saveTransaction(trans, user);
		assertNotNull("Ensure we have a valid transaction", updatedTrans);
		assertEquals("Ensure it has been updated.", 1, updatedTrans.getTransactionUpdateCount());
		assertEquals("Ensure it is still updated.", 100.00, updatedTrans.getTransactionAmount().doubleValue(), 0.001);
	}

	/**
	 * Test the saveTransaction method by updating a Transaction but failing due to updt_cnt.
	 */
	@Test(expected = OptimisticLockingFailureException.class)
	public void testSaveTransaction_Update_Failed() {
		User user = new User();
		user.setUserUID(1L);

		Transaction trans = repo.getTransactionByUID(1L);

		assertNotNull("Ensure we found a transaction.", trans);
		assertEquals("Ensure it is the correct transaction (amount).", 70.00, trans.getTransactionAmount().doubleValue(),
				0.001);

		trans.setTransactionAmount(new BigDecimal(100));
		trans.setTransactionUpdateCount(trans.getTransactionUpdateCount() + 1);

		repo.saveTransaction(trans, user);
	}

	private DataSource createTestDataSource() {
		Resource schemaLocation = new ClassPathResource("/testDb/schema.sql");
		Resource testDataLocation = new ClassPathResource("/testDb/test_data.sql");

		DataSourceFactory dsFactory = new DataSourceFactory("swkroadb", schemaLocation, testDataLocation);
		return dsFactory.getDataSource();
	}
}
