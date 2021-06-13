/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.internal;

import zigen.plugin.db.core.IDBConfig;

public class View extends Table implements ITable {

	private static final long serialVersionUID = 1L;

	public View(String name, String remarks) {
		super(name);
		this.remarks = remarks;
	}

	public View(String name) {
		super(name);
	}

	public View() {
		super();
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}

		View castedObj = (View) o;
		IDBConfig config = castedObj.getDbConfig();
		Schema schema = castedObj.getSchema();

		if (config == null) {
			return false;
		}

		if (castedObj.getName().equals(getName()) && config.equals(getDbConfig()) && schema.equals(getSchema())) {
			return true;
		} else {
			return false;
		}

	}
}
