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
import zigen.plugin.db.ui.internal.Schema;

public class CopySchemaNameAction extends Action implements Runnable {

	// private final String LINE_SEP = System.getProperty("line.separator");

	StructuredViewer viewer = null;

	public CopySchemaNameAction(StructuredViewer viewer) {
		this.viewer = viewer;

		this.setText(Messages.getString("CopySchemaNameAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("CopySchemaNameAction.1")); //$NON-NLS-1$

	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		try {

			StringBuffer sb = new StringBuffer();
			Clipboard clipboard = ClipboardUtils.getInstance();

			int index = 0;
			for (Iterator iter = selection.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof Schema) {
					Schema schema = (Schema) obj;
					if (index == 0) {
						sb.append(schema.getName());
					} else {
						sb.append(", " + schema.getName()); //$NON-NLS-1$
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
