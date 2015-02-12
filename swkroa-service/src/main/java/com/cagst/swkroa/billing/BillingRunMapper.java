package com.cagst.swkroa.billing;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cagst.common.util.CGTDateTimeUtils;
import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a row in the resultset into a {@link BillingRun} object. Used to marshal / unmarshal a {@link BillingRun}
 * to / from the database.
 *
 * @author Craig Gaskill
 */
public class BillingRunMapper implements RowMapper<BillingRun> {
  private static final String TRANSACTION_DT    = "transaction_dt";
  private static final String TRANSACTION_DESC  = "transaction_desc";
  private static final String TRANSACTION_COUNT = "transaction_count";
  private static final String TRANSACTION_TOTAL = "transaction_total";

  @Override
  public BillingRun mapRow(ResultSet rs, int rowNum) throws SQLException {
    BillingRun run = new BillingRun();

    run.setRunDate(CGTDateTimeUtils.getDateTime(rs, TRANSACTION_DT));
    run.setRunDescription(rs.getString(TRANSACTION_DESC));
    run.setRunCount(rs.getLong(TRANSACTION_COUNT));
    run.setRunTotal(rs.getBigDecimal(TRANSACTION_TOTAL).negate());

    return run;
  }
}
