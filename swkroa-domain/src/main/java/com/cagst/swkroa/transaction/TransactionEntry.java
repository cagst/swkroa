package com.cagst.swkroa.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.cagst.swkroa.codevalue.CodeValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

  public BigDecimal getTransactionEntryAmount() {
    return transaction_entry_amount;
  }

  public void setTransactionEntryAmount(final BigDecimal amount) {
    this.transaction_entry_amount = amount;
  }

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
    return Objects.hash(transaction, transaction_entry_type, transaction_entry_amount);
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

    return Objects.equals(transaction, rhs.getTransaction()) &&
        Objects.equals(transaction_entry_type, rhs.getTransactionEntryType()) &&
        Objects.equals(transaction_entry_amount, rhs.getTransactionEntryAmount());
  }
}
