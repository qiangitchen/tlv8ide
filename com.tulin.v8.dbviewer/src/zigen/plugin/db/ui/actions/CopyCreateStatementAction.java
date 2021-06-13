/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.ClipboardUtils;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.AbstractSQLCreatorFactory;
import zigen.plugin.db.core.rule.ISQLCreatorFactory;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.views.ColumnSearchAction;
import zigen.plugin.db.ui.views.TreeView;

public class CopyCreateStatementAction implements IViewActionDelegate {

	private ISelection selection = null;

	private IViewPart viewPart;

	TreeViewer treeViewer;

	boolean isSelectedColumn = false;

	StringBuffer sb = null;

	public void init(IViewPart view) {
		this.viewPart = view;

		if (view instanceof TreeView) {
			treeViewer = ((TreeView) view).getTreeViewer();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	public void run(IAction action) {

		try {
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection) selection;

				StringBuffer sb = new StringBuffer();

				if (!checkLoadColumn(ss)) {
					String msg = Messages.getString("CopyCreateStatementAction.0"); //$NON-NLS-1$
					msg += Messages.getString("CopyCreateStatementAction.1"); //$NON-NLS-1$
					if (DbPlugin.getDefault().confirmDialog(msg)) {
						new LoadAction(treeViewer, ss).run();
					} else {
						return;
					}
				} else {
					new LoadAction(treeViewer, ss).run();
				}

			}

		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
	}

	private boolean checkLoadColumn(IStructuredSelection ss) {
		for (Iterator iter = ss.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof ITable) {
				ITable table = (ITable) obj;
				if (!table.isExpanded()) {
					return false;
				}
			}
		}
		return true;
	}

	private String getCommentStatement(ITable tableNode) {
		StringBuffer sb = new StringBuffer();
		if (tableNode != null) {
			IDBConfig config = tableNode.getDbConfig();
			ISQLCreatorFactory factory = AbstractSQLCreatorFactory.getFactoryNoCache(config, tableNode);
			factory.setVisibleSchemaName(false);
			sb.append(factory.getTableComment());
			sb.append(factory.getColumnComment());

		}
		return sb.toString();
	}

	void setContents(String contents) {
		Clipboard clipboard = ClipboardUtils.getInstance();
		if (contents.length() > 0) {
			clipboard.setContents(new Object[] {contents}, new Transfer[] {TextTransfer.getInstance()});
		}
	}

	class LoadAction extends Action {

		IStructuredSelection ss;

		StructuredViewer viewer;

		public LoadAction(StructuredViewer viewer, IStructuredSelection ss) {
			this.viewer = viewer;
			this.ss = ss;
		}

		public void run() {
			try {
				IRunnableWithProgress op = new IRunnableWithProgress() {

					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask(Messages.getString("CopyCreateStatementAction.2"), ss.size()); //$NON-NLS-1$
						sb = new StringBuffer(); // clear
						int i = 1;
						for (Iterator iter = ss.iterator(); iter.hasNext();) {
							if (monitor.isCanceled())
								throw new InterruptedException();

							Object obj = iter.next();
							if (obj instanceof ITable) {
								ITable table = (ITable) obj;
								monitor.subTask("Target : " + table + ", " + i + "/" + ss.size()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								if (!table.isExpanded()) {
									table.setExpanded(true);

									new ColumnSearchAction(viewer, table).run();
								}

								sb.append(getCommentStatement(table));
							}
							monitor.worked(1);
							i++;
						}

						monitor.done();
						setContents(sb.toString());
					}
				};

				new ProgressMonitorDialog(DbPlugin.getDefault().getShell()).run(true, true, op);

			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				DbPlugin.getDefault().showInformationMessage(Messages.getString("CopyCreateStatementAction.6")); //$NON-NLS-1$
			}
		}

	}
}
