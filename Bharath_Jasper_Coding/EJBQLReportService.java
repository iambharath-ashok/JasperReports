package com.guru.bharath.jasper.ejbql;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.ctl.epwf.batch.report.common.AbstractEJBQLReportService;
import com.ctl.epwf.batch.report.common.ReportService;
import com.ctl.epwf.batch.report.common.ReportService.DATA_SOURCE_TYPE;
import com.ctl.epwf.batch.report.common.ReportService.OUTPUT_TYPE;
import com.guru.bharath.jasper.jdbc.JDBCReportService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory;

/**
 * 
 */

/**
 * @author AB40286
 *
 */
public class EJBQLReportService extends AbstractEJBQLReportService{

	protected EJBQLReportService(String templatePath, String outputPath, DATA_SOURCE_TYPE dataSourceType,
			OUTPUT_TYPE outputType) throws JRException {
		super(templatePath, outputPath, dataSourceType, outputType);
	}
	
	public static Map<String, Object> getReportParameter() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EPWFMainDS", new HashMap<Object, Object>());
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, entityManager);
		return parameters;
	}
	
	public static void main(String[] args) {
		ReportService<?> reportService = null;
		try {
			reportService = new EJBQLReportService("jrxml/jdbc_report.jrxml",
					"jrxml/jdbc_report", DATA_SOURCE_TYPE.JDBC, OUTPUT_TYPE.VIEW);
			reportService.getReport(getReportParameter(),null);
		} catch (JRException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
