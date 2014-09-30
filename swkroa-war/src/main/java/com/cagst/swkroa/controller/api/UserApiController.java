package com.cagst.swkroa.controller.api;

/**
 * Handles and retrieves {@link User} objects depending upon the URI template.
 *
 * @author Craig Gaskill
 *
 * @version 1.0.0
 *
 */

import com.cagst.swkroa.exception.BadRequestException;
import com.cagst.swkroa.exception.ResourceNotFoundException;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserService;
import com.cagst.swkroa.user.UsernameTakenException;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public final class UserApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserApiController.class);

  private static final String ACTION_UNLOCK   = "unlock";
  private static final String ACTION_ENABLE   = "enable";
  private static final String ACTION_DISABLE  = "disable";
  private static final String ACTION_RESETPWD = "resetpwd";

  @Autowired
  private UserService userService;

  /**
   * Handles the request and retrieves the active {@link User Users} within the system.
   *
   * @return A JSON representation of the active {@link User Users} within the system.
   */
  @RequestMapping(value = "/api/users", method = RequestMethod.GET)
  @ResponseBody
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
  @ResponseBody
  public User getUser(final @PathVariable long userUID) {
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
  @ResponseBody
  public User updateUser(final @PathVariable long userUID, final @RequestParam String action) {
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
   * Handles the request and checks if the specified username is already being used.
   *
   * @param username
   *      The username to check to see if it already exists.
   *
   * @return {@code true} if the username is being used, {@code false} otherwise.
   */
  @RequestMapping(value = "/api/users/{username}/exists", method = RequestMethod.GET)
  @ResponseBody
  public boolean usernameExists(final @PathVariable String username) {
    LOGGER.info("Received request to check if username [{}] already exists", username);

    return userService.doesUsernameExist(username);
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
  @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
  @ResponseBody
  public ResponseEntity<User> saveUser(final @RequestBody User user) {
    LOGGER.info("Received request to save membership [{}]", user.getUserUID());

    try {
      return new ResponseEntity<User>(userService.saveUser(user, WebAppUtils.getUser()), HttpStatus.OK);
    } catch (UsernameTakenException ex) {
      return new ResponseEntity<User>(user, HttpStatus.BAD_REQUEST);
    } catch (OptimisticLockingFailureException ex) {
      return new ResponseEntity<User>(user, HttpStatus.CONFLICT);
    } catch (Exception ex) {
      return new ResponseEntity<User>(user, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
