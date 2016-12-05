package com.cagst.swkroa.controller.api;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

import com.cagst.swkroa.deposit.Deposit;
import com.cagst.swkroa.deposit.DepositService;
import com.cagst.swkroa.exception.ResourceNotFoundException;
import com.cagst.swkroa.web.util.WebAppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
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
@RolesAllowed({"ROLE_STAFF", "ROLE_ADMIN"})
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
  public DepositApiController(DepositService depositService) {
    this.depositService = depositService;
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<Deposit> getDeposits() {
    LOGGER.info("Received request to retrieve deposits.");

    return depositService.getDeposits();
  }

  @RequestMapping(value = "/{depositId}", method = RequestMethod.GET)
  public Deposit getDeposit(@PathVariable("depositId") long depositId) {
    LOGGER.info("Received request to retrieve deposit [{}].", depositId);

    if (depositId == 0L) {
      Deposit deposit = new Deposit();
      deposit.setDepositDate(LocalDate.now());

      return deposit;
    }

    try {
      return depositService.getDeposit(depositId);
    } catch (IncorrectResultSizeDataAccessException ex) {
      throw new ResourceNotFoundException(ex);
    }
  }

  /**
   * Handles the request and persists the {@link Deposit} to persistent storage.
   *
   * @param deposit
   *    The {@link Deposit} to persist.
   *
   * @return The {@link Deposit} after it has been persisted.
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Deposit> saveDeposit(@RequestBody Deposit deposit) {
    LOGGER.info("Received request to save deposit.");

    // determine if this is a new deposit
    boolean newDeposit = (deposit.getDepositUID() == 0);

    // save the deposit
    Deposit savedDeposit = depositService.saveDeposit(deposit, WebAppUtils.getUser());

    return new ResponseEntity<>(savedDeposit, newDeposit ? HttpStatus.CREATED : HttpStatus.OK);
  }
}
