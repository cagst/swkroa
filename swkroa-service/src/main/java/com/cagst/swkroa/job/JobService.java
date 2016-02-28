package com.cagst.swkroa.job;

import com.cagst.swkroa.user.User;

/**
 * Definitions of a service that retrieves and persists {@link Job} and {@link JobDetail} objects.
 *
 * @author Craig Gaskill
 */
public interface JobService {
  /**
   * Submits the {@link Job} for processing.
   *
   * @param job
   *    The {@link Job} to submit for processing.
   * @param user
   *    The {@link User} that performed the changes.
   *
   * @return The {@link Job} after it has been submitted for processing.
   */
  Job submitJob(final Job job, User user);
}
