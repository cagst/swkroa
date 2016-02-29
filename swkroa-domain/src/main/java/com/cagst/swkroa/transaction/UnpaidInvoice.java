package com.cagst.swkroa.transaction;

import java.math.BigDecimal;

/**
 * Representation of an invoice Transaction that has not been fully paid.
 *
 * @author Craig Gaskill
 */
public final class UnpaidInvoice extends Transaction {
  private String membership_name;
  private BigDecimal transaction_amount;
  private BigDecimal amount_paid;
  private BigDecimal amount_remaining;

  public String getMembershipName() {
    return membership_name;
  }

  public void setMembershipName(final String name) {
    this.membership_name = name;
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
}
