package com.cagst.swkroa.job;

import com.cagst.swkroa.user.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a row in the resultset into a {@link JobDetail} objects. Used to marshal / unmarshal a {@link JobDetail}
 * to / from the database.
 *
 * @author Craig Gaskill
 */
/* package */ final class JobDetailMapper implements RowMapper<JobDetail> {
  private static final String JOB_DETAIL_ID      = "job_detail_id";
  private static final String JOB_ID             = "job_id";
  private static final String JOB_STATUS         = "job_status";
  private static final String PARENT_ENTITY_ID   = "parent_entity_id";
  private static final String PARENT_ENTITY_NAME = "parent_entity_name";

  // meta-data
  private static final String ACTIVE_IND          = "active_ind";
  private static final String CREATE_ID           = "create_id";
  private static final String UPDT_ID             = "updt_id";
  private static final String JOB_DETAIL_UPDT_CNT = "job_detail_updt_cnt";

  @Override
  public JobDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
    JobDetail detail = new JobDetail();
    detail.setJobDetailUID(rs.getLong(JOB_DETAIL_ID));
    detail.setJobUID(rs.getLong(JOB_ID));
    detail.setJobStatus(JobStatus.valueOf(rs.getString(JOB_STATUS)));
    detail.setParentEntityUID(rs.getLong(PARENT_ENTITY_ID));
    detail.setParentEntityName(rs.getString(PARENT_ENTITY_NAME));
    detail.setCreateUID(rs.getLong(CREATE_ID));

    detail.setActive(rs.getBoolean(ACTIVE_IND));
    detail.setJobDetailUpdateCount(rs.getLong(JOB_DETAIL_UPDT_CNT));

    return detail;
  }

  /**
   * Will marshal a {@link JobDetail} into a {@link MapSqlParameterSource} for inserting into the database.
   *
   * @param jobDetail
   *    The {@link JobDetail} to map into an insert statement.
   * @param user
   *    The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapInsertStatement(final JobDetail jobDetail, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(JOB_ID, jobDetail.getJobUID());
    params.addValue(PARENT_ENTITY_ID, jobDetail.getParentEntityUID());
    params.addValue(PARENT_ENTITY_NAME, jobDetail.getParentEntityName());
    params.addValue(JOB_STATUS, jobDetail.getJobStatus().name());

    params.addValue(ACTIVE_IND, jobDetail.isActive());
    params.addValue(CREATE_ID, user.getUserUID());
    params.addValue(UPDT_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link JobDetail} into a {@link MapSqlParameterSource} for updating into the database.
   *
   * @param jobDetail
   *    The {@link JobDetail} to map into an update statement.
   * @param user
   *    The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(final JobDetail jobDetail, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();

    params.addValue(JOB_STATUS, jobDetail.getJobStatus().name());
    params.addValue(ACTIVE_IND, jobDetail.isActive());
    params.addValue(UPDT_ID, user.getUserUID());

    params.addValue(JOB_DETAIL_ID, jobDetail.getJobDetailUID());
    params.addValue(JOB_DETAIL_UPDT_CNT, jobDetail.getJobDetailUpdateCount());

    return params;
  }
}
