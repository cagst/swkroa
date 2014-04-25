package com.cagst.swkroa.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserService;
import com.cagst.swkroa.web.util.WebAppUtils;

/**
 * Handler class that gets called upon successful authentication. Used to determine if the user
 * needs to change their password.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 */
public final class SigninSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	private final UserService userService;

	/**
	 * Primary Constructor used to create an instance of <i>SigninSuccessHandler</i>.
	 * 
	 * @param userService
	 *          The {@link UserService} to user. to retrieve/update {@link User Users}.
	 */
	public SigninSuccessHandler(final UserService userService) {
		this.userService = userService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.authentication.AuthenticationSuccessHandler#
	 * onAuthenticationSuccess(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication auth) throws IOException, ServletException {

		if (auth.getPrincipal() != null && auth.getPrincipal() instanceof User) {
			User user = (User) auth.getPrincipal();
			String ipAddress = null;

			if (auth.getDetails() != null && auth.getDetails() instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails authDetails = (WebAuthenticationDetails) auth.getDetails();
				ipAddress = authDetails.getRemoteAddress();
			}

			user = userService.signinSuccessful(user, ipAddress);
			WebAppUtils.setUser(user);

			// update our session timeout
			HttpSession session = request.getSession();
			if (session != null) {
				session.setMaxInactiveInterval(user.getSecurityPolicy().getTimeoutPeriodInMinutes() * 60);
			}
		}

		super.onAuthenticationSuccess(request, response, auth);
	}
}
