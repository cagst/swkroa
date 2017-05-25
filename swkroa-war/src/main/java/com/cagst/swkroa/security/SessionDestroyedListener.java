package com.cagst.swkroa.security;

import javax.inject.Inject;

import com.cagst.swkroa.user.UserService;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

/**
 * Implements the {@link ApplicationListener} interface for session destroyed (signed out / timed out) event.
 *
 * @author Craig Gaskill
 */
@Component
public class SessionDestroyedListener implements ApplicationListener<SessionDestroyedEvent> {
  private final UserService userService;

  @Inject
  public SessionDestroyedListener(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void onApplicationEvent(SessionDestroyedEvent event) {
    userService.signedOut(WebAppUtils.getUser());
  }
}
