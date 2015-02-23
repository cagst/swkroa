package com.cagst.swkroa.contact;

import java.io.Serializable;
import java.text.Collator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents an EmailAddress within the system.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public final class EmailAddress implements Serializable, Comparable<EmailAddress> {
  private static final long serialVersionUID = 722090805243595723L;

  private long email_id;
  private long parent_entity_id;
  private String parent_entity_name;
  private long email_type_cd;
  private String email_address;
  private boolean primary_ind;

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  public long getEmailAddressUID() {
    return email_id;
  }

  /* package */void setEmailAddressUID(final long uid) {
    this.email_id = uid;
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

  public long getEmailTypeCD() {
    return email_type_cd;
  }

  public void setEmailTypeCD(final long email_type_cd) {
    this.email_type_cd = email_type_cd;
  }

  public String getEmailAddress() {
    return email_address;
  }

  public void setEmailAddress(final String emailAddress) {
    this.email_address = emailAddress;
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

  public long getEmailAddressUpdateCount() {
    return updt_cnt;
  }

  /* package */void setEmailAddressUpdateCount(final long updateCount) {
    this.updt_cnt = updateCount;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(email_address);

    return builder.build();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof EmailAddress)) {
      return false;
    }

    EmailAddress rhs = (EmailAddress) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.append(email_address, rhs.getEmailAddress());

    return builder.build();
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("email", email_address);

    return builder.build();
  }

  @Override
  public int compareTo(final EmailAddress rhs) {
    Collator collator = Collator.getInstance();
    collator.setStrength(Collator.PRIMARY);

    return collator.compare(email_address, rhs.getEmailAddress());
  }
}
