package com.tulin.v8.ide.wizards;

import java.io.ByteArrayInputStream;
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
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.tulin.v8.core.TuLinPlugin;
import com.tulin.v8.editors.page.WebPageEditor;
import com.tulin.v8.ide.wizards.LeftTree.LeftTreePageEnd;
import com.tulin.v8.ide.wizards.LeftTree.LeftTreedataPermision;
import com.tulin.v8.ide.wizards.LeftTree.LeftWriteTreePage;
import com.tulin.v8.ide.wizards.deatail.SampleDeatailEnd;
import com.tulin.v8.ide.wizards.deatail.SampleDeatailPage;
import com.tulin.v8.ide.wizards.deatail.WriteSampleDetail;
import com.tulin.v8.ide.wizards.directDetail.DirectDetailPageEnd;
import com.tulin.v8.ide.wizards.directDetail.DirectDetailPermision;
import com.tulin.v8.ide.wizards.directDetail.WriteDirectDetail;
import com.tulin.v8.ide.wizards.directFlow.DirectFlowPageEnd;
import com.tulin.v8.ide.wizards.directFlow.DirectFlowPermision;
import com.tulin.v8.ide.wizards.directFlow.WriteDirectFlow;
import com.tulin.v8.ide.wizards.directGrid.DirectGridPageEnd;
import com.tulin.v8.ide.wizards.directGrid.DirectGridPermision;
import com.tulin.v8.ide.wizards.directGrid.WriteDirectGrid;
import com.tulin.v8.ide.wizards.flow.SampleFlowEnd;
import com.tulin.v8.ide.wizards.flow.SampleFlowPage;
import com.tulin.v8.ide.wizards.flow.WriteSampleFlow;
import com.tulin.v8.ide.wizards.gridCheckDialog.CheckListPageEnd;
import com.tulin.v8.ide.wizards.gridCheckDialog.CheckListPermision;
import com.tulin.v8.ide.wizards.gridCheckDialog.WriteCheckList;
import com.tulin.v8.ide.wizards.gridSelectDialog.SelectListPageEnd;
import com.tulin.v8.ide.wizards.gridSelectDialog.SelectListPermision;
import com.tulin.v8.ide.wizards.gridSelectDialog.WriteSelectList;
import com.tulin.v8.ide.wizards.list.SampleListPageEnd;
import com.tulin.v8.ide.wizards.list.SampleListPageLayout;
import com.tulin.v8.ide.wizards.list.WriteSampleList;
import com.tulin.v8.ide.wizards.listDetail.ListDetailPageEnd;
import com.tulin.v8.ide.wizards.listDetail.ListDetailPermision;
import com.tulin.v8.ide.wizards.listDetail.ListDetailPortalPageEnd;
import com.tulin.v8.ide.wizards.listDetail.WriteListDetailPortal;
import com.tulin.v8.ide.wizards.listDetail.WriteListDetailTag;
import com.tulin.v8.ide.wizards.mobileform.MobileSampleDeatailEnd;
import com.tulin.v8.ide.wizards.mobileform.MobileSampleDeatailPage;
import com.tulin.v8.ide.wizards.mobileform.MobileWriteSampleDetail;
import com.tulin.v8.ide.wizards.moblielist.MobileListPageEnd;
import com.tulin.v8.ide.wizards.moblielist.MobileListdataPermision;
import com.tulin.v8.ide.wizards.moblielist.WriteMobileListPage;
import com.tulin.v8.ide.wizards.moblietree.MobileTreePageEnd;
import com.tulin.v8.ide.wizards.moblietree.MobileTreedataPermision;
import com.tulin.v8.ide.wizards.moblietree.WriteMobileTreePage;
import com.tulin.v8.ide.wizards.tree.TreePageEnd;
import com.tulin.v8.ide.wizards.tree.TreedataPermision;
import com.tulin.v8.ide.wizards.tree.WriteTreePage;
import com.tulin.v8.ide.wizards.treeGrid.TreeGridPageEnd;
import com.tulin.v8.ide.wizards.treeGrid.TreeGriddataPermision;
import com.tulin.v8.ide.wizards.treeGrid.WriteTreeGridPage;

public class SampleNewWizard extends Wizard implements INewWizard {
	ISelection selection;

	private ProjectSelectPage projctPage;
	private DataSelectPage dataSelectPage;

	public SampleNewWizard() {
		super();
		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
	}
	
	public SampleNewWizard(ISelection selection) {
		super();
		this.selection = selection;
		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
		setWindowTitle(Messages.getString("wizards.message.title"));
		TuLinPlugin.setSelection(selection);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		setWindowTitle(Messages.getString("wizards.message.title"));
		TuLinPlugin.setSelection(selection);
	}

	@Override
	public void addPages() {
		projctPage = new ProjectSelectPage();
		addPage(projctPage);
		dataSelectPage = new DataSelectPage(projctPage);
		addPage(dataSelectPage);
		addPage(new SampleListPageLayout(dataSelectPage));
		addPage(new SampleListPageEnd(selection));
		addPage(new DirectGridPermision());
		addPage(new DirectGridPageEnd(selection));
		addPage(new SampleDeatailPage(dataSelectPage));
		addPage(new SampleDeatailEnd(selection));
		addPage(new ListDetailPermision());
		addPage(new ListDetailPortalPageEnd(selection));
		addPage(new ListDetailPageEnd(selection));
		addPage(new DirectDetailPermision());
		addPage(new DirectDetailPageEnd(selection));
		addPage(new SampleFlowPage(dataSelectPage));
		addPage(new SampleFlowEnd(selection));
		addPage(new DirectFlowPermision());
		addPage(new DirectFlowPageEnd(selection));
		addPage(new TreedataPermision());
		addPage(new TreePageEnd(selection));
		addPage(new LeftTreedataPermision());
		addPage(new LeftTreePageEnd(selection));
		addPage(new TreeGriddataPermision());
		addPage(new TreeGridPageEnd(selection));
		addPage(new SelectListPermision());
		addPage(new SelectListPageEnd(selection));
		addPage(new CheckListPermision());
		addPage(new CheckListPageEnd(selection));
		addPage(new MobileListdataPermision());
		addPage(new MobileListPageEnd(selection));
		addPage(new MobileTreedataPermision());
		addPage(new MobileTreePageEnd(selection));
		addPage(new MobileSampleDeatailPage(dataSelectPage));
		addPage(new MobileSampleDeatailEnd(selection));

		addPage(new SampleNewWizardPage(selection));// 空白页
	}

	@Override
	public boolean performFinish() {
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(monitor);
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
			e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean canFinish() {
		return getContainer().getCurrentPage().getNextPage() == null;
	}

	private void doFinish(IProgressMonitor monitor) throws Exception {
		IWizardPage currentPage = getContainer().getCurrentPage();
		IFile files = null;
		if (currentPage instanceof SampleListPageEnd) {// 简单列表
			try {
				files = new WriteSampleList((SampleListPageLayout) getPage("samplelistPageLayout"),
						(SampleListPageEnd) getPage("sampleListPageEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof DirectGridPageEnd) {// 主从列表
			try {
				files = new WriteDirectGrid((DirectGridPermision) getPage("directGridPermision"),
						(DirectGridPageEnd) getPage("directGridPageEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof SampleDeatailEnd) {// 单表详细
			try {
				files = new WriteSampleDetail((SampleDeatailPage) getPage("sampleDeatailPage"),
						(SampleDeatailEnd) getPage("sampleDeatailEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof ListDetailPageEnd) {// 列表详细(tab)
			try {
				files = new WriteListDetailTag((ListDetailPermision) getPage("listDetailPermision"),
						(ListDetailPageEnd) getPage("listDetailPageEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof ListDetailPortalPageEnd) {// 列表详细(open)
			try {
				files = new WriteListDetailPortal((ListDetailPermision) getPage("listDetailPermision"),
						(ListDetailPortalPageEnd) getPage("listDetailPortalPageEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof DirectDetailPageEnd) {// 主从详细
			try {
				files = new WriteDirectDetail((DirectDetailPermision) getPage("directDetailPermision"),
						(DirectDetailPageEnd) getPage("directDetailPageEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof SampleFlowEnd) {// 单表流程
			try {
				files = new WriteSampleFlow((SampleFlowPage) getPage("sampleFlowPage"),
						(SampleFlowEnd) getPage("sampleFlowEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof DirectFlowPageEnd) {// 主从流程
			try {
				files = new WriteDirectFlow((DirectFlowPermision) getPage("directFlowPermision"),
						(DirectFlowPageEnd) getPage("directFlowPageEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof TreePageEnd) {// 简单树形
			try {
				files = new WriteTreePage((TreedataPermision) getPage("treedataPermision"),
						(TreePageEnd) getPage("treePageEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof LeftTreePageEnd) {// 左边树形
			try {
				files = new LeftWriteTreePage((LeftTreedataPermision) getPage("lefttreedataPermision"),
						(LeftTreePageEnd) getPage("lefttreePageEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof TreeGridPageEnd) {// 树形列表
			try {
				files = new WriteTreeGridPage((TreeGriddataPermision) getPage("treeGriddataPermision"),
						(TreeGridPageEnd) getPage("treeGridPageEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof SelectListPageEnd) {// 列表单选对话框
			try {
				files = new WriteSelectList((SelectListPermision) getPage("selectListPermision"),
						(SelectListPageEnd) getPage("selectListPageEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof CheckListPageEnd) {// 列表多选对话框
			try {
				files = new WriteCheckList((CheckListPermision) getPage("checkListPermision"),
						(CheckListPageEnd) getPage("checkListPageEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof MobileListPageEnd) {// 手机版列表
			try {
				files = new WriteMobileListPage((MobileListdataPermision) getPage("mobileListdataPermision"),
						(MobileListPageEnd) getPage("mobileListPageEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof MobileTreePageEnd) {// 手机版树形
			try {
				files = new WriteMobileTreePage((MobileTreedataPermision) getPage("mobiletreedataPermision"),
						(MobileTreePageEnd) getPage("mobiletreePageEnd")).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		if (currentPage instanceof MobileSampleDeatailEnd) {// 手机版本表单
			try {
				files = new MobileWriteSampleDetail((MobileSampleDeatailPage) getPage("mobilesampleDeatailPage"),
						(MobileSampleDeatailEnd) getPage("mobilesampleDeatailEnd"), projctPage).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		}
		System.out.println(currentPage);
		if (currentPage instanceof SampleNewWizardPage) {// 空白页面
			SampleNewWizardPage page = (SampleNewWizardPage) currentPage;
			String containerName = page.getContainerName();
			System.out.println(containerName);
			String fileName = page.getFileName();
			if (fileName.indexOf(".") < 0) {
				fileName = fileName + ".html";
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
		}
		if (files == null) {
			return;
		}
		final IFile file = files;
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					file.setPersistentProperty(IDE.EDITOR_KEY, WebPageEditor.ID);
					IDE.openEditor(page, file);
				} catch (Exception e) {
				}
			}
		});
		monitor.worked(1);
	}

	private InputStream openContentStream() throws Exception {
		String contents = "<html>\n" + "  <head>\n"
				+ "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n"
				+ "    <title>TuLin Page</title>\n" + "  </head>\n" + "  <body>This is a HTML page.</body>\n"
				+ "</html>";
		return new ByteArrayInputStream(contents.getBytes("UTF-8"));
	}

	void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "TuLin Studio", IStatus.OK, message, null);
		throw new CoreException(status);
	}

}
