package com.cagst.swkroa.billing;

import java.util.List;

/**
 * Definition of a repository that retrieves and persists billing objects.
 *
 * @author Craig Gaskill
 */
public interface BillingRepository {
  /**
   * Retrieves a {@link List} of {@link BillingRun BillingRuns} defined within the system.
   *
   * @return A {@link List} of {@link BillingRun BillingRuns} defined within the system.
   */
  public List<BillingRun> getBillingRuns();
}
