package com.cagst.swkroa.model;

import java.time.LocalDate;
import java.util.Set;

/**
 * A model that contains information needed to create a new billing run.
 *
 * @author Craig Gaskill
 */
public class BillingRunModel {
  private LocalDate transactionDt;
  private String transactionDesc;
  private String transactionMemo;
  private Set<Long> membershipIds;

  public LocalDate getTransactionDate() {
    return transactionDt;
  }

  public void setTransactionDate(LocalDate transactionDate) {
    this.transactionDt = transactionDate;
  }

  public String getTransactionDescription() {
    return transactionDesc;
  }

  public void setTransactionDescription(String desc) {
    this.transactionDesc = desc;
  }

  public String getTransactionMemo() {
    return transactionMemo;
  }

  public void setTransactionMemo(String memo) {
    this.transactionMemo = memo;
  }

  public Set<Long> getMembershipIds() {
    return membershipIds;
  }

  public void setMembershipIds(Set<Long> ids) {
    this.membershipIds = ids;
  }
}
