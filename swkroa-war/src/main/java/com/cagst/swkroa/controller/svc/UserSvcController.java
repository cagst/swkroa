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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	@RequestMapping(value = "/svc/user", method = RequestMethod.GET)
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
}
