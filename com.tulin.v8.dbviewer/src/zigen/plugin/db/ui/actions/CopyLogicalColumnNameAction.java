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

public class CopyLogicalColumnNameAction extends AbstractCopyAction {

	public void run(IAction action) {
		try {

			StringBuffer sb = new StringBuffer();
			Clipboard clipboard = ClipboardUtils.getInstance();

			int index = 0;
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof Column) {
					Column col = (Column) obj;

					String remarks = col.getRemarks();
					if (remarks == null || "".equals(remarks.trim())) {
						remarks = col.getName();
					}
					if (index == 0) {
						sb.append(remarks);
					} else {
						sb.append(", ");//$NON-NLS-1$
						sb.append(remarks);
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
