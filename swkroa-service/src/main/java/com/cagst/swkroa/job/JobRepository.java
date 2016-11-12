package com.cagst.swkroa.job;

import java.util.List;

import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UsernameTakenException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Definition of a repository that retrieves and persists {@link Job} objects.
 *
 * @author Craig Gaskill
 */
public interface JobRepository {
  /**
   * Retrieves the {@link Job} associated with the specified unique identifier.
   *
   * @param uid
   *    A {@link long} that uniquely identifies the {@link Job} to retrieve.
   *
   * @return The {@link Job} associated with the specified uid, {@code null} if no Job was found.
   */
  Job getJobByUID(final long uid);

  /**
   * Retrieves a {@link List} of {@link Job}s that are associated with the specified {@link JobStatus}.
   *
   * @param jobStatus
   *    The {@link JobStatus} to retrieve jobs for.
   *
   * @return A {@link List} of {@link Job}s that are associated with the specified Status. An empty collection if none are found.
   */
  List<Job> getJobsForStatus(final JobStatus jobStatus);

  /**
   * Retrieves a {@link List} of {@link Job}s that are associated with the specified {@link JobType}.
   *
   * @param jobType
   *    The {@link JobType} to retrieves jobs for.
   *
   * @return A {@link List} of {@link Job}s that are associated with the specified Type. An empty collection if none are found.
   */
  List<Job> getJobsForType(final JobType jobType);

  /**
   * Retrieves a {@link List} of {@link Job}s that are associated with the specified {@link JobType} and {@link JobStatus}.
   *
   * @param jobType
   *    The {@link JobType} to retrieves jobs for.
   * @param jobStatus
   *    The {@link JobStatus} to retrieve jobs for.
   *
   * @return A {@link List} of {@link Job}s that are associated with the specified Type and Status. An empty collection if none are found.
   */
  List<Job> getJobsForTypeAndStatus(final JobType jobType, final JobStatus jobStatus);

  /**
   * Retrieves a {@link List} of {@link Job}s that are pending for the specified {@link JobType}.
   *
   * @param jobType
   *    The {@link JobType} to retrieve pending jobs for.
   *
   * @return A {@link List} of {@link Job}s that are pending for the specified job type.
   */
  List<Job> getPendingJobsForType(final JobType jobType);

  /**
   * Retrieves the {@link List} of {@link JobDetail} details associated with the specified Job (id).
   *
   * @param jobId
   *    A {@link long} that uniquely identifies the job to retrieve the details for.
   *
   * @return The {@link List} of {@link JobDetail JobDetails} associated to the specified job, an empty list if none are found.
   */
  List<JobDetail> getDetailsForJob(final long jobId);

  /**
   * Commits the specified {@link Job} to persistent storage.
   *
   * @param job
   *    The {@link Job} to persist.
   * @param user
   *    The {@link User} that performed the changes.
   *
   * @return A {@link Job} once it has been committed to persistent storage.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws UsernameTakenException
   *     if the username associated to the {@code builder} is already being used by another
   *     user
   * @throws DataAccessException
   *     if the query fails
   */
  Job saveJob(final Job job, final User user);

  /**
   * Commits the specified {@link JobDetail} to persistent storage.
   *
   * @param jobDetail
   *    The {@link JobDetail} to persist.
   * @param user
   *    The {@link User} that performed the changes.
   *
   * @return The {@link JobDetail} once it has been committed to persistent storage.
   *
   * @throws OptimisticLockingFailureException
   *     if the updt_cnt doesn't match (meaning someone has updated it since it was last read)
   * @throws IncorrectResultSizeDataAccessException
   *     if the number of rows inserted / updated exceeded the expected number
   * @throws UsernameTakenException
   *     if the username associated to the {@code builder} is already being used by another
   *     user
   * @throws DataAccessException
   *     if the query fails
   */
  JobDetail saveJobDetail(final JobDetail jobDetail, final User user);
}
