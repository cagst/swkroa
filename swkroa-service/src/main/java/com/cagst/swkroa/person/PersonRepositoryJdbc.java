package com.cagst.swkroa.person;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.user.User;

/**
 * A JDBC Template implementation of the {@link PersonRepository} interface.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public class PersonRepositoryJdbc extends BaseRepositoryJdbc implements PersonRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepositoryJdbc.class);

	private static final String GET_PERSON_BY_UID = "GET_PERSON_BY_UID";
	private static final String INSERT_PERSON = "INSERT_PERSON";
	private static final String UPDATE_PERSON = "UPDATE_PERSON";

	private final CodeValueRepository codeValueRepo;

	/**
	 * Primary Constructor used to create an instance of <i>PersonRepositoryJdbc</i>.
	 * 
	 * @param dataSource
	 *          The {@link DataSource} used to retrieve / persist data objects.
	 * @param codeValueRepo
	 *          The {@link CodeValueRepository} to use to retrieve coded values.
	 */
	public PersonRepositoryJdbc(final DataSource dataSource, final CodeValueRepository codeValueRepo) {
		super(dataSource);

		this.codeValueRepo = codeValueRepo;
	}

	protected CodeValueRepository getCodeValueRepository() {
		return codeValueRepo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.person.PersonRepository#getPersonByUID(long)
	 */
	@Override
	public Person getPersonByUID(final long uid) {
		LOGGER.info("Calling getPersonByUID for [{}].", uid);

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		Map<String, Long> params = new HashMap<String, Long>();
		params.put("person_id", uid);

		List<Person> persons = getJdbcTemplate().query(stmtLoader.load(GET_PERSON_BY_UID), params,
				new PersonMapper(codeValueRepo));

		if (persons.size() == 1) {
			return persons.get(0);
		} else if (persons.size() == 0) {
			LOGGER.warn("Person with UID of [{}] was not found.", uid);
			throw new EmptyResultDataAccessException(1);
		} else {
			LOGGER.error("More then one Person with UID of [{}] was found.", uid);
			throw new IncorrectResultSizeDataAccessException(1, persons.size());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.person.PersonRepository#savePerson(com.cagst.swkroa.person.PersonBuilder,
	 * com.cagst.swkroa.user.User)
	 */
	@Override
	public Person savePerson(final Person person, final User user) {
		Assert.notNull(person, "Assertion Failure - argument [person] cannot be null");
		Assert.notNull(user, "Assertion Failure - argument [user] cannot be null");

		LOGGER.info("Saving person [{}, {}].", person.getLastName(), person.getFirstName());

		if (person.getPersonUID() == 0L) {
			return insert(person, user);
		} else {
			return update(person, user);
		}
	}

	private Person insert(final Person person, final User user) {
		LOGGER.info("Inserting person [{}, {}].", person.getLastName(), person.getFirstName());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
		KeyHolder keyHolder = new GeneratedKeyHolder();

		int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_PERSON), PersonMapper.mapInsertStatement(person, user),
				keyHolder);

		if (cnt == 1) {
			person.setPersonUID(keyHolder.getKey().longValue());
		} else {
			throw new IncorrectResultSizeDataAccessException(1, cnt);
		}

		return person;
	}

	private Person update(final Person person, final User user) {
		LOGGER.info("Updating person [{}, {}].", person.getLastName(), person.getFirstName());

		StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

		int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_PERSON), PersonMapper.mapUpdateStatement(person, user));
		if (cnt == 1) {
			person.setPersonUpdateCount(person.getPersonUpdateCount() + 1);
		} else if (cnt == 0) {
			throw new OptimisticLockingFailureException("invalid update count of [" + cnt
					+ "] possible update count mismatch");
		} else {
			throw new IncorrectResultSizeDataAccessException(1, cnt);
		}

		return person;
	}
}
