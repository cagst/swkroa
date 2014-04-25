package com.cagst.swkroa.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.cagst.common.util.CGTCollatorBuilder;
import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.member.Member;

/**
 * Representation of a Transaction within the system.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public final class Transaction implements Serializable, Comparable<Transaction> {
	private static final long serialVersionUID = -5519051342330445823L;

	private long transaction_id;
	private long membership_id;
	private Member member;
	private DateTime transaction_dt;
	private CodeValue transaction_type;
	private BigDecimal transaction_amount;
	private String ref_num;
	private String memo_txt;

	private List<Transaction> related_transactions = new ArrayList<Transaction>();

	// meta-data
	private boolean active_ind = true;
	private long updt_cnt;

	public long getTransactionUID() {
		return transaction_id;
	}

	public void setTransactionUID(final long uid) {
		this.transaction_id = uid;
	}

	public long getMembershipUID() {
		return membership_id;
	}

	public void setMembershipUID(final long uid) {
		this.membership_id = uid;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(final Member member) {
		this.member = member;
	}

	public DateTime getTransactionDate() {
		return transaction_dt;
	}

	public void setTransactionDate(final DateTime transactionDate) {
		this.transaction_dt = transactionDate;
	}

	public CodeValue getTransactionType() {
		return transaction_type;
	}

	public void setTransactionType(final CodeValue transactionType) {
		this.transaction_type = transactionType;
	}

	public BigDecimal getTransactionAmount() {
		return transaction_amount;
	}

	public void setTransactionAmount(final BigDecimal transactionAmount) {
		this.transaction_amount = transactionAmount;
	}

	public String getReferenceNumber() {
		return ref_num;
	}

	public void setReferenceNumber(final String refNum) {
		this.ref_num = refNum;
	}

	public String getMemo() {
		return memo_txt;
	}

	public void setMemo(final String memo) {
		this.memo_txt = memo;
	}

	public void clearRelatedTransactions() {
		related_transactions.clear();
	}

	public void addRelatedTransaction(final Transaction transaction) {
		related_transactions.add(transaction);
	}

	public void removeRelatedTransaction(final Transaction transaction) {
		related_transactions.remove(transaction);
	}

	public Collection<Transaction> getRelatedTransactions() {
		return Collections.unmodifiableList(related_transactions);
	}

	public void setRelatedTransactions(final List<Transaction> relatedTransactions) {
		this.related_transactions = relatedTransactions;
	}

	public boolean isActive() {
		return active_ind;
	}

	public void setActive(final boolean active) {
		this.active_ind = active;
	}

	public long getTransactionUpdateCount() {
		return updt_cnt;
	}

	public void setTransactionUpdateCount(final long updateCount) {
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
		builder.append("transaction_dt", transaction_dt);
		builder.append("transaction_type", transaction_type);
		builder.append("transaction_amount", transaction_amount);
		builder.append("ref_num", ref_num);
		builder.append("memo", memo_txt);

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
		builder.append(membership_id);
		builder.append(transaction_type);
		builder.append(transaction_dt);
		builder.append(transaction_amount);

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
		if (!(obj instanceof Transaction)) {
			return false;
		}

		Transaction rhs = (Transaction) obj;

		EqualsBuilder builder = new EqualsBuilder();
		builder.append(membership_id, rhs.getMembershipUID());
		builder.append(transaction_type, rhs.getTransactionType());
		builder.append(transaction_dt, rhs.getTransactionDate());
		builder.append(transaction_amount, rhs.getTransactionAmount());

		return builder.build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final Transaction rhs) {
		if (rhs == null) {
			return 0;
		}

		CGTCollatorBuilder builder = new CGTCollatorBuilder();
		builder.append(transaction_dt, rhs.getTransactionDate());

		return builder.build();
	}
}
