package com.tulin.v8.ide.wizards.functiontree;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.Sys;
import com.tulin.v8.ide.navigator.views.internal.TreeContentProvider;
import com.tulin.v8.ide.ui.editors.function.FunctionEditor;
import com.tulin.v8.ide.utils.SelectionUtil;
import com.tulin.v8.ide.wizards.Messages;

import zigen.plugin.db.ui.internal.TreeNode;

public class NewFunctionTreeFileWizard extends Wizard implements INewWizard {
	NewFunctionTreeFilePage newfilepage;
	TreeViewer viewer = null;
	String extname;
	private String fileNames;

	public NewFunctionTreeFileWizard(TreeViewer viewer, String extname) {
		super();
		this.viewer = viewer;
		this.extname = extname;
		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
	}

	public void addPages() {
		newfilepage = new NewFunctionTreeFilePage("newfolder", viewer.getSelection(), extname);
		addPage(newfilepage);
		setWindowTitle(Messages.getString("wizardsaction.newfilefold.new")
				+ Messages.getString("wizardsaction.newfilefold.title"));
	}

	public boolean canFinish() {
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public boolean performFinish() {
		if (!newfilepage.isCanFinish()) {
			return false;
		}
		final String containerName = newfilepage.getContainerName();
		final String fileName = newfilepage.getFileName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, fileName, monitor);
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}

	private void doFinish(final String containerName, String fileName, IProgressMonitor monitor) throws Exception {
		try {
			final File fofile = new File(StudioPlugin.getWorkPath() + containerName);
			if (!fofile.exists()) {
				throwCoreException(
						Messages.getString("wizards.dataselect.message.datatdesc").replace("{1}", containerName));
			}
			if (extname != null) {
				if (fileName.indexOf(".") < 0) {
					fileNames = fileName + "." + extname;
				} else {
					fileNames = fileName;
				}
			} else {
				fileNames = fileName;
			}
			final File file = new File(StudioPlugin.getWorkPath() + containerName + "/" + fileNames);
			file.createNewFile();
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					Object obj = SelectionUtil.fiendItem(((TreeContentProvider) viewer.getContentProvider()).getRoot(),
							fofile.getAbsolutePath());
					if (obj != null) {
						TreeNode node = (TreeNode) obj;
						TreeNode newnode = new TreeNode(fileNames);
						newnode.setBiz(node.getBiz() != null ? node.getBiz() : node.getName());
						newnode.setPath(file.getAbsolutePath());
						newnode.setTvtype("file");
						node.addChild(newnode);
						viewer.refresh(node);
						viewer.setSelection(new StructuredSelection(newnode));
						IFile iFile = StudioPlugin.getWorkspace().getRoot()
								.getFileForLocation(new Path(file.getAbsolutePath()));
						try {
							iFile.refreshLocal(0, null);
						} catch (Exception ee) {
						}
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						try {
							FileEditorInput localFileEditorInput2 = new FileEditorInput(iFile);
							IDE.openEditor(page, localFileEditorInput2, FunctionEditor.ID);
						} catch (PartInitException e) {
							showMessage(Messages.getString("wizardsaction.newfilefold.open") + obj.toString() + "<path:"
									+ file.getAbsolutePath() + ">"
									+ Messages.getString("wizardsaction.newfilefold.errormsgend") + e.toString());
						}
					}
				}
			});
		} catch (Exception e) {
			throwCoreException(Messages.getString("wizardsaction.newfilefold.nameerrmsg") + e.toString());
			Sys.packErrMsg(e.toString());
		}
		monitor.worked(1);
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "TuLin Studio", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				StudioPlugin.getResourceString("perspective.title.0"), message);
	}

}
