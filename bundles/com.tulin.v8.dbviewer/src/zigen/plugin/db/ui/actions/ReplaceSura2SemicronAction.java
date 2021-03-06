/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.ui.views.SQLExecuteView;

public class ReplaceSura2SemicronAction implements IViewActionDelegate {

//	private ISelection selection;

	private IViewPart viewPart;

	public void init(IViewPart view) {
		this.viewPart = view;
	}

	public void run(IAction action) {

		if (viewPart instanceof SQLExecuteView) {
			SQLExecuteView view = (SQLExecuteView) viewPart;
			IDocument doc = view.getSqlViewer().getDocument();
			String sql = doc.get();
			sql = sql.replaceAll(DbPluginConstant.LINE_SEP + "/", ";");
			view.setSqlText(sql);
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
//		this.selection = selection;
	}

}
