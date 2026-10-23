/*
 * TLV8 DbViewer - Database SQL export job
 *
 */
package zigen.plugin.db.export;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.progress.IProgressConstants;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.Messages;
import zigen.plugin.db.core.ConnectionManager;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.jobs.AbstractJob;

/**
 * Background job that generates the SQL script (tables & views DDL) and writes
 * it to the given file. The resulting file is opened in the default text
 * editor.
 *
 */
public class ExportSQLJob extends AbstractJob {

	private List elements;

	private String filePath;

	private boolean includeData;

	public ExportSQLJob(List elements, String filePath, boolean includeData) {
		super(Messages.getString(includeData ? "ExportSQLJobWithData.0" : "ExportSQLJob.0")); //$NON-NLS-1$ //$NON-NLS-2$
		this.elements = elements;
		this.filePath = filePath;
		this.includeData = includeData;
	}

	protected IStatus run(IProgressMonitor monitor) {
		Connection con = null;
		try {
			setProperty(IProgressConstants.ICON_PROPERTY, null);

			IDBConfig config = null;
			String dbName = ""; //$NON-NLS-1$

			// resolve all table/view nodes
			List allTables = new ArrayList();
			for (Iterator iter = elements.iterator(); iter.hasNext();) {
				Object element = iter.next();
				if (element instanceof ITable) {
					if (config == null) {
						config = ((ITable) element).getDbConfig();
						dbName = config.getDbName();
					}
					allTables.add(element);
				} else {
					if (config == null) {
						config = ((zigen.plugin.db.ui.internal.TreeLeaf) element).getDbConfig();
						dbName = config.getDbName();
					}
					allTables.addAll(ExportSQLUtil.collectTables(element));
				}
			}

			if (allTables.isEmpty()) {
				showErrorMessage(Messages.getString("ExportSQLJob.1"), null); //$NON-NLS-1$
				return Status.OK_STATUS;
			}

			monitor.beginTask(Messages.getString("ExportSQLJob.2") + dbName, allTables.size() * 2); //$NON-NLS-1$

			// ensure metadata is loaded and build the script
			con = ConnectionManager.getConnection(config);
			for (Iterator iter = allTables.iterator(); iter.hasNext();) {
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
				ITable table = (ITable) iter.next();
				monitor.subTask(table.getFolderName() + ": " + table.getName()); //$NON-NLS-1$
				try {
					ExportSQLUtil.ensureLoaded(con, table);
				} catch (Exception e) {
					DbPlugin.log(e);
				}
				monitor.worked(1);
			}

			String sql = ExportSQLUtil.buildSQLText(dbName, config, allTables);

			// append data (INSERT statements) if requested
			if (includeData) {
				StringBuffer sb = new StringBuffer(sql);
				sb.append(DbPluginConstant.LINE_SEP);
				sb.append("-- =============================================================").append(DbPluginConstant.LINE_SEP);
				sb.append("-- Data (INSERT statements)").append(DbPluginConstant.LINE_SEP);
				sb.append("-- =============================================================").append(DbPluginConstant.LINE_SEP);
				for (Iterator iter = allTables.iterator(); iter.hasNext();) {
					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
					ITable table = (ITable) iter.next();
					if ("VIEW".equals(table.getFolderName())) { //$NON-NLS-1$
						continue; // no data for views
					}
					monitor.subTask(Messages.getString("ExportSQLJobWithData.1") + table.getName()); //$NON-NLS-1$
					sb.append(DbPluginConstant.LINE_SEP);
					sb.append("-- Data for table: ").append(table.getName()).append(DbPluginConstant.LINE_SEP);
					sb.append(ExportSQLUtil.buildInsertStatements(con, table, monitor));
					monitor.worked(1);
				}
				sql = sb.toString();
			}

			// write file (UTF-8)
			OutputStreamWriter writer = null;
			try {
				writer = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"); //$NON-NLS-1$
				writer.write(sql);
			} finally {
				if (writer != null) {
					writer.close();
				}
			}

			monitor.done();

			final String path = filePath;
			showResults(new Runnable() {
				public void run() {
					openInEditor(path);
				}
			});

		} catch (Exception e) {
			DbPlugin.log(e);
			showErrorMessage(Messages.getString("ExportSQLJob.3"), e); //$NON-NLS-1$
		} finally {
			ConnectionManager.closeConnection(con);
		}
		return Status.OK_STATUS;
	}

	private void openInEditor(String path) {
		try {
			IFileStore fileStore = EFS.getLocalFileSystem().getStore(new org.eclipse.core.runtime.Path(path));
			IWorkbenchPage page = DbPlugin.getDefault().getPage();
			if (fileStore != null && fileStore.fetchInfo().exists()) {
				IEditorPart editor = IDE.openEditor(page, fileStore.toURI(), "org.eclipse.ui.DefaultTextEditor", true); //$NON-NLS-1$
				if (editor != null) {
					// opened successfully
				}
			} else {
				DbPlugin.getDefault().showInformationMessage(Messages.getString("ExportSQLJob.4") + path); //$NON-NLS-1$
			}
		} catch (PartInitException e) {
			DbPlugin.log(e);
		}
	}

}
