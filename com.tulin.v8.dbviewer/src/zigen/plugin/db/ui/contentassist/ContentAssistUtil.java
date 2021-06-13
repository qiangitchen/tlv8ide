/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DBType;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.sql.ISqlEditor;
import zigen.plugin.db.ui.editors.sql.SqlEditor;
import zigen.plugin.db.ui.internal.ContentAssistTable;
import zigen.plugin.db.ui.views.ColumnSearchAction;
import zigen.plugin.db.ui.views.SQLExecuteView;
import zigen.plugin.db.ui.views.internal.SQLWhitespaceDetector;

public class ContentAssistUtil {

	public static boolean isAfterPeriod(IDocument document, int documentOffset) {
		try {
			char c = document.getChar(documentOffset - 1);
			if (c == '.') {
				return true;
			} else {
				return false;
			}
		} catch (BadLocationException e) {
			return false;
		}
	}

	public static String getPreviousWord(IDocument document, int documentOffset) {

		if (isAfterPeriod(document, documentOffset)) {
			--documentOffset;
		}

		SQLWhitespaceDetector whiteSpace = new SQLWhitespaceDetector();

		StringBuffer buf = new StringBuffer();
		while (true) {
			try {
				char c = document.getChar(--documentOffset);
				if (whiteSpace.isWhitespace(c) || c == '.')
					return buf.reverse().toString();
				buf.append(c);

			} catch (BadLocationException e) {
				return buf.reverse().toString();
			}
		}
	}

	public static String getPreviousWordGroup(IDocument document, int documentOffset) {
		if (isAfterPeriod(document, documentOffset)) {
			--documentOffset;
		}

		SQLWhitespaceDetector whiteSpace = new SQLWhitespaceDetector();

		StringBuffer buf = new StringBuffer();
		while (true) {
			try {
				char c = document.getChar(--documentOffset);
				if (whiteSpace.isWhitespace(c))
					return buf.reverse().toString();

				buf.append(c);

			} catch (BadLocationException e) {
				return buf.reverse().toString();
			}
		}
	}

	public static String subString(String modifier, int length) {
		if (modifier == null)
			return null;
		if (modifier.length() <= length) {
			return modifier;
		} else {
			return modifier.substring(0, length);
		}
	}

	public static IDBConfig getIDBConfig() {
		try {
			IWorkbenchPage page = DbPlugin.getDefault().getPage();
			IWorkbenchPart part = page.getActivePart();

			if (part instanceof SqlEditor) {
				SqlEditor editorPart = (SqlEditor) part;
				if (editorPart instanceof ISqlEditor) {
					return ((ISqlEditor) editorPart).getConfig();
				} else {
					return null;
				}


			} else if (part instanceof SQLExecuteView) {
				SQLExecuteView viewPart = (SQLExecuteView) part;
				return viewPart.getConfig();

			} else if (part instanceof TableViewEditorFor31) {
				TableViewEditorFor31 editor = (TableViewEditorFor31) part;
				return editor.getTableNode().getDbConfig();

			} else {
				throw new IllegalStateException("UnExpected Object." + part.getClass().getName()); //$NON-NLS-1$
			}
		} catch (Exception e) {
			DbPlugin.log(e);
		}
		return null;
	}

	public static ContentAssistTable createContentAssistTable(String schemaName, String tableName) {
		Display display = Display.getDefault();
		IDBConfig config = getIDBConfig();

		ContentAssistTable contentTable;
		switch (config.getDbType()) {
		case DBType.DB_TYPE_ORACLE:
			contentTable = new ContentAssistTable(config, schemaName, tableName.toUpperCase());

			display.syncExec((Runnable) new ColumnSearchAction(contentTable));
			break;
		default:
			contentTable = new ContentAssistTable(config, schemaName, tableName);
			display.syncExec((Runnable) new ColumnSearchAction(contentTable));

			if (contentTable.getColumns() == null || contentTable.getColumns().length == 0) {
				contentTable = new ContentAssistTable(config, schemaName, tableName.toUpperCase());
				display.syncExec((Runnable) new ColumnSearchAction(contentTable));

				if (contentTable.getColumns() == null || contentTable.getColumns().length == 0) {
					contentTable = new ContentAssistTable(config, schemaName, tableName.toLowerCase());
					display.syncExec((Runnable) new ColumnSearchAction(contentTable));
				}
			}
			break;
		}

		return contentTable;
	}

}
