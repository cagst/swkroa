package com.cagst.swkroa.controller.web;

import com.cagst.swkroa.deposit.DepositRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles and retrieves the Deposit page depending on the URI template.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@Controller
public class DepositController {
  private static final Logger LOGGER = LoggerFactory.getLogger(DepositController.class);

  @Autowired
  private DepositRepository depositRepo;

  @RequestMapping(value = "/accounting/deposits", method = RequestMethod.GET)
  public String getDepositPage() {
    LOGGER.info("Received request to show the deposit page.");

    return "accounting/deposit";
  }
}
