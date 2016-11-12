package com.cagst.swkroa.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * Maps a row in the resultset into a {@link UserQuestion} object. Used to marshal / un-marshal a {@link UserQuestion}
 * object from / to the database.
 *
 * @author Craig Gaskill
 */
/* package */ final class UserQuestionMaper implements RowMapper<UserQuestion> {
  private static final String USER_QUESTION_ID = "user_question_id";
  private static final String USER_ID          = "user_id";
  private static final String QUESTION_CD      = "question_cd";
  private static final String ANSWER           = "answer";
  private static final String ACTIVE_IND       = "active_ind";
  private static final String CREATE_ID        = "create_id";
  private static final String UPDT_ID          = "updt_id";
  private static final String UPDT_CNT         = "updt_cnt";

  @Override
  public UserQuestion mapRow(ResultSet rs, int rowNum) throws SQLException {
    return UserQuestion.builder()
        .setUserQuestionUID(rs.getLong(USER_QUESTION_ID))
        .setQuestionCD(rs.getLong(QUESTION_CD))
        .setAnswer(rs.getString(ANSWER))
        .setActive(rs.getBoolean(ACTIVE_IND))
        .setUserQuestionUpdateCount(rs.getLong(UPDT_CNT))
        .build();
  }

  /**
   * Will marshal a {@link UserQuestion} into a {@link MapSqlParameterSource} for inserting into the database.
   *
   * @param question
   *    The {@link UserQuestion} to map into an insert statement.
   * @param user
   *    The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapInsertStatement(long userId, UserQuestion question, User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(USER_ID, userId);
    params.addValue(QUESTION_CD, question.getQuestionCD());
    params.addValue(ANSWER, question.getAnswer());
    params.addValue(ACTIVE_IND, question.isActive());
    params.addValue(CREATE_ID, user.getUserUID());
    params.addValue(UPDT_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link UserQuestion} into a {@link MapSqlParameterSource} for updating into the database.
   *
   * @param question
   *    The {@link UserQuestion} to map into an update statement.
   * @param user
   *    The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(UserQuestion question, User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(QUESTION_CD, question.getQuestionCD());
    params.addValue(ANSWER, question.getAnswer());
    params.addValue(ACTIVE_IND, question.isActive());
    params.addValue(UPDT_ID, user.getUserUID());

    params.addValue(USER_QUESTION_ID, question.getUserQuestionUID());
    params.addValue(UPDT_CNT, question.getUserQuestionUpdateCount());

    return params;
  }
}
