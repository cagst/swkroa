package com.cagst.swkroa.web.util;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Will deserialize a CodeValue from it's unique identifier into an actual {@link CodeValue} object when receiving from
 * a web browser.
 *
 * @author Craig Gaskill
 *
 * @version 1.0.0
 */
public class CodeValueDeserializer extends JsonDeserializer<CodeValue> {
  @Autowired
  private CodeValueRepository codeValueRepository;

  @Override
  public CodeValue deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    return codeValueRepository.getCodeValueByUID(jsonParser.getNumberValue().longValue());
  }
}
