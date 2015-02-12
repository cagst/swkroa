package com.cagst.swkroa.model;

import java.util.Set;

import com.cagst.swkroa.codevalue.CodeValue;

/**
 * A model that contains information need to close memberships.
 *
 * @author Craig Gaskill
 */
public final class CloseMembershipsModel {
  private CodeValue closeReason;
  private String closeText;
  private Set<Long> memberships;

  public CodeValue getCloseReason() {
    return closeReason;
  }

  public void setCloseReason(final CodeValue reason) {
    this.closeReason = reason;
  }

  public String getCloseText() {
    return closeText;
  }

  public void setCloseText(final String closeText) {
    this.closeText = closeText;
  }

  public Set<Long> getMembershipIds() {
    return memberships;
  }

  public void setMembershipIds(final Set<Long> memberships) {
    this.memberships = memberships;
  }
}
