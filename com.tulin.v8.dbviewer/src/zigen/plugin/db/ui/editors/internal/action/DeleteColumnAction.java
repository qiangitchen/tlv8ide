/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal.action;

import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISharedImages;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.actions.TableViewEditorAction;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.internal.thread.AbstractSQLThread;
import zigen.plugin.db.ui.editors.internal.thread.DropColumnThread;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.ITable;

public class DeleteColumnAction extends TableViewEditorAction {

	public DeleteColumnAction() {
		this.setText(Messages.getString("DeleteColumnAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("DeleteColumnAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(ISharedImages.IMG_TOOL_DELETE);
	}

	public void run() {

		try {
			if (editor instanceof TableViewEditorFor31) {
				TableViewEditorFor31 tEditor = (TableViewEditorFor31) editor;

				IStructuredSelection selection = (IStructuredSelection) tEditor.getTableDefineEditor().getDefineViewer().getSelection();

				Object obj = selection.getFirstElement();
				if (obj instanceof Column) {
					Column col = (Column) obj;

					IDBConfig config = tEditor.getDBConfig();
					ITable table = tEditor.getTableNode();
					ISQLCreatorFactory factory = AbstractSQLCreatorFactory.getFactory(config, table);

					AbstractSQLThread thread;

					String msg = Messages.getString("DeleteColumnAction.2"); //$NON-NLS-1$
					if (factory.supportsDropColumnCascadeConstraints()) {
						String opt = Messages.getString("DeleteColumnAction.3"); //$NON-NLS-1$
						MessageDialogWithToggle dialog = DbPlugin.getDefault().confirmDialogWithToggle(msg, opt, true);
						final int YES = 2;
						if (dialog.getReturnCode() == YES) {
							thread = new DropColumnThread(table, col, dialog.getToggleState());
							thread.run();
						}
					} else {
						if (DbPlugin.getDefault().confirmDialog(msg)) {
							thread = new DropColumnThread(table, col, false);
							thread.run();
						}
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
