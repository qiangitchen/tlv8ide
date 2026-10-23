package com.tulin.v8.webtools.ide.formatter;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

public class HtmlFormator {
	/** 统一的缩进单位大小：与 Eclipse Java/Web 默认保持一致 */
	public static final int INDENT_SIZE = 4;
	/** 统一的缩进字符：空格（避免不同渲染环境下 TAB 宽度不一致） */
	public static final char INDENT_CHAR = ' ';
	/** script/style 内嵌内容的缩进模式：normal(+1)、keep、separate */
	public static final String INDENT_SCRIPTS = "normal";
	/** 大括号风格：end-expand 让 JS 中的结束括号独占一行，视觉更整齐 */
	public static final String BRACE_STYLE = "end-expand";

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
		engine.eval(FileUtils.FileToString("/js/format/beautify-css.js"));
		engine.eval(FileUtils.FileToString("/js/format/beautify-html.js"));

		// 构建与 beautify-html.js / js / css 一致的统一 options
		StringBuilder optsJs = new StringBuilder();
		optsJs.append("var __htmlfmt_opts = {\n");
		optsJs.append("  indent_size: ").append(INDENT_SIZE).append(",\n");
		optsJs.append("  indent_char: '").append(toJsChar(INDENT_CHAR)).append("',\n");
		optsJs.append("  brace_style: '").append(BRACE_STYLE).append("',\n");
		optsJs.append("  indent_scripts: '").append(INDENT_SCRIPTS).append("',\n");
		optsJs.append("  preserve_newlines: false,\n");        // 拒绝原样保留乱七八糟的换行 → 重新统一断行
		optsJs.append("  max_preserve_newlines: 2,\n");
		optsJs.append("  max_char: 120,\n");                   // 单行超过 120 折行
		optsJs.append("  wrap_line_length: 0,\n");
		optsJs.append("  unformatted: ['a', 'span', 'bdo', 'em', 'strong', 'dfn', 'code', 'samp', 'kbd', 'var',\n");
		optsJs.append("               'cite', 'abbr', 'acronym', 'q', 'sub', 'sup', 'tt', 'i', 'b', 'big', 'small',\n");
		optsJs.append("               'u', 's', 'strike', 'font', 'ins', 'del', 'address', 'dt', 'h1', 'h2', 'h3',\n");
		optsJs.append("               'h4', 'h5', 'h6', 'pre', 'textarea', 'script', 'style']\n"); // 不格式化 inline 短标签
		optsJs.append("};\n");
		engine.eval(optsJs.toString());

		Invocable inv = (Invocable) engine;
		String formatted = String.valueOf(
				inv.invokeFunction("style_html", new Object[] { text, engine.eval("__htmlfmt_opts") }));

		// 统一输出规范化：
		//   (1) 清理"只含空白字符"的空行 —— 这种行在编辑器里肉眼看起来像排版不齐
		//   (2) 统一换行符为 \n
		//   (3) 去除开头/结尾的多余空白，保证整体紧凑
		formatted = normalizeWhitespace(formatted);
		return formatted;
	}

	private static String toJsChar(char c) {
		if (c == '\t') return "\\t";
		if (c == '\n') return "\\n";
		if (c == '\r') return "\\r";
		if (c == '\'') return "\\'";
		if (c == '\\') return "\\\\";
		return String.valueOf(c);
	}

	/**
	 * 规范化输出的空白：
	 *   - CR/LF 统一为 LF
	 *   - 去掉每行末尾的尾随空格
	 *   - 去掉"仅含空白字符"的空行
	 *   - 去掉文首/文末的多余换行
	 *   - 保证文件末尾有且仅有一个换行符
	 */
	static String normalizeWhitespace(String s) {
		if (s == null) return null;
		s = s.replace("\r\n", "\n").replace('\r', '\n');
		String[] lines = s.split("\n", -1);
		StringBuilder sb = new StringBuilder(s.length());
		for (String line : lines) {
			// 去掉每行 trailing 空格
			int last = line.length();
			while (last > 0) {
				char c = line.charAt(last - 1);
				if (c == ' ' || c == '\t') last--;
				else break;
			}
			String trimmed = last == line.length() ? line : line.substring(0, last);
			if (trimmed.isEmpty()) {
				continue; // 跳过只有空白的空行
			}
			sb.append(trimmed).append('\n');
		}
		// 去除开头的空换行（我们上一步已把空白空行→空，开头空行来自原文）
		while (sb.length() > 0 && sb.charAt(0) == '\n') sb.deleteCharAt(0);
		// 去除末尾多余换行，保留一个
		while (sb.length() > 1 && sb.charAt(sb.length() - 1) == '\n'
				&& sb.charAt(sb.length() - 2) == '\n') {
			sb.deleteCharAt(sb.length() - 1);
		}
		if (sb.length() == 0 || sb.charAt(sb.length() - 1) != '\n') {
			sb.append('\n');
		}
		return sb.toString();
	}
}
