package com.cagst.swkroa.deposit;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cagst.common.util.CGTDateTimeUtils;
import org.springframework.jdbc.core.RowMapper;

/**
 * Used to marshal/unmarshal a {@link Deposit} to/from the database.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
/* package */ class DepositMapper implements RowMapper<Deposit> {
  private static final String DEPOSIT_ID     = "deposit_id";
  private static final String DEPOSIT_DATE   = "deposit_dt";
  private static final String DEPOSIT_REF    = "deposit_ref";
  private static final String DEPOSIT_AMOUNT = "deposit_amount";

  // meta-data
  private static final String ACTIVE_IND = "active_ind";
  private static final String CREATE_ID  = "create_id";
  private static final String UPDT_ID    = "updt_id";
  private static final String DEPOSIT_UPDT_CNT = "deposit_updt_cnt";

  @Override
  public Deposit mapRow(ResultSet rs, int rowNum) throws SQLException {
    Deposit deposit = new Deposit();
    deposit.setDepositUID(rs.getLong(DEPOSIT_ID));
    deposit.setDepositDate(CGTDateTimeUtils.getDateTime(rs, DEPOSIT_DATE));
    deposit.setDepositNumber(rs.getString(DEPOSIT_REF));
    deposit.setDepositAmount(rs.getBigDecimal(DEPOSIT_AMOUNT));
    deposit.setActive(rs.getBoolean(ACTIVE_IND));

    return deposit;
  }
}
