package com.cagst.swkroa.contact;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.user.User;

/**
 * A JDBC Template implementation of the {@link ContactRepository} interface.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
@Repository("contactRepo")
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

	private final CodeValueRepository codeValueRepo;

	/**
	 * Primary Constructor used to create an instance of <i>PersonRepositoryJdbc</i>.
	 * 
	 * @param dataSource
	 *          The {@link DataSource} used to retrieve / persist data objects.
	 * @param codeValueRepo
	 *          The {@link CodeValueRepository} to use to retrieve codified information.
	 */
	public ContactRepositoryJdbc(final DataSource dataSource, final CodeValueRepository codeValueRepo) {
		super(dataSource);

		this.codeValueRepo = codeValueRepo;
	}

	protected CodeValueRepository getCodeValueRepository() {
		return codeValueRepo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cagst.swkroa.contact.ContactRepository#getAddressesForMember(com.cagst.swkroa.member.Member
	 * )
	 */
	@Override
	public List<Address> getAddressesForMember(final Member member) {
		Assert.notNull(member, "Assertion Failed - argument [member] cannot be null");

		LOGGER.info("Calling getAddressesForMember for [{}].", member.getMemberUID());

		return getAddressesForEntity(member.getMemberUID(), ENTITY_MEMBER);
	}

	private List<Address> getAddressesForEntity(final long id, final String name) {
		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("parent_entity_id", id);
		params.put("parent_entity_name", name);

		return getJdbcTemplate().query(stmtLoader.load(GET_ADDRESSES_FOR_ENTITY), params, new AddressMapper(codeValueRepo));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cagst.swkroa.contact.ContactRepository#getPhoneNumbersForMember(com.cagst.swkroa.member
	 * .Member)
	 */
	@Override
	public List<PhoneNumber> getPhoneNumbersForMember(final Member member) {
		Assert.notNull(member, "Assertion Failed - argument [member] cannot be null");

		LOGGER.info("Calling getPhoneNumbersForMember for [{}].", member.getMemberUID());

		return getPhoneNumbersForEntity(member.getMemberUID(), ENTITY_MEMBER);
	}

	private List<PhoneNumber> getPhoneNumbersForEntity(final long id, final String name) {
		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("parent_entity_id", id);
		params.put("parent_entity_name", name);

		return getJdbcTemplate().query(stmtLoader.load(GET_PHONENUMBERS_FOR_ENTITY), params,
				new PhoneNumberMapper(codeValueRepo));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cagst.swkroa.contact.ContactRepository#getEmailAddressesForMember(com.cagst.swkroa.member
	 * .Member)
	 */
	@Override
	public List<EmailAddress> getEmailAddressesForMember(final Member member) {
		Assert.notNull(member, "Assertion Failed - argument [member] cannot be null");

		LOGGER.info("Calling getEmailAddressesForMember for [{}, {}].", member.getMemberUID());

		return getEmailAddressesForEntity(member.getMemberUID(), ENTITY_MEMBER);
	}

	private List<EmailAddress> getEmailAddressesForEntity(final long id, final String name) {
		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("parent_entity_id", id);
		params.put("parent_entity_name", name);

		return getJdbcTemplate().query(stmtLoader.load(GET_EMAILADDRESSES_FOR_ENTITY), params,
				new EmailAddressMapper(codeValueRepo));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.contact.ContactRepository#saveAddress(com.cagst.swkroa.contact.Address,
	 * com.cagst.swkroa.user.User)
	 */
	@Override
	public Address saveAddress(final Address address, final User user) {
		Assert.notNull(address, "Assertion Failure - argument [address] cannot be null");
		Assert.notNull(user, "Assertion Failure - argument [user] cannot be null");

		LOGGER.info("Saving address [{}].", address.toString());

		if (address.getAddressUID() == 0L) {
			return insert(address, user);
		} else {
			return update(address, user);
		}
	}

	private Address insert(final Address address, final User user) {
		LOGGER.info("Inserting address [{}].", address.toString());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		KeyHolder keyHolder = new GeneratedKeyHolder();

		int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_ADDRESS),
				AddressMapper.mapInsertStatement(address, user), keyHolder);

		if (cnt == 1) {
			address.setAddressUID(keyHolder.getKey().longValue());
		} else {
			throw new IncorrectResultSizeDataAccessException(1, cnt);
		}

		return address;
	}

	private Address update(final Address address, final User user) {
		LOGGER.info("Updating address [{}].", address.toString());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

		int cnt = getJdbcTemplate()
				.update(stmtLoader.load(UPDATE_ADDRESS), AddressMapper.mapUpdateStatement(address, user));
		if (cnt == 1) {
			address.setAddressUpdateCount(address.getAddressUpdateCount() + 1);
		} else if (cnt == 0) {
			throw new OptimisticLockingFailureException("invalid update count of [" + cnt
					+ "] possible update count mismatch");
		} else {
			throw new IncorrectResultSizeDataAccessException(1, cnt);
		}

		return address;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cagst.swkroa.contact.ContactRepository#savePhoneNumber(com.cagst.swkroa.contact.PhoneNumber
	 * , com.cagst.swkroa.user.User)
	 */
	@Override
	public PhoneNumber savePhoneNumber(final PhoneNumber phoneNumber, final User user) {
		Assert.notNull(phoneNumber, "Assertion Failure - argument [phoneNumber] cannot be null");
		Assert.notNull(user, "Assertion Failure - argument [user] cannot be null");

		LOGGER.info("Saving phone number [{}].", phoneNumber.getPhoneNumber());

		if (phoneNumber.getPhoneUID() == 0L) {
			return insert(phoneNumber, user);
		} else {
			return update(phoneNumber, user);
		}
	}

	private PhoneNumber insert(final PhoneNumber phoneNumber, final User user) {
		LOGGER.info("Inserting phone number [{}].", phoneNumber.getPhoneNumber());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		KeyHolder keyHolder = new GeneratedKeyHolder();

		int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_PHONE),
				PhoneNumberMapper.mapInsertStatement(phoneNumber, user), keyHolder);

		if (cnt == 1) {
			phoneNumber.setPhoneUID(keyHolder.getKey().longValue());
		} else {
			throw new IncorrectResultSizeDataAccessException(1, cnt);
		}

		return phoneNumber;
	}

	private PhoneNumber update(final PhoneNumber phoneNumber, final User user) {
		LOGGER.info("Updating phone number [{}].", phoneNumber.getPhoneNumber());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

		int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_PHONE),
				PhoneNumberMapper.mapUpdateStatement(phoneNumber, user));

		if (cnt == 1) {
			phoneNumber.setPhoneUpdateCount(phoneNumber.getPhoneUpdateCount() + 1);
		} else if (cnt == 0) {
			throw new OptimisticLockingFailureException("invalid update count of [" + cnt
					+ "] possible update count mismatch");
		} else {
			throw new IncorrectResultSizeDataAccessException(1, cnt);
		}

		return phoneNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cagst.swkroa.contact.ContactRepository#saveEmailAddress(com.cagst.swkroa.contact.EmailAddress
	 * , com.cagst.swkroa.user.User)
	 */
	@Override
	public EmailAddress saveEmailAddress(final EmailAddress emailAddress, final User user) {
		Assert.notNull(emailAddress, "Assertion Failure - argument [emailAddress] cannot be null");
		Assert.notNull(user, "Assertion Failure - argument [user] cannot be null");

		LOGGER.info("Saving email address [{}].", emailAddress.getEmailAddress());

		if (emailAddress.getEmailAddressUID() == 0L) {
			return insert(emailAddress, user);
		} else {
			return update(emailAddress, user);
		}
	}

	public EmailAddress insert(final EmailAddress emailAddress, final User user) {
		LOGGER.info("Inserting email address [{}].", emailAddress.getEmailAddress());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		KeyHolder keyHolder = new GeneratedKeyHolder();

		int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_EMAIL),
				EmailAddressMapper.mapInsertStatement(emailAddress, user), keyHolder);

		if (cnt == 1) {
			emailAddress.setEmailAddressUID(keyHolder.getKey().longValue());
		} else {
			throw new IncorrectResultSizeDataAccessException(1, cnt);
		}

		return emailAddress;
	}

	public EmailAddress update(final EmailAddress emailAddress, final User user) {
		LOGGER.info("Updating email address [{}].", emailAddress.getEmailAddress());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

		int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_EMAIL),
				EmailAddressMapper.mapUpdateStatement(emailAddress, user));

		if (cnt == 1) {
			emailAddress.setEmailAddressUpdateCount(emailAddress.getEmailAddressUpdateCount() + 1);
		} else if (cnt == 0) {
			throw new OptimisticLockingFailureException("invalid update count of [" + cnt
					+ "] possible update count mismatch");
		} else {
			throw new IncorrectResultSizeDataAccessException(1, cnt);
		}

		return emailAddress;
	}
}