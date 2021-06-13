/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import org.eclipse.jface.text.IDocument;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.parser.util.CurrentSql;
import zigen.plugin.db.preference.SQLEditorPreferencePage;
import zigen.plugin.db.ui.jobs.SqlExecJob;
import zigen.plugin.db.ui.views.internal.SQLSourceViewer;

abstract public class AbstractExecuteSQLAction extends SQLSourceViewerAction implements Runnable {

	private static int MAX_BYTES_SIZE = 1024 * 1024;

	protected IDBConfig config;

	protected IDocument doc;

	protected int offset;

	protected int viewer;

	protected String secondaryId;

	public AbstractExecuteSQLAction(IDBConfig config, SQLSourceViewer viewer, String secondaryId) {
		super(viewer);
		this.config = config;
		this.doc = viewer.getDocument();
		this.offset = viewer.getTextWidget().getCaretOffset();
		this.secondaryId = secondaryId;

		this.setImageDescriptor(DbPlugin.getDefault().getImageDescriptor(DbPlugin.IMG_CODE_EXECUTE));
	}

	protected String getDemiliter() {
		return DbPlugin.getDefault().getPreferenceStore().getString(SQLEditorPreferencePage.P_SQL_DEMILITER);
	}

	protected String getAllSql() {
		return doc.get();
	}

	protected String getCurrentSql() {
		String demiliter = getDemiliter();
		return new CurrentSql(doc, offset, demiliter).getSql();
	}

	protected void executeSql(String sql) {
		if (sql != null && sql.trim().length() > 0) {

			Transaction trans = Transaction.getInstance(config);
			SqlExecJob job = new SqlExecJob(trans, sql, secondaryId);
			job.setPriority(SqlExecJob.INTERACTIVE);
			job.setUser(false);
			job.schedule();

		} else {
			DbPlugin.getDefault().showInformationMessage(Messages.getString("AbstractExecuteSQLAction.Message")); //$NON-NLS-1$
		}
	}


}
