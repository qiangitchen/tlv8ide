/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.Serializable;

import zigen.plugin.db.core.rule.AbstractStatementFactory;

public class SchemaInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	IDBConfig config;

	boolean isChecked = false;

	String name = null;

	public SchemaInfo() {}

	public SchemaInfo(IDBConfig config, String name) {
		this(config, name, false);
	}

	public SchemaInfo(IDBConfig config, String name, boolean checked) {
		this.config = config;
		this.name = name;
		this.isChecked = checked;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (config != null) {
			sb.append("DBConfig:").append(config.getDbName());

		} else {
			sb.append("DBConfig:null");
		}
		sb.append(", NAME:").append(name);
		sb.append(", isChecked:").append(isChecked);
		return sb.toString();

	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		SchemaInfo castedObj = (SchemaInfo) o;
		return (this.name == castedObj.name && this.config.equals(castedObj.config));
	}

	public IDBConfig getConfig() {
		return config;
	}

	public void setConfig(IDBConfig config) {
		this.config = config;
	}

}
