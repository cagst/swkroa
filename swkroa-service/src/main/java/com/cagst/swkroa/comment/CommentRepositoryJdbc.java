package com.cagst.swkroa.comment;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.List;

import com.cagst.swkroa.internal.BaseRepositoryJdbc;
import com.cagst.swkroa.internal.StatementLoader;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

/**
 * JDBC Template implementation of the {@link CommentRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("commentRepo")
/* package */class CommentRepositoryJdbc extends BaseRepositoryJdbc implements CommentRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommentRepositoryJdbc.class);

  private static final String GET_COMMENTS_FOR_ENTITY = "GET_COMMENTS_FOR_ENTITY";
  private static final String GET_COMMENT_BY_UID = "GET_COMMENT_BY_UID";

  private static final String INSERT_COMMENT = "INSERT_COMMENT";
  private static final String UPDATE_COMMENT = "UPDATE_COMMENT";

  /**
   * Primary Constructor used to create an instance of the CommentRepositoryJdbc.
   *
   * @param dataSource
   *     The {@link DataSource} used to retrieve / persists data objects.
   */
  @Inject
  public CommentRepositoryJdbc(final DataSource dataSource) {
    super(dataSource);
  }

  @Override
  @Cacheable(value = "commentsList", key = "#membership.getMembershipUID()")
  public List<Comment> getCommentsForMembership(final Membership membership) {
    Assert.notNull(membership, "Argument [membership] cannot be null");

    LOGGER.info("Calling getCommentsForMembership [{}].", membership.getMembershipUID());

    return getCommentsForEntity(Comment.MEMBERSHIP, membership.getMembershipUID());
  }

  @Override
  public Comment getCommentByUID(final long uid) {
    LOGGER.info("Calling getCommentByUID for [{}]", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    List<Comment> comments = getJdbcTemplate().query(
        stmtLoader.load(GET_COMMENT_BY_UID),
        new MapSqlParameterSource("comment_id", uid),
        new CommentMapper());

    if (comments.size() == 1) {
      return comments.get(0);
    } else if (comments.size() == 0) {
      LOGGER.warn("Comment with UID of [{}] was not found.", uid);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.warn("More than one Comment with UID of [{}] was found.", uid);
      throw new IncorrectResultSizeDataAccessException(1, comments.size());
    }
  }

  @Override
  @CacheEvict(value = "commentsList", key = "#comment.getParentEntityUID()")
  public Comment saveComment(final Comment comment, final User user) throws DataAccessException {
    Assert.notNull(comment, "Argument [comment] cannot be null");
    Assert.notNull(user, "Argument [user] cannot be null");

    LOGGER.info("Saving Comment for [{}, {}].", comment.getParentEntityName(), comment.getParentEntityUID());

    if (comment.getCommentUID() == 0L) {
      return insertCommentForEntity(comment, user);
    } else {
      return updateCommentForEntity(comment, user);
    }
  }

  /**
   * Helper method to retrieve the {@link List} of {@link Comment Comments} by EntityName / EntityID.
   *
   * @param entityName
   *     The <i>name<i/> of the entity the comment is associated with.
   * @param entityID
   *     The unique identifier of the entity the comment is associated with.
   *
   * @return A {@link List} of {@link Comment Comments} associated with the specified EntityName / EntityID.
   */
  private List<Comment> getCommentsForEntity(final String entityName, final long entityID) {
    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("parent_entity_name", entityName);
    params.addValue("parent_entity_id", entityID);

    return getJdbcTemplate().query(stmtLoader.load(GET_COMMENTS_FOR_ENTITY), params, new CommentMapper());
  }

  private Comment insertCommentForEntity(final Comment comment, final User user) {
    LOGGER.info("Inserting new Comment for [{}]", comment.getParentEntityName());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_COMMENT),
        CommentMapper.mapInsertStatement(comment, user), keyHolder);

    if (cnt != 1) {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return Comment.builder(comment).setCommentUID(keyHolder.getKey().longValue()).build();
  }

  private Comment updateCommentForEntity(final Comment comment, final User user) {
    LOGGER.info("Updating Comment [{}]", comment.getCommentUID());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate()
        .update(stmtLoader.load(UPDATE_COMMENT), CommentMapper.mapUpdateStatement(comment, user));

    if (cnt == 1) {
      return Comment.builder(comment)
          .setCommentUpdateCount(comment.getCommentUpdateCount() + 1)
          .build();
    } else if (cnt == 0) {
      throw new OptimisticLockingFailureException("invalid update count of [" + cnt
          + "] possible update count mismatch");
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }
  }
}
