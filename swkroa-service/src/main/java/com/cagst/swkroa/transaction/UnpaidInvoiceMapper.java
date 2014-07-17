package com.cagst.swkroa.transaction;

import com.cagst.common.util.CGTDateTimeUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a row in the resultset into an {@link UnpaidInvoice} object. Used to marshal / unmarshal an {@link UnpaidInvoice} to /
 * from the database.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
/* package */ final class UnpaidInvoiceMapper implements RowMapper<UnpaidInvoice> {
  private static final String TRANSACTION_ID        = "transaction_id";
  private static final String MEMBERSHIP_ID         = "membership_id";
  private static final String TRANSACTION_DT        = "transaction_dt";
  private static final String TRANSACTION_TYPE_FLAG = "transaction_type_flag";
  private static final String TRANSACTION_DESC      = "transaction_desc";
  private static final String REF_NUM               = "ref_num";
  private static final String MEMO_TXT              = "memo_txt";
  private static final String TRANSACTION_AMOUNT    = "transaction_amount";
  private static final String AMOUNT_PAID           = "amount_paid";
  private static final String AMOUNT_REMAINING      = "amount_remaining";

  @Override
  public UnpaidInvoice mapRow(ResultSet rs, int rowNum) throws SQLException {
    UnpaidInvoice inv = new UnpaidInvoice();

    inv.setTransactionUID(rs.getLong(TRANSACTION_ID));
    inv.setMembershipUID(rs.getLong(MEMBERSHIP_ID));
    inv.setTransactionDate(CGTDateTimeUtils.getDateTime(rs.getTimestamp(TRANSACTION_DT)));
    inv.setTransactionType(TransactionType.values()[rs.getInt(TRANSACTION_TYPE_FLAG)]);
    inv.setTransactionDescription(rs.getString(TRANSACTION_DESC));
    inv.setReferenceNumber(rs.getString(REF_NUM));
    inv.setMemo(rs.getString(MEMO_TXT));
    inv.setTransactionAmount(rs.getBigDecimal(TRANSACTION_AMOUNT));
    inv.setAmountPaid(rs.getBigDecimal(AMOUNT_PAID));
    inv.setAmountRemaining(rs.getBigDecimal(AMOUNT_REMAINING));

    return inv;
  }
}
