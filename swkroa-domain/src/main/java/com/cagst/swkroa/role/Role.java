package com.cagst.swkroa.role;

import java.io.Serializable;
import java.text.Collator;

import com.google.auto.value.AutoValue;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Representation of a Role within the system.
 *
 * @author Craig Gaskill
 */
@AutoValue
public abstract class Role implements Serializable, Comparable<Role> {
  public abstract long getRoleUID();

  public abstract String getRoleName();

  public abstract String getRoleKey();

  public abstract boolean isActive();

  public abstract long getRoleUpdateCount();

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    builder.append("name", getRoleName());
    builder.append("key", getRoleKey());

    return builder.build();
  }

  @Override
  public int hashCode() {
    return getRoleKey().hashCode();
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

    return getRoleKey().equals(rhs.getRoleKey());
  }

  @Override
  public int compareTo(final Role rhs) {
    Collator collator = Collator.getInstance();
    collator.setStrength(Collator.PRIMARY);

    return collator.compare(getRoleKey(), rhs.getRoleName());
  }

  public static Builder builder() {
    return new AutoValue_Role.Builder();
  }

  @AutoValue.Builder
  abstract static class Builder {
    public abstract Builder setRoleUID(long uid);

    public abstract Builder setRoleName(String name);

    public abstract Builder setRoleKey(String key);

    public abstract Builder setActive(boolean active);

    public abstract Builder setRoleUpdateCount(long updateCount);

    public abstract Role build();
  }
}
