package com.cagst.swkroa.controller.svc;

import com.cagst.swkroa.codevalue.CodeSet;
import com.cagst.swkroa.member.MemberType;
import com.cagst.swkroa.member.MemberTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

/**
 * Handles and retrieves {@link MemberType} objects depending on the URI template.
 *
 * @author Craig Gaskill
 *
 * @version 1.0.0
 *
 */
@Controller
public class MemberTypeSVCController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MemberTypeSVCController.class);

	@Autowired
	private MemberTypeRepository memberTypeRepository;

	/**
	 * Handles the request and retrieves the active {@link MemberType MemberTypes} within the system.
	 *
	 * @return A JSON representation of the active {@link MemberType MemberTypes} within the system.
	 */
	@RequestMapping(value = {"/svc/membertype", "/membership/svc/membertype"}, method = RequestMethod.GET)
	@ResponseBody
	public List<MemberType> getActiveMemberTypes() {
		LOGGER.info("Received request to retrieve active member types.");

		List<MemberType> types = memberTypeRepository.getActiveMemberTypes();
		Collections.sort(types);

		return types;
	}
}
