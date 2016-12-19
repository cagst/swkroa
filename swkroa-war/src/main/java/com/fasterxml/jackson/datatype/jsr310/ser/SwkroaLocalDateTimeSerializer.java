package com.fasterxml.jackson.datatype.jsr310.ser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Serializer for Java 8 temporal {@link LocalDateTime} class.
 *
 * @author Craig Gaskill
 */
public class SwkroaLocalDateTimeSerializer extends JSR310FormattedSerializerBase<LocalDateTime> {
  private static final long serialVersionUID = 1L;

  public static final SwkroaLocalDateTimeSerializer INSTANCE = new SwkroaLocalDateTimeSerializer();

  private SwkroaLocalDateTimeSerializer() {
    super(LocalDateTime.class);
  }

  private SwkroaLocalDateTimeSerializer(SwkroaLocalDateTimeSerializer base,
                                        Boolean useTimestamp,
                                        DateTimeFormatter dtf) {
    super(base, useTimestamp, dtf);
  }

  @Override
  protected SwkroaLocalDateTimeSerializer withFormat(Boolean useTimestamp, DateTimeFormatter dtf) {
    return new SwkroaLocalDateTimeSerializer(this, useTimestamp, dtf);
  }

  @Override
  public void serialize(LocalDateTime dateTime, JsonGenerator generator, SerializerProvider provider) throws IOException {
    if (useTimestamp(provider)) {
      generator.writeNumber(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    } else {
      String str = (_formatter == null) ? dateTime.toString() : dateTime.format(_formatter);
      generator.writeString(str);
    }
  }
}
