package com.cagst.swkroa.controller.api;

import javax.inject.Inject;

import java.util.List;

import com.cagst.swkroa.model.ListModel;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.transaction.TransactionGroup;
import com.cagst.swkroa.transaction.TransactionRepository;
import com.cagst.swkroa.transaction.TransactionType;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles and retrieves {@link Transaction} objects depending on the URI template.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping(value = "/api/transactions")
public final class TransactionApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionApiController.class);

  private final TransactionRepository transactionRepo;

  @Inject
  public TransactionApiController(final TransactionRepository transactionRepo) {
    this.transactionRepo = transactionRepo;
  }

  /**
   * Handles the request to retrieve the {@link List} of {$link TransactionGroup} for invoices in the system.
   *
   * @return The {@link List} of {@link TransactionGroup} for invoices in the system.
   */
  @RequestMapping(value = "/invoices", method = RequestMethod.GET)
  public ResponseEntity<ListModel<TransactionGroup>> getTransactionGroupsForInvoices(
      final @RequestParam(value = "start", required = false) Integer start,
      final @RequestParam(value = "limit", required = false) Integer limit) {

    int newStart = (start != null ? start : 0);
    int newLimit = (limit != null ? limit : 20);

    LOGGER.info("Received request to retrieve transaction groups for invoices starting at [{}] with limit of[{}]", newStart, newLimit);

    ListModel<TransactionGroup> model = new ListModel<TransactionGroup>(
        transactionRepo.getTransactionGroupsForType(TransactionType.INVOICE, newStart, newLimit),
        transactionRepo.getCountOfTransactionGroupsForType(TransactionType.INVOICE)
    );

    return new ResponseEntity<ListModel<TransactionGroup>>(model, HttpStatus.OK);
  }

  /**
   * Handles the request to retrieve the {@link List} of {@link TransactionGroup} for payments in the system.
   *
   * @return A JSON representation of the {@link TransactionGroup TransactionGroups} defined within the system.
   */
  @RequestMapping(value = "/payments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ListModel<TransactionGroup>> getTransactionGroupsForPayments(
      final @RequestParam(value = "start", required = false) Integer start,
      final @RequestParam(value = "limit", required = false) Integer limit) {

    int newStart = (start != null ? start : 0);
    int newLimit = (limit != null ? limit : 20);

    LOGGER.info("Received request to retrieve transaction groups for payments starting at [{}] with limit of[{}]", newStart, newLimit);

    ListModel<TransactionGroup> model = new ListModel<TransactionGroup>(
        transactionRepo.getTransactionGroupsForType(TransactionType.PAYMENT, newStart, newLimit),
        transactionRepo.getCountOfTransactionGroupsForType(TransactionType.PAYMENT)
    );

    return new ResponseEntity<ListModel<TransactionGroup>>(model, HttpStatus.OK);
  }

  /**
   * Handles the request and persists the {@link Transaction} to persistent storage.
   *
   * @param transaction
   *     The {@link Transaction} to persist.
   *
   * @return The {@link Transaction} after it has been persisted.
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Transaction> saveTransaction(final @RequestBody Transaction transaction) {
    LOGGER.info("Received request to save transaction.");

    // determine if this is a new transaction
    boolean newTransaction = (transaction.getTransactionUID() == 0);

    // save the transaction
    Transaction savedTransaction = transactionRepo.saveTransaction(transaction, WebAppUtils.getUser());

    return new ResponseEntity<Transaction>(savedTransaction, newTransaction ? HttpStatus.CREATED : HttpStatus.OK);
  }
}
