package com.cagst.swkroa.transaction;

import com.cagst.common.util.CGTDateTimeUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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
    return TransactionGroup.create(
        CGTDateTimeUtils.getDateTime(rs, TRANSACTION_DT),
        rs.getLong(TRANSACTION_COUNT),
        rs.getBigDecimal(TRANSACTION_AMOUNT)
        );
  }
}
