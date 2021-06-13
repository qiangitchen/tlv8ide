/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.ui.internal.ITable;

public class CopyTableNameWithRemarksAction extends Action implements Runnable {

	StructuredViewer viewer = null;

	public CopyTableNameWithRemarksAction(StructuredViewer viewer) {
		this.viewer = viewer;

		this.setText(Messages.getString("CopyTableNameWithRemarksAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("CopyTableNameWithRemarksAction.1")); //$NON-NLS-1$

	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		try {

			StringBuffer sb = new StringBuffer();
			Clipboard clipboard = ClipboardUtils.getInstance();

			int index = 0;
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof ITable) {
					ITable table = (ITable) obj;
					if (index == 0) {
						sb.append(table.getName());
					} else {
						sb.append(", " + table.getName()); //$NON-NLS-1$
					}

					String remarks = table.getRemarks();
					if (remarks != null && !"".equals(remarks.trim())) { //$NON-NLS-1$
						sb.append("("); //$NON-NLS-1$
						sb.append(remarks);
						sb.append(")"); //$NON-NLS-1$
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
