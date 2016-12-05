package com.cagst.swkroa.user;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.cagst.swkroa.person.Person;
import com.cagst.swkroa.role.Role;
import com.cagst.swkroa.security.SecurityPolicy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Representation of a User within the system.
 *
 * @author Craig Gaskill
 */
public final class User extends Person implements UserDetails {
  private static final long serialVersionUID = 459919042550669570L;

  private long user_id;
  private String username;
  private String password;
  private LocalDateTime last_signin_dt_tm;
  private String last_signin_ip;
  private int signin_attempts;
  private boolean temporary_pwd_ind;
  private LocalDateTime account_locked_dt_tm;
  private LocalDateTime account_expired_dt_tm;
  private LocalDateTime password_changed_dt_tm;
  private long updt_cnt;
  private LocalDateTime create_dt_tm;

  private SecurityPolicy securityPolicy;
  private UserType userType = UserType.STAFF;

  private final List<GrantedAuthority> authorities = new ArrayList<>();
  private final List<Role> roles = new ArrayList<>();
  private final List<UserQuestion> questions = new ArrayList<>();

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
   * @param userUID
   *     {@link long} the unique identifier for the User.
   */
  public void setUserUID(long userUID) {
    this.user_id = userUID;
  }

  /**
   * Gets the username associated to the User.
   *
   * @return The {@link String} username for the User.
   */
  @Override
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username associated to the User.
   *
   * @param username
   *     The {@link String} username to associate to this User.
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets the password associated to the User.
   *
   * @return The {@link String} password for the User.
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password associated to the User.
   *
   * @param password
   *     The {@link String} password to associate to this User.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Gets the date this User last signed in.
   *
   * @return The {@link LocalDateTime} this User last signed in.
   */
  public LocalDateTime getLastSigninDate() {
    return last_signin_dt_tm;
  }

  /**
   * Sets the date this User last signed in.
   *
   * @param lastSignin
   *     The {@link LocalDateTime} this User lasted signed in.
   */
  public void setLastSigninDate(LocalDateTime lastSignin) {
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
   * @param lastSigninIp
   *     The IP address of where the user last signed in from.
   */
  public void setLastSigninIp(String lastSigninIp) {
    this.last_signin_ip = lastSigninIp;
  }

  /**
   * Gets the number of times this User has attempted to sign in but failed to do so.
   *
   * @return The number of times this User has attempted to sign in.
   */
  public int getSigninAttempts() {
    return signin_attempts;
  }

  /**
   * Sets the number of times this User has attempted to sign in but failed to do so.
   *
   * @param signinAttempts
   *     The number of times this User has attempted to sign in.
   */
  public void setSigninAttempts(int signinAttempts) {
    this.signin_attempts = signinAttempts;
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
   * @param temporaryPwd
   *     <code>true</code> if the User's password is temporary and should changed the next time
   *     they sign in.
   */
  public void setPasswordTemporary(boolean temporaryPwd) {
    this.temporary_pwd_ind = temporaryPwd;
  }

  /**
   * @return The date this account was locked.
   */
  public LocalDateTime getAccountLockedDate() {
    return account_locked_dt_tm;
  }

  @Override
  public boolean isAccountNonLocked() {
    return (account_locked_dt_tm == null || account_locked_dt_tm.isAfter(LocalDateTime.now()));
  }

  /**
   * Sets the date this account was locked.
   *
   * @param accountLockedDate
   *     The {@link LocalDateTime} this account was locked.
   */
  public void setAccountedLockedDate(LocalDateTime accountLockedDate) {
    this.account_locked_dt_tm = accountLockedDate;
  }

  public LocalDateTime getAccountExpiredDate() {
    return account_expired_dt_tm;
  }

  @Override
  public boolean isAccountNonExpired() {
    return (account_expired_dt_tm == null || account_expired_dt_tm.isAfter(LocalDateTime.now()));
  }

  public void setAccountExpiredDate(LocalDateTime expiredDate) {
    this.account_expired_dt_tm = expiredDate;
  }

  public LocalDateTime getPasswordChangedDate() {
    return password_changed_dt_tm;
  }

  public void setPasswordChangedDate(LocalDateTime changedDate) {
    this.password_changed_dt_tm = changedDate;
  }

  /**
   * @return {@code true} if the password can / will expire, {@code false} otherwise.
   */
  private boolean willPasswordExpire() {
    return (securityPolicy.getPasswordExpiryInDays() > 0);
  }

  /**
   * @return {@code true} if the password has expired and needs to be changed, {@code false}
   * otherwise.
   */
  @JsonIgnore
  public boolean isPasswordExpired() {
    if (!willPasswordExpire()) {
      return false;
    }

    if (password_changed_dt_tm == null) {
      return true;
    }

    LocalDateTime expireDtTm = password_changed_dt_tm.plusDays(securityPolicy.getPasswordExpiryInDays());
    return expireDtTm.isBefore(LocalDateTime.now());
  }

  /**
   * @return The number of days until the password will expire, a negative number will be returned
   * if the password will not expire.
   */
  @JsonIgnore
  public long getPasswordExpiryInDays() {
    if (!willPasswordExpire()) {
      return -1L;
    }

    LocalDateTime lastChangedDtTm = (password_changed_dt_tm != null ? password_changed_dt_tm : create_dt_tm);
    Duration variance = Duration.between(lastChangedDtTm, LocalDateTime.now());

    return variance.toDays();
  }

  /**
   * @return The {@link Date} the password will expire, {@code null} if the password will not
   * expire.
   */
  @JsonIgnore
  public LocalDateTime getPasswordExpiryDate() {
    if (!willPasswordExpire()) {
      return null;
    }

    LocalDateTime lastChangedDtTm = (password_changed_dt_tm != null ? password_changed_dt_tm : create_dt_tm);
    return lastChangedDtTm.plusDays(securityPolicy.getPasswordExpiryInDays());
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return isActive();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public void addGrantedAuthority(GrantedAuthority authority) {
    authorities.add(authority);
  }

  public void addGrantedAuthority(String authority) {
    authorities.add(new SimpleGrantedAuthority(authority));
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
   * @param updateCount
   *     {@link long} the number of times the object has been updated.
   */
  /* package */void setUserUpdateCount(long updateCount) {
    this.updt_cnt = updateCount;
  }

  /**
   * Gets the {@link LocalDateTime} when this object was created.
   *
   * @return The {@link LocalDateTime} of when this object was created.
   */
  public LocalDateTime getCreateDateTime() {
    return create_dt_tm;
  }

  /**
   * Sets the {@link LocalDateTime} of when this object was created.
   *
   * @param createDtTm
   *     The {@link LocalDateTime} of when this object was created.
   */
  public void setCreateDateTime(LocalDateTime createDtTm) {
    this.create_dt_tm = createDtTm;
  }

  public SecurityPolicy getSecurityPolicy() {
    return securityPolicy;
  }

  public void setSecurityPolicy(SecurityPolicy securityPolicy) {
    this.securityPolicy = securityPolicy;
  }

  public UserType getUserType() {
    return userType;
  }

  public void setUserType(UserType userType) {
    this.userType = userType;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void clearRoles() {
    roles.clear();
  }

  public void addRole(Role role) {
    this.roles.add(role);
  }

  public void deleteRole(Role role) {
    this.roles.remove(role);
  }

  public List<UserQuestion> getUserQuestions() {
    return questions;
  }

  public void clearQuestions() {
    this.questions.clear();
  }

  public void addQuestion(UserQuestion question) {
    this.questions.add(question);
  }

  public void deleteQuestion(UserQuestion question) {
    this.questions.remove(question);
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("username", username);
    builder.append("last_signin_dt_tm", last_signin_dt_tm);
    builder.append("last_signin_ip", last_signin_ip);
    builder.append("signin_attempts", signin_attempts);
    builder.append("temporary_pwd_ind", temporary_pwd_ind);
    builder.append("account_locked_dt_tm", account_locked_dt_tm);
    builder.append("account_expired_dt_tm", account_expired_dt_tm);
    builder.append("password_changed_dt_tm", password_changed_dt_tm);
    builder.append("updateCount", updt_cnt);
    builder.appendSuper(super.toString());

    return builder.build();
  }
}
