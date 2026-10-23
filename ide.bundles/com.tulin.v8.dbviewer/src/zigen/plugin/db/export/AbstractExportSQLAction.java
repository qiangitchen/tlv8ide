/*
 * TLV8 DbViewer - SQL export action support
 *
 */
package zigen.plugin.db.export;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.Messages;
import zigen.plugin.db.ui.internal.ITable;

/**
 * Abstract base for SQL export popup actions.
 *
 */
public abstract class AbstractExportSQLAction implements IViewActionDelegate {

	protected ISelection selection = null;

	public void init(IViewPart view) {
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	/**
	 * Collect the elements (ITable, DataBase, Schema or Folder) to export from
	 * the current selection. Returns empty list if nothing is exportable.
	 */
	protected abstract List collectExportElements();

	/**
	 * Suggest a file name for the save dialog.
	 */
	protected abstract String suggestFileName(List exportElements);

	public void run(IAction action) {
		try {
			List exportElements = collectExportElements();
			if (exportElements.isEmpty()) {
				MessageDialog.openWarning(null, Messages.getString("ExportSQLAction.0"), Messages.getString("ExportSQLAction.1")); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}

			// choose export scope: structure only / structure + data
			ExportScopeDialog scopeDialog = new ExportScopeDialog(DbPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell());
			if (scopeDialog.open() != ExportScopeDialog.OK) {
				return;
			}
			boolean includeData = scopeDialog.isIncludeData();

			String suggested = suggestFileName(exportElements);
			String path = openSaveDialog(suggested);
			if (path == null) {
				return; // canceled
			}

			new ExportSQLJob(exportElements, path, includeData).schedule();

		} catch (Exception e) {
			DbPlugin.log(e);
			MessageDialog.openError(null, Messages.getString("ExportSQLAction.0"), e.getMessage()); //$NON-NLS-1$
		}
	}

	protected List getSelectedInstances(Class clazz) {
		List result = new ArrayList();
		if (selection instanceof IStructuredSelection) {
			for (Iterator iter = ((IStructuredSelection) selection).iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (clazz.isInstance(obj)) {
					result.add(obj);
				}
			}
		}
		return result;
	}

	/**
	 * Opens a file save dialog and returns the absolute path or null if
	 * canceled.
	 */
	protected String openSaveDialog(String suggestedFileName) {
		FileDialog dialog = new FileDialog(DbPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.sql", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
		dialog.setFilterNames(new String[] { Messages.getString("ExportSQLAction.2"), "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
		dialog.setFileName(suggestedFileName);
		dialog.setOverwrite(true);
		String path = dialog.open();
		if (path != null && path.indexOf('.') < 0) {
			path = path + ".sql"; //$NON-NLS-1$
		}
		return path;
	}

	/**
	 * Default suggested file name for DataBase/Schema/Folder exports.
	 */
	protected String suggestForContainer(Object element) {
		if (element instanceof ITable) {
			return ((ITable) element).getName() + ".sql"; //$NON-NLS-1$
		}
		try {
			zigen.plugin.db.core.IDBConfig config = ((zigen.plugin.db.ui.internal.TreeLeaf) element).getDbConfig();
			if (config != null) {
				return config.getDbName() + "_" + ((zigen.plugin.db.ui.internal.TreeLeaf) element).getName() + ".sql"; //$NON-NLS-1$ //$NON-NLS-2$
			}
		} catch (Exception e) {
		}
		return "export.sql"; //$NON-NLS-1$
	}

}
