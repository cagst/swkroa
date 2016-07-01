package com.cagst.swkroa.county;

import java.io.Serializable;
import java.util.Objects;

import com.cagst.common.util.CGTCollatorBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents a County within the system.
 *
 * @author Craig Gaskill
 */
public final class County implements Serializable, Comparable<County> {
  private static final long serialVersionUID = 6048112429446895825L;

  private static final String VOTING_COUNTY = "Voting County";
  private static final String NON_VOTING_COUNTY = "Non-Voting County";

  private long county_id;
  private String state_code;
  private String county_code;
  private String county_name;
  private boolean swkroa_county_ind;

  // meta-data
  private boolean active_ind = true;
  private long updt_cnt;

  public long getCountyUID() {
    return county_id;
  }

  public void setCountyUID(final long uid) {
    this.county_id = uid;
  }

  public String getState() {
    return state_code;
  }

  public void setState(final String stateCode) {
    this.state_code = stateCode;
  }

  public String getCountyCode() {
    return county_code;
  }

  public void setCountyCode(final String code) {
    this.county_code = code;
  }

  public String getCountyName() {
    return county_name;
  }

  public void setCountyName(final String name) {
    this.county_name = name;
  }

  public boolean isSwkroaCounty() {
    return swkroa_county_ind;
  }

  public String getVotingCounty() {
    return isSwkroaCounty() ? VOTING_COUNTY : NON_VOTING_COUNTY;
  }

  public void setSwkroaCounty(final boolean swkroaCounty) {
    this.swkroa_county_ind = swkroaCounty;
  }

  public boolean isActive() {
    return active_ind;
  }

  public void setActive(final boolean active) {
    this.active_ind = active;
  }

  public long getCountyUpdateCount() {
    return updt_cnt;
  }

  public void setCountyUpdateCount(final long count) {
    this.updt_cnt = count;
  }

  @Override
  public int hashCode() {
    return Objects.hash(state_code, county_code);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof County)) {
      return false;
    }

    County rhs = (County) obj;

    return Objects.equals(state_code, rhs.getState()) &&
        Objects.equals(county_code, rhs.getCountyCode());
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("state_code", state_code);
    builder.append("county_code", county_code);

    return builder.build();
  }

  @Override
  public int compareTo(final County rhs) {
    CGTCollatorBuilder builder = new CGTCollatorBuilder();

    builder.append(!isSwkroaCounty(), !rhs.isSwkroaCounty());
    builder.append(state_code, rhs.getState());
    builder.append(county_code, rhs.getCountyCode());

    return builder.build();
  }
}
