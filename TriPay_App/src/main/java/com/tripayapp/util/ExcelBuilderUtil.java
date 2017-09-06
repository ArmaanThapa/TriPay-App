package com.tripayapp.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.tripayapp.entity.PQTransaction;

public class ExcelBuilderUtil extends AbstractExcelView {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<PQTransaction> pqTransactions = (List<PQTransaction>) model.get("transactionList");
		HSSFSheet sheet = workbook.createSheet("PayQwik");
		sheet.setDefaultColumnWidth(30);

		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Arial");
		style.setFillForegroundColor(HSSFColor.BLACK.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.WHITE.index);
		style.setFont(font);

		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Order Id");
		header.getCell(0).setCellStyle(style);

		header.createCell(1).setCellValue("Amount");
		header.getCell(1).setCellStyle(style);

		header.createCell(2).setCellValue("Status");
		header.getCell(2).setCellStyle(style);

		int rowCount = 1;
		for (PQTransaction pqTransaction : pqTransactions) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(0).setCellValue(pqTransaction.getTransactionRefNo());
			aRow.createCell(1).setCellValue(pqTransaction.getAmount());
			aRow.createCell(2).setCellValue(pqTransaction.getStatus() + "");
		}
	}
}
