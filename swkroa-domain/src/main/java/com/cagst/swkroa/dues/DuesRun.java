package com.cagst.swkroa.dues;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.DateTime;

/**
 * Represents a Dues Run within the system.
 *
 * @author Craig Gaskill
 */
public final class DuesRun implements Serializable {
  private DateTime runDate;
  private String runDesc;
  private long runCount;
  private BigDecimal runTotal;

  public DateTime getRunDate() {
    return runDate;
  }

  public void setRunDate(final DateTime runDate) {
    this.runDate = runDate;
  }

  public String getRunDescription() {
    return runDesc;
  }

  public void setRunDescription(final String runDescription) {
    this.runDesc = runDescription;
  }

  public long getRunCount() {
    return runCount;
  }

  public void setRunCount(final long count) {
    this.runCount = count;
  }

  public BigDecimal getRunTotal() {
    return runTotal;
  }

  public void setRunTotal(final BigDecimal total) {
    this.runTotal = total;
  }
}
