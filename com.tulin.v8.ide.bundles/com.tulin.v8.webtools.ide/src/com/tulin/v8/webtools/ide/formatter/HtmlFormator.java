package com.tulin.v8.webtools.ide.formatter;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class HtmlFormator {
	public static String format(String text) throws Exception {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = null;
		try {
			engine = mgr.getEngineByName("JavaScript");
		} catch (Exception e) {
		}
		if (engine == null) {
			mgr.registerEngineName("customScriptEngineFactory",
					new org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory());
			engine = mgr.getEngineByName("JavaScript");
		}
		String scriptText = FileUtils.FileToString("/js/format/beautify-html.js");
		engine.eval(scriptText);
		Invocable inv = (Invocable) engine;
		return String.valueOf(inv.invokeFunction("style_html", new Object[] { text }));
	}
}
