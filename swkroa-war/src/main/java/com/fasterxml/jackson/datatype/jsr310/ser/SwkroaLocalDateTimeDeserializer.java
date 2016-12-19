package com.fasterxml.jackson.datatype.jsr310.ser;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

/**
 * Deserializer for Java 8 temporal {@link LocalDateTime} class.
 *
 * @author Craig Gaskill
 */
public class SwkroaLocalDateTimeDeserializer extends LocalDateTimeDeserializer {
  private static final long serialVersionUID = 1L;

  private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  public static final SwkroaLocalDateTimeDeserializer INSTANCE = new SwkroaLocalDateTimeDeserializer();

  private SwkroaLocalDateTimeDeserializer() {
    this(DEFAULT_FORMATTER);
  }

  private SwkroaLocalDateTimeDeserializer(DateTimeFormatter formatter) {
    super(formatter);
  }

  @Override
  public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
      BigInteger val = parser.getBigIntegerValue();

      return Instant.ofEpochMilli(val.longValue()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    } else {
      return super.deserialize(parser, context);
    }
  }
}
