package com.cagst.swkroa.controller.api;

import com.cagst.swkroa.contact.Address;
import com.cagst.swkroa.contact.ContactRepository;
import com.cagst.swkroa.contact.PhoneNumber;
import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.member.MemberRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
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
  private ContactRepository contactRepo;

  @Inject
  public void setMemberRepository(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Inject
  public void setContactRepository(ContactRepository contactRepository) {
    this.contactRepo = contactRepository;
  }

  @RequestMapping(value = "/identification/{ownerId}", method = RequestMethod.GET)
  public ResponseEntity registerIdentification(@PathVariable(value = "ownerId") String ownerId) {
    LOGGER.info("Received request to identify membership for Owner ID [{}]", ownerId);

    Optional<Member> checkMember = memberRepository.getMemberByOwnerId(ownerId.toUpperCase());
    if (!checkMember.isPresent()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok().build();
    }
  }

  @RequestMapping(value = "/verification/{ownerId}", method = RequestMethod.GET)
  public ResponseEntity registerVerify(
      @PathVariable(value = "ownerId") String ownerId,
      @RequestParam(value = "firstName", required = false) String firstName,
      @RequestParam(value = "lastName", required = false) String lastName,
      @RequestParam(value = "zipCode", required = false) String zipCode,
      @RequestParam(value = "phoneNumber", required = false) String phoneNumber
  ) {
    LOGGER.info("Received request to verify membership for Owner ID [{}]", ownerId);

    Optional<Member> checkMember = memberRepository.getMemberByOwnerId(ownerId.toUpperCase());
    if (!checkMember.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    Member member = checkMember.get();

    long suppliedItems = 0L;
    long verifiedItems = 0L;

    if (StringUtils.isNotBlank(firstName)) {
      suppliedItems++;

      if (member.getPerson() != null) {
        if (firstName.equalsIgnoreCase(member.getPerson().getFirstName())) {
          verifiedItems++;
        }
      }
    }

    if (StringUtils.isNotBlank(lastName)) {
      suppliedItems++;

      if (member.getPerson() != null) {
        if (lastName.equalsIgnoreCase(member.getPerson().getLastName())) {
          verifiedItems++;
        }
      }
    }

    if (StringUtils.isNotBlank(zipCode)) {
      suppliedItems++;

      int len = zipCode.length();

      List<Address> addresses = contactRepo.getAddressesForMember(member);
      for (Address address : addresses) {
        if (zipCode.equals(StringUtils.left(address.getPostalCode(), len))) {
          verifiedItems++;
          break;
        }
      }
    }

    if (StringUtils.isNotBlank(phoneNumber)) {
      suppliedItems++;

      int len = phoneNumber.length();

      List<PhoneNumber> phoneNumbers = contactRepo.getPhoneNumbersForMember(member);
      for (PhoneNumber phone : phoneNumbers) {
        if (phoneNumber.equals(StringUtils.left(phone.getPhoneNumber(), len))) {
          verifiedItems++;
          break;
        }
      }
    }

    if (suppliedItems == verifiedItems) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
