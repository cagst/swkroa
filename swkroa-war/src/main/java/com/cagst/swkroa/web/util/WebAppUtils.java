package com.cagst.swkroa.web.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cagst.swkroa.user.User;

/**
 * This is a utility class used to share logic across the web application.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public final class WebAppUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebAppUtils.class);

	/**
	 * Will attempt to retrieve the {@link User} that is currently logged in from the spring security
	 * context.
	 * <p>
	 * <b>NOTE: </b>This may fail and return a <code>null</code> user if the security context has
	 * already been cleared by spring security.!
	 * 
	 * @return The {@link User} that is currently logged in, <code>null</code> if the security context
	 *         has already been cleared by spring security.
	 */
	public static User getUser() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext == null) {
			LOGGER.warn("The SecurityContextHolder SecurityContext is null.");
			return null;
		}

		Authentication auth = securityContext.getAuthentication();
		if (auth == null) {
			LOGGER.warn("The SecurityContextHolder Authentication is null.");
		}

		if (auth == null) {
			LOGGER.warn("The Authentication object is null.");
			return null;
		}

		if (!(auth.getPrincipal() instanceof User)) {
			LOGGER.warn("The Authentication Principal is not a User.");
			return null;
		}

		return (User) auth.getPrincipal();
	}

	/**
	 * Will attempt to set the {@link User} into the spring security context.
	 * <p>
	 * <b>NOTE: </b>This may fail if the spring security context has already been cleared by spring
	 * security. Check the return value to determine if the User was set into the security context
	 * appropriately.
	 * 
	 * @param user
	 *          The {@link User} to set into the spring security context.
	 */
	public static void setUser(final User user) {
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	/**
	 * Will perform a redirect to the specified url.
	 * 
	 * @param request
	 *          The current {@link HttpServetRequest}.
	 * @param response
	 *          The current {@link HttpServletResponse}.
	 * @param url
	 *          The url to redirect to.
	 * 
	 * @throws IOException
	 */
	public static void redirectToUrl(final HttpServletRequest request, final HttpServletResponse response,
			final String url) throws IOException {

		String redirectTo = url;

		if (!StringUtils.startsWith(url, "http://") && !StringUtils.startsWith(url, "https://")) {
			redirectTo = request.getContextPath() + url;
		}

		response.sendRedirect(response.encodeRedirectURL(redirectTo));
	}
}
