package com.cagst.swkroa.util;

import java.sql.Timestamp;

import org.joda.time.DateTime;

/**
 * This is a utility class that will help in the conversion of a date/time column in the database that is stored as UTC
 * into a {@link DateTime} when reading from and writing to a database.
 *
 * @author Craig Gaskill
 */
public abstract class DateTimeConverter {
  /**
   * Returns the {@link DateTime} from the specified {@link Timestamp}.
   *
   * @param timestamp
   *     The {@link Timestamp} to retrieve as a {@link DateTime}.
   *
   * @return The {@link DateTime} representation of the specified {@link Timestamp}.
   */
  public static DateTime convert(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }

    return new DateTime(timestamp);
  }

  /**
   * Returns the {@link Timestamp} from the specified {@link DateTime}.
   *
   * @param dt
   *    The {@link DateTime} to retrieve as a {@link Timestamp}.
   *
   * @return The {@link Timestamp} representation of the specified {@link DateTime}.
   */
  public static Timestamp convert(DateTime dt) {
    if (dt == null) {
      return null;
    }

    return new Timestamp(dt.getMillis());
  }
}
