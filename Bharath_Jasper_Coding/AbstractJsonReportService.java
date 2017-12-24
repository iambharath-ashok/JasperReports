/**
 * 
 */
package com.ctl.epwf.batch.report.common;

import net.sf.jasperreports.engine.JRException;

/**
 * @author AB40286
 * @param <D>
 *
 */
public abstract class AbstractJsonReportService<D extends Object> extends AbstractReportService<D> {

	protected AbstractJsonReportService(String templatePath, String outputPath, DATA_SOURCE_TYPE dataSourceType,
			OUTPUT_TYPE outputType) throws JRException {
		super(templatePath, outputPath, dataSourceType, outputType);
	}

}
