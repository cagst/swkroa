package com.cagst.swkroa.member;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.cagst.common.util.CGTCollatorBuilder;
import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.comment.Comment;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.utils.DefaultMineralUtilities;
import com.cagst.swkroa.utils.MineralUtilities;
import org.joda.time.DateTime;
import org.springframework.util.CollectionUtils;

/**
 * Representation of a Membership within the system.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public final class Membership implements Serializable, Comparable<Membership> {
  private static final long serialVersionUID = 5583617519331577882L;

  private long membership_id;
  private CodeValue entity_type;
  private DateTime next_due_dt;
  private BigDecimal dues_amount;
  private BigDecimal amount_due;
  private long close_reason_id;
  private String close_reason_txt;

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  private List<Member> members = new ArrayList<Member>();
  private List<MembershipCounty> counties = new ArrayList<MembershipCounty>();
  private List<Comment> comments = new ArrayList<Comment>();
  private List<Transaction> transactions = new ArrayList<Transaction>();

  private MineralUtilities mineralUtils = new DefaultMineralUtilities();

  public void setMineralUtilities(final MineralUtilities mineralUtils) {
    this.mineralUtils = mineralUtils;
  }

  public long getMembershipUID() {
    return membership_id;
  }

  public void setMembershipUID(final long membershipID) {
    this.membership_id = membershipID;
  }

  @NotNull
  public CodeValue getEntityType() {
    return entity_type;
  }

  public void setEntityType(final CodeValue entityType) {
    this.entity_type = entityType;
  }

  @NotNull
  public DateTime getNextDueDate() {
    return next_due_dt;
  }

  public void setNextDueDate(final DateTime dueDate) {
    this.next_due_dt = dueDate;
  }

  public BigDecimal getDuesAmount() {
    return dues_amount;
  }

  /**
   * The effective dues amount that is either calculated, based off the members and counties, or specified.
   *
   * @return The effective dues amount for the membership.
   */
  public BigDecimal getEffectiveDuesAmount() {
    if (dues_amount != null) {
      return dues_amount;
    }

    return getCalculatedDuesAmount();
  }

  /**
   * @return The calculated dues amount for the membership.
   */
  public BigDecimal getCalculatedDuesAmount() {
    BigDecimal countyDues = new BigDecimal(0d);
    for (MembershipCounty county : counties) {
      countyDues = countyDues.add(mineralUtils.calculateFeesForMembershipCounty(county));
    }

    BigDecimal memberDues = new BigDecimal(0d);
    for (Member member : members) {
      memberDues = memberDues.add(member.getMemberType().getDuesAmount());
    }

    return memberDues.add(countyDues);
  }

  public void setDuesAmount(final BigDecimal duesAmount) {
    this.dues_amount = duesAmount;
  }

  public BigDecimal getAmountDue() {
    return amount_due;
  }

  public void setAmountDue(final BigDecimal amountDue) {
    this.amount_due = amountDue;
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

  public boolean isActive() {
    return active_ind;
  }

  public void setActive(final boolean active) {
    this.active_ind = active;
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

  @Valid
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
  public int compareTo(Membership rhs) {
    if (rhs == null) {
      return 0;
    }

    CGTCollatorBuilder builder = new CGTCollatorBuilder();
    builder.append(getPrimaryMember(), rhs.getPrimaryMember());

    return builder.build();
  }
}
