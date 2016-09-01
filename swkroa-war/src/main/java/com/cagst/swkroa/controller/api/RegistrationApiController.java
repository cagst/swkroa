package com.cagst.swkroa.controller.api;

import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.member.MemberRepository;
import com.cagst.swkroa.model.RegistrationIdentification;
import com.cagst.swkroa.person.Person;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
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

  @RequestMapping(value = "/identification", method = RequestMethod.POST)
  public ResponseEntity registerIdentification(@RequestBody RegistrationIdentification registrationIdentification) {
    LOGGER.info("Received request to register membership identity for Owner ID [{}]", registrationIdentification.getOwnerId());

    boolean found = true;

    Optional<Member> checkMember = memberRepository.getMemberByOwnerId(registrationIdentification.getOwnerId());
    if (!checkMember.isPresent()) {
      found = false;
    } else {
      Member member = checkMember.get();
      if (member.getPerson() != null) {
        Person person = member.getPerson();

        if (StringUtils.isNotBlank(registrationIdentification.getFirstName()) &&
            !StringUtils.equalsIgnoreCase(registrationIdentification.getFirstName(), person.getFirstName())) {
          found = false;
        }

        if (StringUtils.isNotBlank(registrationIdentification.getLastName()) &&
            !StringUtils.equalsIgnoreCase(registrationIdentification.getLastName(), person.getLastName())) {
          found = false;
        }
      }
    }

    if (found) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
