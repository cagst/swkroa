package com.fasterxml.jackson.datatype.jsr310.ser;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;

/**
 * Serializer for Java 8 temporal {@link LocalDate} class.
 *
 * @author Craig Gaskill
 */
public class SwkroaLocalDateSerializer extends JSR310FormattedSerializerBase<LocalDate> {
  private static final long serialVersionUID = 1L;

  public static final SwkroaLocalDateSerializer INSTANCE = new SwkroaLocalDateSerializer();

  private SwkroaLocalDateSerializer() {
    super(LocalDate.class);
  }

  private SwkroaLocalDateSerializer(SwkroaLocalDateSerializer base,
                                    Boolean useTimestamp,
                                    DateTimeFormatter dtf) {
    super(base, useTimestamp, dtf);
  }

  @Override
  protected SwkroaLocalDateSerializer withFormat(Boolean useTimestamp, DateTimeFormatter dtf) {
    return new SwkroaLocalDateSerializer(this, useTimestamp, dtf);
  }

  @Override
  public void serialize(LocalDate date, JsonGenerator generator, SerializerProvider provider) throws IOException {
    if (useTimestamp(provider)) {
      generator.writeNumber(date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
    } else {
      String str = (_formatter == null) ? date.toString() : date.format(_formatter);
      generator.writeString(str);
    }
  }

  @Override
  public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
    SerializerProvider provider = visitor.getProvider();
    boolean useTimestamp = (provider != null) && useTimestamp(provider);
    if (useTimestamp) {
      _acceptTimestampVisitor(visitor, typeHint);
    } else {
      JsonStringFormatVisitor v2 = visitor.expectStringFormat(typeHint);
      if (v2 != null) {
        v2.format(JsonValueFormat.DATE);
      }
    }
  }
}
