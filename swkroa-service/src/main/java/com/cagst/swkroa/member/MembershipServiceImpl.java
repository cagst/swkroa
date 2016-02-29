package com.cagst.swkroa.member;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.comment.Comment;
import com.cagst.swkroa.comment.CommentRepository;
import com.cagst.swkroa.contact.Address;
import com.cagst.swkroa.contact.ContactRepository;
import com.cagst.swkroa.contact.EmailAddress;
import com.cagst.swkroa.contact.PhoneNumber;
import com.cagst.swkroa.job.Job;
import com.cagst.swkroa.job.JobDetail;
import com.cagst.swkroa.job.JobRepository;
import com.cagst.swkroa.job.JobService;
import com.cagst.swkroa.job.JobStatus;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.transaction.TransactionRepository;
import com.cagst.swkroa.user.User;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the {@link MembershipService} interface.
 *
 * @author Craig Gaskill
 */
@Named("membershipService")
public final class MembershipServiceImpl implements MembershipService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MembershipServiceImpl.class);

  private final MembershipRepository membershipRepo;
  private final MemberRepository memberRepo;
  private final ContactRepository contactRepo;
  private final CommentRepository commentRepo;
  private final TransactionRepository transactionRepo;
  private final JobRepository jobRepo;
  private final JobService jobService;

  /**
   * Primary Constructor used to create an instance of <i>MembershipServiceImpl</i>.
   *
   * @param membershipRepo
   *    The {@link MembershipRepository} used to retrieve {@link Membership Memberships}.
   * @param memberRepo
   *    The {@link MemberRepository} used to retrieve {@link Member Members} for a membership.
   * @param contactRepo
   *    The {@link ContactRepository} used to retrieve / persist {@link Address}, {@link PhoneNumber}, and
   *    {@link EmailAddress} objects.
   * @param commentRepo
   *    The {@link CommentRepository} used to retrieve / persist {@link Comment} objects related to a
   *    {@link Membership}.
   * @param transactionRepo
   *    The {@link TransactionRepository} used to retrieve / persist {@link Transaction} objects related to a
   *    {@link Membership}.
   */
  @Inject
  public MembershipServiceImpl(final MembershipRepository membershipRepo,
                               final MemberRepository memberRepo,
                               final ContactRepository contactRepo,
                               final CommentRepository commentRepo,
                               final TransactionRepository transactionRepo,
                               final JobRepository jobRepo,
                               final JobService jobService) {
    this.membershipRepo = membershipRepo;
    this.memberRepo = memberRepo;
    this.contactRepo = contactRepo;
    this.commentRepo = commentRepo;
    this.transactionRepo = transactionRepo;
    this.jobRepo = jobRepo;
    this.jobService = jobService;
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
  public List<Membership> getMemberships(final Status status, final MembershipBalance balance) {
    LOGGER.info("Calling getActiveMemberships");

    return membershipRepo.getMemberships(status, balance);
  }

  @Override
  public List<Membership> getMembershipsForName(final String name, final Status status, final MembershipBalance balance) {
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
  public int closeMemberships(final Set<Long> membershipIds, final CodeValue closeReason, final String closeText, final User user) {
    LOGGER.info("Calling closeMemberships for [{}]", closeReason.getDisplay());

    return membershipRepo.closeMemberships(membershipIds, closeReason, closeText, user);
  }

  @Override
  @Async
  public void renewMemberships(final DateTime transactionDate,
                               final String transactionDescription,
                               final String transactionMemo,
                               final Job job,
                               final User user)
      throws DataAccessException {

    LOGGER.info("Calling createBillingInvoicesForMemberships [{}]", transactionDescription);

    // mark the job as being in-process
    job.setJobStatus(JobStatus.INPROCESS);
    jobRepo.saveJob(job, user);

    int succeeded = 0;
    int failed    = 0;

    for (JobDetail jobDetail : job.getJobDetails()) {
      jobService.processRenewalJob(jobDetail, transactionDescription, transactionDate, transactionMemo, user);

      if (jobDetail.getJobStatus() == JobStatus.SUCCEEDED) {
        succeeded++;
      } else {
        failed++;
      }
    }

    if (succeeded == job.getJobDetails().size()) {
      // if all the JobDetails succeeded, then the Job succeeded
      job.setJobStatus(JobStatus.SUCCEEDED);
    } else if (failed == job.getJobDetails().size()) {
      // if all the JobDetails failed, then the Job failed
      job.setJobStatus(JobStatus.FAILED);
    } else {
      job.setJobStatus(JobStatus.PARTIAL);
    }

    // clear out our JobDetails list since we don't need to persist them again
    job.setJobDetails(new ArrayList<>());

    jobRepo.saveJob(job, user);
  }
}
