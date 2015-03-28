package com.cagst.swkroa.member;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.cagst.common.formatter.DefaultNameFormatter;
import com.cagst.common.formatter.NameFormatter;
import com.cagst.common.util.CGTCollatorBuilder;
import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.comment.Comment;
import com.cagst.swkroa.transaction.Transaction;
import org.joda.time.DateTime;
import org.springframework.util.CollectionUtils;

/**
 * Representation of a Membership within the system.
 *
 * @author Craig Gaskill
 */
public final class Membership implements Serializable, Comparable<Membership> {
  private static final long serialVersionUID = 5583617519331577882L;

  private long membership_id;
  private CodeValue entity_type;
  private DateTime next_due_dt;
  private long member_id;
  private String company_name;
  private String owner_ident;
  private DateTime join_dt;
  private MemberType member_type;
  private String name_last;
  private String name_middle;
  private String name_first;
  private BigDecimal fixed_dues;
  private BigDecimal calculated_dues;
  private BigDecimal incremental_dues;
  private BigDecimal balance;
  private DateTime last_payment_dt_tm;
  private long close_reason_id;
  private String close_reason_txt;
  private DateTime close_dt_tm;

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  private List<Member> members = new ArrayList<Member>();
  private List<MembershipCounty> counties = new ArrayList<MembershipCounty>();
  private List<Comment> comments = new ArrayList<Comment>();
  private List<Transaction> transactions = new ArrayList<Transaction>();

  private NameFormatter nameFormatter = new DefaultNameFormatter();

  public long getMembershipUID() {
    return membership_id;
  }

  public void setMembershipUID(final long membershipID) {
    this.membership_id = membershipID;
  }

  public CodeValue getEntityType() {
    return entity_type;
  }

  public void setEntityType(final CodeValue entityType) {
    this.entity_type = entityType;
  }

  public DateTime getNextDueDate() {
    return next_due_dt;
  }

  public void setNextDueDate(final DateTime dueDate) {
    this.next_due_dt = dueDate;
  }

  public long getMemberUID() {
    return member_id;
  }

  public void setMemberUID(final long memberID) {
    this.member_id = memberID;
  }

  public String getCompanyName() {
    return company_name;
  }

  public void setCompanyName(final String companyName) {
    this.company_name = companyName;
  }

  public String getOwnerId() {
    return owner_ident;
  }

  public void setOwnerId(final String ownderId) {
    this.owner_ident = ownderId;
  }

  public DateTime getJoinDate() {
    return join_dt;
  }

  public void setJoinDate(final DateTime joinDate) {
    this.join_dt = joinDate;
  }

  public MemberType getMemberType() {
    return member_type;
  }

  public void setMemberType(final MemberType memberType) {
    this.member_type = memberType;
  }

  public String getLastName() {
    return name_last;
  }

  public void setLastName(final String lastName) {
    this.name_last = lastName;
  }

  public String getMiddleName() {
    return name_middle;
  }

  public void setMiddleName(final String middleName) {
    this.name_middle = middleName;
  }

  public String getFirstName() {
    return name_first;
  }

  public void setFirstName(final String firstName) {
    this.name_first = firstName;
  }

  public String getFullName() {
    if (nameFormatter == null) {
      return null;
    }

    return nameFormatter.formatFullName(name_last, name_first, name_middle);
  }

  public String getMembershipName() {
    if (company_name != null) {
      return company_name;
    }

    return getFullName();
  }

  public BigDecimal getFixedDuesAmount() {
    return fixed_dues;
  }

  public void setFixedDuesAmount(final BigDecimal dues) {
    this.fixed_dues = dues;
  }

  public BigDecimal getCalculatedDuesAmount() {
    if (CollectionUtils.isEmpty(members)) {
      return (calculated_dues == null ? BigDecimal.ZERO : calculated_dues);
    }

    BigDecimal memberDues = BigDecimal.ZERO;
    for (Member member : members) {
      memberDues = memberDues.add(member.getMemberType().getDuesAmount());
    }

    return memberDues.add(incremental_dues != null ? incremental_dues : BigDecimal.ZERO);
  }

  public void setCalculatedDuesAmount(final BigDecimal dues) {
    this.calculated_dues = dues;
  }

  public BigDecimal getIncrementalDues() {
    return incremental_dues;
  }

  public void setIncrementalDues(final BigDecimal dues) {
    this.incremental_dues = dues;
  }

  /**
   * The effective dues amount that is either calculated, based off the members and counties, or specified.
   *
   * @return The effective dues amount for the membership.
   */
  public BigDecimal getEffectiveDuesAmount() {
    if (getFixedDuesAmount() != null) {
      return getFixedDuesAmount();
    }

    return getCalculatedDuesAmount();
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(final BigDecimal balance) {
    this.balance = balance;
  }

  public DateTime getLastPaymentDate() {
    return last_payment_dt_tm;
  }

  public void setLastPaymentDate(final DateTime paymentDate) {
    this.last_payment_dt_tm = paymentDate;
  }

  public boolean isActive() {
    return active_ind;
  }

  public void setActive(final boolean active) {
    this.active_ind = active;
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

  public long getMembershipUpdateCount() {
    return updt_cnt;
  }

  public void setMembershipUpdateCount(final long updateCount) {
    this.updt_cnt = updateCount;
  }

  public void clearMembers() {
    members.clear();
  }

  public void addMember(final Member member) {
    members.add(member);
  }

  public void removeMember(final Member member) {
    members.remove(member);
  }

  public List<Member> getMembers() {
    return Collections.unmodifiableList(members);
  }

  public void setMembers(final List<Member> members) {
    this.members = members;
  }

  /**
   * Helper method to return the "primary" member associated with this membership.
   *
   * @return The "primary" {@link Member} associated with this membership,
   * {@code null} if none is found making the membership invalid.
   */
  public Member getPrimaryMember() {
    for (Member member : members) {
      if (member.getMemberType().isPrimary()) {
        return member;
      }
    }

    if (CollectionUtils.isEmpty(members)) {
      return null;
    } else {
      return members.get(0);
    }
  }

  /**
   * Helper method to replace (or add) the "primary" member associated with this membership.
   *
   * @param primary
   *     The {@link Member} to replace (or add) as the "primary" member.
   */
  public void setPrimaryMember(final Member primary) {
    // remove the existing "primary" member if one exists
    Iterator<Member> it = members.iterator();
    while (it.hasNext()) {
      if (it.next().getMemberType().isPrimary()) {
        it.remove();
        break;
      }
    }

    // add the "primary" member to our members list
    members.add(primary);
  }

  /**
   * Helper method to return the "spouse" associated with this membership.
   *
   * @return The "spouse" {@link Member} associated with this membership,
   * {@code null} if no spouse is found.
   */
  public Member getSpouse() {
    for (Member member : members) {
      if (MemberType.SPOUSE.equals(member.getMemberType().getMemberTypeMeaning())) {
        return member;
      }
    }

    return null;
  }

  /**
   * Helper method to replace (or add) the "spouse" member associated with this membership.
   *
   * @param spouse
   *     The {@link Member} to replace (or add) as the "spouse" member.
   */
  public void setSpouse(final Member spouse) {
    if (spouse == null) {
      return;
    }

    // remove the existing "spouse" member if one exists
    Iterator<Member> it = members.iterator();
    while (it.hasNext()) {
      if (MemberType.SPOUSE.equals(it.next().getMemberType().getMemberTypeMeaning())) {
        it.remove();
        break;
      }
    }

    // add the "spouse" member to our members list
    members.add(spouse);
  }

  public void clearCounties() {
    counties.clear();
  }

  public void addCounty(final MembershipCounty county) {
    counties.add(county);
  }

  public void removeCounty(final MembershipCounty county) {
    counties.remove(county);
  }

  public List<MembershipCounty> getMembershipCounties() {
    return Collections.unmodifiableList(counties);
  }

  public void setMembershipCounties(final List<MembershipCounty> membershipCounties) {
    this.counties = membershipCounties;
  }

  public void clearComments() {
    comments.clear();
  }

  public void addComment(final Comment comment) {
    comments.add(comment);
  }

  public void removeComment(final Comment comment) {
    comments.remove(comment);
  }

  public List<Comment> getComments() {
    return Collections.unmodifiableList(comments);
  }

  public void setComments(final List<Comment> comments) {
    this.comments = comments;
  }

  public void clearTransactions() {
    transactions.clear();
  }

  public void addTransaction(final Transaction transaction) {
    this.transactions.add(transaction);
  }

  public void removeTransaction(final Transaction transaction) {
    this.transactions.remove(transaction);
  }

  public List<Transaction> getTransactions() {
    return Collections.unmodifiableList(transactions);
  }

  public void setTransactions(final List<Transaction> transactions) {
    this.transactions = transactions;
  }

  @Override
  public int compareTo(final @Nullable Membership rhs) {
    if (rhs == null) {
      return 0;
    }

    CGTCollatorBuilder builder = new CGTCollatorBuilder();
    builder.append(getMembershipName(), rhs.getMembershipName());

    return builder.build();
  }
}
