package com.cagst.swkroa.controller.api;

import javax.inject.Inject;
import java.util.List;

import com.cagst.swkroa.job.Job;
import com.cagst.swkroa.job.JobRepository;
import com.cagst.swkroa.job.JobType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles and retrieves {@link Job} objects depending upon the URI template.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping("/api/jobs")
public final class JobApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(JobApiController.class);

  private final JobRepository jobRepo;

  @Inject
  public JobApiController(final JobRepository jobRepo) {
    this.jobRepo = jobRepo;
  }

  @RequestMapping(value = "/pending/{jobType}", method = RequestMethod.GET)
  public List<Job> getPendingJobsForType(final @PathVariable("jobType") JobType jobType) {
    LOGGER.info("Received request to retrieve pending jobs for type [{}].", jobType);

    return jobRepo.getPendingJobsForType(jobType);
  }
}
