/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.IDocument;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.Transaction;

abstract public class AbstractExecuteSQLForEditorAction extends Action implements Runnable {

	protected SqlEditor2 editor;

	public AbstractExecuteSQLForEditorAction(SqlEditor2 editor) {
		this.editor = editor;
		this.setImageDescriptor(DbPlugin.getDefault().getImageRegistry().getDescriptor(DbPlugin.IMG_CODE_EXECUTE));
	}

	public void run() {
		Transaction trans = Transaction.getInstance(editor.getDBConfig());
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		String sql = targetSql(doc);
		if (sql != null && sql.trim().length() > 0) {
			editor.toolBar.setDisplayResultChecked(true);

			SqlExecJob2 job = new SqlExecJob2(editor, trans, targetSql(doc));
			job.setUser(false);
			job.schedule();
		} else {
			DbPlugin.getDefault().showWarningMessage("Please input SQL.");
		}
	}

	abstract protected String targetSql(IDocument doc);


}
