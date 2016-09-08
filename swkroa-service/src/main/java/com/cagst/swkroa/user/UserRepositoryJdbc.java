package com.cagst.swkroa.user;

import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.audit.AuditEventType;
import com.cagst.swkroa.audit.annotation.AuditInstigator;
import com.cagst.swkroa.audit.annotation.AuditMessage;
import com.cagst.swkroa.audit.annotation.Auditable;
import com.cagst.swkroa.contact.ContactRepository;
import com.cagst.swkroa.person.PersonRepositoryJdbc;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JDBC Template implementation of the {@link UserRepository} interface.
 *
 * @author Craig Gaskill
 */
/* package */ final class UserRepositoryJdbc extends PersonRepositoryJdbc implements UserRepository, MessageSourceAware {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryJdbc.class);

  private static final String MSG_USERNAME_EXISTS = "com.cagst.swkroa.username.exists";

  private static final String GET_USER_BY_USERNAME    = "GET_USER_BY_USERNAME";
  private static final String GET_USER_BY_UID         = "GET_USER_BY_UID";
  private static final String GET_USER_BY_PERSON_ID   = "GET_USER_BY_PERSON_ID";
  private static final String SIGNIN_ATTEMPT          = "SIGNIN_ATTEMPT";
  private static final String SIGNIN_SUCCESSFUL       = "SIGNIN_SUCCESSFUL";
  private static final String CHANGE_USER_PASSWORD    = "CHANGE_USER_PASSWORD";
  private static final String RESET_USER_PASSWORD     = "RESET_USER_PASSWORD";
  private static final String CHECK_USERNAME_NEW      = "CHECK_USERNAME_NEW";
  private static final String CHECK_USERNAME_EXISTING = "CHECK_USERNAME_EXISTING";
  private static final String USER_LOCK               = "USER_LOCK";
  private static final String USER_UNLOCK             = "USER_UNLOCK";
  private static final String USER_ENABLE             = "USER_ENABLE";
  private static final String USER_DISABLE            = "USER_DISABLE";

  private static final String GET_ALL_USERS = "GET_ALL_USERS";

  private static final String INSERT_USER = "INSERT_USER";
  private static final String UPDATE_USER = "UPDATE_USER";

  private MessageSourceAccessor messages;

  /**
   * Primary constructor used to create an instance of <i>UserRepositoryJdbc</i>.
   *
   * @param dataSource
   *     The {@link DataSource} used to retrieve / persist data objects.
   * @param contactRepo
   *     The {@link ContactRepository} to use to populate contact objects.
   */
  public UserRepositoryJdbc(DataSource dataSource, ContactRepository contactRepo) {
    super(dataSource, contactRepo);
  }

  @Override
  public void setMessageSource(MessageSource messageSource) {
    messages = new MessageSourceAccessor(messageSource);
  }

  @Override
  public Optional<User> getUserByUsername(String username) throws IllegalArgumentException {
    Assert.hasLength(username, "Assertion Failed - argument [username] cannot be null or empty");

    LOGGER.info("Calling getUserByUsername [{}].", username);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, String> params = new HashMap<>(1);
    params.put("username", username);

    List<User> users = getJdbcTemplate().query(stmtLoader.load(GET_USER_BY_USERNAME), params, new UserMapper());
    if (users.size() == 1) {
      return Optional.of(users.get(0));
    } else if (users.size() == 0) {
      LOGGER.warn("User [{}] was not found!", username);
      return Optional.empty();
    } else {
      LOGGER.error("More than 1 user with username of [{}] was found!", username);
      return Optional.empty();
    }
  }

  @Override
  @Cacheable(value = "users")
  public User getUserByUID(long uid) throws IncorrectResultSizeDataAccessException {
    LOGGER.info("Calling getUserByUID [{}].", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<>(1);
    params.put("user_id", uid);

    List<User> users = getJdbcTemplate().query(stmtLoader.load(GET_USER_BY_UID), params, new UserMapper());
    if (users.size() == 1) {
      return users.get(0);
    } else if (users.size() == 0) {
      LOGGER.warn("User [{}] was not found!", uid);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.error("More than 1 user with ID of [{}] was found!", uid);
      throw new IncorrectResultSizeDataAccessException(1, users.size());
    }
  }

  @Override
  public Optional<User> getUserByPersonId(long personId) throws IncorrectResultSizeDataAccessException {
    LOGGER.info("Calling getUserByPersonId [{}].", personId);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<>(1);
    params.put("person_id", personId);

    List<User> users = getJdbcTemplate().query(stmtLoader.load(GET_USER_BY_PERSON_ID), params, new UserMapper());
    if (users.size() == 1) {
      return Optional.of(users.get(0));
    } else if (users.size() == 0) {
      return Optional.empty();
    } else {
      LOGGER.error("More than 1 user with person_id of [{}] was found!", personId);
      throw new IncorrectResultSizeDataAccessException(1, users.size());
    }
  }

  @Override
  public User signinAttempt(User user) throws IllegalArgumentException, IncorrectResultSizeDataAccessException {
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Calling incrementSigninAttempts for User [{}].", user.getUsername());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<>(1);
    params.put("user_id", user.getUserUID());

    int cnt = getJdbcTemplate().update(stmtLoader.load(SIGNIN_ATTEMPT), params);
    if (cnt != 1L) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    user.setSigninAttempts(user.getSigninAttempts() + 1);

    return user;
  }

  @Override
  public User signinSuccessful(User user, final String ipAddress) throws IllegalArgumentException,
      IncorrectResultSizeDataAccessException {

    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Calling updateLastSigninInfo for User [{}].", user.getUsername());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Object> params = new HashMap<>(2);
    params.put("user_id", user.getUserUID());
    params.put("last_signin_ip", ipAddress);

    int cnt = getJdbcTemplate().update(stmtLoader.load(SIGNIN_SUCCESSFUL), params);
    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    user.setSigninAttempts(0);

    return user;
  }

  @Override
  @CacheEvict(value = "users", key = "#user.getUserUID()")
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_ACCOUNT_LOCKED)
  public User lockUserAccount(User user, @AuditMessage String message, @AuditInstigator User instigator)
      throws IllegalArgumentException, IncorrectResultSizeDataAccessException {

    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");
    Assert.notNull(instigator, "Assertion Failed - argument [instigator] cannot be null");

    LOGGER.info("Calling lockUserAccount for User [{}].", user.getUsername());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<>(2);
    params.put("user_id", user.getUserUID());
    params.put("updt_id", instigator.getUserUID());

    int cnt = getJdbcTemplate().update(stmtLoader.load(USER_LOCK), params);
    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    user.setAccountedLockedDate(new DateTime());

    return user;
  }

  @Override
  @CacheEvict(value = "users", key = "#user.getUserUID()")
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_ACCOUNT_UNLOCKED)
  public User unlockUserAccount(User user, @AuditMessage String message, @AuditInstigator User instigator)
      throws IllegalArgumentException, IncorrectResultSizeDataAccessException {

    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");
    Assert.notNull(instigator, "Assertion Failed - argument [instigator] cannot be null");

    LOGGER.info("Calling unlockUserAccount for User [{}] by User [{}].", user.getUsername(), instigator.getUsername());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<>(2);
    params.put("user_id", user.getUserUID());
    params.put("updt_id", instigator.getUserUID());

    int cnt = getJdbcTemplate().update(stmtLoader.load(USER_UNLOCK), params);
    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    user.setAccountedLockedDate(null);
    user.setSigninAttempts(0);

    return user;
  }

  @Override
  @CacheEvict(value = "users", key = "#user.getUserUID()")
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_ACCOUNT_ENABLED)
  public User enableUserAccount(User user, @AuditMessage String message, @AuditInstigator User instigator)
      throws IllegalArgumentException, IncorrectResultSizeDataAccessException {

    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");
    Assert.notNull(instigator, "Assertion Failed - argument [instigator] cannot be null");

    LOGGER.info("Calling enableUserAccount for User [{}].", user.getUsername());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<>(2);
    params.put("user_id", user.getUserUID());
    params.put("updt_id", instigator.getUserUID());

    int cnt = getJdbcTemplate().update(stmtLoader.load(USER_ENABLE), params);
    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    user.setActive(true);

    return user;
  }

  @Override
  @CacheEvict(value = "users", key = "#user.getUserUID()")
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_ACCOUNT_DISABLED)
  public User disableUserAccount(User user, @AuditMessage String message, @AuditInstigator User instigator)
      throws IllegalArgumentException, IncorrectResultSizeDataAccessException {

    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");
    Assert.notNull(instigator, "Assertion Failed - argument [instigator] cannot be null");

    LOGGER.info("Calling disableUserAccount for User [{}]", user.getUsername());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<>(2);
    params.put("user_id", user.getUserUID());
    params.put("updt_id", instigator.getUserUID());

    int cnt = getJdbcTemplate().update(stmtLoader.load(USER_DISABLE), params);
    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    user.setActive(false);

    return user;
  }

  @Override
  @CacheEvict(value = "users", key = "#user.getUserUID()")
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_PASSWORD_CHANGED)
  public User changeUserPassword(@AuditInstigator User user, String newPassword,
                                 @AuditMessage String message) throws IllegalArgumentException {

    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");
    Assert.hasLength(newPassword, "Assertion Failed - argument [password] cannot be null or empty");

    LOGGER.info("Calling changeUserPassword for User [{}].", user.getUsername());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Object> params = new HashMap<>(2);
    params.put("user_id", user.getUserUID());
    params.put("password", newPassword);

    int cnt = getJdbcTemplate().update(stmtLoader.load(CHANGE_USER_PASSWORD), params);
    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    user.setPasswordChangedDate(new DateTime(DateTimeZone.UTC));
    user.setPasswordTemporary(false);

    return user;
  }

  @Override
  @CacheEvict(value = "users", key = "#user.getUserUID()")
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_PASSWORD_RESET)
  public User resetUserPassword(User user, String tempPassword, @AuditMessage String message,
                                @AuditInstigator User instigator) throws IllegalArgumentException {

    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");
    Assert.hasLength(tempPassword, "Assertion Failed - argument [tempPassword] cannot be null or empty");

    LOGGER.info("Calling resetUserPassword for User [{}].", user.getUsername());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Object> params = new HashMap<>(3);
    params.put("user_id", user.getUserUID());
    params.put("password", tempPassword);
    params.put("updt_id", instigator.getUserUID());

    int cnt = getJdbcTemplate().update(stmtLoader.load(RESET_USER_PASSWORD), params);
    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    user.setPasswordTemporary(true);

    return user;
  }

  @Override
  public boolean doesUsernameExist(String username) {
    Assert.hasLength(username, "Assertion Failed - argument [username] cannot be null or empty");

    LOGGER.info("Calling doesUsernameExist for username [{}].", username);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, String> params = new HashMap<>(1);
    params.put("username", username);

    int cnt = getJdbcTemplate().queryForObject(stmtLoader.load(CHECK_USERNAME_NEW), params, Integer.class);
    return (cnt > 0);
  }

  @Override
  public boolean doesUsernameExist(String username, User user) {
    Assert.hasLength(username, "Assertion Failed - argument [username] cannot be null or empty");
    Assert.notNull(user, "Assertion failed - argument [user] cannot be null");

    LOGGER.info("Calling doesUsernameExist for username [{}].", username);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Object> params = new HashMap<>(2);
    params.put("username", username);
    params.put("user_id", user.getUserUID());

    int cnt = getJdbcTemplate().queryForObject(stmtLoader.load(CHECK_USERNAME_EXISTING), params, Integer.class);
    return (cnt > 0);
  }

  @Override
  @CacheEvict(value = "users", key = "#builder.getUserUID()")
  public User saveUser(User newUser, User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, UsernameTakenException {

    Assert.notNull(newUser, "Assertion Failed - argument [newUser] cannot be null");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Calling saveUser for [{}]", newUser.getUsername());

    // save the Person portion of the User
    savePerson(newUser, user);

    if (newUser.getUserUID() == 0L) {
      return insertUser(newUser, user);
    } else {
      return updateUser(newUser, user);
    }
  }

  @Override
  public User registerUser(User newUser, User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, UsernameTakenException {

    Assert.notNull(newUser, "Assertion Failed - argument [newUser] cannot be null");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Calling saveUser for [{}]", newUser.getUsername());

    if (newUser.getUserUID() == 0L) {
      return insertUser(newUser, user);
    } else {
      return updateUser(newUser, user);
    }
  }

  @Override
  public List<User> getAllUsers() {
    LOGGER.info("Retrieving all users defined in the system.");

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    return getJdbcTemplate().getJdbcOperations().query(stmtLoader.load(GET_ALL_USERS), new UserMapper());
  }

  /**
   * Place helper methods below this line. *
   */

  private User insertUser(User newUser, User user)
      throws IncorrectResultSizeDataAccessException, UsernameTakenException {

    LOGGER.info("Calling insertUser for [{}]", newUser.getUsername());

    if (doesUsernameExist(newUser.getUsername())) {
      throw new UsernameTakenException(
          messages.getMessage(MSG_USERNAME_EXISTS, new Object[]{newUser.getUsername()}, "Username {0} is already in use!"));
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

  private User updateUser(User updateUser, User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, UsernameTakenException {

    LOGGER.info("Calling updateUser for [{}]", updateUser.getUsername());

    if (doesUsernameExist(updateUser.getUsername(), updateUser)) {
      throw new UsernameTakenException(
          messages.getMessage(MSG_USERNAME_EXISTS, new Object[]{updateUser.getUsername()}, "Username {0} is already in use!"));
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
