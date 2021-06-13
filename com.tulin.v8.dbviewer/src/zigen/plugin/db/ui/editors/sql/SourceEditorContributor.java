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
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

import zigen.plugin.db.ui.actions.GlobalAction;

public class SourceEditorContributor extends MultiPageEditorActionBarContributor {

	private SourceEditor editor;

	private SourceViewer viewer;

	public SourceEditorContributor() {
		createActions();
	}

	public void init(IActionBars bars, IWorkbenchPage page) {
		super.init(bars, page);

	}

	private void createActions() {}

	public void fillContextMenu(IMenuManager manager) {
		manager.add(new GlobalAction(viewer, ITextOperationTarget.UNDO));
		manager.add(new GlobalAction(viewer, ITextOperationTarget.REDO));
		manager.add(new Separator());
		manager.add(new GlobalAction(viewer, ITextOperationTarget.CUT));
		manager.add(new GlobalAction(viewer, ITextOperationTarget.COPY));
		manager.add(new GlobalAction(viewer, ITextOperationTarget.PASTE));
		manager.add(new Separator());
		manager.add(new GlobalAction(viewer, ITextOperationTarget.DELETE));
		manager.add(new GlobalAction(viewer, ITextOperationTarget.SELECT_ALL));

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public void contributeToToolBar(IToolBarManager toolBarManager) {}

	public void setActivePage(IEditorPart activeEditor) {}

	public void setActiveEditor(IEditorPart target) {
		super.setActiveEditor(target);
		if (target instanceof SourceEditor) {
			editor = (SourceEditor) target;
			// viewer = editor.sourceViewer;
		}
	}

	public void dispose() {
		super.dispose();
	}

}
