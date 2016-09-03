package com.cagst.swkroa.controller.api;

import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.member.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Handles and retrieves requests for Registration.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping(value = "/api/register")
public final class RegistrationApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationApiController.class);

  private MemberRepository memberRepository;

  @Inject
  public void setMemberRepository(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @RequestMapping(value = "/identification/{ownerId}", method = RequestMethod.GET)
  public ResponseEntity<Member> registerIdentification(@PathVariable(value = "ownerId") String ownerId) {
    LOGGER.info("Received request to register membership identity for Owner ID [{}]", ownerId);

    Optional<Member> checkMember = memberRepository.getMemberByOwnerId(ownerId.toUpperCase());
    if (!checkMember.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(checkMember.get(), HttpStatus.OK);
    }
  }
}
