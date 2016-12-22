package com.cagst.swkroa.job;

import com.cagst.swkroa.user.User;
import org.joda.time.DateTime;

/**
 * Definitions of a service that retrieves and persists {@link Job} and {@link JobDetail} objects.
 *
 * @author Craig Gaskill
 */
public interface JobService {
  void processRenewalJob(final JobDetail jobDetail,
                         final String transactionDescription,
                         final DateTime transactionDate,
                         final String transactionMemo,
                         final User user);
}
