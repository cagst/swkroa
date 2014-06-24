package com.cagst.swkroa.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

import com.cagst.common.util.CGTCollatorBuilder;
import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.comment.Comment;
import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.member.MemberType;
import com.cagst.swkroa.member.MemberTypeRepository;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.member.MembershipCounty;
import com.cagst.swkroa.transaction.Transaction;

/**
 * This class is the Model for the add/edit Membership page. It re-maps the attributes of a {@link Membership} for
 * editing on a web page.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
public final class MembershipModel implements Comparable<MembershipModel> {
	private final Membership membership;

	private MemberTypeRepository memberTypeRepo;

	private Member primary;
	private Member spouse;

	private List<Member> additional = new ArrayList<Member>();

	/**
	 * Default Constructor used to create a new instance of <i>MembershipModel</i>.
	 */
	public MembershipModel() {
		this.membership = new Membership();
	}

	/**
	 * Primary constructor used to create an instance of <i>MembershipModel</i>.
	 * 
	 * @param membership
	 *          The {@link Membership} that is being edited.
	 */
	public MembershipModel(final Membership membership) {
		this.membership = membership;

		// we need to un-flatten our list of members into our Primary member, their Spouse, and
		// additional members
		for (Member member : membership.getMembers()) {
			String memberType = member.getMemberType().getMemberTypeMeaning();

			if (MemberType.SPOUSE.equals(memberType)) {
				this.spouse = member;
			} else if (MemberType.FAMILY_MEMBER.equals(memberType)) {
				this.additional.add(member);
			} else {
				this.primary = member;
			}
		}
	}

	public void setMemberTypeRepository(final MemberTypeRepository memberTypeRepo) {
		this.memberTypeRepo = memberTypeRepo;
	}

	public long getMembershipUID() {
		return membership.getMembershipUID();
	}

	public void setMembershipUID(final long membershipID) {
		this.membership.setMembershipUID(membershipID);
	}

	@NotNull
	public MemberType getMembershipType() {
		if (primary != null) {
			return primary.getMemberType();
		}

		return null;
	}

	@NotNull
	public CodeValue getEntityType() {
		return membership.getEntityType();
	}

	public void setEntityType(final CodeValue entityType) {
		this.membership.setEntityType(entityType);
	}

	@NotNull
	public DateTime getDueOn() {
		return membership.getDueOn();
	}

	public void setDueOn(final DateTime dueOn) {
		this.membership.setDueOn(dueOn);
	}

	public BigDecimal getDuesAmount() {
		return membership.getDuesAmount();
	}

	/**
	 * The effective dues amount that is either calculated, based off the members and counties, or specified.
	 * 
	 * @return The effective dues amount for the membership.
	 */
	public BigDecimal getEffectiveDuesAmount() {
		return membership.getEffectiveDuesAmount();
	}

	/**
	 * @return The calculated dues amount for the membership.
	 */
	public BigDecimal getCalculatedDuesAmount() {
		return membership.getCalculatedDuesAmount();
	}

	public void setDuesAmount(final BigDecimal duesAmount) {
		this.membership.setDuesAmount(duesAmount);
	}

	public BigDecimal getAmountDue() {
		return membership.getAmountDue();
	}

	public void setAmountDue(final BigDecimal amountDue) {
		this.membership.setAmountDue(amountDue);
	}

	public boolean isActive() {
		return membership.isActive();
	}

	public void setActive(final boolean active) {
		this.membership.setActive(active);
	}

	public long getMembershipUpdateCount() {
		return membership.getMembershipUpdateCount();
	}

	public void setMembershipUpdateCount(final long updateCount) {
		this.membership.setMembershipUpdateCount(updateCount);
	}

	@Valid
	public Member getPrimaryMember() {
		return primary;
	}

	public Member getSafePrimaryMember() {
		if (primary == null) {
			return new Member();
		}

		return primary;
	}

	public void setPrimaryMember(final Member primary) {
		this.primary = primary;
	}

	public Member getPrimarySpouse() {
		return spouse;
	}

	public void setPrimarySpouse(final Member spouse) {
		this.spouse = spouse;
	}

	public List<MembershipCounty> getMembershipCounties() {
		return this.membership.getMembershipCounties();
	}

	public void setMembershipCounties(final List<MembershipCounty> counties) {
		this.membership.setMembershipCounties(counties);
	}

	public List<Member> getAdditionalMembers() {
		return additional;
	}

	public void setAdditionalMembers(final List<Member> members) {
		this.additional = members;
	}

	public List<Member> getAllMembers() {
		return this.membership.getMembers();
	}

	public void setAllMembers(final List<Member> members) {

	}

	public List<Comment> getComments() {
		return membership.getComments();
	}

	public void setComments(final List<Comment> comments) {
		membership.setComments(comments);
	}

	public List<Transaction> getTransactions() {
		return membership.getTransactions();
	}

	public void setTransactions(final List<Transaction> transactions) {
		membership.setTransactions(transactions);
	}

	public Membership build() {
		// we need to rebuild (flatten back out) our list of members associated with this membership
		membership.clearMembers();
		membership.addMember(primary);

		if (spouse != null) {
			spouse.setMemberType(memberTypeRepo.getMemberTypeByMeaning(MemberType.SPOUSE));
			membership.addMember(spouse);
		}

		for (Member member : additional) {
			membership.addMember(member);
		}

		return membership;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final MembershipModel rhs) {
		if (rhs == null) {
			return 0;
		}

		CGTCollatorBuilder builder = new CGTCollatorBuilder();
		builder.append(primary, rhs.getPrimaryMember());

		return builder.build();
	}
}
