/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.preference.SQLFormatPreferencePage;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class CopySelectStatementAction implements IViewActionDelegate {

	private ISelection selection = null;

	private IViewPart viewPart;

	boolean isSelectedColumn = false;

	public void init(IViewPart view) {
		this.viewPart = view;

	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	public void run(IAction action) {

		try {
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection) selection;
				StringBuffer sb = new StringBuffer();

				int index = 0;

				String tableName = null;

				for (Iterator iter = ss.iterator(); iter.hasNext();) {
					Object obj = iter.next();

					if (!isSelectedColumn && obj instanceof ITable) {
						// select * from table
						sb.append("SELECT * FROM ");
						// sb.append(((ITable)obj).getSqlTableName());
						sb.append(((ITable) obj).getName());
						copyString(sb.toString());
						return;

					} else if (obj instanceof Column) {
						isSelectedColumn = true;

						Column col = (Column) obj;
						tableName = col.getTable().getName();
						if (index == 0) {
							sb.append("SELECT ");

							// tableName = col.getTable().getSqlTableName();
							sb.append(tableName);
							sb.append(".");
							sb.append(col.getName());


						} else {
							sb.append(", ");
							sb.append(tableName);
							sb.append(".");
							sb.append(col.getName()); //$NON-NLS-1$
						}
						index++;
					}

				}
				if (isSelectedColumn) {
					sb.append(" FROM ").append(tableName);
					copyString(sb.toString());
				}

			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	private void copyString(String sql) throws PartInitException {
		IPreferenceStore ps = DbPlugin.getDefault().getPreferenceStore();
		String demiliter = ps.getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
		boolean onPatch = ps.getBoolean(SQLFormatPreferencePage.P_FORMAT_PATCH);
		int type = ps.getInt(SQLFormatPreferencePage.P_USE_FORMATTER_TYPE);
		int max = ps.getInt(SQLFormatPreferencePage.P_MAX_SQL_COUNT);

		sql = SQLFormatter.format(sql, type, onPatch);

		Clipboard clipboard = ClipboardUtils.getInstance();
		TextTransfer text_transfer = TextTransfer.getInstance();

		clipboard.setContents(new Object[] {sql}, new Transfer[] {text_transfer});

	}
}
