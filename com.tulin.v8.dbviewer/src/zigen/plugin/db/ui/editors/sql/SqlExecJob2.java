package zigen.plugin.db.ui.editors.sql;

import org.eclipse.swt.widgets.Display;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.SQLInvoker;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.actions.MaxRecordException;
import zigen.plugin.db.ui.jobs.SqlExecJob;

public class SqlExecJob2 extends SqlExecJob {

	SqlEditor2 editor;

	public SqlExecJob2(SqlEditor2 editor, Transaction trans, String sqlString) {
		super(trans, sqlString, null);
		this.editor = editor;

	}

	protected void showDBEditor(String query) throws Exception {
		TableElement[] elements = null;
		TimeWatcher time = new TimeWatcher();
		time.start();
		IDBConfig config = trans.getConfig();
		try {
			elements = SQLInvoker.executeQuery(trans.getConnection(), query, config.isConvertUnicode(), config.isNoLockMode());
			time.stop();
			showResults(new ShowResultAction(config, query, elements, time.getTotalTime()));

		} catch (MaxRecordException e) {
			time.stop();
			elements = e.getTableElements();
			showResults(new ShowResultAction(config, query, elements, time.getTotalTime(), e.getMessage()));
		} catch (Exception e) {
			throw e;
		}
	}

	protected class ShowResultAction implements Runnable {

		IDBConfig config = null;

		String query = null;

		TableElement[] elements = null;

		String responseTime = null;

		String message = ""; //$NON-NLS-1$

		public ShowResultAction(IDBConfig config, String query, TableElement[] elements, String responseTime, String message) {
			this.config = config;
			this.query = query;
			this.elements = elements;
			this.responseTime = responseTime;
			this.message = message;
		}

		public ShowResultAction(IDBConfig config, String query, TableElement[] elements, String responseTime) {
			this(config, query, elements, responseTime, ""); //$NON-NLS-1$
		}

		public void run() {
			try {
				editor.update(query, elements, responseTime, false);

			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}

	}


	protected void updateMessage(IDBConfig config, String message, String secondaryId) {
		Display.getDefault().asyncExec((Runnable) new UpdateStatusMessageAction(message));
	}


	public class UpdateStatusMessageAction implements Runnable {

		private String message;

		public UpdateStatusMessageAction(String message) {
			this.message = message;
		}

		public void run() {
			try {
				editor.getIStatusLineManager().setMessage(message);
			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}

	}

}
