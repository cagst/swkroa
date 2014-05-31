package com.cagst.swkroa.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

/**
 * Maps a row in the resultset into a {@link JsonNode}.
 *
 * @author Craig Gaskill
 *
 * @version 1.0.0
 */
public class JsonNodeRowMapper implements RowMapper<JsonNode> {
	private static final ObjectMapper mapper = new ObjectMapper();

	@Override
	public JsonNode mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		ObjectNode node = mapper.createObjectNode();

		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnCnt = rsmd.getColumnCount();

		for (int idx = 1; idx <= columnCnt; idx++) {
			String column = JdbcUtils.lookupColumnName(rsmd, idx);
			Object value = resultSet.getObject(idx);

			if (value == null) {
				node.putNull(column);
			} else if (value instanceof Integer) {
				node.put(column, (Integer)value);
			} else if (value instanceof Long) {
				node.put(column, (Long)value);
			} else if (value instanceof Float) {
				node.put(column, (Float)value);
			} else if (value instanceof Double) {
				node.put(column, (Double) value);
			} else if (value instanceof BigInteger) {
				node.set(column, new BigIntegerNode((BigInteger)value));
			} else if (value instanceof BigDecimal) {
				node.put(column, (BigDecimal)value);
			} else if (value instanceof String) {
				node.put(column, (String)value);
			} else if (value instanceof Date) {
				node.put(column, ((Date)value).getTime());
			}
		}

		return node;
	}
}
