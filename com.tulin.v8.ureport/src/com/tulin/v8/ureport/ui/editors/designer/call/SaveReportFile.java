package com.tulin.v8.ureport.ui.editors.designer.call;

import java.net.URLDecoder;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.tulin.v8.core.XMLFormator;
import com.tulin.v8.ureport.ui.editors.designer.UReportEditor;

public class SaveReportFile extends BrowserFunction {
	UReportEditor editor;

	public SaveReportFile(UReportEditor editor, Browser browser, String name) {
		super(browser, name);
		this.editor = editor;
	}

	@Override
	public Object function(Object[] arguments) {
		String content = (String) arguments[0];
		content = decodeContent(content);
		editor.setText(XMLFormator.formatXMLStr(content));
		return true;
	}

	protected String decodeContent(String content) {
		if (content == null) {
			return content;
		}
		try {
			content = URLDecoder.decode(content, "utf-8");
			return content;
		} catch (Exception ex) {
			return content;
		}
	}
}
