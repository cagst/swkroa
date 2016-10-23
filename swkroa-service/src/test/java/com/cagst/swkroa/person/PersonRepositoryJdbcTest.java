package com.cagst.swkroa.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.sql.DataSource;

import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.contact.ContactRepository;
import com.cagst.swkroa.test.BaseTestRepository;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Test class for PersonRepositoryJdbc class.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class PersonRepositoryJdbcTest extends BaseTestRepository {
  private PersonRepositoryJdbc repo;

  private ContactRepository contactRepo;

  private User user;

  @Before
  public void setUp() {
    user = new User();
    user.setUserUID(1L);

    contactRepo = Mockito.mock(ContactRepository.class);

    DataSource dataSource = createTestDataSource();

    repo = new PersonRepositoryJdbc(dataSource, contactRepo);
    repo.setStatementDialect(StatementLoader.HSQLDB_DIALECT);
  }

  /**
   * Test the getPersonByUID and not finding the person.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetPersonByUID_NoneFound() {
    repo.getPersonByUID(999L);
  }

  /**
   * Test the getPersonByUID and finding the person.
   */
  @Test
  public void testGetPersonByUID_Found() {
    Person person = repo.getPersonByUID(1L);

    assertNotNull("Ensure the person is not NULL!", person);
    assertEquals("Ensure we found the correct person!", 1, person.getPersonUID());
    assertEquals("Ensure we found the correct person (by name)!", "Gaskill", person.getLastName());
  }

  /**
   * Test the savePerson method by inserting a new Person.
   */
  @Test
  public void testSavePerson_Insert() {
    Person person = new Person();
    person.setLastName("Person");
    person.setFirstName("Test");

    assertEquals("Ensure our new Person doesn't have an Id yet.", 0, person.getPersonUID());

    Person newPerson = repo.savePerson(person, UserType.STAFF, user);
    assertNotNull("Ensure we have a new Person.", newPerson);
    assertTrue("Ensure the Person has an Id.", newPerson.getPersonUID() > 0L);
    assertEquals("Ensure it is the same person.", newPerson.getLastName(), person.getLastName());
  }

  /**
   * Test the savePerson method by updating a Person.
   */
  @Test
  public void testSavePerson_Update() {
    Person person = new Person();
    person.setPersonUID(1L);
    person.setLastName("Gaskill");
    person.setFirstName("Craig");

    Person updatedPerson = repo.savePerson(person, UserType.STAFF, user);
    assertNotNull("Ensure we have a Person.", person);
    assertEquals("Ensure the Person Id is corect.", updatedPerson.getPersonUID(), person.getPersonUID());
    assertEquals("Ensure the Person update count has been incremented.", 1, updatedPerson.getPersonUpdateCount());
  }

  /**
   * Test the savePerson method by updating a Person by failing due to an invalid update count.
   */
  @Test(expected = OptimisticLockingFailureException.class)
  public void testSavePerson_Update_Failed() {
    Person person = new Person();
    person.setPersonUID(1L);
    person.setLastName("Gaskill");
    person.setFirstName("Craig");

    // force a failure due to update count
    person.setPersonUpdateCount(99L);

    repo.savePerson(person, UserType.STAFF, user);
  }
}
