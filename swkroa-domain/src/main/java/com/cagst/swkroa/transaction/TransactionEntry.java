package com.cagst.swkroa.transaction;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Representation of an entry within a transaction defined within the system.
 *
 * @author Craig Gaskill
 */
public final class TransactionEntry implements Serializable {

  private long transaction_entry_id;
  private Transaction transaction;
  private long related_transaction_id;
  private Transaction related_transaction;
  private Member member;
  private BigDecimal transaction_entry_amount;
  private CodeValue transaction_entry_type;

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  public long getTransactionEntryUID() {
    return transaction_entry_id;
  }

  public void setTransactionEntryUID(final long uid) {
    this.transaction_entry_id = uid;
  }

  @JsonIgnore
  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(final Transaction transaction) {
    this.transaction = transaction;
  }

  public long getRelatedTransactionUID() {
    return related_transaction_id;
  }

  public void setRelatedTransactionUID(final long uid) {
    this.related_transaction_id = uid;
  }

  public Transaction getRelatedTransaction() {
    return related_transaction;
  }

  public void setRelatedTransaction(final Transaction trans) {
    this.related_transaction = trans;
  }

  public Member getMember() {
    return member;
  }

  public void setMember(final Member member) {
    this.member = member;
  }

  @DecimalMin(value = "0.01")
  public BigDecimal getTransactionEntryAmount() {
    return transaction_entry_amount;
  }

  public void setTransactionEntryAmount(final BigDecimal amount) {
    this.transaction_entry_amount = amount;
  }

  @NotNull
  public CodeValue getTransactionEntryType() {
    return transaction_entry_type;
  }

  public void setTransactionEntryType(final CodeValue type) {
    this.transaction_entry_type = type;
  }

  public boolean isActive() {
    return active_ind;
  }

  public void setActive(final boolean active) {
    this.active_ind = active;
  }

  public long getTransactionEntryUpdateCount() {
    return updt_cnt;
  }

  public void setTransactionEntryUpdateCount(final long updateCount) {
    this.updt_cnt = updateCount;
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("transaction_entry_type", transaction_entry_type);
    builder.append("transaction_entry_amount", transaction_entry_amount);

    return builder.build();
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(transaction);
    builder.append(transaction_entry_type);
    builder.append(transaction_entry_amount);

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
    if (!(obj instanceof TransactionEntry)) {
      return false;
    }

    TransactionEntry rhs = (TransactionEntry) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.append(transaction, rhs.getTransaction());
    builder.append(transaction_entry_type, rhs.getTransactionEntryType());
    builder.append(transaction_entry_amount, rhs.getTransactionEntryAmount());

    return builder.build();
  }
}
