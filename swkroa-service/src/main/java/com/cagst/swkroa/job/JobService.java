package com.cagst.swkroa.job;

import java.time.LocalDate;

import com.cagst.swkroa.user.User;

/**
 * Definitions of a service that retrieves and persists {@link Job} and {@link JobDetail} objects.
 *
 * @author Craig Gaskill
 */
public interface JobService {
  void processRenewalJob(final JobDetail jobDetail,
                         final String transactionDescription,
                         final LocalDate transactionDate,
                         final String transactionMemo,
                         final User user);
}
