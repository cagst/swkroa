package com.cagst.swkroa.controller.api;

import javax.inject.Inject;

import java.util.List;

import com.cagst.swkroa.billing.BillingRepository;
import com.cagst.swkroa.billing.BillingRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles and retrieves {@link BillingRun} objects depending on the URI template.
 *
 * @author Craig Gaskill
 */
@RestController
@RequestMapping("/api/billing")
public class BillingApiController {
  private static final Logger LOGGER = LoggerFactory.getLogger(BillingApiController.class);

  private final BillingRepository billingRepo;

  @Inject
  public BillingApiController(final BillingRepository billingRepo) {
    this.billingRepo = billingRepo;
  }

  /**
   * Handles the request and retrieves the Billing Runs within the system.
   *
   * @return A JSON representation of the Billing Runs within the system.
   */
  @RequestMapping(method = RequestMethod.GET)
  public List<BillingRun> getBillingRuns() {
    return billingRepo.getBillingRuns();
  }
}
