package com.tulin.v8.ide.navigator.views;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;

import com.tulin.v8.ide.Sys;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PluginSettingsManager extends DefaultXmlManager {

	public PluginSettingsManager(IPath path) {
		super(path, "plugin_settings.xml");
		map = load();

		if (map == null) {
			map = new HashMap();
		}
	}

	private Map map = null;

	public static boolean isLink = false;

	public Boolean getValue() {
		return isLink;
	}

	public Object getValue(String key) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			return null;
		}
	}

	public void setValue(boolean checked) {
		isLink = checked;
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
			Sys.packErrMsg(e.toString());
		}
		return null;
	}

	public void save() {
		try {
			super.saveXml(map);
		} catch (IOException e) {
			Sys.packErrMsg(e.toString());
		}

	}

}
