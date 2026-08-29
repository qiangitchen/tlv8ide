package com.tulin.v8.webtools.ide.formatter;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

/**
 * CSS 格式化
 *
 * 与 HtmlFormator 使用同一套 indent_size / indent_char 选项，
 * 保证 HTML 内嵌 <style> 和 独立 .css 文件的缩进视觉风格一致。
 * beautify-css.js 中已对冒号 ':' 做了强制空格处理。
 *
 * @author chenqian
 */
public class CSSFormator {

	public static String format(String text) throws Exception {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = null;
		try {
			engine = mgr.getEngineByName("JavaScript");
		} catch (Exception e) {
			WebToolsPlugin.logException(e);
		}
		if (engine == null) {
			mgr.registerEngineName("customScriptEngineFactory",
					new org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory());
			engine = mgr.getEngineByName("JavaScript");
		}
		engine.eval(FileUtils.FileToString("/js/format/beautify-css.js"));

		// 使用 JS 对象构造 options，便于将来扩展字段
		String opts = ""
				+ "var __cssfmt_opts = {\n"
				+ "  indent_size: " + HtmlFormator.INDENT_SIZE + ",\n"
				+ "  indent_char: '" + jsChar(HtmlFormator.INDENT_CHAR) + "'\n"
				+ "};\n";
		engine.eval(opts);

		Invocable inv = (Invocable) engine;
		String formatted = String.valueOf(
				inv.invokeFunction("css_beautify", text, engine.eval("__cssfmt_opts")));
		return HtmlFormator.normalizeWhitespace(formatted);
	}

	private static String jsChar(char c) {
		if (c == '\t') return "\\t";
		if (c == '\n') return "\\n";
		if (c == '\r') return "\\r";
		if (c == '\'') return "\\'";
		if (c == '\\') return "\\\\";
		return String.valueOf(c);
	}
}
