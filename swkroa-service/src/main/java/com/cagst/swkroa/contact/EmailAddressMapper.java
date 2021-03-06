package com.cagst.swkroa.contact;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cagst.swkroa.user.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Maps a row in the resultset into a {@link EmailAddress} object. Used to marshal / unmarshal a
 * {@link EmailAddress} to / from the database.
 *
 * @author Craig Gaskill
 */
public class EmailAddressMapper implements RowMapper<EmailAddress> {
  private static final String EMAIL_ID = "email_id";
  private static final String PARENT_ENTITY_ID = "parent_entity_id";
  private static final String PARENT_ENTITY_NAME = "parent_entity_name";
  private static final String EMAIL_TYPE = "email_type_cd";
  private static final String EMAIL_ADDRESS = "email_address";
  private static final String PRIMARY_IND = "primary_ind";
  private static final String ACTIVE_IND = "active_ind";
  private static final String CREATE_ID = "create_id";
  private static final String UPDT_ID = "updt_id";
  private static final String UPDT_CNT = "updt_cnt";

  @Override
  public EmailAddress mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    return EmailAddress.builder()
        .setEmailAddressUID(rs.getLong(EMAIL_ID))
        .setParentEntityUID(rs.getLong(PARENT_ENTITY_ID))
        .setParentEntityName(rs.getString(PARENT_ENTITY_NAME))
        .setEmailTypeCD(rs.getLong(EMAIL_TYPE))
        .setEmailAddress(rs.getString(EMAIL_ADDRESS))
        .setPrimary(rs.getBoolean(PRIMARY_IND))
        .setActive(rs.getBoolean(ACTIVE_IND))
        .setEmailAddressUpdateCount(rs.getLong(UPDT_CNT))
        .build();
  }

  /**
   * Will marshal a {@link EmailAddress} into a {@link MapSqlParameterSource} for inserting into the
   * database.
   *
   * @param emailAddress
   *     The {@link EmailAddress} to map into an insert statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update}
   * statement.
   */
  public static MapSqlParameterSource mapInsertStatement(final EmailAddress emailAddress, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(PARENT_ENTITY_ID, emailAddress.getParentEntityUID());
    params.addValue(PARENT_ENTITY_NAME, emailAddress.getParentEntityName());
    params.addValue(EMAIL_TYPE, emailAddress.getEmailTypeCD() != 0L ? emailAddress.getEmailTypeCD() : null);
    params.addValue(EMAIL_ADDRESS, emailAddress.getEmailAddress());
    params.addValue(PRIMARY_IND, emailAddress.isPrimary());
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
   *     The {@link EmailAddress} to map into an update statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update}
   * statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(final EmailAddress emailAddress, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();

    params.addValue(EMAIL_TYPE, emailAddress.getEmailTypeCD() != 0L ? emailAddress.getEmailTypeCD() : null);
    params.addValue(EMAIL_ADDRESS, emailAddress.getEmailAddress());
    params.addValue(PRIMARY_IND, emailAddress.isPrimary());
    params.addValue(ACTIVE_IND, emailAddress.isActive());
    params.addValue(UPDT_ID, user.getUserUID());

    params.addValue(EMAIL_ID, emailAddress.getEmailAddressUID());
    params.addValue(UPDT_CNT, emailAddress.getEmailAddressUpdateCount());

    return params;
  }
}
