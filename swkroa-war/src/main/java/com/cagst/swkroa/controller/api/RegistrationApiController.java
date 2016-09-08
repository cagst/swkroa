package com.cagst.swkroa.controller.api;

import com.cagst.swkroa.contact.Address;
import com.cagst.swkroa.contact.ContactRepository;
import com.cagst.swkroa.contact.EmailAddress;
import com.cagst.swkroa.contact.PhoneNumber;
import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.member.MemberRepository;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserRepository;
import com.cagst.swkroa.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Handles and retrieves requests for Registration.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping(value = "/api/register")
public final class RegistrationApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationApiController.class);

  private final ResourceBundle resourceBundle;

  private MemberRepository memberRepository;
  private ContactRepository contactRepo;
  private UserRepository userRepo;
  private UserService userService;

  /**
   * Default Constructor
   */
  public RegistrationApiController() {
    resourceBundle = ResourceBundle.getBundle("properties.i18n.auth", Locale.US);
  }

  @Inject
  public void setMemberRepository(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Inject
  public void setContactRepository(ContactRepository contactRepository) {
    this.contactRepo = contactRepository;
  }

  @Inject
  public void setUserRepository(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Inject
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(value = "/identification/{ownerId}", method = RequestMethod.GET)
  public ResponseEntity<String> registerIdentification(@PathVariable(value = "ownerId") String ownerId) {
    LOGGER.info("Received request to identify membership for Owner ID [{}]", ownerId);

    Optional<Member> checkMember = memberRepository.getMemberByOwnerId(ownerId.toUpperCase());
    if (!checkMember.isPresent()) {
      String msg = resourceBundle.getString("signin.register.identify.notfound");
      return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    Member member = checkMember.get();
    if (member.getPerson() == null) {
      String msg = resourceBundle.getString("signin.register.identify.noperson");
      return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    Optional<User> user = userService.getUserByPersonId(member.getPerson().getPersonUID());
    if (user.isPresent()) {
      String msg = resourceBundle.getString("signin.register.identify.exists");
      return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/verification/{ownerId}", method = RequestMethod.POST)
  public ResponseEntity<String> registerVerify(
      @PathVariable(value = "ownerId") String ownerId,
      @RequestParam(value = "firstName", required = false) String firstName,
      @RequestParam(value = "lastName", required = false) String lastName,
      @RequestParam(value = "zipCode", required = false) String zipCode,
      @RequestParam(value = "phoneNumber", required = false) String phoneNumber
  ) {
    LOGGER.info("Received request to verify membership for Owner ID [{}]", ownerId);

    // performing the same operations as the registerIdentify method
    // in case this API was called maliciously or out of order
    Optional<Member> checkMember = memberRepository.getMemberByOwnerId(ownerId.toUpperCase());
    if (!checkMember.isPresent()) {
      String msg = resourceBundle.getString("signin.register.identify.notfound");
      return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    Member member = checkMember.get();
    if (member.getPerson() == null) {
      String msg = resourceBundle.getString("signin.register.identify.noperson");
      return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    Optional<User> user = userService.getUserByPersonId(member.getPerson().getPersonUID());
    if (user.isPresent()) {
      String msg = resourceBundle.getString("signin.register.identify.exists");
      return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
    }

    boolean verified = verify(member, firstName, lastName, zipCode, phoneNumber);

    if (!verified) {
      String msg = resourceBundle.getString("signin.register.verify.error");
      return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    List<EmailAddress> emailAddresses = contactRepo.getEmailAddressesForMember(member);
    EmailAddress primaryEmailAddress = null;
    for (EmailAddress emailAddress : emailAddresses) {
      if (emailAddress.isPrimary()) {
        primaryEmailAddress = emailAddress;
        break;
      } else if (primaryEmailAddress == null) {
        primaryEmailAddress = emailAddress;
      }
    }

    String email = (primaryEmailAddress != null ? primaryEmailAddress.getEmailAddress() : null);
    return new ResponseEntity<>(email, HttpStatus.OK);
  }

  @RequestMapping(value = "/completion/{ownerId}", method = RequestMethod.POST)
  public ResponseEntity<String> registerComplete(
      @PathVariable(value = "ownerId") String ownerId,
      @RequestParam(value = "firstName", required = false) String firstName,
      @RequestParam(value = "lastName", required = false) String lastName,
      @RequestParam(value = "zipCode", required = false) String zipCode,
      @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
      @RequestParam(value = "emailAddress") String emailAddress,
      @RequestParam(value = "username") String username,
      @RequestParam(value = "password") String password,
      @RequestParam(value = "confirm") String confirm
  ) {
    LOGGER.info("Received request to verify membership for Owner ID [{}]", ownerId);

    // performing the same operations as the registerIdentify method
    // and performing the same operations as the registerVerify method
    // in case this API was called maliciously or out of order
    Optional<Member> checkMember = memberRepository.getMemberByOwnerId(ownerId.toUpperCase());
    if (!checkMember.isPresent()) {
      String msg = resourceBundle.getString("signin.register.identify.notfound");
      return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    Member member = checkMember.get();
    if (member.getPerson() == null) {
      String msg = resourceBundle.getString("signin.register.identify.noperson");
      return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    Optional<User> checkUser = userService.getUserByPersonId(member.getPerson().getPersonUID());
    if (checkUser.isPresent()) {
      String msg = resourceBundle.getString("signin.register.identify.exists");
      return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
    }

    boolean verified = verify(member, firstName, lastName, zipCode, phoneNumber);
    if (!verified) {
      String msg = resourceBundle.getString("signin.register.verify.error");
      return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    if (!StringUtils.equals(password, confirm)) {
      String msg = resourceBundle.getString("signin.register.complete.password.mismatch");
      return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

    boolean usernameTaken = userService.doesUsernameExist(username);
    if (usernameTaken) {
      String msgFormat = resourceBundle.getString("signin.register.complete.username.exists");
      String msg = MessageFormat.format(msgFormat, username);

      return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
    }

    Optional<User> checkSystemUser = userRepo.getUserByUsername(UserService.SYSTEM_USER);
    if (!checkSystemUser.isPresent()) {
      LOGGER.error("Unable to find the 'system' user.");
      return new ResponseEntity<>("Unable to find the 'system' user.", HttpStatus.NOT_FOUND);
    }

    User systemUser = checkSystemUser.get();

    User user = new User();
    user.setPersonUID(member.getPerson().getPersonUID());
    user.setUsername(username);
    user.setPassword(password);
    user.setPasswordTemporary(false);
    user.setPasswordChangedDate(new DateTime());
    user.setActive(true);

    userService.registerUser(user, systemUser);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  private boolean verify(Member member, String firstName, String lastName, String zipCode, String phoneNumber) {
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

    return (suppliedItems == verifiedItems);
  }
}
