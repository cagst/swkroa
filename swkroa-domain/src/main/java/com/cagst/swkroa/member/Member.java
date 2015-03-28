package com.cagst.swkroa.member;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.cagst.common.util.CGTCollatorBuilder;
import com.cagst.common.util.CGTStringUtils;
import com.cagst.swkroa.contact.Address;
import com.cagst.swkroa.contact.EmailAddress;
import com.cagst.swkroa.contact.PhoneNumber;
import com.cagst.swkroa.person.Person;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

/**
 * Representation of a Member within the system.
 *
 * @author Craig Gaskill
 */
public final class Member implements Serializable, Comparable<Member> {
  private static final long serialVersionUID = 3919236058086901376L;

  private long member_id;
  private String member_name;
  private long membership_id;
  private String company_name;
  private Person person;
  private String owner_ident;
  private MemberType member_type;
  private String greeting;
  private String inCareOf;
  private DateTime join_dt;
  private boolean mail_newsletter_ind = true;
  private boolean email_newsletter_ind = false;
  private long close_reason_id;
  private String close_reason_txt;
  private DateTime close_dt_tm;

  private List<Address> addresses = new ArrayList<Address>();
  private List<EmailAddress> emailAddresses = new ArrayList<EmailAddress>();
  private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  /**
   * Gets the unique identifier for the Member.
   *
   * @return A {@link long} that uniquely identifies the Member.
   */
  public long getMemberUID() {
    return member_id;
  }

  /**
   * Sets the unique identifier for the Member.
   *
   * @param uid
   *     A {@link long} that uniquely identifies the Member.
   */
  public void setMemberUID(final long uid) {
    this.member_id = uid;
  }

  public String getMemberName() {
    return member_name;
  }

  /* package */ void setMemberName(final String name) {
    this.member_name = name;
  }

  public long getMembershipUID() {
    return membership_id;
  }

  /* package */ void setMembershipUID(final long uid) {
    this.membership_id = uid;
  }

  public String getCompanyName() {
    return company_name;
  }

  public void setCompanyName(final String name) {
    this.company_name = name;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(final Person person) {
    this.person = person;
  }

  /**
   * Gets the user identifiable identifier for the Member.
   *
   * @return A {@link String} that identifies a Member for the user.
   */
  public String getOwnerIdent() {
    return owner_ident;
  }

  /**
   * Sets the user identifiable identifier for the Member.
   *
   * @param ownerIdent
   *     The {@link String} that identifies the Member for the user.
   */
  public void setOwnerIdent(final String ownerIdent) {
    this.owner_ident = CGTStringUtils.normalizeToKey(ownerIdent);
  }

  public MemberType getMemberType() {
    return member_type;
  }

  public void setMemberType(final MemberType memberType) {
    this.member_type = memberType;
  }

  public String getGreeting() {
    return greeting;
  }

  public void setGreeting(final String greeting) {
    this.greeting = greeting;
  }

  public String getInCareOf() {
    return inCareOf;
  }

  public void setInCareOf(final String inCareOf) {
    this.inCareOf = inCareOf;
  }

  /**
   * @return The {@link DateTime} the member started (Joined) the association.
   */
  public DateTime getJoinDate() {
    return join_dt;
  }

  public void setJoinDate(final DateTime joinDate) {
    this.join_dt = joinDate;
  }

  public boolean isMailNewsletter() {
    return mail_newsletter_ind;
  }

  public void setMailNewsletter(final boolean mail_newsletter) {
    this.mail_newsletter_ind = mail_newsletter;
  }

  public boolean isEmailNewsletter() {
    return email_newsletter_ind;
  }

  public void setEmailNewsletter(final boolean emailNewsletter) {
    this.email_newsletter_ind = emailNewsletter;
  }

  public long getCloseReasonUID() {
    return close_reason_id;
  }

  public void setCloseReasonUID(final long closeReasonUID) {
    this.close_reason_id = closeReasonUID;
  }

  public String getCloseReasonText() {
    return close_reason_txt;
  }

  public void setCloseReasonText(final String closeReasonText) {
    this.close_reason_txt = closeReasonText;
  }

  public DateTime getCloseDate() {
    return close_dt_tm;
  }

  public void setCloseDate(final DateTime closeDate) {
    this.close_dt_tm = closeDate;
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

  public boolean isActive() {
    return active_ind;
  }

  public void setActive(final boolean active) {
    this.active_ind = active;
  }

  public long getMemberUpdateCount() {
    return updt_cnt;
  }

  public void setMemberUpdateCount(final long updateCount) {
    this.updt_cnt = updateCount;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.appendSuper(super.hashCode());
    builder.append(owner_ident);

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
    if (!(obj instanceof Member)) {
      return false;
    }

    Member rhs = (Member) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.appendSuper(super.equals(obj));
    builder.append(owner_ident, rhs.getOwnerIdent());

    return builder.build();
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.appendSuper(super.toString());
    builder.append(owner_ident);

    return builder.build();
  }

  @Override
  public int compareTo(final Member rhs) {
    if (rhs == null) {
      return 0;
    }

    CGTCollatorBuilder builder = new CGTCollatorBuilder();
    builder.append(getMemberName(), rhs.getMemberName());

    return builder.build();
  }
}
