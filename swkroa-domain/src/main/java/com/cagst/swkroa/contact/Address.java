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
 * Represents an Address within the system.
 *
 * @author Craig Gaskill
 */
@AutoValue
@JsonPropertyOrder({
    "addressUID",
    "parentEntityUID",
    "parentEntityName",
    "addressTypeCD",
    "addressLine1",
    "addressLine2",
    "addressLine3",
    "city",
    "state",
    "postalCode",
    "country",
    "primary",
    "active",
    "addressUpdateCount"
})
@JsonDeserialize(builder = Address.Builder.class)
public abstract class Address implements Serializable, Comparable<Address> {
  private static final long serialVersionUID = 1L;

  @JsonProperty(value = "addressUID", required = true)
  public abstract long getAddressUID();

  @JsonProperty(value = "parentEntityUID", required = true)
  public abstract long getParentEntityUID();

  @JsonProperty(value = "parentEntityName", required = true)
  public abstract String getParentEntityName();

  @JsonProperty(value = "addressTypeCD", required = true)
  public abstract long getAddressTypeCD();

  @JsonProperty(value = "addressLine1", required = true)
  public abstract String getAddressLine1();

  @Nullable
  @JsonProperty(value = "addressLine2")
  public abstract String getAddressLine2();

  @Nullable
  @JsonProperty(value = "addressLine3")
  public abstract String getAddressLine3();

  @JsonProperty(value = "city", required =  true)
  public abstract String getCity();

  @JsonProperty(value = "state", required = true)
  public abstract String getState();

  @JsonProperty(value = "postalCode", required = true)
  public abstract String getPostalCode();

  @JsonProperty(value = "country", required = true)
  public abstract String getCountry();

  @JsonProperty(value = "primary", required = true)
  public abstract boolean isPrimary();

  @JsonProperty(value = "active", required = true)
  public abstract boolean isActive();

  @JsonProperty(value = "addressUpdateCount", required = true)
  public abstract long getAddressUpdateCount();

  @Override
  public int hashCode() {
    return Objects.hash(
        getAddressLine1(),
        getAddressLine2(),
        getAddressLine3(),
        getCity(),
        getState(),
        getPostalCode(),
        getCountry());
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Address)) {
      return false;
    }

    Address rhs = (Address) obj;

    return Objects.equals(getAddressLine1(), rhs.getAddressLine1()) &&
        Objects.equals(getAddressLine2(), rhs.getAddressLine2()) &&
        Objects.equals(getAddressLine3(), rhs.getAddressLine3()) &&
        Objects.equals(getCity(), rhs.getCity()) &&
        Objects.equals(getState(), rhs.getState()) &&
        Objects.equals(getPostalCode(), rhs.getPostalCode()) &&
        Objects.equals(getCountry(), rhs.getCountry());
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, SwkroaToStringStyle.SWKROA_PREFIX_STYLE);
    builder.append("line1", getAddressLine1());
    builder.append("line2", getAddressLine2());
    builder.append("line3", getAddressLine3());
    builder.append("city", getCity());
    builder.append("state_code", getState());
    builder.append("zip", getPostalCode());
    builder.append("country_code", getCountry());

    return builder.build();
  }

  @Override
  public int compareTo(final Address rhs) {
    CompareToBuilder builder = new CompareToBuilder();
    builder.append(getAddressLine1(), rhs.getAddressLine1());
    builder.append(getAddressLine2(), rhs.getAddressLine2());
    builder.append(getAddressLine3(), rhs.getAddressLine3());
    builder.append(getCity(), rhs.getCity());
    builder.append(getState(), rhs.getState());
    builder.append(getPostalCode(), rhs.getPostalCode());
    builder.append(getCountry(), rhs.getCountry());

    return builder.build();
  }

  /**
   * Returns a {@link Builder} with default values.
   *
   * @return A {@link Builder}
   */
  public static Builder builder() {
    return new AutoValue_Address.Builder()
        .setAddressUID(0L)
        .setParentEntityUID(0L)
        .setPrimary(false)
        .setActive(true)
        .setAddressUpdateCount(0L);
  }

  /**
   * Returns a {@link Builder} based upon the values from the specified {@link Address}.
   *
   * @param address
   *    The {@link Address} to base this builder off of.
   *
   * @return A {@link Builder}.
   */
  public static Builder builder(Address address) {
    return new AutoValue_Address.Builder()
        .setAddressUID(address.getAddressUID())
        .setParentEntityUID(address.getParentEntityUID())
        .setParentEntityName(address.getParentEntityName())
        .setAddressTypeCD(address.getAddressTypeCD())
        .setAddressLine1(address.getAddressLine1())
        .setAddressLine2(address.getAddressLine2())
        .setAddressLine3(address.getAddressLine3())
        .setCity(address.getCity())
        .setState(address.getState())
        .setPostalCode(address.getPostalCode())
        .setCountry(address.getCountry())
        .setPrimary(address.isPrimary())
        .setActive(address.isActive())
        .setAddressUpdateCount(address.getAddressUpdateCount());
  }

  @AutoValue.Builder
  @JsonPOJOBuilder
  public abstract static class Builder {
    @JsonProperty(value = "addressUID")
    public abstract Builder setAddressUID(long uid);

    @JsonProperty(value = "parentEntityUID")
    public abstract Builder setParentEntityUID(long parentEntityUID);

    @JsonProperty(value = "parentEntityName")
    public abstract Builder setParentEntityName(String parentEntityName);

    @JsonProperty(value = "addressTypeCD", required = true)
    public abstract Builder setAddressTypeCD(long addressTypeCD);

    @JsonProperty(value = "addressLine1", required = true)
    public abstract Builder setAddressLine1(String addressLine1);

    @JsonProperty(value = "addressLine2")
    public abstract Builder setAddressLine2(String addressLine2);

    @JsonProperty(value = "addressLine3")
    public abstract Builder setAddressLine3(String addressLine3);

    @JsonProperty(value = "city", required =  true)
    public abstract Builder setCity(String city);

    @JsonProperty(value = "state", required = true)
    public abstract Builder setState(String state);

    @JsonProperty(value = "postalCode", required = true)
    public abstract Builder setPostalCode(String postalCode);

    @JsonProperty(value = "country", required = true)
    public abstract Builder setCountry(String country);

    @JsonProperty(value = "primary")
    public abstract Builder setPrimary(boolean primary);

    @JsonProperty(value = "active")
    public abstract Builder setActive(boolean active);

    @JsonProperty(value = "addressUpdateCount")
    public abstract Builder setAddressUpdateCount(long updateCount);

    public abstract Address build();

    @JsonCreator
    private static Builder builder() {
      return Address.builder();
    }
  }
}
