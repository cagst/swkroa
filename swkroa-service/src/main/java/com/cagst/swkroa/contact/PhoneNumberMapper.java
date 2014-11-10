package com.cagst.swkroa.contact;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cagst.swkroa.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Maps a row in the resultset into a {@link PhoneNumber} object. Used to marsahl / unmarshal a
 * {@link PhoneNumber} to / from the database.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public class PhoneNumberMapper implements RowMapper<PhoneNumber> {
  private static final String PHONE_ID = "phone_id";
  private static final String PARENT_ENTITY_ID = "parent_entity_id";
  private static final String PARENT_ENTITY_NAME = "parent_entity_name";
  private static final String PHONE_TYPE = "phone_type_cd";
  private static final String PHONE_NUMBER = "phone_number";
  private static final String PHONE_EXTENSION = "phone_extension";
  private static final String PRIMARY_IND = "primary_ind";
  private static final String ACTIVE_IND = "active_ind";
  private static final String CREATE_ID = "create_id";
  private static final String UPDT_ID = "updt_id";
  private static final String UPDT_CNT = "updt_cnt";

  @Override
  public PhoneNumber mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    PhoneNumber phone = new PhoneNumber();
    phone.setPhoneUID(rs.getLong(PHONE_ID));
    phone.setParentEntityUID(rs.getLong(PARENT_ENTITY_ID));
    phone.setParentEntityName(rs.getString(PARENT_ENTITY_NAME));
    phone.setPhoneTypeCD(rs.getLong(PHONE_TYPE));
    phone.setPhoneNumber(rs.getString(PHONE_NUMBER));
    phone.setPhoneExtension(rs.getString(PHONE_EXTENSION));
    phone.setPrimary(rs.getBoolean(PRIMARY_IND));
    phone.setActive(rs.getBoolean(ACTIVE_IND));
    phone.setPhoneUpdateCount(rs.getLong(UPDT_CNT));

    return phone;
  }

  /**
   * Will marshal a {@link PhoneNumber} into a {@link MapSqlParameterSource} for inserting into the
   * database.
   *
   * @param phoneNumber
   *     The {@link PhoneNumber} to map into an insert statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update}
   * statement.
   */
  public static MapSqlParameterSource mapInsertStatement(final PhoneNumber phoneNumber, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(PARENT_ENTITY_ID, phoneNumber.getParentEntityUID());
    params.addValue(PARENT_ENTITY_NAME, phoneNumber.getParentEntityName());
    params.addValue(PHONE_TYPE, phoneNumber.getPhoneTypeCD() != 0L ? phoneNumber.getPhoneTypeCD() : null);
    params.addValue(PHONE_NUMBER, phoneNumber.getPhoneNumber());
    params.addValue(PHONE_EXTENSION,
        StringUtils.isNotBlank(phoneNumber.getPhoneExtension()) ? phoneNumber.getPhoneExtension() : null);
    params.addValue(PRIMARY_IND, phoneNumber.isPrimary());
    params.addValue(ACTIVE_IND, phoneNumber.isActive());
    params.addValue(CREATE_ID, user.getUserUID());
    params.addValue(UPDT_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link PhoneNumber} into a {@link MapSqlParameterSource} for updating into the
   * database.
   *
   * @param phoneNumber
   *     The {@link PhoneNumber} to map into an update statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update}
   * statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(final PhoneNumber phoneNumber, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(PHONE_TYPE, phoneNumber.getPhoneTypeCD() != 0L ? phoneNumber.getPhoneTypeCD() : null);
    params.addValue(PHONE_NUMBER, phoneNumber.getPhoneNumber());
    params.addValue(PHONE_EXTENSION,
        StringUtils.isNotBlank(phoneNumber.getPhoneExtension()) ? phoneNumber.getPhoneExtension() : null);
    params.addValue(PRIMARY_IND, phoneNumber.isPrimary());
    params.addValue(ACTIVE_IND, phoneNumber.isActive());
    params.addValue(UPDT_ID, user.getUserUID());

    params.addValue(PHONE_ID, phoneNumber.getPhoneUID());
    params.addValue(UPDT_CNT, phoneNumber.getPhoneUpdateCount());

    return params;
  }
}
