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
import zigen.plugin.db.ui.editors.internal.thread.DropConstraintThread;
import zigen.plugin.db.ui.internal.Constraint;
import zigen.plugin.db.ui.internal.ITable;

public class DropConstraintAction extends TableViewEditorAction {

	public DropConstraintAction() {
		this.setText(Messages.getString("DropConstraintAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("DropConstraintAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(ISharedImages.IMG_TOOL_DELETE);
	}

	public void run() {

		try {
			if (editor instanceof TableViewEditorFor31) {
				TableViewEditorFor31 tEditor = (TableViewEditorFor31) editor;

				IStructuredSelection selection = (IStructuredSelection) tEditor.getTableDefineEditor().getConstraintViewer().getSelection();

				Object obj = selection.getFirstElement();
				if (obj instanceof Constraint) {
					Constraint constraint = (Constraint) obj;
					ITable table = tEditor.getTableNode();
					AbstractSQLThread invoker;
					if (DbPlugin.getDefault().confirmDialog(Messages.getString("DropConstraintAction.2"))) { //$NON-NLS-1$
						invoker = new DropConstraintThread(table, constraint);
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
