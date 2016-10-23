package com.cagst.swkroa.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import com.cagst.common.util.CGTDateTimeUtils;
import com.cagst.swkroa.person.BasePersonMapper;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Maps a row in the resultset into a {@link User} object. Used to marshal / unmarshall a {@link User} object from the
 * database.
 *
 * @author Craig Gaskill
 */
/* package */final class UserMapper extends BasePersonMapper implements RowMapper<User> {
  private static final Logger logger = LoggerFactory.getLogger(UserMapper.class);

  // user fields
  private static final String USER_ID = "user_id";
  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private static final String TEMPORARY_PWD_IND = "temporary_pwd_ind";
  private static final String SIGNIN_ATTEMPTS = "signin_attempts";
  private static final String LAST_SIGNIN_DATE = "last_signin_dt_tm";
  private static final String LAST_SIGNIN_IP = "last_signin_ip";
  private static final String ACCOUNT_LOCKED_DATE = "account_locked_dt_tm";
  private static final String ACCOUNT_EXPIRED_DATE = "account_expired_dt_tm";
  private static final String PASSWORD_CHANGED_DATE = "password_changed_dt_tm";
  private static final String USER_TYPE = "user_type";

  // meta-data
  private static final String ACTIVE_IND = "active_ind";
  private static final String USER_UPDT_CNT = "user_updt_cnt";
  private static final String USER_CREATE_DT_TM = "user_create_dt_tm";

  @Override
  public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    User user = new User();

    user.setUserUID(rs.getLong(USER_ID));
    user.setPersonUID(rs.getLong(PERSON_ID));
    user.setTitleCD(rs.getLong(TITLE_CD));
    user.setLastName(rs.getString(NAME_LAST));
    user.setFirstName(rs.getString(NAME_FIRST));
    user.setMiddleName(rs.getString(NAME_MIDDLE));
    user.setUsername(rs.getString(USERNAME));
    user.setPassword(rs.getString(PASSWORD));
    user.setPasswordTemporary(rs.getBoolean(TEMPORARY_PWD_IND));
    user.setSigninAttempts(rs.getInt(SIGNIN_ATTEMPTS));

    String language = rs.getString(LOCALE_LANGUAGE);
    String country = rs.getString(LOCALE_COUNTRY);
    if (!StringUtils.isEmpty(language)) {
      user.setLocale(new Locale(language, country));
    }

    String time_zone = rs.getString(TIME_ZONE);
    if (!StringUtils.isEmpty(time_zone)) {
      try {
        DateTimeZone tz = DateTimeZone.forID(time_zone);
        user.setTimeZone(tz);
      } catch (IllegalArgumentException ex) {
        logger.warn(ex.getMessage());
      }
    }

    user.setLastSigninDate(CGTDateTimeUtils.getDateTime(rs, LAST_SIGNIN_DATE));
    user.setLastSigninIp(rs.getString(LAST_SIGNIN_IP));
    user.setAccountedLockedDate(CGTDateTimeUtils.getDateTime(rs, ACCOUNT_LOCKED_DATE));
    user.setAccountExpiredDate(CGTDateTimeUtils.getDateTime(rs, ACCOUNT_EXPIRED_DATE));
    user.setPasswordChangedDate(CGTDateTimeUtils.getDateTime(rs, PASSWORD_CHANGED_DATE));
    user.setUserType(UserType.valueOf(rs.getString(USER_TYPE)));

    // meta-data
    user.setActive(rs.getBoolean(ACTIVE_IND));
    user.setUserUpdateCount(rs.getLong(USER_UPDT_CNT));
    user.setPersonUpdateCount(rs.getLong(PERSON_UPDT_CNT));
    user.setCreateDateTime(CGTDateTimeUtils.getUTCDateTime(rs, USER_CREATE_DT_TM));

    return user;
  }

  /**
   * Will marshal a {@link User} into a {@link MapSqlParameterSource} for inserting into the database.
   *
   * @param newUser
   *     The {@link User} to map into an insert statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapInsertStatement(User newUser, User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(PERSON_ID, newUser.getPersonUID());
    params.addValue(USERNAME, newUser.getUsername());
    params.addValue(PASSWORD, newUser.getPassword());
    params.addValue(PASSWORD_CHANGED_DATE, CGTDateTimeUtils.convertDateTimeToTimestamp(newUser.getPasswordChangedDate()));
    params.addValue(TEMPORARY_PWD_IND, newUser.isPasswordTemporary());
    params.addValue(ACCOUNT_LOCKED_DATE, CGTDateTimeUtils.convertDateTimeToTimestamp(newUser.getAccountLockedDate()));
    params.addValue(ACCOUNT_EXPIRED_DATE, CGTDateTimeUtils.convertDateTimeToTimestamp(newUser.getAccountExpiredDate()));
    params.addValue(USER_TYPE, newUser.getUserType().name());
    params.addValue(ACTIVE_IND, newUser.isActive());
    params.addValue(CREATE_ID, user.getUserUID());
    params.addValue(UPDT_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link User} into a {@link MapSqlParameterSource} for updating into the database.
   *
   * @param saveUser
   *     The {@link User} to map into an update statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(User saveUser, User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(USERNAME, saveUser.getUsername());
    params.addValue(TEMPORARY_PWD_IND, saveUser.isPasswordTemporary());
    params.addValue(ACCOUNT_LOCKED_DATE, CGTDateTimeUtils.convertDateTimeToTimestamp(saveUser.getAccountLockedDate()));
    params.addValue(ACCOUNT_EXPIRED_DATE, CGTDateTimeUtils.convertDateTimeToTimestamp(saveUser.getAccountExpiredDate()));
    params.addValue(USER_TYPE, saveUser.getUserType().name());
    params.addValue(ACTIVE_IND, saveUser.isActive());
    params.addValue(UPDT_ID, user.getUserUID());

    params.addValue(USER_ID, saveUser.getUserUID());
    params.addValue(USER_UPDT_CNT, saveUser.getUserUpdateCount());

    return params;
  }
}
