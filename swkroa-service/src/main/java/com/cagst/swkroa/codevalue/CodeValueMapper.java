package com.cagst.swkroa.codevalue;

import com.cagst.common.codevalue.CGTCodeValue;
import com.cagst.swkroa.user.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a row in the resultset into a {@link CGTCodeValue} object. Used to unmarshall a CodeValue
 * from the database.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
/* package */class CodeValueMapper implements RowMapper<CodeValue> {
  private static final String CODESET_ID = "codeset_id";
  private static final String CODEVALUE_ID = "codevalue_id";
  private static final String CODEVALUE_DISPLAY = "codevalue_display";
  private static final String CODEVALUE_MEANING = "codevalue_meaning";

  // meta-data
  private static final String ACTIVE_IND = "active_ind";
  private static final String CREATE_ID = "create_id";
  private static final String UPDT_ID = "updt_id";
  private static final String CODEVALUE_UPDT_CNT = "codevalue_updt_cnt";

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
   */
  @Override
  public CodeValue mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    CodeValue codevalueBuilder = new CodeValue();
    codevalueBuilder.setCodeSetUID(rs.getLong(CODESET_ID));
    codevalueBuilder.setCodeValueUID(rs.getLong(CODEVALUE_ID));
    codevalueBuilder.setDisplay(rs.getString(CODEVALUE_DISPLAY));
    codevalueBuilder.setMeaning(rs.getString(CODEVALUE_MEANING));
    codevalueBuilder.setActive(rs.getBoolean(ACTIVE_IND));
    codevalueBuilder.setCodeValueUpdateCount(rs.getLong(CODEVALUE_UPDT_CNT));

    return codevalueBuilder;
  }

  /**
   * Will marshal a {@link CodeValue} into a {@link MapSqlParameterSource} for inserting into the
   * database.
   *
   * @param codeValue
   *     The {@link CodeValue} to map into an insert statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update}
   * statement.
   */
  public static MapSqlParameterSource mapInsertStatement(final CodeValue codeValue, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(CODESET_ID, codeValue.getCodeSetUID());
    params.addValue(CODEVALUE_DISPLAY, codeValue.getDisplay());
    params.addValue(CODEVALUE_MEANING, codeValue.getMeaning());
    params.addValue(ACTIVE_IND, codeValue.isActive());
    params.addValue(CREATE_ID, user.getUserUID());
    params.addValue(UPDT_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link CodeValue} into a {@link MapSqlParameterSource} for updating into the
   * database.
   *
   * @param codeValue
   *     The {@link CodeValue} to map into an update statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update}
   * statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(final CodeValue codeValue, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(CODEVALUE_DISPLAY, codeValue.getDisplay());
    params.addValue(CODEVALUE_MEANING, codeValue.getMeaning());
    params.addValue(ACTIVE_IND, codeValue.isActive());
    params.addValue(UPDT_ID, user.getUserUID());

    params.addValue(CODEVALUE_ID, codeValue.getCodeValueUID());
    params.addValue(CODEVALUE_UPDT_CNT, codeValue.getCodeValueUpdateCount());

    return params;
  }
}
