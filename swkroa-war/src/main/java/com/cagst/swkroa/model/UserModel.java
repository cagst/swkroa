package com.cagst.swkroa.model;

import com.cagst.swkroa.security.SecurityPolicy;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Date;

/**
 * Representation of a User within the system.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public final class UserModel extends PersonModel {
  private static final long serialVersionUID = 459919042550669570L;

  private long user_id;
  private String username;
  private DateTime last_signin_dt_tm;
  private String last_signin_ip;
  private boolean temporary_pwd_ind;
  private DateTime account_locked_dt_tm;
  private DateTime account_expired_dt_tm;
  private DateTime password_changed_dt_tm;

  // meta-data
  private long updt_cnt;
  private DateTime create_dt_tm;

  private SecurityPolicy securityPolicy;

  /**
   * Gets the unique identifier for the User object.
   *
   * @return A {@link long} that uniquely identifier for the UserBuilder.
   */
  public long getUserUID() {
    return user_id;
  }

  /**
   * Sets the unique identifier for the User object.
   *
   * @param userUID {@link long} the unique identifier for the User.
   */
  public void setUserUID(final long userUID) {
    this.user_id = userUID;
  }

  /**
   * Gets the username associated to the User.
   *
   * @return The {@link String} username for the User.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username associated to the User.
   *
   * @param username The {@link String} username to associate to this User.
   */
  public void setUsername(final String username) {
    this.username = username;
  }

  /**
   * Gets the date this User last signed in.
   *
   * @return The {@link org.joda.time.DateTime} this User last signed in.
   */
  public DateTime getLastSigninDate() {
    return last_signin_dt_tm;
  }

  /**
   * Sets the date this User last signed in.
   *
   * @param lastSignin The {@link org.joda.time.DateTime} this User lasted signed in.
   */
  public void setLastSigninDate(final DateTime lastSignin) {
    this.last_signin_dt_tm = lastSignin;
  }

  /**
   * Gets the IP address of where they last signed in from.
   *
   * @return A {@link String} that represents the IP address of where the user last signed in from.
   */
  public String getLastSigninIp() {
    return last_signin_ip;
  }

  /**
   * Sets the IP address of where they last signed in from.
   *
   * @param lastSigninIp The IP address of where the user last signed in from.
   */
  public void setLastSigninIp(final String lastSigninIp) {
    this.last_signin_ip = lastSigninIp;
  }

  /**
   * @return <code>true</code> if the User's password is temporary.
   */
  public boolean isPasswordTemporary() {
    return temporary_pwd_ind;
  }

  /**
   * Sets whether the User's password is temporary and should be changed the next time they sign in.
   *
   * @param temporaryPwd <code>true</code> if the User's password is temporary and should changed the next time
   *                     they sign in.
   */
  public void setPasswordTemporary(final boolean temporaryPwd) {
    this.temporary_pwd_ind = temporaryPwd;
  }

  /**
   * @return The date this account was locked.
   */
  public DateTime getAccountLockedDate() {
    return account_locked_dt_tm;
  }

  /**
   * Sets the date this account was locked.
   *
   * @param accountLockedDate The {@link org.joda.time.DateTime} this account was locked.
   */
  public void setAccountedLockedDate(final DateTime accountLockedDate) {
    this.account_locked_dt_tm = accountLockedDate;
  }

  public DateTime getAccountExpiredDate() {
    return account_expired_dt_tm;
  }

  public void setAccountExpiredDate(final DateTime expiredDate) {
    this.account_expired_dt_tm = expiredDate;
  }

  public DateTime getPasswordChangedDate() {
    return password_changed_dt_tm;
  }

  public void setPasswordChangedDate(final DateTime changedDate) {
    this.password_changed_dt_tm = changedDate;
  }

  /**
   * @return {@code true} if the password can / will expire, {@code false} otherwise.
   */
  private boolean willPasswordExpire() {
    return (securityPolicy.getPasswordExpiryInDays() > 0);
  }

  /**
   * @return The number of days until the password will expire, a negative number will be returned
   * if the password will not expire.
   */
  public long getPasswordExpiryInDays() {
    if (!willPasswordExpire()) {
      return -1L;
    }

    DateTime lastChangedDtTm = (password_changed_dt_tm != null ? password_changed_dt_tm : create_dt_tm);
    Duration variance = new Duration(lastChangedDtTm, DateTime.now());

    return variance.getStandardDays();
  }

  /**
   * @return The {@link java.util.Date} the password will expire, {@code null} if the password will not
   * expire.
   */
  public Date getPasswordExpiryDate() {
    if (!willPasswordExpire()) {
      return null;
    }

    DateTime lastChangedDtTm = (password_changed_dt_tm != null ? password_changed_dt_tm : create_dt_tm);
    DateTime expiryDate = lastChangedDtTm.plusDays(securityPolicy.getPasswordExpiryInDays());

    return expiryDate.toDate();
  }

  /**
   * Gets the number of times this object has been updated.
   *
   * @return {@link long} number of times the object has been updated.
   */
  public long getUserUpdateCount() {
    return updt_cnt;
  }

  /**
   * Sets the number of times this object has been updated.
   *
   * @param updateCount {@link long} the number of times the object has been updated.
   */
  /* package */void setUserUpdateCount(final long updateCount) {
    this.updt_cnt = updateCount;
  }

  /**
   * Gets the {@link org.joda.time.DateTime} when this object was created.
   *
   * @return The {@link org.joda.time.DateTime} of when this object was created.
   */
  public DateTime getCreateDateTime() {
    return create_dt_tm;
  }

  /**
   * Sets the {@link org.joda.time.DateTime} of when this object was created.
   *
   * @param createDtTm The {@link org.joda.time.DateTime} of when this object was created.
   */
  public void setCreateDateTime(final DateTime createDtTm) {
    this.create_dt_tm = createDtTm;
  }

  public SecurityPolicy getSecurityPolicy() {
    return securityPolicy;
  }

  public void setSecurityPolicy(final SecurityPolicy securityPolicy) {
    this.securityPolicy = securityPolicy;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.cagst.swkroa.person.PersonBuilder#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("username", username);
    builder.append("last_signin_dt_tm", last_signin_dt_tm);
    builder.append("last_signin_ip", last_signin_ip);
    builder.append("temporary_pwd_ind", temporary_pwd_ind);
    builder.append("account_locked_dt_tm", account_locked_dt_tm);
    builder.append("account_expired_dt_tm", account_expired_dt_tm);
    builder.append("password_changed_dt_tm", password_changed_dt_tm);
    builder.append("updateCount", updt_cnt);
    builder.appendSuper(super.toString());

    return builder.build();
  }
}
