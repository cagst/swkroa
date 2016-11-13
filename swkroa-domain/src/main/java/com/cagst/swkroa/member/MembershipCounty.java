package com.cagst.swkroa.member;

import java.io.Serializable;

import com.cagst.swkroa.county.County;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Representation of a Membership County within the system.
 *
 * A membership county is a county that the membership has mineral interests in. This is used to
 * calculate any additional charges to the membership above the base charge.
 *
 * @author Craig Gaskill
 */
public final class MembershipCounty implements Serializable {
  private static final long serialVersionUID = 1014489262063740891L;

  private long membership_county_id;
  private County county;
  private int net_mineral_acres;
  private int surface_acres;
  private boolean voting_ind;

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  /**
   * Default Constructor used to create a new, uninitialized, <i>MembershipCounty</i>.
   */
  public MembershipCounty() {
  }

  public long getMembershipCountyUID() {
    return membership_county_id;
  }

  /* package */void setMembershipCountyUID(final long uid) {
    this.membership_county_id = uid;
  }

  public County getCounty() {
    return county;
  }

  public void setCounty(final County county) {
    this.county = county;
  }

  public int getNetMineralAcres() {
    return net_mineral_acres;
  }

  public void setNetMineralAcres(final int mineralAcres) {
    this.net_mineral_acres = mineralAcres;
  }

  public int getSurfaceAcres() {
    return surface_acres;
  }

  public void setSurfaceAcres(final int surfaceAcres) {
    this.surface_acres = surfaceAcres;
  }

  public boolean isVotingCounty() {
    return voting_ind;
  }

  public void setVotingCounty(final boolean votingCounty) {
    this.voting_ind = votingCounty;
  }

  public boolean isActive() {
    return active_ind;
  }

  public void setActive(final boolean active) {
    this.active_ind = active;
  }

  public long getMembershipCountyUpdateCount() {
    return updt_cnt;
  }

  public void setMembershipCountyUpdateCount(final long updateCount) {
    this.updt_cnt = updateCount;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return county.hashCode();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof MembershipCounty)) {
      return false;
    }

    MembershipCounty rhs = (MembershipCounty) obj;

    return county.equals(rhs.getCounty());
  }

  /*
   * (non-Javadoc)
   *
   * @see com.cagst.swkroa.county.County#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.appendSuper(super.toString());
    builder.append("net_mineral_acres", net_mineral_acres);
    builder.append("surface_acres", surface_acres);
    builder.append("voting", voting_ind);

    return builder.build();
  }
}
