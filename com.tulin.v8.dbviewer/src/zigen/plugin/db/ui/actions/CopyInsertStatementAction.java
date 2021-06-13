/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.rule.DefaultStatementFactory;
import zigen.plugin.db.core.rule.IStatementFactory;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.internal.ITable;

public class CopyInsertStatementAction extends TableViewEditorAction {

	private final String LINE_SEP = System.getProperty("line.separator"); //$NON-NLS-1$

	IStructuredSelection selection;

	public CopyInsertStatementAction() {
		this.setText(Messages.getString("CopyInsertStatementAction.1")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("CopyInsertStatementAction.2")); //$NON-NLS-1$
	}

	public void run() {
		try {
			String demiliter = DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
			StringBuffer sb = new StringBuffer();
			Clipboard clipboard = ClipboardUtils.getInstance();
			TextTransfer text_transfer = TextTransfer.getInstance();
			ITable table = editor.getTableNode();

			Iterator iter = selection.iterator();
			int index = 0;
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof TableElement) {
					TableElement elem = (TableElement) obj;
					TableColumn[] columns = elem.getColumns();

					sb.append("INSERT INTO "); //$NON-NLS-1$
					sb.append(table.getSqlTableName());
					sb.append(" ("); //$NON-NLS-1$

					for (int i = 0; i < columns.length; i++) {
						TableColumn col = columns[i];

						if (i == 0) {
							sb.append(" "); //$NON-NLS-1$
						} else {

							sb.append(", "); //$NON-NLS-1$
						}
						sb.append(col.getColumnName());

					}
					sb.append(" )"); //$NON-NLS-1$
					sb.append(" VALUES ("); //$NON-NLS-1$

					for (int i = 0; i < columns.length; i++) {
						TableColumn col = columns[i];
						int type = col.getDataType();
						Object value = elem.getItem(col);

						IStatementFactory factory = DefaultStatementFactory.getFactory(table.getDbConfig());
						if (i == 0) {
							sb.append(factory.getString(type, value));
						} else {
							sb.append("," + factory.getString(type, value)); //$NON-NLS-1$
						}

					}

					sb.append(")");
					if ("/".equals(demiliter)) { //$NON-NLS-1$
						sb.append(DbPluginConstant.LINE_SEP);
					}
					sb.append(demiliter);
					sb.append(DbPluginConstant.LINE_SEP);


					index++;

				}
			}
			clipboard.setContents(new Object[] {sb.toString()}, new Transfer[] {text_transfer});

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}

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

}
