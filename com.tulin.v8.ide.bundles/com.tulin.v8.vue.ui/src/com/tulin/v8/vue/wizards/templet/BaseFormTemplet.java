package com.tulin.v8.vue.wizards.templet;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.tulin.v8.core.StringArray;
import com.tulin.v8.core.utils.PlaceholderUtils;
import com.tulin.v8.vue.wizards.templet.utils.TempletsReader;

public class BaseFormTemplet extends TempletsReader {

	public static String getPageContext(String dbkey, String tableName, String keyField, StringArray formInfo,
			JSONObject formColumns) throws Exception {
		String context = getTempletPageContext("baseForm");
		Map<String, Object> data = new HashMap<>();
		data.put("tableName", tableName);
		data.put("keyField", keyField);
		data.put("formInfo", formInfo.join("\n"));
		data.put("formColumns", formColumns.toString());
		context = PlaceholderUtils.process(context, data);
		return context;
	}
}
