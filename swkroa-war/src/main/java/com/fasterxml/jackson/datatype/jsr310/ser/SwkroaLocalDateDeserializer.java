package com.fasterxml.jackson.datatype.jsr310.ser;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

/**
 * Deserializer for Java 8 temporal {@link LocalDate} class.
 *
 * @author Craig Gaskill
 */
public class SwkroaLocalDateDeserializer extends LocalDateDeserializer {
  private static final long serialVersionUID = 1L;

  private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  public static final SwkroaLocalDateDeserializer INSTANCE = new SwkroaLocalDateDeserializer();

  private SwkroaLocalDateDeserializer() {
    this(DEFAULT_FORMATTER);
  }

  public SwkroaLocalDateDeserializer(DateTimeFormatter dtf) {
    super(dtf);
  }

  @Override
  public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
    if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
      BigInteger val = parser.getBigIntegerValue();

      return Instant.ofEpochMilli(val.longValue()).atZone(ZoneId.systemDefault()).toLocalDate();
    } else {
      return super.deserialize(parser, context);
    }
  }
}
