package com.cagst.swkroa.person;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.cagst.common.formatter.DefaultNameFormatter;
import com.cagst.common.formatter.NameFormatter;
import com.cagst.common.util.CGTCollatorBuilder;
import com.cagst.swkroa.contact.Address;
import com.cagst.swkroa.contact.EmailAddress;
import com.cagst.swkroa.contact.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Representation of a generic Person within the system.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public class Person implements Serializable, Comparable<Person> {
  private static final long serialVersionUID = 4546930957600757055L;

  private long person_id;
  private long title_cd;
  private String name_last;
  private String name_first;
  private String name_middle;
  private Locale locale;
  private DateTimeZone time_zone;

  private List<Address> addresses = new ArrayList<Address>();
  private List<EmailAddress> emailAddresses = new ArrayList<EmailAddress>();
  private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  @Autowired
  private final NameFormatter nameFormatter = new DefaultNameFormatter();

  /**
   * Gets the unique identifier for the Person.
   *
   * @return A {@link long} that uniquely identifies the Person.
   */
  public long getPersonUID() {
    return person_id;
  }

  /**
   * Sets the unique identifier for the Person.
   *
   * @param personUID
   *     A {@link long} that uniquely identifies the Person.
   */
  public void setPersonUID(final long personUID) {
    this.person_id = personUID;
  }

  public long getTitleCD() {
    return title_cd;
  }

  public void setTitleCD(final long title_cd) {
    this.title_cd = title_cd;
  }

  /**
   * Gets the last name for the Person.
   *
   * @return {@link String} the Persons last name.
   */
  @NotNull
  @Size(min = 1, max = 100)
  public String getLastName() {
    return name_last;
  }

  /**
   * Sets the last name for the Person.
   *
   * @param lastName
   *     {@link String} the Persons last name.
   */
  public void setLastName(final String lastName) {
    this.name_last = lastName;
  }

  /**
   * Gets the first name for the Person.
   *
   * @return {@link String} the Persons first name.
   */
  @NotNull
  @Size(min = 1, max = 100)
  public String getFirstName() {
    return name_first;
  }

  /**
   * Sets the first name for the Person.
   *
   * @param firstName
   *     {@link String} the Persons first name.
   */
  public void setFirstName(final String firstName) {
    this.name_first = firstName;
  }

  /**
   * Gets the middle name for the Person.
   *
   * @return {@link String} the Persons middle name.
   */

  @Size(max = 100)
  public String getMiddleName() {
    return name_middle;
  }

  /**
   * Sets the middle name for the Person.
   *
   * @param middleName
   *     {@link String} the Persons middle name.
   */
  public void setMiddleName(final String middleName) {
    this.name_middle = middleName;
  }

  public String getFullName() {
    if (getPersonUID() == 1L) {
      return name_last;
    }

    return nameFormatter.formatFullName(name_last, name_first, name_middle);
  }

  public Locale getLocale() {
    return locale;
  }

  /**
   * @return The {@link Locale} associated with this Person or the default Locale if no specific locale is associated
   * with this person.
   */
  @JsonIgnore
  public Locale getEffectiveLocale() {
    if (locale != null) {
      return locale;
    }

    return Locale.getDefault();
  }

  public void setLocale(final Locale locale) {
    this.locale = locale;
  }

  public DateTimeZone getTimeZone() {
    return time_zone;
  }

  /**
   * @return The {@link DateTimeZone} associated with this Person or the default DateTimeZone if no specific time zone
   * is associated with this person.
   */
  @JsonIgnore
  public DateTimeZone getEffectiveTimeZone() {
    if (time_zone != null) {
      return time_zone;
    }

    return DateTimeZone.getDefault();
  }

  public void setTimeZone(final DateTimeZone time_zone) {
    this.time_zone = time_zone;
  }

  public void clearAddresses() {
    addresses.clear();
  }

  public void addAddress(final Address address) {
    addresses.add(address);
  }

  public void removeAddress(final Address address) {
    addresses.remove(address);
  }

  @Valid
  public List<Address> getAddresses() {
    return addresses;
  }

  public void setAddresses(final List<Address> addresses) {
    this.addresses = addresses;
  }

  public void clearEmailAddresses() {
    emailAddresses.clear();
  }

  public void addEmailAddress(final EmailAddress email) {
    emailAddresses.add(email);
  }

  public void removeEmailAddress(final EmailAddress email) {
    emailAddresses.remove(email);
  }

  @Valid
  public List<EmailAddress> getEmailAddresses() {
    return emailAddresses;
  }

  public void setEmailAddresses(final List<EmailAddress> emailAddresses) {
    this.emailAddresses = emailAddresses;
  }

  public void clearPhoneNumbers() {
    phoneNumbers.clear();
  }

  public void addPhoneNumber(final PhoneNumber phone) {
    phoneNumbers.add(phone);
  }

  public void removePhoneNumber(final PhoneNumber phone) {
    phoneNumbers.remove(phone);
  }

  @Valid
  public List<PhoneNumber> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(final List<PhoneNumber> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
  }

  /**
   * Gets the active status of the Person.
   *
   * @return {@link boolean} <code>true</code> if the Person is active, <code>false</code> otherwise.
   */
  public boolean isActive() {
    return active_ind;
  }

  /**
   * Sets the active status of the Person.
   *
   * @param active
   *     {@link boolean} <code>true</code> to make the Person active, <code>false</code> to make the object
   *     inactive.
   */
  public void setActive(final boolean active) {
    this.active_ind = active;
  }

  /**
   * Gets the number of times this object has been updated.
   *
   * @return {@link long} number of times the object has been updated.
   */
  public long getPersonUpdateCount() {
    return updt_cnt;
  }

  /**
   * Sets the number of times this object has been updated.
   *
   * @param updateCount
   *     {@link long} the number of times the object has been updated.
   */
  public void setPersonUpdateCount(final long updateCount) {
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
    builder.append(name_last);
    builder.append(name_first);

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
    if (!(obj instanceof Person)) {
      return false;
    }

    Person rhs = (Person) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.append(name_last, rhs.getLastName());
    builder.append(name_first, rhs.getFirstName());

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
    builder.append("person_id", person_id);
    builder.append("name_last", name_last);
    builder.append("name_first", name_first);
    builder.append("updateCount", updt_cnt);
    builder.appendSuper(super.toString());

    return builder.build();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Person rhs) {
    if (rhs == null) {
      return 0;
    }

    CGTCollatorBuilder builder = new CGTCollatorBuilder();
    builder.append(name_last, rhs.getLastName());
    builder.append(name_first, rhs.getFirstName());

    return builder.build();
  }
}
