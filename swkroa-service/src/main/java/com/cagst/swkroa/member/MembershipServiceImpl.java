package com.cagst.swkroa.member;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.cagst.swkroa.LoadingPolicy;
import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.comment.Comment;
import com.cagst.swkroa.comment.CommentRepository;
import com.cagst.swkroa.contact.Address;
import com.cagst.swkroa.contact.ContactRepository;
import com.cagst.swkroa.contact.EmailAddress;
import com.cagst.swkroa.contact.PhoneNumber;
import com.cagst.swkroa.document.Document;
import com.cagst.swkroa.document.DocumentRepository;
import com.cagst.swkroa.job.Job;
import com.cagst.swkroa.job.JobDetail;
import com.cagst.swkroa.job.JobRepository;
import com.cagst.swkroa.job.JobService;
import com.cagst.swkroa.job.JobStatus;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.transaction.TransactionRepository;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.user.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

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
  private final DocumentRepository documentRepository;
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
   * @param documentRepository
   *    The {@link DocumentRepository} used to retrieve / persist {@link Document} objects related to a {@link Membership}.
   */
  @Inject
  public MembershipServiceImpl(MembershipRepository membershipRepo,
                               MemberRepository memberRepo,
                               ContactRepository contactRepo,
                               CommentRepository commentRepo,
                               TransactionRepository transactionRepo,
                               DocumentRepository documentRepository,
                               JobRepository jobRepo,
                               JobService jobService) {
    this.membershipRepo = membershipRepo;
    this.memberRepo = memberRepo;
    this.contactRepo = contactRepo;
    this.commentRepo = commentRepo;
    this.transactionRepo = transactionRepo;
    this.documentRepository = documentRepository;
    this.jobRepo = jobRepo;
    this.jobService = jobService;
  }

  @Override
  public Membership getMembershipByUID(long uid, LoadingPolicy loadingPolicy) {
    LOGGER.info("Calling getMembershipByUID for [{}]", uid);

    Membership membership = membershipRepo.getMembershipByUID(uid);

    if (loadingPolicy.containsAttribute(LOAD_MEMBERS)) {
      List<Member> members = memberRepo.getMembersForMembership(membership);
      membership.setMembers(members);

      if (loadingPolicy.containsAttribute(LOAD_CONTACTS)) {
        for (Member member : membership.getMembers()) {
          member.setAddresses(contactRepo.getAddressesForMember(member));
          member.setPhoneNumbers(contactRepo.getPhoneNumbersForMember(member));
          member.setEmailAddresses(contactRepo.getEmailAddressesForMember(member));
        }
      }
    }

    if (loadingPolicy.containsAttribute(LOAD_COUNTIES)) {
      List<MembershipCounty> counties = memberRepo.getMembershipCountiesForMembership(membership);
      membership.setMembershipCounties(counties);
    }

    if (loadingPolicy.containsAttribute(LOAD_COMMENTS)) {
      List<Comment> comments = commentRepo.getCommentsForMembership(membership);
      Collections.sort(comments);
      membership.setComments(comments);
    }

    if (loadingPolicy.containsAttribute(LOAD_TRANSACTIONS)) {
      List<Transaction> transactions = transactionRepo.getTransactionsForMembership(membership);
      Collections.sort(transactions);
      membership.setTransactions(transactions);
    }

    if (loadingPolicy.containsAttribute(LOAD_DOCUMENTS)) {
      List<Document> documents = documentRepository.getDocumentsForMembership(membership);
      membership.setDocuments(documents);
    }

    return membership;
  }

  @Override
  public List<Membership> getMemberships(Status status, MembershipBalance balance) {
    LOGGER.info("Calling getActiveMemberships");

    return membershipRepo.getMemberships(status, balance);
  }

  @Override
  public List<Membership> getMembershipsForName(String name, Status status, MembershipBalance balance) {
    LOGGER.info("Calling getMembershipsByName for [{}]", name);

    return membershipRepo.getMembershipsByName(name, status, balance);
  }

  @Override
  public List<Membership> getMembershipsDueInXDays(int days) {
    LOGGER.info("Calling getMembershipsDueInXDays for [{}]", days);

    return membershipRepo.getMembershipsDueInXDays(days);
  }

  @Override
  @Transactional
  public Membership saveMembership(Membership membership, User user) {
    LOGGER.info("Calling saveMembership for [{}]", membership.getMembershipUID());

    Membership savedMembership = membershipRepo.saveMembership(membership, user);

    // save the Contact information (Addresses, Phone Numbers, Email Addresses)
    for (Member member : savedMembership.getMembers()) {
      for (Address address : member.getAddresses()) {
        address.setParentEntityUID(member.getMemberUID());
        address.setParentEntityName(UserType.MEMBER.name());

        contactRepo.saveAddress(address, user);
      }

      for (PhoneNumber phone : member.getPhoneNumbers()) {
        phone.setParentEntityUID(member.getMemberUID());
        phone.setParentEntityName(UserType.MEMBER.name());

        contactRepo.savePhoneNumber(phone, user);
      }

      for (EmailAddress email : member.getEmailAddresses()) {
        email.setParentEntityUID(member.getMemberUID());
        email.setParentEntityName(UserType.MEMBER.name());

        contactRepo.saveEmailAddress(email, user);
      }
    }

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
  public int closeMemberships(Set<Long> membershipIds, CodeValue closeReason, String closeText, User user) {
    LOGGER.info("Calling closeMemberships for [{}]", closeReason.getDisplay());

    return membershipRepo.closeMemberships(membershipIds, closeReason, closeText, user);
  }

  @Override
  @Async
  public void renewMemberships(LocalDate transactionDate,
                               String transactionDescription,
                               String transactionMemo,
                               Job job,
                               User user)
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
