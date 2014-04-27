package com.cagst.swkroa.security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

import com.cagst.swkroa.user.UserService;

/**
 * Implements the {@link ApplicationListener} interface for authentication failure.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public final class AuthenticationFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {
	private final UserService userService;

	/**
	 * Primary Constructor used to create an instance of <i>UserService</i>.
	 * 
	 * @param userService
	 *          The {@link UserService} to use for sign-in failures.
	 */
	public AuthenticationFailureListener(final UserService userService) {
		this.userService = userService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context
	 * .ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(final AbstractAuthenticationFailureEvent event) {
		userService.signinFailure(event.getAuthentication().getName(), event.getException().getLocalizedMessage());
	}
}