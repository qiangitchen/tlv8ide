package com.tulin.v8.echarts.ui.wizards;

import java.io.ByteArrayInputStream;
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
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.tulin.v8.echarts.ui.wizards.chart.ChartFilePage;
import com.tulin.v8.echarts.ui.wizards.chart.ChartModlePage;
import com.tulin.v8.echarts.ui.wizards.chart.ChartOptionPage;
import com.tulin.v8.echarts.ui.wizards.chart.ChartTypeSelectPage;

public class ChartNewWizard extends Wizard implements INewWizard {
	private ISelection selection;

	private ChartTypeSelectPage charttypeselectpage;
	private ChartOptionPage chartoptionpage;
	private ChartModlePage chartmodlepage;
	private ChartFilePage chartfilepage;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;

		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
	}

	public void addPages() {
		charttypeselectpage = new ChartTypeSelectPage();
		addPage(charttypeselectpage);

		chartoptionpage = new ChartOptionPage();
		addPage(chartoptionpage);

		chartmodlepage = new ChartModlePage();
		addPage(chartmodlepage);

		chartfilepage = new ChartFilePage(selection);
		addPage(chartfilepage);

		setWindowTitle("ECharts");
	}

	public boolean canFinish() {
		IWizardPage currentPage = this.getContainer().getCurrentPage();
		if (chartfilepage == currentPage) {
			return chartfilepage.isPageComplete();
		}
		return false;
	}

	@Override
	public boolean performFinish() {
		final String containerName = chartfilepage.getContainerName();
		final String fileName = chartfilepage.getFileName();
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
		IFile files = null;
		try {
			if (!fileName.endsWith(".echt")) {
				fileName = fileName + ".echt";
			}
			monitor.beginTask(Messages.getString("wizards.message.createFile") + fileName, 2);
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource resource = root.findMember(new Path(containerName));
			if (!resource.exists() || !(resource instanceof IContainer)) {
				throwCoreException(
						Messages.getString("wizards.dataselect.message.datatdesc").replace("{1}", containerName));
			}
			IContainer container = (IContainer) resource;
			files = container.getFile(new Path(fileName));
			try {
				InputStream stream = new ByteArrayInputStream(chartmodlepage.getModleText().getBytes("UTF-8"));
				if (files.exists()) {
					files.setContents(stream, true, true, null);
				} else {
					files.create(stream, true, null);
				}
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			monitor.worked(1);
			monitor.setTaskName(Messages.getString("wizards.message.doingOpenfile"));
		} catch (Exception e) {
			e.printStackTrace();
			throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
		}
		final IFile file = files;
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file);
				} catch (Exception e) {
				}
			}
		});
		monitor.worked(1);
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "TuLin Studio", IStatus.OK, message, null);
		throw new CoreException(status);
	}

}
