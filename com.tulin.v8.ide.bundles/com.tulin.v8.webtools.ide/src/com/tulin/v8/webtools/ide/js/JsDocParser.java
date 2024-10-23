package com.tulin.v8.webtools.ide.js;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tulin.v8.webtools.ide.utils.StringUtils;

public class JsDocParser {

	private static final Pattern PATTERN_PARAM = Pattern.compile("@param\\s+(.*)");
	private static final Pattern PATTERN_RETURN = Pattern.compile("@return\\s+(.*)");
	private static final Pattern PATTERN_RETURNS = Pattern.compile("@returns\\s+(.*)");
	private static final Pattern PATTERN_TYPE = Pattern.compile("@type\\s+(.*)");
	private static final Pattern PATTERN_STATIC = Pattern.compile("@static");
	private static final Pattern PATTERN_PRIVATE = Pattern.compile("@private");
	private static final Pattern PATTERN_CLASS = Pattern.compile("@class\\s+(.*)");

	public static String[] parseInline(String comment) {
		comment = comment.replaceFirst("^/\\*\\*\\s*", "");
		comment = comment.replaceFirst("\\s*\\*/$", "");
		comment = comment.trim();
		if (comment.length() > 0) {
			List<String> list = new ArrayList<String>();
			String[] values = comment.split("\\|");
			for (String type : values) {
				list.add(convertType(type));
			}
			return list.toArray(new String[list.size()]);
		}
		return new String[0];
	}

	public static JsDoc parse(String comment) {
		JsDoc jsDoc = new JsDoc();

		comment = comment.replaceAll("\r\n", "\n");
		comment = comment.replaceAll("\r", "\n");

		comment = comment.replaceFirst("^/\\*\\*\\s*", "");
		comment = comment.replaceFirst("\\s*\\*/$", "");

		StringBuilder sb = new StringBuilder();
		String[] lines = comment.split("\n");
		for (String line : lines) {
			line = line.replaceFirst("^\\s*\\*\\s*", "");
			sb.append(line).append("\n");

			{
				Matcher matcher = PATTERN_PARAM.matcher(line);
				if (matcher.matches()) {
					JsDocParam param = new JsDocParam();
					String[] dim = matcher.group(1).trim().split("\\s");
					StringBuilder desc = new StringBuilder();
					for (int i = 0; i < dim.length; i++) {
						if (param.typeList.isEmpty() && dim[i].startsWith("{") && dim[i].endsWith("}")) {
							String typeStr = dim[i].substring(1, dim[i].length() - 1);
							String[] types = typeStr.split("\\|");
							for (String type : types) {
								if (!StringUtils.isEmpty(type)) {
									param.typeList.add(convertType(type));
								}
							}
						} else if (param.name == null) {
							param.name = dim[i];
						} else {
							desc.append(dim[i]).append(" ");
						}
					}
					if (param.typeList.isEmpty()) {
						param.typeList.add("*");
					}
					param.desciption = desc.toString();
					jsDoc.params.add(param);
					continue;
				}
			}
			{
				Matcher matcher = PATTERN_RETURN.matcher(line);
				if (matcher.matches()) {
					jsDoc.returnDescription = matcher.group(1).trim();
					continue;
				}
			}
			{
				Matcher matcher = PATTERN_TYPE.matcher(line);
				if (matcher.matches()) {
					updateReturnType(jsDoc, matcher.group(1));
					continue;
				}
			}
			{
				Matcher matcher = PATTERN_RETURNS.matcher(line);
				if (matcher.matches()) {
					String[] dim = matcher.group(1).trim().split("\\s");
					StringBuilder desc = new StringBuilder();
					for (int i = 0; i < dim.length; i++) {
						if (jsDoc.returnTypeList.isEmpty() && dim[i].startsWith("{") && dim[i].endsWith("}")) {
							updateReturnType(jsDoc, dim[i].substring(1, dim[i].length() - 1));
						} else {
							desc.append(dim[i]).append(" ");
						}
					}
					jsDoc.returnDescription = desc.toString();
					continue;
				}
			}
			{
				Matcher matcher = PATTERN_STATIC.matcher(line);
				if (matcher.matches()) {
					jsDoc.isStatic = true;
					continue;
				}
			}
			{
				Matcher matcher = PATTERN_PRIVATE.matcher(line);
				if (matcher.matches()) {
					jsDoc.isPrivate = true;
					continue;
				}
			}
			{
				Matcher matcher = PATTERN_CLASS.matcher(line);
				if (matcher.matches()) {
					jsDoc.className = matcher.group(1).trim();
					continue;
				}
			}
		}

		jsDoc.text = sb.toString();

		return jsDoc;
	}

	private static void updateReturnType(JsDoc jsDoc, String value) {
		if (value != null) {
			String[] returnTypes = value.split("\\|");
			for (String returnType : returnTypes) {
				returnType = returnType.trim();
				if (returnType.length() > 0) {
					jsDoc.returnTypeList.add(convertType(returnType));
				}
			}
		}
	}

	private static String convertType(String type) {
		if ("string".equals(type)) {
			return "String";
		} else if ("boolean".equals(type)) {
			return "Boolean";
		} else if ("number".equals(type)) {
			return "Number";
		}
		return type;
	}

	public static class JsDoc {
		public String text;
		public List<JsDocParam> params = new ArrayList<JsDocParam>();
		public String returnDescription;
		public List<String> returnTypeList = new ArrayList<String>();
		public boolean isStatic = false;
		public boolean isPrivate = false;
		public String className;
	}

	public static class JsDocParam {
		public String name;
		public List<String> typeList = new ArrayList<String>();
		public String desciption;
	}

}