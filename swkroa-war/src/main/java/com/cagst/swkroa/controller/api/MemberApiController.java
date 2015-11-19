package com.cagst.swkroa.controller.api;

import javax.inject.Inject;

import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.member.MemberRepository;
import com.cagst.swkroa.member.Status;
import com.cagst.swkroa.model.ListModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
   * @param start
   *      An {@link int} that defines the first Member to retrieve.
   * @param limit
   *      An {@link int} that defines the number of Members to retrieve.
   *
   * @return A JSON representation of the {@link Member Members} within the system matching the specific parameters.
   */
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ListModel<Member>> getMembers(
      final @RequestParam(value = "q", required = true) String query,
      final @RequestParam(value = "status", required = false) String status,
      final @RequestParam(value = "start", required = false) Integer start,
      final @RequestParam(value = "limit", required = false) Integer limit) {

    LOGGER.info("Received request to retrieve memberships using query [{}] and status [{}]", query, status);

    Status newStatus = StringUtils.isNotBlank(status) ? Status.valueOf(status) : Status.ACTIVE;

    int newStart = (start != null ? start : 0);
    int newLimit = (limit != null ? limit : 20);

    ListModel<Member> model = new ListModel<>(
        memberRepo.getMembersByName(query, newStatus, newStart, newLimit),
        memberRepo.getMembersByNameCount(query, newStatus)
    );

    return new ResponseEntity<>(model, HttpStatus.OK);
  }
}
