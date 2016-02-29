package com.cagst.swkroa.job;

/**
 * Defines the available statuses a Job may be in.
 *
 * @author Craig Gaskill
 */
public enum JobStatus {
  SUBMITTED,
  INPROCESS,
  SUCCEEDED,
  FAILED,
  PARTIAL
}
