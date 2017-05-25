package com.cagst.swkroa.country;

import java.io.Serializable;
import java.util.Objects;

import com.cagst.swkroa.utils.SwkroaToStringStyle;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a County within the system.
 *
 * @author Craig Gaskill
 */
@AutoValue
@JsonPropertyOrder({
    "countyUID",
    "stateCode",
    "countyCode",
    "countyName",
    "swkroaCounty",
    "active",
    "countyUpdateCount"
})
@JsonDeserialize(builder = AutoValue_County.Builder.class)
public abstract class County implements Serializable, Comparable<County> {
  private static final long serialVersionUID = 1L;

  private static final String VOTING_COUNTY = "Voting County";
  private static final String NON_VOTING_COUNTY = "Non-Voting County";

  @JsonProperty(value = "countyUID", required = true)
  public abstract long getCountyUID();

  @JsonProperty(value = "stateCode", required = true)
  public abstract String getStateCode();

  @JsonProperty(value = "countyCode", required = true)
  public abstract String getCountyCode();

  @JsonProperty(value = "countyName", required = true)
  public abstract String getCountyName();

  @JsonProperty(value = "swkroaCounty")
  public abstract boolean isSwkroaCounty();

  public String getVotingCounty() {
    return isSwkroaCounty() ? VOTING_COUNTY : NON_VOTING_COUNTY;
  }

  @JsonProperty(value = "active")
  public abstract boolean isActive();

  @JsonProperty(value = "countyUpdateCount")
  public abstract long getCountyUpdateCount();

  @Override
  public int hashCode() {
    return Objects.hash(getStateCode(), getCountyCode());
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof County)) {
      return false;
    }

    County rhs = (County) obj;

    return Objects.equals(getStateCode(), rhs.getStateCode()) &&
        Objects.equals(getCountyCode(), rhs.getCountyCode());
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, SwkroaToStringStyle.SWKROA_PREFIX_STYLE);
    builder.append("state_code", getStateCode());
    builder.append("county_code", getCountyCode());

    return builder.build();
  }

  @Override
  public int compareTo(final County rhs) {
    int cmp = Boolean.compare(isSwkroaCounty(), rhs.isSwkroaCounty());
    if (cmp != 0) {
      return cmp;
    }

    cmp = getStateCode().compareTo(rhs.getStateCode());
    if (cmp != 0) {
      return cmp;
    }

    return getCountyCode().compareTo(rhs.getCountyCode());
  }

  public static Builder builder() {
    return new AutoValue_County.Builder()
        .setActive(true)
        .setCountyUpdateCount(0L)
        .setSwkroaCounty(false);
  }

  @AutoValue.Builder
  public interface Builder {
    @JsonProperty(value = "countyUID", required = true)
    Builder setCountyUID(long uid);

    @JsonProperty(value = "stateCode", required = true)
    Builder setStateCode(String stateCode);

    @JsonProperty(value = "countyCode", required = true)
    Builder setCountyCode(String countyCode);

    @JsonProperty(value = "countyName", required = true)
    Builder setCountyName(String countyName);

    @JsonProperty(value = "swkroaCounty")
    Builder setSwkroaCounty(boolean swkroaCounty);

    @JsonProperty(value = "active")
    Builder setActive(boolean active);

    @JsonProperty(value = "countyUpdateCount")
    Builder setCountyUpdateCount(long updateCount);

    County build();
  }
}
