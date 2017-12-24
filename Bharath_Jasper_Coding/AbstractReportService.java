/**
 * 
 */
package com.ctl.epwf.batch.report.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.table.TableModel;

import org.json.JSONObject;
import org.w3c.dom.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.ExcelDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.data.JRXlsxDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOdsReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.view.JasperViewer;

/**
 * @author AB40286
 *
 */
public abstract class AbstractReportService<D> implements ReportService<D> {

	private File outputFile;
	private JasperReport jasperReport;
	private OUTPUT_TYPE outputType;
	private DATA_SOURCE_TYPE dataSourceType;

	protected AbstractReportService(final String templatePath, final String outputPath,
			final DATA_SOURCE_TYPE dataSourceType, final OUTPUT_TYPE outputType) throws JRException {
		this.dataSourceType = dataSourceType;
		this.outputType = outputType;
		this.jasperReport = compileReport(templatePath);
		this.outputFile = new File(outputPath + outputType.getFormatType());
	}

	private File fillReport(final Map<String, Object> params, final D dataSource) throws JRException, IOException {
		JasperPrint jasperPrint = null;
		switch (dataSourceType) {
		case ARRAY:
			jasperPrint = JasperFillManager.fillReport(jasperReport, params,
					new JRBeanArrayDataSource((Object[]) dataSource));
			break;
		case COLLECTION:
			jasperPrint = JasperFillManager.fillReport(jasperReport, params,
					new JRBeanCollectionDataSource((Collection<?>) dataSource));
			break;
		case CSV:
			// Need to pass Columns Names
			String[] csvColumnNames = null;
			JRCsvDataSource jRCsvDataSource = new JRCsvDataSource(JRLoader.getLocationInputStream((String) dataSource));
			jRCsvDataSource.setRecordDelimiter("\r\n");
			// dataSource.setUseFirstRowAsHeader(true);
			jRCsvDataSource.setColumnNames(csvColumnNames);
			jasperPrint = JasperFillManager.fillReport(jasperReport, params, jRCsvDataSource);
			break;
		case EMPTY:
			// TODO IMPLEMENT HERE
			break;
		case JDBC:
			jasperPrint = JasperFillManager.fillReport(jasperReport, params, (Connection) dataSource);
			break;
		case JSON:
			JsonDataSource jsonDataSource = null;
			if (dataSource instanceof File) {
				jsonDataSource = new JsonDataSource((File) dataSource);
			} else if (dataSource instanceof String) {
				jsonDataSource = new JsonDataSource(new ByteArrayInputStream(dataSource.toString().getBytes()));
			} else if (dataSource instanceof JSONObject) {
				jsonDataSource = new JsonDataSource(new ByteArrayInputStream(dataSource.toString().getBytes()));
			} else if (dataSource instanceof Object) {
				jsonDataSource = new JsonDataSource(
						new ByteArrayInputStream(createObjectMapper().writeValueAsString(dataSource).getBytes()));
			}
			jasperPrint = JasperFillManager.fillReport(jasperReport, params, jsonDataSource);
			break;
		case MAP_ARRAY:
			// TODO IMPLEMENT HERE
			break;
		case MAP_COLLECTION:
			// TODO IMPLEMENT HERE
			break;
		case TABLE:
			jasperPrint = JasperFillManager.fillReport(jasperReport, params,
					new JRTableModelDataSource((TableModel) dataSource));
			break;
		case XLS:
			ExcelDataSource excelDataSource = null;
			String[] xlsColumnNames = null;
			int[] columnIndexes = null;
			excelDataSource = new ExcelDataSource(JRLoader.getLocationInputStream((String) dataSource));
			// ds.setUseFirstRowAsHeader(true);
			excelDataSource.setColumnNames(xlsColumnNames, columnIndexes);
			// uncomment the below line to see how sheet selection works
			// ds.setSheetSelection("xlsdatasource2");
			jasperPrint = JasperFillManager.fillReport(jasperReport, params, excelDataSource);
			break;
		case XLSX:
			JRXlsxDataSource jRXlsxDataSource = null;
			String[] excelColumnNames = null;
			int[] excelColumnIndexes = null;
			jRXlsxDataSource = new JRXlsxDataSource(JRLoader.getLocationInputStream((String) dataSource));
			// ds.setUseFirstRowAsHeader(true);
			jRXlsxDataSource.setColumnNames(excelColumnNames, excelColumnIndexes);
			// ds.setSheetName("XlsxDataSource3");
			jasperPrint = JasperFillManager.fillReport(jasperReport, params, jRXlsxDataSource);
			break;
		case XML:
			Document document = JRXmlUtils.parse(JRLoader.getLocationInputStream((String) dataSource));
			params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
			params.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
			params.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.##");
			params.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
			params.put(JRParameter.REPORT_LOCALE, Locale.US);
			jasperPrint = JasperFillManager.fillReport(jasperReport, params);
			break;
		case EJBQL:
			jasperPrint = JasperFillManager.fillReport(jasperReport, params);
			break;
		default:
			break;
		}
		exportReport(jasperPrint);
		return outputFile;

	}

	private ObjectMapper createObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return mapper;
	}

	private JasperReport compileReport(final String templatePath) throws JRException {
		return JasperCompileManager.compileReport(JRXmlLoader.load(templatePath));
	}

	private void exportReport(final JasperPrint jasperPrint) throws FileNotFoundException, JRException {

		switch (outputType) {
		case CSV:
			JRCsvExporter jRCsvExporter = new JRCsvExporter();
			jRCsvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			jRCsvExporter.setExporterOutput(new SimpleWriterExporterOutput(outputFile));
			jRCsvExporter.exportReport();
			break;
		case DOCX:
			JRDocxExporter jRDocxExporter = new JRDocxExporter();
			jRDocxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			jRDocxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
			jRDocxExporter.exportReport();
			break;
		case HTML:
			JasperExportManager.exportReportToXmlStream(jasperPrint, new FileOutputStream(outputFile));
			break;
		case JXL:
			net.sf.jasperreports.engine.export.JExcelApiExporter jExcelApiExporter = new net.sf.jasperreports.engine.export.JExcelApiExporter();
			jExcelApiExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			jExcelApiExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
			net.sf.jasperreports.export.SimpleJxlReportConfiguration simpleJxlReportConfiguration = new net.sf.jasperreports.export.SimpleJxlReportConfiguration();
			simpleJxlReportConfiguration.setOnePagePerSheet(true);
			jExcelApiExporter.setConfiguration(simpleJxlReportConfiguration);
			jExcelApiExporter.exportReport();
			break;
		case ODS:
			JROdsExporter jROdsExporter = new JROdsExporter();
			jROdsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			jROdsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
			SimpleOdsReportConfiguration simpleOdsReportConfiguration = new SimpleOdsReportConfiguration();
			simpleOdsReportConfiguration.setOnePagePerSheet(true);
			jROdsExporter.setConfiguration(simpleOdsReportConfiguration);
			jROdsExporter.exportReport();
			break;
		case ODT:
			JROdtExporter jROdtExporter = new JROdtExporter();
			jROdtExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			jROdtExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
			jROdtExporter.exportReport();
			break;
		case PDF:
			JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(outputFile));
			break;
		case PPTX:
			JRPptxExporter jRPptxExporter = new JRPptxExporter();
			jRPptxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			jRPptxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
			jRPptxExporter.exportReport();
			break;
		case RTF:
			JRRtfExporter exporter = new JRRtfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleWriterExporterOutput(outputFile));
			exporter.exportReport();
			break;
		case XLS:
			JRXlsExporter jRXlsExporter = new JRXlsExporter();
			jRXlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			jRXlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setOnePagePerSheet(false);
			jRXlsExporter.setConfiguration(configuration);
			jRXlsExporter.exportReport();
			break;
		case XLSX:
			JRXlsxExporter jRXlsxExporter = new JRXlsxExporter();
			jRXlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			jRXlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
			SimpleXlsxReportConfiguration simpleXlsxReportConfiguration = new SimpleXlsxReportConfiguration();
			simpleXlsxReportConfiguration.setOnePagePerSheet(false);
			jRXlsxExporter.setConfiguration(simpleXlsxReportConfiguration);
			jRXlsxExporter.exportReport();
			break;
		case XML:
			JasperExportManager.exportReportToXmlStream(jasperPrint, new FileOutputStream(outputFile));
			break;
		case PRINT:
			JasperPrintManager.printReport(jasperPrint, true);
			break;
		case VIEW:
			JasperViewer.viewReport(jasperPrint);
			break;
		default:
			break;

		}
	}

	@Override
	public File getReport(final Map<String, Object> params, final D dataSource) throws JRException, IOException {
		return fillReport(params, dataSource);
	}

}
