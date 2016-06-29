package com.cagst.swkroa.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.cagst.common.util.CGTCollatorBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

/**
 * Representation of a Transaction within the system.
 *
 * @author Craig Gaskill
 */
public class Transaction implements Serializable, Comparable<Transaction> {
  private long transaction_id;
  private long membership_id;
  private String membership_name;
  private DateTime transaction_dt;
  private TransactionType transaction_type;
  private String transaction_desc;
  private String ref_num;
  private String memo_txt;
  private boolean deposit_ind;

  private List<TransactionEntry> entries = new ArrayList<TransactionEntry>();

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  public long getTransactionUID() {
    return transaction_id;
  }

  protected void setTransactionUID(final long uid) {
    this.transaction_id = uid;
  }

  public long getMembershipUID() {
    return membership_id;
  }

  public void setMembershipUID(final long uid) {
    this.membership_id = uid;
  }

  public String getMembershipName() {
    return membership_name;
  }

  public void setMembershipName(final String name) {
    this.membership_name = name;
  }

  public DateTime getTransactionDate() {
    return transaction_dt;
  }

  public void setTransactionDate(final DateTime transactionDate) {
    this.transaction_dt = transactionDate;
  }

  public TransactionType getTransactionType() {
    return transaction_type;
  }

  public void setTransactionType(final TransactionType transactionType) {
    this.transaction_type = transactionType;
  }

  public BigDecimal getTransactionAmount() {
    BigDecimal amount = new BigDecimal(0d);
    for (TransactionEntry entry : entries) {
      amount = amount.add(entry.getTransactionEntryAmount());
    }

    return amount;
  }

  public String getTransactionDescription() {
    return transaction_desc;
  }

  public void setTransactionDescription(final String desc) {
    this.transaction_desc = desc;
  }

  public String getReferenceNumber() {
    return ref_num;
  }

  public void setReferenceNumber(final String refNum) {
    this.ref_num = refNum;
  }

  public String getMemo() {
    return memo_txt;
  }

  public void setMemo(final String memo) {
    this.memo_txt = memo;
  }

  public boolean isInDeposit() {
    return deposit_ind;
  }

  public void setInDeposit(boolean inDeposit) {
    this.deposit_ind = inDeposit;
  }

  public void clearEntries() {
    entries.clear();
  }

  public void addEntry(final TransactionEntry entry) {
    entries.add(entry);
  }

  public void removeEntry(final TransactionEntry entry) {
    entries.remove(entry);
  }

  public List<TransactionEntry> getTransactionEntries() {
    return Collections.unmodifiableList(entries);
  }

  public void setTransactionEntries(final List<TransactionEntry> entries) {
    this.entries = entries;
  }

  public boolean isActive() {
    return active_ind;
  }

  public void setActive(final boolean active) {
    this.active_ind = active;
  }

  public long getTransactionUpdateCount() {
    return updt_cnt;
  }

  public void setTransactionUpdateCount(final long updateCount) {
    this.updt_cnt = updateCount;
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("transaction_dt", transaction_dt);
    builder.append("transaction_type", transaction_type);
    builder.append("transaction_amount", getTransactionAmount());
    builder.append("transaction_desc", transaction_desc);
    builder.append("ref_num", ref_num);
    builder.append("memo", memo_txt);

    return builder.build();
  }

  @Override
  public int hashCode() {
    return Objects.hash(membership_id, transaction_dt, transaction_type, getTransactionAmount());
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Transaction)) {
      return false;
    }

    Transaction rhs = (Transaction) obj;

    return Objects.equals(membership_id, rhs.getMembershipUID()) &&
        Objects.equals(transaction_dt, rhs.getTransactionDate()) &&
        Objects.equals(transaction_type, rhs.getTransactionType()) &&
        Objects.equals(getTransactionAmount(), rhs.getTransactionAmount());
  }

  @Override
  public int compareTo(final Transaction rhs) {
    if (rhs == null) {
      return 0;
    }

    CGTCollatorBuilder builder = new CGTCollatorBuilder();
    builder.append(transaction_dt, rhs.getTransactionDate());

    return builder.build();
  }
}
