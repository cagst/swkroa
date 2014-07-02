package com.cagst.swkroa.transaction;

import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.member.MemberRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransactionListExtractor implements ResultSetExtractor {
	private final TransactionMapper transactionMapper;
	private final TransactionEntryMapper entryMapper;

	public TransactionListExtractor(final CodeValueRepository codeValueRepo,
																	final MemberRepository memberRepo) {

		transactionMapper = new TransactionMapper();
		entryMapper = new TransactionEntryMapper(codeValueRepo, memberRepo);
	}

	@Override
	public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<Long, Transaction> transactions = new HashMap<Long, Transaction>();

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

		return new ArrayList<Transaction>(transactions.values());
	}

	private void addChild(final Transaction trans, final TransactionEntry entry) {
		trans.addEntry(entry);
		entry.setTransaction(trans);
	}
}
