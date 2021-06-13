/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class CopyColumnNameWithTableNameAction extends AbstractCopyAction {

	public void run(IAction action) {
		try {

			StringBuffer sb = new StringBuffer();
			Clipboard clipboard = ClipboardUtils.getInstance();

			int index = 0;
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof Column) {
					Column col = (Column) obj;
					ITable table = col.getTable();

					String tableName = table.getName();
					String columnName = col.getName();

					if (index == 0) {
						sb.append(tableName);
						sb.append(".");//$NON-NLS-1$
						sb.append(columnName);
					} else {
						sb.append(", ");//$NON-NLS-1$
						sb.append(tableName);
						sb.append(".");//$NON-NLS-1$
						sb.append(columnName);
					}

					index++;
				}

			}

			clipboard.setContents(new Object[] {sb.toString()}, new Transfer[] {TextTransfer.getInstance()});

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

}
