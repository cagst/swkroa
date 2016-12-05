package com.cagst.swkroa.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Converter class to convert various formats into a {@link LocalDate} and a {@link LocalDate} into a {@link Timestamp}.
 *
 * @author Craig Gaskill
 */
public abstract class LocalDateConverter {
  /**
   * Will convert the specified {@link Timestamp} into its corresponding {@link LocalDate} representation.
   *
   * @param timestamp
   *    The {@link Timestamp} to convert.
   *
   * @return A {@link LocalDate} representation of the specified timestamp.
   */
  @SuppressWarnings("deprecation")
  public static LocalDate convert(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }

    return LocalDate.of(timestamp.getYear() + 1900, timestamp.getMonth() + 1, timestamp.getDate());
  }

  /**
   * Will convert the specified {@link Date} into its corresponding {@link LocalDate} representation.
   *
   * @param date
   *    The {@link Date} to convert.
   *
   * @return A {@link LocalDate} representation of the specified date.
   */
  @SuppressWarnings("deprecation")
  public static LocalDate convert(Date date) {
    if (date == null) {
      return null;
    }

    return LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
  }

  /**
   * Will convert the specified {@link LocalDate} into its corresponding {@link Timestamp} representation.
   *
   * @param localDate
   *    The {@link LocalDate} to convert.
   *
   * @return A {@link Timestamp} representation of the specified local date.
   */
  public static Timestamp convert(LocalDate localDate) {
    if (localDate == null) {
      return null;
    }

    return Timestamp.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }
}
