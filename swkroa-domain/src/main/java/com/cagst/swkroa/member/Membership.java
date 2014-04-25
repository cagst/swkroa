package com.cagst.swkroa.member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.comment.Comment;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.utils.DefaultMineralUtilities;
import com.cagst.swkroa.utils.MineralUtilities;

/**
 * Representation of a Membership within the system.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public final class Membership implements Serializable {
	private static final long serialVersionUID = 5583617519331577882L;

	public static final String MEMBERSHIP_ASSOCIATE = "MEMBERSHIP_ASSOCIATE";
	public static final String MEMBERSHIP_FAMILY = "MEMBERSHIP_FAMILY";
	public static final String MEMBERSHIP_REGULAR = "MEMBERSHIP_REGULAR";
	public static final String MEMBERSHIP_MAILING = "MEMBERSHIP_MAILING";

	private long membership_id;
	private CodeValue membership_type;
	private CodeValue entity_type;
	private DateTime due_on;
	private BigDecimal dues_amount;
	private BigDecimal amount_due;

	// meta-data
	private boolean active_ind = true;
	private long updt_cnt;

	private List<Member> members = new ArrayList<Member>();
	private List<MembershipCounty> counties = new ArrayList<MembershipCounty>();
	private List<Comment> comments = new ArrayList<Comment>();
	private List<Transaction> transactions = new ArrayList<Transaction>();

	private MineralUtilities mineralUtils = new DefaultMineralUtilities();

	public void setMineralUtilities(final MineralUtilities mineralUtils) {
		this.mineralUtils = mineralUtils;
	}

	public long getMembershipUID() {
		return membership_id;
	}

	public void setMembershipUID(final long membershipID) {
		this.membership_id = membershipID;
	}

	@NotNull
	public CodeValue getMembershipType() {
		return membership_type;
	}

	public void setMembershipType(final CodeValue membershipType) {
		this.membership_type = membershipType;
	}

	@NotNull
	public CodeValue getEntityType() {
		return entity_type;
	}

	public void setEntityType(final CodeValue entityType) {
		this.entity_type = entityType;
	}

	@NotNull
	public DateTime getDueOn() {
		return due_on;
	}

	public void setDueOn(final DateTime dueOn) {
		this.due_on = dueOn;
	}

	public BigDecimal getDuesAmount() {
		return dues_amount;
	}

	/**
	 * The effective dues amount that is either calculated, based off the members and counties, or specified.
	 * 
	 * @return The effective dues amount for the membership.
	 */
	public BigDecimal getEffectiveDuesAmount() {
		if (dues_amount != null) {
			return dues_amount;
		}

		return getCalculatedDuesAmount();
	}

	/**
	 * @return The calculated dues amount for the membership.
	 */
	public BigDecimal getCalculatedDuesAmount() {
		BigDecimal countyDues = new BigDecimal(0d);
		for (MembershipCounty county : counties) {
			countyDues = countyDues.add(mineralUtils.calculateFeesForMembershipCounty(county));
		}

		BigDecimal memberDues = new BigDecimal(0d);
		for (Member member : members) {
			memberDues = memberDues.add(member.getMemberType().getDuesAmount());
		}

		return memberDues.add(countyDues);
	}

	public void setDuesAmount(final BigDecimal duesAmount) {
		this.dues_amount = duesAmount;
	}

	public BigDecimal getAmountDue() {
		return amount_due;
	}

	public void setAmountDue(final BigDecimal amountDue) {
		this.amount_due = amountDue;
	}

	public boolean isActive() {
		return active_ind;
	}

	public void setActive(final boolean active) {
		this.active_ind = active;
	}

	public long getMembershipUpdateCount() {
		return updt_cnt;
	}

	public void setMembershipUpdateCount(final long updateCount) {
		this.updt_cnt = updateCount;
	}

	public void clearMembers() {
		members.clear();
	}

	public void addMember(final Member member) {
		members.add(member);
	}

	public void removeMember(final Member member) {
		members.remove(member);
	}

	@Valid
	public List<Member> getMembers() {
		return Collections.unmodifiableList(members);
	}

	public void setMembers(final List<Member> members) {
		this.members = members;
	}

	public void clearCounties() {
		counties.clear();
	}

	public void addCounty(final MembershipCounty county) {
		counties.add(county);
	}

	public void removeCounty(final MembershipCounty county) {
		counties.remove(county);
	}

	public List<MembershipCounty> getMembershipCounties() {
		return Collections.unmodifiableList(counties);
	}

	public void setMembershipCounties(final List<MembershipCounty> membershipCounties) {
		this.counties = membershipCounties;
	}

	public void clearComments() {
		comments.clear();
	}

	public void addComment(final Comment comment) {
		comments.add(comment);
	}

	public void removeComment(final Comment comment) {
		comments.remove(comment);
	}

	public List<Comment> getComments() {
		return Collections.unmodifiableList(comments);
	}

	public void setComments(final List<Comment> comments) {
		this.comments = comments;
	}

	public void clearTransactions() {
		transactions.clear();
	}

	public void addTransaction(final Transaction transaction) {
		this.transactions.add(transaction);
	}

	public void removeTransaction(final Transaction transaction) {
		this.transactions.remove(transaction);
	}

	public List<Transaction> getTransactions() {
		return Collections.unmodifiableList(transactions);
	}

	public void setTransactions(final List<Transaction> transactions) {
		this.transactions = transactions;
	}
}
