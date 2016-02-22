package com.cagst.swkroa.report.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * A class that provides methods for loading jasper report files from the classpath.
 *
 * @author Craig Gaskill
 */
public final class JasperReportLoader {
  private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportLoader.class);

  public static final String MEMBERSHIP_LISTING_PDF    = "MembershipListingPdf.jasper";
  public static final String MEMBERSHIP_LISTING_CSV    = "MembershipListingCsv.jasper";
  public static final String MEMBERSHIP_STATUS         = "MembershipStatusReport.jasper";
  public static final String MEMBERSHIP_DELINQUENT_CSV = "accounting/MembershipDelinquencyCsv.jasper";
  public static final String MEMBERSHIP_DELINQUENT_PDF = "accounting/MembershipDelinquencyPdf.jasper";
  public static final String MEMBERSHIP_RENEWAL_PDF    = "accounting/MembershipDuesRenewalPdf.jasper";
  public static final String MEMBERSHIP_RENEWAL_CSV    = "accounting/MembershipDuesRenewalCsv.jasper";
  public static final String MEMBERSHIP_REMINDER_PDF   = "accounting/MembershipDuesReminderPdf.jasper";
  public static final String MEMBERSHIP_PAYMENTS_PDF   = "accounting/MembershipPaymentsPdf.jasper";
  public static final String MEMBERSHIP_PAYMENTS_CSV   = "accounting/MembershipPaymentsCsv.jasper";
  public static final String MEMBERSHIP_INVOICES_PDF   = "accounting/MembershipInvoicesPdf.jasper";
  public static final String MEMBERSHIP_INVOICES_CSV   = "accounting/MembershipInvoicesCsv.jasper";

  public static final String MEMBER_MAILINGLIST_CSV = "MemberMailingListCsv.jasper";
  public static final String MEMBER_MAILINGLIST_PDF = "MemberMailingListPdf.jasper";
  public static final String MEMBER_EMAILLIST_CSV   = "MemberEmailListCsv.jasper";
  public static final String MEMBER_EMAILLIST_PDF   = "MemberEmailListPdf.jasper";

  public static final String CUSTOM_MEMBERS_BY_COUNTY = "custom/MembersForCounties.jasper";

  /**
   * Loads the jasper report from the specified classpath.
   *
   * @param classpath
   *    A {@link String} that represents the location in the classpath to load the jasper report from.
   *
   * @return The {@link JasperReport} located at the specified classpath, {@code null} if not found.
   */
  public static JasperReport getReport(final String classpath) {
    InputStream stream = JasperReportLoader.class.getResourceAsStream(classpath);
    if (stream == null) {
      LOGGER.error("Unable to locate jasper report [{}]", classpath);
    }

    try {
      return (JasperReport) JRLoader.loadObject(stream);
    } catch (JRException ex) {
      LOGGER.error(ex.getMessage(), ex);
      return null;
    } finally {
      IOUtils.closeQuietly(stream);
    }
  }
}
