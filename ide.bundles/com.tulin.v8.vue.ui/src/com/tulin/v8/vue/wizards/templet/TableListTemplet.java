package com.tulin.v8.vue.wizards.templet;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tulin.v8.core.StringArray;
import com.tulin.v8.core.utils.PlaceholderUtils;
import com.tulin.v8.vue.wizards.templet.utils.TempletsReader;

public class TableListTemplet extends TempletsReader {

	public static String getPageContext(String dbkey, String tableName, String keyField, String dataOrder,
			JSONArray jsona, JSONArray searchColumns, StringArray formInfo, JSONObject formColumns) throws Exception {
		String context = getTempletPageContext("tableList");
		Map<String, Object> data = new HashMap<>();
		data.put("tableName", tableName);
		data.put("keyField", keyField);
		data.put("dataOrder", dataOrder);
		data.put("columns", jsona.toString());
		data.put("searchColumns", searchColumns.toString());
		data.put("formInfo", formInfo.join("\n"));
		data.put("formColumns", formColumns.toString());
		context = PlaceholderUtils.process(context, data);
		return context;
	}

}
