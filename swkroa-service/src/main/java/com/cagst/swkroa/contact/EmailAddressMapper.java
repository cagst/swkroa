package com.cagst.swkroa.contact;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.user.User;

/**
 * Maps a row in the resultset into a {@link EmailAddress} object. Used to marshal / unmarshal a
 * {@link EmailAddress} to / from the database.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public class EmailAddressMapper implements RowMapper<EmailAddress> {
	private static final String EMAIL_ID = "email_id";
	private static final String PARENT_ENTITY_ID = "parent_entity_id";
	private static final String PARENT_ENTITY_NAME = "parent_entity_name";
	private static final String EMAIL_TYPE = "email_type_cd";
	private static final String EMAIL_ADDRESS = "email_address";
	private static final String ACTIVE_IND = "active_ind";
	private static final String CREATE_ID = "create_id";
	private static final String UPDT_ID = "updt_id";
	private static final String UPDT_CNT = "updt_cnt";

	private final CodeValueRepository codeValueRepo;

	/**
	 * Primary Constructor used to create an instance of <i>EmailAddressMapper</i> used to create
	 * {@link EmailAddress} from a resultset.
	 * 
	 * @param codeValueRepo
	 *          The {@link CodeValueRepository} to use to retrieve the email type for the
	 *          EmailAddress.
	 */
	public EmailAddressMapper(final CodeValueRepository codeValueRepo) {
		this.codeValueRepo = codeValueRepo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public EmailAddress mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		EmailAddress email = new EmailAddress();
		email.setEmailAddressUID(rs.getLong(EMAIL_ID));
		email.setParentEntityUID(rs.getLong(PARENT_ENTITY_ID));
		email.setParentEntityName(rs.getString(PARENT_ENTITY_NAME));
		email.setEmailType(codeValueRepo.getCodeValueByUID(rs.getLong(EMAIL_TYPE)));
		email.setEmailAddress(rs.getString(EMAIL_ADDRESS));
		email.setActive(rs.getBoolean(ACTIVE_IND));
		email.setEmailAddressUpdateCount(rs.getLong(UPDT_CNT));

		return email;
	}

	/**
	 * Will marshal a {@link EmailAddress} into a {@link MapSqlParameterSource} for inserting into the
	 * database.
	 * 
	 * @param emailAddress
	 *          The {@link EmailAddress} to map into an insert statement.
	 * @param user
	 *          The {@link User} that performed the changes.
	 * 
	 * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update}
	 *         statement.
	 */
	public static MapSqlParameterSource mapInsertStatement(final EmailAddress emailAddress, final User user) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(PARENT_ENTITY_ID, emailAddress.getParentEntityUID());
		params.addValue(PARENT_ENTITY_NAME, emailAddress.getParentEntityName());
		params.addValue(EMAIL_TYPE, emailAddress.getEmailType() != null ? emailAddress.getEmailType().getCodeValueUID()
				: null);
		params.addValue(EMAIL_ADDRESS, emailAddress.getEmailAddress());
		params.addValue(ACTIVE_IND, emailAddress.isActive());
		params.addValue(CREATE_ID, user.getUserUID());
		params.addValue(UPDT_ID, user.getUserUID());

		return params;
	}

	/**
	 * Will marshal a {@link EmailAddress} into a {@link MapSqlParameterSource} for updating into the
	 * database.
	 * 
	 * @param emailAddress
	 *          The {@link EmailAddress} to map into an update statement.
	 * @param user
	 *          The {@link User} that performed the changes.
	 * 
	 * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update}
	 *         statement.
	 */
	public static MapSqlParameterSource mapUpdateStatement(final EmailAddress emailAddress, final User user) {
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue(EMAIL_TYPE, emailAddress.getEmailType() != null ? emailAddress.getEmailType().getCodeValueUID()
				: null);
		params.addValue(EMAIL_ADDRESS, emailAddress.getEmailAddress());
		params.addValue(ACTIVE_IND, emailAddress.isActive());
		params.addValue(UPDT_ID, user.getUserUID());

		params.addValue(EMAIL_ID, emailAddress.getEmailAddressUID());
		params.addValue(UPDT_CNT, emailAddress.getEmailAddressUpdateCount());

		return params;
	}
}
