package com.cagst.swkroa.report;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsCsvView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsXlsView;

/**
 * Factory that will return a ...
 *
 * @author Craig Gaskill
 * @version 1.0.0
 */
public class JasperReportsViewFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportsViewFactory.class);

  /**
   * Constant that defines "Content-Disposition" header.
   */
  private static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";

  public static final String REPORT_FORMAT_PDF = "pdf";
  public static final String REPORT_FORMAT_XLS = "xls";
  public static final String REPORT_FORMAT_CSV = "csv";

  public static AbstractJasperReportsView getJasperReportsView(final HttpServletRequest request,
                                                               final DataSource dataSource,
                                                               final String reportUrl,
                                                               final String reportFormat,
                                                               final String reportFilename) {

    Assert.notNull(request, "Assertion Failed - argument [request] cannot be null");
    Assert.notNull(dataSource, "Assertion Failed - argument [dataSource] cannot be null");
    Assert.hasText(reportUrl, "Assertion Failed - argument [reportUrl] cannot be null or empty");

    String format = StringUtils.isEmpty(reportFilename) ? REPORT_FORMAT_PDF : reportFormat;

    // set possible content headers
    Properties availableHeaders = new Properties();
    availableHeaders.put("html", "inline; filename=" + reportFilename + ".html");
    availableHeaders.put("csv", "inline; filename=" + reportFilename + ".csv");
    availableHeaders.put("pdf", "inline; filename=" + reportFilename + ".pdf");
    availableHeaders.put("xls", "inline; filename=" + reportFilename + ".xls");

    AbstractJasperReportsSingleFormatView view;
    if (REPORT_FORMAT_PDF.equals(format)) {
      view = new JasperReportsPdfView();
    } else if (REPORT_FORMAT_XLS.equals(format)) {
      view = new JasperReportsXlsView();
    } else if (REPORT_FORMAT_CSV.equals(format)) {
      view = new JasperReportsCsvView();
    } else {
      LOGGER.error("Unsupported report format [{}]", format);
      return null;
    }

    WebApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());

    view.setJdbcDataSource(dataSource);
    view.setUrl(reportUrl);
    view.setApplicationContext(appCtx);

    if (StringUtils.isNotBlank(reportFormat)) {
      Properties headers = new Properties();
      headers.put(HEADER_CONTENT_DISPOSITION, availableHeaders.get(format));

      view.setHeaders(headers);
    }

    return view;
  }
}
