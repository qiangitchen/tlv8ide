/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.Serializable;

import zigen.plugin.db.ui.internal.ITable;


public class TableNewElement extends TableElement implements Serializable {

	private static final long serialVersionUID = 1L;

	public TableNewElement(ITable table, int recordNo, TableColumn[] columns, Object[] items, TableColumn[] uniqueColumns) {
		super(table, recordNo, columns, items, null, null);
		this.uniqueColumns = uniqueColumns;
		this.isNew = true;
		convertItems();
	}

	private void convertItems() {
		for (int i = 0; i < items.length; i++) {
			items[i] = padding(i, items[i]);
		}
	}


}
