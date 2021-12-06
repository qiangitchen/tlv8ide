package com.tulin.v8.ureport.ui.editors.designer.call;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.bstek.ureport.console.designer.ReportDefinitionWrapper;
import com.bstek.ureport.definition.ReportDefinition;
import com.bstek.ureport.parser.ReportParser;
import com.tulin.v8.ureport.ui.editors.designer.UReportEditor;

public class SWTLoadReport extends BrowserFunction {
	protected UReportEditor editor;

	public SWTLoadReport(UReportEditor editor, Browser browser, String name) {
		super(browser, name);
		this.editor = editor;
	}

	@Override
	public Object function(Object[] arguments) {
		ReportDefinition reportDef = new ReportParser().parse(editor.getText(), editor.getEditorInput().getName());
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

	protected void writeObjectToJson(ByteArrayOutputStream out, Object obj) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Inclusion.NON_NULL);
		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		try {
			mapper.writeValue(out, obj);
		} finally {
			out.flush();
			out.close();
		}
	}

}
