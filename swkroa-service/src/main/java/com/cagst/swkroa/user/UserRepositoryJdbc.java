package com.cagst.swkroa.user;

import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.audit.AuditEventType;
import com.cagst.swkroa.audit.annotation.AuditInstigator;
import com.cagst.swkroa.audit.annotation.AuditMessage;
import com.cagst.swkroa.audit.annotation.Auditable;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.contact.ContactRepository;
import com.cagst.swkroa.person.PersonRepositoryJdbc;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC Template implementation of the {@link UserRepository} interface.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@Repository("userRepo")
/* package */ final class UserRepositoryJdbc extends PersonRepositoryJdbc implements UserRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryJdbc.class);

  private static final String GET_USER_BY_USERNAME    = "GET_USER_BY_USERNAME";
  private static final String GET_USER_BY_UID         = "GET_USER_BY_UID";
  private static final String SIGNIN_ATTEMPT          = "SIGNIN_ATTEMPT";
  private static final String SIGNIN_SUCCESSFUL       = "SIGNIN_SUCCESSFUL";
  private static final String CHANGE_USER_PASSWORD    = "CHANGE_USER_PASSWORD";
  private static final String CHECK_USERNAME_NEW      = "CHECK_USERNAME_NEW";
  private static final String CHECK_USERNAME_EXISTING = "CHECK_USERNAME_EXISTING";
  private static final String USER_LOCK               = "USER_LOCK";
  private static final String USER_UNLOCK             = "USER_UNLOCK";
  private static final String USER_ENABLE             = "USER_ENABLE";
  private static final String USER_DISABLE            = "USER_DISABLE";

  private static final String GET_ALL_USERS = "GET_ALL_USERS";

  private static final String INSERT_USER = "INSERT_USER";
  private static final String UPDATE_USER = "UPDATE_USER";

  /**
   * Primary constructor used to create an instance of <i>UserRepositoryJdbc</i>.
   *
   * @param dataSource
   *      The {@link DataSource} used to retrieve / persist data objects.
   * @param contactRepo
   *      The {@link ContactRepository} to use to populate contact objects.
   */
  public UserRepositoryJdbc(final DataSource dataSource, final ContactRepository contactRepo) {
    super(dataSource, contactRepo);
  }

  @Override
  public User getUserByUsername(final String username) throws IllegalArgumentException {
    Assert.hasLength(username, "Assertion Failed - argument [username] cannot be null or empty");

    LOGGER.info("Calling getUserByUsername [{}].", username);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, String> params = new HashMap<String, String>(1);
    params.put("username", username);

    List<User> users = getJdbcTemplate().query(stmtLoader.load(GET_USER_BY_USERNAME), params, new UserMapper());
    if (users.size() == 1) {
      return users.get(0);
    } else if (users.size() == 0) {
      LOGGER.warn("User [{}] was not found!", username);
      return null;
    } else {
      LOGGER.error("More than 1 user with username of [{}] was found!", username);
      return null;
    }
  }

  @Override
  @Cacheable(value = "users")
  public User getUserByUID(final long uid) {
    LOGGER.info("Calling getUserByUID [{}].", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<String, Long>(1);
    params.put("user_id", uid);

    List<User> users = getJdbcTemplate().query(stmtLoader.load(GET_USER_BY_UID), params, new UserMapper());
    if (users.size() == 1) {
      return users.get(0);
    } else if (users.size() == 0) {
      LOGGER.warn("User [{}] was not found!", uid);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.error("More than 1 user with username of [{}] was found!", uid);
      throw new IncorrectResultSizeDataAccessException(1, users.size());
    }
  }

  @Override
  public User signinAttempt(final User user) throws IllegalArgumentException, IncorrectResultSizeDataAccessException {
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Calling incrementSigninAttempts for User [{}].", user.getUsername());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<String, Long>(1);
    params.put("user_id", user.getUserUID());

    int cnt = getJdbcTemplate().update(stmtLoader.load(SIGNIN_ATTEMPT), params);
    if (cnt != 1L) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    user.setSigninAttempts(user.getSigninAttempts() + 1);

    return user;
  }

  @Override
  public User signinSuccessful(final User user, final String ipAddress) throws IllegalArgumentException,
      IncorrectResultSizeDataAccessException {

    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Calling updateLastSigninInfo for User [{}].", user.getUsername());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Object> params = new HashMap<String, Object>(2);
    params.put("user_id", user.getUserUID());
    params.put("last_signin_ip", ipAddress);

    int cnt = getJdbcTemplate().update(stmtLoader.load(SIGNIN_SUCCESSFUL), params);
    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    user.setSigninAttempts(0);
    user.setUserUpdateCount(user.getUserUpdateCount() + 1);

    return user;
  }

  @Override
  @CacheEvict(value = "users", key = "#lockUserUID")
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_ACCOUNT_LOCKED)
  public User lockUserAccount(final long lockUserUID, final @AuditMessage String message, final @AuditInstigator User user)
      throws IllegalArgumentException, IncorrectResultSizeDataAccessException {

    Assert.isTrue(lockUserUID > 0, "Assertion Failed - argument[lockUserUID] must be greater than zero");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Calling lockUserAccount for User [{}].", lockUserUID);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<String, Long>(2);
    params.put("user_id", lockUserUID);
    params.put("updt_id", user.getUserUID());

    int cnt = getJdbcTemplate().update(stmtLoader.load(USER_LOCK), params);
    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return getUserByUID(lockUserUID);
  }

  @Override
  @CacheEvict(value = "users", key = "#unlockUserUID")
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_ACCOUNT_UNLOCKED)
  public User unlockUserAccount(final long unlockUserUID, final @AuditMessage String message, final @AuditInstigator User user)
      throws IllegalArgumentException, IncorrectResultSizeDataAccessException {

    Assert.isTrue(unlockUserUID > 0, "Assertion Failed - argument [unlockUserUID] must be greater than zero");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Calling unlockUserAccount for User [{}] by User [{}].", unlockUserUID, user.getUsername());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<String, Long>(2);
    params.put("user_id", unlockUserUID);
    params.put("updt_id", user.getUserUID());

    int cnt = getJdbcTemplate().update(stmtLoader.load(USER_UNLOCK), params);
    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return getUserByUID(unlockUserUID);
  }

  @Override
  @CacheEvict(value = "users", key = "#enableUserUID")
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_ACCOUNT_ENABLED)
  public User enableUserAccount(final long enableUserUID, final @AuditMessage String message, final @AuditInstigator User user)
      throws IllegalArgumentException, IncorrectResultSizeDataAccessException {

    Assert.isTrue(enableUserUID > 0, "Assertion Failed - argument [enableUserUID] must be greater than zero");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Calling enableUserAccount for User [{}].", user.getUsername());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<String, Long>(2);
    params.put("user_id", enableUserUID);
    params.put("updt_id", user.getUserUID());

    int cnt = getJdbcTemplate().update(stmtLoader.load(USER_ENABLE), params);
    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return getUserByUID(enableUserUID);
  }

  @Override
  @CacheEvict(value = "users", key = "#disableUserUID")
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_ACCOUNT_DISABLED)
  public User disableUserAccount(final long disableUserUID, final @AuditMessage String message, final @AuditInstigator User user)
      throws IllegalArgumentException, IncorrectResultSizeDataAccessException {

    Assert.isTrue(disableUserUID > 0, "Assertion Failed - argument [disableUserUID] must be greater than zero");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Calling disableUserAccount for User [{}]", disableUserUID);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<String, Long>(2);
    params.put("user_id", disableUserUID);
    params.put("updt_id", user.getUserUID());

    int cnt = getJdbcTemplate().update(stmtLoader.load(USER_DISABLE), params);
    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return getUserByUID(disableUserUID);
  }

  @Override
  @CacheEvict(value = "users", key = "#user.getUserUID()")
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_PASSWORD_CHANGED)
  public User changeUserPassword(final @AuditInstigator User user, final String newPassword,
                                 final @AuditMessage String message) throws IllegalArgumentException {

    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");
    Assert.hasLength(newPassword, "Assertion Failed - argument [password] cannot be null or empty");

    LOGGER.info("Calling changeUserPassword for User [{}].", user.getUsername());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Object> params = new HashMap<String, Object>(2);
    params.put("user_id", user.getUserUID());
    params.put("password", newPassword);

    int cnt = getJdbcTemplate().update(stmtLoader.load(CHANGE_USER_PASSWORD), params);
    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    user.setPassword(newPassword);
    user.setPasswordChangedDate(new DateTime(DateTimeZone.UTC));
    user.setPasswordTemporary(false);

    return user;
  }

  @Override
  public boolean doesUsernameExist(final String username) {
    Assert.hasLength(username, "Assertion Failed - argument [username] cannot be null or empty");

    LOGGER.info("Calling doesUsernameExist for username [{}].", username);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, String> params = new HashMap<String, String>(1);
    params.put("username", username);

    int cnt = getJdbcTemplate().queryForInt(stmtLoader.load(CHECK_USERNAME_NEW), params);
    return (cnt > 0);
  }

  @Override
  public boolean doesUsernameExist(final String username, final User user) {
    Assert.hasLength(username, "Assertion Failed - argument [username] cannot be null or empty");
    Assert.notNull(user, "Assertion failed - argument [user] cannot be null");

    LOGGER.info("Calling doesUsernameExist for username [{}].", username);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Object> params = new HashMap<String, Object>(2);
    params.put("username", username);
    params.put("user_id", user.getUserUID());

    int cnt = getJdbcTemplate().queryForInt(stmtLoader.load(CHECK_USERNAME_EXISTING), params);
    return (cnt > 0);
  }

  @Override
  @CacheEvict(value = "users", key = "#builder.getUserUID()")
  public User saveUser(final User builder, final User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, DataAccessException {

    Assert.notNull(builder, "Assertion Failed - argument [builder] cannot be null");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Calling saveUser for [{}]", builder.getUsername());

    // save the Person portion of the User
    savePerson(builder, user);

    if (builder.getUserUID() == 0L) {
      return insertUser(builder, user);
    } else {
      return updateUser(builder, user);
    }
  }

  @Override
  public List<User> getAllUsers() {
    LOGGER.info("Retrieving all users defined in the system.");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().getJdbcOperations().query(stmtLoader.load(GET_ALL_USERS), new UserMapper());
  }

  /** Place helper methods below this line. **/

  private User insertUser(final User newUser, final User user)
      throws IncorrectResultSizeDataAccessException, DataAccessException, UsernameTakenException {

    LOGGER.info("Calling insertUser for [{}]", newUser.getUsername());

    if (doesUsernameExist(newUser.getUsername())) {
      throw new UsernameTakenException(newUser.getUsername());
    }

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_USER), UserMapper.mapInsertStatement(newUser, user),
        keyHolder);
    if (cnt == 1) {
      newUser.setUserUID(keyHolder.getKey().longValue());
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return newUser;
  }

  private User updateUser(final User updateUser, final User user) throws OptimisticLockingFailureException,
      IncorrectResultSizeDataAccessException, DataAccessException {

    LOGGER.info("Calling updateUser for [{}]", updateUser.getUsername());

    if (doesUsernameExist(updateUser.getUsername(), updateUser)) {
      throw new UsernameTakenException(updateUser.getUsername());
    }

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_USER), UserMapper.mapUpdateStatement(updateUser, user));
    if (cnt == 1) {
      updateUser.setUserUpdateCount(updateUser.getUserUpdateCount() + 1);
    } else if (cnt == 0) {
      throw new OptimisticLockingFailureException("invalid update count of [" + cnt
          + "] possible update count mismatch");
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return updateUser;
  }
}
