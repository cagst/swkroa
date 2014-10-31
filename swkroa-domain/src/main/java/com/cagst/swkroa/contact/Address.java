package com.cagst.swkroa.contact;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.*;

/**
 * Represents an Address within the system.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public final class Address implements Serializable, Comparable<Address> {
  private static final long serialVersionUID = 7499345705626185164L;

  private long address_id;
  private long parent_entity_id;
  private String parent_entity_name;
  private long address_type_cd;
  private String address1;
  private String address2;
  private String address3;
  private String city;
  private String state_code;
  private String postal_code;
  private String country_code = "US";
  private boolean primary_ind;

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  public long getAddressUID() {
    return address_id;
  }

  /* package */void setAddressUID(final long uid) {
    this.address_id = uid;
  }

  @JsonIgnore
  public long getParentEntityUID() {
    return parent_entity_id;
  }

  public void setParentEntityUID(final long uid) {
    this.parent_entity_id = uid;
  }

  @JsonIgnore
  public String getParentEntityName() {
    return parent_entity_name;
  }

  public void setParentEntityName(final String name) {
    this.parent_entity_name = name;
  }

  @NotNull
  public long getAddressTypeCD() {
    return address_type_cd;
  }

  public void setAddressTypeCD(final long address_type_cd) {
    this.address_type_cd = address_type_cd;
  }

  @NotNull
  @Size(min = 1, max = 50)
  public String getAddressLine1() {
    return address1;
  }

  public void setAddressLine1(final String address1) {
    this.address1 = address1;
  }

  @Size(max = 50)
  public String getAddressLine2() {
    return address2;
  }

  public void setAddressLine2(final String address2) {
    this.address2 = address2;
  }

  @Size(max = 50)
  public String getAddressLine3() {
    return address3;
  }

  public void setAddressLine3(final String address3) {
    this.address3 = address3;
  }

  @NotNull
  @Size(min = 1, max = 50)
  public String getCity() {
    return city;
  }

  public void setCity(final String city) {
    this.city = city;
  }

  @NotNull
  @Size(min = 1, max = 2)
  public String getState() {
    return state_code;
  }

  public void setState(final String stateCode) {
    this.state_code = stateCode;
  }

  @NotNull
  @Size(min = 1, max = 15)
  public String getPostalCode() {
    return postal_code;
  }

  public void setPostalCode(final String postalCode) {
    this.postal_code = postalCode;
  }

  @NotNull
  @Size(min = 1, max = 2)
  public String getCountry() {
    return country_code;
  }

  public void setCountry(final String countryCode) {
    this.country_code = countryCode;
  }

  public boolean isPrimary() {
    return primary_ind;
  }

  public void setPrimary(boolean primary_ind) {
    this.primary_ind = primary_ind;
  }

  public boolean isActive() {
    return active_ind;
  }

  public void setActive(final boolean active) {
    this.active_ind = active;
  }

  public long getAddressUpdateCount() {
    return updt_cnt;
  }

  /* package */void setAddressUpdateCount(final long updateCount) {
    this.updt_cnt = updateCount;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(address1);
    builder.append(address2);
    builder.append(address3);
    builder.append(city);
    builder.append(state_code);
    builder.append(postal_code);
    builder.append(country_code);

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
    if (!(obj instanceof Address)) {
      return false;
    }

    Address rhs = (Address) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.append(address1, rhs.getAddressLine1());
    builder.append(address2, rhs.getAddressLine2());
    builder.append(address3, rhs.getAddressLine3());
    builder.append(city, rhs.getCity());
    builder.append(state_code, rhs.getState());
    builder.append(postal_code, rhs.getPostalCode());
    builder.append(country_code, rhs.getCountry());

    return builder.build();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("line1", address1);
    builder.append("line2", address2);
    builder.append("line3", address3);
    builder.append("city", city);
    builder.append("state_code", state_code);
    builder.append("zip", postal_code);
    builder.append("country_code", country_code);

    return builder.build();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Address rhs) {
    CompareToBuilder builder = new CompareToBuilder();
    builder.append(address1, rhs.getAddressLine1());
    builder.append(address2, rhs.getAddressLine2());
    builder.append(address3, rhs.getAddressLine3());
    builder.append(city, rhs.getCity());
    builder.append(state_code, rhs.getState());
    builder.append(postal_code, rhs.getPostalCode());
    builder.append(country_code, rhs.getCountry());

    return builder.build();
  }
}
