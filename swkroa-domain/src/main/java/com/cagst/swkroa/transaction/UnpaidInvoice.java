package com.cagst.swkroa.transaction;

import com.cagst.common.util.CGTCollatorBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Representation of an invoice Transaction that has not been fully paid.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public final class UnpaidInvoice implements Serializable, Comparable<UnpaidInvoice> {
  private long transaction_id;
  private long membership_id;
  private DateTime transaction_dt;
  private TransactionType transaction_type;
  private String transaction_desc;
  private String ref_num;
  private String memo_txt;
  private BigDecimal transaction_amount;
  private BigDecimal amount_paid;
  private BigDecimal amount_remaining;

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

  public BigDecimal getTransactionAmount() {
    return transaction_amount;
  }

  public void setTransactionAmount(final BigDecimal amount) {
    this.transaction_amount = amount;
  }

  public BigDecimal getAmountPaid() {
    return amount_paid;
  }

  public void setAmountPaid(final BigDecimal amount) {
    this.amount_paid = amount;
  }

  public BigDecimal getAmountRemaining() {
    return amount_remaining;
  }

  public void setAmountRemaining(final BigDecimal amount) {
    this.amount_remaining = amount;
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("transaction_dt", transaction_dt);
    builder.append("transaction_type", transaction_type);
    builder.append("transaction_amount", transaction_amount);
    builder.append("transaction_desc", transaction_desc);
    builder.append("ref_num", ref_num);
    builder.append("memo", memo_txt);

    return builder.build();
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(membership_id);
    builder.append(transaction_dt);
    builder.append(transaction_type);
    builder.append(transaction_amount);

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
    if (!(obj instanceof Transaction)) {
      return false;
    }

    UnpaidInvoice rhs = (UnpaidInvoice) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.append(membership_id, rhs.getMembershipUID());
    builder.append(transaction_dt, rhs.getTransactionDate());
    builder.append(transaction_type, rhs.getTransactionType());
    builder.append(transaction_amount, rhs.getTransactionAmount());

    return builder.build();
  }

  @Override
  public int compareTo(final UnpaidInvoice rhs) {
    if (rhs == null) {
      return 0;
    }

    CGTCollatorBuilder builder = new CGTCollatorBuilder();
    builder.append(transaction_dt, rhs.getTransactionDate());

    return builder.build();
  }
}
