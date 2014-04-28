package com.cagst.swkroa.controller.svc;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cagst.swkroa.codevalue.CodeSet;
import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.web.util.WebAppUtils;

/**
 * Handles and retrieves {@link CodeSet} and {@link CodeValue} objects depending on the URI
 * template.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
@Controller
public final class CodeSetSVCController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CodeSetSVCController.class);

	@Autowired
	private CodeValueRepository codeValueRepo;

	/**
	 * Handles the request and retrieves the active {@link CodeSet CodeSets} within the system.
	 * 
	 * @return A JSON representation of the active {@link CodeSet CodeSets} within the system.
	 */
	@RequestMapping(value = "/svc/codeset", method = RequestMethod.GET)
	@ResponseBody
	public List<CodeSet> getActiveCodeSets() {
		LOGGER.info("Received request to retrieve active codesets.");

		List<CodeSet> codeSets = codeValueRepo.getActiveCodeSets();
		Collections.sort(codeSets);

		return codeSets;
	}

	/**
	 * Handles the request and retrieves the {@link CodeValue CodeValues} associated to the specified CodeSet.
	 * 
	 * @param codeSetMeaning
	 *          A {@link String} that represents the {@link CodeSet} to retrieve {@link CodeValue
	 *          CodeValues} for.
	 * 
	 * @return A JSON representation of the CodeValues associated to the specified CodeSet.
	 */
	@RequestMapping(value = { "/svc/codeset/{codeSetMeaning}", "/membership/svc/codeset/{codeSetMeaning}" }, method = RequestMethod.GET)
	@ResponseBody
	public List<CodeValue> getCodeValuesForCodeSet(final @PathVariable String codeSetMeaning) {
		LOGGER.info("Received request to retrieve codevalues for codeset [{}].", codeSetMeaning);

		List<CodeValue> codeValues = codeValueRepo.getCodeValuesForCodeSetByMeaning(codeSetMeaning);
		Collections.sort(codeValues);

		return codeValues;
	}

	/**
	 * Handles the request and persists the {@link CodeValue} to persistent storage.
	 * 
	 * @param codeValue
	 *          The {@link CodeValue} to persist.
	 * 
	 * @return The {@link CodeValue} after it has been persisted.
	 */
	@RequestMapping(value = "/svc/codevalue", method = RequestMethod.PUT)
	@ResponseBody
	public CodeValue saveCodeValue(final @RequestBody CodeValue codeValue) {
		LOGGER.info("Received request to save codevalue [{}]", codeValue.getDisplay());

		return codeValueRepo.saveCodeValueForCodeSet(codeValue, WebAppUtils.getUser());
	}
}
