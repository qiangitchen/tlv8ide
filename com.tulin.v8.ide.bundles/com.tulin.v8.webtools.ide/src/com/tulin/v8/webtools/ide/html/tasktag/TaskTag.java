package com.tulin.v8.webtools.ide.html.tasktag;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.preference.IPreferenceStore;

import com.tulin.v8.webtools.ide.WebToolsPlugin;

public class TaskTag implements Cloneable {

	private String tag;
	private int priority;

	/** The display string for IMaker.PRIORITY_HIGH */
	public static final String HIGH = "High";

	/** The display string for IMaker.PRIORITY_NORMAL */
	public static final String NORMAL = "Normal";

	/** The display string for IMaker.PRIORITY_LOW */
	public static final String LOW = "Low";

	/** The display strings for priorities */
	public static final String[] PRIORITIES = { HIGH, NORMAL, LOW };

	public TaskTag(String tag, int priority) {
		setTag(tag);
		setPriority(priority);
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getPriorityName() {
		switch (this.priority) {
		case IMarker.PRIORITY_HIGH:
			return HIGH;
		case IMarker.PRIORITY_NORMAL:
			return NORMAL;
		case IMarker.PRIORITY_LOW:
			return LOW;
		default:
			return "";
		}
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public TaskTag clone() throws CloneNotSupportedException {
		return new TaskTag(getTag(), getPriority());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TaskTag) {
			TaskTag tag = (TaskTag) obj;
			return getTag().equals(tag.getTag()) && getPriority() == tag.getPriority();
		}
		return false;
	}

	public static boolean hasChange(List<TaskTag> tags1, List<TaskTag> tags2) {
		if (tags1.size() != tags2.size()) {
			return true;
		}
		for (int i = 0; i < tags1.size(); i++) {
			TaskTag tag1 = (TaskTag) tags1.get(i);
			TaskTag tag2 = (TaskTag) tags2.get(i);
			if (!tag1.equals(tag2)) {
				return true;
			}
		}
		return false;
	}

	public static List<TaskTag> loadFromPreference(boolean defaults) {
		IPreferenceStore store = WebToolsPlugin.getDefault().getPreferenceStore();
		String value = null;
		if (defaults) {
			value = store.getDefaultString(WebToolsPlugin.PREF_TASK_TAGS);
		} else {
			value = store.getString(WebToolsPlugin.PREF_TASK_TAGS);
		}
		List<TaskTag> list = new ArrayList<TaskTag>();
		if (value != null) {
			String[] values = value.split("\n");
			for (int i = 0; i < values.length; i++) {
				String[] split = values[i].split("\t");
				if (split.length == 2) {
					list.add(new TaskTag(split[0], Integer.parseInt(split[1])));
				}
			}
		}
		return list;
	}

	public static void saveToPreference(List<TaskTag> list) {
		IPreferenceStore store = WebToolsPlugin.getDefault().getPreferenceStore();
		StringBuffer sb = new StringBuffer();
		for (TaskTag tag : list) {
			sb.append(tag.getTag());
			sb.append("\t");
			sb.append(tag.getPriority());
			sb.append("\n");
		}
		store.setValue(WebToolsPlugin.PREF_TASK_TAGS, sb.toString());
	}

	/**
	 * Converts the display string to the priority which is defined in
	 * {@link IMarker}.
	 * 
	 * @param name the display string of the priority
	 * @return the priority value
	 */
	public static int convertPriority(String name) {
		int priority = IMarker.PRIORITY_NORMAL;

		if (name.equals("High")) {
			priority = IMarker.PRIORITY_HIGH;

		} else if (name.equals("Normal")) {
			priority = IMarker.PRIORITY_NORMAL;

		} else if (name.equals("Low")) {
			priority = IMarker.PRIORITY_LOW;
		}

		return priority;
	}

}
