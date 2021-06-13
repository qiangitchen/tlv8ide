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

import com.tulin.v8.ide.ui.editors.page.WebPageEditor;
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
	private ProjectSelectPage projctPage;
	private DataSelectPage dataSelectPage;
	private SampleListPageLayout sampleListlay;
	private SampleListPageEnd sampleListPageEnd;
	private DirectGridPermision directGridPermision;
	private DirectGridPageEnd directGridPageEnd;
	private SampleDeatailPage sampleDeatailPage;
	private SampleDeatailEnd sampleDeatailEnd;
	private ListDetailPermision listDetailPermision;
	private ListDetailPageEnd listDetailPageEnd;
	private ListDetailPortalPageEnd listDetailPortalPageEnd;
	private DirectDetailPermision directDetailPermision;
	private DirectDetailPageEnd directDetailPageEnd;
	private SampleFlowPage sampleFlowPage;
	private SampleFlowEnd sampleFlowEnd;
	private DirectFlowPermision directFlowPermision;
	private DirectFlowPageEnd directFlowPageEnd;
	private TreedataPermision treedataPermision;
	private TreePageEnd treePageEnd;
	private LeftTreedataPermision lefttreedataPermision;
	private LeftTreePageEnd lefttreePageEnd;
	private TreeGriddataPermision treeGriddataPermision;
	private TreeGridPageEnd treeGridPageEnd;
	private SelectListPermision selectListPermision;
	private SelectListPageEnd selectListPageEnd;
	private CheckListPermision checkListPermision;
	private CheckListPageEnd checkListPageEnd;
	private MobileTreedataPermision mobiletreedatapermision;
	private MobileTreePageEnd mobiletreepageend;
	private MobileListdataPermision mobileListdataPermision;
	private MobileListPageEnd mobileListPageEnd;
	private MobileSampleDeatailPage mobilesampleDeatailPage;
	private MobileSampleDeatailEnd mobilesampleDeatailEnd;
	private SampleNewWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public SampleNewWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	public SampleNewWizard(ISelection selection) {
		this(selection, null);
	}

	public SampleNewWizard(ISelection selection, Object object) {
		super();
		this.selection = selection;

		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
	}

	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		projctPage = new ProjectSelectPage();
		addPage(projctPage);
		// 列表
		dataSelectPage = new DataSelectPage(projctPage);
		addPage(dataSelectPage);
		sampleListlay = new SampleListPageLayout(dataSelectPage);
		addPage(sampleListlay);
		sampleListPageEnd = new SampleListPageEnd(selection);
		addPage(sampleListPageEnd);
		// 主从列表
		directGridPermision = new DirectGridPermision();
		addPage(directGridPermision);
		directGridPageEnd = new DirectGridPageEnd(selection);
		addPage(directGridPageEnd);
		// 详细
		sampleDeatailPage = new SampleDeatailPage(dataSelectPage);
		addPage(sampleDeatailPage);
		sampleDeatailEnd = new SampleDeatailEnd(selection);
		addPage(sampleDeatailEnd);
		// 列表详细(tab)
		listDetailPermision = new ListDetailPermision();
		addPage(listDetailPermision);
		listDetailPageEnd = new ListDetailPageEnd(selection);
		addPage(listDetailPageEnd);
		// 列表详细(open)
		listDetailPortalPageEnd = new ListDetailPortalPageEnd(selection);
		addPage(listDetailPortalPageEnd);
		// 主从详细
		directDetailPermision = new DirectDetailPermision();
		addPage(directDetailPermision);
		directDetailPageEnd = new DirectDetailPageEnd(selection);
		addPage(directDetailPageEnd);
		// 单表流程
		sampleFlowPage = new SampleFlowPage(dataSelectPage);
		addPage(sampleFlowPage);
		sampleFlowEnd = new SampleFlowEnd(selection);
		addPage(sampleFlowEnd);
		// 主从流程
		directFlowPermision = new DirectFlowPermision();
		addPage(directFlowPermision);
		directFlowPageEnd = new DirectFlowPageEnd(selection);
		addPage(directFlowPageEnd);
		// 树形
		treedataPermision = new TreedataPermision();
		addPage(treedataPermision);
		treePageEnd = new TreePageEnd(selection);
		addPage(treePageEnd);
		// 左边树形
		lefttreedataPermision = new LeftTreedataPermision();
		addPage(lefttreedataPermision);
		lefttreePageEnd = new LeftTreePageEnd(selection);
		addPage(lefttreePageEnd);
		// 树形列表
		treeGriddataPermision = new TreeGriddataPermision();
		addPage(treeGriddataPermision);
		treeGridPageEnd = new TreeGridPageEnd(selection);
		addPage(treeGridPageEnd);
		// 列表单选对话框
		selectListPermision = new SelectListPermision();
		addPage(selectListPermision);
		selectListPageEnd = new SelectListPageEnd(selection);
		addPage(selectListPageEnd);
		// 列表多选对话框
		checkListPermision = new CheckListPermision();
		addPage(checkListPermision);
		checkListPageEnd = new CheckListPageEnd(selection);
		addPage(checkListPageEnd);
		// 手机树形
		mobiletreedatapermision = new MobileTreedataPermision();
		addPage(mobiletreedatapermision);
		mobiletreepageend = new MobileTreePageEnd(selection);
		addPage(mobiletreepageend);
		// 手机版列表
		mobileListdataPermision = new MobileListdataPermision();
		addPage(mobileListdataPermision);
		mobileListPageEnd = new MobileListPageEnd(selection);
		addPage(mobileListPageEnd);
		// 手机版表单
		mobilesampleDeatailPage = new MobileSampleDeatailPage(dataSelectPage);
		addPage(mobilesampleDeatailPage);
		mobilesampleDeatailEnd = new MobileSampleDeatailEnd(selection);
		addPage(mobilesampleDeatailEnd);
		// 空白
		page = new SampleNewWizardPage(selection);
		addPage(page);
		setWindowTitle(Messages.getString("wizards.message.title"));
	}

	public boolean performFinish() {
		final String containerName = page.getContainerName();
		final String fileName = page.getFileName();
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

	public boolean canFinish() {
		// 仅当当前页面为结束页面时才将“完成”按钮置为可用状态
		IWizardPage currentPage = this.getContainer().getCurrentPage();
		if (page == currentPage) {
			return true;
		} else if (sampleListPageEnd == currentPage) {
			return true;
		} else if (sampleDeatailEnd == currentPage) {
			return true;
		} else if (listDetailPageEnd == currentPage) {
			return true;
		} else if (listDetailPortalPageEnd == currentPage) {
			return true;
		} else if (directDetailPageEnd == currentPage) {
			return true;
		} else if (sampleFlowEnd == currentPage) {
			return true;
		} else if (directFlowPageEnd == currentPage) {
			return true;
		} else if (treePageEnd == currentPage) {
			return true;
		} else if (directGridPageEnd == currentPage) {
			return true;
		} else if (lefttreePageEnd == currentPage) {
			return true;
		} else if (treeGridPageEnd == currentPage) {
			return true;
		} else if (selectListPageEnd == currentPage) {
			return true;
		} else if (checkListPageEnd == currentPage) {
			return true;
		} else if (mobiletreepageend == currentPage) {
			return true;
		} else if (mobileListPageEnd == currentPage) {
			return true;
		} else if (mobilesampleDeatailEnd == currentPage) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 完成
	 * 
	 * @throws Exception
	 */
	private void doFinish(String containerName, String fileName, IProgressMonitor monitor) throws Exception {
		IWizardPage currentPage = this.getContainer().getCurrentPage();
		IFile files = null;
		if (currentPage == sampleListPageEnd) {// 简单列表
			try {
				files = new WriteSampleList(sampleListlay, sampleListPageEnd).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == directGridPageEnd) {// 主从列表
			try {
				files = new WriteDirectGrid(directGridPermision, directGridPageEnd).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == sampleDeatailEnd) {// 单表详细
			try {
				files = new WriteSampleDetail(sampleDeatailPage, sampleDeatailEnd).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == listDetailPageEnd) {// 列表详细(tab)
			try {
				files = new WriteListDetailTag(listDetailPermision, listDetailPageEnd).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == listDetailPortalPageEnd) {// 列表详细(open)
			try {
				files = new WriteListDetailPortal(listDetailPermision, listDetailPortalPageEnd).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == directDetailPageEnd) {// 主从详细
			try {
				files = new WriteDirectDetail(directDetailPermision, directDetailPageEnd).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == sampleFlowEnd) {// 单表流程
			try {
				files = new WriteSampleFlow(sampleFlowPage, sampleFlowEnd).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == directFlowPageEnd) {// 主从流程
			try {
				files = new WriteDirectFlow(directFlowPermision, directFlowPageEnd).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == treePageEnd) {// 简单树形
			try {
				files = new WriteTreePage(treedataPermision, treePageEnd).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == lefttreePageEnd) {// 左边树形
			try {
				files = new LeftWriteTreePage(lefttreedataPermision, lefttreePageEnd).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == treeGridPageEnd) {// 树形列表
			try {
				files = new WriteTreeGridPage(treeGriddataPermision, treeGridPageEnd).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == selectListPageEnd) {// 列表单选对话框
			try {
				files = new WriteSelectList(selectListPermision, selectListPageEnd).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == checkListPageEnd) {// 列表多选对话框
			try {
				files = new WriteCheckList(checkListPermision, checkListPageEnd).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == mobiletreepageend) {
			try {
				files = new WriteMobileTreePage(mobiletreedatapermision, mobiletreepageend).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == mobileListPageEnd) {
			try {
				files = new WriteMobileListPage(mobileListdataPermision, mobileListPageEnd).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == mobilesampleDeatailEnd) {// 手机版本表单
			try {
				files = new MobileWriteSampleDetail(mobilesampleDeatailPage, mobilesampleDeatailEnd, projctPage).writePage();
			} catch (Exception e) {
				e.printStackTrace();
				throwCoreException(Messages.getString("wizards.message.writefileErr") + e.toString());
			}
		} else if (currentPage == page) {// 空白页面
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

	/**
	 * We will initialize file contents with a sample text.
	 * 
	 * @throws Exception
	 */
	private InputStream openContentStream() throws Exception {
		String contents = "<html>\n" + "  <head>\n"
				+ "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n"
				+ "    <title>TuLin Page</title>\n" + "  </head>\n" + "  <body>This is a HTML page.</body>\n"
				+ "</html>";
		return new ByteArrayInputStream(contents.getBytes("UTF-8"));
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "TuLin Studio", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}