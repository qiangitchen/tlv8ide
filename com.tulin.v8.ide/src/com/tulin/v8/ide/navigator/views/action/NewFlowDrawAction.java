package com.tulin.v8.ide.navigator.views.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.Sys;
import com.tulin.v8.ide.navigator.views.dialog.NewFlowDrawDialog;
import com.tulin.v8.ide.ui.editors.process.ProcessEditor;
import com.tulin.v8.ide.ui.editors.process.ProcessEditorInput;
import com.tulin.v8.ide.ui.editors.process.element.ProcessDrawElement;
import com.tulin.v8.ide.ui.internal.FlowFolder;

public class NewFlowDrawAction extends Action implements Runnable {
	TreeViewer viewer = null;

	public NewFlowDrawAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(Messages.getString("View.Action.NewFile.10"));
		setImageDescriptor(ImageDescriptor.createFromImage(StudioPlugin
				.getIcon("M.gif")));
	}

	public void run() {
		Shell shell = StudioPlugin.getShell();
		Object obj = (Object) ((StructuredSelection) viewer.getSelection())
				.getFirstElement();
		final FlowFolder node = (FlowFolder) obj;
		NewFlowDrawDialog dialog = new NewFlowDrawDialog(shell, node);
		int state = dialog.open();
		if (state == IDialogConstants.OK_ID) {
			ActionUtils.loadFlowFolder(node);
			viewer.refresh(node);
			final String ndrawid = dialog.getDrawid();
			final ProcessDrawElement element = new ProcessDrawElement(ndrawid);
			shell.getDisplay().asyncExec(new Runnable() {
				public void run() {
					try {
						IWorkbenchPage page = PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage();
						try {
							ProcessEditorInput localFileEditorInput2 = new ProcessEditorInput(
									element);
							IDE.openEditor(page, localFileEditorInput2,
									ProcessEditor.ID);
						} catch (Exception e) {
							Sys.packErrMsg(e.toString());
						}
					} catch (Exception e) {
						Sys.packErrMsg(e.toString());
						showMessage(Messages
								.getString("View.Action.openfile.3")
								+ element.getSprocessname()
								+ "<processid:"
								+ element.getSprocessid()
								+ Messages.getString("View.Action.openfile.4")
								+ e.toString());
					}
				}
			});
		}
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				StudioPlugin.getResourceString("perspective.title.0"),
				message);
	}
}
