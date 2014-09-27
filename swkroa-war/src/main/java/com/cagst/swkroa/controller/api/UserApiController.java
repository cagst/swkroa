package com.cagst.swkroa.controller.api;

/**
 * Handles and retrieves {@link User} objects depending upon the URI template.
 *
 * @author Craig Gaskill
 *
 * @version 1.0.0
 *
 */

import com.cagst.swkroa.exception.ResourceNotFoundException;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserService;
import com.cagst.swkroa.user.UsernameTakenException;
import com.cagst.swkroa.web.util.WebAppUtils;
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

  /**
   * Handles the request and unlocks the User associated with the specified id.
   *
   * @param userUID
   *     The unique identifier of the user to unlock.
   *
   * @return A JSON representation of the {@link User} after it has been unlocked.
   */
  @RequestMapping(value = "/api/users/{userUID}/unlock", method = RequestMethod.PUT)
  @ResponseBody
  public User unlockUser(final @PathVariable long userUID) {
    LOGGER.info("Received request to unlock user [{}]", userUID);

    return userService.unlockAccount(userUID, WebAppUtils.getUser());
  }

  /**
   * Handles the request and enables the User associated with the specified id.
   *
   * @param userUID
   *     The unique identifier of the user to enable.
   *
   * @return A JSON representation of the {@link User} after it has been enabled.
   */
  @RequestMapping(value = "/api/users/{userUID}/enable", method = RequestMethod.PUT)
  @ResponseBody
  public User enableUser(final @PathVariable long userUID) {
    LOGGER.info("Received request to enable user [{}]", userUID);

    return userService.enableAccount(userUID, WebAppUtils.getUser());
  }

  /**
   * Handles the request and disables the User associated with the specified id.
   *
   * @param userUID
   *     The unique identifier of the user to disable.
   *
   * @return A JSON representation of the {@link User} after it has been disable.
   */
  @RequestMapping(value = "/api/users/{userUID}/disable", method = RequestMethod.PUT)
  @ResponseBody
  public User disableUser(final @PathVariable long userUID) {
    LOGGER.info("Received request to disable user [{}]", userUID);

    return userService.disableAccount(userUID, WebAppUtils.getUser());
  }

  /**
   * Handles the request and resets the User's password associated with the specified id.
   *
   * @param userUID
   *     The unique identifier of the user to reset the password for.
   *
   * @return A JSON representation of the {@link User} after it has had its password reset.
   */
  @RequestMapping(value = "/api/users/{userUID}/resetpassword", method = RequestMethod.PUT)
  @ResponseBody
  public User resetUserPassword(final @PathVariable long userUID) {
    LOGGER.info("Received request to reset user [{}] password", userUID);

    try {
      User user = userService.getUserByUID(userUID);
      return userService.resetPassword(user, WebAppUtils.getUser());
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
