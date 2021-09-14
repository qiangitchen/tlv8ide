package com.tulin.v8.ide.views.navigator.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.tulin.v8.core.Sys;
import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.flowdesigner.ui.editors.process.ProcessEditor;
import com.tulin.v8.flowdesigner.ui.editors.process.ProcessEditorInput;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.internal.FlowDraw;

public class OpenFlowDrawAction extends Action implements Runnable {
	TreeViewer viewer;

	public OpenFlowDrawAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(Messages.getString("editor.name.flowdraw"));
		setImageDescriptor(ImageDescriptor.createFromImage(TuLinPlugin.getIcon("M.gif")));
	}

	public void run() {
		openFile();
	}

	private void openFile() {
		ISelection selection = viewer.getSelection();
		StudioPlugin.setSelection(selection);
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (obj instanceof FlowDraw) {
			FlowDraw treeobj = (FlowDraw) obj;
			try {
				try {
					ProcessEditorInput localFileEditorInput2 = new ProcessEditorInput(treeobj.getElement());
					IDE.openEditor(page, localFileEditorInput2, ProcessEditor.ID);
				} catch (Exception e) {
					Sys.packErrMsg(e.toString());
				}
			} catch (Exception e) {
				Sys.packErrMsg(e.toString());
				showMessage(Messages.getString("View.Action.openfile.3") + obj.toString() + "<path:" + treeobj.getPath()
						+ Messages.getString("View.Action.openfile.4") + e.toString());
			}
		}
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				StudioPlugin.getResourceString("perspective.title.0"), message);
	}
}
