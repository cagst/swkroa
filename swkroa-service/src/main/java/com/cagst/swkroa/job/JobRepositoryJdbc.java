package com.cagst.swkroa.job;

import com.cagst.common.db.BaseRepositoryJdbc;
import com.cagst.common.db.StatementLoader;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UsernameTakenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A JDBC implementation of the {@link JobRepository} interface.
 *
 * @author Craig Gaskill
 */
@Named("jobRepo")
/* package */ class JobRepositoryJdbc extends BaseRepositoryJdbc implements JobRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(JobRepositoryJdbc.class);

  private static final String GET_JOB_BY_UID               = "GET_JOB_BY_UID";
  private static final String GET_JOBS_FOR_STATUS          = "GET_JOBS_FOR_STATUS";
  private static final String GET_JOBS_FOR_TYPE            = "GET_JOBS_FOR_TYPE";
  private static final String GET_JOBS_FOR_TYPE_AND_STATUS = "GET_JOBS_FOR_TYPE_AND_STATUS";
  private static final String GET_PENDING_JOBS_FOR_TYPE    = "GET_PENDING_JOBS_FOR_TYPE";
  private static final String GET_JOB_DETAILS_FOR_JOB      = "GET_JOB_DETAILS_FOR_JOB";

  private static final String INSERT_JOB = "INSERT_JOB";
  private static final String UPDATE_JOB = "UPDATE_JOB";

  private static final String INSERT_JOB_DETAIL = "INSERT_JOB_DETAIL";
  private static final String UPDATE_JOB_DETAIL = "UPDATE_JOB_DETAIL";

  /**
   * Primary Constructor used to create an instance of JobRepositoryJdbc.
   *
   * @param dataSource
   *    The {@link DataSource} used to retrieve / persist data objects.
   */
  @Inject
  public JobRepositoryJdbc(final DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public Job getJobByUID(final long uid) {
    LOGGER.info("Calling getJobByUID for [{}]", uid);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<>(1);
    params.put("job_id", uid);

    List<Job> jobs = getJdbcTemplate().query(stmtLoader.load(GET_JOB_BY_UID), params, new JobMapper());
    if (jobs.size() == 1) {
      return jobs.get(0);
    } else if (jobs.size() == 0) {
      LOGGER.warn("Job with UID of [{}] was not found.", uid);
      throw new EmptyResultDataAccessException(1);
    } else {
      LOGGER.warn("More than one Job with UID of [{}] was found.", uid);
      throw new IncorrectResultSizeDataAccessException(1, jobs.size());
    }
  }

  @Override
  public List<Job> getJobsForStatus(final JobStatus jobStatus) {
    LOGGER.info("Calling getJobsForStatus for [{}]", jobStatus);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, String> params = new HashMap<>(1);
    params.put("job_status", jobStatus.name());

    return getJdbcTemplate().query(stmtLoader.load(GET_JOBS_FOR_STATUS), params, new JobMapper());
  }

  @Override
  public List<Job> getJobsForType(final JobType jobType) {
    LOGGER.info("Calling getJobsForType for [{}]", jobType);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, String> params = new HashMap<>(1);
    params.put("job_type", jobType.name());

    return getJdbcTemplate().query(stmtLoader.load(GET_JOBS_FOR_TYPE), params, new JobMapper());
  }

  @Override
  public List<Job> getJobsForTypeAndStatus(JobType jobType, JobStatus jobStatus) {
    LOGGER.info("Calling getJobsForStatusAndType for Type [{}] and Status [{}]", jobType, jobStatus);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, String> params = new HashMap<>(2);
    params.put("job_type", jobType.name());
    params.put("job_status", jobStatus.name());

    return getJdbcTemplate().query(stmtLoader.load(GET_JOBS_FOR_TYPE_AND_STATUS), params, new JobMapper());
  }

  @Override
  public List<Job> getPendingJobsForType(JobType jobType) {
    LOGGER.info("Calling getPendingJobsForType for Type [{}]", jobType);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, String> params = new HashMap<>(1);
    params.put("job_type", jobType.name());

    return getJdbcTemplate().query(stmtLoader.load(GET_PENDING_JOBS_FOR_TYPE), params, new JobMapper());
  }

  @Override
  public List<JobDetail> getDetailsForJob(long jobId) {
    LOGGER.info("Calling getDetailsForJob for [{}]", jobId);

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    Map<String, Long> params = new HashMap<>(1);
    params.put("job_id", jobId);

    return getJdbcTemplate().query(stmtLoader.load(GET_JOB_DETAILS_FOR_JOB), params, new JobDetailMapper());
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Job saveJob(final Job job, final User user)
      throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, UsernameTakenException {

    Assert.notNull(job, "Assertion Failed - argument [job] cannot be null");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Calling saveJob for [{}]", job.getJobName());

    if (job.getJobUID() == 0L) {
      return insertJob(job, user);
    } else {
      return updateJob(job, user);
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public JobDetail saveJobDetail(final JobDetail jobDetail, final User user)
    throws OptimisticLockingFailureException, IncorrectResultSizeDataAccessException, UsernameTakenException {

    Assert.notNull(jobDetail, "Assertion Failed - argument [jobDetail] cannot be null");
    Assert.notNull(user, "Assertion Failed - argument [user] cannot be null");

    LOGGER.info("Calling saveJobDetail for [{}, {}]", jobDetail.getParentEntityUID(), jobDetail.getParentEntityName());

    if (jobDetail.getJobDetailUID() == 0L) {
      return insertJobDetail(jobDetail, user);
    } else {
      return updateJobDetail(jobDetail, user);
    }
  }

  /**
   * Helper method to insert the job into persistent storage.
   */
  private Job insertJob(final Job job, final User user) {
    LOGGER.info("Calling insertJob for [{}]", job.getJobName());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_JOB), JobMapper.mapInsertStatement(job, user), keyHolder);
    if (cnt == 1) {
      job.setJobUID(keyHolder.getKey().longValue());
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    for (JobDetail jobDetail : job.getJobDetails()) {
      jobDetail.setJobUID(job.getJobUID());

      saveJobDetail(jobDetail, user);
    }

    return job;
  }

  /**
   * Helper method to update the job in persistent storage.
   */
  private Job updateJob(final Job job, final User user) {
    LOGGER.info("Calling updateJob for [{}]", job.getJobName());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_JOB), JobMapper.mapUpdateStatement(job, user));
    if (cnt == 1) {
      job.setJobUpdateCount(job.getJobUpdateCount() + 1);
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    for (JobDetail jobDetail : job.getJobDetails()) {
      jobDetail.setJobUID(job.getJobUID());

      saveJobDetail(jobDetail, user);
    }

    return job;
  }

  /**
   * Helper method to insert the job detail into persistent storage.
   */
  private JobDetail insertJobDetail(final JobDetail jobDetail, final User user) {
    LOGGER.info("Calling insertJobDetail for [{}, {}]", jobDetail.getParentEntityUID(), jobDetail.getParentEntityName());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());
    KeyHolder keyHolder = new GeneratedKeyHolder();

    int cnt = getJdbcTemplate().update(stmtLoader.load(INSERT_JOB_DETAIL), JobDetailMapper.mapInsertStatement(jobDetail, user), keyHolder);
    if (cnt == 1) {
      jobDetail.setJobDetailUID(keyHolder.getKey().longValue());
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return jobDetail;
  }

  /**
   * Helper method to update the job detail in persistent storage.
   */
  private JobDetail updateJobDetail(final JobDetail jobDetail, final User user) {
    LOGGER.info("Calling updateJobDetail for [{}, {}]", jobDetail.getParentEntityUID(), jobDetail.getParentEntityName());

    StatementLoader stmtLoader = StatementLoader.getLoader(getClass(), getStatementDialect());

    int cnt = getJdbcTemplate().update(stmtLoader.load(UPDATE_JOB_DETAIL), JobDetailMapper.mapUpdateStatement(jobDetail, user));
    if (cnt == 1) {
      jobDetail.setJobDetailUpdateCount(jobDetail.getJobDetailUpdateCount() + 1);
    } else {
      throw new IncorrectResultSizeDataAccessException(1, cnt);
    }

    return jobDetail;
  }

}
