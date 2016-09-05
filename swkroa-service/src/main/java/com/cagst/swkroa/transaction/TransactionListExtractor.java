package com.cagst.swkroa.transaction;

import com.cagst.swkroa.codevalue.CodeValueRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionListExtractor implements ResultSetExtractor<List<Transaction>> {
  private final TransactionMapper transactionMapper;
  private final TransactionEntryMapper entryMapper;

  public TransactionListExtractor(final CodeValueRepository codeValueRepo) {
    transactionMapper = new TransactionMapper();
    entryMapper = new TransactionEntryMapper(codeValueRepo);
  }

  @Override
  public List<Transaction> extractData(ResultSet rs) throws SQLException, DataAccessException {
    Map<Long, Transaction> transactions = new HashMap<>();

    while (rs.next()) {
      long transactionId = rs.getLong(TransactionMapper.TRANSACTION_ID);
      if (transactions.containsKey(transactionId)) {
        addChild(transactions.get(transactionId), entryMapper.mapRow(rs, rs.getRow()));
      } else {
        Transaction trans = transactionMapper.mapRow(rs, rs.getRow());
        transactions.put(transactionId, trans);

        addChild(trans, entryMapper.mapRow(rs, rs.getRow()));
      }
    }

    return new ArrayList<>(transactions.values());
  }

  private void addChild(final Transaction trans, final TransactionEntry entry) {
    trans.addEntry(entry);
    entry.setTransaction(trans);
  }
}
