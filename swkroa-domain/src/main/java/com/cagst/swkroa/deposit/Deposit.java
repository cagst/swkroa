package com.cagst.swkroa.deposit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents a Deposit within the system.
 *
 * @author Craig Gaskill
 */
public final class Deposit implements Serializable {
  private long deposit_id;
  private LocalDate deposit_dt;
  private String deposit_ref;
  private BigDecimal deposit_amount;

  private List<DepositTransaction> transactions = new ArrayList<>();

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  public long getDepositUID() {
    return deposit_id;
  }

  public void setDepositUID(final long uid) {
    this.deposit_id = uid;
  }

  public LocalDate getDepositDate() {
    return deposit_dt;
  }

  public void setDepositDate(final LocalDate dt) {
    this.deposit_dt = dt;
  }

  public String getDepositNumber() {
    return deposit_ref;
  }

  public void setDepositNumber(final String ref) {
    this.deposit_ref = ref;
  }

  public BigDecimal getDepositAmount() {
    return deposit_amount;
  }

  public void setDepositAmount(final BigDecimal amount) {
    this.deposit_amount = amount;
  }

  public boolean isActive() {
    return active_ind;
  }

  public void setActive(boolean active) {
    this.active_ind = active;
  }

  public void clearTransactions() {
    transactions.clear();
  }

  public void addTransaction(final DepositTransaction transaction) {
    this.transactions.add(transaction);
  }

  public void removeTransaction(final DepositTransaction transaction) {
    this.transactions.remove(transaction);
  }

  public List<DepositTransaction> getTransactions() {
    return Collections.unmodifiableList(transactions);
  }

  public void setTransactions(final List<DepositTransaction> transactions) {
    this.transactions = new ArrayList<>(transactions);
  }

  public long getDepositUpdateCount() {
    return updt_cnt;
  }

  public void setDepositUpdateCount(final long updateCount) {
    this.updt_cnt = updateCount;
  }

  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append(deposit_dt);
    builder.append(deposit_ref);
    builder.append(deposit_amount);

    return builder.build();
  }
}
