package com.tulin.v8.ide.views.navigator.action;

import java.io.File;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.tulin.v8.core.DBUtils;
import com.tulin.v8.core.Sys;
import com.tulin.v8.ide.EditorUtility;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.internal.FlowDraw;
import com.tulin.v8.ide.internal.FlowFolder;
import com.tulin.v8.ide.utils.ActionUtils;
import com.tulin.v8.ide.utils.FlowUtils;

import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.internal.View;

public class DeleteObjAction extends Action implements Runnable {
	TreeViewer viewer = null;

	public DeleteObjAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(Messages.getString("View.Action.DeleteOb.1"));
		setToolTipText(Messages.getString("View.Action.DeleteOb.2"));
		setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
	}

	@SuppressWarnings("rawtypes")
	public void run() {
		StructuredSelection selections = (StructuredSelection) viewer.getSelection();
		if (!selections.isEmpty()) {
			Iterator iter = selections.iterator();
			boolean result = MessageDialog.openConfirm(viewer.getControl().getShell(),
					Messages.getString("View.Action.DeleteOb.title"), Messages.getString("View.Action.DeleteOb.3"));
			if (!result)
				return;
			while (iter.hasNext()) {
				Object obj = iter.next();
				TreeNode trp = (TreeNode) obj;
				if (trp.getDbkey() != null) {
					String dbkey = trp.getDbkey();
					String sql = "drop table " + trp.getName();
					if (obj instanceof View) {
						sql = "drop view " + trp.getName();
					}
					try {
						boolean re = DBUtils.execute(dbkey, sql);
						if (re) {
							trp.getParent().removeChild(trp);
							viewer.refresh(trp.getParent());
						}
					} catch (Exception e) {
						Sys.printErrMsg(e.toString());
					}
				} else {
					try {
						Object selectobj = ((IStructuredSelection) viewer.getSelection()).getFirstElement();
						boolean closed = false;
						if (selectobj != null) {
							IWorkbenchPage page = StudioPlugin.getActivePage();
							IEditorPart part = EditorUtility.isOpenInEditor(obj);
							if (part != null) {
								closed = page.closeEditor(part, false);
							} else {
								closed = true;
							}
						}
						if (closed) {
							if (selectobj instanceof FlowDraw) {
								FlowDraw draw = (FlowDraw) selectobj;
								FlowUtils.removeFlowDraw(draw.getElement().getSid());
							} else if (selectobj instanceof FlowFolder) {
								FlowFolder flwfolder = (FlowFolder) selectobj;
								FlowUtils.removeFlowFolder(flwfolder.getElement().getSid());
							} else {
								File f = new File(trp.getPath());
								if (f.isDirectory()) {
									ActionUtils.clearDir(f);
								} else {
									f.delete();
								}
							}
							trp.getParent().removeChild(trp);
							viewer.refresh(trp.getParent());
						}
					} catch (Exception e) {
						Sys.printErrMsg(e.toString());
					}
				}
			}
		}
	}
}
