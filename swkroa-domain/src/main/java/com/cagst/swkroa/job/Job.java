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
  public static final String MEMBERSHIP = "MEMBERSHIP";

  private long jobId;
  private String jobName;
  private JobType jobType;
  private JobStatus jobStatus;
  private long parent_entity_id;
  private String parent_entity_name;
  private long createId;

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

  public long getParentEntityUID() {
    return parent_entity_id;
  }

  public void setParentEntityUID(final long uid) {
    this.parent_entity_id = uid;
  }

  public String getParentEntityName() {
    return parent_entity_name;
  }

  public void setParentEntityName(final String name) {
    this.parent_entity_name = name;
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

  public long getCreateUID() {
    return createId;
  }

  public void setCreateUID(final long userId) {
    this.createId = userId;
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append(jobName);
    builder.append(jobStatus);

    return builder.build();
  }
}
