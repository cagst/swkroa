package com.cagst.swkroa.controller.api;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;

import com.cagst.swkroa.country.Country;
import com.cagst.swkroa.country.CountryRepository;
import com.cagst.swkroa.country.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles and retrieves {@link Country} and {@link State} objects depending on the URI template.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping(value = "/api/countries")
public class CountryApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(CountryApiController.class);

  private final CountryRepository countryRepo;

  @Inject
  public CountryApiController(CountryRepository countryRepo) {
    this.countryRepo = countryRepo;
  }

  /**
   * Handles the request and retrieves the Countries within the system.
   *
   * @return A JSON representation of the Countries within the system.
   */
  @RequestMapping(method = RequestMethod.GET)
  public List<Country> getCountries() {
    LOGGER.info("Received request to retrieve countries.");

    List<Country> countries = countryRepo.getActiveCountries();
    countries.sort(Comparator.comparing(Country::getCountryName));

    return countries;
  }

  /**
   * Handles the request and retrieves the States within the system.
   *
   * @return A JSON representation of the States within the system.
   */
  @RequestMapping(value = "/states", method = RequestMethod.GET)
  public List<State> getStates() {
    LOGGER.info("Received request to retrieve states");

    List<State> states = countryRepo.getActiveStates();
    states.sort(Comparator.comparing(State::getStateName));

    return states;
  }

  /**
   * Handles the request and retrieves the States within the system for the specific Country.
   *
   * @param countryCode
   *    A {@link String} that represents the Country to retrieve States for.
   *
   * @return A JSON representation of the States associated with the specified Country.
   */
  @RequestMapping(value = "/{countryCode}/states", method = RequestMethod.GET)
  public List<State> getStates(@PathVariable("countryCode") String countryCode) {
    LOGGER.info("Received request to retrieve states for [{}]", countryCode);

    List<State> states = countryRepo.getActiveStatesForCountry(countryCode);
    states.sort(Comparator.comparing(State::getStateName));

    return states;
  }
}
