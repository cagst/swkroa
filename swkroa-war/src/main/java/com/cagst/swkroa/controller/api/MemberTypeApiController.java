package com.cagst.swkroa.controller.api;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

import com.cagst.swkroa.member.MemberType;
import com.cagst.swkroa.member.MemberTypeRepository;
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
 * Handles and retrieves {@link MemberType} objects depending on the URI template.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping("/api/membertypes")
public class MemberTypeApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MemberTypeApiController.class);

  @Inject
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
  public List<MemberType> getMemberTypes(final @PathVariable("memberTypeId") long memberTypeId) {
    LOGGER.info("Received request to retrieve all active member types for member type [{}]", memberTypeId);

    List<MemberType> types = memberTypeRepository.getActiveMemberTypesForMemberType(memberTypeId);
    types.sort((MemberType rhs, MemberType lhs) -> rhs.getBeginEffectiveDate().compareTo(rhs.getBeginEffectiveDate()));

    return types;
  }

  /**
   * Handles the request and persists the {@link MemberType} to persistent storage.
   *
   * @param memberType
   *    The {@link MemberType} to persist.
   * @param request
   *    The {@link HttpServletRequest} sent from the caller.
   *
   * @return The {@link MemberType} after it has been persisted.
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<MemberType> saveMemberType(final @RequestBody MemberType memberType,
                                                   final HttpServletRequest request) {

    LOGGER.info("Received request to save membertype [{}]", memberType.getMemberTypeUID());

    // determine if this is a new MemberType before we save it
    boolean newMemberType = (memberType.getMemberTypeUID() == 0);

    // save the MemberType
    MemberType savedMemberType = memberTypeRepository.saveMemberType(memberType, WebAppUtils.getUser());

    // specify the location of the resource
    UriComponents locationUri = ServletUriComponentsBuilder
        .fromContextPath(request)
        .path("/api/membertypes/{memberTypeId}")
        .buildAndExpand(savedMemberType.getMemberTypeUID());

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(locationUri.toUri());

    return new ResponseEntity<>(savedMemberType, headers, newMemberType ? HttpStatus.CREATED : HttpStatus.OK);
  }

  @RequestMapping(value = "/{memberTypeId}", method = RequestMethod.PUT)
  public ResponseEntity<MemberType> saveMemberTypeRate(final @PathVariable("memberTypeId") long memberTypeId,
                                                       final @RequestBody MemberType memberType) {

    LOGGER.info("Received request to save membertype rate [{}]", memberTypeId);

    // find the currently active and effective member type and set its end effective date
    MemberType currentMemberType = memberTypeRepository.getMemberTypeByUID(memberTypeId);
    currentMemberType.setEndEffectiveDate(memberType.getBeginEffectiveDate());

    memberTypeRepository.saveMemberType(currentMemberType, WebAppUtils.getUser());

    // ensure the new member type has the correct values
    memberType.setPreviousMemberTypeUID(currentMemberType.getPreviousMemberTypeUID());
    memberType.setMemberTypeDisplay(currentMemberType.getMemberTypeDisplay());
    memberType.setMemberTypeMeaning(currentMemberType.getMemberTypeMeaning());
    memberType.setPrimary(currentMemberType.isPrimary());
    memberType.setAllowSpouse(currentMemberType.isAllowSpouse());
    memberType.setAllowMember(currentMemberType.isAllowMember());

    // save the new MemberType
    MemberType savedMemberType = memberTypeRepository.saveMemberType(memberType, WebAppUtils.getUser());

    return new ResponseEntity<>(savedMemberType, HttpStatus.OK);
  }
}
