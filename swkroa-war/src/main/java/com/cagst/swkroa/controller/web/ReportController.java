package com.cagst.swkroa.controller.web;

import com.cagst.swkroa.report.JasperReportsViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles and retrieves the Report pages depending on the URI template.
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "/report")
public final class ReportController {
  private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

  private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  private static final String MEMBERSHIP_LISTING  = "/WEB-INF/reports/jasper/MembershipListingReport.jasper";
  private static final String MEMBERSHIP_PAST_DUE = "/WEB-INF/reports/jasper/MembershipPastDueReport.jasper";

  @Autowired
  private DataSource dataSource;

  /**
   * Handles and retrieves the Membership Listing report page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "/membership/listing", method = RequestMethod.GET)
  public ModelAndView getMembershipListingReport() {
    LOGGER.info("Received request to show Membership Listing report options.");

    return new ModelAndView("report/membership/listing");
  }

  /**
   * Handles the request to run/generate the Membership Listing report page.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/membership/listing", method = RequestMethod.POST)
  public ModelAndView runMembershipListingReport(final HttpServletRequest request) {
    LOGGER.info("Received request to run Membership Listing report.");

    String reportFilename = "Membership_Listing_" + dateFormat.format(new Date());

    AbstractJasperReportsView view = JasperReportsViewFactory.getJasperReportsView(
        request,
        dataSource,
        MEMBERSHIP_LISTING,
        JasperReportsViewFactory.REPORT_FORMAT_PDF,
        reportFilename);

    return new ModelAndView(view);
  }

  /**
   * Handles and retrieves the Membershp Past Due report page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "/membership/pastdue", method = RequestMethod.GET)
  public ModelAndView getMembershipPastDueReport() {
    LOGGER.info("Received request to show Membership Past Due report options.");

    return new ModelAndView("report/membership/pastdue");
  }

  /**
   * Handles the request to run/generate the Membership Past Due report page.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/membership/pastdue", method = RequestMethod.POST)
  public ModelAndView runMembershipPastDueReport(final HttpServletRequest request) {
    LOGGER.info("Received request to run Membership Listing report.");

    String reportFilename = "Membership_PastDue_" + dateFormat.format(new Date());

    AbstractJasperReportsView view = JasperReportsViewFactory.getJasperReportsView(
        request,
        dataSource,
        MEMBERSHIP_PAST_DUE,
        JasperReportsViewFactory.REPORT_FORMAT_XLS,
        reportFilename);

    ModelAndView mav = new ModelAndView(view);
    mav.addObject("IS_IGNORE_PAGINATION", true);

    return mav;
  }
}
