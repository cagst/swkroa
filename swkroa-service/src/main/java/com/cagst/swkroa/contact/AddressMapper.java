package com.cagst.swkroa.contact;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cagst.swkroa.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Maps a row in the resultset into a {@link Address} object. Used to marshal / unmarshal a
 * {@link Address} to / from the database.
 *
 * @author Craig Gaskill
 */
/* package */final class AddressMapper implements RowMapper<Address> {
  private static final String ADDRESS_ID = "address_id";
  private static final String PARENT_ENTITY_ID = "parent_entity_id";
  private static final String PARENT_ENTITY_NAME = "parent_entity_name";
  private static final String ADDRESS_TYPE = "address_type_cd";
  private static final String ADDRESS1 = "address1";
  private static final String ADDRESS2 = "address2";
  private static final String ADDRESS3 = "address3";
  private static final String CITY = "city";
  private static final String STATE_CODE = "state_code";
  private static final String POSTAL_CODE = "postal_code";
  private static final String COUNTRY_CODE = "country_code";
  private static final String PRIMARY_IND = "primary_ind";
  private static final String ACTIVE_IND = "active_ind";
  private static final String CREATE_ID = "create_id";
  private static final String UPDT_ID = "updt_id";
  private static final String UPDT_CNT = "updt_cnt";

  @Override
  public Address mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    return Address.builder()
        .setAddressUID(rs.getLong(ADDRESS_ID))
        .setParentEntityUID(rs.getLong(PARENT_ENTITY_ID))
        .setParentEntityName(rs.getString(PARENT_ENTITY_NAME))
        .setAddressTypeCD(rs.getLong(ADDRESS_TYPE))
        .setAddressLine1(rs.getString(ADDRESS1))
        .setAddressLine2(rs.getString(ADDRESS2))
        .setAddressLine3(rs.getString(ADDRESS3))
        .setCity(rs.getString(CITY))
        .setState(rs.getString(STATE_CODE))
        .setPostalCode(rs.getString(POSTAL_CODE))
        .setCountry(rs.getString(COUNTRY_CODE))
        .setPrimary(rs.getBoolean(PRIMARY_IND))
        .setActive(rs.getBoolean(ACTIVE_IND))
        .setAddressUpdateCount(rs.getLong(UPDT_CNT))
        .build();
  }

  /**
   * Will marshal a {@link Address} into a {@link MapSqlParameterSource} for inserting into the
   * database.
   *
   * @param address
   *     The {@link Address} to map into an insert statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update}
   * statement.
   */
  public static MapSqlParameterSource mapInsertStatement(final Address address, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(PARENT_ENTITY_ID, address.getParentEntityUID());
    params.addValue(PARENT_ENTITY_NAME, address.getParentEntityName());
    params.addValue(ADDRESS_TYPE, address.getAddressTypeCD() != 0L ? address.getAddressTypeCD() : null);
    params.addValue(ADDRESS1, address.getAddressLine1());
    params.addValue(ADDRESS2, StringUtils.isNotBlank(address.getAddressLine2()) ? address.getAddressLine2() : null);
    params.addValue(ADDRESS3, StringUtils.isNotBlank(address.getAddressLine3()) ? address.getAddressLine3() : null);
    params.addValue(CITY, address.getCity());
    params.addValue(STATE_CODE, address.getState());
    params.addValue(POSTAL_CODE, address.getPostalCode());
    params.addValue(COUNTRY_CODE, address.getCountry());
    params.addValue(PRIMARY_IND, address.isPrimary());
    params.addValue(ACTIVE_IND, address.isActive());
    params.addValue(CREATE_ID, user.getUserUID());
    params.addValue(UPDT_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link Address} into a {@link MapSqlParameterSource} for updating into the
   * database.
   *
   * @param address
   *     The {@link Address} to map into an update statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update}
   * statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(final Address address, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();

    params.addValue(ADDRESS_TYPE, address.getAddressTypeCD() != 0L ? address.getAddressTypeCD() : null);
    params.addValue(ADDRESS1, address.getAddressLine1());
    params.addValue(ADDRESS2, StringUtils.isNotBlank(address.getAddressLine2()) ? address.getAddressLine2() : null);
    params.addValue(ADDRESS3, StringUtils.isNotBlank(address.getAddressLine3()) ? address.getAddressLine3() : null);
    params.addValue(CITY, address.getCity());
    params.addValue(STATE_CODE, address.getState());
    params.addValue(POSTAL_CODE, address.getPostalCode());
    params.addValue(COUNTRY_CODE, address.getCountry());
    params.addValue(PRIMARY_IND, address.isPrimary());
    params.addValue(ACTIVE_IND, address.isActive());
    params.addValue(UPDT_ID, user.getUserUID());

    params.addValue(ADDRESS_ID, address.getAddressUID());
    params.addValue(UPDT_CNT, address.getAddressUpdateCount());

    return params;
  }
}
