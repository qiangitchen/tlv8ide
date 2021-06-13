/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import java.util.TreeMap;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SchemaInfo;
import zigen.plugin.db.ui.internal.Schema;

public class TreeViewSchemaFilter extends ViewerFilter {

	TreeMap settingSchemaMap = new TreeMap();

	public TreeViewSchemaFilter(SchemaInfo[] settingSchemas) {

		if (settingSchemas != null) {
			for (int i = 0; i < settingSchemas.length; i++) {
				SchemaInfo info = settingSchemas[i];
				String key = info.getConfig().getDbName() + "@" + info.getName();
				settingSchemaMap.put(key.toLowerCase(), info);
			}
		}

	}

	public boolean select(Viewer viewer, Object parent, Object element) {

		if (!settingSchemaMap.isEmpty()) {
			if (element instanceof Schema) {
				Schema _schema = (Schema) element;
				IDBConfig config = _schema.getDbConfig();

				String key = (config.getDbName() + "@" + _schema.getName()).toLowerCase();
				if (settingSchemaMap.containsKey(key)) {
					SchemaInfo si = (SchemaInfo) settingSchemaMap.get(key);
					return si.isChecked();

				} else {
					return true;
				}
			}
			return true;
		} else {
			return true;
		}

	}

}
