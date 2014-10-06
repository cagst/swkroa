package com.cagst.swkroa.controller.web;

import javax.servlet.http.HttpServletRequest;

import ch.qos.logback.classic.LoggerContext;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserService;
import com.cagst.swkroa.user.UsernameTakenException;
import com.cagst.swkroa.web.util.WebAppUtils;
import net.sf.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Handles and retrieves the Home page depending on the URI template.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@Controller
public final class HomeController {
  private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

  @Autowired
  private CacheManager cacheManager;

  @Autowired
  private UserService userService;

  /**
   * Handles and retrieves the Home page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  // @PreAuthorize("isAuthenticated()")
  public String getHomePage() {
    LOGGER.info("Received request to show the home page.");

    return "home";
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

      return new ResponseEntity<User>(savedUser, HttpStatus.OK);
    } catch (OptimisticLockingFailureException ex) {
      return new ResponseEntity<User>(user, HttpStatus.CONFLICT);
    } catch (Exception ex) {
      return new ResponseEntity<User>(user, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Handles and retrieves the Console page.
   *
   * @return The name of the JSP page.
   */
  @RequestMapping(value = "console", method = RequestMethod.GET)
  public ModelAndView getConsolePage() {
    LOGGER.info("Received request to show the console page.");

    ModelAndView mav = new ModelAndView("console");

    Collection<String> cacheNames = cacheManager.getCacheNames();
    List<Cache> caches = new ArrayList<Cache>();
    for (String cacheName : cacheNames) {
      caches.add((Cache) cacheManager.getCache(cacheName).getNativeCache());
    }

    LoggerContext logCtx = (LoggerContext) LoggerFactory.getILoggerFactory();
    mav.addObject("loggers", logCtx.getLoggerList());
    mav.addObject("caches", caches);

    return mav;
  }
}
