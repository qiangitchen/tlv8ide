/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

import zigen.plugin.db.csv.CreateCSVForQueryAction;
import zigen.plugin.db.ui.actions.CopyRecordDataAction;
import zigen.plugin.db.ui.actions.SelectAllRecordAction;

public class QueryViewerContributor extends MultiPageEditorActionBarContributor {

	private SelectAllRecordAction selectAllAction;

	private CopyRecordDataAction copyRecordDataAction;

	private CreateCSVForQueryAction createCSVForQueryAction;

	public QueryViewerContributor() {
		copyRecordDataAction = new CopyRecordDataAction();
		createCSVForQueryAction = new CreateCSVForQueryAction();
		selectAllAction = new SelectAllRecordAction();
	}

	public void init(IActionBars bars, IWorkbenchPage page) {
		super.init(bars, page);
	}

	public void fillContextMenu(IMenuManager manager) {
		reflesh();

		manager.add(copyRecordDataAction);
		manager.add(selectAllAction);
		// manager.add(new Separator());
		manager.add(createCSVForQueryAction);

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public void contributeToToolBar(IToolBarManager toolBarManager) {
	}

	public void setActivePage(IEditorPart target) {
		makeActions(target);
	}

	public void setActiveEditor(IEditorPart target) {
		super.setActiveEditor(target);
		makeActions(target);
	}

	private void makeActions(IEditorPart target) {
		if (target instanceof QueryViewEditor2) {
			QueryViewEditor2 editor = (QueryViewEditor2) target;
			selectAllAction.setActiveEditor(editor);
			copyRecordDataAction.setActiveEditor(editor);
			createCSVForQueryAction.setActiveEditor(editor);
		}
	}

	void reflesh() {
		copyRecordDataAction.refresh();
	}

	public void dispose() {
		super.dispose();
		copyRecordDataAction.setActiveEditor(null);
		createCSVForQueryAction.setActiveEditor(null);

	}
}
