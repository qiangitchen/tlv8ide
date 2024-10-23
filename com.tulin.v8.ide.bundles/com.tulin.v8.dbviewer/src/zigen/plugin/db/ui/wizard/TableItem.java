/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.wizard;

import zigen.plugin.db.core.IDBConfig;

public class TableItem implements IItem {

	IDBConfig config;

	boolean checked = false;

	public TableItem(IDBConfig config) {
		this(config, false);
	}

	public TableItem(IDBConfig config, boolean b) {
		this.config = config;
		this.checked = b;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getDbName() {
		return config.getDbName();
	}

	public String getUrl() {
		return config.getUrl();
	}

	public IDBConfig getConfig() {
		return config;
	}

}
