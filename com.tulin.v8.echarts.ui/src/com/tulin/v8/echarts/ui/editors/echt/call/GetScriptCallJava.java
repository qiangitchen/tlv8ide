package com.tulin.v8.echarts.ui.editors.echt.call;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.tulin.v8.echarts.ui.editors.echt.EchartsDesignPage;
import com.tulin.v8.echarts.ui.editors.echt.ModleParse;

public class GetScriptCallJava extends BrowserFunction {

	EchartsDesignPage design;

	public GetScriptCallJava(Browser browser, String name, EchartsDesignPage design) {
		super(browser, name);
		this.design = design;
	}

	@Override
	public Object function(Object[] arguments) {
		Document doc = design.getDocument();
		ModleParse pasr = design.getModlepasr();
		Element root = doc.getRootElement();
		List<Element> scripts = root.elements("script");
		StringBuilder strb = new StringBuilder();
		for (Element script : scripts) {
			strb.append(pasr.valueTranse(script.getText()));
		}
		return strb.toString();
	}

}
