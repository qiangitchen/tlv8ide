/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.SQLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TableManager;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.rule.DefaultSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.actions.MaxRecordException;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.internal.ITable;

public class RecordSearchJob extends AbstractJob {

	public static final String JOB_NAME = RecordSearchJob.class.getName();

	private TableViewEditorFor31 editor;

	private String condition;

	private String orderBy;

	private int offset;

	private int limit;

	public RecordSearchJob(TableViewEditorFor31 editor, String condition, String orderBy, int offset, int limit) {
		super(Messages.getString("RecordSearchJob.0")); //$NON-NLS-1$
		this.editor = editor;
		this.condition = condition;
		this.orderBy = orderBy;
		this.offset = offset;
		this.limit = limit;
	}

	protected IStatus run(IProgressMonitor monitor) {
		TableElement[] elements = null;

		TimeWatcher time = new TimeWatcher();
		time.start();
		boolean doCalculate = false;
		try {
			ITable table = editor.getTableNode();

			if (orderBy != null) {
				if (condition == null) {
					elements = TableManager.invoke(table.getDbConfig(), table, orderBy, offset, limit);
				} else {
					elements = TableManager.invoke(table.getDbConfig(), table, condition + " " + orderBy, offset, limit);
				}

			} else {
				elements = TableManager.invoke(table.getDbConfig(), table, condition);
			}
			time.stop();
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			if (limit == 0) {
				showResults(new ShowResultAction(condition, elements, time.getTotalTime(), false));

			} else {
				IDBConfig config = table.getDbConfig();
				ISQLCreatorFactory factory = DefaultSQLCreatorFactory.getFactory(config, table);

				if (factory.isSupportPager()) {
					showResults(new ShowResultAction(condition, elements, time.getTotalTime(), true));
				} else {
					
					//showResults(new ShowResultAction(condition, elements, time.getTotalTime(), false));
					showResults(new ShowResultAction(condition, elements, time.getTotalTime(), true));	// [change] doCalculate is true
				}
			}

		} catch (MaxRecordException e) {
			time.stop();
			elements = e.getTableElements();
			showResults(new ShowResultAction(condition, elements, time.getTotalTime(), e.getMessage(), true));

		} catch (SQLException e) {
			showWarningMessage(e.getMessage());

		} catch (Exception e) {
			showErrorMessage(Messages.getString("RecordSearchJob.1"), e); //$NON-NLS-1$

		}

		return Status.OK_STATUS;
	}

	protected class ShowResultAction implements Runnable {

		String condition = null;

		TableElement[] elements = null;

		String responseTime = null;

		String message = null;

		boolean doCalculate;

		public ShowResultAction(String condition, TableElement[] elements, String responseTime, String message, boolean doCalculate) {
			this.condition = condition;
			this.elements = elements;
			this.responseTime = responseTime;
			this.message = message;
			this.doCalculate = doCalculate;
		}

		public ShowResultAction(String condition, TableElement[] elements, String responseTime, boolean doCalculate) {
			this(condition, elements, responseTime, "", doCalculate);
		}

		public void run() {
			try {
				editor.updateTableViewer(condition, elements, responseTime, doCalculate);
				editor.setInfomationText(message);
			} catch (Exception e) {
				DbPlugin.log(e);
			}

		}
	}

}
