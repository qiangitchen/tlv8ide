/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.Serializable;

public class FolderInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	boolean isChecked = false;

	String name = null;

	public FolderInfo() {

	}

	public FolderInfo(String name, boolean checked) {
		this.name = name;
		this.isChecked = checked;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("NAME:").append(name);
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
		FolderInfo castedObj = (FolderInfo) o;
		return (this.name == castedObj.name);
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
}
