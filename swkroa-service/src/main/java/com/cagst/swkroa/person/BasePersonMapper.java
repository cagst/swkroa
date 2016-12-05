package com.cagst.swkroa.person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import com.cagst.swkroa.user.User;
import com.cagst.swkroa.utils.SwkroaStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Used to marshal/unmarshal a {@link Person} to/from the database.
 *
 * @author Craig Gaskill
 */
public abstract class BasePersonMapper {
  protected static final String PERSON_ID = "person_id";
  protected static final String TITLE_CD = "title_cd";
  protected static final String NAME_LAST = "name_last";
  protected static final String NAME_LAST_KEY = "name_last_key";
  protected static final String NAME_FIRST = "name_first";
  protected static final String NAME_FIRST_KEY = "name_first_key";
  protected static final String NAME_MIDDLE = "name_middle";
  protected static final String LOCALE_LANGUAGE = "locale_language";
  protected static final String LOCALE_COUNTRY = "locale_country";
  protected static final String TIME_ZONE = "time_zone";

  // meta-data
  protected static final String ACTIVE_IND = "active_ind";
  protected static final String CREATE_ID = "create_id";
  protected static final String UPDT_ID = "updt_id";
  protected static final String PERSON_UPDT_CNT = "person_updt_cnt";

  public Person mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    Person person = new Person();

    String language = rs.getString(LOCALE_LANGUAGE);
    String country = rs.getString(LOCALE_COUNTRY);
    if (StringUtils.isNotEmpty(language)) {
      if (StringUtils.isNotEmpty(country)) {
        person.setLocale(new Locale(language, country));
      } else {
        person.setLocale(new Locale(language));
      }
    }

    person.setPersonUID(rs.getLong(PERSON_ID));
    person.setTitleCD(rs.getLong(TITLE_CD));
    person.setFirstName(rs.getString(NAME_FIRST));
    person.setMiddleName(rs.getString(NAME_MIDDLE));
    person.setLastName(rs.getString(NAME_LAST));

    // meta-data
    person.setPersonUpdateCount(rs.getLong(PERSON_UPDT_CNT));
    person.setActive(rs.getBoolean(ACTIVE_IND));

    return person;
  }

  /**
   * Will marshal a {@link Person} into a {@link MapSqlParameterSource} for inserting into the database.
   *
   * @param person
   *     The {@link Person} to map into an insert statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapInsertStatement(final Person person, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();

    mapCommonProperties(params, person, user);
    params.addValue(CREATE_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link Person} into a {@link MapSqlParameterSource} for updating into the database.
   *
   * @param person
   *     The {@link Person} to map into an update statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(final Person person, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();

    mapCommonProperties(params, person, user);
    params.addValue(PERSON_ID, person.getPersonUID());
    params.addValue(PERSON_UPDT_CNT, person.getPersonUpdateCount());

    return params;
  }

  private static void mapCommonProperties(final MapSqlParameterSource params, final Person person, final User user) {
    params.addValue(TITLE_CD, person.getTitleCD() > 0L ? person.getTitleCD() : null);
    params.addValue(NAME_LAST, person.getLastName());
    params.addValue(NAME_LAST_KEY, SwkroaStringUtils.normalizeToKey(person.getLastName()));
    params.addValue(NAME_FIRST, person.getFirstName());
    params.addValue(NAME_FIRST_KEY, SwkroaStringUtils.normalizeToKey(person.getFirstName()));
    params.addValue(NAME_MIDDLE, StringUtils.isNotBlank(person.getMiddleName()) ? person.getMiddleName() : null);
    params.addValue(LOCALE_LANGUAGE, person.getLocale() != null ? person.getLocale().getLanguage() : null);
    params.addValue(LOCALE_COUNTRY, person.getLocale() != null ? person.getLocale().getCountry() : null);
    params.addValue(TIME_ZONE, person.getTimeZone() != null ? person.getTimeZone().toString() : null);
    params.addValue(ACTIVE_IND, person.isActive());
    params.addValue(UPDT_ID, user.getUserUID());
  }
}
