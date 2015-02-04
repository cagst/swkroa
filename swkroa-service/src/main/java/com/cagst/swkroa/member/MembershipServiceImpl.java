package com.cagst.swkroa.member;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.List;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.comment.Comment;
import com.cagst.swkroa.comment.CommentRepository;
import com.cagst.swkroa.contact.Address;
import com.cagst.swkroa.contact.ContactRepository;
import com.cagst.swkroa.contact.EmailAddress;
import com.cagst.swkroa.contact.PhoneNumber;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.transaction.TransactionRepository;
import com.cagst.swkroa.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link MembershipService} interface.
 *
 * @author Craig Gaskill
 */
@Named("membershipService")
/* package */ final class MembershipServiceImpl implements MembershipService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MembershipServiceImpl.class);

  private final MembershipRepository membershipRepo;
  private final MemberRepository memberRepo;
  private final ContactRepository contactRepo;
  private final CommentRepository commentRepo;
  private final TransactionRepository transactionRepo;

  /**
   * Primary Constructor used to create an instance of <i>MembershipServiceImpl</i>.
   *
   * @param membershipRepo
   *     The {@link MembershipRepository} used to retrieve {@link Membership Memberships}.
   * @param memberRepo
   *     The {@link MemberRepository} used to retrieve {@link Member Members} for a membership.
   * @param contactRepo
   *     The {@link ContactRepository} used to retrieve / persist {@link Address}, {@link PhoneNumber}, and
   *     {@link EmailAddress} objects.
   * @param commentRepo
   *     The {@link CommentRepository} used to retrieve / persist {@link Comment} objects related to a
   *     {@link Membership}.
   * @param transactionRepo
   *     The {@link TransactionRepository} used to retrieve / persist {@link Transaction} objects related to a
   *     {@link Membership}.
   */
  @Inject
  public MembershipServiceImpl(final MembershipRepository membershipRepo,
                               final MemberRepository memberRepo,
                               final ContactRepository contactRepo,
                               final CommentRepository commentRepo,
                               final TransactionRepository transactionRepo) {

    this.membershipRepo = membershipRepo;
    this.memberRepo = memberRepo;
    this.contactRepo = contactRepo;
    this.commentRepo = commentRepo;
    this.transactionRepo = transactionRepo;
  }

  @Override
  public Membership getMembershipByUID(final long uid) {
    LOGGER.info("Calling getMembershipByUID for [{}]", uid);

    Membership membership = membershipRepo.getMembershipByUID(uid);

    List<Member> members = memberRepo.getMembersForMembership(membership);
    membership.setMembers(members);

    for (Member member : membership.getMembers()) {
      member.setAddresses(contactRepo.getAddressesForMember(member));
      member.setPhoneNumbers(contactRepo.getPhoneNumbersForMember(member));
      member.setEmailAddresses(contactRepo.getEmailAddressesForMember(member));
    }

    List<MembershipCounty> counties = memberRepo.getMembershipCountiesForMembership(membership);
    membership.setMembershipCounties(counties);

    List<Comment> comments = commentRepo.getCommentsForMembership(membership);
    Collections.sort(comments);
    membership.setComments(comments);

    List<Transaction> transactions = transactionRepo.getTransactionsForMembership(membership);
    Collections.sort(transactions);
    membership.setTransactions(transactions);

    return membership;
  }

  @Override
  public List<Membership> getMemberships(final MembershipStatus status, final MembershipBalance balance) {
    LOGGER.info("Calling getActiveMemberships");

    return membershipRepo.getMemberships(status, balance);
  }

  @Override
  public List<Membership> getMembershipsForName(final String name, final MembershipStatus status, final MembershipBalance balance) {
    LOGGER.info("Calling getMembershipsByName for [{}]", name);

    return membershipRepo.getMembershipsByName(name, status, balance);
  }

  @Override
  public List<Membership> getMembershipsDueInXDays(final int days) {
    LOGGER.info("Calling getMembershipsDueInXDays for [{}]", days);

    return membershipRepo.getMembershipsDueInXDays(days);
  }

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

  @Override
  @Transactional
  public int closeMemberships(final List<Long> membershipIds, final CodeValue closeReason, final String closeText, final User user) {
    LOGGER.info("Calling closeMemberships");

    return membershipRepo.closeMemberships(membershipIds, closeReason, closeText, user);
  }
}
