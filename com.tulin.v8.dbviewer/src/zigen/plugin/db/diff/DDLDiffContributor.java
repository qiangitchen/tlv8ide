/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.diff;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

public class DDLDiffContributor extends MultiPageEditorActionBarContributor {

	private CopyNodeNameAction copyNodeNameAction;


	public DDLDiffContributor() {
		copyNodeNameAction = new CopyNodeNameAction();
	}

	public void init(IActionBars bars, IWorkbenchPage page) {
		super.init(bars, page);
	}

	public void fillContextMenu(IMenuManager manager) {
		copyNodeNameAction.refresh();
		manager.add(copyNodeNameAction);
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
		if (target instanceof DDLDiffEditor) {
			DDLDiffEditor editor = (DDLDiffEditor) target;
			copyNodeNameAction.setActiveEditor(editor);
		}
	}

	public void dispose() {
		super.dispose();
		copyNodeNameAction.setActiveEditor(null);

	}
}
