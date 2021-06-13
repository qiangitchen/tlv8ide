/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal;

import java.sql.Types;

import zigen.plugin.db.core.TableColumn;

public class CellEditorType {

	public static boolean isFileSaveType(TableColumn col) {
		boolean b = false;

		switch (col.getDataType()) {
		case Types.BINARY: // -2
		case Types.VARBINARY: // -3
		case Types.LONGVARBINARY: // -4
		case Types.CLOB:
		case Types.BLOB:
			b = true;
			break;
		default:
			break;
		}
		return b;
	}

	public static String getDataTypeName(TableColumn col) {
		String name = ""; //$NON-NLS-1$

		switch (col.getDataType()) {
		case Types.BINARY: // -2
			name = Messages.getString("CellEditorType.1"); //$NON-NLS-1$
			break;
		case Types.VARBINARY: // -3
			name = Messages.getString("CellEditorType.2"); //$NON-NLS-1$
			break;
		case Types.LONGVARBINARY: // -4
			name = Messages.getString("CellEditorType.3"); //$NON-NLS-1$
			break;
		case Types.CLOB:
			name = Messages.getString("CellEditorType.4"); //$NON-NLS-1$
			break;
		case Types.BLOB:
			name = Messages.getString("CellEditorType.5"); //$NON-NLS-1$
			break;
		default:
			break;
		}
		return name;
	}
}
