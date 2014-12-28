package com.cagst.swkroa.controller.api;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.exception.ResourceNotFoundException;
import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.member.MemberRepository;
import com.cagst.swkroa.member.MemberType;
import com.cagst.swkroa.member.MemberTypeRepository;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.member.MembershipBalance;
import com.cagst.swkroa.member.MembershipCounty;
import com.cagst.swkroa.member.MembershipService;
import com.cagst.swkroa.member.MembershipStatus;
import com.cagst.swkroa.model.CloseMembership;
import com.cagst.swkroa.person.Person;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

/**
 * Handles and retrieves {@link Membership}, {@link Member}, and {@link MembershipCounty} objects depending upon the URI
 * template.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/memberships")
public final class MembershipApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MembershipApiController.class);

  private final CodeValueRepository codeValueRepo;
  private final MembershipService membershipService;
  private final MemberRepository memberRepo;
  private final MemberTypeRepository memberTypeRepo;

  @Inject
  public MembershipApiController(final CodeValueRepository codeValueRepo,
                                 final MembershipService membershipService,
                                 final MemberRepository memberRepo,
                                 final MemberTypeRepository memberTypeRepo) {
    this.codeValueRepo = codeValueRepo;
    this.membershipService = membershipService;
    this.memberRepo = memberRepo;
    this.memberTypeRepo = memberTypeRepo;
  }

  /**
   * Handles the request and retrieves the active Memberships within the system.
   *
   * @return A JSON representation of the active Memberships within the system.
   */
  @RequestMapping(method = RequestMethod.GET)
  public List<Membership> getMemberships(final @RequestParam(value = "q", required = false) String query,
                                         final @RequestParam(value = "status", required = false) String status,
                                         final @RequestParam(value = "balance", required = false) String balance) {

    LOGGER.info("Received request to retrieve memberships using query [{}], status [{}], and balance [{}]", query, status, balance);

    List<Membership> memberships;

    if (StringUtils.isNotBlank(query)) {
      memberships = membershipService.getMembershipsForName(
          query,
          StringUtils.isNotBlank(status) ? MembershipStatus.valueOf(status) : MembershipStatus.ACTIVE,
          StringUtils.isNotBlank(balance) ? MembershipBalance.valueOf(balance) : MembershipBalance.ALL
      );
    } else {
      memberships = membershipService.getMemberships(
          StringUtils.isNotBlank(status) ? MembershipStatus.valueOf(status) : MembershipStatus.ACTIVE,
          StringUtils.isNotBlank(balance) ? MembershipBalance.valueOf(balance) : MembershipBalance.ALL
      );
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
  @RequestMapping(value = "/{membershipId}", method = RequestMethod.GET)
  public Membership getMembership(final @PathVariable("membershipId") long membershipId) {
    LOGGER.info("Received request to retrieve membership [{}].", membershipId);

    if (membershipId == 0L) {
      Member primary = new Member();
      primary.setMemberType(memberTypeRepo.getMemberTypeByMeaning(MemberType.REGULAR));
      primary.setPerson(new Person());

      Membership membership = new Membership();
      membership.setEntityType(codeValueRepo.getCodeValueByMeaning("ENTITY_INDIVIDUAL"));
      membership.addMember(primary);

      return membership;
    }

    try {
      return membershipService.getMembershipByUID(membershipId);
    } catch (EmptyResultDataAccessException ex) {
      throw new ResourceNotFoundException(ex);
    } catch (IncorrectResultSizeDataAccessException ex) {
      throw new ResourceNotFoundException(ex);
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
  @RequestMapping(value = "/ownerId/{firstName}/{lastName}", method = RequestMethod.GET)
  public String generateOwnerId(final @PathVariable String firstName, final @PathVariable String lastName) {
    String ownerId = StringUtils.EMPTY;

    try {
      ownerId = memberRepo.generateOwnerId(firstName, lastName);
    } catch (IllegalArgumentException ex) {
      // do nothing
    }

    return ownerId;
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
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Membership> saveMembership(final @RequestBody Membership membership, final HttpServletRequest request) {
    LOGGER.info("Received request to save membership [{}]", membership.getMembershipUID());

    try {
      // determine if this is a new membership before we save it (since we will update the membership after the save)
      boolean newMembership = (membership.getMembershipUID() == 0);

      // save the membership
      Membership savedMembership = membershipService.saveMembership(membership, WebAppUtils.getUser());

      // specify the location of the resource
      UriComponents locationUri = ServletUriComponentsBuilder
          .fromContextPath(request)
          .path("/api/memberships/{membershipUID}")
          .buildAndExpand(savedMembership.getMembershipUID());

      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(locationUri.toUri());

      return new ResponseEntity<Membership>(savedMembership, headers, newMembership ? HttpStatus.CREATED : HttpStatus.OK);
    } catch (OptimisticLockingFailureException ex) {
      return new ResponseEntity<Membership>(membership, HttpStatus.CONFLICT);
    } catch (Exception ex) {
      return new ResponseEntity<Membership>(membership, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Handles the request and ...
   */
  @RequestMapping(value = "/close", method = RequestMethod.POST)
  public ResponseEntity deleteMembership(final @RequestBody CloseMembership closeMemberships) {
    LOGGER.info("Received request to delete memberships");

    if (CollectionUtils.isEmpty(closeMemberships.getMemberships())) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    if (closeMemberships.getCloseReason() == null && StringUtils.isBlank(closeMemberships.getCloseText())) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    membershipService.closeMemberships(
        closeMemberships.getMemberships(),
        closeMemberships.getCloseReason(),
        closeMemberships.getCloseText()
    );

    return new ResponseEntity(HttpStatus.OK);
  }
}
