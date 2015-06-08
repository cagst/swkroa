package com.cagst.swkroa.controller.api;

import java.util.Collections;
import java.util.List;

import com.cagst.swkroa.member.MemberType;
import com.cagst.swkroa.member.MemberTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles and retrieves {@link MemberType} objects depending on the URI template.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping("/api/membertypes")
public class MemberTypeApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MemberTypeApiController.class);

  @Autowired
  private MemberTypeRepository memberTypeRepository;

  /**
   * Handles the request and retrieves the active {@link MemberType MemberTypes} within the system.
   *
   * @return A JSON representation of the active {@link MemberType MemberTypes} within the system.
   */
  @RequestMapping(method = RequestMethod.GET)
  public List<MemberType> getActiveMemberTypes() {
    LOGGER.info("Received request to retrieve active member types.");

    List<MemberType> types = memberTypeRepository.getActiveMemberTypes();
    Collections.sort(types);

    return types;
  }

  @RequestMapping(value = "/{memberTypeId}", method = RequestMethod.GET)
  @ResponseBody
  public List<MemberType> getMemberTypes(final @PathVariable("memberTypeId") long memberTypeId) {
    LOGGER.info("Received request to retrieve all active member types for member type [{}]", memberTypeId);

    List<MemberType> types = memberTypeRepository.getActiveMemberTypesForMemberType(memberTypeId);

    return null;
  }
}
