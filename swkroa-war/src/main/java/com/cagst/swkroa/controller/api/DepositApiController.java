package com.cagst.swkroa.controller.api;

import javax.inject.Inject;

import java.util.List;

import com.cagst.swkroa.deposit.Deposit;
import com.cagst.swkroa.deposit.DepositService;
import com.cagst.swkroa.exception.ResourceNotFoundException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles and retrieves {@link Deposit} objects depending on the URI template.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping("/api/deposits")
public class DepositApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(DepositApiController.class);

  private final DepositService depositService;

  /**
   * Primary Constructor used to create an instance of <i>DepositApiController</i>.
   *
   * @param depositService
   *      The {@link DepositService} to use to retrieve / persist {@link Deposit} objects.
   */
  @Inject
  public DepositApiController(final DepositService depositService) {
    this.depositService = depositService;
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<Deposit> getDeposits() {
    LOGGER.info("Received request to retrieve deposits.");

    return depositService.getDeposits();
  }

  @RequestMapping(value = "/{depositId}", method = RequestMethod.GET)
  public Deposit getDeposit(final @PathVariable("depositId") long depositId) {
    LOGGER.info("Received request to retrieve deposit [{}].", depositId);

    if (depositId == 0L) {
      Deposit deposit = new Deposit();
      deposit.setDepositDate(new DateTime());

      return deposit;
    }

    try {
      return depositService.getDeposit(depositId);
    } catch (IncorrectResultSizeDataAccessException ex) {
      throw new ResourceNotFoundException(ex);
    }
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Deposit> saveDeposit(final @RequestBody Deposit deposit) {
    LOGGER.info("Received request to save deposit.");

    // determine if this is a new deposit
    boolean newDeposit = (deposit.getDepositUID() == 0);

    // the transactions coming in for this deposit are "invoice" transactions that are being paid
    // so we need to create new "payment" transactions for these "invoice" transactions and associate
    // the "invoice" transaction to the new "payment" transaction.

    // save the deposit

    return null;
  }
}
