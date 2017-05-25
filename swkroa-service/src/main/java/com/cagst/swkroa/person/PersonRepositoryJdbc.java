package com.cagst.swkroa.person;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;

import com.cagst.swkroa.internal.BaseRepositoryJdbc;
import com.cagst.swkroa.internal.StatementLoader;
import com.cagst.swkroa.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

/**
 * A JDBC Template implementation of the {@link PersonRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("personRepo")
public class PersonRepositoryJdbc extends BaseRepositoryJdbc implements PersonRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepositoryJdbc.class);

  private static final String GET_PERSON_BY_UID = "GET_PERSON_BY_UID";
  private static final String INSERT_PERSON = "INSERT_PERSON";
  private static final String UPDATE_PERSON = "UPDATE_PERSON";

  /**
   * Primary Constructor used to create an instance of <i>PersonRepositoryJdbc</i>.
   *
   * @param dataSource
   *     The {@link DataSource} used to retrieve / persist data objects.
   */
  @Inject
  public PersonRepositoryJdbc(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public Person getPersonByUID(long uid) {
    LOGGER.info("Calling getPersonByUID for [{}].", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("person_id", uid);

    List<Person> persons = getJdbcTemplate().query(
        stmtLoader.load(GET_PERSON_BY_UID),
        new MapSqlParameterSource("person_id", uid),
        new PersonMapper());

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

  @Override
  public Person savePerson(Person person, User user) {
    Assert.notNull(person, "Argument [person] cannot be null");
    Assert.notNull(user, "Argument [user] cannot be null");

    LOGGER.info("Saving person [{}, {}].", person.getLastName(), person.getFirstName());

    Person savedPerson;
    if (person.getPersonUID() == 0L) {
      savedPerson = insert(person, user);
    } else {
      savedPerson = update(person, user);
    }

    return savedPerson;
  }

  private Person insert(Person person, User user) {
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

  private Person update(Person person, User user) {
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
