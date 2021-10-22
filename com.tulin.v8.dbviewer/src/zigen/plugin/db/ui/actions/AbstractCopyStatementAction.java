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
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.views.ColumnSearchAction;
import zigen.plugin.db.ui.views.TreeView;

abstract public class AbstractCopyStatementAction implements IViewActionDelegate {

	protected ISelection selection = null;

	protected IViewPart viewPart;

	protected TreeViewer treeViewer;

	protected StringBuffer sb = null;

	public void init(IViewPart view) {
		this.viewPart = view;
		if (view instanceof TreeView) {
			this.treeViewer = ((TreeView) view).getTreeViewer();
		} else {
			throw new RuntimeException("Required TreeView"); //$NON-NLS-1$
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

				new CopyAction(treeViewer, ss).run();


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


	void setContents(String contents) {
		Clipboard clipboard = ClipboardUtils.getInstance();
		if (contents.length() > 0) {
			clipboard.setContents(new Object[] {contents}, new Transfer[] {TextTransfer.getInstance()});
		}
	}

	class CopyAction extends Action {

		IStructuredSelection ss;

		StructuredViewer viewer;

		public CopyAction(StructuredViewer viewer, IStructuredSelection ss) {
			this.viewer = viewer;
			this.ss = ss;
		}

		public void run() {
			try {
				IRunnableWithProgress op = new IRunnableWithProgress() {

					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask("Loading Column information...", ss.size());
						sb = new StringBuffer(); // clear
						int i = 1;
						for (Iterator iter = ss.iterator(); iter.hasNext();) {
							if (monitor.isCanceled())
								throw new InterruptedException();

							Object obj = iter.next();
							if (obj instanceof ITable) {
								ITable table = (ITable) obj;
								monitor.subTask("Target : " + table + ", " + i + "/" + ss.size());
								if (!table.isExpanded()) {
									table.setExpanded(true);

									new ColumnSearchAction(viewer, table).run();
								}

								sb.append(createStatement(table));
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
				DbPlugin.getDefault().showInformationMessage("Copy was canceled.");
			}
		}

	}

	abstract String createStatement(ITable tableNode);
}
