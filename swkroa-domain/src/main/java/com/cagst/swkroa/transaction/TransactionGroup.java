package com.cagst.swkroa.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.auto.value.AutoValue;

/**
 * Contains information about a grouping of transactions.
 *
 * @author Craig Gaskill
 */
@AutoValue
@JsonPropertyOrder({
    "transactionDate",
    "transactionCount",
    "transactionAmount"
})
public abstract class TransactionGroup implements Serializable {
  @JsonProperty(value = "transactionDate")
  public abstract LocalDate getTransactionDate();

  @JsonProperty(value = "transactionCount")
  public abstract long getTransactionCount();

  @JsonProperty(value = "transactionAmount")
  public abstract BigDecimal getTransactionAmount();

  public static Builder builder() {
    return new AutoValue_TransactionGroup.Builder();
  }

  @AutoValue.Builder
  interface Builder {
    @JsonProperty(value = "transactionDate")
    Builder setTransactionDate(LocalDate transactionDate);

    @JsonProperty(value = "transactionCount")
    Builder setTransactionCount(long transactionCount);

    @JsonProperty(value = "transactionAmount")
    Builder setTransactionAmount(BigDecimal amount);

    TransactionGroup build();
  }
}
