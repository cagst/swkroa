package com.cagst.swkroa.security;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Representation of a Security Policy within the system.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public final class SecurityPolicy implements Serializable {
  private static final long serialVersionUID = -5761738041576289806L;

  private long security_policy_id;
  private String security_policy_name;
  private int max_signin_attempts;
  private int timeout_period_mins;
  private int password_expiry_days;
  private int account_locked_days;

  public long getSecurityPolicyUID() {
    return security_policy_id;
  }

  /* package */void setSecurityPolicyUID(final long securityPolicyUID) {
    this.security_policy_id = securityPolicyUID;
  }

  public String getSecurityPolicyName() {
    return security_policy_name;
  }

  public void setSecurityPolicyName(final String securityPolicyName) {
    this.security_policy_name = securityPolicyName;
  }

  public int getMaxSigninAttempts() {
    return max_signin_attempts;
  }

  public void setMaxSigninAttempts(final int maxSigninAttempts) {
    this.max_signin_attempts = maxSigninAttempts;
  }

  public int getTimeoutPeriodInMinutes() {
    return timeout_period_mins;
  }

  public void setTimeoutPeriodInMinutes(final int timeoutPeriod) {
    this.timeout_period_mins = timeoutPeriod;
  }

  public int getPasswordExpiryInDays() {
    return password_expiry_days;
  }

  public void setPasswordExpiryInDays(final int passwordExpiryDays) {
    this.password_expiry_days = passwordExpiryDays;
  }

  public int getAccountLockedDays() {
    return account_locked_days;
  }

  public void setAccountLockedDays(final int accountLockedDays) {
    this.account_locked_days = accountLockedDays;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append(security_policy_id);
    builder.append(security_policy_name);
    builder.append(max_signin_attempts);
    builder.append(timeout_period_mins);
    builder.append(password_expiry_days);
    builder.append(account_locked_days);

    return builder.build();

  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(security_policy_name);

    return builder.build();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof SecurityPolicy)) {
      return false;
    }

    SecurityPolicy rhs = (SecurityPolicy) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.append(security_policy_name, rhs.getSecurityPolicyName());

    return builder.build();
  }
}
