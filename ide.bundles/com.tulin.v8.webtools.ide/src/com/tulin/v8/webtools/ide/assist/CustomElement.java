package com.tulin.v8.webtools.ide.assist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

public class CustomElement {

	private String displayName;
	private String assistString;

	public CustomElement(String displayName, String assistString) {
		this.displayName = displayName;
		this.assistString = assistString;
	}

	public String getAssistString() {
		return assistString;
	}

	public void setAssistString(String assistString) {
		this.assistString = assistString;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public static List<CustomElement> loadFromPreference(boolean defaults) {
		IPreferenceStore store = WebToolsPlugin.getDefault().getPreferenceStore();
		String value = null;
		if (defaults) {
			value = store.getDefaultString(WebToolsPlugin.PREF_CUSTOM_ELEMENTS);
		} else {
			value = store.getString(WebToolsPlugin.PREF_CUSTOM_ELEMENTS);
		}
		List<CustomElement> list = new ArrayList<CustomElement>();
		if (value != null) {
			String[] values = value.split("\n");
			for (int i = 0; i < values.length; i++) {
				String[] split = values[i].split("\t");
				if (split.length == 2) {
					list.add(new CustomElement(split[0], split[1]));
				}
			}
		}
		return list;
	}

	public static void saveToPreference(List<CustomElement> list) {
		IPreferenceStore store = WebToolsPlugin.getDefault().getPreferenceStore();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			CustomElement element = list.get(i);
			sb.append(element.getDisplayName());
			sb.append("\t");
			sb.append(element.getAssistString());
			sb.append("\n");
		}
		store.setValue(WebToolsPlugin.PREF_CUSTOM_ELEMENTS, sb.toString());
	}
}
