package com.cagst.swkroa.transaction;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.math.BigDecimal;

import com.google.auto.value.AutoValue;
import org.joda.time.DateTime;

/**
 * Contains information about a grouping of transactions.
 *
 * @author Craig Gaskill
 */
@AutoValue
public abstract class TransactionGroup implements Serializable {
  static TransactionGroup create(final DateTime date,
                                 final long count,
                                 final BigDecimal amount) {
    return new AutoValue_TransactionGroup(date, count, amount);
  }

  public abstract DateTime getTransactionDate();

  public abstract long getTransactionCount();

  public abstract BigDecimal getTransactionAmount();
}
