package com.tulin.v8.echarts.ui.editors.echt.call;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

import com.tulin.v8.echarts.ui.editors.echt.EchartsDesignPage;
import com.tulin.v8.echarts.ui.editors.echt.ModleParse;

public class LoadSourceCallJava extends BrowserFunction {
	EchartsDesignPage design;

	public LoadSourceCallJava(Browser browser, String name, EchartsDesignPage design) {
		super(browser, name);
		this.design = design;
	}

	@Override
	public Object function(Object[] arguments) {
		ModleParse pasr = design.getModlepasr();
		String res = "";
		if (pasr.toJSON().length() > 0) {
			res = pasr.toJSONString();
		} else {
			res = pasr.getChartText();
		}
		return res;
	}
}
