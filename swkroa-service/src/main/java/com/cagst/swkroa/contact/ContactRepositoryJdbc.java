package com.cagst.swkroa.contact;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;

import com.cagst.swkroa.internal.BaseRepositoryJdbc;
import com.cagst.swkroa.internal.StatementLoader;
import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.person.Person;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

/**
 * A JDBC Template implementation of the {@link ContactRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("contactRepository")
public class ContactRepositoryJdbc extends BaseRepositoryJdbc implements ContactRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(ContactRepositoryJdbc.class);

  private static final String GET_ADDRESSES_FOR_ENTITY = "GET_ADDRESSES_FOR_ENTITY";
  private static final String GET_PHONENUMBERS_FOR_ENTITY = "GET_PHONENUMBERS_FOR_ENTITY";
  private static final String GET_EMAILADDRESSES_FOR_ENTITY = "GET_EMAILADDRESSES_FOR_ENTITY";

  private static final String INSERT_ADDRESS = "INSERT_ADDRESS";
  private static final String UPDATE_ADDRESS = "UPDATE_ADDRESS";
  private static final String INSERT_PHONE = "INSERT_PHONE";
  private static final String UPDATE_PHONE = "UPDATE_PHONE";
  private static final String INSERT_EMAIL = "INSERT_EMAIL";
  private static final String UPDATE_EMAIL = "UPDATE_EMAIL";

  /**
   * Primary Constructor used to create an instance of <i>ContactRepositoryJdbc</i>.
   *
   * @param dataSource
   *     The {@link DataSource} used to retrieve / persist data objects.
   */
  @Inject
  public ContactRepositoryJdbc(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public List<Address> getAddressesForMember(Member member) {
    Assert.notNull(member, "Argument [member] cannot be null");

    LOGGER.info("Calling getAddressesForMember for [{}].", member.getMemberUID());

    return getAddressesForEntity(member.getMemberUID(), UserType.MEMBER.name());
  }

  @Override
  public List<Address> getAddressesForPerson(Person person) {
    Assert.notNull(person, "Argument [person] cannot be null");

    LOGGER.info("Calling getAddressesForPerson for [{}].", person.getPersonUID());

    return getAddressesForEntity(person.getPersonUID(), UserType.STAFF.name());
  }

  private List<Address> getAddressesForEntity(long id, String name) {
    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("parent_entity_id", id);
    params.addValue("parent_entity_name", name);

    return getJdbcTemplate().query(stmtLoader.load(GET_ADDRESSES_FOR_ENTITY), params, new AddressMapper());
  }

  @Override
  public List<PhoneNumber> getPhoneNumbersForMember(Member member) {
    Assert.notNull(member, "Argument [member] cannot be null");

    LOGGER.info("Calling getPhoneNumbersForMember for [{}].", member.getMemberUID());

    return getPhoneNumbersForEntity(member.getMemberUID(), UserType.MEMBER.name());
  }

  @Override
  public List<PhoneNumber> getPhoneNumbersForPerson(Person person) {
    Assert.notNull(person, "Argument [person] cannot be null");

    LOGGER.info("Calling getPhoneNumbersForPerson for [{}].", person.getPersonUID());

    return getPhoneNumbersForEntity(person.getPersonUID(), UserType.STAFF.name());
  }

  private List<PhoneNumber> getPhoneNumbersForEntity(long id, String name) {
    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("parent_entity_id", id);
    params.addValue("parent_entity_name", name);

    return getJdbcTemplate().query(stmtLoader.load(GET_PHONENUMBERS_FOR_ENTITY), params, new PhoneNumberMapper());
  }

  @Override
  public List<EmailAddress> getEmailAddressesForMember(Member member) {
    Assert.notNull(member, "Argument [member] cannot be null");

    LOGGER.info("Calling getEmailAddressesForMember for [{}].", member.getMemberUID());

    return getEmailAddressesForEntity(member.getMemberUID(), UserType.MEMBER.name());
  }

  @Override
  public List<EmailAddress> getEmailAddressesForPerson(Person person) {
    Assert.notNull(person, "Argument [person] cannot be null");

    LOGGER.info("Calling getEmailAddressesForPerson for [{}].", person.getPersonUID());

    return getEmailAddressesForEntity(person.getPersonUID(), UserType.STAFF.name());
  }

  private List<EmailAddress> getEmailAddressesForEntity(long id, String name) {
    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("parent_entity_id", id);
    params.addValue("parent_entity_name", name);

    return getJdbcTemplate().query(stmtLoader.load(GET_EMAILADDRESSES_FOR_ENTITY), params, new EmailAddressMapper());
  }

  @Override
  public Address saveAddress(Address address, User user) {
    Assert.notNull(address, "Argument [address] cannot be null");
    Assert.notNull(user, "Argument [user] cannot be null");

    LOGGER.info("Saving address [{}].", address.toString());

    if (address.getAddressUID() == 0L) {
      return insert(address, user);
    } else {
      return update(address, user);
    }
  }

  private Address insert(Address address, User user) {
    LOGGER.info("Inserting address [{}].", address.toString());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_ADDRESS),
        AddressMapper.mapInsertStatement(address, user), keyHolder);

    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return Address.builder(address)
        .setAddressUID(keyHolder.getKey().longValue())
        .build();
  }

  private Address update(Address address, User user) {
    LOGGER.info("Updating address [{}].", address.toString());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate()
        .update(stmtLoader.load(UPDATE_ADDRESS), AddressMapper.mapUpdateStatement(address, user));
    if (cnt == 1) {
      return Address.builder(address)
          .setAddressUpdateCount(address.getAddressUpdateCount() + 1)
          .build();
    } else if (cnt == 0) {
      throw new OptimisticLockingFailureException("invalid update count of [" + cnt
          + "] possible update count mismatch");
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }
  }

  @Override
  public PhoneNumber savePhoneNumber(PhoneNumber phoneNumber, User user) {
    Assert.notNull(phoneNumber, "Argument [phoneNumber] cannot be null");
    Assert.notNull(user, "Argument [user] cannot be null");

    LOGGER.info("Saving phone number [{}].", phoneNumber.getPhoneNumber());

    if (phoneNumber.getPhoneUID() == 0L) {
      return insert(phoneNumber, user);
    } else {
      return update(phoneNumber, user);
    }
  }

  private PhoneNumber insert(PhoneNumber phoneNumber, User user) {
    LOGGER.info("Inserting phone number [{}].", phoneNumber.getPhoneNumber());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_PHONE),
        PhoneNumberMapper.mapInsertStatement(phoneNumber, user), keyHolder);

    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return PhoneNumber.builder(phoneNumber)
        .setPhoneUID(keyHolder.getKey().longValue())
        .build();
  }

  private PhoneNumber update(PhoneNumber phoneNumber, User user) {
    LOGGER.info("Updating phone number [{}].", phoneNumber.getPhoneNumber());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_PHONE),
        PhoneNumberMapper.mapUpdateStatement(phoneNumber, user));

    if (cnt == 1) {
      return PhoneNumber.builder(phoneNumber)
          .setPhoneUpdateCount(phoneNumber.getPhoneUpdateCount() + 1)
          .build();
    } else if (cnt == 0) {
      throw new OptimisticLockingFailureException("invalid update count of [" + cnt
          + "] possible update count mismatch");
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }
  }

  @Override
  public EmailAddress saveEmailAddress(EmailAddress emailAddress, User user) {
    Assert.notNull(emailAddress, "Argument [emailAddress] cannot be null");
    Assert.notNull(user, "Argument [user] cannot be null");

    LOGGER.info("Saving email address [{}].", emailAddress.getEmailAddress());

    if (emailAddress.getEmailAddressUID() == 0L) {
      return insert(emailAddress, user);
    } else {
      return update(emailAddress, user);
    }
  }

  public EmailAddress insert(EmailAddress emailAddress, User user) {
    LOGGER.info("Inserting email address [{}].", emailAddress.getEmailAddress());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_EMAIL),
        EmailAddressMapper.mapInsertStatement(emailAddress, user), keyHolder);

    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return EmailAddress.builder(emailAddress)
        .setEmailAddressUID(keyHolder.getKey().longValue())
        .build();
  }

  public EmailAddress update(EmailAddress emailAddress, User user) {
    LOGGER.info("Updating email address [{}].", emailAddress.getEmailAddress());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_EMAIL),
        EmailAddressMapper.mapUpdateStatement(emailAddress, user));

    if (cnt == 1) {
      return EmailAddress.builder(emailAddress)
          .setEmailAddressUpdateCount(emailAddress.getEmailAddressUpdateCount() + 1)
          .build();
    } else if (cnt == 0) {
      throw new OptimisticLockingFailureException("invalid update count of [" + cnt
          + "] possible update count mismatch");
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }
  }
}
