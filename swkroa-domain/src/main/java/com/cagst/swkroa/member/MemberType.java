package com.cagst.swkroa.member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Collator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

/**
 * Representation of a Member Type within the system.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public final class MemberType implements Serializable, Comparable<MemberType> {
	private static final long serialVersionUID = -8799075313261854806L;

	public static final String MEMBER_ASSOCIATE = "MEMBER_ASSOCIATE";
	public static final String MEMBER_FAMILY_HEAD = "MEMBER_FAMILY_HEAD";
	public static final String MEMBER_FAMILY_MEMBER = "MEMBER_FAMILY_MEMBER";
	public static final String MEMBER_REGULAR = "MEMBER_REGULAR";
	public static final String MEMBER_SPOUSE = "MEMBER_SPOUSE";
	public static final String MEMBER_MAIL_LIST = "MEMBER_MAIL_LIST";

	private long member_type_id;
	private long prev_member_type_id;
	private String member_type_display;
	private String member_type_meaning;
	private BigDecimal dues_amount;
	private DateTime beg_eff_dt_tm;
	private DateTime end_eff_dt_tm;
	private boolean active_ind = true;
	private long updt_cnt;

	public long getMemberTypeUID() {
		return member_type_id;
	}

	/* package */void setMemberTypeUID(final long uid) {
		this.member_type_id = uid;
	}

	public long getPreviousMemberTypeUID() {
		return prev_member_type_id;
	}

	/* package */void setPreviousMemberTypeUID(final long prevUID) {
		this.prev_member_type_id = prevUID;
	}

	public String getMemberTypeDisplay() {
		return member_type_display;
	}

	public void setMemberTypeDisplay(final String display) {
		this.member_type_display = display;
	}

	public String getMemberTypeMeaning() {
		return member_type_meaning;
	}

	public void setMemberTypeMeaning(final String meaning) {
		this.member_type_meaning = meaning;
	}

	public BigDecimal getDuesAmount() {
		return dues_amount;
	}

	public void setDuesAmount(final BigDecimal duesAmount) {
		this.dues_amount = duesAmount;
	}

	public DateTime getBeginEffectiveDateTime() {
		return beg_eff_dt_tm;
	}

	public void setBeginEffectiveDateTime(final DateTime effectiveDateTime) {
		this.beg_eff_dt_tm = effectiveDateTime;
	}

	public DateTime getEndEffectiveDateTime() {
		return end_eff_dt_tm;
	}

	public void setEndEffectiveDateTime(final DateTime effectiveDateTime) {
		this.end_eff_dt_tm = effectiveDateTime;
	}

	public boolean isActive() {
		return active_ind;
	}

	public void setActive(final boolean active) {
		this.active_ind = active;
	}

	public long getMemberTypeUpdateCount() {
		return updt_cnt;
	}

	/* package */void setMemberTypeUpdateCount(final long updateCount) {
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
		builder.append(member_type_display);

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
		if (!(obj instanceof MemberType)) {
			return false;
		}

		MemberType rhs = (MemberType) obj;

		EqualsBuilder builder = new EqualsBuilder();
		builder.append(member_type_display, rhs.getMemberTypeDisplay());

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
		builder.append("display", member_type_display);
		builder.append("meaning", member_type_meaning);
		builder.append("duesAmount", dues_amount);
		builder.append("begEffDtTm", beg_eff_dt_tm);
		builder.append("endEffDtTm", end_eff_dt_tm);

		return builder.build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final MemberType rhs) {
		Collator collator = Collator.getInstance();
		collator.setStrength(Collator.PRIMARY);

		return collator.compare(member_type_display, rhs.getMemberTypeDisplay());
	}
}
