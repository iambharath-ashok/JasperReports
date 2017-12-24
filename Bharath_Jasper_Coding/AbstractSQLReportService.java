/**
 * 
 */
package com.ctl.epwf.batch.report.common;

import java.sql.Connection;

import net.sf.jasperreports.engine.JRException;

/**
 * @author AB40286
 *
 */
public abstract class AbstractSQLReportService<D extends Connection> extends AbstractReportService<D> {

	protected AbstractSQLReportService(String templatePath, String outputPath, DATA_SOURCE_TYPE dataSourceType,
			OUTPUT_TYPE outputType) throws JRException {
		super(templatePath, outputPath, dataSourceType, outputType);
	}
}
