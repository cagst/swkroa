package com.cagst.swkroa.member;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Collator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.comment.Comment;
import com.cagst.swkroa.document.Document;
import com.cagst.swkroa.transaction.Transaction;
import org.springframework.util.CollectionUtils;

/**
 * Representation of a Membership within the system.
 *
 * @author Craig Gaskill
 */
public final class Membership implements Serializable, Comparable<Membership> {
  private static final long serialVersionUID = 1L;

  private long membership_id;
  private String membership_name;
  private CodeValue entity_type;
  private LocalDate next_due_dt;
  private long member_id;
  private String company_name;
  private String owner_ident;
  private LocalDate join_dt;
  private MemberType member_type;
  private String name_last;
  private String name_middle;
  private String name_first;
  private BigDecimal calculated_dues;
  private BigDecimal incremental_dues;
  private BigDecimal balance;
  private LocalDateTime last_payment_dt_tm;
  private long close_reason_id;
  private String close_reason_txt;
  private LocalDateTime close_dt_tm;

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  private List<Member> members = new ArrayList<>();
  private List<MembershipCounty> counties = new ArrayList<>();
  private List<Comment> comments = new ArrayList<>();
  private List<Transaction> transactions = new ArrayList<>();
  private List<Document> documents = new ArrayList<>();

  public long getMembershipUID() {
    return membership_id;
  }

  public void setMembershipUID(final long membershipID) {
    this.membership_id = membershipID;
  }

  public String getMembershipName() {
    return membership_name;
  }

  /* package */ void setMembershipName(final String name) {
    this.membership_name = name;
  }

  public CodeValue getEntityType() {
    return entity_type;
  }

  public void setEntityType(final CodeValue entityType) {
    this.entity_type = entityType;
  }

  public LocalDate getNextDueDate() {
    return next_due_dt;
  }

  public void setNextDueDate(final LocalDate dueDate) {
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

  public LocalDate getJoinDate() {
    return join_dt;
  }

  public void setJoinDate(final LocalDate joinDate) {
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

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(final BigDecimal balance) {
    this.balance = balance;
  }

  public LocalDateTime getLastPaymentDate() {
    return last_payment_dt_tm;
  }

  public void setLastPaymentDate(final LocalDateTime paymentDate) {
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

  public LocalDateTime getCloseDate() {
    return close_dt_tm;
  }

  public void setCloseDate(final LocalDateTime closeDate) {
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

  public void clearDocuments() {
    documents.clear();
  }

  public void addDocument(final Document document) {
    this.documents.add(document);
  }

  public void removeDocument(final Document document) {
    this.documents.remove(document);
  }

  public List<Document> getDocuments() {
    return Collections.unmodifiableList(documents);
  }

  public void setDocuments(final List<Document> documents) {
    this.documents = documents;
  }

  @Override
  public int compareTo(final @Nullable Membership rhs) {
    if (rhs == null) {
      return 0;
    }

    Collator collator = Collator.getInstance();
    collator.setStrength(Collator.PRIMARY);

    return collator.compare(getMembershipName(), rhs.getMembershipName());
  }
}
