package com.cagst.swkroa.contact;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;

import com.cagst.swkroa.utils.SwkroaToStringStyle;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a PhoneNumber within the system.
 *
 * @author Craig Gaskill
 */
@AutoValue
@JsonPropertyOrder({
    "phoneUID",
    "parentEntityUID",
    "parentEntityName",
    "phoneTypeCD",
    "phoneNumber",
    "phoneExtension",
    "primary",
    "active",
    "phoneUpdateCount"
})
@JsonDeserialize(builder = PhoneNumber.Builder.class)
public abstract class PhoneNumber implements Serializable, Comparable<PhoneNumber> {
  private static final long serialVersionUID = 1L;

  @JsonProperty(value = "phoneUID", required = true)
  public abstract long getPhoneUID();

  @JsonProperty(value = "parentEntityUID", required = true)
  public abstract long getParentEntityUID();

  @Nullable
  @JsonProperty(value = "parentEntityName", required = true)
  public abstract String getParentEntityName();

  @JsonProperty(value = "phoneTypeCD", required = true)
  public abstract long getPhoneTypeCD();

  @JsonProperty(value = "phoneNumber", required = true)
  public abstract String getPhoneNumber();

  public String getCleanPhoneNumber() {
    return getPhoneNumber().replaceAll("[^0-9]", "");
  }

  @Nullable
  @JsonProperty(value = "phoneExtension")
  public abstract String getPhoneExtension();

  @JsonProperty(value = "primary", required = true)
  public abstract boolean isPrimary();

  @JsonProperty(value = "active", required = true)
  public abstract boolean isActive();

  @JsonProperty(value = "phoneUpdateCount", required = true)
  public  abstract long getPhoneUpdateCount();

  @Override
  public int hashCode() {
    return Objects.hash(getPhoneNumber(), getPhoneExtension());
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof PhoneNumber)) {
      return false;
    }

    PhoneNumber rhs = (PhoneNumber) obj;

    return Objects.equals(getPhoneNumber(), rhs.getPhoneNumber()) &&
        Objects.equals(getPhoneExtension(), rhs.getPhoneExtension());
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, SwkroaToStringStyle.SWKROA_PREFIX_STYLE);
    builder.append("number", getPhoneNumber());
    builder.append("extension", getPhoneExtension());

    return builder.build();
  }

  @Override
  public int compareTo(final PhoneNumber rhs) {
    CompareToBuilder builder = new CompareToBuilder();
    builder.append(getPhoneNumber(), rhs.getPhoneNumber());
    builder.append(getPhoneExtension(), rhs.getPhoneExtension());

    return builder.build();
  }

  /**
   * Returns a {@link Builder} with default values.
   *
   * @return A {@link Builder}
   */
  public static Builder builder() {
    return new AutoValue_PhoneNumber.Builder()
        .setPhoneUID(0L)
        .setParentEntityUID(0L)
        .setPhoneTypeCD(0L)
        .setPrimary(false)
        .setActive(true)
        .setPhoneUpdateCount(0L);
  }

  /**
   * Returns a {@link Builder} based upon the values from the current {@link PhoneNumber}.
   *
   * @return A new {@link Builder}
   */
  public abstract Builder toBuilder();

  @AutoValue.Builder
  @JsonPOJOBuilder
  public abstract static class Builder {
    @JsonProperty(value = "phoneUID", required = true)
    public abstract Builder setPhoneUID(long phoneUID);

    @JsonProperty(value = "parentEntityUID", required = true)
    public abstract Builder setParentEntityUID(long parentEntityUID);

    @JsonProperty(value = "parentEntityName", required = true)
    public abstract Builder setParentEntityName(String parentEntityName);

    @JsonProperty(value = "phoneTypeCD", required = true)
    public abstract Builder setPhoneTypeCD(long phoneTypeCD);

    @JsonProperty(value = "phoneNumber", required = true)
    public abstract Builder setPhoneNumber(String phoneNumber);

    @JsonProperty(value = "phoneExtension")
    public abstract Builder setPhoneExtension(String phoneExtension);

    @JsonProperty(value = "primary", required = true)
    public abstract Builder setPrimary(boolean primary);

    @JsonProperty(value = "active", required = true)
    public abstract Builder setActive(boolean active);

    @JsonProperty(value = "phoneUpdateCount", required = true)
    public  abstract Builder setPhoneUpdateCount(long updateCount);

    public abstract PhoneNumber build();

    @JsonCreator
    private static Builder builder() {
      return PhoneNumber.builder();
    }
  }
}
