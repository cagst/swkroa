package com.cagst.swkroa.integration;

import com.cagst.swkroa.job.Job;
import com.cagst.swkroa.job.JobDetail;
import com.cagst.swkroa.job.JobStatus;
import com.cagst.swkroa.job.JobType;
import com.cagst.swkroa.member.MembershipService;
import com.cagst.swkroa.user.User;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test class for the {@link com.cagst.swkroa.member.MembershipServiceImpl} class.
 *
 * @author Craig Gaskill
 */
@RunWith(JUnit4.class)
public class MembershipServiceImplTest {
  private User user;

  private MembershipService membershipService;

  @Before
  public void setUp() {
    user = new User();
    user.setUserUID(1L);

    ApplicationContext appCtx = new ClassPathXmlApplicationContext("classpath*:/appCtx/**/*.xml");

    membershipService = (MembershipService) appCtx.getBean("membershipService");
  }

  /**
   * Test the renewMemberships method.
   */
  @Test
  @Ignore
  public void testRenewMemberships() {
    DateTime transDate = new DateTime(2015, 3, 1, 0, 0);
    String transDesc = "2015 - 2016 Dues";
    String transMemo = "Due upon receipt";

    JobDetail jobDetail1 = new JobDetail();
    jobDetail1.setParentEntityName(Job.MEMBERSHIP);
    jobDetail1.setParentEntityUID(1L);
    jobDetail1.setJobStatus(JobStatus.SUBMITTED);

    JobDetail jobDetail2 = new JobDetail();
    jobDetail2.setParentEntityName(Job.MEMBERSHIP);
    jobDetail2.setParentEntityUID(2L);
    jobDetail2.setJobStatus(JobStatus.SUBMITTED);

    Job job = new Job();
    job.setJobName("TEST_JOB");
    job.setJobType(JobType.RENEWAL);
    job.setJobStatus(JobStatus.SUBMITTED);
    job.setJobDetails(Lists.newArrayList(jobDetail1, jobDetail2));

    membershipService.renewMemberships(transDate, transDesc, transMemo, job, user);
  }
}
