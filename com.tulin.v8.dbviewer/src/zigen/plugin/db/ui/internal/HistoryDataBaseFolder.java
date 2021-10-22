/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import zigen.plugin.db.core.IDBConfig;

public class HistoryDataBaseFolder extends TreeNode {

	private static final long serialVersionUID = 1L;

	private IDBConfig config;

	public HistoryDataBaseFolder(IDBConfig config) {
		super();
		this.config = config;
	}

	public String getName() {
		if (config != null) {
			return config.getDbName();
		} else {
			return null;
		}
	}


}
