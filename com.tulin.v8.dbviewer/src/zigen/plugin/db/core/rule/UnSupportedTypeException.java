/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core.rule;

import zigen.plugin.db.core.TableColumn;

public class UnSupportedTypeException extends Exception {

	private static final long serialVersionUID = 1L;

	TableColumn column;

	Object value;

	public UnSupportedTypeException(TableColumn column, Object value) {
		super();
		this.column = column;
		this.value = value;

	}

	public TableColumn getColumn() {
		return column;
	}

	public Object getValue() {
		return value;
	}

}
