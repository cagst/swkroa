package com.cagst.swkroa.transaction;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Representation of an invoice Transaction that has not been fully paid.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public final class UnpaidInvoice extends Transaction implements Serializable {
  private BigDecimal transaction_amount;
  private BigDecimal amount_paid;
  private BigDecimal amount_remaining;

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
