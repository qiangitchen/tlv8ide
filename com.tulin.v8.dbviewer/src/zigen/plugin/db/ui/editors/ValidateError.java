/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors;

import java.io.Serializable;

public class ValidateError implements Serializable {

	private static final long serialVersionUID = 1L;

	int row = 0;

	int column = 0;

	String message = null;

	public ValidateError(int row, int column, String message) {
		this.row = row;
		this.column = column;
		this.message = message;
	}

	public int getColumn() {
		return column;
	}

	public String getMessage() {
		return message;
	}

	public int getRow() {
		return row;
	}
}
