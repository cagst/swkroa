package com.cagst.swkroa.controller.web;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cagst.swkroa.report.JasperReportsViewFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView;

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

  private static final String BASE_PATH = "/WEB-INF/reports/jasper/";

  private static final String MEMBERSHIP_LISTING_PDF    = BASE_PATH + "MembershipListingPdf.jasper";
  private static final String MEMBERSHIP_LISTING_CSV    = BASE_PATH + "MembershipListingCsv.jasper";
  private static final String MEMBERSHIP_STATUS         = BASE_PATH + "MembershipStatusReport.jasper";
  private static final String MEMBERSHIP_DELINQUENT_CSV = BASE_PATH + "accounting/MembershipDelinquencyCsv.jasper";
  private static final String MEMBERSHIP_DELINQUENT_PDF = BASE_PATH + "accounting/MembershipDelinquencyPdf.jasper";
  private static final String MEMBERSHIP_RENEWAL_PDF    = BASE_PATH + "accounting/MembershipDuesRenewalPdf.jasper";
  private static final String MEMBERSHIP_RENEWAL_CSV    = BASE_PATH + "accounting/MembershipDuesRenewalCsv.jasper";
  private static final String MEMBERSHIP_REMINDER_PDF   = BASE_PATH + "accounting/MembershipDuesReminderPdf.jasper";
  private static final String MEMBERSHIP_PAYMENTS_PDF   = BASE_PATH + "accounting/MembershipPaymentsPdf.jasper";
  private static final String MEMBERSHIP_PAYMENTS_CSV   = BASE_PATH + "accounting/MembershipPaymentsCsv.jasper";
  private static final String MEMBERSHIP_INVOICES_PDF   = BASE_PATH + "accounting/MembershipInvoicesPdf.jasper";
  private static final String MEMBERSHIP_INVOICES_CSV   = BASE_PATH + "accounting/MembershipInvoicesCsv.jasper";

  private static final String MEMBER_MAILINGLIST_CSV = BASE_PATH + "MemberMailingListCsv.jasper";
  private static final String MEMBER_MAILINGLIST_PDF = BASE_PATH + "MemberMailingListPdf.jasper";
  private static final String MEMBER_EMAILLIST_CSV   = BASE_PATH + "MemberEmailListCsv.jasper";
  private static final String MEMBER_EMAILLIST_PDF   = BASE_PATH + "MemberEmailListPdf.jasper";

  private static final String CUSTOM_MEMBERS_BY_COUNTY = BASE_PATH + "custom/MembersForCounties.jasper";

  @Inject
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
  @RequestMapping(value = "/membership/listing", method = RequestMethod.POST)
  public ModelAndView generateMembershipListingReportAsPdf(final @RequestParam("reportType") String reportType,
                                                           final HttpServletRequest request) {

    LOGGER.info("Received request to generate Membership Listing report.");

    String reportFilename = "Membership_Listing_" + dateFormat.format(new Date());

    ModelAndView mav = null;
    if (JasperReportsViewFactory.REPORT_FORMAT_PDF.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBERSHIP_LISTING_PDF, JasperReportsViewFactory.REPORT_FORMAT_PDF, reportFilename);
    } else if (JasperReportsViewFactory.REPORT_FORMAT_CSV.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBERSHIP_LISTING_CSV, JasperReportsViewFactory.REPORT_FORMAT_CSV, reportFilename);
    }

    return mav;
  }

  /**
   * Handles the request to run/generate the Membership Delinquency report.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/membership/delinquent", method = RequestMethod.POST)
  public ModelAndView generateMembershipDelinquentReport(final @RequestParam("reportType") String reportType,
                                                         final HttpServletRequest request) {

    LOGGER.info("Received request to generate Membership Delinquent report as [{}]", reportType);

    String reportFilename = "Membership_Delinquencies_" + dateFormat.format(new Date());

    ModelAndView mav = null;
    if (JasperReportsViewFactory.REPORT_FORMAT_PDF.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBERSHIP_DELINQUENT_PDF, JasperReportsViewFactory.REPORT_FORMAT_PDF, reportFilename);
    } else if (JasperReportsViewFactory.REPORT_FORMAT_CSV.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBERSHIP_DELINQUENT_CSV, JasperReportsViewFactory.REPORT_FORMAT_CSV, reportFilename);
    }

    Map<String, String[]> params = request.getParameterMap();

    List<Long> membershipIds = new ArrayList<Long>();
    for (Map.Entry<String, String[]> param : params.entrySet()) {
      if (param.getKey().contains("selectedMembership_")) {
        String[] sections = StringUtils.split(param.getKey(), "_");

        membershipIds.add(Long.valueOf(sections[1]));
      }
    }

    if (mav != null) {
      mav.addObject("memberships", membershipIds);
    }

    return mav;
  }

  /**
   * Handles the request to run/generate the Membership Dues Renewal report.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/membership/renew", method = RequestMethod.POST)
  public ModelAndView generateMembershipDuesRenewalReport(final @RequestParam("reportType") String reportType,
                                                          final HttpServletRequest request) {

    LOGGER.info("Received request to generate Membership Dues Renewal report as [{}]", reportType);

    String reportFilename = "Membership_Dues_Renewals_" + dateFormat.format(new Date());

    ModelAndView mav = null;
    if (JasperReportsViewFactory.REPORT_FORMAT_PDF.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBERSHIP_RENEWAL_PDF, JasperReportsViewFactory.REPORT_FORMAT_PDF, reportFilename);
    } else if (JasperReportsViewFactory.REPORT_FORMAT_CSV.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBERSHIP_RENEWAL_CSV, JasperReportsViewFactory.REPORT_FORMAT_CSV, reportFilename);
    }

    Map<String, String[]> params = request.getParameterMap();

    List<Long> membershipIds = new ArrayList<Long>();
    for (Map.Entry<String, String[]> param : params.entrySet()) {
      if (param.getKey().contains("selectedMembership_")) {
        String[] sections = StringUtils.split(param.getKey(), "_");

        membershipIds.add(Long.valueOf(sections[1]));
      }
    }

    String[] periods = params.get("membershipPeriod");

    if (mav != null) {
      mav.addObject("memberships", membershipIds);
      mav.addObject("membershipPeriod", periods[0]);
    }

    return mav;
  }

  /**
   * Handles the request to run/generate the Membership Dues Reminder report.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/membership/remind", method = RequestMethod.POST)
  public ModelAndView generateMembershipDuesRemindReport(final @RequestParam("reportType") String reportType,
                                                         final HttpServletRequest request) {

    LOGGER.info("Received request to generate Membership Dues Reminder report as [{}]", reportType);

    String reportFilename = "Membership_Dues_Reminders_" + dateFormat.format(new Date());

    ModelAndView mav = null;
    if (JasperReportsViewFactory.REPORT_FORMAT_PDF.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBERSHIP_REMINDER_PDF, JasperReportsViewFactory.REPORT_FORMAT_PDF, reportFilename);
    }

    Map<String, String[]> params = request.getParameterMap();

    List<Long> membershipIds = new ArrayList<Long>();
    for (Map.Entry<String, String[]> param : params.entrySet()) {
      if (param.getKey().contains("selectedMembership_")) {
        String[] sections = StringUtils.split(param.getKey(), "_");

        membershipIds.add(Long.valueOf(sections[1]));
      }
    }

    if (mav != null) {
      mav.addObject("memberships", membershipIds);
    }

    return mav;
  }

  /**
   * Handles the request to run/generate the Membership Payments report.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/membership/payments", method = RequestMethod.POST)
  public ModelAndView generateMembershipPaymentsReport(final @RequestParam("reportType") String reportType,
                                                       final HttpServletRequest request) {

    LOGGER.info("Received request to generate Membership Payments report as [{}]", reportType);

    String reportFilename = "Membership_Payments_" + dateFormat.format(new Date());

    ModelAndView mav = null;
    if (JasperReportsViewFactory.REPORT_FORMAT_PDF.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBERSHIP_PAYMENTS_PDF, JasperReportsViewFactory.REPORT_FORMAT_PDF, reportFilename);
    } else if (JasperReportsViewFactory.REPORT_FORMAT_CSV.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBERSHIP_PAYMENTS_CSV, JasperReportsViewFactory.REPORT_FORMAT_CSV, reportFilename);
    }

    String startDate = request.getParameter("start_date");
    String endDate   = request.getParameter("end_date");

    if (mav != null) {
      mav.addObject("start_date", new Date(Long.valueOf(startDate)));
      mav.addObject("end_date", new Date(Long.valueOf(endDate)));
    }

    return mav;
  }

  /**
   * Handles the request to run/generate the Membership Invoices report.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/membership/invoices", method = RequestMethod.POST)
  public ModelAndView generateMembershipInvoicesReport(final @RequestParam("reportType") String reportType,
                                                       final HttpServletRequest request) {

    LOGGER.info("Received request to generate Membership Invoices report as [{}]", reportType);

    String reportFilename = "Membership_Invoices_" + dateFormat.format(new Date());

    ModelAndView mav = null;
    if (JasperReportsViewFactory.REPORT_FORMAT_PDF.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBERSHIP_INVOICES_PDF, JasperReportsViewFactory.REPORT_FORMAT_PDF, reportFilename);
    } else if (JasperReportsViewFactory.REPORT_FORMAT_CSV.equalsIgnoreCase(reportType)) {
      mav = getReportModalAndView(request, MEMBERSHIP_INVOICES_CSV, JasperReportsViewFactory.REPORT_FORMAT_CSV, reportFilename);
    }

    String startDate = request.getParameter("start_date");
    String endDate   = request.getParameter("end_date");

    if (mav != null) {
      mav.addObject("start_date", new Date(Long.valueOf(startDate)));
      mav.addObject("end_date", new Date(Long.valueOf(endDate)));
    }

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

  /**
   * Handles and retrieves the Custom Members by County report page.
   *
   * @return The name of the page.
   */
  @RequestMapping(value = "/custom/membersbycounty", method = RequestMethod.GET)
  public ModelAndView getCustomMembersByCounty() {
    LOGGER.info("Received request to show the custom report Members by County");

    return new ModelAndView("report/custom/membersbycounty");
  }

  /**
   * Handles the request to run/generate the Custome Members by County report.
   *
   * @return The generated report.
   */
  @RequestMapping(value = "/custom/membersbycounty.pdf", method = RequestMethod.GET)
  public ModelAndView generateCustomMembersByCountyReportAsPdf(final HttpServletRequest request) {
    LOGGER.info("Received request to generate Custom Members by County report.");

    String reportFilename = "Member_by_County" + dateFormat.format(new Date());

    return getReportModalAndView(request, CUSTOM_MEMBERS_BY_COUNTY, JasperReportsViewFactory.REPORT_FORMAT_PDF, reportFilename);
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
