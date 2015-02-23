package com.cagst.swkroa.dues;

import java.util.List;

/**
 * Definition of a repository that retrieves and persists dues objects.
 *
 * @author Craig Gaskill
 */
public interface DuesRepository {
  /**
   * Retrieves a {@link List} of {@link DuesRun DuesRuns} defined within the system.
   *
   * @return A {@link List} of {@link DuesRun DuesRuns} defined within the system.
   */
  public List<DuesRun> getDuesRuns();
}
