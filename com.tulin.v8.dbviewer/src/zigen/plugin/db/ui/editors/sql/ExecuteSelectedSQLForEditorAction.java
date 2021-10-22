/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;


public class ExecuteSelectedSQLForEditorAction extends AbstractExecuteSQLForEditorAction {

	public ExecuteSelectedSQLForEditorAction(SqlEditor2 editor) {
		super(editor);
	}

	protected String targetSql(IDocument doc) {
		TextSelection selection = (TextSelection) editor.getSelectionProvider().getSelection();
		if (selection != null) {
			return selection.getText();
		} else {
			return null;
		}
	}

}
