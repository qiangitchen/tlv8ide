/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.event;

import java.sql.Connection;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TableElementSearcher;
import zigen.plugin.db.core.TableNewElement;
import zigen.plugin.db.core.TransactionForTableEditor;
import zigen.plugin.db.core.rule.AbstractValidatorFactory;
import zigen.plugin.db.core.rule.IValidatorFactory;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.actions.InsertRecordAction;
import zigen.plugin.db.ui.editors.ITableViewEditor;
import zigen.plugin.db.ui.editors.TextCellEditor;
import zigen.plugin.db.ui.editors.exceptions.UpdateException;
import zigen.plugin.db.ui.editors.exceptions.ZeroUpdateException;
import zigen.plugin.db.ui.editors.internal.RecordUpdateThread;
import zigen.plugin.db.ui.editors.internal.TableViewerManager;
import zigen.plugin.db.ui.internal.ITable;

public class TableKeyEventHandler {

	protected ITableViewEditor editor;

	protected TableViewer viewer;

	protected Table table;

	protected IDBConfig config;

	public TableKeyEventHandler(ITableViewEditor editor) {
		this.editor = editor;
		this.viewer = editor.getViewer();
		this.table = editor.getViewer().getTable();
		this.config = editor.getDBConfig();
	}

	public int getSelectedRow() {
		return table.getSelectionIndex();
	}

	public void selectRow(int index) {
		table.select(index);
	}

	public int getSelectedCellEditorIndex() {
		int defaultIndex = 0;
		CellEditor[] editors = viewer.getCellEditors();
		if (editors == null)
			return -1;
		for (int i = 0; i < editors.length; i++) {
			if (editors[i] != null && editors[i].isActivated()) {
				return i;
			}
		}
		return defaultIndex;
	}

	public int getEditableNextColumn(int cuurentCol) {
		ICellModifier modifier = viewer.getCellModifier();
		int nextCol = (cuurentCol < table.getColumnCount() - 1) ? cuurentCol + 1 : 1;
		if (modifier.canModify(getHeaderTableElement(), String.valueOf(nextCol))) {
			return nextCol;
		} else {
			return getEditableNextColumn(nextCol);
		}
	}

	public int getEditablePrevColumn(int CurrentCol) {
		ICellModifier modifier = viewer.getCellModifier();
		int nextCol = (CurrentCol == 1) ? table.getColumnCount() - 1 : CurrentCol - 1;
		if (modifier.canModify(getHeaderTableElement(), String.valueOf(nextCol))) {
			return nextCol;
		} else {
			return getEditablePrevColumn(nextCol);
		}
	}

	public TableElement getHeaderTableElement() {
		Object obj = viewer.getInput();
		if (obj instanceof TableElement[]) {
			TableElement[] elements = (TableElement[]) obj;
			if (elements.length > 0) {
				return elements[0];
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public void editTableElement(int rowIndex, int columnIndex) {
		Object element = viewer.getElementAt(rowIndex);
		if (element != null) {
			if (columnIndex == 1) {
				table.showColumn(table.getColumn(0));
			} else {
				table.showColumn(table.getColumn(columnIndex));
			}
			viewer.editElement(element, columnIndex);

		}
	}

	public void updateColumn(TableElement element, int col, Object newValue) {
		element.updateItems(col - 1, newValue);
		viewer.update(element, null);
		columnsPack();

	}


	public void setMessage(String msg) {
		DbPlugin.getDefault().showWarningMessage(msg);
	}

	private void columnsPack() {
	}


	public boolean validate(int row, int col) {
		TableElement element = (TableElement) viewer.getElementAt(row);
		Object newValue = null;
		CellEditor editor = viewer.getCellEditors()[col];
		if (editor == null)
			throw new IllegalStateException("CellEditor is not set."); //$NON-NLS-1$
		int columnIndex = -1;
		if (editor instanceof TextCellEditor) {
			TextCellEditor tce = (TextCellEditor) editor;
			newValue = tce.getInputValue();
			columnIndex = tce.getColumnIndex();

		}
		Object oldValue = element.getItems()[col - 1];
		if (!oldValue.equals(newValue)) {
			String msg = editor.getErrorMessage();
			if (msg == null) {
				updateColumn(element, col, newValue);
				return true;

			} else {
				viewer.cancelEditing();
				updateColumn(element, col, newValue);
				setMessage(msg); //$NON-NLS-1$
				editTableElement(row, col);
				return false;
			}

		}
		return true;

	}

	public boolean validateAll() {
		// log.debug("validateAll");
		try {
			int row = getSelectedRow();

			if (row == -1) {
				;// CTRL+C
			} else {
				TableElement element = (TableElement) viewer.getElementAt(row);
				IDBConfig config = element.getTable().getDbConfig();
				IValidatorFactory factory = AbstractValidatorFactory.getFactory(config);
				zigen.plugin.db.core.TableColumn[] columns = element.getColumns();
				String msg = null;
				for (int col = 0; col < columns.length; col++) {
					Object[] items = element.getItems();
					msg = factory.validate(columns[col], items[col]);
					if (msg != null) {
						viewer.cancelEditing();
						setMessage(msg);
						editTableElement(row, col + 1);
						return false;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			DbPlugin.getDefault().showErrorDialog(e);
		}
		return true;
	}

	public boolean updateDataBase(TableElement element) throws Exception {
		try {
			PasteRecordMonitor.isPasting();

			// TimeWatcher tw = new TimeWatcher();
			// tw.start();
			Display display = Display.getDefault();
			element.setUpdatedDataBase(false);
			if (element.isNew()) {
				if (validateAll()) {
					if (hasSameRecord(element)) {
						return false;
					} else {
						display.syncExec(new RecordUpdateThread(editor, element));
					}
					columnsPack();
				} else {
					return false;
				}

			} else if (element.isModify()) {
				if (validateAll()) {
					display.syncExec(new RecordUpdateThread(editor, element));
				} else {
					return false;
				}
			} else {
				;
			}
			viewer.getTable().select(getSelectedRow());
			viewer.getControl().notifyListeners(SWT.Selection, null);

			// tw.stop();

			element.setUpdatedDataBase(true);

			return true;

		} catch (ZeroUpdateException e) {
			return false;
		} catch (UpdateException e) {
			return false;
		}

	}

	public boolean hasSameRecord(TableElement element) throws Exception {
		Connection con = TransactionForTableEditor.getInstance(config).getConnection();
		TableElement registedElem = TableElementSearcher.findElement(con, element, true);
		return registedElem != null ? true : false;
	}

	public void createNewRecord() {

		String nullSymbol = DbPlugin.getDefault().getPreferenceStore().getString(PreferencePage.P_NULL_SYMBOL);

		TableElement elem = getHeaderTableElement();
		ITable tbl = elem.getTable();
		int count = table.getItems().length;

		Object[] items = new Object[elem.getColumns().length];
		for (int i = 0; i < items.length; i++) {
			zigen.plugin.db.core.TableColumn column = elem.getColumns()[i];
			items[i] = InsertRecordAction.getDefaultValue(column);
		}

		TableElement newElement = new TableNewElement(tbl, count + 1, elem.getColumns(), items, elem.getUniqueColumns());

		TableViewerManager.insert(viewer, newElement);

		editTableElement(count, 1);

	}

	public void removeRecord(TableElement element) {
		TableViewerManager.remove(viewer, element);
	}

	public void dispose() {
	}


}
