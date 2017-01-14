package com.cagst.swkroa.country;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;

import com.cagst.swkroa.utils.SwkroaToStringStyle;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a State within the system.
 *
 * @author Craig Gaskill
 */
@AutoValue
@JsonPropertyOrder({
    "countryCode",
    "stateCode",
    "fipsCode",
    "stateName",
    "active",
    "stateUpdateCount"
})
@JsonDeserialize(builder = AutoValue_State.Builder.class)
public abstract class State implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty(value = "countryCode", required = true)
  public abstract String getCountryCode();

  @JsonProperty(value = "stateCode", required = true)
  public abstract String getStateCode();

  @Nullable
  @JsonProperty(value = "fipsCode")
  public abstract String getFipsCode();

  @JsonProperty(value = "stateName", required = true)
  public abstract String getStateName();

  @JsonProperty(value = "active")
  public abstract boolean isActive();

  @JsonProperty(value = "stateUpdateCount")
  public abstract long getStateUpdateCount();

  @Override
  public int hashCode() {
    return Objects.hash(getCountryCode(), getStateCode());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof State)) {
      return false;
    }

    State rhs = (State) obj;

    return Objects.equals(getCountryCode(), rhs.getCountryCode()) &&
        Objects.equals(getStateCode(), rhs.getStateCode());
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, SwkroaToStringStyle.SWKROA_PREFIX_STYLE);
    builder.append("country_code", getCountryCode());
    builder.append("state_code", getStateCode());
    builder.append("fips_code", getFipsCode());
    builder.append("state_name", getStateName());

    return builder.build();
  }

  public static Builder builder() {
    return new AutoValue_State.Builder()
        .setActive(true);
  }

  @AutoValue.Builder
  public interface Builder {
    @JsonProperty(value = "countryCode", required = true)
    Builder setCountryCode(String countryCode);

    @JsonProperty(value = "stateCode", required = true)
    Builder setStateCode(String stateCode);

    @JsonProperty(value = "fipsCode")
    Builder setFipsCode(String fipsCode);

    @JsonProperty(value = "stateName", required = true)
    Builder setStateName(String countryName);

    @JsonProperty(value = "active")
    Builder setActive(boolean active);

    @JsonProperty(value = "stateUpdateCount")
    Builder setStateUpdateCount(long updateCount);

    State build();
  }
}
