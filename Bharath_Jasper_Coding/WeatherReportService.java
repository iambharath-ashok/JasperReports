/**
 * 
 */
package com.guru.bharath.jasper.weather;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.ctl.epwf.batch.report.common.AbstractBeanReportService;
import com.ctl.epwf.batch.report.common.ReportService;

import net.sf.jasperreports.engine.JRException;

/**
 * @author AB40286
 *
 */
public class WeatherReportService<D extends WeatherBean> extends AbstractBeanReportService<Collection<D>> {

	protected WeatherReportService(String templatePath, String outputPath, DATA_SOURCE_TYPE dataSourceType,
			OUTPUT_TYPE outputType) throws JRException {
		super(templatePath, outputPath, dataSourceType, outputType);
	}

	public static Map<String, Object> getReportParameter() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		return parameters;
	}

	public static void main(String[] args) {
		try {
			ReportService<Collection<WeatherBean>> reportService = new WeatherReportService<WeatherBean>("jrxml/WeatherReport.jrxml",
					"reports/WeatherReport", DATA_SOURCE_TYPE.COLLECTION, OUTPUT_TYPE.RTF);
			reportService.getReport(getReportParameter(), WeatherBeanFactory.getWeatherBeansList());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
