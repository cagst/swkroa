package com.cagst.swkroa.security;

import javax.inject.Inject;

import com.cagst.swkroa.user.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;

/**
 * Implements the {@link ApplicationListener} interface for authentication failure.
 *
 * @author Craig Gaskill
 */
@Component
public class AuthenticationFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {
  private final UserService userService;

  @Inject
  public AuthenticationFailureListener(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
    userService.signinFailure(event.getAuthentication().getName(), event.getException().getLocalizedMessage());
  }
}
