package com.cagst.swkroa.controller.api;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import com.cagst.swkroa.country.CountryRepository;
import com.cagst.swkroa.country.County;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles and retrieves {@link County} objects depending on the URI template.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping(value = "/api/counties")
public final class CountyApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(CountyApiController.class);

  private final CountryRepository countryRepo;

  @Inject
  public CountyApiController(CountryRepository countryRepo) {
    this.countryRepo = countryRepo;
  }

  /**
   * Handles the request and retrieves the active Counties within the system.
   *
   * @return A JSON representation of the active Counties within the system.
   */
  @RequestMapping(method = RequestMethod.GET)
  public List<County> getActiveCounties() {
    LOGGER.info("Received request to retrieve active counties.");

    List<County> counties = countryRepo.getActiveCounties();
    Collections.sort(counties);

    return counties;
  }
}
