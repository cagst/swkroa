package com.cagst.swkroa.job;

import com.cagst.swkroa.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;

/**
 * Implementation of the {@link JobService} interface.
 *
 * @author Craig Gaskill
 */
@Named("jobService")
public class JobServiceImpl implements JobService {
  private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);

  private final JobRepository jobRepo;

  /**
   * Primary Constructor used to create an instance of <i>JobServiceImpl</i>.
   *
   * @param jobRepo
   *    The {@link JobRepository} used to retrieve {@link Job} and {@link JobDetail} objects.
   */
  public JobServiceImpl(final JobRepository jobRepo) {
    this.jobRepo = jobRepo;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Job submitJob(Job job, User user) {
    LOGGER.info("Calling submitJob for [{}]", job);

    // ensure the Job status is SUBMITTED
    job.setJobStatus(JobStatus.SUBMITTED);
    for (JobDetail detail : job.getJobDetails()) {
      detail.setJobStatus(JobStatus.SUBMITTED);
    }

    return jobRepo.saveJob(job, user);
  }
}
