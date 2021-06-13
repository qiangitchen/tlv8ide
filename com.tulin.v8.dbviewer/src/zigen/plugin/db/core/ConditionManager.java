/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.DefaultXmlManager;

public class ConditionManager extends DefaultXmlManager {

	private Map map = null;

	public ConditionManager(IPath path) {
		super(path, DbPluginConstant.FN_CONDITION_HISTORY);
		map = load();
		if (map == null) {
			map = new HashMap();
		}
	}

	public Condition getCondition(String connectionUrl, String schema, String table) {
		String key;
		if (schema != null) {
			key = connectionUrl + "." + schema + "." + table; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			key = connectionUrl + "." + table; //$NON-NLS-1$
		}
		if (map.containsKey(key)) {
			return (Condition) map.get(key);
		} else {
			return null;
		}
	}

	public void setCondition(Condition condition) {
		String key;
		if (condition.getSchema() != null) {
			key = condition.getConnectionUrl() + "." + condition.getSchema() + "." + condition.getTable(); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			key = condition.getConnectionUrl() + "." + condition.getTable(); //$NON-NLS-1$
		}
		map.put(key, condition);
	}

	public Map load() {
		try {
			Object obj = super.loadXml();
			if (obj instanceof Map) {
				return (Map) obj;
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
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
