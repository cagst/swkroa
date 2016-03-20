package com.cagst.swkroa.document;

import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.List;

/**
 * Definition of a repository that retrieves and persists {@link Document} objects.
 *
 * @author Craig Gaskill
 */
public interface DocumentRepository {
  /**
   * Retrieves the {@link Document} associated with the specified unique identifier.
   *
   * @param uid
   *    A {@link long} that uniquely identifies the {@link Document} to retrieve.
   *
   * @return The {@link Document} associated with the specified uid, {@code null} if no Document was found.
   */
  Document getDocumentByUID(final long uid);

  /**
   * Retrieves a {@link List} of {@link Document Documents} defined within the system for the specified {@link Membership}.
   *
   * @param membership
   *    The {@link Membership} to retrieve documents for.
   *
   * @return A {@link List} of {@link Document Documents} associated with the specified Membership.
   */
  List<Document> getDocumentsForMembership(final Membership membership);

  /**
   * Retrieves a {@link List} of {@link Document Documents} that are globally defined in the system.
   *
   * @return A {@link List} of {@link Document Documents} that are globally defined in the system.
   */
  List<Document> getGlobalDocuments();

  /**
   * Persists the specified {@link Document}.
   *
   * @param document
   *    The {@link Document} to persist.
   * @param user
   *    The {@link User} that performed the changes.
   *
   * @return A {@link Document} after it has been persisted.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws DataAccessException
   *     if the query fails
   */
  Document saveDocument(final Document document, final User user) throws DataAccessException;
}
