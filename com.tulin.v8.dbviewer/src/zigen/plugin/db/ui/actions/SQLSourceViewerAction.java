/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.action.Action;

import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public class SQLSourceViewerAction extends Action {

	protected SQLSourceViewer fSQLSourceViewer;

	public SQLSourceViewerAction(SQLSourceViewer SourceViewer) {
		this.fSQLSourceViewer = SourceViewer;
	}

	public SQLSourceViewerAction(SQLSourceViewer SourceViewer, String text, int style) {
		super(text, style);
		this.fSQLSourceViewer = SourceViewer;
	}

	public void setSQLSourceViewer(SQLSourceViewer viewer) {
		this.fSQLSourceViewer = viewer;
	}
}
