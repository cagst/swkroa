package com.cagst.swkroa.controller.svc;

/**
 * Handles and retrieves {@link User} objects depending upon the URI template.
 *
 * @author Craig Gaskill
 *
 * @version 1.0.0
 *
 */

import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserService;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public final class UserSvcController {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserSvcController.class);

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
   * @param userUID A {@link long} that uniquely identifies the user to retrieve.
   * @return A JSON representation of the {@link User} associated with the specified id.
   */
  @RequestMapping(value = "/api/users/{userUID}", method = RequestMethod.GET)
  @ResponseBody
  public User getUser(final @PathVariable long userUID) {
    LOGGER.info("Received request to retrieve the user [{}].", userUID);

    return userService.getUserByUID(userUID);
  }

  /**
   * Handles the request and unlocks the {@link User} specified.
   *
   * @param unlockUser The {@link User} to unlock.
   * @return A JSON representation of the {@link User} after it has been unlocked.
   */
  @RequestMapping(value = "/api/users/unlock", method = RequestMethod.PUT)
  @ResponseBody
  public User unlockUser(final @RequestBody User unlockUser) {
    LOGGER.info("Received request to unlock user [{}]", unlockUser.getUsername());

    return userService.unlockAccount(unlockUser, WebAppUtils.getUser());
  }
}
