/**
 * 
 */
package com.ctl.epwf.batch.report.common;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

/**
 * @author AB40286
 *
 */
public interface ReportService<D> {

	enum DATA_SOURCE_TYPE {
		JDBC, COLLECTION, ARRAY, MAP_ARRAY, MAP_COLLECTION, TABLE, XML, CSV, XLS, EMPTY, JSON, XLSX, EJBQL
	}

	enum OUTPUT_TYPE {
		HTML(".html"), PDF(".pdf"), XML(".xml"), RTF(".rtf"), XLS(".xls"), JXL(".jxl.xls"), CSV(".csv"), ODT(
				".odt"), ODS(".ods"), XLSX(".xlsx"), DOCX(".docx"), PPTX(".pptx"), PRINT, VIEW;

		private String formatType;

		private OUTPUT_TYPE(final String formatType) {
			this.formatType = formatType;
		}

		private OUTPUT_TYPE() {
		}

		public String getFormatType() {
			return formatType;
		}

		public void setFormatType(String formatType) {
			this.formatType = formatType;
		}

	}

	File getReport(final Map<String, Object> params, final D datasource) throws JRException, IOException;

}
