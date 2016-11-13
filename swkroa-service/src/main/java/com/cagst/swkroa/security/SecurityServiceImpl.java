package com.cagst.swkroa.security;

import javax.inject.Named;

import com.cagst.swkroa.user.User;
import org.springframework.beans.factory.annotation.Value;

/**
 * An implementation of the {@link SecurityService} interface.
 *
 * @author Craig Gaskill
 */
@Named("securityService")
public final class SecurityServiceImpl implements SecurityService {
  private int max_signin_attempts = 5;
  private int timeout_period_mins = 15;
  private int password_expiry_days = 90;
  private int account_locked_days = 7;

  private SecurityPolicy defaultSecurityPolicy;

  public int getMaximumSigninAttempts() {
    return max_signin_attempts;
  }

  @Value("${security.max.signin.attempts}")
  public void setMaximumSigninAttempts(final int maxSigninAttempts) {
    this.max_signin_attempts = maxSigninAttempts;
  }

  public int getTimeoutPeriodInMinutes() {
    return timeout_period_mins;
  }

  @Value("${security.timeout.period.mins}")
  public void setTimeoutPeriodInMinutes(final int timeoutPeriod) {
    this.timeout_period_mins = timeoutPeriod;
  }

  public int getPasswordExpiryInDays() {
    return password_expiry_days;
  }

  @Value("${security.password.expiry.days}")
  public void setPasswordExpiryInDays(final int passwordExpiry) {
    this.password_expiry_days = passwordExpiry;
  }

  public int getAccountLockedDays() {
    return account_locked_days;
  }

  @Value("${security.account.locked.days}")
  public void setAccountLockedDays(final int accountLockedDays) {
    this.account_locked_days = accountLockedDays;
  }

  @Override
  public SecurityPolicy getSecurityPolicy(final User user) {
    return getDefaultSecurityPolicy();
  }

  private SecurityPolicy getDefaultSecurityPolicy() {
    if (null == defaultSecurityPolicy) {
      SecurityPolicy policy = new SecurityPolicy();

      policy.setSecurityPolicyName("Default");
      policy.setMaxSigninAttempts(getMaximumSigninAttempts());
      policy.setTimeoutPeriodInMinutes(getTimeoutPeriodInMinutes());
      policy.setPasswordExpiryInDays(getPasswordExpiryInDays());
      policy.setAccountLockedDays(getAccountLockedDays());

      defaultSecurityPolicy = policy;
    }

    return defaultSecurityPolicy;
  }
}
