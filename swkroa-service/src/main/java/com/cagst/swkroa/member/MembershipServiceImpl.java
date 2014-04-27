package com.cagst.swkroa.member;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cagst.swkroa.comment.Comment;
import com.cagst.swkroa.comment.CommentRepository;
import com.cagst.swkroa.contact.Address;
import com.cagst.swkroa.contact.ContactRepository;
import com.cagst.swkroa.contact.EmailAddress;
import com.cagst.swkroa.contact.PhoneNumber;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.transaction.TransactionRepository;
import com.cagst.swkroa.user.User;

/**
 * Implementation of the {@link MembershipService} interface.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
@Service("membershipService")
public final class MembershipServiceImpl implements MembershipService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MembershipServiceImpl.class);

	private final MembershipRepository membershipRepo;
	private final ContactRepository contactRepo;
	private final CommentRepository commentRepo;
	private final TransactionRepository transactionRepo;

	/**
	 * Primary Constructor used to create an instance of <i>MembershipServiceImpl</i>.
	 * 
	 * @param membershipRepo
	 *          The {@link MembershipRepository} used to retrieve {@link Membership Memberships}.
	 * @param contactRepo
	 *          The {@link ContactRepository} used to retrieve / persist {@link Address}, {@link PhoneNumber}, and
	 *          {@link EmailAddress} objects.
	 * @param commentRepo
	 *          The {@link CommentRepository} used to retrieve / persist {@link Comment} objects related to a
	 *          {@link Membership}.
	 * @param transactionRepo
	 *          The {@link TransactionRepository} used to retrieve / persist {@link Transaction} objects related to a
	 *          {@link Membership}.
	 * 
	 */
	public MembershipServiceImpl(final MembershipRepository membershipRepo, final ContactRepository contactRepo,
			final CommentRepository commentRepo, final TransactionRepository transactionRepo) {

		this.membershipRepo = membershipRepo;
		this.contactRepo = contactRepo;
		this.commentRepo = commentRepo;
		this.transactionRepo = transactionRepo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.member.MembershipService#getActiveMemberships()
	 */
	@Override
	public List<Membership> getActiveMemberships() {
		LOGGER.info("Calling getActiveMemberships");

		return membershipRepo.getActiveMemberships();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.cagst.swkroa.member.MembershipService#getMembershipsForName()
	 */
	@Override
	public List<Membership> getMembershipsForName(final String name) {
		LOGGER.info("Calling getMembershipsByName for [{}]", name);

		return membershipRepo.getMembershipsByName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.member.MembershipService#getMembershipByUID(long)
	 */
	@Override
	public Membership getMembershipByUID(final long uid) {
		LOGGER.info("Calling getMembershipByUID for [{}]", uid);

		Membership membership = membershipRepo.getMembershipByUID(uid);
		for (Member member : membership.getMembers()) {
			member.setAddresses(contactRepo.getAddressesForMember(member));
			member.setPhoneNumbers(contactRepo.getPhoneNumbersForMember(member));
			member.setEmailAddresses(contactRepo.getEmailAddressesForMember(member));
		}

		List<Comment> comments = commentRepo.getCommentsForMembership(membership);
		Collections.sort(comments);
		membership.setComments(comments);

		List<Transaction> transactions = transactionRepo.getTransactionsForMembership(membership);
		Collections.sort(transactions);
		membership.setTransactions(transactions);

		return membership;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cagst.swkroa.member.MembershipService#saveMembership(com.cagst.swkroa.member.Membership,
	 * com.cagst.swkroa.user.User)
	 */
	@Override
	@Transactional
	public Membership saveMembership(final Membership membership, final User user) {
		LOGGER.info("Calling saveMembership for [{}]", membership.getMembershipUID());

		Membership savedMembership = membershipRepo.saveMembership(membership, user);

		// save Comments
		for (Comment comment : savedMembership.getComments()) {
			comment.setParentEntityUID(savedMembership.getMembershipUID());
			comment.setParentEntityName(Comment.MEMBERSHIP);

			commentRepo.saveComment(comment, user);
		}

		// save Transactions
		for (Transaction transaction : savedMembership.getTransactions()) {
			transaction.setMembershipUID(savedMembership.getMembershipUID());

			transactionRepo.saveTransaction(transaction, user);
		}

		return savedMembership;
	}
}