package com.cagst.swkroa.controller.api;

import javax.inject.Inject;

import java.util.List;

import com.cagst.swkroa.exception.BadRequestException;
import com.cagst.swkroa.transaction.Transaction;
import com.cagst.swkroa.transaction.TransactionRepository;
import com.cagst.swkroa.transaction.UnpaidInvoice;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    try {
      // determine if this is a new transaction
      boolean newTransaction = (transaction.getTransactionUID() == 0);

      // save the transaction
      Transaction savedTransaction = transactionRepo.saveTransaction(transaction, WebAppUtils.getUser());

      return new ResponseEntity<Transaction>(savedTransaction, newTransaction ? HttpStatus.CREATED : HttpStatus.OK);
    } catch (OptimisticLockingFailureException ex) {
      return new ResponseEntity<Transaction>(transaction, HttpStatus.CONFLICT);
    } catch (Exception ex) {
      return new ResponseEntity<Transaction>(transaction, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
