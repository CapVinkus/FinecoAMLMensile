package it.gepo.engine;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;


public class FileExcelWriter implements ItemWriter<List<List<Map<String, Object>>>>, InitializingBean {
	private String nomeFile;
	private String header;
	protected List<String> headers;
	protected List<String> headers2;
	private int rowNum = 1;
	private boolean eseguito = false;
	protected SXSSFWorkbook workbook;
	protected SXSSFSheet sheet;
	protected String sheetName;
	private XSSFCellStyle dataCellStyle;
	private CellStyle dateCellStyle;
	protected int currRow = 0;
	private int lavorare;
	private int controllare;

	public void autoSizeColumns(Workbook workbook) {
		SXSSFSheet sheet = (SXSSFSheet) workbook.getSheet(sheetName);
		sheet.trackAllColumnsForAutoSizing();
		for (int i = 0; i < headers.size(); i++) {
			sheet.autoSizeColumn(i);
		}
	}

	protected void createStringCell(Row row, String val, int col) {
		Cell cell = row.createCell(col);
		cell.setCellStyle(dataCellStyle);
		cell.setCellType(CellType.STRING);
		cell.setCellValue(val);
	}
	private void addHeaders(Sheet sheet) {

		Workbook workbook = sheet.getWorkbook();

		XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
		Font font = workbook.createFont();

		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		font.setBold(true);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFont(font);

		Row row = sheet.createRow(currRow);
		int col = 0;
		for (String header : headers) {
			Cell cell = row.createCell(col);
			cell.setCellValue(header);
			cell.setCellStyle(style);
			col++;
		}
	}

	private void addHeaders2(Sheet sheet2) {

		Workbook workbook = sheet2.getWorkbook();

		XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
		Font font = workbook.createFont();

		font.setFontHeightInPoints((short) 10);
		font.setFontName("Arial");
		font.setBold(true);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFont(font);

		Row row2 = sheet2.createRow(currRow);
		int col = 0;
		for (String header : headers2) {
			Cell cell2 = row2.createCell(col);
			cell2.setCellValue(header);
			cell2.setCellStyle(style);
			col++;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public CellStyle getDateCellStyle() {
		return dateCellStyle;
	}

	public void setDateCellStyle(CellStyle dateCellStyle) {
		this.dateCellStyle = dateCellStyle;
	}

	@Override
	public void write(List<? extends List<List<Map<String, Object>>>> liste) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook();
		if (!eseguito) {
			eseguito = true;
			headers = Arrays.asList(header.split(";"));

			Cell cell = null;
			XSSFSheet sheet = workbook.createSheet("Da Controllare");
			addHeaders(sheet);
			addHeaders(sheet);
			for (Map<String, Object> map : liste.get(0).get(0)) {

				Row row = sheet.createRow(rowNum++);
				cell = row.createCell(0);
				cell.setCellValue(map.get("IDENTIFICATIVO").toString());
				cell = row.createCell(1);
				cell.setCellValue(map.get("NDG") != null ? map.get("NDG").toString().trim() : "");
				cell = row.createCell(2);
				cell.setCellValue(map.get("CONTO") != null ? map.get("CONTO").toString().trim() : "");
				cell = row.createCell(3);
				cell.setCellValue(map.get("INTESTATARIO") != null ? map.get("INTESTATARIO").toString().trim() : "");
				cell = row.createCell(4);
				cell.setCellValue(map.get("MITTENTE MAIL").toString());
				cell = row.createCell(5);
				cell.setCellValue(map.get("OGGETTO MAIL").toString());
				cell = row.createCell(6);
				cell.setCellValue(map.get("NOME FILE MAIL").toString());
				cell = row.createCell(7);
				cell.setCellValue(map.get("STATO ATTUALE").toString());
			}
			for (int i = 0; i < headers.size(); i++) {
				sheet.autoSizeColumn(i);
			}
			headers2 = Arrays.asList(header.split(";"));
			XSSFSheet sheet2 = workbook.createSheet("Da Lavorare");
			int rowNum2 = 1;
			addHeaders2(sheet2);
			Cell cell2 = null;
			for (Map<String, Object> map : liste.get(0).get(1)) {
				Row row2 = sheet2.createRow(rowNum2++);
				cell2 = row2.createCell(0);
				cell2.setCellValue(map.get("IDENTIFICATIVO").toString());
				cell2 = row2.createCell(1);
				cell2.setCellValue(map.get("NDG") != null ? map.get("NDG").toString().trim() : "");
				cell2 = row2.createCell(2);
				cell2.setCellValue(map.get("CONTO") != null ? map.get("CONTO").toString().trim() : "");
				cell2 = row2.createCell(3);
				cell2.setCellValue(map.get("INTESTATARIO") != null ? map.get("INTESTATARIO").toString().trim() : "");
				cell2 = row2.createCell(4);
				cell2.setCellValue(map.get("MITTENTE MAIL").toString());
				cell2 = row2.createCell(5);
				cell2.setCellValue(map.get("OGGETTO MAIL").toString());
				cell2 = row2.createCell(6);
				cell2.setCellValue(map.get("NOME FILE MAIL").toString());
				cell2 = row2.createCell(7);
				cell2.setCellValue(map.get("STATO ATTUALE").toString());
			}
			for (int j = 0; j < headers2.size(); j++) {
				sheet2.autoSizeColumn(j);
			}
			String excelFilePath = nomeFile;
			FileOutputStream fileOut = new FileOutputStream(excelFilePath);
			workbook.write(fileOut);
			workbook.close();
			fileOut.close();
		}
		
	}

	public int getLavorare() {
		return lavorare;
	}

	public void setLavorare(int lavorare) {
		this.lavorare = lavorare;
	}

	public int getControllare() {
		return controllare;
	}

	public void setControllare(int controllare) {
		this.controllare = controllare;
	}
	
	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
}
