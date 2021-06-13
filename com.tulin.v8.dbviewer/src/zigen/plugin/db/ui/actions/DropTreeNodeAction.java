/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.core.DropSQLInvoker;
import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.ext.oracle.internal.OracleSequenceInfo;
import zigen.plugin.db.ui.editors.QueryViewEditorInput;
import zigen.plugin.db.ui.editors.TableViewEditorInput;
import zigen.plugin.db.ui.editors.sql.SequenceEditorInput;
import zigen.plugin.db.ui.editors.sql.SourceEditorInput;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.jobs.OracleSourceSearchJob;

public class DropTreeNodeAction extends Action implements Runnable {

	StructuredViewer viewer = null;

	public DropTreeNodeAction(StructuredViewer viewer) {
		this.viewer = viewer;
		this.setText(Messages.getString("DropTreeNodeAction.0")); //$NON-NLS-1$
		this.setToolTipText(Messages.getString("DropTreeNodeAction.1")); //$NON-NLS-1$
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));

	}

	public void run() {
		try {
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

			Iterator iter = selection.iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof OracleSource) {
					OracleSource elem = (OracleSource) obj;
					TreeNode parent = elem.getParent();
					Schema schema = elem.getSchema();
					String owner = schema.getName();
					String type = elem.getType();
					String name = elem.getName();

					if (DbPlugin.getDefault().confirmDialog(name + Messages.getString("DropTreeNodeAction.2") + type + Messages.getString("DropTreeNodeAction.3"))) { //$NON-NLS-1$ //$NON-NLS-2$
						DropSQLInvoker.execute(elem.getDbConfig(), owner, type, name);
						parent.removeChild(elem);
						viewer.refresh(parent);
						closeEditor(obj);
					}

				} else if (obj instanceof OracleSequence) {
					OracleSequence elem = (OracleSequence) obj;
					TreeNode parent = elem.getParent();
					Schema schema = elem.getSchema();
					String owner = schema.getName();
					String type = "SEQUENCE"; //$NON-NLS-1$
					String name = elem.getName();

					if (DbPlugin.getDefault().confirmDialog(name + Messages.getString("DropTreeNodeAction.5") + type + Messages.getString("DropTreeNodeAction.6"))) { //$NON-NLS-1$ //$NON-NLS-2$
						DropSQLInvoker.execute(elem.getDbConfig(), owner, type, name);
						parent.removeChild(elem);
						viewer.refresh(parent);
						closeEditor(obj);
					}

				}
			}


		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);

		}

	}

	private void closeEditor(Object obj) throws PartInitException{
		List target = new ArrayList();
		IEditorReference[] references = DbPlugin.getDefault().getPage().getEditorReferences();
		for (int i = 0; i < references.length; i++) {
			IEditorReference reference = references[i];
			IEditorInput input = reference.getEditorInput();

			if (input instanceof SequenceEditorInput) {
				OracleSequenceInfo info= ((SequenceEditorInput)input).getSequenceInfo();
				if (info.equals(obj)) {
					target.add(reference);
				}
			} else if (input instanceof SourceEditorInput) {
				OracleSource source = ((SourceEditorInput) input).getOracleSource();
				if (source.equals(obj)) {
					target.add(reference);
				}
			}

		}

		if (target.size() > 0) {
			DbPlugin.getCloseEditors((IEditorReference[]) target.toArray(new IEditorReference[0]));
		}
	}
}
