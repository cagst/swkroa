package com.cagst.swkroa.member;

import java.util.List;

import com.cagst.swkroa.user.User;
import org.joda.time.DateTime;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

/**
 * Definition of a repository that retrieves and persists {@link MemberType} objects.
 *
 * @author Craig Gaskill
 */
public interface MemberTypeRepository {
  /**
   * Retrieves a {@link MemberType} by its identifier that is in effect as of NOW.
   *
   * @param id
   *     A {@link long} that identifies the member type to retrieve.
   *
   * @return A {@link MemberType}, if one exists, that matches the specified id and is currently in
   * effect; otherwise, a {@code null} is returned.
   *
   * @throws EmptyResultDataAccessException
   *     if no MemberType was found.
   * @throws IncorrectResultSizeDataAccessException
   *     if more than 1 MemberType was found.
   */
  MemberType getMemberTypeByUID(long id) throws IncorrectResultSizeDataAccessException;

  /**
   * Retrieves a {@link MemberType} by its meaning that is in effect as of NOW.
   *
   * @param meaning
   *     A {@link String} that identifies the member type to retrieve.
   *
   * @return A {@link MemberType}, if one exists, that matches the specified meaning and is
   * currently in effect; otherwise, a {@code null} is returned.
   *
   * @throws EmptyResultDataAccessException
   *     if no MemberType was found.
   * @throws IncorrectResultSizeDataAccessException
   *     if more than 1 MemberType was found.
   */
  MemberType getMemberTypeByMeaning(String meaning) throws IncorrectResultSizeDataAccessException;

  /**
   * Retrieves a {@link MemberType} by its meaning that is in effect as of the specified effective
   * date time.
   *
   * @param meaning
   *     A {@link String} that identifies the member type to retrieve.
   * @param effectiveDateTime
   *     A {@link DateTime} that specifies the effective time to retrieve the member type for.
   *
   * @return A {@link MemberType}, if one exists, that matches the specified meaning and is in
   * effect as of the time specified; otherwise, a {@code null} is returned.
   *
   * @throws EmptyResultDataAccessException
   *     if no MemberType was found.
   * @throws IncorrectResultSizeDataAccessException
   *     if more than 1 MemberType was found.
   */
  MemberType getMemberTypeByMeaningAsOf(String meaning, DateTime effectiveDate)
      throws IncorrectResultSizeDataAccessException;

  /**
   * Retrieves all active {@link MemberType MemberTypes} in the system that are currently in effect
   * as of NOW.
   *
   * @return A {@link List} of active {@link MemberType MemberTypes} in the system that are
   * currently effective.
   */
  List<MemberType> getActiveMemberTypes();

  /**
   * Retrieves all active {@link MemberType MemberTypes} in the system that were in effect as of the
   * time specified
   *
   * @param effectiveDateTime
   *     The {@link DateTime} to retrieve member types for.
   *
   * @return A {@link List} of active {@link MemberType MemberTypes} in the system that were in
   * effect as of the time specified.
   */
  List<MemberType> getActiveMemberTypesAsOf(DateTime effectiveDate);

  /**
   * Retrieves all active {@link MemberType MemberTypes} in the system that correspond to the specified MemberType.
   *
   * @param memberTypeId The unique identifier of the {@link MemberType} to retrieve all the related MemberTypes for.
   *
   * @return A {@link List} of active {@link MemberType MemberTypes} in the system that are related to the specified
   * MemberType.
   */
  List<MemberType> getActiveMemberTypesForMemberType(final long memberTypeId);

  MemberType saveMemberType(MemberType memberType, User user) throws DataAccessException;
}
