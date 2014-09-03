package com.cagst.swkroa.contact;

import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.person.Person;
import com.cagst.swkroa.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.List;

/**
 * Definition of a repository that retrieves and persist contact objects.
 * <ul>
 * <li>Address</li>
 * <li>Phone</li>
 * <li>EmailAddress</li>
 * </ul>
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public interface ContactRepository {
  public static final String ENTITY_MEMBER = "MEMBER";
  public static final String ENTITY_PERSON = "PERSON";

  /**
   * Retrieves a {@link List} of {@link Address Addresses} associated to the specified
   * {@link Member}.
   *
   * @param member
   *     The {@link Member} to retrieve addresses for.
   *
   * @return A {@link List} of {@link Address Addresses} associated to the member.
   */
  public List<Address> getAddressesForMember(final Member member);

  /**
   * Retrieves a {@link List} of {@link Address Addresses} associated to the specified
   * {@link Person}.
   *
   * @param person
   *     The {@link Person} to retrieve addresses for.
   *
   * @return A {@link List} of {@link Address Addresses} associated to the person.
   */
  public List<Address> getAddressesForPerson(final Person person);

  /**
   * Retrieves a {@link List} of {@link PhoneNumber PhoneNumbers} associated to the specified
   * {@link Member}.
   *
   * @param member
   *     The {@link Member} to retrieve phone numbers for.
   *
   * @return A {@link List} of {@link PhoneNumber PhoneNumbers} associated to the member.
   */
  public List<PhoneNumber> getPhoneNumbersForMember(final Member member);

  /**
   * Retrieves a {@link List} of {@link PhoneNumber PhoneNumbers} associated to the specified
   * {@link Person}.
   *
   * @param person
   *     The {@link Person} to retrieve phone numbers for.
   *
   * @return A {@link List} of {@link PhoneNumber PhoneNumbers} associated to the person.
   */
  public List<PhoneNumber> getPhoneNumbersForPerson(final Person person);

  /**
   * Retrieves a {@link List} of {@link EmailAddress} associated to the specified {@link Member}.
   *
   * @param member
   *     The {@link Member} to retrieve the email addresses for.
   *
   * @return A {@link List} of {@link EmailAddress EmailAddresses} associated to the member.
   */
  public List<EmailAddress> getEmailAddressesForMember(final Member member);

  /**
   * Retrieves a {@link List} of {@link EmailAddress} associated to the specified {@link Person}.
   *
   * @param person
   *     The {@link Person} to retrieve the email addresses for.
   *
   * @return A {@link List} of {@link EmailAddress EmailAddresses} associated to the person.
   */
  public List<EmailAddress> getEmailAddressesForPerson(final Person person);

  /**
   * Commits the specified {@link Address Address} to persistent storage.
   *
   * @param address
   *     The {@link Address} to persist.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link Address} once it has been committed to persistent storage.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws DataAccessException
   *     if the query fails
   */
  public Address saveAddress(final Address address, final User user) throws OptimisticLockingFailureException,
      IncorrectResultSizeDataAccessException, DataAccessException;

  /**
   * Commits the specified {@link PhoneNumber PhoneNumber} to persistent storage.
   *
   * @param phoneNumber
   *     The {@link PhoneNumber} to persist.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link PhoneNumber} once it has been committed to persistent storage.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws DataAccessException
   *     if the query fails
   */
  public PhoneNumber savePhoneNumber(final PhoneNumber phoneNumber, final User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, DataAccessException;

  /**
   * Commits the specified {@link EmailAddress EmailAddress} to persistent storage.
   *
   * @param emailAddress
   *     The {@link EmailAddress} to persist.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link EmailAddress} once it has been committed to persistent storage.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws DataAccessException
   *     if the query fails
   */
  public EmailAddress saveEmailAddress(final EmailAddress emailAddress, final User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, DataAccessException;
}
