/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.ContentOutline;

import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.parser.util.CurrentSql;
import zigen.plugin.db.ui.views.internal.SQLOutinePage;
import zigen.sql.parser.INode;
import zigen.sql.parser.ast.ASTAlias;
import zigen.sql.parser.ast.ASTStatement;

public class ExecuteAction implements IObjectActionDelegate {

	IAction action;

	IStructuredSelection selection;

	StructuredViewer structuredViewer;

	SQLOutinePage page;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

		if (targetPart instanceof ContentOutline) {

			ContentOutline outline = (ContentOutline) targetPart;
			if (outline.getCurrentPage() instanceof SQLOutinePage) {
				this.page = (SQLOutinePage) outline.getCurrentPage();
			}

		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.action = action;
		if (selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection) selection;
			this.action.setEnabled(true);
		} else {
			this.action.setEnabled(false);
			throw new RuntimeException("Required IStructuredSelection."); //$NON-NLS-1$
		}
	}

	public void run(IAction action) {
		Object obj = selection.getFirstElement();
		if (obj instanceof ASTStatement) {
			ASTStatement st = (ASTStatement) obj;

			INode last = getEndNode(st);
			int offset = st.getOffset();
			int length = last.getOffset() + last.getLength() - offset;

			if (last instanceof ASTAlias) {
				ASTAlias as = (ASTAlias) last;
				if (as.hasAlias()) {
					length = as.getAliasOffset() + as.getAliasLength() - offset;
				}
			}


			SqlEditor2 editor = page.getEditor();

			editor.toolBar.setDisplayResultChecked(true);
			CurrentSql cs = page.getCurrentSql();
			Transaction trans = Transaction.getInstance(editor.getDBConfig());
			// IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
			String sql = cs.getSql().substring(offset, offset + length);
			if (sql != null && sql.trim().length() > 0) {
				SqlExecJob2 job = new SqlExecJob2(editor, trans, sql);
				job.setUser(false);
				job.schedule();
			}

		}

	}

	public INode getEndNode(INode node) {
		INode n = node.getLastChild();
		if (n == null) {
			return node;
		} else {
			return getEndNode(n);
		}
	}

}
