package com.tulin.v8.flowdesigner.ui.editors.process.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tulin.v8.flowdesigner.ui.editors.process.FlowDesignEditor;

public class ConditionPropertySet {
	public static List<String> properlist = new ArrayList<String>();
	public static String DomConditionSelects = "[]";
	private static Map<String, String> LabelMap = new HashMap<String, String>();
	private static Map<String, String> textlMap = new HashMap<String, String>();

	static {
		properlist.add("c_p_id");
		LabelMap.put("c_p_id", Messages.getString("TLEditor.PropertySet.c_p_id"));
		textlMap.put("c_p_id", "span");
		properlist.add("c_p_name");
		LabelMap.put("c_p_name", Messages.getString("TLEditor.PropertySet.c_p_name"));
		textlMap.put("c_p_name", "input");
		properlist.add("c_p_expression");
		LabelMap.put("c_p_expression", Messages.getString("TLEditor.PropertySet.c_p_expression"));
		textlMap.put("c_p_expression", "input");
		properlist.add("c_p_trueOut");
		LabelMap.put("c_p_trueOut", Messages.getString("TLEditor.PropertySet.c_p_trueOut"));
		textlMap.put("c_p_trueOut", "select");
		properlist.add("c_p_falseOut");
		LabelMap.put("c_p_falseOut", Messages.getString("TLEditor.PropertySet.c_p_falseOut"));
		textlMap.put("c_p_falseOut", "select");
	}

	public static String getProperLabel(String properid) {
		return LabelMap.get(properid);
	}

	public static String getProperText(String properid) {
		return textlMap.get(properid);
	}

	public static String[][] getOptions(String properid, FlowDesignEditor processeditor) {
		processeditor.callJsFunction("JavaCreatDomConditionSelect('" + properid + "')");
		String[][] res = new String[][] {};
		try {
			// System.err.println(DomConditionSelects);
			JSONArray json = new JSONArray(DomConditionSelects);
			res = new String[json.length()][2];
			for (int i = 0; i < json.length(); i++) {
				JSONObject option = json.getJSONObject(i);
				res[i][0] = option.getString("name");
				res[i][1] = option.getString("id");
			}
		} catch (Exception e) {
		}
		return res;
	}
}