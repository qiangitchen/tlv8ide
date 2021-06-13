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

public class CopyCommentStatementAction implements IViewActionDelegate {

	private ISelection selection = null;

	private IViewPart viewPart;

	TreeViewer treeViewer;

	boolean isSelectedColumn = false;

	StringBuffer buffer = new StringBuffer();

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


				new CopyAction(treeViewer, ss).run();
				setContents(buffer.toString());
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
		if (contents.length() > 0) {
			Clipboard clipboard = ClipboardUtils.getInstance();
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
						monitor.beginTask(Messages.getString("CopyCommentStatementAction.0"), ss.size()); //$NON-NLS-1$
						buffer = new StringBuffer(); // clear
						int i = 1;
						for (Iterator iter = ss.iterator(); iter.hasNext();) {
							if (monitor.isCanceled())
								throw new InterruptedException();

							Object obj = iter.next();
							if (obj instanceof ITable) {
								ITable table = (ITable) obj;

								StringBuffer sb = new StringBuffer();
								sb.append(Messages.getString("CopyCommentStatementAction.1")); //$NON-NLS-1$
								sb.append(table.getName());
								sb.append(", "); //$NON-NLS-1$
								sb.append(i);
								sb.append(" / "); //$NON-NLS-1$
								sb.append(ss.size());

								monitor.subTask(sb.toString());
								if (!table.isExpanded()) {
									table.setExpanded(true);

									new ColumnSearchAction(viewer, table).run();
								}

								buffer.append(getCommentStatement(table));
							}
							monitor.worked(1);
							i++;
						}

						monitor.done();


					}
				};

				new ProgressMonitorDialog(DbPlugin.getDefault().getShell()).run(true, true, op);

			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				DbPlugin.getDefault().showInformationMessage(Messages.getString("CopyCommentStatementAction.4")); //$NON-NLS-1$
			}
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
	}
}
