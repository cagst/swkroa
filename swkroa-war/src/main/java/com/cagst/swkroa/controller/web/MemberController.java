package com.cagst.swkroa.controller.web;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.cagst.swkroa.document.Document;
import com.cagst.swkroa.document.DocumentRepository;
import com.cagst.swkroa.member.Member;
import com.cagst.swkroa.member.MemberRepository;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.transaction.TransactionRepository;
import com.cagst.swkroa.user.User;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles and retrieves the member page(s) depending on the URI template.
 *
 * @author Craig Gaskill
 */
@Controller
@RolesAllowed("ROLE_MEMBER")
@RequestMapping(value = "/member")
public class MemberController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);

  private final MemberRepository memberRepository;
  private final DocumentRepository documentRepository;
  private final TransactionRepository transactionRepository;

  @Inject
  public MemberController(MemberRepository memberRepository,
                          DocumentRepository documentRepository,
                          TransactionRepository transactionRepository
  ) {
    this.memberRepository = memberRepository;
    this.documentRepository = documentRepository;
    this.transactionRepository = transactionRepository;
  }

  @RequestMapping(value = "/home", method = RequestMethod.GET)
  public ModelAndView getMemberHomePage() {
    LOGGER.info("Received request to show the member's home page.");

    ModelAndView mav = new ModelAndView("member/home");

    User user = WebAppUtils.getUser();
    if (user != null) {
      Optional<Member> checkMember = memberRepository.getMemberByPersonUID(user.getPersonUID());
      if (checkMember.isPresent()) {
        Member member = checkMember.get();
        mav.addObject("membershipUID", member.getMembershipUID());
        mav.addObject("isPrimaryMember", member.getMemberType().isPrimary());
      }
    }

    return mav;
  }

  @RequestMapping(value = "/transactions", method = RequestMethod.GET)
  public ModelAndView getTransactionsPage() {
    LOGGER.info("Received request to show the member's transaction page.");

    ModelAndView mav = new ModelAndView("member/transactions");

    User user = WebAppUtils.getUser();
    if (user != null) {
      Optional<Member> checkMember = memberRepository.getMemberByPersonUID(user.getPersonUID());
      if (checkMember.isPresent()) {
        long membershipUID = checkMember.get().getMembershipUID();

        List<Transaction> transactions = transactionRepository.getTransactionsForMembership(membershipUID);
        transactions.sort(Comparator.comparing(Transaction::getTransactionDate).reversed());

        mav.addObject("membershipUID", membershipUID);
        mav.addObject("transactions", transactions);
      }
    }

    return mav;
  }

  @RequestMapping(value = "/documents", method = RequestMethod.GET)
  public ModelAndView getDocumentsPage() {
    LOGGER.info("Received request to show the member's documents page.");

    ModelAndView mav = new ModelAndView("member/documents");

    User user = WebAppUtils.getUser();
    if (user != null) {
      Optional<Member> checkMember = memberRepository.getMemberByPersonUID(user.getPersonUID());
      if (checkMember.isPresent()) {
        long membershipUID = checkMember.get().getMembershipUID();

        List<Document> documents = documentRepository.getDocumentsForMembership(membershipUID);
        documents.sort(Comparator.comparing(Document::getBeginEffectiveDate).reversed());

        mav.addObject("membershipUID", membershipUID);
        mav.addObject("documents", documents);
      }
    }

    return mav;
  }

  /**
   * Handles and retrieve a Single Document to view.
   *
   * @param documentId
   *    A {@link long} that uniquely identifies the Document to retrieve.
   *
   * @return A {@link ResponseEntity} that contains the document for display.
   */
  @RequestMapping(value = "/documents/{documentId}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> viewDocument(@PathVariable("documentId") long documentId) {
    LOGGER.info("Received request to view the document [{}]", documentId);

    Document document = documentRepository.getDocumentByUID(documentId);
    if (document == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    MediaType mediaType;
    try {
      mediaType = MediaType.parseMediaType(document.getDocumentFormat());
    } catch (InvalidMediaTypeException ex) {
      LOGGER.warn("Unable to parse document format [{}] as media type.", document.getDocumentFormat());

      mediaType = MediaType.ALL;
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(mediaType);

    return new ResponseEntity<>(document.getDocumentContents(), headers, HttpStatus.OK);
  }

}
