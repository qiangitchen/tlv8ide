package com.tulin.v8.ide.ui.editors.process.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinePropertySet {
	public static List<String> lineproperlist = new ArrayList<String>();
	private static Map<String, String> LabelMap = new HashMap<String, String>();

	static {
		lineproperlist.add("l_p_id");
		LabelMap.put("l_p_id",
				Messages.getString("TLEditor.PropertySet.l_p_id"));
		LabelMap.put("l_p_name",
				Messages.getString("TLEditor.PropertySet.l_p_name"));
		lineproperlist.add("l_p_name");
		LabelMap.put("l_p_pre",
				Messages.getString("TLEditor.PropertySet.l_p_pre"));
		lineproperlist.add("l_p_pre");
		LabelMap.put("l_p_next",
				Messages.getString("TLEditor.PropertySet.l_p_next"));
		lineproperlist.add("l_p_next");
	}

	public static String getProperLabel(String properid) {
		return LabelMap.get(properid);
	}

}
