package com.cagst.swkroa.web.util;

import com.cagst.swkroa.codevalue.CodeValue;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * This mapper extends the Jackson {@link ObjectMapper} to add support for Joda-Time JSON support as well as other
 * serialization/deserialization feature needed for domain objects.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public final class JacksonObjectMapper extends ObjectMapper {
  private static final long serialVersionUID = -2081244189288220564L;

  public JacksonObjectMapper() {
    super();

    this.registerModule(new JodaModule());

    // Ensures the Joda DateTime is serialized/de-serialized formatted as ISO Date and Time
    this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    // Adds support for read-only properties (getters with no setters).
    this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // Create a module to contain our custom serializer / deserializers.
//    SimpleModule module = new SimpleModule();
//    module.addSerializer(CodeValue.class, new CodeValueSerializer());
//    module.addDeserializer(CodeValue.class, new CodeValueDeserializer());
//
//    this.registerModule(module);
  }
}
