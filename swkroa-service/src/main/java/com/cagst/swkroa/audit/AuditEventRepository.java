package com.cagst.swkroa.audit;

import java.util.Collection;

/**
 * Definition of a repository that retrieves and persists {@link AuditEvent} objects.
 *
 * @author Craig Gaskill
 */
public interface AuditEventRepository {
  /**
   * Retrieves all {@link AuditEvent}s that exist within the system.
   *
   * @return A {@link Collection} of {@link AuditEvent}s that exist within the system.
   */
  Collection<AuditEvent> getAllAuditEvents();

  /**
   * Persists the specified {@link AuditEvent} to persistent storage.
   *
   * @param auditEvent
   *     The {@link AuditEvent} to persist.
   */
  void save(final AuditEvent auditEvent);
}
