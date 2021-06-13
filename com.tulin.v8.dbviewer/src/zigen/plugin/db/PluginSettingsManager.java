/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;

public class PluginSettingsManager extends DefaultXmlManager {

	public static final String KEY_DEFAULT_DB = "KEY_DEFAULT_DB"; //$NON-NLS-1$

	public static final String KEY_FILTER_LIST = "KEY_FILTER_LIST"; //$NON-NLS-1$

	public static final String KEY_FILTER_LIST_COLUMN = "KEY_FILTER_LIST_COLUMN"; //$NON-NLS-1$

	public static final String KEY_FILTER_LIST_HISTORY = "KEY_FILTER_LIST_HISTORY"; //$NON-NLS-1$

	public static final String KEY_LOB_CHARSET = "KEY_LOG_CHARSET"; //$NON-NLS-1$

	public static final String KEY_SQLHISTORY_LAYOUT = "KEY_SQLHISTORY_LAYOUT"; //$NON-NLS-1$

	public static final String KEY_LINKED_EDITOR = "KEY_LINKED_EDITOR"; //$NON-NLS-1$

	public static final String KEY_AUTO_FORMAT = "KEY_AUTO_FORMAT"; //$NON-NLS-1$

	// element filter
	public static final String KEY_ELEM_FILTER_PATTERN = "KEY_ELEM_FILTER_PATTERN"; //$NON-NLS-1$

	public static final String KEY_ELEM_FILTER_VISIBLE = "KEY_ELEM_FILTER_VISIBLE"; //$NON-NLS-1$

	public static final String KEY_ELEM_FILTER_REGULAREXP = "KEY_ELEM_FILTER_REGULAREXP"; //$NON-NLS-1$

	public static final String KEY_ELEM_FILTER_CASESENSITIVE = "KEY_ELEM_FILTER_CASESENSITIVE"; //$NON-NLS-1$

	public static final String KEY_ELEM_FILTER_FOLDER_LIST = "KEY_ELEM_FILTER_FOLDER_LIST"; //$NON-NLS-1$

	private Map map = null;

	public PluginSettingsManager(IPath path) {
		super(path, DbPluginConstant.FN_PLUGIN);
		map = load();

		if (map == null) {
			map = new HashMap();
		}
	}

	public Object getValue(String key) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			return null;
		}
	}

	public boolean getValueBoolean(String key) {
		boolean b = false;
		Boolean obj = (Boolean) getValue(key);
		if (obj != null)
			b = obj.booleanValue();
		return b;
	}

	public String getValueString(String key) {
		String obj = (String) getValue(key);
		return obj != null ? obj : "";
	}


	public void setValue(String key, String value) {
		map.put(key, value);
	}

	public void setValue(String key, Object value) {
		map.put(key, value);
	}

	public Map load() {
		try {
			Object obj = super.loadXml();
			if (obj instanceof Map) {
				return (Map) obj;
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e); //$NON-NLS-1$
		}
		return null;
	}

	public void save() {
		try {
			super.saveXml(map);
		} catch (IOException e) {
			DbPlugin.log(e);
		}

	}
}
