package com.cagst.swkroa.transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cagst.swkroa.util.DateTimeConverter;
import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a row in the resultset into a {@link TransactionGroup} object. Used to unmarshal a {@link TransactionGroup} from the database.
 *
 * @author Craig Gaskill
 */
/* package */ class TransactionGroupMapper implements RowMapper<TransactionGroup> {
  private static final String TRANSACTION_DT     = "transaction_dt";
  private static final String TRANSACTION_COUNT  = "transaction_count";
  private static final String TRANSACTION_AMOUNT = "transaction_amount";

  @Override
  public TransactionGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
    return TransactionGroup.builder()
        .setTransactionDate(DateTimeConverter.convert(rs.getTimestamp(TRANSACTION_DT)))
        .setTransactionAmount(rs.getBigDecimal(TRANSACTION_AMOUNT))
        .setTransactionCount(rs.getLong(TRANSACTION_COUNT))
        .build();
  }
}
