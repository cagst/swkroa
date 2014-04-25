package com.cagst.swkroa.person;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.cagst.swkroa.codevalue.CodeValueRepository;

/**
 * Used to marshal/unmarshal a {@link Person} to/from the database.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public final class PersonMapper extends BasePersonMapper implements RowMapper<Person> {
	/**
	 * Primary Constructor used to create an instance of <i>PersonMapper</i>.
	 * 
	 * @param codeValueRepo
	 *          The {@link CodeValueRepository} to use to retrieve additional attributes.
	 */
	public PersonMapper(final CodeValueRepository codeValueRepo) {
		super(codeValueRepo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public Person mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		return super.mapRow(rs, rowNum);
	}
}
