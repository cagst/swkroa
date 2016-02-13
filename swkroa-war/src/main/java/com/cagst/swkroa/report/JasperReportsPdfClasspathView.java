package com.cagst.swkroa.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import java.io.IOException;
import java.io.InputStream;

/**
 * An implementation of the {@link JasperReportsPdfView} that will load the report from the classpath.
 *
 * @author Craig Gaskill
 */
public final class JasperReportsPdfClasspathView extends JasperReportsPdfView {
  private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportsPdfClasspathView.class);

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
    InputStream stream = this.getClass().getResourceAsStream(classpath);
    try {
      return (JasperReport) JRLoader.loadObject(stream);
    } catch (JRException ex) {
      LOGGER.error(ex.getMessage(), ex);
    } finally {
      if (stream != null) {
        try {
          stream.close();
        } catch (IOException ex) {
          // do nothing
        }
      }
    }
    return null;
  }
}
