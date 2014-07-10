package com.cagst.swkroa.member;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

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
import org.springframework.dao.OptimisticLockingFailureException;

import com.cagst.common.db.DataSourceFactory;
import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.user.User;

/**
 * Test class for the MemberRepositoryJdbc class.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class MembershipRepositoryJdbcTest {
	private MembershipRepositoryJdbc repo;

	private CodeValueRepository codeValueRepo;
	private MemberRepository memberRepo;

	private final CodeValue associate = new CodeValue();
	private final CodeValue regular = new CodeValue();
	private final CodeValue family = new CodeValue();

	private User user;

	@Before
	public void setUp() throws Exception {
		user = new User();
		user.setUserUID(1L);

		codeValueRepo = Mockito.mock(CodeValueRepository.class);
		memberRepo = Mockito.mock(MemberRepository.class);

		associate.setCodeValueUID(1L);
		associate.setDisplay("Associate Membership");

		regular.setCodeValueUID(2L);
		regular.setDisplay("Regular Membership");

		family.setCodeValueUID(3L);
		family.setDisplay("Family Membership");

		Mockito.when(codeValueRepo.getCodeValueByUID(1L)).thenReturn(associate);
		Mockito.when(codeValueRepo.getCodeValueByUID(2L)).thenReturn(regular);
		Mockito.when(codeValueRepo.getCodeValueByUID(3L)).thenReturn(family);

		repo = new MembershipRepositoryJdbc(createTestDataSource(), memberRepo, codeValueRepo);
		repo.setStatementDialect(StatementLoader.HSQLDB_DIALECT);
	}

	/**
	 * Test the getMembershipsByName method and not finding any.
	 */
	@Test
	public void testGetMembershipsByName_NoneFound() {
		Collection<Membership> memberships = repo.getMembershipsByName("zzz");

		assertNotNull("Ensure the memberships collection is not null.", memberships);
		assertTrue("Ensure the memberships collection is emtpy.", memberships.isEmpty());
	}

	/**
	 * Test the getMembershipsByName method and finding Memberships.
	 */
	@Test
	public void testGetMembershipsByName_Found() {
		Collection<Membership> memberships1 = repo.getMembershipsByName("dori");

		assertNotNull("Ensure the memberships collection is not null.", memberships1);
		assertFalse("Ensure the memberships collection is not empty.", memberships1.isEmpty());
		assertEquals("Ensure we found the correct number of memberships.", 1, memberships1.size());

		Collection<Membership> memberships2 = repo.getMembershipsByName("dud");

		assertNotNull("Ensure the memberships collection is not null.", memberships2);
		assertFalse("Ensure the memberships collection is not empty.", memberships2.isEmpty());
		assertEquals("Ensure we found the correct number of memberships.", 2, memberships2.size());
	}

	/**
	 * Test the getActiveMemberships method.
	 */
	@Test
	public void testGetActiveMemberships_Found() {
		Collection<Membership> memberships = repo.getActiveMemberships();

		assertNotNull("Ensure the membersships collection is not null!", memberships);
		assertFalse("Ensure the membersships collection is not empty!", memberships.isEmpty());
		assertEquals("Ensure we found the correct number of membersships!", 5, memberships.size());
	}

	/**
	 * Test the getMembershipByUID method and not finding the membership.
	 */
	@Test(expected = EmptyResultDataAccessException.class)
	public void testGetMembershipByUID_NotFound() {
		repo.getMembershipByUID(999L);
	}

	/**
	 * Test the getMembershipByUID method and finding a membership.
	 */
	@Test
	public void testGetMembershipByUID_Found() {
		Membership membership = repo.getMembershipByUID(1L);
		assertNotNull("Ensure we found the membership.", membership);
	}

	/**
	 * Test the saveMembership method and inserting a membership.
	 */
	@Test
	public void testSaveMembership_Insert() {
		Collection<Membership> memberships1 = repo.getActiveMemberships();

		assertNotNull("Ensure the membersships collection is not null!", memberships1);
		assertFalse("Ensure the membersships collection is not empty!", memberships1.isEmpty());
		assertEquals("Ensure we found the correct number of membersships!", 5, memberships1.size());

		DateTime now = new DateTime();

		Membership builder = new Membership();
		builder.setNextDueDate(new DateTime(now.plusYears(1).toDate()));
		builder.setEntityType(codeValueRepo.getCodeValueByUID(1L));

		Membership membership = repo.saveMembership(builder, user);
		assertNotNull("Ensure the membership was created.", membership);
		assertTrue("Ensure the membership has a valid uid.", membership.getMembershipUID() > 0L);

		Collection<Membership> memberships2 = repo.getActiveMemberships();

		assertNotNull("Ensure the membersships collection is not null!", memberships2);
		assertFalse("Ensure the membersships collection is not empty!", memberships2.isEmpty());
		assertEquals("Ensure we found the correct number of membersships!", 6, memberships2.size());
	}

	/**
	 * Test the saveMembership method and updating a membership.
	 */
	@Test
	public void testSaveMembership_Update() {
		Membership membership1 = repo.getMembershipByUID(1L);
		assertNotNull("Ensure we found the membership.", membership1);

		membership1.setEntityType(family);

		Membership membership2 = repo.saveMembership(membership1, user);
		assertNotNull("Ensure we found the membership.", membership1);
		assertEquals("Ensure it is the same membership.", membership1.getMembershipUID(), membership2.getMembershipUID());
		assertEquals("Ensure it has been updated.", family, membership2.getEntityType());
		assertEquals("Ensure it has been updated.", 1, membership2.getMembershipUpdateCount());
	}

	/**
	 * Test the saveMembership method and updating a membership but failing (due to an invalid update count).
	 */
	@Test(expected = OptimisticLockingFailureException.class)
	public void testSaveMembership_Update_Failed() {
		Membership membership1 = repo.getMembershipByUID(1L);
		assertNotNull("Ensure we found the membership.", membership1);

		membership1.setEntityType(family);
		membership1.setMembershipUpdateCount(1L);

		repo.saveMembership(membership1, user);
	}

	private DataSource createTestDataSource() {
		Resource schemaLocation = new ClassPathResource("/testDb/schema.sql");
		Resource testDataLocation = new ClassPathResource("/testDb/test_data.sql");

		DataSourceFactory dsFactory = new DataSourceFactory("swkroadb", schemaLocation, testDataLocation);
		return dsFactory.getDataSource();
	}
}
