package com.cagst.swkroa.model;

import java.util.List;

import com.cagst.swkroa.codevalue.CodeValue;

/**
 * A model that contains information need to close memberships.
 *
 * @author Craig Gaskill
 */
public final class CloseMemberships {
  private CodeValue closeReason;
  private String closeText;
  private List<Long> memberships;

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

  public List<Long> getMemberships() {
    return memberships;
  }

  public void setMemberships(final List<Long> memberships) {
    this.memberships = memberships;
  }
}
