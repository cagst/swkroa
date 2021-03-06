package com.cagst.swkroa.controller.api;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

import com.cagst.swkroa.codevalue.CodeSet;
import com.cagst.swkroa.codevalue.CodeSetType;
import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

/**
 * Handles and retrieves {@link CodeSet} and {@link CodeValue} objects depending on the URI
 * template.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping(value = "/api/codesets")
public class CodeSetApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(CodeSetApiController.class);

  private final CodeValueRepository codeValueRepo;

  @Inject
  public CodeSetApiController(CodeValueRepository codeValueRepo) {
    this.codeValueRepo = codeValueRepo;
  }

  /**
   * Handles the request and retrieves the active {@link CodeSet CodeSets} within the system.
   *
   * @return A JSON representation of the active {@link CodeSet CodeSets} within the system.
   */
  @RequestMapping(method = RequestMethod.GET)
  public List<CodeSet> getActiveCodeSets() {
    LOGGER.info("Received request to retrieve active codesets.");

    List<CodeSet> codeSets = codeValueRepo.getActiveCodeSets();
    Collections.sort(codeSets);

    return codeSets;
  }

  /**
   * Handles the request and retrieves the {@link CodeValue CodeValues} associated to the specified CodeSet.
   *
   * @param codeSetType
   *    The {@link CodeSetType} that represents the {@link CodeSet} to retrieve {@link CodeValue CodeValues} for.
   *
   * @return A JSON representation of the CodeValues associated to the specified CodeSet.
   */
  @RequestMapping(value = "/{codeSetType}", method = RequestMethod.GET)
  public List<CodeValue> getCodeValuesForCodeSet(@PathVariable CodeSetType codeSetType) {
    LOGGER.info("Received request to retrieve codevalues for codeset [{}].", codeSetType);

    List<CodeValue> codeValues = codeValueRepo.getCodeValuesForCodeSetByType(codeSetType);
    Collections.sort(codeValues);

    return codeValues;
  }

  /**
   * Handles the request and persists the {@link CodeValue} to persistent storage.
   *
   * @param codeSetMeaning
   *    A {@link String} that represents the {@link CodeSet} to associate the {@link CodeValue CodeValues} to.
   * @param codeValue
   *    The {@link CodeValue} to persist.
   * @param request
   *    The {@link HttpServletRequest} sent from the caller.
   *
   * @return The {@link CodeValue} after it has been persisted.
   */
  @RolesAllowed("ROLE_ADMIN")
  @RequestMapping(value = "/{codeSetMeaning}", method = RequestMethod.POST)
  public ResponseEntity<CodeValue> saveCodeValue(@PathVariable String codeSetMeaning,
                                                 @RequestBody CodeValue codeValue,
                                                 HttpServletRequest request) {

    LOGGER.info("Received request to save codevalue [{}]", codeValue.getDisplay());

    // determine if this is a new CodeValue
    boolean newCodeValue = (codeValue.getCodeValueUID() == 0);

    // save the CodeValue
    CodeValue savedCodeValue = codeValueRepo.saveCodeValueForCodeSet(codeValue, WebAppUtils.getUser());

    // specify the location of the resource
    UriComponents locationUri = ServletUriComponentsBuilder
        .fromContextPath(request)
        .path("/api/codesets/{codeSetMeaning}/{codeValueUID}")
        .buildAndExpand(new Object[] {codeSetMeaning, savedCodeValue.getCodeValueUID()});

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(locationUri.toUri());

    return new ResponseEntity<>(savedCodeValue, headers, newCodeValue ? HttpStatus.CREATED : HttpStatus.OK);
  }
}
