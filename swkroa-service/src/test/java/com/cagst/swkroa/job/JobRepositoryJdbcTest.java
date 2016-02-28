package com.cagst.swkroa.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.cagst.swkroa.test.BaseTestRepository;
import com.cagst.swkroa.user.User;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

/**
 * Test class for the {@link JobRepositoryJdbc} class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class JobRepositoryJdbcTest extends BaseTestRepository {
  private JobRepositoryJdbc repo;

  @Before
  public void setUp() {
    repo = new JobRepositoryJdbc(createTestDataSource());
  }

  /**
   * Test the getJobByUID method and not finding the Job.
   */
  @Test(expected = EmptyResultDataAccessException.class)
  public void testGetJobByUID_NotFound() {
    repo.getJobByUID(999L);
  }

  /**
   * Test the getJobByUID method and finding the Job.
   */
  @Test
  public void testGetJobByUID_Found() {
    Job job = repo.getJobByUID(1L);

    assertNotNull("Ensure the Job was found", job);
    assertEquals("Ensure it is the correct type", JobType.RENEWAL, job.getJobType());
    assertEquals("Ensure it is the correct status", JobStatus.SUCCEEDED, job.getJobStatus());
  }

  /**
   * Test the getJobsForStatus method.
   */
  @Test
  public void testGetJobsForStatus() {
    List<Job> jobs1 = repo.getJobsForStatus(JobStatus.SUCCEEDED);
    assertNotNull("Ensure the collection is not null", jobs1);
    assertFalse("Ensure the collection is not empty", jobs1.isEmpty());
    assertEquals("Ensure we found the correct number of jobs", 2, jobs1.size());

    List<Job> jobs2 = repo.getJobsForStatus(JobStatus.INPROCESS);
    assertNotNull("Ensure the collection is not null", jobs2);
    assertTrue("Ensure the collection is empty", jobs2.isEmpty());
  }

  /**
   * Test the getJobsForType method.
   */
  @Test
  public void testGetJobsForType() {
    List<Job> jobs = repo.getJobsForType(JobType.RENEWAL);
    assertNotNull("Ensure the collection is not null", jobs);
    assertFalse("Ensure the collection is not empty", jobs.isEmpty());
    assertEquals("Ensure we found the correct number of jobs", 4, jobs.size());
  }

  /**
   * Test the getJobsForTypeAndStatus method.
   */
  @Test
  public void testGetJobsForTypeAndStatus() {
    List<Job> jobs = repo.getJobsForTypeAndStatus(JobType.RENEWAL, JobStatus.SUBMITTED);
    assertNotNull("Ensure the collection is not null", jobs);
    assertFalse("Ensure the collection is not empty", jobs.isEmpty());
    assertEquals("Ensure we found the correct number of jobs", 1, jobs.size());
  }

  /**
   * Test the getDetailsForJob method and not finding any details.
   */
  @Test
  public void testGetDetailsForJob_NoneFound() {
    List<JobDetail> details = repo.getDetailsForJob(1L);
    assertNotNull("Ensure the collection is not null", details);
    assertTrue("Ensure the collection is empty", details.isEmpty());
  }

  /**
   * Test the getDetailsForJob method and finding details.
   */
  @Test
  public void testGetDetailsForJob_Found() {
    List<JobDetail> details = repo.getDetailsForJob(4L);
    assertNotNull("Ensure the collection is not null", details);
    assertFalse("Ensure the collection is not empty", details.isEmpty());
    assertEquals("Ensure we found the correct number of details", 5, details.size());
  }

  /**
   * Test the saveJob method by inserting a new Job.
   */
  @Test
  public void testSaveJob_Insert() {
    User editingUser = new User();
    editingUser.setUserUID(1L);

    JobDetail jobDetail1 = new JobDetail();
    jobDetail1.setJobStatus(JobStatus.SUBMITTED);
    jobDetail1.setParentEntityUID(1L);
    jobDetail1.setParentEntityName(Job.MEMBERSHIP);

    JobDetail jobDetail2 = new JobDetail();
    jobDetail2.setJobStatus(JobStatus.SUBMITTED);
    jobDetail2.setParentEntityUID(2L);
    jobDetail2.setParentEntityName(Job.MEMBERSHIP);

    Job newJob = new Job();
    newJob.setJobName("Renew Membership");
    newJob.setJobType(JobType.RENEWAL);
    newJob.setJobStatus(JobStatus.SUBMITTED);
    newJob.setJobDetails(Lists.newArrayList(jobDetail1, jobDetail2));

    List<Job> jobs1 = repo.getJobsForStatus(JobStatus.SUBMITTED);
    assertNotNull("Ensure the collection is not null", jobs1);
    assertFalse("Ensure the collection is not empty", jobs1.isEmpty());
    assertEquals("Ensure we found the correct number of jobs", 1, jobs1.size());

    Job savedJob = repo.saveJob(newJob, editingUser);
    assertNotNull("Ensure we have a new job", savedJob);
    assertTrue("Ensure the job has an ID", savedJob.getJobUID() > 0L);

    List<Job> jobs2 = repo.getJobsForStatus(JobStatus.SUBMITTED);
    assertNotNull("Ensure the collection is not null", jobs2);
    assertFalse("Ensure the collection is not empty", jobs2.isEmpty());
    assertEquals("Ensure we found the correct number of jobs", 2, jobs2.size());
  }

  @Test
  public void testSaveJob_Update() {
    User editingUser = new User();
    editingUser.setUserUID(1L);

    Job job1 = repo.getJobByUID(4L);
    assertNotNull("Ensure we found the job", job1);
    assertEquals("Ensure it is the expected status", JobStatus.SUBMITTED, job1.getJobStatus());

    job1.setJobStatus(JobStatus.INPROCESS);

    Job savedJob = repo.saveJob(job1, editingUser);
    assertNotNull("Ensure we found the job", savedJob);
    assertEquals("Ensure it is the expected status", JobStatus.INPROCESS, savedJob.getJobStatus());

    Job job2 = repo.getJobByUID(4L);
    assertNotNull("Ensure we found the job", job2);
    assertEquals("Ensure it is the expected status", JobStatus.INPROCESS, job2.getJobStatus());
  }
}
