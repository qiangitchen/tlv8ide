/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.SQLUtil;
import zigen.plugin.db.ui.views.SQLExecuteView;

public class SQLToUpperCaseAction implements IViewActionDelegate {

	private static final String LINE_SEP = System.getProperty("line.separator");

	private ISelection selection;

	private IViewPart viewPart;

	public void init(IViewPart view) {
		this.viewPart = view;
	}

	public void run(IAction action) {
		if (viewPart instanceof SQLExecuteView) {
			SQLExecuteView view = (SQLExecuteView) viewPart;
			IDocument doc = view.getSqlViewer().getDocument();
			String sql = doc.get();
			if (selection instanceof ITextSelection) {
				try {
					ITextSelection textSelection = (ITextSelection) selection;
					String text = textSelection.getText();

					if (text == null || text.length() == 0) {
						view.setSqlText(SQLUtil.toUpperCase(sql));
					} else {
						int offset = textSelection.getOffset();
						int length = textSelection.getLength();

						doc.replace(offset, length, SQLUtil.toUpperCase(text));

					}
				} catch (Exception e) {
					DbPlugin.getDefault().showErrorDialog(e);
				}
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

}
