package com.cagst.swkroa.county;

import java.util.Collection;

/**
 * Definition of a repository that retrieves and persists {@link Country} and {@link State} objects.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public interface CountyRepository {

	/**
	 * Retrieves a {@link Collection} of active {@link County Counties} defined within the system.
	 * 
	 * @return A {@link Collection} of active {@link County Counties} defined within the system.
	 */
	public Collection<County> getActiveCounties();

	/**
	 * Retrieves a {@link Collection} of {@link County Counties} associated with the specified
	 * {@link State}.
	 * 
	 * @param stateCode
	 *          A {@link String} that represents the State to retrieve a {@link Collection} of
	 *          {@link County Counties} for.
	 * 
	 * @return The {@link Collection} of {@link County Counties} associated with the specified
	 *         {@link State}.
	 */
	public Collection<County> getCountiesForState(final String stateCode);

	/**
	 * Retrieves the {@link County} associated with the specified unique identifier.
	 * 
	 * @param uid
	 *          A {@link long} that uniquely identifies the {@link County} to retrieve.
	 * 
	 * @return The {@link County} associated with the specified uid, {@code null} if no County was
	 *         found.
	 */
	public County getCountyByUID(final long uid);

	/**
	 * Retrieves the {@link County} associated with the specified state and county.
	 * 
	 * @param state
	 *          A {@link String} that represents the state the {@link County} is associated with.
	 * @param county
	 *          A {@link String} that represents the county to retrieve.
	 * 
	 * @return The {@link County} associated to the specified state and county.
	 */
	public County getCountyByStateAndCode(final String state, final String county);
}
