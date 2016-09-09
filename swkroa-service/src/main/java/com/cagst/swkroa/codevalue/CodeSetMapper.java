package com.cagst.swkroa.codevalue;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a row in the resultset into a {@link CodeSet} object. Used to unmarshall a CodeSet from the
 * database.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
/* package */class CodeSetMapper implements RowMapper<CodeSet> {
  private static final String CODESET_ID = "codeset_id";
  private static final String CODESET_DISPLAY = "codeset_display";
  private static final String CODESET_MEANING = "codeset_meaning";
  private static final String ACTIVE_IND = "active_ind";
  private static final String UPDT_CNT = "codeset_updt_cnt";

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
   */
  @Override
  public CodeSet mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    CodeSet codeset = new CodeSet();

    codeset.setCodeSetUID(rs.getLong(CODESET_ID));
    codeset.setDisplay(rs.getString(CODESET_DISPLAY));
    codeset.setMeaning(rs.getString(CODESET_MEANING));
    codeset.setActive(rs.getBoolean(ACTIVE_IND));
    codeset.setCodeSetUpdateCount(rs.getInt(UPDT_CNT));

    return codeset;
  }

}
