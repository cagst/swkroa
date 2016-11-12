package com.cagst.swkroa.deposit;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cagst.common.util.CGTDateTimeUtils;
import com.cagst.swkroa.user.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

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
    deposit.setDepositUpdateCount(rs.getLong(DEPOSIT_UPDT_CNT));

    return deposit;
  }

  /**
   * Will marshal a {@link Deposit} into a {@link MapSqlParameterSource} for inserting into the database.
   *
   * @param deposit
   *    The {@link Deposit} to map into an insert statement.
   * @param user
   *    The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapInsertStatement(final Deposit deposit, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();

    params.addValue(DEPOSIT_DATE, CGTDateTimeUtils.convertDateTimeToTimestamp(deposit.getDepositDate()));
    params.addValue(DEPOSIT_REF, deposit.getDepositNumber());
    params.addValue(DEPOSIT_AMOUNT, deposit.getDepositAmount());
    params.addValue(ACTIVE_IND, deposit.isActive());
    params.addValue(UPDT_ID, user.getUserUID());
    params.addValue(CREATE_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link Deposit} into a {@link MapSqlParameterSource} for updating into the database.
   *
   * @param deposit
   *    The {@link Deposit} to map into an update statement.
   * @param user
   *    The {@link User} that performed the changes.
   *
   * @return An {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(final Deposit deposit, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();

    params.addValue(DEPOSIT_DATE, CGTDateTimeUtils.convertDateTimeToTimestamp(deposit.getDepositDate()));
    params.addValue(DEPOSIT_REF, deposit.getDepositNumber());
    params.addValue(DEPOSIT_AMOUNT, deposit.getDepositAmount());
    params.addValue(ACTIVE_IND, deposit.isActive());
    params.addValue(UPDT_ID, user.getUserUID());
    params.addValue(DEPOSIT_ID, deposit.getDepositUID());
    params.addValue(DEPOSIT_UPDT_CNT, deposit.getDepositUpdateCount());

    return params;
  }
}
