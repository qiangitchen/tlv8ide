/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;

import zigen.plugin.db.ui.actions.GlobalAction;
import zigen.plugin.db.ui.views.ISQLOperationTarget;

public class PlsqlEditorContributor extends SqlEditorContributor {


	public PlsqlEditorContributor() {}

	public void init(IActionBars bars, IWorkbenchPage page) {
		super.init(bars, page);
	}

	public void fillContextMenu(IMenuManager manager) {
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.UNDO));
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.REDO));
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.CUT));
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.COPY));
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.PASTE));
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.DELETE));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.LINE_DEL));
		manager.add(new GlobalAction(sqlViewer, ITextOperationTarget.SELECT_ALL));
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.ALL_EXECUTE));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.CURRENT_EXECUTE));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.SELECTED_EXECUTE));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.SCRIPT_EXECUTE));
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.FORMAT));
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.UNFORMAT));
		manager.add(new Separator());
		manager.add(new GlobalAction(sqlViewer, ISQLOperationTarget.COMMENT));
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public void contributeToToolBar(IToolBarManager manager) {
		super.contributeToToolBar(manager);
	}

	public void contributeToMenu(IMenuManager menu) {}

	public void setActivePage(IEditorPart target) {
		if (target != null && target instanceof PlsqlEditor) {
			sqlViewer = ((PlsqlEditor) target).sqlViewer;

		}
	}

	public void setActiveEditor(IEditorPart target) {
		super.setActiveEditor(target);
		if (target instanceof PlsqlEditor) {
			sqlViewer = ((PlsqlEditor) target).sqlViewer;
		}
	}

}
