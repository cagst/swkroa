package com.cagst.swkroa.transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.deposit.DepositTransaction;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class DepositTransactionListExtractor implements ResultSetExtractor<List<DepositTransaction>> {
  private final DepositTransactionMapper depositTransactionMapper;
  private final TransactionEntryMapper entryMapper;

  public DepositTransactionListExtractor(final CodeValueRepository codeValueRepo) {
    depositTransactionMapper = new DepositTransactionMapper();
    entryMapper = new TransactionEntryMapper(codeValueRepo);
  }

  @Override
  public List<DepositTransaction> extractData(ResultSet rs) throws SQLException, DataAccessException {
    Map<Long, DepositTransaction> transactions = new HashMap<>();

    while (rs.next()) {
      long transactionId = rs.getLong(TransactionMapper.TRANSACTION_ID);
      if (transactions.containsKey(transactionId)) {
        addChild(transactions.get(transactionId), entryMapper.mapRow(rs, rs.getRow()));
      } else {
        DepositTransaction tx = depositTransactionMapper.mapRow(rs, rs.getRow());
        transactions.put(transactionId, tx);

        addChild(tx, entryMapper.mapRow(rs, rs.getRow()));
      }
    }

    return new ArrayList<>(transactions.values());
  }

  private void addChild(final Transaction trans, final TransactionEntry entry) {
    trans.addEntry(entry);
    entry.setTransaction(trans);
  }
}
