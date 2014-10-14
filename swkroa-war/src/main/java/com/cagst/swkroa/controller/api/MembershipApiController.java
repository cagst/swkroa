package com.cagst.swkroa.controller.api;

import com.cagst.common.web.servlet.tags.StaticResourceAssistant;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.member.*;
import com.cagst.swkroa.person.Person;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * Handles and retrieves {@link Membership}, {@link Member}, and {@link MembershipCounty} objects depending upon the URI
 * template.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@Controller
public final class MembershipApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MembershipApiController.class);

  @Autowired
  private CodeValueRepository codeValueRepo;

  @Autowired
  private MembershipService membershipService;

  @Autowired
  private MemberRepository memberRepo;

  @Autowired
  private MemberTypeRepository memberTypeRepo;

  /**
   * Handles the request and retrieves the active Memberships within the system.
   *
   * @return A JSON representation of the active Memberships within the system.
   */
  @RequestMapping(value = "/api/memberships", method = RequestMethod.GET)
  @ResponseBody
  public List<Membership> getMemberships(final @RequestParam("q") String query) {
    LOGGER.info("Received request to retrieve memberships using query string [{}]", query);

    List<Membership> memberships;
    if (StringUtils.isNotBlank(query)) {
      memberships = membershipService.getMembershipsForName(query);
    } else {
      memberships = membershipService.getActiveMemberships();
    }

    Collections.sort(memberships);

    return memberships;
  }

  /**
   * Handles the request and retrieves the {@link Membership} associated with the specified membership ID.
   *
   * @param membershipId
   *     A {@link long} that uniquely identifies the {@link Membership} to retrieve.
   *
   * @return A {@link Membership} that represents the {@link Membership} for the specified membership ID.
   */
  @RequestMapping(value = {"/api/memberships/{membershipId}"}, method = RequestMethod.GET)
  @ResponseBody
  public Membership getMembership(final @PathVariable long membershipId) {
    LOGGER.info("Received request to retrieve membership [{}].", membershipId);

    if (membershipId == 0L) {
      Member primary = new Member();
      primary.setMemberType(memberTypeRepo.getMemberTypeByMeaning(MemberType.REGULAR));
      primary.setPerson(new Person());

      Membership membership = new Membership();
      membership.setEntityType(codeValueRepo.getCodeValueByMeaning("ENTITY_INDIVIDUAL"));
      membership.addMember(primary);

      return membership;
    } else {
      return membershipService.getMembershipByUID(membershipId);
    }
  }

  /**
   * Handles the generation of an OwnerID based upon a first name and last name.
   *
   * @param firstName
   *     The first name of the member to generate an OwnerID for.
   * @param lastName
   *     The last name of the member to generate an OwnerID for.
   *
   * @return The next available OwnerId based upon the specified first name and last name, or an empty string if no
   * OwnerId could be determined.
   */
  @RequestMapping(value = {"/api/generateOwnerId/{firstName}/{lastName}"})
  public
  @ResponseBody
  String generateOwnerId(final @PathVariable String firstName, final @PathVariable String lastName) {
    String ownerId = StringUtils.EMPTY;

    try {
      ownerId = memberRepo.generateOwnerId(firstName, lastName);
    } catch (IllegalArgumentException ex) {
      // do nothing
    }

    return ownerId;
  }

  /**
   * Handles the request and persists the {@link Membership} to persistent storage. Called from the Add/Edit
   * Membership page.
   *
   * @param membership
   *     The {@link Membership} to persist.
   */
  @RequestMapping(value = {"/api/memberships"}, method = RequestMethod.POST)
  @ResponseBody
  public String saveMembership(final @RequestBody Membership membership, final HttpServletRequest request) {
    LOGGER.info("Received request to save membership [{}]", membership.getMembershipUID());

    membershipService.saveMembership(membership, WebAppUtils.getUser());

    return StaticResourceAssistant.getString("url.membership.home", request);
  }

  /**
   * Handles the request and persists the {@link Membership} to persistent storage. Called from the Add/Edit Membership
   * page when adding/editing a membership.
   *
   * @param membership
   *     The {@link Membership} to persist.
   *
   * @return The {@link Membership} after it has been persisted.
   */
  @RequestMapping(value = {"/api/memberships"}, method = RequestMethod.PUT)
  @ResponseBody
  public Membership saveMembership(final @RequestBody Membership membership) {
    LOGGER.info("Received request to save membership [{}]", membership.getMembershipUID());

    return membershipService.saveMembership(membership, WebAppUtils.getUser());
  }
}
