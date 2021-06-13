/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public class SQLSourceViewer2 extends SQLSourceViewer {

	SqlEditor2 editor;

	public SQLSourceViewer2(Composite parent, IVerticalRuler ruler, IOverviewRuler overviewRuler, boolean showsAnnotationOverview, int styles) {
		super(parent, ruler, overviewRuler, showsAnnotationOverview, styles);
	}

	public void setSqlEditor(SqlEditor2 editor) {
		this.editor = editor;
	}

	protected void doAllSQLExecute() {
		if (isFormatPreExecute)
			doFormat();

		Display display = Display.getDefault();
		display.syncExec((Runnable) new ExecuteSQLForEditorAction(editor));

	}

	protected void doCurrentSQLExecute() {

		if (isFormatPreExecute)
			doFormat();

		Display display = Display.getDefault();
		display.syncExec((Runnable) new ExecuteCurrentSQLForEditorAction(editor));

	}

	protected void doSelectedSQLExecute() {
		if (isFormatPreExecute)
			doFormat();

		Display display = Display.getDefault();
		display.syncExec(new ExecuteSelectedSQLForEditorAction(editor));

	}


}
