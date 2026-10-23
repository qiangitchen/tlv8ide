package com.tulin.v8.webtools.ide.formatter;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

/**
 * JavaScript 格式化
 *
 * 与 HtmlFormator 使用同一套 indent_size / indent_char / brace_style 选项，
 * 保证 HTML 内嵌 <script> 和 独立 .js 文件的缩进视觉风格一致。
 *
 * @author chenqian
 */
public class JavascriptFormator {

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
		engine.eval(FileUtils.FileToString("/js/format/beautify.js"));

		String opts = ""
				+ "var __jsfmt_opts = {\n"
				+ "  indent_size: " + HtmlFormator.INDENT_SIZE + ",\n"
				+ "  indent_char: '" + jsChar(HtmlFormator.INDENT_CHAR) + "',\n"
				+ "  brace_style: '" + HtmlFormator.BRACE_STYLE + "',\n"
				+ "  preserve_newlines: false,\n"
				+ "  max_preserve_newlines: 2,\n"
				+ "  space_in_paren: false,\n"
				+ "  jslint_happy: false\n"
				+ "};\n";
		engine.eval(opts);

		Invocable inv = (Invocable) engine;
		String value = String.valueOf(
				inv.invokeFunction("js_beautify", text, engine.eval("__jsfmt_opts")));
		return HtmlFormator.normalizeWhitespace(value);
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
