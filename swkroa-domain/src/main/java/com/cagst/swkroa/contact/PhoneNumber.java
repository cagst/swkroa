package com.cagst.swkroa.contact;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.cagst.swkroa.codevalue.CodeValue;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a PhoneNumber within the system.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public final class PhoneNumber implements Serializable, Comparable<PhoneNumber> {
	private static final long serialVersionUID = 8469738041039540107L;

	private long phone_id;
	private long parent_entity_id;
	private String parent_entity_name;
	private CodeValue phone_type;
	private String phone_number;
	private String phone_extension;

	// meta-data
	private boolean active_ind = true;
	private long updt_cnt;

	public long getPhoneUID() {
		return phone_id;
	}

	/* package */void setPhoneUID(final long uid) {
		this.phone_id = uid;
	}

	@JsonIgnore
	public long getParentEntityUID() {
		return parent_entity_id;
	}

	public void setParentEntityUID(final long uid) {
		this.parent_entity_id = uid;
	}

	@JsonIgnore
	public String getParentEntityName() {
		return parent_entity_name;
	}

	public void setParentEntityName(final String name) {
		this.parent_entity_name = name;
	}

	@NotNull
	public CodeValue getPhoneType() {
		return phone_type;
	}

	public void setPhoneType(final CodeValue phoneType) {
		this.phone_type = phoneType;
	}

	@NotNull
	@Size(min = 1, max = 25)
	public String getPhoneNumber() {
		return phone_number;
	}

	public void setPhoneNumber(final String phoneNumber) {
		if (phoneNumber != null) {
			this.phone_number = phoneNumber.replaceAll("[^0-9]", "");
		} else {
			this.phone_number = null;
		}
	}

	public String getPhoneExtension() {
		return phone_extension;
	}

	@Size(max = 10)
	public void setPhoneExtension(final String extension) {
		this.phone_extension = extension;
	}

	public boolean isActive() {
		return active_ind;
	}

	public void setActive(final boolean active) {
		this.active_ind = active;
	}

	public long getPhoneUpdateCount() {
		return updt_cnt;
	}

	/* package */void setPhoneUpdateCount(final long updateCount) {
		this.updt_cnt = updateCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(phone_number);
		builder.append(phone_extension);

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
		if (!(obj instanceof PhoneNumber)) {
			return false;
		}

		PhoneNumber rhs = (PhoneNumber) obj;

		EqualsBuilder builder = new EqualsBuilder();
		builder.append(phone_type, rhs.getPhoneType());
		builder.append(phone_number, rhs.getPhoneNumber());
		builder.append(phone_extension, rhs.getPhoneExtension());

		return builder.build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("type", phone_type);
		builder.append("number", phone_number);
		builder.append("extension", phone_extension);

		return builder.build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final PhoneNumber rhs) {
		CompareToBuilder builder = new CompareToBuilder();
		builder.append(phone_number, rhs.getPhoneNumber());
		builder.append(phone_extension, rhs.getPhoneExtension());

		return builder.build();
	}
}
