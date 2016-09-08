package com.cagst.swkroa.user;

import com.cagst.swkroa.audit.AuditEventType;
import com.cagst.swkroa.audit.annotation.AuditInstigator;
import com.cagst.swkroa.audit.annotation.AuditMessage;
import com.cagst.swkroa.audit.annotation.Auditable;
import com.cagst.swkroa.contact.ContactRepository;
import com.cagst.swkroa.role.RoleRepository;
import com.cagst.swkroa.security.SecurityPolicy;
import com.cagst.swkroa.security.SecurityService;
import com.cagst.swkroa.utils.RandomPasswordGenerator;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

/**
 * User Service that provides authentication for SWKROA.
 *
 * @author Craig Gaskill
 */
@Named("userService")
public class UserServiceImpl implements UserService, MessageSourceAware {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private static final String MSG_SIGNIN_ATTEMPTS_EXCEEDED    = "com.cagst.swkroa.signin.message.attempts.exceeded";
  private static final String MSG_CHANGEPWD_OLD_PWD_INCORRECT = "com.cagst.swkroa.changepwd.password.err.wrongold";
  private static final String MSG_CHANGEPWD_MISMATCH          = "com.cagst.swkroa.changepwd.password.err.mismatch";
  private static final String MSG_CHANGEPWD_MATCHES_USERNAME  = "com.cagst.swkroa.changepwd.password.err.username";
  private static final String MSG_CHANGEPWD_NOT_CHANGED       = "com.cagst.swkroa.changepwd.password.err.same";
  private static final String MSG_CHANGEPWD_CHANGED           = "com.cagst.swkroa.audit.message.changepwd";
  private static final String MSG_RESETPWD_RESET              = "com.cagst.swkroa.audit.message.resetpwd";
  private static final String MSG_ACCOUNT_UNLOCK_AUTO         = "com.cagst.swkroa.audit.message.unlock.auto";
  private static final String MSG_ACCOUNT_UNLOCK_MANUAL       = "com.cagst.swkroa.audit.message.unlock.manual";
  private static final String MSG_ACCOUNT_ENABLED             = "com.cagst.swkroa.audit.message.enable";
  private static final String MSG_ACCOUNT_DISABLED            = "com.cagst.swkroa.audit.message.disable";

  private final UserRepository userRepo;
  private final RoleRepository roleRepo;
  private final SecurityService securityService;
  private final ContactRepository contactRepo;
  private final PasswordEncoder passwordEncoder;

  private MessageSourceAccessor messages;

  /**
   * Primary Constructor used to create an instance of <i>UserServiceImpl</i>.
   *
   * @param userRepo
   *     The {@link UserRepository} to use to retrieve / update {@link User Users}.
   * @param roleRepo
   *     The {@link RoleRepository} to use to retrieve the roles / privileges for the {@link User}.
   * @param securityService
   *     The {@link SecurityService} to use.
   * @param contactRepo
   *     The {@link ContactRepository} to use to retrieve contact information of the {@link User}.
   * @param passwordEncoder
   *     The {@link PasswordEncoder} to use to check / encode user passwords.
   */
  @Inject
  public UserServiceImpl(final UserRepository userRepo,
                         final RoleRepository roleRepo,
                         final SecurityService securityService,
                         final ContactRepository contactRepo,
                         final PasswordEncoder passwordEncoder) {

    this.userRepo = userRepo;
    this.roleRepo = roleRepo;
    this.securityService = securityService;
    this.contactRepo = contactRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void setMessageSource(final MessageSource messageSource) {
    messages = new MessageSourceAccessor(messageSource);
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(final @AuditInstigator String userName) throws UsernameNotFoundException,
      DataAccessException {

    LOGGER.info("Calling loadUserByUsername for [{}].", userName);

    Optional<User> checkUser = userRepo.getUserByUsername(userName);
    if (!checkUser.isPresent()) {
      throw new UsernameNotFoundException(null);
    }

    User user = userRepo.signinAttempt(checkUser.get());

    // retrieve SecurityPolicy for user
    SecurityPolicy securityPolicy = securityService.getSecurityPolicy(user);
    user.setSecurityPolicy(securityPolicy);

    // check to see if the account is already locked and should therefore be unlocked
    int accountLockedDays = user.getSecurityPolicy().getAccountLockedDays();
    if (user.getAccountLockedDate() != null && accountLockedDays > 0) {
      DateTime lockedDate = user.getAccountLockedDate();
      DateTime unlockAfter = lockedDate.plusDays(accountLockedDays);

      if (unlockAfter.isBeforeNow()) {
        user = unlockAccount(user, user);
      }
    }

    // if the account isn't locked, see if it needs to be locked (because they exceeded their login attempts)
    if (user.isAccountNonLocked()) {
      // if the security policy has a maximum number of sign-in attempts > 0
      // and we have exceeded the number of maximum attempts
      // then lock the account.
      if (securityPolicy.getMaxSigninAttempts() > 0 && user.getSigninAttempts() > securityPolicy.getMaxSigninAttempts()) {
        String msg = messages.getMessage(
            MSG_SIGNIN_ATTEMPTS_EXCEEDED,
            "The number of allowed sign-in attempts has been exceeded.  Account has been locked.");

        user = userRepo.lockUserAccount(user, msg, user);
      }
    }

    return user;
  }

  @Override
  @Transactional
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_SIGNIN_SUCCESSFUL)
  public User signinSuccessful(final @AuditInstigator User user, final String ipAddress) {
    User newUser = userRepo.signinSuccessful(user, ipAddress);

    // Collection<Privilege> privs = roleRepo.getPrivilegesForUser(newUser);
    // if (CollectionUtils.isEmpty(privs)) {
    // LOGGER.warn("No privileges found for user [{}].", newUser.getUsername());
    // }
    //
    // for (Privilege priv : privs) {
    // user.addGrantedAuthority(priv.getPrivilegeKey());
    // }

    return newUser;
  }

  @Override
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_SIGNIN_FAILURE)
  public void signinFailure(final @AuditInstigator String username, final @AuditMessage String message) {
    // Currently we don't do anything for a sign-in failure other than the audit record.
  }

  @Override
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_SIGNED_OUT)
  public void signedOut(final @AuditInstigator User user) {
    SecurityContext secCtx = SecurityContextHolder.getContext();
    if (secCtx != null) {
      secCtx.setAuthentication(null);
    }

    SecurityContextHolder.clearContext();
  }

  @Override
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_TIMED_OUT)
  public void timedOut(final @AuditInstigator User user) {
    SecurityContext secCtx = SecurityContextHolder.getContext();
    if (secCtx != null) {
      secCtx.setAuthentication(null);
    }

    SecurityContextHolder.clearContext();
  }

  @Override
  @Transactional
  public User changePassword(final User user, final String oldPassword, final String newPassword,
                             final String confirmPassword) throws BadCredentialsException {

    Assert.notNull(user, "[Assertion Failed] - argument [user] cannot be null.");
    Assert.hasText(oldPassword, "[Assertion Failed] - argument [oldPassword] must not be null, empty, or blank.");
    Assert.hasText(newPassword, "[Assertion Failed] - argument [newPassword] must not be null, empty, or blank.");
    Assert.hasText(confirmPassword, "[Assertion Failed] - argument [confirmPassword] must not be null, empty, or blank.");

    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      LOGGER.warn("Old password is not valid!");
      throw new BadCredentialsException(
          messages.getMessage(MSG_CHANGEPWD_OLD_PWD_INCORRECT, "The old password is incorrect."));
    }

    if (!StringUtils.equals(newPassword, confirmPassword)) {
      LOGGER.warn("New password does not match confirmation password!");
      throw new BadCredentialsException(
          messages.getMessage(MSG_CHANGEPWD_MISMATCH, "The confirmation password does not match the new password."));
    }

    if (StringUtils.equals(newPassword, user.getUsername())) {
      LOGGER.warn("New password matches username!");
      throw new BadCredentialsException(
          messages.getMessage(MSG_CHANGEPWD_MATCHES_USERNAME, "The new password cannot be the same as your user name."));
    }

    String checkNewPassword = passwordEncoder.encode(newPassword);
    if (StringUtils.equals(user.getPassword(), checkNewPassword)) {
      LOGGER.warn("New password is the same as the old password!");
      throw new BadCredentialsException(
          messages.getMessage(MSG_CHANGEPWD_NOT_CHANGED, "The new password is the same as the old password."));
    }

    String msg = messages.getMessage(MSG_CHANGEPWD_CHANGED, new Object[]{user.getUsername()}, "Password changed for user [{0}].");

    user.setPassword(checkNewPassword);
    return userRepo.changeUserPassword(user, checkNewPassword, msg);
  }

  @Override
  @Transactional
  public User resetPassword(final User user, final User instigator) {
    Assert.notNull(user, "[Assertion Failed] - argument [user] cannot be null.");

    String msg = messages.getMessage(
        MSG_RESETPWD_RESET,
        new Object[]{instigator.getUsername(), user.getUsername()},
        "Password reset by user [{0}] for user [{1}].");

    String tempPassword = RandomPasswordGenerator.generateRandomPassword(10);

    User updatedUser = userRepo.resetUserPassword(user, passwordEncoder.encode(tempPassword), msg, instigator);
    updatedUser.setPassword(tempPassword);

    return updatedUser;
  }

  @Override
  @Transactional
  public User unlockAccount(final User user, final User instigator) {
    Assert.notNull(user, "[Assertion Failed] - argument [user] cannot be null.");
    Assert.notNull(instigator, "[Assertion Failed] - argument [instigator] cannot be null.");

    String msg;
    if (user.getUserUID() == instigator.getUserUID()) {
      msg = messages.getMessage(
          MSG_ACCOUNT_UNLOCK_AUTO,
          new Object[]{user.getUsername()},
          "Account was automatically unlocked for user [{0}].");
    } else {
      msg = messages.getMessage(
          MSG_ACCOUNT_UNLOCK_MANUAL,
          new Object[]{instigator.getUsername(), user.getUsername()},
          "Account was manually unlocked by user [{0}] for user [{1}].");
    }

    return userRepo.unlockUserAccount(user, msg, instigator);
  }

  @Override
  @Transactional
  public User enableAccount(final User user, final User instigator) {
    Assert.notNull(user, "[Assertion Failed] - argument [user] cannot be null.");
    Assert.notNull(instigator, "[Assertion Failed] - argument [instigator] cannot be null.");

    String msg = messages.getMessage(
        MSG_ACCOUNT_ENABLED,
        new Object[]{instigator.getUsername(), user.getUsername()},
        "Account was enabled by user [{0}] for user [{1}].");

    return userRepo.enableUserAccount(user, msg, instigator);
  }

  @Override
  @Transactional
  public User disableAccount(final User user, final User instigator) {
    Assert.notNull(user, "[Assertion Failed] - argument [user] cannot be null.");
    Assert.notNull(instigator, "[Assertion Failed] - argument [instigator] cannot be null.");

    String msg = messages.getMessage(
        MSG_ACCOUNT_DISABLED,
        new Object[]{instigator.getUsername(), user.getUsername()},
        "Account was disabled by user [{0}] for user [{1}].");

    return userRepo.disableUserAccount(user, msg, instigator);
  }

  @Override
  public boolean doesUsernameExist(final String username) {
    Assert.hasText(username, "[Assertion Failed] - argument [username] cannot be null or empty.");

    return userRepo.doesUsernameExist(username);
  }

  @Override
  @Transactional
  public User saveUser(final User saveUser, final User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, UsernameTakenException {

    Assert.notNull(saveUser, "[Assertion Failed] - argument [saveUser] cannot be null");
    Assert.notNull(user, "[Assertion Failed] - argument [user] cannot be null");

    if (saveUser.getUserUID() == 0) {
      saveUser.setPassword(passwordEncoder.encode(saveUser.getPassword()));
    }

    return userRepo.saveUser(saveUser, user);
  }

  @Override
  public User registerUser(User registerUser, User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, UsernameTakenException {

    Assert.notNull(registerUser, "[Assertion Failed] - argument [registerUser] cannot be null");
    Assert.notNull(user, "[Assertion Failed] - argument [user] cannot be null");

    if (registerUser.getUserUID() == 0) {
      registerUser.setPassword(passwordEncoder.encode(registerUser.getPassword()));
    }

    return userRepo.registerUser(registerUser, user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    return userRepo.getAllUsers();
  }

  @Override
  @Transactional(readOnly = true)
  public User getUserByUID(final long uid) {
    User user = userRepo.getUserByUID(uid);

    // retrieve SecurityPolicy for user
    SecurityPolicy securityPolicy = securityService.getSecurityPolicy(user);
    user.setSecurityPolicy(securityPolicy);

    user.setAddresses(contactRepo.getAddressesForPerson(user));
    user.setPhoneNumbers(contactRepo.getPhoneNumbersForPerson(user));
    user.setEmailAddresses(contactRepo.getEmailAddressesForPerson(user));

    return user;
  }

  @Override
  public Optional<User> getUserByPersonId(long personId) {
    Optional<User> checkUser = userRepo.getUserByPersonId(personId);

    if (checkUser.isPresent()) {
      User user = checkUser.get();

      // retrieve SecurityPolicy for user
      SecurityPolicy securityPolicy = securityService.getSecurityPolicy(user);
      user.setSecurityPolicy(securityPolicy);
    }

    return checkUser;
  }

  @Override
  @Transactional(readOnly = true)
  public User getProfileUser(final User user) {
    // retrieve SecurityPolicy for user
    SecurityPolicy securityPolicy = securityService.getSecurityPolicy(user);
    user.setSecurityPolicy(securityPolicy);

    user.setAddresses(contactRepo.getAddressesForPerson(user));
    user.setPhoneNumbers(contactRepo.getPhoneNumbersForPerson(user));
    user.setEmailAddresses(contactRepo.getEmailAddressesForPerson(user));

    return user;
  }
}
