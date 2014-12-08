package com.cagst.swkroa.audit;

import java.util.Collection;

/**
 * Definition of a repository that retrieves and persists {@link AuditEvent} objects.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public interface AuditEventRepository {
  /**
   * Retrieves all {@link AuditEvent}s that exist within the system.
   *
   * @return A {@link Collection} of {@link AuditEvent}s that exist within the system.
   */
  public Collection<AuditEvent> getAllAuditEvents();

  /**
   * Persists the specified {@link AuditEvent} to persistent storage.
   *
   * @param auditEvent
   *     The {@link AuditEvent} to persist.
   */
  public void save(final AuditEvent auditEvent);
}
