package com.cagst.swkroa.codevalue;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.cagst.common.util.CGTCollatorBuilder;
import com.cagst.common.util.CGTStringUtils;

/**
 * Represents a CodeValue within the system.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public final class CodeValue implements Serializable, Comparable<CodeValue> {
	private static final long serialVersionUID = 571293295760168134L;

	private long codeset_id;
	private long codevalue_id;
	private String codevalue_display;
	private String codevalue_meaning;

	// meta-data
	private boolean active_ind = true;
	private long updt_cnt;

	/**
	 * Gets the unique identifier for the {@link CodeSet} this CodeValue is associated with.
	 * 
	 * @return A {@link long} that uniquely identifies the {@link CodeSet} this CodeValue is associated with.
	 */
	public long getCodeSetUID() {
		return codeset_id;
	}

	/**
	 * Sets the unique identifier for the {@link CodeSet} this CodeValue should be associated with.
	 * 
	 * @param codeSetId
	 *          A {@link long} that uniquely identifies the {@link CodeSet} this CodeValue should be associated with.
	 */
	/* package */void setCodeSetUID(final long codeSetId) {
		this.codeset_id = codeSetId;
	}

	/**
	 * Gets the unique identifier for the CodeValue.
	 * 
	 * @return A {@link long} that uniquely identifies the CodeValue.
	 */
	public long getCodeValueUID() {
		return codevalue_id;
	}

	/**
	 * Sets the unique identifier for the CodeValue.
	 * 
	 * @param codeValueUID
	 *          A {@link long} that uniquely identifies the CodeValue.
	 */
	public void setCodeValueUID(final long codeValueUID) {
		this.codevalue_id = codeValueUID;
	}

	/**
	 * @return The display / name of the CodeValue.
	 */
	@NotNull
	@Size(min = 1, max = 50)
	public String getDisplay() {
		return codevalue_display;
	}

	/**
	 * Sets the display / name of the CodeValue.
	 * 
	 * @param display
	 *          The {@link String} display / name for the CodeValue.
	 */
	public void setDisplay(final String display) {
		this.codevalue_display = display;
	}

	/**
	 * Gets the meaning of the CodeValue.
	 * 
	 * @return A {@link String} that represents the meaning of the CodeValue.
	 */
	public String getMeaning() {
		return codevalue_meaning;
	}

	/**
	 * Sets the meaning of the CodeValue.
	 * 
	 * @param meaning
	 *          A {@link String} that represents the meaning of the CodeValue.
	 */
	public void setMeaning(final String meaning) {
		this.codevalue_meaning = CGTStringUtils.normalizeToKey(meaning);
	}

	/**
	 * Gets the active status of the CodeValue.
	 * 
	 * @return {@link boolean} <code>true</code> if the CodeValue is active, <code>false</code> otherwise.
	 */
	public boolean isActive() {
		return active_ind;
	}

	/**
	 * Sets the active status of the CodeValue.
	 * 
	 * @param active
	 *          {@link boolean} <code>true</code> to make the CodeValue active, <code>false</code> to make the object
	 *          inactive.
	 */
	public void setActive(final boolean active) {
		this.active_ind = active;
	}

	/**
	 * Gets the number of times this object has been updated.
	 * 
	 * @return {@link long} number of times the object has been updated.
	 */
	public long getCodeValueUpdateCount() {
		return updt_cnt;
	}

	/**
	 * Sets the number of times this object has been updated.
	 * 
	 * @param updateCount
	 *          {@link long} the number of times the object has been updated.
	 */
	/* package */void setCodeValueUpdateCount(final long updateCount) {
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
		builder.append(getCodeSetUID());
		builder.append(getDisplay());

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
		if (!(obj instanceof CodeValue)) {
			return false;
		}

		CodeValue rhs = (CodeValue) obj;

		EqualsBuilder builder = new EqualsBuilder();
		builder.append(getCodeSetUID(), rhs.getCodeSetUID());
		builder.append(getDisplay(), rhs.getDisplay());

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
		builder.append("display", getDisplay());
		builder.append("meaning", getMeaning());

		return builder.build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final CodeValue rhs) {
		CGTCollatorBuilder builder = new CGTCollatorBuilder();
		builder.append(getDisplay(), rhs.getDisplay());
		return builder.build();
	}
}
