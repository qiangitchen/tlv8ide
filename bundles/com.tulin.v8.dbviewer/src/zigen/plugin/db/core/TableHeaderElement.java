/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.Serializable;

import zigen.plugin.db.ui.internal.ITable;

public class TableHeaderElement extends TableElement implements Serializable {

	private static final long serialVersionUID = 1L;

	public TableHeaderElement(TableColumn[] columns) {
		super(null, 0, columns, null, null, null);
	}

	public TableHeaderElement(ITable table, TableColumn[] columns, TableColumn[] uniqueColumns) {
		super(table, 0, columns, null, uniqueColumns, null);
	}

}
