package com.tripayapp.model.excel;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.tripayapp.entity.PQTransaction;

public class TransactionExcelView extends AbstractExcelView {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HSSFSheet excelSheet = workbook.createSheet("Transaction Report");
		setExcelHeader(excelSheet);

		List<PQTransaction> transactionReport = (List<PQTransaction>) model.get("transactionList");
		for (PQTransaction pqTransaction : transactionReport) {
			System.out.println("PQ @! ::" + pqTransaction.getDescription());
		}
		setExcelRows(excelSheet, transactionReport);
	}

	public void setExcelHeader(HSSFSheet excelSheet) {
		HSSFRow excelHeader = excelSheet.createRow(0);
		excelHeader.createCell(0).setCellValue("OrderId");
		excelHeader.createCell(1).setCellValue("Description");
		excelHeader.createCell(2).setCellValue("Transaction Amount");
		excelHeader.createCell(3).setCellValue("Status");

	}

	public void setExcelRows(HSSFSheet excelSheet, List<PQTransaction> transactionReports) {
		int record = 1;
		for (PQTransaction transaction : transactionReports) {
			System.out.println("PQ @ ::" + transaction.getDescription());
			HSSFRow excelRow = excelSheet.createRow(record++);
			excelRow.createCell(0).setCellValue(transaction.getTransactionRefNo());
			excelRow.createCell(1).setCellValue(transaction.getDescription());
			excelRow.createCell(2).setCellValue(transaction.getAmount());
			excelRow.createCell(3).setCellValue(transaction.getStatus().getValue());
		}
	}
}
