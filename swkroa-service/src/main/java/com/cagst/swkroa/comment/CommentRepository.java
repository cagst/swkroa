package com.cagst.swkroa.comment;

import java.util.List;

import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Definition of a repository that retrieves and persists {@link Comment} objects.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public interface CommentRepository {
  /**
   * Retrieves a {@link List} of {@link Comment Comments} defined within the system for the specified {@link Membership}
   * .
   *
   * @param membership
   *     The {@link Membership} to retrieve comments for.
   *
   * @return A {@link List} of {@link Comment Comments} associated with the specified Membership.
   */
  public List<Comment> getCommentsForMembership(final Membership membership);

  /**
   * Retrieves the {@link Comment} associated with the specified unique identifier.
   *
   * @param uid
   *     A {@link long} that uniquely identifies the {@link Comment} to retrieve.
   *
   * @return The {@link Comment} associated with the specified uid, {@code null} if no Comment was found.
   */
  public Comment getCommentByUID(final long uid);

  /**
   * Persists the specified {@link Comment}.
   *
   * @param comment
   *     The {@link Comment} to persist.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link Comment} after it has been persisted.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws DataAccessException
   *     if the query fails
   */
  public Comment saveComment(final Comment comment, final User user) throws OptimisticLockingFailureException,
      IncorrectResultSizeDataAccessException, DataAccessException;
}
