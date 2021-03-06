package com.cagst.swkroa.transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.user.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * Maps a row in the resultset into a {@link TransactionEntry} object. Used to marshal / unmarshal a {@link Transaction}
 * to / from the database.
 *
 * @author Craig Gaskill
 */
/* package */ class TransactionEntryMapper implements RowMapper<TransactionEntry> {
  private static final String TRANSACTION_ENTRY_ID = "transaction_entry_id";
  private static final String TRANSACTION_ID = "transaction_id";
  private static final String RELATED_TRANSACTION_ID = "related_transaction_id";
  private static final String TRANSACTION_ENTRY_TYPE_CD = "transaction_entry_type_cd";
  private static final String TRANSACTION_ENTRY_AMOUNT = "transaction_entry_amount";

  // meta-data
  private static final String ACTIVE_IND = "active_ind";
  private static final String CREATE_ID = "create_id";
  private static final String UPDT_ID = "updt_id";
  private static final String TRANSACTION_ENTRY_UPDT_CNT = "transaction_entry_updt_cnt";

  private final CodeValueRepository codeValueRepo;

  /**
   * Primary Constructor used to create an instance of <i>TransactionEntryMapper</i>
   *
   * @param codeValueRepo
   *     The {@link CodeValueRepository} to use to retrieve codified values associated with the transaction entry.
   */
  public TransactionEntryMapper(final CodeValueRepository codeValueRepo) {
    this.codeValueRepo = codeValueRepo;
  }

  @Override
  public TransactionEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
    TransactionEntry entry = new TransactionEntry();
    entry.setTransactionEntryUID(rs.getLong(TRANSACTION_ENTRY_ID));

    entry.setTransactionEntryType(codeValueRepo.getCodeValueByUID(rs.getLong(TRANSACTION_ENTRY_TYPE_CD)));
    entry.setTransactionEntryAmount(rs.getBigDecimal(TRANSACTION_ENTRY_AMOUNT));
    entry.setRelatedTransactionUID(rs.getLong(RELATED_TRANSACTION_ID));

    entry.setActive(rs.getBoolean(ACTIVE_IND));
    entry.setTransactionEntryUpdateCount(rs.getLong(TRANSACTION_ENTRY_UPDT_CNT));

    return entry;
  }

  public static TransactionEntry mapRow(SqlRowSet rs,
                                        Transaction transaction,
                                        CodeValueRepository codeValueRepo) {

    TransactionEntry entry = new TransactionEntry();
    entry.setTransactionEntryUID(rs.getLong(TRANSACTION_ENTRY_ID));
    entry.setTransaction(transaction);
    entry.setTransactionEntryType(codeValueRepo.getCodeValueByUID(rs.getLong(TRANSACTION_ENTRY_TYPE_CD)));
    entry.setTransactionEntryAmount(rs.getBigDecimal(TRANSACTION_ENTRY_AMOUNT));
    entry.setRelatedTransactionUID(rs.getLong(RELATED_TRANSACTION_ID));

    entry.setActive(rs.getBoolean(ACTIVE_IND));
    entry.setTransactionEntryUpdateCount(rs.getLong(18));

    return entry;
  }

  /**
   * Will marshal a {@link TransactionEntry} into a {@link MapSqlParameterSource} for inserting into the database.
   *
   * @param entry
   *     The {@link TransactionEntry} to map into an insert statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapInsertStatement(TransactionEntry entry, User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    mapCommonParameters(params, entry, user);
    params.addValue(CREATE_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link TransactionEntry} into a {@link MapSqlParameterSource} for updating into the database.
   *
   * @param entry
   *     The {@link TransactionEntry} to map into an update statement.
   * @param user
   *     the {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(TransactionEntry entry, User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    mapCommonParameters(params, entry, user);
    params.addValue(TRANSACTION_ENTRY_ID, entry.getTransactionEntryUID());
    params.addValue(TRANSACTION_ENTRY_UPDT_CNT, entry.getTransactionEntryUpdateCount());

    return params;
  }

  private static void mapCommonParameters(MapSqlParameterSource params, TransactionEntry entry, User user) {
//    params.addValue(MEMBER_ID, entry.getMember() != null ? entry.getMember().getMemberUID() : null);
    params.addValue(TRANSACTION_ID, entry.getTransaction().getTransactionUID());
    params.addValue(TRANSACTION_ENTRY_TYPE_CD, entry.getTransactionEntryType().getCodeValueUID());
    params.addValue(TRANSACTION_ENTRY_AMOUNT, entry.getTransactionEntryAmount());
    params.addValue(ACTIVE_IND, entry.isActive());
    params.addValue(UPDT_ID, user.getUserUID());

    if (entry.getRelatedTransaction() != null) {
      params.addValue(RELATED_TRANSACTION_ID, entry.getRelatedTransaction().getTransactionUID());
    } else {
      params.addValue(RELATED_TRANSACTION_ID, entry.getRelatedTransactionUID() != 0L ? entry.getRelatedTransactionUID() : null);
    }
  }
}
