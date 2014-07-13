package com.cagst.swkroa.transaction;

import com.cagst.common.util.CGTCollatorBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representation of a Transaction within the system.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public final class Transaction implements Serializable, Comparable<Transaction> {
  private static final long serialVersionUID = -5519051342330445823L;

  private long transaction_id;
  private long membership_id;
  private DateTime transaction_dt;
  private TransactionType transaction_type;
  private String transaction_desc;
  private String ref_num;
  private String memo_txt;

  private List<TransactionEntry> entries = new ArrayList<TransactionEntry>();

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  public long getTransactionUID() {
    return transaction_id;
  }

  /* package */ void setTransactionUID(final long uid) {
    this.transaction_id = uid;
  }

  public long getMembershipUID() {
    return membership_id;
  }

  public void setMembershipUID(final long uid) {
    this.membership_id = uid;
  }

  @NotNull
  public DateTime getTransactionDate() {
    return transaction_dt;
  }

  public void setTransactionDate(final DateTime transactionDate) {
    this.transaction_dt = transactionDate;
  }

  @NotNull
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

  public BigDecimal getUnrelatedAmount() {
    BigDecimal amount = new BigDecimal(0d);
    if (transaction_type == TransactionType.PAYMENT) {
      for (TransactionEntry entry : entries) {
        if (entry.getRelatedTransactionUID() == 0l) {
          amount = amount.add(entry.getTransactionEntryAmount());
        }
      }
    }

    return amount;
  }

  public BigDecimal getBalance() {
    BigDecimal balance = getTransactionAmount();
    if (transaction_type == TransactionType.PAYMENT) {
      for (TransactionEntry entry : entries) {
        if (entry.getRelatedTransactionUID() != 0l) {
          balance = balance.add(entry.getTransactionEntryAmount());
        }
      }
    }

    return balance;
  }

  @Size(max = 50)
  public String getTransactionDescription() {
    return transaction_desc;
  }

  public void setTransactionDescription(final String desc) {
    this.transaction_desc = desc;
  }

  @Size(max = 50)
  public String getReferenceNumber() {
    return ref_num;
  }

  public void setReferenceNumber(final String refNum) {
    this.ref_num = refNum;
  }

  @Size(max = 250)
  public String getMemo() {
    return memo_txt;
  }

  public void setMemo(final String memo) {
    this.memo_txt = memo;
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

  @Size(min = 1)
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

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
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

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(membership_id);
    builder.append(transaction_dt);
    builder.append(transaction_type);
    builder.append(getTransactionAmount());

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
    if (!(obj instanceof Transaction)) {
      return false;
    }

    Transaction rhs = (Transaction) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.append(membership_id, rhs.getMembershipUID());
    builder.append(transaction_dt, rhs.getTransactionDate());
    builder.append(transaction_type, rhs.getTransactionType());
    builder.append(getTransactionAmount(), rhs.getTransactionAmount());

    return builder.build();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
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
