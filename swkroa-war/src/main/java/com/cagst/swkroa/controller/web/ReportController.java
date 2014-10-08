package com.cagst.swkroa.controller.web;

import com.cagst.swkroa.report.JasperReportsViewFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

  private static final String MEMBERSHIP_LISTING = "/WEB-INF/reports/jasper/MembershipListingReport.jasper";
  private static final String MEMBERSHIP_PASTDUE = "/WEB-INF/reports/jasper/MembershipPastDueReport.jasper";
  private static final String MEMBERSHIP_STATUS  = "/WEB-INF/reports/jasper/MembershipStatusReport.jasper";

  private static final String MEMBER_MAILINGLIST_CSV = "/WEB-INF/reports/jasper/MemberMailingListCsv.jasper";
  private static final String MEMBER_MAILINGLIST_PDF = "/WEB-INF/reports/jasper/MemberMailingListPdf.jasper";
  private static final String MEMBER_EMAILLIST_CSV   = "/WEB-INF/reports/jasper/MemberEmailListCsv.jasper";
  private static final String MEMBER_EMAILLIST_PDF   = "/WEB-INF/reports/jasper/MemberEmailListPdf.jasper";

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
   * Handles the request to run/generate the Membership Listing report.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/membership/listing.pdf", method = RequestMethod.GET)
  public ModelAndView generateMembershipListingReportAsPdf(final HttpServletRequest request) {
    LOGGER.info("Received request to generate Membership Listing report.");

    String reportFilename = "Membership_Listing_" + dateFormat.format(new Date());

    return getReportModalAndView(request, MEMBERSHIP_LISTING, JasperReportsViewFactory.REPORT_FORMAT_PDF, reportFilename);
  }

  /**
   * Handles and retrieves the Membership Past Due report page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "/membership/pastdue", method = RequestMethod.GET)
  public ModelAndView getMembershipPastDueReport() {
    LOGGER.info("Received request to show Membership Past Due report options.");

    return new ModelAndView("report/membership/pastdue");
  }

  /**
   * Handles the request to run/generate the Membership Past Due report.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/membership/pastdue.pdf", method = RequestMethod.GET)
  public ModelAndView generateMembershipPastDueReportAsPdf(final HttpServletRequest request) {
    LOGGER.info("Received request to generate Membership Past Due report.");

    String reportFilename = "Membership_PastDue_" + dateFormat.format(new Date());

    return getReportModalAndView(request, MEMBERSHIP_PASTDUE, JasperReportsViewFactory.REPORT_FORMAT_PDF, reportFilename);
  }

  /**
   * Handles the request to run/generate the Membership Past Due report.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/membership/pastdue.xls", method = RequestMethod.GET)
  public ModelAndView generateMembershipPastDueReportAsXls(final HttpServletRequest request) {
    LOGGER.info("Received request to generate Membership Past Due report.");

    String reportFilename = "Membership_PastDue_" + dateFormat.format(new Date());

    ModelAndView mav = getReportModalAndView(request, MEMBERSHIP_PASTDUE, JasperReportsViewFactory.REPORT_FORMAT_XLS, reportFilename);
    mav.addObject("IS_IGNORE_PAGINATION", true);

    return mav;
  }

  /**
   * Handles and retrieves the Membership Status report page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "/membership/status", method = RequestMethod.GET)
  public ModelAndView getMembershipStatusReport() {
    LOGGER.info("Received request to show Membership Status report options.");

    return new ModelAndView("report/membership/status");
  }

  /**
   * Handles the request to run/generate the Membership Status report.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/membership/status", method = RequestMethod.POST)
  public ModelAndView generateMembershipStatusReport(final HttpServletRequest request) {
    LOGGER.info("Received request to generate Membership Status report.");

    String reportFilename = "Membership_Status_" + dateFormat.format(new Date());

    return getReportModalAndView(request, MEMBERSHIP_STATUS, JasperReportsViewFactory.REPORT_FORMAT_PDF, reportFilename);
  }

  /**
   * Handles and retrieves the Member Mailing List report page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "/member/mailinglist", method = RequestMethod.GET)
  public ModelAndView getMemberMailingList() {
    LOGGER.info("Received request to show Member Mailing List options.");

    return new ModelAndView("report/member/mailinglist");
  }

  /**
   * Handles the request to run/generate the Member Mailing List report.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/member/mailinglist", method = RequestMethod.POST)
  public ModelAndView generateMemberMailingList(final @RequestParam("reportType") String reportType,
                                                final HttpServletRequest request) {

    LOGGER.info("Received request to generate Member Mailing List report.");

    String newslettersOnly = request.getParameter("newslettersOnly");

    String reportFilename = "Member_MailingList_" + dateFormat.format(new Date());

    ModelAndView mav = null;
    if (JasperReportsViewFactory.REPORT_FORMAT_PDF.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBER_MAILINGLIST_PDF, JasperReportsViewFactory.REPORT_FORMAT_PDF, reportFilename);
    } else if (JasperReportsViewFactory.REPORT_FORMAT_CSV.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBER_MAILINGLIST_CSV, JasperReportsViewFactory.REPORT_FORMAT_CSV, reportFilename);
    }

    if (mav != null) {
      mav.addObject("newsletters_only", BooleanUtils.toBoolean(newslettersOnly));
    }

    return mav;
  }

  /**
   * Handles and retrieves the Member Email List report page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "/member/emaillist", method = RequestMethod.GET)
  public ModelAndView getMemberEmailList() {
    LOGGER.info("Received request to show Member Email List options.");

    return new ModelAndView("report/member/emaillist");
  }

  /**
   * Handles the request to run/generate the Member Email List report.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/member/emaillist", method = RequestMethod.POST)
  public ModelAndView generateMemberEmailList(final @RequestParam("reportType") String reportType,
                                              final HttpServletRequest request) {

    LOGGER.info("Received request to generate Member Email List report.");

    String newslettersOnly = request.getParameter("newslettersOnly");

    String reportFilename = "Member_EmailList_" + dateFormat.format(new Date());

    ModelAndView mav = null;
    if (JasperReportsViewFactory.REPORT_FORMAT_PDF.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBER_EMAILLIST_PDF, JasperReportsViewFactory.REPORT_FORMAT_PDF, reportFilename);
    } else if (JasperReportsViewFactory.REPORT_FORMAT_CSV.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBER_EMAILLIST_CSV, JasperReportsViewFactory.REPORT_FORMAT_CSV, reportFilename);
    }

    if (mav != null) {
      mav.addObject("newsletters_only", BooleanUtils.toBoolean(newslettersOnly));
    }

    return mav;
  }

  private ModelAndView getReportModalAndView(final HttpServletRequest request,
                                             final String reportUrl,
                                             final String format,
                                             final String filename) {

    AbstractJasperReportsView view = JasperReportsViewFactory.getJasperReportsView(
        request,
        dataSource,
        reportUrl,
        format,
        filename);

    return new ModelAndView(view);
  }
}
