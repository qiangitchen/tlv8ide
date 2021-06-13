/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views.internal;

import java.io.File;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.ui.PartInitException;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.IStatusChangeListener;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLHistory;
import zigen.plugin.db.core.SQLHistoryManager;
import zigen.plugin.db.core.SQLTokenizer;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.actions.ExecuteCurrentSQLAction;
import zigen.plugin.db.ui.actions.ExecuteSQLAction;
import zigen.plugin.db.ui.actions.ExecuteScriptAction;
import zigen.plugin.db.ui.actions.ExecuteSelectedSQLAction;
import zigen.plugin.db.ui.jobs.SqlFormatJob;
import zigen.plugin.db.ui.views.ISQLOperationTarget;
import zigen.plugin.db.ui.views.SQLExecuteView;

public class SQLSourceViewer extends ProjectionViewer implements ISQLOperationTarget {

	SQLHistoryManager mgr = DbPlugin.getDefault().getHistoryManager();

	protected IDBConfig config;

	protected String secondaryId;

	protected IPreferenceStore preferenceStore;

	protected String sqlFileName;

	protected boolean isFormatPreExecute;

	// protected boolean isLockedDataBase = false;

	public SQLSourceViewer(Composite parent, IVerticalRuler ruler, IOverviewRuler overviewRuler, boolean showsAnnotationOverview, int styles) {
		// super(parent, ruler, styles);
		super(parent, ruler, overviewRuler, showsAnnotationOverview, styles);
		this.preferenceStore = DbPlugin.getDefault().getPreferenceStore();
		super.appendVerifyKeyListener(new VerifyKeyAdapter());


	}

	protected SQLExecuteView getSQLExecuteView() {
		try {
			return (SQLExecuteView) DbPlugin.findView(DbPluginConstant.VIEW_ID_SQLExecute, secondaryId);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean canDoOperation(int operation) {
		if (getTextWidget() == null || (!redraws() && operation != ISQLOperationTarget.FORMAT))
			return false;

		switch (operation) {
		case ISQLOperationTarget.ALL_EXECUTE:
		case ISQLOperationTarget.CURRENT_EXECUTE:
		case ISQLOperationTarget.SELECTED_EXECUTE:
		case ISQLOperationTarget.SCRIPT_EXECUTE:

		case ISQLOperationTarget.NEXT_SQL:
		case ISQLOperationTarget.BACK_SQL:

		case ISQLOperationTarget.FORMAT:
		case ISQLOperationTarget.UNFORMAT:

		case ISQLOperationTarget.LINE_DEL:
		case ISQLOperationTarget.COMMENT:

		case ISQLOperationTarget.COMMIT:
		case ISQLOperationTarget.ROLLBACK:
		case ISQLOperationTarget.ALL_CLEAR:

			return isEditable();

		default:
			return super.canDoOperation(operation);
		}

	}

	public void doOperation(int operation) {
		if (getTextWidget() == null || (!redraws() && operation != ISQLOperationTarget.FORMAT))
			return;

		switch (operation) {
		case ISQLOperationTarget.FORMAT:
			doFormat();

			return;
		case ISQLOperationTarget.UNFORMAT:
			doUnFormat();
			return;

		case ISQLOperationTarget.ALL_EXECUTE:
			doAllSQLExecute();
			return;
		case ISQLOperationTarget.CURRENT_EXECUTE:
			doCurrentSQLExecute();
			return;
		case ISQLOperationTarget.SELECTED_EXECUTE:
			doSelectedSQLExecute();
			return;
		case ISQLOperationTarget.SCRIPT_EXECUTE:
			doScriptExecute();
			return;

		case ISQLOperationTarget.NEXT_SQL:
			doNextSQL();
			return;
		case ISQLOperationTarget.BACK_SQL:
			doBackSQL();
			return;
		case ISQLOperationTarget.LINE_DEL:
			doLineDelete();
			return;
		case ISQLOperationTarget.COMMENT:
			doComment();
			return;

		case ISQLOperationTarget.COMMIT:
			doCommit();
			return;
		case ISQLOperationTarget.ROLLBACK:
			doRollback();
			return;

		case ISQLOperationTarget.ALL_CLEAR:
			doAllClear();
			return;

		default:
			super.doOperation(operation);
		}

	}

	protected void doAllSQLExecute() {
		SQLExecuteView view = getSQLExecuteView();
		if (view != null && isFormatPreExecute) {
			setEditable(false);
			SqlFormatJob job = new SqlFormatJob(this, secondaryId);
			job.setPriority(SqlFormatJob.SHORT);
			job.setUser(false);
			// job.setUser(true);
			job.schedule();

		}

		int offset = getTextWidget().getCaretOffset();
		Display display = Display.getDefault();
		display.syncExec((Runnable) new ExecuteSQLAction(config, this, secondaryId));

	}

	protected void doCurrentSQLExecute() {
		SQLExecuteView view = getSQLExecuteView();
		if (view != null && isFormatPreExecute) {
			setEditable(false);
			SqlFormatJob job = new SqlFormatJob(this, secondaryId);
			job.setPriority(SqlFormatJob.SHORT);
			job.setUser(false);
			// job.setUser(true);
			job.schedule();

		}


		Display display = Display.getDefault();
		display.syncExec((Runnable) new ExecuteCurrentSQLAction(config, this, secondaryId));

	}

	protected void doSelectedSQLExecute() {
		TextSelection selection = (TextSelection) getSelection();

		SQLExecuteView view = getSQLExecuteView();
		if (view != null && isFormatPreExecute) {
			setEditable(false);
			SqlFormatJob job = new SqlFormatJob(this, secondaryId);
			job.setPriority(SqlFormatJob.SHORT);
			job.setUser(false);
			// job.setUser(true);
			job.schedule();

		}

		Display display = Display.getDefault();
		ExecuteSelectedSQLAction action = new ExecuteSelectedSQLAction(config, this, secondaryId, selection);
		display.syncExec((Runnable) action);

	}

	protected void doScriptExecute() {
		SQLExecuteView view = getSQLExecuteView();
		if (view != null && isFormatPreExecute) {
			doFormat();
		}

		IDocument doc = getDocument();
		Display display = Display.getDefault();
		display.syncExec((Runnable) new ExecuteScriptAction(config, doc, secondaryId));

	}

	protected void doUnFormat() {
		try {
			// Point selectedRange = getSelectedRange();
			// IDocument doc = getDocument();
			// String text = getSelectedText();
			// String newtext = SQLFormatter.unformat(text);
			// doc.replace(selectedRange.x, selectedRange.y, newtext);
			// setSelectedRange(selectedRange.x, newtext.length());

			String preSql = getDocument().get();
			String formatSql = getUnFormatSQL(preSql);
			getDocument().replace(0, preSql.length(), formatSql);
			setSelectedRange(formatSql.length(), 0);

		} catch (MalformedTreeException e) {
			DbPlugin.log(e);
		} catch (BadLocationException e) {
			DbPlugin.log(e);
		}

	}

	protected void doFormat() {
		setEditable(false);
		SqlFormatJob job = new SqlFormatJob(this, secondaryId);
		job.setPriority(SqlFormatJob.SHORT);
		job.setUser(false);
		// job.setUser(true);
		job.schedule();

	}

	private String getUnFormatSQL(String preSql) {
		String demiliter = preferenceStore.getString(SQLEditorPreferencePage.P_SQL_DEMILITER);

		StringBuffer sb = new StringBuffer();
		SQLTokenizer st = new SQLTokenizer(preSql, demiliter);
		while (st.hasMoreElements()) {
			String sql = (String) st.nextElement();
			if (sql != null && sql.length() > 0) {
				sb.append(SQLFormatter.unformat(sql));
				if ("/".equals(demiliter)) { //$NON-NLS-1$
					sb.append(DbPluginConstant.LINE_SEP);
				}
				sb.append(demiliter);
				sb.append(DbPluginConstant.LINE_SEP);
			}
		}
		return sb.toString();
	}

	protected void doLineDelete() {
		try {
			IDocument doc = getDocument();
			int offset = getTextWidget().getCaretOffset();

			// supported multiple line delete
			TextSelection tSelection = (TextSelection) getSelection();
			int startLine = tSelection.getStartLine();
			int endLine = tSelection.getEndLine();
			int startOffset = doc.getLineInformation(startLine).getOffset();
			int endOffset = doc.getLineInformation(endLine).getOffset() + doc.getLineInformation(endLine).getLength();
			String demiliter = doc.getLineDelimiter(endLine);
			int length = endOffset - startOffset;
			if (demiliter != null) {
				doc.replace(startOffset, length + demiliter.length(), ""); //$NON-NLS-1$
			} else {
				doc.replace(startOffset, length, ""); //$NON-NLS-1$
			}

			Position pos = new Position(offset);
			doc.removePosition(pos);
		} catch (MalformedTreeException e) {
			DbPlugin.log(e);
		} catch (BadLocationException e) {
			DbPlugin.log(e);
		}

	}

	protected void doComment() {
		try {
			IDocument doc = getDocument();
			int offset = getTextWidget().getCaretOffset();

			String select = getTextWidget().getSelectionText();

			if (select == null || "".equals(select)) { //$NON-NLS-1$
				IRegion region = doc.getLineInformationOfOffset(offset);

				String lineStr = doc.get(region.getOffset(), region.getLength());
				String wk = lineStr.trim();
				if (wk.startsWith("/*") && wk.endsWith("*/")) { //$NON-NLS-1$ //$NON-NLS-2$
					int start = region.getOffset() + lineStr.indexOf("/*"); //$NON-NLS-1$
					int end = region.getOffset() + lineStr.lastIndexOf("*/"); //$NON-NLS-1$
					MultiTextEdit multiTextEdit = new MultiTextEdit();
					multiTextEdit.addChild(new DeleteEdit(start, 2));
					multiTextEdit.addChild(new DeleteEdit(end, 2));
					multiTextEdit.apply(doc);

				} else {
					int start = region.getOffset();
					start += StringUtil.firstWordPosition(lineStr);

					int end = region.getOffset() + region.getLength();
					MultiTextEdit multiTextEdit = new MultiTextEdit();
					multiTextEdit.addChild(new InsertEdit(start, "/*")); //$NON-NLS-1$
					multiTextEdit.addChild(new InsertEdit(end, "*/")); //$NON-NLS-1$
					multiTextEdit.apply(doc);

				}

			} else {
				Point selectedRange = getSelectedRange();

				IRegion startRegion = doc.getLineInformationOfOffset(selectedRange.x);
				IRegion endRegion = doc.getLineInformationOfOffset(selectedRange.x + selectedRange.y);
				String startLineStr = doc.get(startRegion.getOffset(), startRegion.getLength());
				String endLineStr = doc.get(endRegion.getOffset(), endRegion.getLength());
				String beginWk = startLineStr.trim();
				String endWk = endLineStr.trim();

				if (beginWk.startsWith("/*") && endWk.endsWith("*/")) { //$NON-NLS-1$ //$NON-NLS-2$
					int start = startRegion.getOffset() + startLineStr.indexOf("/*"); //$NON-NLS-1$
					int end = endRegion.getOffset() + endLineStr.lastIndexOf("*/"); //$NON-NLS-1$
					MultiTextEdit multiTextEdit = new MultiTextEdit();
					multiTextEdit.addChild(new DeleteEdit(start, 2));
					multiTextEdit.addChild(new DeleteEdit(end, 2));
					multiTextEdit.apply(doc);

				} else {
					int start = startRegion.getOffset();
					start += StringUtil.firstWordPosition(startLineStr);

					int end = endRegion.getOffset() + endRegion.getLength();
					MultiTextEdit multiTextEdit = new MultiTextEdit();
					multiTextEdit.addChild(new InsertEdit(start, "/*")); //$NON-NLS-1$
					multiTextEdit.addChild(new InsertEdit(end, "*/")); //$NON-NLS-1$
					multiTextEdit.apply(doc);

				}
			}

		} catch (MalformedTreeException e) {
			DbPlugin.log(e);
		} catch (BadLocationException e) {
			DbPlugin.log(e);
		}

	}

	public void doNextSQL() {
		SQLHistory history = mgr.nextHisotry();
		if (history != null) {
			getDocument().set(mgr.loadContents(history));
			setSelectedRange(history.getSql().length(), 0);
			invalidateTextPresentation();
		} else {
			getDocument().set(""); //$NON-NLS-1$
		}
		DbPlugin.fireStatusChangeListener(this, IStatusChangeListener.EVT_UpdateHistory);
	}

	public void doBackSQL() {
		SQLHistory history = mgr.prevHisotry();
		if (history != null) {
			getDocument().set(mgr.loadContents(history));
			setSelectedRange(history.getSql().length(), 0);
			invalidateTextPresentation();

		} else {
			getDocument().set(""); //$NON-NLS-1$
		}
		DbPlugin.fireStatusChangeListener(this, IStatusChangeListener.EVT_UpdateHistory);
	}

	protected void doCommit() {
		try {
			Transaction trans = Transaction.getInstance(config);
			if (!trans.isConneting()) {
				DbPlugin.getDefault().showWarningMessage(DbPluginConstant.MSG_NO_CONNECTED_DB);
				return;
			}

			SQLExecuteView view = getSQLExecuteView();
			if (view != null) {

				int transCount = trans.getTransactionCount();

				trans.commit();
				StringBuffer sb = new StringBuffer();
				sb.append(transCount);
				sb.append(Messages.getString("SQLSourceViewer.21")); //$NON-NLS-1$
				view.setStatusMessage(sb.toString());

			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	protected void doRollback() {
		try {
			Transaction trans = Transaction.getInstance(config);
			if (!trans.isConneting()) {
				DbPlugin.getDefault().showWarningMessage(DbPluginConstant.MSG_NO_CONNECTED_DB);
				return;
			}

			SQLExecuteView view = getSQLExecuteView();
			if (view != null) {
				int transCount = trans.getTransactionCount();
				trans.rollback();
				StringBuffer sb = new StringBuffer();
				sb.append(transCount);
				sb.append(Messages.getString("SQLSourceViewer.22")); //$NON-NLS-1$
				view.setStatusMessage(sb.toString());
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	protected void doAllClear() {
		getDocument().set(""); //$NON-NLS-1$
	}


	public IDBConfig getDbConfig() {
		return config;
	}

	public void setDbConfig(IDBConfig config) {
		this.config = config;
	}

	public void setSecondaryId(String secondaryId) {
		this.secondaryId = secondaryId;
	}

	public String getSqlFileName() {
		return sqlFileName;
	}

	public void setSqlFile(File sqlFile) {
		this.sqlFileName = sqlFile.getName();
	}

	public void setSqlFileName(String fileName) {
		this.sqlFileName = fileName;
	}

	public boolean isFormatPreExecute() {
		return isFormatPreExecute;
	}

	public void setFormatPreExecute(boolean isFormatPreExecute) {
		this.isFormatPreExecute = isFormatPreExecute;
	}

	public class VerifyKeyAdapter implements VerifyKeyListener {

		public void verifyKey(VerifyEvent event) {

			if (event.character == SWT.CR) {
				try {
					String demiliter = DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
					IDocument doc = getDocument();
					StyledText text = getTextWidget();
					int offset = text.getCaretOffset();
					if (offset > 0) {
						char c = doc.getChar(offset - 1);
						if (demiliter.equals(new String(new char[] {c}))) {
							// if (c == '/') {
							IRegion region = doc.getLineInformationOfOffset(offset);
							String wk = doc.get(region.getOffset(), region.getLength() - 1).trim();
							if (wk.length() == 0) {
								doc.replace(region.getOffset(), region.getLength() - 1, ""); //$NON-NLS-1$
							}
						}
					}
					event.doit = true;
				} catch (BadLocationException e) {
					DbPlugin.log(e);
				}
			}

			// Ctrl + Space add for Mac OS
			if ((event.stateMask == SWT.CTRL && event.character == ' ')
				|| (event.stateMask == SWT.CTRL && event.keyCode == 32)) {
				if (canDoOperation(ISourceViewer.CONTENTASSIST_PROPOSALS))
					doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
				event.doit = false;

			}
			// Ctrl + D
			if (event.stateMask == SWT.CTRL && event.keyCode == 100) {
				if (canDoOperation(ISQLOperationTarget.LINE_DEL)) {
					doOperation(ISQLOperationTarget.LINE_DEL);
				}
				event.doit = false;
			}

			// Ctrl + Shift + F
			if (event.stateMask == SWT.CTRL + SWT.SHIFT && event.keyCode == 102) {
				if (canDoOperation(ISQLOperationTarget.FORMAT)) {
					doOperation(ISQLOperationTarget.FORMAT);
				}

				event.doit = false;
			}

			// Ctrl + SHIFT + U
			if (event.stateMask == SWT.CTRL + SWT.SHIFT && event.keyCode == 117) {
				if (canDoOperation(ISQLOperationTarget.UNFORMAT)) {
					doOperation(ISQLOperationTarget.UNFORMAT);
				}
				event.doit = false;
			}

			// Ctrl + /
			if (event.stateMask == SWT.CTRL && event.keyCode == 47) {
				if (canDoOperation(ISQLOperationTarget.COMMENT)) {
					doOperation(ISQLOperationTarget.COMMENT);
				}
				event.doit = false;
			}

			// if(event.stateMask == SWT.SHIFT + SWT.TAB){
			// doOperation();
			// event.doit = false;
			//
			// }

		}

	}


}
