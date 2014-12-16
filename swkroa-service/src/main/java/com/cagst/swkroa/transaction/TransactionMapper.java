package com.cagst.swkroa.transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cagst.common.util.CGTDateTimeUtils;
import com.cagst.swkroa.user.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * Maps a row in the resultset into a {@link Transaction} object. Used to marshal / unmarshal a {@link Transaction} to /
 * from the database.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
/* package */final class TransactionMapper implements RowMapper<Transaction> {
  /* package */ static final String TRANSACTION_ID = "transaction_id";

  private static final String MEMBERSHIP_ID = "membership_id";
  private static final String TRANSACTION_DT = "transaction_dt";
  private static final String TRANSACTION_TYPE_FLAG = "transaction_type_flag";
  private static final String TRANSACTION_DESC = "transaction_desc";
  private static final String REF_NUM = "ref_num";
  private static final String MEMO_TXT = "memo_txt";

  // meta-data
  private static final String ACTIVE_IND = "active_ind";
  private static final String CREATE_ID = "create_id";
  private static final String UPDT_ID = "updt_id";
  private static final String TRANSACTION_UPDT_CNT = "transaction_updt_cnt";

  @Override
  public Transaction mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    Transaction trans = new Transaction();
    trans.setTransactionUID(rs.getLong(TRANSACTION_ID));
    trans.setMembershipUID(rs.getLong(MEMBERSHIP_ID));
    trans.setTransactionDate(CGTDateTimeUtils.getDateTime(rs, TRANSACTION_DT));
    trans.setTransactionType(TransactionType.values()[rs.getInt(TRANSACTION_TYPE_FLAG)]);
    trans.setTransactionDescription(rs.getString(TRANSACTION_DESC));
    trans.setReferenceNumber(rs.getString(REF_NUM));
    trans.setMemo(rs.getString(MEMO_TXT));

    trans.setActive(rs.getBoolean(ACTIVE_IND));
    trans.setTransactionUpdateCount(rs.getLong(TRANSACTION_UPDT_CNT));

    return trans;
  }

  public Transaction mapRow(final SqlRowSet rs) throws SQLException {
    Transaction trans = new Transaction();
    trans.setTransactionUID(rs.getLong(TRANSACTION_ID));
    trans.setMembershipUID(rs.getLong(MEMBERSHIP_ID));
    trans.setTransactionDate(CGTDateTimeUtils.getDateTime(rs.getTimestamp(TRANSACTION_DT)));
    trans.setTransactionType(TransactionType.values()[rs.getInt(TRANSACTION_TYPE_FLAG)]);
    trans.setTransactionDescription(rs.getString(TRANSACTION_DESC));
    trans.setReferenceNumber(rs.getString(REF_NUM));
    trans.setMemo(rs.getString(MEMO_TXT));

    trans.setActive(rs.getBoolean(ACTIVE_IND));
    trans.setTransactionUpdateCount(rs.getLong(11));

    return trans;
  }

  /**
   * Will marshal a {@link Transaction} into a {@link MapSqlParameterSource} for inserting into the database.
   *
   * @param transaction
   *     The {@link Transaction} to map into an insert statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapInsertStatement(final Transaction transaction, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();

    mapCommonProperties(params, transaction, user);
    params.addValue(CREATE_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link Transaction} into a {@link MapSqlParameterSource} for updating into the database.
   *
   * @param transaction
   *     The {@link Transaction} to map into an update statement.
   * @param user
   *     the {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(final Transaction transaction, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();

    mapCommonProperties(params, transaction, user);
    params.addValue(TRANSACTION_ID, transaction.getTransactionUID());
    params.addValue(TRANSACTION_UPDT_CNT, transaction.getTransactionUpdateCount());

    return params;
  }

  private static void mapCommonProperties(final MapSqlParameterSource params, final Transaction transaction,
                                          final User user) {

    params.addValue(MEMBERSHIP_ID, transaction.getMembershipUID());
    params.addValue(TRANSACTION_DT, CGTDateTimeUtils.convertDateTimeToTimestamp(transaction.getTransactionDate()));
    params.addValue(TRANSACTION_TYPE_FLAG, transaction.getTransactionType().ordinal());
    params.addValue(TRANSACTION_DESC, transaction.getTransactionDescription());
    params.addValue(REF_NUM, transaction.getReferenceNumber());
    params.addValue(MEMO_TXT, transaction.getMemo());
    params.addValue(ACTIVE_IND, transaction.isActive());
    params.addValue(UPDT_ID, user.getUserUID());
  }
}
