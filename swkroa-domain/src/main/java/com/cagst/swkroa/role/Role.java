package com.cagst.swkroa.role;

import java.io.Serializable;
import java.text.Collator;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;

/**
 * Representation of a Role within the system.
 *
 * @author Craig Gaskill
 */
public final class Role implements Serializable, Comparable<Role> {
  private static final long serialVersionUID = 5588184824390805426L;

  private long role_id;
  private String role_key;
  private String role_name;
  private boolean active_ind = true;
  private long updt_cnt;

  private final Map<String, Privilege> privileges = new HashMap<String, Privilege>();

  public long getRoleUID() {
    return role_id;
  }

  /* package */void setRoleUID(final long roleUid) {
    this.role_id = roleUid;
  }

  public String getRoleKey() {
    return role_key;
  }

  public void setRoleKey(final String key) {
    this.role_key = key;
  }

  public String getRoleName() {
    return role_name;
  }

  public void setRoleName(final String name) {
    this.role_name = name;
  }

  public boolean isActive() {
    return active_ind;
  }

  public void setActive(final boolean active) {
    this.active_ind = active;
  }

  public long getRoleUpdateCount() {
    return updt_cnt;
  }

  /* package */void setRoleUpdateCount(final long updateCount) {
    this.updt_cnt = updateCount;
  }

  /**
   * Clears all {@link Privilege Privileges} associated with this Role.
   */
  public void clearPrivileges() {
    this.privileges.clear();
  }

  /**
   * Adds (associates) the specified {@link Privilege} to this Role.
   *
   * @param privilege
   *     The {@link Privilege} to add (associate) to this Role.
   */
  public void addPrivilege(final Privilege privilege) {
    Assert.notNull(privilege, "[Assertion failed] - argument [privilege] is required; it must not be null");

    privileges.put(privilege.getPrivilegeKey(), privilege);
  }

  /**
   * Removes (dis-associates) the specified {@link Privilege} from this Role.
   *
   * @param privilege
   *     The {@link Privilege} to remove (dis-associate) from this Role.
   */
  public void removePrivilege(final Privilege privilege) {
    Assert.notNull(privilege, "[Assertion failed] - argument [privilege] is required; it must not be null");

    privileges.remove(privilege.getPrivilegeKey());
  }

  /**
   * @return A {@link Collection} of {@link Privilege Privileges} associated with this Role.
   */
  public Collection<Privilege> getPrivileges() {
    return privileges.values();
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("role_key", role_key);
    builder.append("role_name", role_name);

    return builder.build();
  }

  @Override
  public int hashCode() {
    return role_key.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Role)) {
      return false;
    }

    Role rhs = (Role) obj;

    return role_key.equals(rhs.getRoleKey());
  }

  @Override
  public int compareTo(final Role rhs) {
    Collator collator = Collator.getInstance();
    collator.setStrength(Collator.PRIMARY);

    return collator.compare(role_name, rhs.getRoleName());
  }
}
