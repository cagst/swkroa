package com.cagst.swkroa.billing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import com.cagst.swkroa.transaction.Transaction;
import org.joda.time.LocalDate;

/**
 * Represents a Billing Run within the system.
 *
 * @author Craig Gaskill
 */
public final class BillingRun implements Serializable {
  private LocalDate runDate;
  private String runDesc;
  private BigDecimal runTotal;
  private List<Transaction> transactions;

  public LocalDate getRunDate() {
    return runDate;
  }

  public String getRunDescription() {
    return runDesc;
  }

  public void setRunDescription(final String runDescription) {
    this.runDesc = runDescription;
  }

  public void setRunDate(final LocalDate runDate) {
    this.runDate = runDate;
  }

  public BigDecimal getRunTotal() {
    return runTotal;
  }

  public void setRunTotal(final BigDecimal total) {
    this.runTotal = total;
  }

  public List<Transaction> getTransactions() {
    return Collections.unmodifiableList(transactions);
  }

  public void addTransaction(final Transaction transaction) {
    this.transactions.add(transaction);
  }

  public void removeTransaction(final Transaction transaction) {
    this.transactions.remove(transaction);
  }

  public void clearTransactions() {
    this.transactions.clear();
  }
}
