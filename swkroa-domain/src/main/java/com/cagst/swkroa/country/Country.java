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
 * Represents a Country within the system.
 *
 * @author Craig Gaskill
 */
@AutoValue
@JsonPropertyOrder({
    "countryCode",
    "countryName",
    "active",
    "countryUpdateCount"
})
@JsonDeserialize(builder = AutoValue_Country.Builder.class)
public abstract class Country implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty(value = "countryCode", required = true)
  public abstract String getCountryCode();

  @JsonProperty(value = "countryName", required = true)
  public abstract String getCountryName();

  @JsonProperty(value = "active")
  public abstract boolean isActive();

  @JsonProperty(value = "countryUpdateCount")
  public abstract long getCountryUpdateCount();

  @Override
  public int hashCode() {
    return getCountryCode().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Country)) {
      return false;
    }

    Country rhs = (Country) obj;

    return Objects.equals(getCountryCode(), rhs.getCountryCode());
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, SwkroaToStringStyle.SWKROA_PREFIX_STYLE);
    builder.append("country_code", getCountryCode());
    builder.append("country_name", getCountryName());

    return builder.build();
  }

  public static Builder builder() {
    return new AutoValue_Country.Builder()
        .setActive(true);
  }

  @AutoValue.Builder
  public interface Builder {
    @JsonProperty(value = "countryCode", required = true)
    Builder setCountryCode(String countryCode);

    @JsonProperty(value = "countryName", required = true)
    Builder setCountryName(String countryName);

    @JsonProperty(value = "active")
    Builder setActive(boolean active);

    @JsonProperty(value = "countryUpdateCount")
    Builder setCountryUpdateCount(long updateCount);

    Country build();
  }
}
