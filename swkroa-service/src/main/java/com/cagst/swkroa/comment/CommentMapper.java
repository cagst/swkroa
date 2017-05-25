package com.cagst.swkroa.comment;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cagst.swkroa.user.User;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Maps a row in the resultset into a {@link Comment} object. Used to marshal / unmarshal a {@link Comment} to / from
 * the database.
 *
 * @author Craig Gaskill
 */
/* package */final class CommentMapper implements RowMapper<Comment> {
  private static final String COMMENT_ID = "comment_id";
  private static final String COMMENT_DT = "comment_dt";
  private static final String COMMENT_TXT = "comment_txt";
  private static final String PARENT_ENTITY_ID = "parent_entity_id";
  private static final String PARENT_ENTITY_NAME = "parent_entity_name";

  // meta-data
  private static final String ACTIVE_IND = "active_ind";
  private static final String CREATE_ID = "create_id";
  private static final String UPDT_ID = "updt_id";
  private static final String COMMENT_UPDT_CNT = "comment_updt_cnt";

  @Override
  public Comment mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    return Comment.builder()
        .setCommentUID(rs.getLong(COMMENT_ID))
        .setParentEntityUID(rs.getLong(PARENT_ENTITY_ID))
        .setParentEntityName(rs.getString(PARENT_ENTITY_NAME))
        .setCommentDate(new DateTime(rs.getTimestamp(COMMENT_DT)))
        .setCommentText(rs.getString(COMMENT_TXT))
        .setCommentUpdateCount(rs.getLong(COMMENT_UPDT_CNT))
        .setActive(rs.getBoolean(ACTIVE_IND))
        .build();
  }

  /**
   * Will marshal a {@link Comment} into a {@link MapSqlParameterSource} for inserting into the database.
   *
   * @param comment
   *     The {@link Comment} to map into an insert statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapInsertStatement(final Comment comment, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(PARENT_ENTITY_NAME, comment.getParentEntityName());
    params.addValue(PARENT_ENTITY_ID, comment.getParentEntityUID());
    params.addValue(COMMENT_DT, comment.getCommentDate().toDate());
    params.addValue(COMMENT_TXT, comment.getCommentText());
    params.addValue(ACTIVE_IND, comment.isActive());
    params.addValue(CREATE_ID, user.getUserUID());
    params.addValue(UPDT_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link Comment} into a {@link MapSqlParameterSource} for updating into the database.
   *
   * @param comment
   *     The {@link Comment} to map into an update statement.
   * @param user
   *     The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(final Comment comment, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(COMMENT_DT, comment.getCommentDate().toDate());
    params.addValue(COMMENT_TXT, comment.getCommentText());
    params.addValue(ACTIVE_IND, comment.isActive());
    params.addValue(UPDT_ID, user.getUserUID());

    params.addValue(COMMENT_ID, comment.getCommentUID());
    params.addValue(COMMENT_UPDT_CNT, comment.getCommentUpdateCount());

    return params;
  }
}
