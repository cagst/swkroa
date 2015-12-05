package com.cagst.swkroa.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles and retrieves the Accounting page(s) depending on the URI template.
 *
 * @author Craig Gaskill
 */
@Controller
@RequestMapping("/accounting")
public final class AccountingController {
  private static final Logger LOGGER = LoggerFactory.getLogger(AccountingController.class);

  /**
   * Handles and retrieves the Invoices page.
   *
   * @return The location and name of the page template.
   */
  @RequestMapping(value = "/invoices", method = RequestMethod.GET)
  public String getInvoicesPage() {
    LOGGER.info("Received request to show invoices page");

    return "accounting/invoices";
  }

  /**
   * Handles and retrieves the New Invoices page.
   *
   * @return The location and name of the page template.
   */
  @RequestMapping(value = "/invoices/new", method = RequestMethod.GET)
  public String getInvoicesNewPage() {
    LOGGER.info("Received request to show new invoices page");

    return "accounting/new_invoice";
  }

  /**
   * Handles and retrieves the Payments page.
   *
   * @return The location and name of the page template.
   */
  @RequestMapping(value = "/payments", method = RequestMethod.GET)
  public String getPaymentsPage() {
    LOGGER.info("Received request to show payments page");

    return "accounting/payments";
  }

  /**
   * Handles and retrieves the Deposits page.
   *
   * @return The location and name of the page template.
   */
  @RequestMapping(value = "/deposits", method = RequestMethod.GET)
  public String getDepositPage() {
    LOGGER.info("Received request to show the deposit page.");

    return "accounting/deposit";
  }

  /**
   * Handles and retrieves the Delinquencies page.
   *
   * @return The location and name fo the page template.
   */
  @RequestMapping(value = "/delinquencies", method = RequestMethod.GET)
  public String getDelinquenciesPage() {
    LOGGER.info("Received request to show delinquencies page");

    return "accounting/delinquency";
  }
}
