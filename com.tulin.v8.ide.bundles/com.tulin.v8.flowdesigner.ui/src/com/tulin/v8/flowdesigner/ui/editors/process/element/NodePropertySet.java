package com.tulin.v8.flowdesigner.ui.editors.process.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodePropertySet {
	public static List<String> nodeproperlist = new ArrayList<String>();
	private static Map<String, String> LabelMap = new HashMap<String, String>();
	private static Map<String, String> textlMap = new HashMap<String, String>();
	private static Map<String, String> buttonMap = new HashMap<String, String>();
	private static Map<String, String[][]> optionsMap = new HashMap<String, String[][]>();

	static {
		nodeproperlist.add("n_p_id");
		LabelMap.put("n_p_id",
				Messages.getString("TLEditor.PropertySet.n_p_id"));
		textlMap.put("n_p_id", "span");
		nodeproperlist.add("n_p_name");
		LabelMap.put("n_p_name",
				Messages.getString("TLEditor.PropertySet.n_p_name"));
		textlMap.put("n_p_name", "input");
		nodeproperlist.add("n_p_exepage");
		LabelMap.put("n_p_exepage",
				Messages.getString("TLEditor.PropertySet.n_p_exepage"));
		textlMap.put("n_p_exepage", "input");
		nodeproperlist.add("n_p_label");
		LabelMap.put("n_p_label",
				Messages.getString("TLEditor.PropertySet.n_p_label"));
		textlMap.put("n_p_label", "input");
		nodeproperlist.add("n_p_desc");
		LabelMap.put("n_p_desc",
				Messages.getString("TLEditor.PropertySet.n_p_desc"));
		textlMap.put("n_p_desc", "input");
		nodeproperlist.add("n_p_group");
		LabelMap.put("n_p_group",
				Messages.getString("TLEditor.PropertySet.n_p_group"));
		textlMap.put("n_p_group", "span");
		nodeproperlist.add("n_p_roleID");
		LabelMap.put("n_p_roleID",
				Messages.getString("TLEditor.PropertySet.n_p_roleID"));
		textlMap.put("n_p_roleID", "span");
		nodeproperlist.add("n_p_role");
		LabelMap.put("n_p_role",
				Messages.getString("TLEditor.PropertySet.n_p_role"));
		textlMap.put("n_p_role", "span");
		nodeproperlist.add("n_r_grab");
		LabelMap.put("n_r_grab",
				Messages.getString("TLEditor.PropertySet.n_r_grab"));
		textlMap.put("n_r_grab", "select");
		nodeproperlist.add("n_r_grabway");
		LabelMap.put("n_r_grabway",
				Messages.getString("TLEditor.PropertySet.n_r_grabway"));
		textlMap.put("n_r_grabway", "select");
		nodeproperlist.add("n_p_in");
		LabelMap.put("n_p_in",
				Messages.getString("TLEditor.PropertySet.n_p_in"));
		textlMap.put("n_p_in", "input");
		nodeproperlist.add("n_p_back");
		LabelMap.put("n_p_back",
				Messages.getString("TLEditor.PropertySet.n_p_back"));
		textlMap.put("n_p_back", "input");
		nodeproperlist.add("n_r_transe");
		LabelMap.put("n_r_transe",
				Messages.getString("TLEditor.PropertySet.n_r_transe"));
		textlMap.put("n_r_transe", "span");
		nodeproperlist.add("n_t_queryt");
		LabelMap.put("n_t_queryt",
				Messages.getString("TLEditor.PropertySet.n_t_queryt"));
		textlMap.put("n_t_queryt", "select");
		nodeproperlist.add("n_p_time");
		LabelMap.put("n_p_time",
				Messages.getString("TLEditor.PropertySet.n_p_time"));
		textlMap.put("n_p_time", "input");
		nodeproperlist.add("n_p_timetype");
		LabelMap.put("n_p_timetype",
				Messages.getString("TLEditor.PropertySet.n_p_timetype"));
		textlMap.put("n_p_timetype", "input");
		nodeproperlist.add("n_p_call1");
		LabelMap.put("n_p_call1",
				Messages.getString("TLEditor.PropertySet.n_p_call1"));
		textlMap.put("n_p_call1", "input");
		nodeproperlist.add("n_p_call2");
		LabelMap.put("n_p_call2",
				Messages.getString("TLEditor.PropertySet.n_p_call2"));
		textlMap.put("n_p_call2", "input");
		nodeproperlist.add("n_p_call3");
		LabelMap.put("n_p_call3",
				Messages.getString("TLEditor.PropertySet.n_p_call3"));
		textlMap.put("n_p_call3", "input");
		nodeproperlist.add("n_p_call4");
		LabelMap.put("n_p_call4",
				Messages.getString("TLEditor.PropertySet.n_p_call4"));
		textlMap.put("n_p_call4", "input");
		nodeproperlist.add("n_p_call5");
		LabelMap.put("n_p_call5",
				Messages.getString("TLEditor.PropertySet.n_p_call5"));
		textlMap.put("n_p_call5", "input");

		buttonMap.put("n_p_exepage", "selectexePage");
		buttonMap.put("n_p_label", "selectexeLabel");
		buttonMap.put("n_p_roleID", "selectexePerson");
		buttonMap.put("n_p_role", "selectexePerson");
		buttonMap.put("n_r_transe", "selectRangeTran");
		buttonMap.put("n_p_group", "selectRange");

		buttonMap.put("c_p_expression", "selectConditionExpression");

		String[][] jss = new String[][] {
				{ Messages.getString("TLEditor.PropertySet.whenOpen"),
						"whenOpen" },
				{ Messages.getString("TLEditor.PropertySet.whenExt"), "whenExt" },
				{ Messages.getString("TLEditor.PropertySet.together"),
						"together" } };
		optionsMap.put("n_r_grab", jss);
		String[][] jsa = new String[][] {
				{ Messages.getString("TLEditor.PropertySet.merge"), "merge" },
				{ Messages.getString("TLEditor.PropertySet.branch"), "branch" } };
		optionsMap.put("n_r_grabway", jsa);
		String[][] jsb = new String[][] {
				{ Messages.getString("TLEditor.PropertySet.yes"), "yes" },
				{ Messages.getString("TLEditor.PropertySet.no"), "no" } };
		optionsMap.put("n_t_queryt", jsb);
	}

	public static String getProperLabel(String properid) {
		return LabelMap.get(properid);
	}

	public static String getProperText(String properid) {
		return textlMap.get(properid);
	}

	public static String getProperButton(String properid) {
		return buttonMap.get(properid);
	}

	public static Map<String, String> getLabelMap() {
		return LabelMap;
	}

	public static String[][] getOptions(String properid) {
		return optionsMap.get(properid);
	}
}
