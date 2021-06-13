/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorPart;

public class SQLToolBarForPlsqlEditor extends SQLToolBarForSqlEditor {

	protected ToolBarContributionItem getToolBarContributionItem1(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		toolBarMgr.add(new SaveAction());
		return new ToolBarContributionItem(toolBarMgr);
	}

	protected ToolBarContributionItem getToolBarContributionItem2(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		// toolBarMgr.add(allExecAction);
		toolBarMgr.add(scriptExecAction);
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
		// toolBarMgr.add(commitModeAction);
		// toolBarMgr.add(commitAction);
		// toolBarMgr.add(rollbackAction);
		return new ToolBarContributionItem(toolBarMgr);
	}

	protected ToolBarContributionItem getToolBarContributionItem5(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		// toolBarMgr.add(formatModeAction);
		return new ToolBarContributionItem(toolBarMgr);
	}


	protected ToolBarContributionItem getToolBarContributionItem6(CoolBarManager coolBarMgr) {
		ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
		comboContributionItem.setSaveLastDb(false);
		toolBarMgr.add(comboContributionItem); //$NON-NLS-1$
		// toolBarMgr.add(lockDataBaseAction);
		return new ToolBarContributionItem(toolBarMgr);
	}

	public SQLToolBarForPlsqlEditor(IEditorPart editor) {
		super(editor);
	}

	public void setSQLSourceViewer(SQLSourceViewer sqlSourceViewer) {
		super.setSQLSourceViewer(sqlSourceViewer);
	}
}
