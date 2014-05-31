package com.cagst.swkroa.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * An implementation of the {@link ReportGenerator} that utilizes the Jasper reporting engine.
 *
 * @author Craig Gaskill
 *
 * @version 1.0.0
 */
public class JasperReportGenerator implements ReportGenerator {
	private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportGenerator.class);

	private final DataSource dataSource;

	/**
	 * Primary Constructor used to create an instance of <i>JasperReportGenerator</i>.
	 *
	 * @param dataSource The {@link DataSource} to use to query for data.
	 */
	public JasperReportGenerator(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Autowired
	private ServletContext context;

	@Override
	public void generateMembershipListingReport() {
		JasperPrint jasperPrint = null;

		try {
			InputStream is2 = context.getResourceAsStream("/reports/MembershipListingReport.jasper");
			jasperPrint = JasperFillManager.fillReport(is2, null, dataSource.getConnection());

			JasperViewer.viewReport(jasperPrint);
		} catch (JRException ex) {
			LOGGER.error("Error running report.", ex);
		} catch (SQLException ex) {
			LOGGER.error("Error running query for report.", ex);
		}
	}
}
