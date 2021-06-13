package com.tulin.v8.ide.wizards.folder;

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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.Sys;
import com.tulin.v8.ide.utils.SelectionUtil;
import com.tulin.v8.ide.wizards.Messages;

import zigen.plugin.db.ui.internal.TreeNode;

public class NewFolderWizard extends Wizard implements INewWizard {
	NewFolderPage newfolderpage;
	TreeViewer viewer = null;

	public NewFolderWizard(TreeViewer viewer) {
		super();
		this.viewer = viewer;
		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
	}

	public void addPages() {
		newfolderpage = new NewFolderPage("newfolder", viewer.getSelection());
		addPage(newfolderpage);
		setWindowTitle(Messages.getString("wizardsaction.newfilefold.new")
				+ Messages.getString("wizardsaction.newfilefold.ftitle"));
	}

	public boolean canFinish() {
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public boolean performFinish() {
		if (!newfolderpage.isCanFinish()) {
			return false;
		}
		final String containerName = newfolderpage.getContainerName();
		final String fileName = newfolderpage.getFileName();
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

	private void doFinish(final String containerName, final String fileName, IProgressMonitor monitor)
			throws Exception {
		try {
			File fofile = new File(StudioPlugin.getWorkPath() + containerName);
			if (!fofile.exists()) {
				throwCoreException(
						Messages.getString("wizards.dataselect.message.datatdesc").replace("{1}", containerName));
			}
			final File file = new File(StudioPlugin.getWorkPath() + containerName + "/" + fileName);
			file.mkdir();
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					ISelection selection = viewer.getSelection();
					Object obj = SelectionUtil.getSingleElement(selection);
					if (obj != null) {
						TreeNode node = (TreeNode) obj;
						TreeNode newnode = new TreeNode(fileName);
						newnode.setBiz(node.getBiz() != null ? node.getBiz() : node.getName());
						newnode.setPath(file.getAbsolutePath());
						newnode.setTvtype("folder");
						node.addChild(newnode);
						viewer.refresh(node);
						viewer.setSelection(new StructuredSelection(newnode));
						IFile iFile = StudioPlugin.getWorkspace().getRoot()
								.getFileForLocation(new Path(containerName));
						try {
							iFile.refreshLocal(0, null);
						} catch (Exception ee) {
						}
					}
				}
			});
		} catch (Exception e) {
			throwCoreException(Messages.getString("wizardsaction.newfilefold.nameerrfmsg") + e.toString());
			Sys.packErrMsg(e.toString());
		}
		monitor.worked(1);
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "TuLin Studio", IStatus.OK, message, null);
		throw new CoreException(status);
	}

}
