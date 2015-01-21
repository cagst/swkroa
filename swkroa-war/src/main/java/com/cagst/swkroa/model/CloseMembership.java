package com.cagst.swkroa.model;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.member.Membership;

/**
 * A model that contains information need to close a membership.
 *
 * @author Craig Gaskill
 */
public final class CloseMembership {
  private CodeValue closeReason;
  private String closeText;
  private Membership membership;

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

  public Membership getMembership() {
    return membership;
  }

  public void setMembership(final Membership membership) {
    this.membership = membership;
  }
}
