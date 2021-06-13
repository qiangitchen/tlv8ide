/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.ui.editors.sql.ExecuteSQLForEditorAction;
import zigen.plugin.db.ui.editors.sql.SqlEditor2;

public class SQLToolBarForSqlEditor extends SQLToolBar {

	protected IEditorPart fEditor;

	protected DisplayResultAction displayResultAction;

	protected ExecuteSQLForEditorAction executeSQLForEditorAction;

	protected ToolBarContributionItem getToolBarContributionItem1(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		toolBarMgr.add(new SaveAction());
		return new ToolBarContributionItem(toolBarMgr);
	}

	protected ToolBarContributionItem getToolBarContributionItem2(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		if (fEditor instanceof SqlEditor2) {
			executeSQLForEditorAction = new ExecuteSQLForEditorAction((SqlEditor2) fEditor);
			toolBarMgr.add(executeSQLForEditorAction);
		} else {
			toolBarMgr.add(executeSQLForEditorAction);
		}
		toolBarMgr.add(allClearAction);
		return new ToolBarContributionItem(toolBarMgr);

	}

	protected ToolBarContributionItem getToolBarContributionItem3(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		// toolBarMgr.add(backSqlAction);
		// toolBarMgr.add(nextSqlAction);
		return new ToolBarContributionItem(toolBarMgr);
	}

	protected ToolBarContributionItem getToolBarContributionItem4(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		toolBarMgr.add(commitModeAction);
		toolBarMgr.add(commitAction);
		toolBarMgr.add(rollbackAction);
		return new ToolBarContributionItem(toolBarMgr);
	}

	protected ToolBarContributionItem getToolBarContributionItem5(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		toolBarMgr.add(formatModeAction);
		return new ToolBarContributionItem(toolBarMgr);
	}

	protected ToolBarContributionItem getToolBarContributionItem6(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		comboContributionItem.setSaveLastDb(false);
		toolBarMgr.add(comboContributionItem); //$NON-NLS-1$
		toolBarMgr.add(new Separator());
		displayResultAction = new DisplayResultAction();
		toolBarMgr.add(displayResultAction);
		return new ToolBarContributionItem(toolBarMgr);
	}

	public SQLToolBarForSqlEditor(IEditorPart editor) {
		super();
		this.fEditor = editor;
	}

	public void setSQLSourceViewer(SQLSourceViewer sqlSourceViewer) {
		super.setSQLSourceViewer(sqlSourceViewer);
	}

	protected class SaveAction extends Action {

		public SaveAction() {
			this.setToolTipText(Messages.getString("SQLToolBarForSqlEditor.0")); //$NON-NLS-1$
			this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_SAVE));
		}

		public void run() {
			fEditor.doSave(null);
		}

	}


	public void setDisplayResultChecked(boolean checked) {
		displayResultAction.setChecked(checked);
	}


	class DisplayResultAction extends Action {

		public DisplayResultAction() {
			super(Messages.getString("SQLToolBarForSqlEditor.1"), IAction.AS_CHECK_BOX); //$NON-NLS-1$
			setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_RESULT));
		}

		public void run() {
		// if (fEditor instanceof SqlEditor2) {
		// ((SqlEditor2) fEditor).setResultVisible(!isChecked());
		// }
		}

		public void setChecked(boolean checked) {
			super.setChecked(checked);
			if (fEditor instanceof SqlEditor2) {
				((SqlEditor2) fEditor).setResultVisible(isChecked());
			}
		}

	}
}
