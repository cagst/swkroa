package com.cagst.swkroa.controller.web;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import com.cagst.swkroa.audit.AuditEventType;
import com.cagst.swkroa.audit.annotation.Auditable;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserService;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles and retrieves the authentication page(s) depending on the URI template.
 *
 * @author Craig Gaskill
 */
@Controller
@RequestMapping(value = "auth")
public class AuthenticationController {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

  private static final String CHANGING_PWD_ERR = "changingpwdError";

  private UserService userService;

  @Inject
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  /**
   * Handles and retrieves the Sign-in page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "signin", method = RequestMethod.GET)
  public String getSigninPage() {
    LOGGER.info("Received request to show signin page.");

    return "auth/signin";
  }

  /**
   * Handles and retrieves the Signed Out page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "signedout", method = RequestMethod.GET)
  public String getSignedoutPage() {
    LOGGER.info("Received request to show signed out page.");

    // User user = WebAppUtils.getUser();
    // userService.signedOut(user);

    return "auth/signedout";
  }

  /**
   * Handles and retrieves the Timed Out page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "timedout", method = RequestMethod.GET)
  @Auditable(eventType = AuditEventType.SECURITY, action = Auditable.ACTION_TIMED_OUT)
  public String getTimeoutPage() {
    LOGGER.info("Received request to show timed out page.");

    User user = WebAppUtils.getUser();
    userService.timedOut(user);

    return "auth/timedout";
  }

  /**
   * Handles and retrieves the Access Denied (Not Authorized) page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "notauthorized", method = RequestMethod.GET)
  public String getAccessDeniedPage() {
    LOGGER.info("Received request to show access denied / not authorized page.");

    return "auth/notauthorized";
  }

  /**
   * Handles and retrieves the Change Password page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "changepwd", method = RequestMethod.GET)
  public String getChangePasswordPage() {
    LOGGER.info("Received request to show change password page.");

    return "auth/changepwd";
  }

  /**
   * Handles and retrieves the Register page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "register", method = RequestMethod.GET)
  public String getRegisterPage() {
    LOGGER.info("Received request to show the register page.");

    return "auth/register";
  }

  /**
   * Handles the request to actually change the password.
   *
   * @param oldPassword
   *     The old (current) password.
   * @param newPassword
   *     The password we want to change to.
   * @param confirmPassword
   *     The password we want to change to (entered again for confirmation purposes).
   */
  @RequestMapping(value = "changepwd", method = RequestMethod.POST)
  public void changePassword(final @RequestParam String oldPassword,
                             final @RequestParam String newPassword,
                             final @RequestParam String confirmPassword,
                             final HttpServletRequest request,
                             final HttpServletResponse response)
      throws IOException {

    User user = WebAppUtils.getUser();
    if (user == null) {
      LOGGER.error("Unable to determine current user.");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    LOGGER.info("Changing password for user [{}]", user.getUsername());

    HttpSession session = request.getSession();
    session.removeAttribute(CHANGING_PWD_ERR);

    if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword) || StringUtils.isBlank(confirmPassword)) {
      session.setAttribute(CHANGING_PWD_ERR, "Please complete all required fields!");

      WebAppUtils.redirectToUrl(request, response, "/auth/changepwd");
      return;
    }

    try {
      user = userService.changePassword(user, oldPassword, newPassword, confirmPassword);
      WebAppUtils.setUser(user);
    } catch (AuthenticationException ex) {
      LOGGER.debug("Unable to change password due to [{}]", ex.getLocalizedMessage());
      session.setAttribute(CHANGING_PWD_ERR, ex.getLocalizedMessage());

      WebAppUtils.redirectToUrl(request, response, "/auth/changepwd");
      return;
    }

    RequestCache requestCache = new HttpSessionRequestCache();
    SavedRequest savedRequest = requestCache.getRequest(request, response);
    if (savedRequest == null) {
      WebAppUtils.redirectToUrl(request, response, "/");
    } else {
      WebAppUtils.redirectToUrl(request, response, savedRequest.getRedirectUrl());
    }
  }
}
