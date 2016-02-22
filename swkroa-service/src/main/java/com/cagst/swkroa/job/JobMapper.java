package com.cagst.swkroa.job;

import com.cagst.swkroa.user.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a row in the resultset into a {@link Job} objects. Used to marsah / unmarshal a {@link Job} to / form the database.
 *
 * @author Craig Gaskill
 */
/* package */ final class JobMapper implements RowMapper<Job> {
  private static final String JOB_ID             = "job_id";
  private static final String JOB_NAME           = "job_name";
  private static final String JOB_TYPE           = "job_type";
  private static final String JOB_STATUS         = "job_status";
  private static final String PARENT_ENTITY_ID   = "parent_entity_id";
  private static final String PARENT_ENTITY_NAME = "parent_entity_name";

  // meta-data
  private static final String ACTIVE_IND   = "active_ind";
  private static final String CREATE_ID    = "create_id";
  private static final String UPDT_ID      = "updt_id";
  private static final String JOB_UPDT_CNT = "job_updt_cnt";

  @Override
  public Job mapRow(ResultSet rs, int rowNum) throws SQLException {
    Job job = new Job();
    job.setJobUID(rs.getLong(JOB_ID));
    job.setJobName(rs.getString(JOB_NAME));
    job.setJobType(JobType.valueOf(rs.getString(JOB_TYPE)));
    job.setJobStatus(JobStatus.valueOf(rs.getString(JOB_STATUS)));
    job.setParentEntityUID(rs.getLong(PARENT_ENTITY_ID));
    job.setParentEntityName(rs.getString(PARENT_ENTITY_NAME));
    job.setCreateUID(rs.getLong(CREATE_ID));

    job.setActive(rs.getBoolean(ACTIVE_IND));
    job.setJobUpdateCount(rs.getLong(JOB_UPDT_CNT));

    return job;
  }

  /**
   * Will marshal a {@link Job} into a {@link MapSqlParameterSource} for inserting into the database.
   *
   * @param job
   *    The {@link Job} to map into an insert statement.
   * @param user
   *    The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapInsertStatement(final Job job, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(JOB_NAME, job.getJobName());
    params.addValue(JOB_TYPE, job.getJobType().name());
    params.addValue(JOB_STATUS, job.getJobStatus().name());
    params.addValue(PARENT_ENTITY_ID, job.getParentEntityUID() != 0L ? job.getParentEntityUID() : null);
    params.addValue(PARENT_ENTITY_NAME, job.getParentEntityName());

    params.addValue(ACTIVE_IND, job.isActive());
    params.addValue(CREATE_ID, user.getUserUID());
    params.addValue(UPDT_ID, user.getUserUID());

    return params;
  }

  /**
   * Will marshal a {@link Job} into a {@link MapSqlParameterSource} for updating into the database.
   *
   * @param job
   *    The {@link Job} to map into an update statement.
   * @param user
   *    The {@link User} that performed the changes.
   *
   * @return A {@link MapSqlParameterSource} that can be used in a {@code jdbcTemplate.update} statement.
   */
  public static MapSqlParameterSource mapUpdateStatement(final Job job, final User user) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue(JOB_NAME, job.getJobName());
    params.addValue(JOB_TYPE, job.getJobType().name());
    params.addValue(JOB_STATUS, job.getJobStatus().name());
    params.addValue(PARENT_ENTITY_ID, job.getParentEntityUID() != 0L ? job.getParentEntityUID() : null);
    params.addValue(PARENT_ENTITY_NAME, job.getParentEntityName());
    params.addValue(ACTIVE_IND, job.isActive());
    params.addValue(UPDT_ID, user.getUserUID());

    params.addValue(JOB_ID, job.getJobUID());
    params.addValue(JOB_UPDT_CNT, job.getJobUpdateCount());

    return params;
  }
}
