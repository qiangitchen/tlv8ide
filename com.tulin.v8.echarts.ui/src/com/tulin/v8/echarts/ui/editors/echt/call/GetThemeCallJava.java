package com.tulin.v8.echarts.ui.editors.echt.call;

import org.dom4j.Element;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.tulin.v8.echarts.ui.editors.echt.EchartsDesignPage;

public class GetThemeCallJava extends BrowserFunction{
	
	EchartsDesignPage design;

	public GetThemeCallJava(Browser browser, String name, EchartsDesignPage design) {
		super(browser, name);
		this.design = design;
	}
	
	@Override
	public Object function(Object[] arguments) {
		Element root = design.getDocument().getRootElement();
		Element chart = root.element("chart");
		String theme = chart.attributeValue("theme");
		if(theme==null) {
			chart.addAttribute("theme", "");
			theme = "";
		}
		return theme;
	}

}
