package com.cagst.swkroa.controller.api;

/**
 * Handles and retrieves {@link User} objects depending upon the URI template.
 *
 * @author Craig Gaskill
 *
 * @version 1.0.0
 *
 */

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.cagst.swkroa.exception.BadRequestException;
import com.cagst.swkroa.exception.ResourceNotFoundException;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserService;
import com.cagst.swkroa.user.UsernameTakenException;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@RestController
public final class UserApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserApiController.class);

  private static final String ACTION_UNLOCK = "unlock";
  private static final String ACTION_ENABLE = "enable";
  private static final String ACTION_DISABLE = "disable";
  private static final String ACTION_RESETPWD = "resetpwd";

  private final UserService userService;

  @Inject
  public UserApiController(final UserService userService) {
    this.userService = userService;
  }
  /**
   * Handles the request and retrieves the active {@link User Users} within the system.
   *
   * @return A JSON representation of the active {@link User Users} within the system.
   */
  @RequestMapping(value = "/api/users", method = RequestMethod.GET)
  public List<User> getUsers() {
    LOGGER.info("Received request to retrieve user listing.");

    List<User> users = userService.getAllUsers();
    Collections.sort(users);

    // remove the System user account
    for (User user : users) {
      if (UserService.SYSTEM_USER.equals(user.getUsername())) {
        users.remove(user);
        break;
      }
    }

    return users;
  }

  /**
   * Handles the request and retrieves the {@link User} associated with the specified id.
   *
   * @param userUID
   *     A {@link long} that uniquely identifies the user to retrieve.
   *
   * @return A JSON representation of the {@link User} associated with the specified id.
   */
  @RequestMapping(value = "/api/users/{userUID}", method = RequestMethod.GET)
  public User getUser(final @PathVariable("userUID") long userUID) {
    LOGGER.info("Received request to retrieve the user [{}].", userUID);

    try {
      return userService.getUserByUID(userUID);
    } catch (EmptyResultDataAccessException ex) {
      throw new ResourceNotFoundException(ex);
    } catch (IncorrectResultSizeDataAccessException ex) {
      throw new ResourceNotFoundException(ex);
    }
  }

  @RequestMapping(value = "/api/users/{userUID}", method = RequestMethod.PUT)
  public User updateUser(final @PathVariable("userUID") long userUID, final @RequestParam String action) {
    LOGGER.info("Received request to update the user [{}] for [{}]", userUID, action);

    if (StringUtils.isBlank(action)) {
      throw new BadRequestException("An 'action' must be specified.");
    }

    if (!ACTION_UNLOCK.equalsIgnoreCase(action) &&
        !ACTION_ENABLE.equalsIgnoreCase(action) &&
        !ACTION_DISABLE.equalsIgnoreCase(action) &&
        !ACTION_RESETPWD.equalsIgnoreCase(action)) {
      throw new BadRequestException("The 'action' [" + action + "] is not recognized.");
    }

    try {
      User user = userService.getUserByUID(userUID);
      if (ACTION_UNLOCK.equalsIgnoreCase(action)) {
        return userService.unlockAccount(user, WebAppUtils.getUser());
      } else if (ACTION_ENABLE.equalsIgnoreCase(action)) {
        return userService.enableAccount(user, WebAppUtils.getUser());
      } else if (ACTION_DISABLE.equalsIgnoreCase(action)) {
        return userService.disableAccount(user, WebAppUtils.getUser());
      } else if (ACTION_RESETPWD.equalsIgnoreCase(action)) {
        return userService.resetPassword(user, WebAppUtils.getUser());
      } else {
        return null;
      }
    } catch (EmptyResultDataAccessException ex) {
      throw new ResourceNotFoundException(ex);
    } catch (IncorrectResultSizeDataAccessException ex) {
      throw new ResourceNotFoundException(ex);
    }
  }

  /**
   * Handles the request and persists the {@link User} to persistent storage. Called from the Add/Edit User
   * page when adding/editing a user.
   *
   * @param user
   *     The {@link User} to persist.
   *
   * @return The {@link User} after it has been persisted.
   */
  @RequestMapping(value = "/api/users", method = RequestMethod.POST)
  public ResponseEntity<User> saveUser(final @RequestBody User user, final HttpServletRequest request) {
    LOGGER.info("Received request to save membership [{}]", user.getUserUID());

    try {
      // save the user
      User savedUser = userService.saveUser(user, WebAppUtils.getUser());

      // specify the location of the resource
      UriComponents locationUri = ServletUriComponentsBuilder
          .fromContextPath(request)
          .path("/api/users/{userUID}")
          .buildAndExpand(savedUser.getUserUID());

      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(locationUri.toUri());

      if (user.getUserUID() == 0) {
        return new ResponseEntity<User>(savedUser, headers, HttpStatus.CREATED);
      } else {
        return new ResponseEntity<User>(savedUser, headers, HttpStatus.OK);
      }
    } catch (UsernameTakenException ex) {
      LOGGER.debug("Unable to save user due to [{}]", ex.getLocalizedMessage());
      throw new BadRequestException(ex.getLocalizedMessage(), ex);
    }
  }

  /**
   * Handles the request and checks if the specified username is already being used.
   *
   * @param username
   *     The username to check to see if it already exists.
   *
   * @return {@code true} if the username is being used, {@code false} otherwise.
   */
  @RequestMapping(value = "/api/users/{username}/exists", method = RequestMethod.GET)
  public boolean usernameExists(final @PathVariable("username") String username) {
    LOGGER.info("Received request to check if username [{}] already exists", username);

    return userService.doesUsernameExist(username);
  }

  /**
   * Handles the request and retrieves the {@link User} currently signed in.
   *
   * @return A JSON representation of the {@link User} currently signed in.
   */
  @RequestMapping(value = "/api/profile", method = RequestMethod.GET)
  public User getProfileUser() {
    LOGGER.info("Received request to retrieve the profile user.");

    return userService.getProfileUser(WebAppUtils.getUser());
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
  @RequestMapping(value = "/api/changepwd", method = RequestMethod.POST)
  public ResponseEntity<User> changePassword(final @RequestParam String oldPassword,
                                             final @RequestParam String newPassword,
                                             final @RequestParam String confirmPassword)
      throws IOException {

    User user = WebAppUtils.getUser();
    if (user == null) {
      LOGGER.error("Unable to determine current user.");
      return new ResponseEntity<User>(user, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    LOGGER.info("Changing password for user [{}]", user.getUsername());

    try {
      user = userService.changePassword(user, oldPassword, newPassword, confirmPassword);
      WebAppUtils.setUser(user);

      return new ResponseEntity<User>(user, HttpStatus.OK);
    } catch (AuthenticationException ex) {
      LOGGER.debug("Unable to change password due to [{}]", ex.getLocalizedMessage());
      throw new BadRequestException(ex.getLocalizedMessage(), ex);
    }
  }
}
