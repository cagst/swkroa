package com.cagst.swkroa.controller.api;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.member.MemberRepository;
import com.cagst.swkroa.member.MembershipStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles and retrieves {@link Member} objects depending upon the the URI template.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping("/api/members")
public final class MemberApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MemberApiController.class);

  private final MemberRepository memberRepo;

  @Inject
  public MemberApiController(final MemberRepository memberRepo) {
    this.memberRepo = memberRepo;
  }

  /**
   * Handles the request and retrieves the Members within the system based upon the specified parameters.
   *
   * @param query
   *        A {@link String} that represents the Name (first or last), OwnerId, or Company to search for Members.
   * @param status
   *        A {@link String} that represents the status (Active / Inactive) to search for Members.
   *
   * @return A JSON representation of the {@link Member Members} within the system matching the specific parameters.
   */
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Member> getMembers(final @RequestParam(value = "q", required = false) String query,
                                 final @RequestParam(value = "status", required = false) String status) {

    LOGGER.info("Received request to retrieve memberships using query [{}] and status [{}]", query, status);

    List<Member> members;

    if (StringUtils.isNotBlank(query)) {
      members = memberRepo.getMembersByName(
          query,
          StringUtils.isNotBlank(status) ? MembershipStatus.valueOf(status) : MembershipStatus.ACTIVE);
    } else {
      members = new ArrayList<Member>();
    }

    return members;
  }
}
