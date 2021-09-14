package com.tulin.v8.flowdesigner.ui.editors.process.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeOvalPropertySet {
	public static List<String> properlist = new ArrayList<String>();
	private static Map<String, String> LabelMap = new HashMap<String, String>();

	static {
		properlist.add("n_p_id");
		LabelMap.put("n_p_id",
				Messages.getString("TLEditor.PropertySet.n_p_id"));
		properlist.add("n_p_name");
		LabelMap.put("n_p_name",
				Messages.getString("TLEditor.PropertySet.n_p_name"));
	}

	public static String getProperLabel(String properid) {
		return LabelMap.get(properid);
	}

}
