/**
 * 
 */
package com.guru.bharath.jasper.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ctl.epwf.batch.report.common.AbstractSQLReportService;
import com.ctl.epwf.batch.report.common.ReportService;

import net.sf.jasperreports.engine.JRException;

/**
 * @author AB40286
 *
 */
public class JDBCReportService<D extends Connection> extends AbstractSQLReportService<D> {

	protected JDBCReportService(String templatePath, String outputPath, DATA_SOURCE_TYPE dataSourceType,
			OUTPUT_TYPE outputType) throws JRException {
		super(templatePath, outputPath, dataSourceType, outputType);
	}

	public static Connection establishConnection() {
		System.out.println("establishConnection method satrted : " + new Date());
		Connection connection = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String oracleURL = "jdbc:oracle:thin:@epwfdv2db.dev.qintra.com:1601:epwfdv2";
			connection = DriverManager.getConnection(oracleURL, "epwf_app", "epwf_app_epwfdv2");
			connection.setAutoCommit(false);
		} catch (SQLException exception) {
			exception.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("establishConnection method ended : " + new Date());
		return connection;
	}

	public static Map<String, Object> getReportParameter() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		return parameters;
	}

	public static void main(String[] args) {
		System.out.println("main method started : " + new Date());
		try {
			ReportService<Connection> reportService = new JDBCReportService<Connection>("jrxml/jdbc_report.jrxml",
					"jrxml/jdbc_report", DATA_SOURCE_TYPE.JDBC, OUTPUT_TYPE.VIEW);
			reportService.getReport(getReportParameter(), establishConnection());
		} catch (JRException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("main method ended : " + new Date());
	}

}
