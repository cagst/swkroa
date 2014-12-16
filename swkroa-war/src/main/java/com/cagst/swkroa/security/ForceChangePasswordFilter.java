package com.cagst.swkroa.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.cagst.swkroa.user.User;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This filter will for users who's password is temporary or has expired to change their password
 * before they can proceed.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public final class ForceChangePasswordFilter extends OncePerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(ForceChangePasswordFilter.class);

  private String changePasswordUrl = null;

  public void setChangePasswordUrl(final String url) {
    changePasswordUrl = url;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.
   * HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
   */
  @Override
  protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                  final FilterChain filterChain) throws ServletException, IOException {

    if (StringUtils.equals("GET", request.getMethod())) {
      User user = WebAppUtils.getUser();
      if (user != null && (user.isPasswordTemporary() || user.isPasswordExpired())) {
        logger.debug("User [{}] password is either temporary or has expired. Redirecting to ChangePassword page.",
            user.getUsername());

        String requestURL = request.getRequestURL().toString();
        if (StringUtils.indexOf(requestURL, changePasswordUrl) == -1) {
          WebAppUtils.redirectToUrl(request, response, changePasswordUrl);
          return;
        }
      }
    }

    filterChain.doFilter(request, response);
  }
}
