package com.cagst.swkroa.role;

import java.io.Serializable;
import java.text.Collator;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Representation of a Privilege within the system.
 *
 * @author Craig Gaskill
 */
public final class Privilege implements Serializable, Comparable<Privilege> {
  private static final long serialVersionUID = -3136199957460235366L;

  private long privilege_id;
  private String privilege_key;
  private String privilege_name;
  private boolean active_ind = true;
  private long updt_cnt;

  public long getPrivilegeUID() {
    return privilege_id;
  }

  /* package */void setPrivilegeUID(final long uid) {
    this.privilege_id = uid;
  }

  public String getPrivilegeKey() {
    return privilege_key;
  }

  public void setPrivilegeKey(final String key) {
    this.privilege_key = key;
  }

  public String getPrivilegeName() {
    return privilege_name;
  }

  public void setPrivilegeName(final String name) {
    this.privilege_name = name;
  }

  public boolean isActive() {
    return active_ind;
  }

  public void setActive(final boolean active) {
    this.active_ind = active;
  }

  public long getPrivilegeUpdateCount() {
    return updt_cnt;
  }

  /* package */void setPrivilegeUpdateCount(final long updateCount) {
    this.updt_cnt = updateCount;
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("privilege_key", privilege_key);
    builder.append("privilege_name", privilege_name);

    return builder.build();
  }

  @Override
  public int hashCode() {
    return privilege_key.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Privilege)) {
      return false;
    }

    Privilege rhs = (Privilege) obj;

    return privilege_key.equals(rhs.getPrivilegeKey());
  }

  @Override
  public int compareTo(final Privilege rhs) {
    Collator collator = Collator.getInstance();
    collator.setStrength(Collator.PRIMARY);

    return collator.compare(privilege_name, rhs.getPrivilegeName());
  }
}
