package com.cagst.swkroa.person;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import com.cagst.swkroa.contact.Address;
import com.cagst.swkroa.contact.EmailAddress;
import com.cagst.swkroa.contact.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Representation of a generic Person within the system.
 *
 * @author Craig Gaskill
 */
public class Person implements Serializable, Comparable<Person> {
  private static final long serialVersionUID = 1L;

  private long person_id;
  private long title_cd;
  private String name_last;
  private String name_first;
  private String name_middle;
  private Locale locale;
  private TimeZone time_zone;

  private List<Address> addresses = new ArrayList<>();
  private List<EmailAddress> emailAddresses = new ArrayList<>();
  private List<PhoneNumber> phoneNumbers = new ArrayList<>();

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

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

  public TimeZone getTimeZone() {
    return time_zone;
  }

  /**
   * @return The {@link TimeZone} associated with this Person or the default DateTimeZone if no specific time zone
   * is associated with this person.
   */
  @JsonIgnore
  public TimeZone getEffectiveTimeZone() {
    if (time_zone != null) {
      return time_zone;
    }

    return TimeZone.getDefault();
  }

  public void setTimeZone(final TimeZone time_zone) {
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

  @Override
  public int hashCode() {
    return Objects.hash(name_last, name_first);
  }

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

    return Objects.equals(name_last, rhs.getLastName()) &&
        Objects.equals(name_first, rhs.getFirstName());
  }

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

  @Override
  public int compareTo(final Person rhs) {
    if (rhs == null) {
      return 0;
    }

    Collator collator = Collator.getInstance();
    collator.setStrength(Collator.PRIMARY);

    int cmp = collator.compare(name_last, rhs.getLastName());
    if (cmp != 0) {
      return cmp;
    }

    return collator.compare(name_first, rhs.getFirstName());
  }
}
