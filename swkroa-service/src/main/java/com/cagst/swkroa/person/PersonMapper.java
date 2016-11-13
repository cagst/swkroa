package com.cagst.swkroa.person;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Used to marshal/unmarshal a {@link Person} to/from the database.
 *
 * @author Craig Gaskill
 */
public final class PersonMapper extends BasePersonMapper implements RowMapper<Person> {
  @Override
  public Person mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    return super.mapRow(rs, rowNum);
  }
}
