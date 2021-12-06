package com.tulin.v8.ureport.ui.editors.designer.call;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.FileDialog;

import com.bstek.ureport.console.designer.ReportDefinitionWrapper;
import com.bstek.ureport.console.importexcel.ExcelParser;
import com.bstek.ureport.console.importexcel.HSSFExcelParser;
import com.bstek.ureport.console.importexcel.XSSFExcelParser;
import com.bstek.ureport.definition.ReportDefinition;
import com.tulin.v8.ureport.ui.editors.designer.UReportEditor;

public class SWTImportExcelReportFile extends SWTLoadReport {

	private List<ExcelParser> excelParsers = new ArrayList<ExcelParser>();

	public SWTImportExcelReportFile(UReportEditor editor, Browser browser, String name) {
		super(editor, browser, name);
		excelParsers.add(new HSSFExcelParser());
		excelParsers.add(new XSSFExcelParser());
	}

	@Override
	public Object function(Object[] arguments) {
		FileDialog filedlg = new FileDialog(editor.getEditorSite().getShell(), SWT.OPEN);
		filedlg.setText("文件选择");
		filedlg.setFilterPath("SystemRoot");
		filedlg.setFilterExtensions(new String[] { "*.xlsx", "*.xls" });
		String selected = filedlg.open();
		System.out.println("您选中的文件路径为：" + selected);
		if (selected != null) {
			return fileHandle(selected);
		}
		return false;
	}

	private String fileHandle(String filepath) {
		File file = new File(filepath);
		String name = file.getName().toLowerCase();
		InputStream inputStream = null;
		ReportDefinition reportDef = null;
		try {
			inputStream = new FileInputStream(file);
			for (ExcelParser parser : excelParsers) {
				if (parser.support(name)) {
					reportDef = parser.parse(inputStream);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
			}
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			writeObjectToJson(out, new ReportDefinitionWrapper(reportDef));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(out.toByteArray(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "{}";
	}
}
