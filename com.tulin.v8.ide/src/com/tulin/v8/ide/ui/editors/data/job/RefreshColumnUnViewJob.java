package com.tulin.v8.ide.ui.editors.data.job;

import java.sql.Connection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.tulin.v8.ide.ui.editors.data.DataEditor;
import com.tulin.v8.ide.ui.editors.data.action.SetDbDdlViewAction;

import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.ui.editors.exceptions.NotFoundSynonymInfoException;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.jobs.Messages;
import zigen.plugin.db.ui.jobs.RefreshColumnJob;

public class RefreshColumnUnViewJob extends RefreshColumnJob {
	protected ITable table;
	private MultiPageEditorPart editor;

	public RefreshColumnUnViewJob(TreeViewer viewer, ITable tableNode) {
		super(viewer, tableNode);
		this.table = tableNode;
	}

	public RefreshColumnUnViewJob(MultiPageEditorPart editor, ITable tableNode) {
		super(null, tableNode);
		this.editor = editor;
		this.table = tableNode;
	}

	protected IStatus run(IProgressMonitor monitor) {
		TimeWatcher tw = new TimeWatcher();
		tw.start();
		Connection con = null;
		IDBConfig config = table.getDbConfig();
		try {
			synchronized (table) {
				monitor.beginTask(Messages.getString("RefreshColumnJob.6"), 6); //$NON-NLS-1$
				con = ConnectionManager.getConnection(config);

				monitor.beginTask(Messages.getString("RefreshColumnJob.5"), 6); //$NON-NLS-1$

				if (!loadColumnInfo(monitor, con, table)) {
					table.setExpanded(false);
					return Status.CANCEL_STATUS;
				}
			}
			if (editor instanceof DataEditor) {
				showResults(new SetDbDdlViewAction((DataEditor) editor,
						table));
			}

		} catch (NotFoundSynonymInfoException e) {
			table.setEnabled(false);
			table.removeChildAll();
			showErrorMessage(Messages.getString("RefreshColumnJob.1"), e); //$NON-NLS-1$

		} catch (Exception e) {
			table.setExpanded(false);
			showErrorMessage(Messages.getString("RefreshColumnJob.2"), e); //$NON-NLS-1$

		} finally {
			ConnectionManager.closeConnection(con);
		}
		tw.stop();
		return Status.OK_STATUS;
	}

}
