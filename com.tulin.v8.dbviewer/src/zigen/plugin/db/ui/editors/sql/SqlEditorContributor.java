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
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.editors.text.TextEditorActionContributor;

import zigen.plugin.db.csv.CreateCSVForQueryAction;
import zigen.plugin.db.ui.actions.CopyRecordDataAction;
import zigen.plugin.db.ui.actions.GlobalAction;
import zigen.plugin.db.ui.actions.SelectAllRecordAction;
import zigen.plugin.db.ui.editors.IQueryViewEditor;
import zigen.plugin.db.ui.editors.ITableViewEditor;
import zigen.plugin.db.ui.views.ISQLOperationTarget;

public class SqlEditorContributor extends TextEditorActionContributor {

	protected SourceViewer sqlViewer;

	protected SelectAllRecordAction selectAllRecordAction;

	protected CopyRecordDataAction copyAction;

	protected CreateCSVForQueryAction createCSVForQueryAction;

	public SqlEditorContributor() {
		selectAllRecordAction = new SelectAllRecordAction();
		copyAction = new CopyRecordDataAction();
		createCSVForQueryAction = new CreateCSVForQueryAction();
	}

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

	public void fillContextMenuForResultView(IMenuManager manager) {
		reflesh();
		manager.add(copyAction);
		manager.add(selectAllRecordAction);
		manager.add(createCSVForQueryAction);
		// manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public void contributeToToolBar(IToolBarManager manager) {
		super.contributeToToolBar(manager);

	}

	public void setActivePage(IEditorPart target) {
		if (target != null && target instanceof SqlEditor) {
			sqlViewer = ((SqlEditor) target).sqlViewer;
		}

	}

	public void setActiveEditor(IEditorPart target) {
		super.setActiveEditor(target);
		if (target instanceof SqlEditor) {
			SqlEditor editor = (SqlEditor) target;
			sqlViewer = editor.sqlViewer;
		}

		if (target != null && target instanceof ITableViewEditor) {
			copyAction.setActiveEditor((ITableViewEditor) target);
			selectAllRecordAction.setActiveEditor((ITableViewEditor) target);
		}
		if (target != null && target instanceof IQueryViewEditor) {
			createCSVForQueryAction.setActiveEditor((IQueryViewEditor) target);
		}

	}

	void reflesh() {
		copyAction.refresh();
	}

	public void dispose() {
		super.dispose();
	}

}
