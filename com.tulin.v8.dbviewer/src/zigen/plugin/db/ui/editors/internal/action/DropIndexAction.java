/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.action;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISharedImages;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.actions.TableViewEditorAction;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.internal.thread.AbstractSQLThread;
import zigen.plugin.db.ui.editors.internal.thread.DropIndexThread;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Index;

public class DropIndexAction extends TableViewEditorAction {

	public DropIndexAction() {
		this.setText(Messages.getString("DropIndexAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("DropIndexAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(ISharedImages.IMG_TOOL_DELETE);
	}

	public void run() {

		try {
			if (editor instanceof TableViewEditorFor31) {
				TableViewEditorFor31 tEditor = (TableViewEditorFor31) editor;

				IStructuredSelection selection = (IStructuredSelection) tEditor.getTableDefineEditor().getConstraintViewer().getSelection();

				Object obj = selection.getFirstElement();
				if (obj instanceof Index) {
					Index index = (Index) obj;
					ITable table = tEditor.getTableNode();
					AbstractSQLThread invoker;
					if (DbPlugin.getDefault().confirmDialog(Messages.getString("DropIndexAction.2"))) { //$NON-NLS-1$
						invoker = new DropIndexThread(table, index.getName());
						invoker.run();
					}

				} else {
					;
				}
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}

	}

}
