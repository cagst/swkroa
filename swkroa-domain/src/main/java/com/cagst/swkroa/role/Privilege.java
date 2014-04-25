package com.cagst.swkroa.role;

import java.io.Serializable;
import java.text.Collator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Representation of a Privilege within the system.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
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

	@NotNull
	@Size(min = 1, max = 25)
	public String getPrivilegeKey() {
		return privilege_key;
	}

	public void setPrivilegeKey(final String key) {
		this.privilege_key = key;
	}

	@NotNull
	@Size(min = 1, max = 50)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("privilege_key", privilege_key);
		builder.append("privilege_name", privilege_name);

		return builder.build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(privilege_key);

		return builder.build();
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
		if (!(obj instanceof Privilege)) {
			return false;
		}

		Privilege rhs = (Privilege) obj;

		EqualsBuilder builder = new EqualsBuilder();
		builder.append(privilege_key, rhs.getPrivilegeKey());

		return builder.build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final Privilege rhs) {
		Collator collator = Collator.getInstance();
		collator.setStrength(Collator.PRIMARY);

		return collator.compare(privilege_name, rhs.getPrivilegeName());
	}
}
