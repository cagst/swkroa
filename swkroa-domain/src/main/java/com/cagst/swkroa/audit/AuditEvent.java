package com.cagst.swkroa.audit;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

/**
 * This class represents an auditable event within the system.
 *
 * @author Craig Gaskill
 */
public class AuditEvent implements Serializable {
  private static final long serialVersionUID = 1L;

  private final AuditEventType audit_event_type;
  private final String audit_action;
  private final String audit_instigator;
  private final String audit_message;
  private final DateTime create_dt_tm;

  /**
   * Primary Constructor used to create an instance of a new <i>AuditEvent</i>.
   *
   * @param eventType
   *     A {@link AuditEventType} that represents the type of audit event.
   * @param action
   *     A {@link String} that identifies the action that caused the audit event.
   * @param instigator
   *     A {@link String} that identifies the individual / system that instigated the audit
   *     event.
   * @param message
   *     A {@link String} that identifies any additional information to associate with the
   *     audit event.
   */
  public AuditEvent(final AuditEventType eventType, final String action, final String instigator, final String message) {
    this.audit_event_type = eventType;
    this.audit_action = action;
    this.audit_instigator = instigator;
    this.audit_message = message;
    this.create_dt_tm = null;
  }

  /**
   * Constructor used to create an instance of an existing <i>AuditEvent</i>.
   *
   * @param eventType
   *     A {@link AuditEventType} that represents the type of audit event.
   * @param action
   *     A {@link String} that identifies the action that caused the audit event.
   * @param instigator
   *     A {@link String} that identifies the individual / system that instigated the audit
   *     event.
   * @param message
   *     A {@link String} that identifies any additional information to associate with the
   *     audit event. triggered.
   * @param createDtTm
   *     A {@link DateTime} that represents when the audit event was created.
   */
  /* package */AuditEvent(final AuditEventType eventType, final String action, final String instigator,
                          final String message, final DateTime createDtTm) {

    this.audit_event_type = eventType;
    this.audit_action = action;
    this.audit_instigator = instigator;
    this.audit_message = message;
    this.create_dt_tm = createDtTm;
  }

  public AuditEventType getAuditEventType() {
    return audit_event_type;
  }

  public String getAuditAction() {
    return audit_action;
  }

  public String getAuditInstigator() {
    return audit_instigator;
  }

  public String getAuditMessage() {
    return audit_message;
  }

  public DateTime getCreateDateTime() {
    return create_dt_tm;
  }

  @Override
  public int hashCode() {
    return Objects.hash(audit_event_type, audit_action, audit_instigator, audit_message);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof AuditEvent)) {
      return false;
    }

    AuditEvent rhs = (AuditEvent) obj;

    return Objects.equals(audit_event_type, rhs.getAuditEventType()) &&
        Objects.equals(audit_action, rhs.getAuditAction()) &&
        Objects.equals(audit_instigator, rhs.getAuditInstigator()) &&
        Objects.equals(audit_message, rhs.getAuditMessage());
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);

    builder.append("event_type", audit_event_type);
    builder.append("action", audit_action);
    builder.append("instigator", audit_instigator);
    builder.append("message", audit_message);

    return builder.build();
  }
}
