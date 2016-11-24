package com.cagst.swkroa.security;

import java.io.Serializable;

import com.cagst.swkroa.utils.SwkroaToStringStyle;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Representation of a Security Policy within the system.
 *
 * @author Craig Gaskill
 */
@AutoValue
@JsonPropertyOrder({
    "securityPolicyUID",
    "name",
    "signinAttempts",
    "timeoutPeriodInMinutes",
    "passwordExpiryInDays",
    "accountLockedDays"
})
@JsonDeserialize(builder = AutoValue_SecurityPolicy.Builder.class)
public abstract class SecurityPolicy implements Serializable {
  private static final long serialVersionUID = -5761738041576289806L;

  @JsonProperty(value = "securityPolicyUID", required = true)
  public abstract long getSecurityPolicyUID();

  @JsonProperty(value = "name", required = true)
  public abstract String getSecurityPolicyName();

  @JsonProperty(value = "signinAttempts", required = true)
  public abstract int getMaxSigninAttempts();

  @JsonProperty(value = "timeoutPeriodInMinutes", required = true)
  public abstract int getTimeoutPeriodInMinutes();

  @JsonProperty(value = "passwordExpiryInDays", required = true)
  public abstract int getPasswordExpiryInDays();

  @JsonProperty(value = "accountLockedDays", required = true)
  public abstract int getAccountLockedDays();

  @Override
  public int hashCode() {
    return getSecurityPolicyName().hashCode();
  }

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

    return getSecurityPolicyName().equals(rhs.getSecurityPolicyName());
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, SwkroaToStringStyle.SWKROA_PREFIX_STYLE);
    builder.append("name", getSecurityPolicyName());
    builder.append("attempts", getMaxSigninAttempts());
    builder.append("timeout", getTimeoutPeriodInMinutes());
    builder.append("expiry", getPasswordExpiryInDays());
    builder.append("locked", getAccountLockedDays());

    return builder.build();
  }

  public static Builder builder() {
    return new AutoValue_SecurityPolicy.Builder()
        .setSecurityPolicyUID(0L)
        .setSecurityPolicyName("Default")
        .setMaxSigninAttempts(3)
        .setTimeoutPeriodInMinutes(15)
        .setPasswordExpiryInDays(90)
        .setAccountLockedDays(7);
  }

  @AutoValue.Builder
  public interface Builder {
    @JsonProperty(value = "securityPolicyUID", required = true)
    Builder setSecurityPolicyUID(long uid);

    @JsonProperty(value = "name", required = true)
    Builder setSecurityPolicyName(String name);

    @JsonProperty(value = "signinAttempts", required = true)
    Builder setMaxSigninAttempts(int attempts);

    @JsonProperty(value = "timeoutPeriodInMinutes", required = true)
    Builder setTimeoutPeriodInMinutes(int timeout);

    @JsonProperty(value = "passwordExpiryInDays", required = true)
    Builder setPasswordExpiryInDays(int expiry);

    @JsonProperty(value = "accountLockedDays", required = true)
    Builder setAccountLockedDays(int lockedDays);

    SecurityPolicy build();
  }
}
