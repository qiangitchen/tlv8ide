package com.tulin.v8.ureport.wizards;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class UReportFileNewWizard extends Wizard implements INewWizard {
	private ISelection selection;
	NewUReportFilePage newfilepage;
	private IFile files;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	public void addPages() {
		newfilepage = new NewUReportFilePage("newureportfile", selection);
		addPage(newfilepage);
		setWindowTitle(Messages.getString("wizards.message.title"));
	}

	@Override
	public boolean performFinish() {
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

	/**
	 * 完成
	 * 
	 * @throws Exception
	 */
	private void doFinish(String containerName, String fileName, IProgressMonitor monitor) throws Exception {
		if (fileName.indexOf(".") < 0) {
			fileName = fileName + ".ureport";
		}
		monitor.beginTask(Messages.getString("wizards.message.createFile") + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException(Messages.getString("wizards.message.missfolder").replace("{1}", containerName));
		}
		IContainer container = (IContainer) resource;
		files = container.getFile(new Path(fileName));
		try {
			InputStream stream = openContentStream();
			if (files.exists()) {
				files.setContents(stream, true, true, monitor);
			} else {
				files.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		}
		monitor.worked(1);
		monitor.setTaskName(Messages.getString("wizards.message.doingOpenfile"));
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, files);
				} catch (Exception e) {
				}
			}
		});
		monitor.worked(1);
	}

	private InputStream openContentStream() throws Exception {
		return this.getClass().getResourceAsStream("template.ureport.xml");
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "TuLin Studio", IStatus.OK, message, null);
		throw new CoreException(status);
	}

}
