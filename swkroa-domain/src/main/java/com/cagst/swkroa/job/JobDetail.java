package com.cagst.swkroa.job;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents an (async) Job within the system.
 *
 * @author Craig Gaskill
 */
public final class JobDetail implements Serializable {
  public static final String MEMBERSHIP = "MEMBERSHIP";

  private long jobDetailId;
  private long jobId;
  private JobStatus jobStatus;
  private long parentEntityId;
  private String parentEntityName;
  private long createId;

  // meta-data
  private boolean active = true;
  private long updateCount;

  public long getJobDetailUID() {
    return jobDetailId;
  }

  public void setJobDetailUID(final long jobDetailId) {
    this.jobDetailId = jobDetailId;
  }

  public long getJobUID() {
    return jobId;
  }

  public void setJobUID(final long uid) {
    this.jobId = uid;
  }

  public JobStatus getJobStatus() {
    return jobStatus;
  }

  public void setJobStatus(final JobStatus status) {
    this.jobStatus = status;
  }

  public long getParentEntityUID() {
    return parentEntityId;
  }

  public void setParentEntityUID(final long uid) {
    this.parentEntityId = uid;
  }

  public String getParentEntityName() {
    return parentEntityName;
  }

  public void setParentEntityName(final String name) {
    this.parentEntityName = name;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public long getJobDetailUpdateCount() {
    return updateCount;
  }

  public void setJobDetailUpdateCount(final long updateCount) {
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
    builder.append(parentEntityId);
    builder.append(parentEntityName);
    builder.append(jobStatus);

    return builder.build();
  }
}
