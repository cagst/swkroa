package com.cagst.swkroa.user;

import com.cagst.swkroa.audit.AuditEventType;
import com.cagst.swkroa.audit.annotation.AuditInstigator;
import com.cagst.swkroa.audit.annotation.AuditMessage;
import com.cagst.swkroa.audit.annotation.Auditable;
import com.cagst.swkroa.role.RoleRepository;
import com.cagst.swkroa.security.SecurityPolicy;
import com.cagst.swkroa.security.SecurityService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * User Service that provides authentication for SWKROA.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@Service("userService")
public class UserServiceImpl implements UserService, MessageSourceAware {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserRepository userRepo;
  private final RoleRepository roleRepo;
  private final SecurityService securityService;
  private final PasswordEncoder passwordEncoder;

  private MessageSourceAccessor messages;

  /**
   * Primary Constructor used to create an instance of <i>UserServiceImpl</i>.
   *
   * @param userRepo        The {@link UserRepository} to use to retrieve / update {@link User Users}.
   * @param roleRepo        The {@link RoleRepository} to use to retrieve the roles / privileges for the {@link User}.
   * @param securityService The {@link SecurityService} to use.
   * @param passwordEncoder The {@link PasswordEncoder} to use to check / encode user passwords.
   */
  public UserServiceImpl(final UserRepository userRepo,
                         final RoleRepository roleRepo,
                         final SecurityService securityService,
                         final PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.roleRepo = roleRepo;
    this.securityService = securityService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void setMessageSource(final MessageSource messageSource) {
    messages = new MessageSourceAccessor(messageSource);
  }

  @Override
  @Transactional
  // @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_SIGNIN_ATTEMP)
  public UserDetails loadUserByUsername(final @AuditInstigator String userName) throws UsernameNotFoundException,
      DataAccessException {

    LOGGER.info("Calling loadUserByUsername for [{}].", userName);

    User user = userRepo.getUserByUsername(userName);
    if (user == null) {
      throw new UsernameNotFoundException(null);
    }

    user = userRepo.signinAttempt(user);

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
        String msg = messages.getMessage("com.cagst.swkroa.signin.message.attempts.exceeded",
            "The number of allowed sign-in attempts has been exceeded.  Account has been locked.");
        user = userRepo.lockUserAccount(user, msg);
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
    Assert.hasText(confirmPassword,
        "[Assertion Failed] - argument [confirmPassword] must not be null, empty, or blank.");

    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      LOGGER.warn("Old password is not valid!");
      throw new BadCredentialsException(messages.getMessage("com.cagst.swkroa.changepwd.password.err.wrongold",
          "The old password is incorrect."));
    }

    if (!StringUtils.equals(newPassword, confirmPassword)) {
      LOGGER.warn("New password does not match confirmation password!");
      throw new BadCredentialsException(messages.getMessage("com.cagst.swkroa.changepwd.password.err.mismatch",
          "The confirmation password does not match the new password."));
    }

    if (StringUtils.equals(newPassword, user.getUsername())) {
      LOGGER.warn("New password matches username!");
      throw new BadCredentialsException(messages.getMessage("com.cagst.swkroa.changepwd.password.err.username",
          "The new password cannot be the same as your user name."));
    }

    String checkNewPassword = passwordEncoder.encode(newPassword);
    if (StringUtils.equals(user.getPassword(), checkNewPassword)) {
      LOGGER.warn("New password is the same as the old password!");
      throw new BadCredentialsException(messages.getMessage("com.cagst.swkroa.changepwd.password.err.same",
          "The new password is the same as the old password."));
    }

    String msg = messages.getMessage("com.cagst.swkroa.changepwd.audit.message", new Object[]{user.getUsername()},
        "Password changed for user [{0}].");

    return userRepo.changeUserPassword(user, checkNewPassword, msg);
  }

  @Override
  @Transactional
  public User unlockAccount(final User unlockUser, final User user) {
    Assert.notNull(unlockUser, "[Assertion Failed] - argument [unlockUser] cannot be null.");
    Assert.notNull(user, "[Assertion Failed] - argument [user] cannot be null.");

    String msg = null;

    if (unlockUser.equals(user)) {
      msg = messages.getMessage("com.cagst.swkroa.unlock.auto.audit.message",
          new Object[]{unlockUser.getUsername()}, "Account was automatically unlocked for user [{0}].");
    } else {
      msg = messages.getMessage("com.cagst.swkroa.unlock.manual.audit.message", new Object[]{user.getUsername(),
          unlockUser.getUsername()}, "Account was manually unlocked by user [{0}] for user [{1}].");
    }

    return userRepo.unlockUserAccount(unlockUser, msg, user);
  }

  @Override
  public boolean doesUsernameExist(final String username) {
    Assert.hasText(username, "[Assertion Failed] - argument [username] cannot be null or empty.");

    return userRepo.doesUsernameExist(username);
  }

  @Override
  @Transactional
  public User saveUser(final User builder, final User user) {
    Assert.notNull(builder, "[Assertion Failed] - argument [builder] cannot be null");
    Assert.notNull(user, "[Assertion Failed] - argument [user] cannot be null");

    return userRepo.saveUser(builder, user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    return userRepo.getAllUsers();
  }
}
