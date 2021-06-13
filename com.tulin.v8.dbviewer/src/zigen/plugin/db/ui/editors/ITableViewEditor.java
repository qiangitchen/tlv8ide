/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors;

import org.eclipse.jface.viewers.TableViewer;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public interface ITableViewEditor {

	public static final String EDIT_MODE_OFF = Messages.getString("ITableViewEditor.0"); //$NON-NLS-1$

	public static final String EDIT_MODE_ON = Messages.getString("ITableViewEditor.1"); //$NON-NLS-1$

	// public static final int SHEET_LOG = -1;

	public static final int SHEET_INFO = 2;

	public static final int SHEET_DDL = 1;

	public static final int SHEET_DATA = 0;

	public abstract TableViewer getViewer();

	public abstract ITable getTableNode();

	public abstract TableElement getHeaderTableElement();

	public abstract void editTableElement(Object element, int column);

	public abstract IDBConfig getDBConfig();

	public abstract String getCondition();

	public abstract void setTotalCount(int dispCount, long totalCount);

	public abstract void changeColumnColor(Column column);

	public abstract void changeColumnColor();

	public abstract void setEnabled(boolean enabled);

	public abstract int getRecordOffset();

	public abstract int getRecordLimit();
}
