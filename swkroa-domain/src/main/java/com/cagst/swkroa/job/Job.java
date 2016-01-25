package com.cagst.swkroa.job;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Represents an (async) Job within the system.
 *
 * @author Craig Gaskill
 */
public final class Job implements Serializable {
  private long jobId;
  private String jobName;
  private JobType jobType;
  private JobStatus jobStatus;
  private String jobDetail;

  // meta-data
  private boolean active = true;
  private long updateCount;

  public long getJobUID() {
    return jobId;
  }

  public void setJobUID(final long uid) {
    this.jobId = uid;
  }

  public String getJobName() {
    return jobName;
  }

  public void setJobName(final String name) {
    this.jobName = name;
  }

  public JobType getJobType() {
    return jobType;
  }

  public void setJobType(final JobType type) {
    this.jobType = type;
  }

  public JobStatus getJobStatus() {
    return jobStatus;
  }

  public void setJobStatus(final JobStatus status) {
    this.jobStatus = status;
  }

  public String getJobDetail() {
    return jobDetail;
  }

  public void setJobDetail(final String detail) {
    this.jobDetail = detail;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public long getJobUpdateCount() {
    return updateCount;
  }

  public void setJobUpdateCount(final long updateCount) {
    this.updateCount = updateCount;
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append(jobName);
    builder.append(jobStatus);

    return builder.build();
  }
}
