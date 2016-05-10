package com.cagst.swkroa.job;

import com.cagst.swkroa.codevalue.CodeValue;
import com.cagst.swkroa.codevalue.CodeValueRepository;
import com.cagst.swkroa.document.Document;
import com.cagst.swkroa.document.DocumentRepository;
import com.cagst.swkroa.exception.NotFoundException;
import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.member.MemberRepository;
import com.cagst.swkroa.member.MemberType;
import com.cagst.swkroa.member.MemberTypeRepository;
import com.cagst.swkroa.member.Membership;
import com.cagst.swkroa.member.MembershipRepository;
import com.cagst.swkroa.report.jasper.JasperReportLoader;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.transaction.TransactionEntry;
import com.cagst.swkroa.transaction.TransactionRepository;
import com.cagst.swkroa.transaction.TransactionType;
import com.cagst.swkroa.user.User;
import com.google.common.net.MediaType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link JobService} interface.
 *
 * @author Craig Gaskill
 */
@Named("jobService")
public class JobServiceImpl implements JobService {
  private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);

  private final JobRepository jobRepo;
  private final CodeValueRepository codeValueRepo;
  private final MembershipRepository membershipRepo;
  private final MemberRepository memberRepo;
  private final MemberTypeRepository memberTypeRepo;
  private final DocumentRepository documentRepo;
  private final TransactionRepository transactionRepo;

  private final DataSource dataSource;

  /**
   * Primary Constructor used to create an instance of <i>JobServiceImpl</i>.
   *
   * @param jobRepo
   *    The {@link JobRepository} used to retrieve {@link Job} and {@link JobDetail} objects.
   */
  @Inject
  public JobServiceImpl(final JobRepository jobRepo,
                        final CodeValueRepository codeValueRepo,
                        final MembershipRepository membershipRepo,
                        final MemberRepository memberRepo,
                        final MemberTypeRepository memberTypeRepo,
                        final DocumentRepository documentRepo,
                        final TransactionRepository transactionRepo,
                        final DataSource dataSource) {
    this.jobRepo = jobRepo;
    this.codeValueRepo = codeValueRepo;
    this.membershipRepo = membershipRepo;
    this.memberRepo = memberRepo;
    this.memberTypeRepo = memberTypeRepo;
    this.documentRepo = documentRepo;
    this.transactionRepo = transactionRepo;

    this.dataSource = dataSource;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void processRenewalJob(final JobDetail jobDetail,
                                final String transactionDescription,
                                final DateTime transactionDate,
                                final String transactionMemo,
                                final User user) {
    // set JobDetail as started (In-Process)
    jobDetail.setJobStatus(JobStatus.INPROCESS);
    jobRepo.saveJobDetail(jobDetail, user);

    long membershipId = jobDetail.getParentEntityUID();

    Membership membership = membershipRepo.getMembershipByUID(membershipId);
    if (membership == null) {
      // set JobDetail as completed (FAILED)
      jobDetail.setJobStatus(JobStatus.FAILED);
      jobRepo.saveJobDetail(jobDetail, user);

      throw new NotFoundException("Membership [" + membershipId + "] was not found.");
    }

    CodeValue renewalLetter = codeValueRepo.getCodeValueByMeaning(CodeValue.DOCUMENT_RENEWAL);

    CodeValue baseDues = codeValueRepo.getCodeValueByMeaning("TRANS_DUES_BASE");
    CodeValue familyDues = codeValueRepo.getCodeValueByMeaning("TRANS_DUES_FAMILY");
    CodeValue incrementalDues = codeValueRepo.getCodeValueByMeaning("TRANS_DUES_INC");

    try {
      // load the report template
      JasperReport jasperReport = JasperReportLoader.getReport(JasperReportLoader.MEMBERSHIP_RENEWAL_PDF);

      // Create the Document for the Renewal Membership Letter
      byte[] reportContent = generateDueRenewalReport(jasperReport, membershipId, transactionDescription);

      Document document = new Document();
      document.setParentEntityUID(membershipId);
      document.setParentEntityName(Document.MEMBERSHIP);
      document.setDocumentType(renewalLetter);
      document.setDocumentName(transactionDescription);
      document.setDocumentFormat(MediaType.PDF.toString());
      document.setDocumentContents(reportContent);
      document.setBeginEffectiveDate(new DateTime());
      document.setDocumentDescription(transactionDescription);

      // Save the Renewal Membership Letter document
      documentRepo.saveDocument(document, user);

      List<Member> members = memberRepo.getMembersForMembership(membership);

      // Create Transaction
      Transaction invoice = new Transaction();
      invoice.setMembershipUID(membershipId);
      invoice.setTransactionDate(transactionDate);
      invoice.setTransactionDescription(transactionDescription);
      invoice.setMemo(transactionMemo);
      invoice.setTransactionType(TransactionType.INVOICE);
      invoice.setActive(true);

      // Create Transaction Entries
      for (Member member : members) {
        MemberType type = memberTypeRepo.getMemberTypeByUID(member.getMemberType().getMemberTypeUID());
        if (type.getDuesAmount().compareTo(BigDecimal.ZERO) > 0) {
          TransactionEntry entry = new TransactionEntry();
          entry.setTransaction(invoice);
          entry.setTransactionEntryType(type.isPrimary() ? baseDues : familyDues);
          entry.setTransactionEntryAmount(type.getDuesAmount().negate());
          entry.setActive(true);

          invoice.addEntry(entry);
        }
      }

      // Create the incremental dues transaction entry
      if (membership.getIncrementalDues() != null) {
        TransactionEntry entry = new TransactionEntry();
        entry.setTransaction(invoice);
        entry.setTransactionEntryType(incrementalDues);
        entry.setTransactionEntryAmount(membership.getIncrementalDues().negate());
        entry.setActive(true);

        invoice.addEntry(entry);
      }

      // Save the Invoice (transaction)
      transactionRepo.saveTransaction(invoice, user);

      // Update Membership (next_due_dt)
      membershipRepo.updateNextDueDate(membershipId, user);
    } catch (Exception ex) {
      // set JobDetail as completed (FAILED)
      jobDetail.setJobStatus(JobStatus.FAILED);
      jobRepo.saveJobDetail(jobDetail, user);

      return;
    }

    // set JobDetail as completed (SUCCEEDED)
    jobDetail.setJobStatus(JobStatus.SUCCEEDED);
    jobRepo.saveJobDetail(jobDetail, user);

  }

  private byte[] generateDueRenewalReport(final JasperReport jasperReport,
                                          final long membershipId,
                                          final String transactionDescription) {

    Connection connection = null;
    try {
      connection = dataSource.getConnection();

      List<Long> membershipIds = new ArrayList<>(1);
      membershipIds.add(membershipId);

      Map<String, Object> params = new HashMap<>(2);
      params.put("memberships", membershipIds);
      params.put("membershipPeriod", transactionDescription);

      return JasperRunManager.runReportToPdf(jasperReport, params, connection);
    } catch (JRException|SQLException ex) {
      LOGGER.error(ex.getMessage(), ex);
      return null;
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException ex) {
          // ignore, we are closing
        }
      }
    }
  }
}
