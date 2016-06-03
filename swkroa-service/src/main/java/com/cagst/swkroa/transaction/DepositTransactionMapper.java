package com.cagst.swkroa.transaction;

import com.cagst.common.util.CGTDateTimeUtils;
import com.cagst.swkroa.deposit.DepositTransaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a row in the resultset into a {@link DepositTransaction} object. Used to marshal / unmarshal a {@link Transaction} to /
 * from the database.
 *
 * @author Craig Gaskill
 */
/* package */final class DepositTransactionMapper implements RowMapper<DepositTransaction> {
  private static final String DEPOSIT_TRANSACTION_ID = "deposit_transaction_id";
  private static final String DEPOSIT_ID             = "deposit_id";
  private static final String TRANSACTION_ID         = "transaction_id";

  private static final String MEMBERSHIP_ID         = "membership_id";
  private static final String TRANSACTION_DT        = "transaction_dt";
  private static final String TRANSACTION_TYPE_FLAG = "transaction_type_flag";
  private static final String TRANSACTION_DESC      = "transaction_desc";
  private static final String REF_NUM               = "ref_num";
  private static final String MEMO_TXT              = "memo_txt";

  // meta-data
  private static final String TRANSACTION_ACTIVE_IND         = "active_ind";
  private static final String DEPOSIT_TRANSACTION_ACTIVE_IND = "deposit_transaction_active_ind";
  private static final String CREATE_ID                      = "create_id";
  private static final String UPDT_ID                        = "updt_id";
  private static final String TRANSACTION_UPDT_CNT           = "transaction_updt_cnt";

  @Override
  public DepositTransaction mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    DepositTransaction trans = new DepositTransaction();
    trans.setDepositTransactionUID(rs.getLong(DEPOSIT_TRANSACTION_ID));
    trans.setDepositUID(rs.getLong(DEPOSIT_ID));
    trans.setTransactionUID(rs.getLong(TRANSACTION_ID));
    trans.setMembershipUID(rs.getLong(MEMBERSHIP_ID));
    trans.setTransactionDate(CGTDateTimeUtils.getDateTime(rs, TRANSACTION_DT));
    trans.setTransactionType(TransactionType.values()[rs.getInt(TRANSACTION_TYPE_FLAG)]);
    trans.setTransactionDescription(rs.getString(TRANSACTION_DESC));
    trans.setReferenceNumber(rs.getString(REF_NUM));
    trans.setMemo(rs.getString(MEMO_TXT));

    trans.setActive(rs.getBoolean(TRANSACTION_ACTIVE_IND));
    trans.setDepositTransactionActive(rs.getBoolean(DEPOSIT_TRANSACTION_ACTIVE_IND));
    trans.setTransactionUpdateCount(rs.getLong(TRANSACTION_UPDT_CNT));

    return trans;
  }
}
