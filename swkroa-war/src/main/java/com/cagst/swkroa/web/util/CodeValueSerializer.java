package com.cagst.swkroa.web.util;

import java.io.IOException;

import com.cagst.swkroa.codevalue.CodeValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Will serialize a {@link CodeValue} into its unique identifier when sending to a web browser.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public class CodeValueSerializer extends JsonSerializer<CodeValue> {
  @Override
  public void serialize(CodeValue codeValue, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeNumberField("codeValueId", codeValue.getCodeSetUID());
    jsonGenerator.writeStringField("codeValueDisplay", codeValue.getDisplay());
    jsonGenerator.writeEndObject();

//    jsonGenerator.writeNumber(codeValue.getCodeSetUID());
  }
}
