/**
 * 
 */
package com.ctl.epwf.batch.report.common;

import net.sf.jasperreports.engine.JRException;

/**
 * @author AB40286
 *
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractEJBQLReportService extends AbstractReportService {

	protected AbstractEJBQLReportService(String templatePath, String outputPath, DATA_SOURCE_TYPE dataSourceType,
			OUTPUT_TYPE outputType) throws JRException {
		super(templatePath, outputPath, dataSourceType, outputType);
	}

}
