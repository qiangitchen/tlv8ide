/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import org.eclipse.jface.text.IDocument;


public class ExecuteSQLForEditorAction extends AbstractExecuteSQLForEditorAction {

	public ExecuteSQLForEditorAction(SqlEditor2 editor) {
		super(editor);
	}

	protected String targetSql(IDocument doc) {
		return doc.get();
	}
}
