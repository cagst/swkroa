package com.cagst.swkroa.controller.web;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserService;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles and retrieves the Home page depending on the URI template.
 *
 * @author Craig Gaskill
 */
@Controller
public final class HomeController {
  private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

  @Inject
  private UserService userService;

  /**
   * Handles and retrieves the Home page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String getHomePage(HttpServletRequest request) {
    LOGGER.info("Received request to show the home page.");

    if (request.isUserInRole("ROLE_MEMBER")) {
      return "redirect:member/home";
    } else {
      return "home";
    }
  }

  /**
   * Handles and retrieves the Profile page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "/profile", method = RequestMethod.GET)
  public String getProfilePage() {
    LOGGER.info("Received request to show the profile page.");

    return "profile";
  }

  @RequestMapping(value = "/profile", method = RequestMethod.POST)
  public ResponseEntity<User> saveProfile(final @RequestBody User user, final HttpServletRequest request) {
    LOGGER.info("Received request to save profile.");

    try {
      // Save the user
      User savedUser = userService.saveUser(user, WebAppUtils.getUser());

      return new ResponseEntity<>(savedUser, HttpStatus.OK);
    } catch (OptimisticLockingFailureException ex) {
      return new ResponseEntity<>(user, HttpStatus.CONFLICT);
    } catch (Exception ex) {
      return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
