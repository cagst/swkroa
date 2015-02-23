package com.cagst.swkroa.model;

import java.util.Set;

import org.joda.time.DateTime;

/**
 * A model that contains information needed to create a new billing run.
 *
 * @author Craig Gaskill
 */
public class BillingRunModel {
  private DateTime transactionDt;
  private String transactionDesc;
  private String transactionMemo;
  private Set<Long> membershipIds;

  public DateTime getTransactionDate() {
    return transactionDt;
  }

  public void setTransactionDate(final DateTime transactionDate) {
    this.transactionDt = transactionDate;
  }

  public String getTransactionDescription() {
    return transactionDesc;
  }

  public void setTransactionDescription(final String desc) {
    this.transactionDesc = desc;
  }

  public String getTransactionMemo() {
    return transactionMemo;
  }

  public void setTransactionMemo(final String memo) {
    this.transactionMemo = memo;
  }

  public Set<Long> getMembershipIds() {
    return membershipIds;
  }

  public void setMembershipIds(final Set<Long> ids) {
    this.membershipIds = ids;
  }
}
