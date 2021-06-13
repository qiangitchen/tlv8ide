/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors;

import java.sql.Types;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.rule.AbstractMappingFactory;
import zigen.plugin.db.core.rule.IMappingFactory;
import zigen.plugin.db.ui.editors.event.TableKeyEventHandler;
import zigen.plugin.db.ui.editors.internal.CellEditorType;
import zigen.plugin.db.ui.editors.internal.ColumnFilterInfo;
import zigen.plugin.db.ui.editors.internal.FileCellEditor;
import zigen.plugin.db.ui.internal.ITable;


public class CellModifier implements ICellModifier {

	protected ITableViewEditor editor;

	protected ITable table;

	protected TableViewer viewer;

	protected Object oldValue = null;

	protected Object newValue = null;

	protected IMappingFactory factory;

	protected ColumnFilterInfo[] filterInfos;

	TableKeyEventHandler handler = null;

	// public CellModifier(TableViewEditorFor31 editor, ColumnFilterInfo[]
	// filterInfos) {
	public CellModifier(ITableViewEditor editor, ColumnFilterInfo[] filterInfos, TableKeyEventHandler handler) {

		if (handler == null)
			System.err.println("TableKeyEventHandler is null !!");

		this.editor = editor;
		this.table = editor.getTableNode();
		this.viewer = editor.getViewer();

		this.factory = AbstractMappingFactory.getFactory(table.getDbConfig());
		this.filterInfos = filterInfos;

		// handler = new TableKeyEventHandler(editor);
		this.handler = handler;
	}

	public boolean canModify(Object element, String property) {
		int index = Integer.parseInt(property);

		if (index == 0)
			return false;

		if (element instanceof TableElement) {
			TableElement elem = (TableElement) element;

			if (elem.isCanModify()) {

				ColumnFilterInfo info = filterInfos[index - 1];
				if (!info.isChecked())
					return false;

				TableColumn col = elem.getColumns()[index - 1];

				if (elem.isNew()) {
					switch (col.getDataType()) {
					case Types.BINARY: // -2
					case Types.VARBINARY: // -3
					case Types.LONGVARBINARY: // -4
					case Types.CLOB:
					case Types.BLOB:
						return false;
					}
				}

				if (factory.canModifyDataType(col.getDataType())) {
					return true;
				} else {
					return false;
				}

			}
		}
		return false;
	}

	public Object getValue(Object element, String property) {
		int index = Integer.parseInt(property);
		if (element instanceof TableElement) {
			TableElement elem = (TableElement) element;
			Object obj = elem.getItems()[index - 1];

			if (obj != null) {
				if (obj instanceof String) {
					oldValue = (String) obj;
					return oldValue;
				} else {
					return CellEditorType.getDataTypeName(elem.getColumns()[index]);
				}
			} else {
				oldValue = ""; //$NON-NLS-1$
				return ""; //$NON-NLS-1$

			}
		}

		return null;
	}

	public void modify(Object element, String property, Object value) {
		// + ", new:" + value);

		if (value == null)
			value = ""; //$NON-NLS-1$

		TableElement tableElement = getTableElement(element);

		newValue = value;
		if (value instanceof FileCellEditor) {
			FileCellEditor fc = (FileCellEditor) value;

			if (!fc.isOpened()) {
				fc.setOpened(true);
				Shell shell = viewer.getControl().getShell();
				int colIndex = Integer.parseInt(property);
				try {
					LobViewDialog v = new LobViewDialog(shell, tableElement, colIndex);
					v.open();
					fc.setOpened(false);
					return;
				} catch (Exception e) {
					DbPlugin.log(e);
				}

			} else {

				return;

			}

		} else {
			if (oldValue != null) {
				int col = Integer.parseInt(property);

				if (tableElement.isNew()) {
					handler.updateColumn(tableElement, col, value);

				} else if (!oldValue.equals(newValue) || tableElement.isModify()) {

					if (!tableElement.isUpdatedDataBase()) {

						handler.updateColumn(tableElement, col, value);

					} else {
						tableElement.setUpdatedDataBase(false);
					}
				}
			}
		}

	}

	private TableElement getTableElement(Object obj) {
		if (obj instanceof Item) {
			Object o = ((Item) obj).getData();
			if (o instanceof TableElement) {
				return (TableElement) o;
			}
		}
		return null;
	}

}
