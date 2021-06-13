/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.internal.ColumnFilterInfo;

public class CopyRecordDataAction extends TableViewEditorAction {

	protected final String LINE_SEP = System.getProperty("line.separator"); //$NON-NLS-1$

	protected final String DEMILITER = "\t"; //$NON-NLS-1$

	protected IStructuredSelection selection;

	public CopyRecordDataAction() {
		setEnabled(false);
		setImage(ITextOperationTarget.COPY);
	}

	protected boolean isVisibled(int columnIndex) {
		if (editor instanceof TableViewEditorFor31) {
			TableViewEditorFor31 editor31 = (TableViewEditorFor31) editor;
			ColumnFilterInfo[] filters = editor31.getFilterInfos();
			return filters[columnIndex].isChecked();

		} else {
			return true;
		}

	}

	public void run() {

		try {

			StringBuffer sb = new StringBuffer();
			Clipboard clipboard = ClipboardUtils.getInstance();

			IStructuredSelection selection = (IStructuredSelection) editor.getViewer().getSelection();
			Iterator iter = selection.iterator();

			int index = 0;
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof TableElement) {
					TableElement elem = (TableElement) obj;
					TableColumn[] columns = elem.getColumns();

					if (index == 0) {
						createHeader(sb, columns);
					}

					boolean isFirst = true;
					for (int i = 0; i < columns.length; i++) {
						if (isVisibled(i)) {
							TableColumn col = columns[i];
							int type = col.getDataType();
							// String value = String.valueOf(elem.getItem(col));
							String value = String.valueOf(elem.getItem(i));


							if (value.indexOf("\"") >= 0) { //$NON-NLS-1$
								value = value.replaceAll("\"", "\"\"");
								// ""
								// //$NON-NLS-1$
								// //$NON-NLS-2$
							}
							if (value.length() == 0) {
								value = "\"\"";
							} else {
								value = value.replaceAll("^|$", "\"");
								// //$NON-NLS-1$
								// //$NON-NLS-2$
							}

							if (isFirst) {
								sb.append(value);
								isFirst = false;
							} else {
								sb.append(DEMILITER + value);
							}
						}

					}
					sb.append(LINE_SEP);
					index++;

				}
			}
			clipboard.setContents(new Object[] {sb.toString()}, new Transfer[] {TextTransfer.getInstance()});

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		} finally {
			editor.getViewer().getControl().notifyListeners(SWT.Selection, null);
		}

	}

	protected void createHeader(StringBuffer sb, TableColumn[] columns) {
		boolean isFirst = true;
		for (int i = 0; i < columns.length; i++) {
			if (isVisibled(i)) {
				TableColumn col = columns[i];
				if (isFirst) {
					sb.append(col.getColumnName());
					isFirst = false;
				} else {
					sb.append(DEMILITER + col.getColumnName());
				}
			}

		}
		sb.append(LINE_SEP);
	}

	public void refresh() {
		if (editor == null) {
			setEnabled(false);
		} else if (editor.getViewer() == null) {
			setEnabled(false);
		} else {
			selection = (IStructuredSelection) editor.getViewer().getSelection();
			if (selection.size() > 0) {
				setEnabled(true);
			} else {
				setEnabled(false);
			}
		}
	}

	public void selectionChanged(ISelection selection) {
		IStructuredSelection _selection = (IStructuredSelection) editor.getViewer().getSelection();
		if (_selection != null && _selection.size() > 0) {
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}


}
