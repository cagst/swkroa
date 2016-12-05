package com.cagst.swkroa.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Converter class to convert various formats into a {@link LocalDateTime} and a {@link LocalDateTime} into a {@link Timestamp}.
 *
 * @author Craig Gaskill
 */
public abstract class LocalDateTimeConverter {
  /**
   * Will convert the specified {@link Timestamp} into its corresponding {@link LocalDateTime} representation.
   *
    * @param timestamp
   *    The {@link Timestamp} to convert.
   *
   * @return A {@link LocalDateTime} representation of the specified timestamp.
   */
  public static LocalDateTime convert(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }

    return timestamp.toLocalDateTime();
  }

  /**
   * Will convert the specified {@link LocalDateTime} into its corresponding {@link Timestamp} representation.
   *
   * @param localDateTime
   *    The {@link LocalDateTime} to convert.
   *
   * @return A {@link Timestamp} representation of the specified local datetime.
   */
  public static Timestamp convert(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }

    return Timestamp.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }
}
