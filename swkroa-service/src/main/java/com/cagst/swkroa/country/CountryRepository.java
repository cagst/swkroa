package com.cagst.swkroa.country;

import java.util.List;

/**
 * Definition of a repository that retrieves and persists {@link Country}, {@link State}, and {@link County} objects.
 *
 * @author Craig Gaskill
 */
public interface CountryRepository {
  /**
   * Retrieves a {@link List} of active {@link Country Countries} defined within the system.
   *
   * @return A {@link List} of active {@link Country Countries} defined within the system.
   */
  List<Country> getActiveCountries();

  /**
   * Retrieves a {@link List} of active {@link State States} defined within the system.
   *
   * @return A {@link List} of active {@link State States} defined within the system.
   */
  List<State> getActiveStates();

  /**
   * Retrieves a {@link List} of active {@link State States} for the specified Country defined within the system.
   *
   * @param countryCode
   *    A {@link String} that represents the Country to retrieves states for.
   *
   * @return A {@link List} of active {@link State States} for the specified Country.
   */
  List<State> getActiveStatesForCountry(String countryCode);

  /**
   * Retrieves a {@link List} of active {@link County Counties} defined within the system.
   *
   * @return A {@link List} of active {@link County Counties} defined within the system.
   */
  List<County> getActiveCounties();

  /**
   * Retrieves a {@link List} of {@link County Counties} associated with the specified State.
   *
   * @param stateCode
   *     A {@link String} that represents the State to retrieve a {@link List} of
   *     {@link County Counties} for.
   *
   * @return The {@link List} of {@link County Counties} associated with the specified State.
   */
  List<County> getCountiesForState(String stateCode);

  /**
   * Retrieves the {@link County} associated with the specified unique identifier.
   *
   * @param uid
   *     A {@link long} that uniquely identifies the {@link County} to retrieve.
   *
   * @return The {@link County} associated with the specified uid, {@code null} if no County was
   * found.
   */
   County getCountyByUID(long uid);

  /**
   * Retrieves the {@link County} associated with the specified state and county.
   *
   * @param state
   *     A {@link String} that represents the state the {@link County} is associated with.
   * @param county
   *     A {@link String} that represents the county to retrieve.
   *
   * @return The {@link County} associated to the specified state and county.
   */
   County getCountyByStateAndCode(String state, String county);
}
