/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.jobs;

import java.sql.Connection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.TimeWatcher;
import zigen.plugin.db.core.Transaction;
import zigen.plugin.db.ui.editors.TableViewEditorFor31;
import zigen.plugin.db.ui.editors.TableViewEditorInput;
import zigen.plugin.db.ui.editors.exceptions.NotFoundSynonymInfoException;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.views.SQLExecuteView;

public class OpenEditorJob extends RefreshColumnJob {

	public static final String JOB_NAME = OpenEditorJob.class.getName();

	private TreeViewer viewer;

	private ITable table;

	public OpenEditorJob(TreeViewer viewer, ITable table) {
		super(viewer, table);
		super.setName(Messages.getString("OpenEditorJob.0")); //$NON-NLS-1$
		this.viewer = viewer;
		this.table = table;
	}

	private static Object lock = new Object();

	protected IStatus run(IProgressMonitor monitor) {
		try {
			synchronized (lock) {
				IDBConfig config = table.getDbConfig();
				Connection con = Transaction.getInstance(config).getConnection();

				monitor.beginTask("Open Editor...", 10);

				showResults(new ShowSQLViewerAction(table));
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}

				showSyncResults(new ShowTableEditorAction(table));

				if (table instanceof Bookmark) {
					Bookmark bookmark = (Bookmark) table;
					DataBase db = findDataBase(bookmark);

					if (!db.isConnected()) {
						db.setConnected(true);
						db.removeChild(db.getChild(DbPluginConstant.TREE_LEAF_LOADING));
						if (db.isSchemaSupport()) {
							db.addChild(new Schema(DbPluginConstant.TREE_LEAF_LOADING));
						} else {
							db.addChild(new Folder(DbPluginConstant.TREE_LEAF_LOADING));
						}
						showResults(new RefreshTreeNodeAction(viewer, db, RefreshTreeNodeAction.MODE_COLLAPSE));

					}
				}
				monitor.done();
			}

		} catch (NotFoundSynonymInfoException e) {
			table.setEnabled(false);
			table.removeChildAll();
			showResults(new RefreshTreeNodeAction(viewer, table));
			showErrorMessage(Messages.getString("OpenEditorJob.1"), e); //$NON-NLS-1$

		} catch (Exception e) {
			showErrorMessage(Messages.getString("OpenEditorJob.2"), e); //$NON-NLS-1$

		}

		return Status.OK_STATUS;
	}

	protected class ShowSQLViewerAction implements Runnable {

		ITable table;

		public ShowSQLViewerAction(ITable table) {
			this.table = table;
		}

		public void run() {
			try {
				IDBConfig config = table.getDbConfig();
				if (config != null) {

					SQLExecuteView view = null;
					IWorkbenchPage page = DbPlugin.getDefault().getPage();
					IViewReference[] references = page.getViewReferences();
					for (int i = 0; i < references.length; i++) {
						IViewReference reference = references[i];
						String viewId = reference.getId();
						String secondaryId = reference.getSecondaryId();

						if (DbPluginConstant.VIEW_ID_SQLExecute.equals(viewId)) {
							if (secondaryId != null) {
								// view = (SQLExecuteView)page.showView(viewId,
								// secondaryId, IWorkbenchPage.VIEW_CREATE);
								view = (SQLExecuteView) reference.getView(true);
							} else {
								// view = (SQLExecuteView)page.findView(viewId);
								view = (SQLExecuteView) reference.getView(true);
							}
							if (view != null) {
								view.updateCombo(config);
								break;
							}
						}
					}

					if (view == null) {
						view = (SQLExecuteView) DbPlugin.showView(DbPluginConstant.VIEW_ID_SQLExecute);
						view.updateCombo(config);
					}

				}
			} catch (Exception e) {
				DbPlugin.getDefault().showErrorDialog(e);
			}

		}
	}

	protected class ShowTableEditorAction implements Runnable {

		ITable table;

		public ShowTableEditorAction(ITable table) {
			this.table = table;
		}

		public void run() {
			IEditorPart editor = null;
			try {

				TimeWatcher tw = new TimeWatcher();
				tw.start();

				IDBConfig config = table.getDbConfig();
				TableViewEditorInput input = new TableViewEditorInput(config, table);
				IWorkbenchPage page = DbPlugin.getDefault().getPage();
				editor = IDE.openEditor(page, input, DbPluginConstant.EDITOR_ID_TableView, true);

				if (editor instanceof TableViewEditorFor31) {
					TableViewEditorFor31 tEditor = (TableViewEditorFor31) editor;
					tEditor.setSelection(viewer.getSelection());
					boolean isSearch = false;
					tEditor.createResultPage(config, table, isSearch);

				}
				tw.stop();

			} catch (Exception e) {
				DbPlugin.getDefault().showErrorDialog(e);
				if (editor != null) {
					editor.dispose();
				}

			}

		}
	}

}
