/**
 * 
 */
package com.guru.bharath.jasper.json;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;

import com.ctl.epwf.batch.report.common.AbstractJsonReportService;
import com.ctl.epwf.batch.report.common.ReportService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;

/**
 * @author AB40286
 *
 */
public class JsonReportService<D extends Object> extends AbstractJsonReportService<D> {

	protected JsonReportService(String templatePath, String outputPath, DATA_SOURCE_TYPE dataSourceType,
			OUTPUT_TYPE outputType) throws JRException {
		super(templatePath, outputPath, dataSourceType, outputType);
	}

	public static Map<String, Object> getReportParameter() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(JsonQueryExecuterFactory.JSON_DATE_PATTERN, "yyyy-MM-dd");
		params.put(JsonQueryExecuterFactory.JSON_NUMBER_PATTERN, "#,##0.##");
		params.put(JsonQueryExecuterFactory.JSON_LOCALE, Locale.ENGLISH);
		params.put(JRParameter.REPORT_LOCALE, Locale.US);
		return params;
	}

	public static void main(String[] args) {
		try {
			ReportService<File> reportServiceFile = new JsonReportService<File>("jrxml/JsonDataSourceDemo.jrxml",
					"jrxml/jdbc_report", DATA_SOURCE_TYPE.JSON, OUTPUT_TYPE.VIEW);
			ReportService<String> reportServiceString = new JsonReportService<String>("jrxml/JsonDataSourceDemo.jrxml",
					"jrxml/jdbc_report", DATA_SOURCE_TYPE.JSON, OUTPUT_TYPE.VIEW);
			ReportService<JSONObject> reportServiceJson = new JsonReportService<JSONObject>(
					"jrxml/JsonDataSourceDemo.jrxml", "jrxml/jdbc_report", DATA_SOURCE_TYPE.JSON, OUTPUT_TYPE.VIEW);
			ReportService<Object> reportServiceObject = new JsonReportService<Object>("jrxml/JsonDataSourceDemo.jrxml",
					"jrxml/jdbc_report", DATA_SOURCE_TYPE.JSON, OUTPUT_TYPE.VIEW);
			reportServiceFile.getReport(getReportParameter(), new File("jrxml/Demodata.json"));
		} catch (JRException | IOException e) {
			e.printStackTrace();
		}
	}

}
