package com.cagst.swkroa.deposit;

import com.cagst.swkroa.transaction.Transaction;

/**
 * Representation of a {@link Transaction} that is associated with a {@link Deposit}.
 *
 * @author Craig Gaskill
 */
public final class DepositTransaction extends Transaction {
  private long deposit_transaction_id;
  private long deposit_id;
  private boolean active_ind = true;

  public long getDepositTransactionUID() {
    return deposit_transaction_id;
  }

  public void setDepositTransactionUID(long uid) {
    this.deposit_transaction_id = uid;
  }

  public long getDepositUID() {
    return deposit_id;
  }

  public void setDepositUID(long uid) {
    this.deposit_id = uid;
  }

  public boolean isDepositTransactionActive() {
    return active_ind;
  }

  public void setDepositTransactionActive(boolean active) {
    this.active_ind = active;
  }
}
