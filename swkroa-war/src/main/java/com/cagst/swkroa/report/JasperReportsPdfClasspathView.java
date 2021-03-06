package com.cagst.swkroa.report;

import com.cagst.swkroa.report.jasper.JasperReportLoader;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

/**
 * An implementation of the {@link JasperReportsPdfView} that will load the report from the classpath.
 *
 * @author Craig Gaskill
 */
public final class JasperReportsPdfClasspathView extends JasperReportsPdfView {
  private final String classpath;

  /**
   * Primary Constructor used to create an instance of <i>JasperReportsPdfClasspathView</i>.
   *
   * @param classpath
   *    A {@link String} that represents the location on the classpath of the Jasper Report to use.
   */
  public JasperReportsPdfClasspathView(final String classpath) {
    this.classpath = classpath;
  }

  @Override
  protected JasperReport getReport() {
    return JasperReportLoader.getReport(classpath);
  }
}
