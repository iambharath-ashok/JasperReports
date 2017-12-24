/**
 * 
 */
package com.ctl.epwf.batch.report.common;

import java.util.Collection;

import net.sf.jasperreports.engine.JRException;

/**
 * @author AB40286
 *
 */
public abstract class AbstractBeanReportService<D extends Collection<? extends Object>> extends AbstractReportService<D> {

	protected AbstractBeanReportService(String templatePath, String outputPath, DATA_SOURCE_TYPE dataSourceType,
			OUTPUT_TYPE outputType) throws JRException {
		super(templatePath, outputPath, dataSourceType, outputType);
	}

}
