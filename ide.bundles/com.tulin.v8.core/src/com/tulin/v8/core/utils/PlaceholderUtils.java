package com.tulin.v8.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 实现${}进行匹配占位符并且替换数据工具类
 * 
 * @author chenqian
 */
public class PlaceholderUtils {
	public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{(.*?)\\}");

	public static String process(String model, Map<String, Object> data)
			throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Matcher matcher = PLACEHOLDER_PATTERN.matcher(model);
		boolean findReplace = matcher.find();
		if (!findReplace) {
			return model;
		}
		return doProcess(matcher, data);
	}

	private static String doProcess(Matcher matcher, Map<String, Object> data)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		StringBuffer values = new StringBuffer();
		matcher.reset();
		while (matcher.find()) {
			String key = matcher.group(1);
			Object value = data.get(key);
			matcher.appendReplacement(values, String.valueOf(value));
		}
		matcher.appendTail(values);
		return values.toString();
	}

}
