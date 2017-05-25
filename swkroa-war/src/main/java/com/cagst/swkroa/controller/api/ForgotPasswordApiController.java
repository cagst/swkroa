package com.cagst.swkroa.controller.api;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles and retrieves requests for Forgot Password.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping(value = "/api/forgotPassword")
public class ForgotPasswordApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(ForgotPasswordApiController.class);

  private final ResourceBundle resourceBundle;

  private UserRepository userRepo;

  /**
   * Default Constructor
   */
  public ForgotPasswordApiController() {
    resourceBundle = ResourceBundle.getBundle("properties.i18n.auth", Locale.US);
  }

  @Inject
  public void setUserRepository(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @RequestMapping(value = "/identification/{username}", method = RequestMethod.GET)
  public ResponseEntity<String> registerIdentification(@PathVariable(value = "username") String username) {
    LOGGER.info("Received request to identify user for Username [{}]", username);

    Optional<User> checkUser = userRepo.getUserByUsername(username);
    if (!checkUser.isPresent()) {
      String msg = resourceBundle.getString("signin.forgotPassword.identify.notfound");
      return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
