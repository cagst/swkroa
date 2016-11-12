package com.cagst.swkroa.codevalue;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Maps a row in the resultset into a {@link CodeSet} object. Used to unmarshall a CodeSet from the
 * database.
 *
 * @author Craig Gaskill
 */
/* package */class CodeSetMapper implements RowMapper<CodeSet> {
  private static final String CODESET_ID = "codeset_id";
  private static final String CODESET_DISPLAY = "codeset_display";
  private static final String CODESET_MEANING = "codeset_meaning";
  private static final String ACTIVE_IND = "active_ind";
  private static final String UPDT_CNT = "codeset_updt_cnt";

  @Override
  public CodeSet mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    return CodeSet.builder()
        .setCodeSetUID(rs.getLong(CODESET_ID))
        .setDisplay(rs.getString(CODESET_DISPLAY))
        .setMeaning(rs.getString(CODESET_MEANING))
        .setActive(rs.getBoolean(ACTIVE_IND))
        .setCodeSetUpdateCount(rs.getInt(UPDT_CNT))
        .build();
  }

}
