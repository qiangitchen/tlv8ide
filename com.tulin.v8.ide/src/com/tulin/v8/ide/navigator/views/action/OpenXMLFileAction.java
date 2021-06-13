package com.tulin.v8.ide.navigator.views.action;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.Sys;

import zigen.plugin.db.ui.internal.TreeNode;

public class OpenXMLFileAction extends Action implements Runnable {
	TreeViewer viewer;

	public OpenXMLFileAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(Messages.getString("View.Action.openfile.23"));
		setText(Messages.getString("View.Action.openfile.24"));
		setImageDescriptor(ImageDescriptor.createFromImage(StudioPlugin.getIcon("xml.gif")));
	}

	public void run() {
		openFile();
	}

	private void openFile() {
		ISelection selection = viewer.getSelection();
		StudioPlugin.setSelection(selection);
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (obj instanceof TreeNode) {
			TreeNode treeobj = (TreeNode) obj;
			String containerName = treeobj.getPath();
			File fle = new File(containerName);
			if (fle.isFile()) {
				try {
					IFile iFile = StudioPlugin.getWorkspace().getRoot()
							.getFileForLocation(new Path(fle.getAbsolutePath()));
					try {
						iFile.refreshLocal(0, null);
					} catch (CoreException localCoreException1) {
						localCoreException1.printStackTrace();
					}
					try {
						FileEditorInput localFileEditorInput2 = new FileEditorInput(iFile);
						IDE.openEditor(page, localFileEditorInput2,
								"org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart");
					} catch (Exception e) {
						Sys.packErrMsg(e.toString());
					}
				} catch (Exception e) {
					Sys.packErrMsg(e.toString());
					showMessage(Messages.getString("View.Action.openfile.3") + obj.toString() + "<path:"
							+ treeobj.getPath() + Messages.getString("View.Action.openfile.4") + e.toString());
				}
			} else {
			}
		}
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				StudioPlugin.getResourceString("perspective.title.0"), message);
	}
}
