/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPage;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.SQLFormatter;
import zigen.plugin.db.core.SQLTokenizer;
import zigen.plugin.db.core.StringUtil;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.preference.SQLFormatPreferencePage;
import zigen.plugin.db.ui.views.SQLExecuteView;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

public class SqlFormatJob extends AbstractJob {

	protected SQLSourceViewer viewer;

	protected IDocument doc;

	protected String secondarlyId;

	protected int executeCount;

	private String targetSql;

	private String formattedSql;

	int selectOffset;

	int selectLength;

	String selectedSql;

	TextSelection selection;

	boolean selectionMode = false;

	int formattOffset = 0;

	int firstWordPosition = 0;

	public SqlFormatJob(SQLSourceViewer viewer, String secondarlyId) {
		super("SQL Formatting...");
		this.viewer = viewer;
		this.doc = viewer.getDocument();

		this.secondarlyId = secondarlyId;

		this.selection = (TextSelection) viewer.getSelection();
		if (selection != null && selection.getText().length() > 0) {
			this.targetSql = selection.getText();
			this.selectionMode = true;

			calcurate(doc, selection);

		} else {
			this.targetSql = viewer.getDocument().get();

			this.selectionMode = false;
		}
		this.formattedSql = targetSql;

	}

	private void calcurate(IDocument doc, TextSelection selection) {
		try {
			int sOffset = doc.getLineOffset(selection.getStartLine());

			firstWordPosition = StringUtil.firstWordPosition(selection.getText());
			this.formattOffset = selection.getOffset() - sOffset + firstWordPosition;

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	protected IStatus run(IProgressMonitor monitor) {
		String responseTime = null;
		try {
			TimeWatcher tw = new TimeWatcher();
			tw.start();

			IPreferenceStore ps = DbPlugin.getDefault().getPreferenceStore();
			String demiliter = ps.getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
			boolean onPatch = ps.getBoolean(SQLFormatPreferencePage.P_FORMAT_PATCH);
			int type = ps.getInt(SQLFormatPreferencePage.P_USE_FORMATTER_TYPE);
			int max = ps.getInt(SQLFormatPreferencePage.P_MAX_SQL_COUNT);


			StringBuffer sb = new StringBuffer();
			SQLTokenizer st = new SQLTokenizer(targetSql, demiliter);

			// while (st.hasMoreElements()) {
			int tokenCount = st.getTokenCount();

			for (int i = 0; i < tokenCount; i++) {

				String sql = (String) st.nextElement();

				if (sql != null && sql.length() > 0) {
					if (i < max) {
						sb.append(SQLFormatter.format(sql, type, onPatch, formattOffset));
					} else {
						sb.append(sql);
					}

					if (!selectionMode) {
						addLine(sb, demiliter);
					} else {

						if (tokenCount > 1) {
							addLine(sb, demiliter);
						}
					}

					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
				}

			}

			formattedSql = sb.toString();
			tw.stop();
			responseTime = tw.getTotalTime();
			showResults(new ShowResultAction(secondarlyId, sb.toString()));
			return Status.OK_STATUS;

		} catch (Exception e) {
			showErrorMessage("Error", e); //$NON-NLS-1$
		} finally {
			showResults(new UnLockAction(secondarlyId, responseTime));
		}
		return Status.OK_STATUS;

	}

	private void addLine(StringBuffer sb, String demiliter) {
		if ("/".equals(demiliter)) { //$NON-NLS-1$
			sb.append(DbPluginConstant.LINE_SEP);
		}
		sb.append(demiliter);
		sb.append(DbPluginConstant.LINE_SEP);
	}

	public String getFormattedSql() {
		return formattedSql;
	}

	protected class ShowResultAction implements Runnable {

		String secondaryId = null;

		String formattedSql = null;

		public ShowResultAction(String secondaryId, String formattedSql) {
			this.secondaryId = secondaryId;
			this.formattedSql = formattedSql;
		}

		public void run() {
			try {

				IDocument doc = viewer.getDocument();
				int offset = viewer.getTextWidget().getCaretOffset();
				int line = doc.getLineOfOffset(offset);
				int x = offset - doc.getLineOffset(line);
				int y = doc.getLineOfOffset(offset);

				int caretPosition = viewer.getTextWidget().getCaretOffset();

				String preSql = null;

				TextSelection selection = (TextSelection) viewer.getSelection();
				if (selection != null && selection.getLength() > 0) {
					preSql = selection.getText();
					doc.replace(selection.getOffset() + firstWordPosition, preSql.length() - firstWordPosition, formattedSql);

				} else {
					preSql = doc.get();
					doc.replace(0, preSql.length(), formattedSql);
				}

				viewer.setEditable(true);

				if (selectionMode) {
					// setSelection(viewer, selection, true);
					setSelection(viewer, new TextSelection(selection.getOffset() + firstWordPosition, formattedSql.length()), true);
				} else {
					int maxLine = doc.getLineOfOffset(formattedSql.length());
					if (line > maxLine)
						line = maxLine;

					int lineLength = doc.getLineLength(line);
					String cr = doc.getLineDelimiter(line);
					if (cr != null)
						lineLength = lineLength - cr.length();

					if (lineLength < x)
						x = lineLength;

					int newOffset = doc.getLineOffset(line) + x;
					if (formattedSql.length() < newOffset)
						newOffset = formattedSql.length();

					setSelection(viewer, new TextSelection(newOffset, 0), true);
				}

				viewer.getTextWidget().notifyListeners(SWT.Selection, null);

			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}

		void setSelection(ISourceViewer viewer, ISelection selection, boolean reveal) {
			if (selection instanceof ITextSelection) {
				ITextSelection s = (ITextSelection) selection;
				viewer.setSelectedRange(s.getOffset(), s.getLength());

				if (reveal)
					viewer.revealRange(s.getOffset(), s.getLength());
			}
		}

	}

	protected class UnLockAction implements Runnable {

		String secondaryId = null;

		String responseTime = null;

		public UnLockAction(String secondaryId, String responseTime) {
			this.secondaryId = secondaryId;
			this.responseTime = responseTime;
		}

		public void run() {
			try {
				IWorkbenchPage page = DbPlugin.getDefault().getPage();
				SQLExecuteView view = (SQLExecuteView) DbPlugin.getDefault().findView(DbPluginConstant.VIEW_ID_SQLExecute, secondaryId);
				if (view != null) {

					if (!view.getSqlViewer().isEditable()) {
						view.getSqlViewer().setEditable(true);
					}

					if (responseTime != null && !"".equals(responseTime)) {
						view.setStatusMessage("formatted. " + responseTime);
					}

				} else {
				}
			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}
	}
}
