package com.cagst.swkroa.contact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.sql.DataSource;
import java.util.Collection;

import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.test.BaseTestRepository;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Test class for PersonRepositoryJdbc class.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@RunWith(JUnit4.class)
public class ContactRepositoryJdbcTest extends BaseTestRepository {
  private ContactRepositoryJdbc repo;

  private User user;

  @Before
  public void setUp() {
    user = new User();
    user.setUserUID(1L);

    DataSource dataSource = createTestDataSource();

    repo = new ContactRepositoryJdbc(dataSource);
    repo.setStatementDialect(StatementLoader.HSQLDB_DIALECT);
  }

  /**
   * Test the getAddressesForMember method and not finding any addresses.
   */
  @Test
  public void testGetAddressesForPerson_NoneFound() {
    Member member = new Member();
    member.setMemberUID(3L);

    Collection<Address> addresses = repo.getAddressesForMember(member);
    assertNotNull("Ensure the addresses collection is not null.", addresses);
    assertTrue("Ensure the addresses collection is empty.", addresses.isEmpty());
  }

  /**
   * Test the getAddressesForMember method and finding addresses.
   */
  @Test
  public void testGetAddressesForPerson_Found() {
    Member member = new Member();
    member.setMemberUID(1L);

    Collection<Address> addresses = repo.getAddressesForMember(member);
    assertNotNull("Ensure the addresses collection is not null.", addresses);
    assertFalse("Ensure the addresses collection not is empty.", addresses.isEmpty());
    assertEquals("Ensure we found the correct number of addresses.", 2, addresses.size());
  }

  /**
   * Test the getPhoneNumbersForMember method and not finding phone numbers.
   */
  @Test
  public void testGetPhoneNumbersForPerson_NoneFound() {
    Member member = new Member();
    member.setMemberUID(3L);

    Collection<PhoneNumber> phones = repo.getPhoneNumbersForMember(member);
    assertNotNull("Ensure the phones collection is not null.", phones);
    assertTrue("Ensure the phones collection is empty.", phones.isEmpty());
  }

  /**
   * Test the getPhoneNumbersForMember method and finding phone numbers.
   */
  @Test
  public void testGetPhoneNumbersForPerson_Found() {
    Member member = new Member();
    member.setMemberUID(1L);

    Collection<PhoneNumber> phones = repo.getPhoneNumbersForMember(member);
    assertNotNull("Ensure the phones collection is not null.", phones);
    assertFalse("Ensure the phones collection not is empty.", phones.isEmpty());
    assertEquals("Ensure we found the correct number of phones.", 2, phones.size());
  }

  /**
   * Test the getEmailAddressesForMember method and not finding any email addresses.
   */
  @Test
  public void testGetEmailAddressesForPerson_NoneFound() {
    Member member = new Member();
    member.setMemberUID(3L);

    Collection<EmailAddress> emails = repo.getEmailAddressesForMember(member);
    assertNotNull("Ensure the emails collection is not null.", emails);
    assertTrue("Ensure the emails collection is empty.", emails.isEmpty());
  }

  /**
   * Test the getEmailAddressesForMember method and finding email addresses.
   */
  @Test
  public void testGetEmailAddressesForPerson_Found() {
    Member member = new Member();
    member.setMemberUID(1L);

    Collection<EmailAddress> emails = repo.getEmailAddressesForMember(member);
    assertNotNull("Ensure the emails collection is not null.", emails);
    assertFalse("Ensure the emails collection not is empty.", emails.isEmpty());
    assertEquals("Ensure we found the correct number of emails.", 2, emails.size());
  }

  /**
   * Test the saveAddress method and inserting an Address.
   */
  @Test
  public void testSaveAddress_Insert() {
    Member member = new Member();
    member.setMemberUID(1L);

    Collection<Address> addresses1 = repo.getAddressesForMember(member);
    assertNotNull("Ensure the addresses collection is not null.", addresses1);
    assertFalse("Ensure the addresses collection not is empty.", addresses1.isEmpty());
    assertEquals("Ensure we found the correct number of addresses.", 2, addresses1.size());

    Address address1 = addresses1.iterator().next();

    Address newAddress = new Address();
    newAddress.setParentEntityUID(member.getMemberUID());
    newAddress.setParentEntityName(UserType.MEMBER.name());
    newAddress.setAddressTypeCD(1L);
    newAddress.setAddressLine1("ADDRESS_LINE_1");
    newAddress.setCity("CITY");
    newAddress.setState(address1.getState());
    newAddress.setPostalCode("POSTAL_CODE");
    newAddress.setCountry(address1.getCountry());

    Address address2 = repo.saveAddress(newAddress, user);
    assertNotNull("Ensure we have a new address.", address2);
    assertTrue("Ensure we have an Id.", address2.getAddressUID() > 0);
    assertEquals("Ensure it is the correct address.", address2.getAddressLine1(), newAddress.getAddressLine1());

    Collection<Address> addresses2 = repo.getAddressesForMember(member);
    assertNotNull("Ensure the addresses collection is not null.", addresses2);
    assertFalse("Ensure the addresses collection not is empty.", addresses2.isEmpty());
    assertEquals("Ensure we found the correct number of addresses.", 3, addresses2.size());
  }

  /**
   * Test the saveAddress method and updating an Address.
   */
  @Test
  public void testSaveAddress_Update() {
    Member member = new Member();
    member.setMemberUID(1L);

    Collection<Address> addresses1 = repo.getAddressesForMember(member);
    assertNotNull("Ensure the addresses collection is not null.", addresses1);
    assertFalse("Ensure the addresses collection not is empty.", addresses1.isEmpty());
    assertEquals("Ensure we found the correct number of addresses.", 2, addresses1.size());

    Address address1 = addresses1.iterator().next();
    address1.setAddressLine1(address1.getAddressLine1() + "_EDITED");

    Address address2 = repo.saveAddress(address1, user);
    assertNotNull("Ensure we have a new address.", address2);
    assertEquals("Ensure the address was updated.", 1, address2.getAddressUpdateCount());
    assertEquals("Ensure it is the correct address.", address2.getAddressLine1(), address1.getAddressLine1());

    Collection<Address> addresses2 = repo.getAddressesForMember(member);
    assertNotNull("Ensure the addresses collection is not null.", addresses2);
    assertFalse("Ensure the addresses collection not is empty.", addresses2.isEmpty());
    assertEquals("Ensure we found the correct number of addresses.", 2, addresses2.size());
  }

  /**
   * Test the saveAddress method and failing to update an Address.
   */
  @Test(expected = OptimisticLockingFailureException.class)
  public void testSaveAddress_Update_Failed() {
    Member member = new Member();
    member.setMemberUID(1L);

    Collection<Address> addresses1 = repo.getAddressesForMember(member);
    assertNotNull("Ensure the addresses collection is not null.", addresses1);
    assertFalse("Ensure the addresses collection not is empty.", addresses1.isEmpty());
    assertEquals("Ensure we found the correct number of addresses.", 2, addresses1.size());

    Address address1 = addresses1.iterator().next();
    address1.setAddressLine1(address1.getAddressLine1() + "_EDITED");

    // force a failure due to update count
    address1.setAddressUpdateCount(99L);

    repo.saveAddress(address1, user);
  }

  /**
   * Test the savePhoneNumber method by inserting a new phone number.
   */
  @Test
  public void testSavePhoneNumber_Insert() {
    Member member = new Member();
    member.setMemberUID(1L);

    Collection<PhoneNumber> phones1 = repo.getPhoneNumbersForMember(member);
    assertNotNull("Ensure the phones collection is not null.", phones1);
    assertFalse("Ensure the phones collection not is empty.", phones1.isEmpty());
    assertEquals("Ensure we found the correct number of phones.", 2, phones1.size());

    PhoneNumber phone = new PhoneNumber();
    phone.setParentEntityUID(member.getMemberUID());
    phone.setParentEntityName(UserType.MEMBER.name());
    phone.setPhoneTypeCD(1L);
    phone.setPhoneNumber("NUMBER");
    phone.setPhoneExtension("EXT");

    PhoneNumber newPhone = repo.savePhoneNumber(phone, user);
    assertNotNull("Ensure we have a new phone.", newPhone);
    assertTrue("Ensure we have an Id.", newPhone.getPhoneUID() > 0);
    assertEquals("Ensure it is the correct phone.", newPhone.getPhoneNumber(), phone.getPhoneNumber());

    Collection<PhoneNumber> phones2 = repo.getPhoneNumbersForMember(member);
    assertNotNull("Ensure the phones collection is not null.", phones2);
    assertFalse("Ensure the phones collection not is empty.", phones2.isEmpty());
    assertEquals("Ensure we found the correct number of phones.", 3, phones2.size());
  }

  /**
   * Test the savePhoneNumber method by updating an existing phone number.
   */
  @Test
  public void testSavePhoneNumber_Update() {
    Member member = new Member();
    member.setMemberUID(1L);

    Collection<PhoneNumber> phones1 = repo.getPhoneNumbersForMember(member);
    assertNotNull("Ensure the phones collection is not null.", phones1);
    assertFalse("Ensure the phones collection not is empty.", phones1.isEmpty());
    assertEquals("Ensure we found the correct number of phones.", 2, phones1.size());

    PhoneNumber phone1 = phones1.iterator().next();
    phone1.setPhoneNumber(phone1.getPhoneNumber() + "00000");

    PhoneNumber phone2 = repo.savePhoneNumber(phone1, user);
    assertNotNull("Ensure we have a new phone number.", phone2);
    assertEquals("Ensure the phone number was updated.", 1, phone2.getPhoneUpdateCount());
    assertEquals("Ensure it is the correct phone number.", phone2.getPhoneNumber(), phone1.getPhoneNumber());

    Collection<PhoneNumber> phones2 = repo.getPhoneNumbersForMember(member);
    assertNotNull("Ensure the phones collection is not null.", phones2);
    assertFalse("Ensure the phones collection not is empty.", phones2.isEmpty());
    assertEquals("Ensure we found the correct number of phone numbers.", 2, phones2.size());
  }

  /**
   * Test the savePhoneNumber method by updating an existing phone number and failing due to invalid
   * update count.
   */
  @Test(expected = OptimisticLockingFailureException.class)
  public void testSavePhoneNumber_Update_Failed() {
    Member member = new Member();
    member.setMemberUID(1L);

    Collection<PhoneNumber> phones1 = repo.getPhoneNumbersForMember(member);
    assertNotNull("Ensure the phones collection is not null.", phones1);
    assertFalse("Ensure the phones collection not is empty.", phones1.isEmpty());
    assertEquals("Ensure we found the correct number of phones.", 2, phones1.size());

    PhoneNumber phone1 = phones1.iterator().next();
    phone1.setPhoneNumber(phone1.getPhoneNumber() + "00000");

    // force a failure due to update count
    phone1.setPhoneUpdateCount(99L);

    repo.savePhoneNumber(phone1, user);
  }

  /**
   * Test the saveEmailAddress method by insert a new email address.
   */
  @Test
  public void testSaveEmailAddress_Insert() {
    Member member = new Member();
    member.setMemberUID(1L);

    Collection<EmailAddress> emails = repo.getEmailAddressesForMember(member);
    assertNotNull("Ensure the emails collection is not null.", emails);
    assertFalse("Ensure the emails collection not is empty.", emails.isEmpty());
    assertEquals("Ensure we found the correct number of emails.", 2, emails.size());

    EmailAddress email = new EmailAddress();
    email.setParentEntityUID(member.getMemberUID());
    email.setParentEntityName(UserType.MEMBER.name());
    email.setEmailTypeCD(1L);
    email.setEmailAddress("emailme@aol.com");

    EmailAddress newEmail = repo.saveEmailAddress(email, user);
    assertNotNull("Ensure we have a new email.", newEmail);
    assertTrue("Ensure we have an Id.", newEmail.getEmailAddressUID() > 0);
    assertEquals("Ensure it is the correct email.", newEmail.getEmailAddress(), email.getEmailAddress());

    Collection<EmailAddress> emails2 = repo.getEmailAddressesForMember(member);
    assertNotNull("Ensure the emails collection is not null.", emails2);
    assertFalse("Ensure the emails collection not is empty.", emails2.isEmpty());
    assertEquals("Ensure we found the correct number of emails.", 3, emails2.size());
  }

  /**
   * Test the saveEmailAddress method by updating an existing email address.
   */
  @Test
  public void testSaveEmailAddress_Update() {
    Member member = new Member();
    member.setMemberUID(1L);

    Collection<EmailAddress> emails1 = repo.getEmailAddressesForMember(member);
    assertNotNull("Ensure the emails collection is not null.", emails1);
    assertFalse("Ensure the emails collection not is empty.", emails1.isEmpty());
    assertEquals("Ensure we found the correct number of emails.", 2, emails1.size());

    EmailAddress email1 = emails1.iterator().next();
    email1.setEmailAddress(email1.getEmailAddress() + "_EDITED");

    EmailAddress email2 = repo.saveEmailAddress(email1, user);
    assertNotNull("Ensure we have a new email address.", email2);
    assertEquals("Ensure the email address was updated.", 1, email2.getEmailAddressUpdateCount());
    assertEquals("Ensure it is the correct email address.", email2.getEmailAddress(), email1.getEmailAddress());

    Collection<EmailAddress> emails2 = repo.getEmailAddressesForMember(member);
    assertNotNull("Ensure the emails collection is not null.", emails2);
    assertFalse("Ensure the emails collection not is empty.", emails2.isEmpty());
    assertEquals("Ensure we found the correct number of email addresses.", 2, emails2.size());
  }

  /**
   * Test the saveEmailAddress method by updating an existing email address but failing.
   */
  @Test(expected = OptimisticLockingFailureException.class)
  public void testSaveEmailAddress_Update_Failed() {
    Member member = new Member();
    member.setMemberUID(1L);

    Collection<EmailAddress> emails1 = repo.getEmailAddressesForMember(member);
    assertNotNull("Ensure the emails collection is not null.", emails1);
    assertFalse("Ensure the emails collection not is empty.", emails1.isEmpty());
    assertEquals("Ensure we found the correct number of emails.", 2, emails1.size());

    EmailAddress email1 = emails1.iterator().next();
    email1.setEmailAddress(email1.getEmailAddress() + "_EDITED");

    // force a failure due to update count
    email1.setEmailAddressUpdateCount(99L);

    repo.saveEmailAddress(email1, user);
  }
}
