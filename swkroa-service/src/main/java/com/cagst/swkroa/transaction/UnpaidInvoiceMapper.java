package com.cagst.swkroa.transaction;

import com.cagst.common.util.CGTDateTimeUtils;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a row in the resultset into an {@link UnpaidInvoice} object. Used to marshal / unmarshal an {@link UnpaidInvoice} to /
 * from the database.
 *
 * @author Craig Gaskill
 */
/* package */ final class UnpaidInvoiceMapper implements RowMapper<UnpaidInvoice> {
  private static final String MEMBERSHIP_NAME    = "membership_name";
  private static final String MEMBERSHIP_ID      = "membership_id";
  private static final String TRANSACTION_ID     = "transaction_id";
  private static final String TRANSACTION_DT     = "transaction_dt";
  private static final String TRANSACTION_TYPE   = "transaction_type_flag";
  private static final String TRANSACTION_DESC   = "transaction_desc";
  private static final String REF_NUM            = "ref_num";
  private static final String TRANSACTION_AMOUNT = "transaction_amount";
  private static final String AMOUNT_PAID        = "amount_paid";

  @Override
  public UnpaidInvoice mapRow(ResultSet rs, int rowNum) throws SQLException {
    UnpaidInvoice inv = new UnpaidInvoice();

    BigDecimal transactionAmount = rs.getBigDecimal(TRANSACTION_AMOUNT).abs();
    BigDecimal paidAmount        = rs.getBigDecimal(AMOUNT_PAID).abs();
    int transactionTypeFlag      = rs.getInt(TRANSACTION_TYPE);

    inv.setMembershipName(rs.getString(MEMBERSHIP_NAME));
    inv.setMembershipUID(rs.getLong(MEMBERSHIP_ID));
    inv.setTransactionUID(rs.getLong(TRANSACTION_ID));
    inv.setTransactionDate(CGTDateTimeUtils.getDateTime(rs.getTimestamp(TRANSACTION_DT)));
    inv.setTransactionType(TransactionType.values()[transactionTypeFlag]);
    inv.setTransactionDescription(rs.getString(TRANSACTION_DESC));
    inv.setReferenceNumber(rs.getString(REF_NUM));
    inv.setTransactionAmount(transactionAmount);
    inv.setAmountPaid(paidAmount);
    inv.setAmountRemaining(transactionAmount.subtract(paidAmount));

    return inv;
  }
}
