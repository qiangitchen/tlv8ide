package com.tulin.v8.ide.wizards.folder;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
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

import com.tulin.v8.core.FileAndString;
import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.Sys;
import com.tulin.v8.ide.navigator.views.internal.TreeContentProvider;
import com.tulin.v8.ide.utils.SelectionUtil;
import com.tulin.v8.ide.utils.StudioConfig;
import com.tulin.v8.ide.wizards.Messages;

import zigen.plugin.db.ui.internal.TreeNode;

public class NewFileWizard extends Wizard implements INewWizard {
	NewFilePage newfilepage;
	TreeViewer viewer = null;
	String extname;
	private String fileNames;

	public NewFileWizard(TreeViewer viewer, String extname) {
		super();
		this.viewer = viewer;
		this.extname = extname;
		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
	}

	public void addPages() {
		newfilepage = new NewFilePage("newfolder", viewer.getSelection(), extname);
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
			if ("jsp".equals(extname) || fileNames.endsWith(".jsp")) {
				writeJspPage(file);
			}
			if ("html".equals(extname) || fileNames.endsWith(".html")) {
				writeHTMLPage(file);
			}
			if ("java".equals(extname) || fileNames.endsWith(".java")) {
				writeJavaFile(file, containerName);
			}
			if ("xml".equals(extname) || fileNames.endsWith(".xml")) {
				writeXMLFile(file);
			}
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
						IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(file.getAbsolutePath()));
						try {
							IDE.openEditorOnFileStore(page, fileStore);
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

	void writeXMLFile(File file) {
		String contents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		FileAndString.string2File(contents, file);
	}

	void writeJspPage(File file) {
		String contents = "<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>\n"
				+ "<html>\n" + "  <head>\n" + "    <title>新建JSP页面</title>\n"
				+ "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" + "  </head>\n"
				+ "  <body>This is a JSP page.</body>\n" + "</html>";
		FileAndString.string2File(contents, file);
	}

	void writeHTMLPage(File file) {
		String contents = "<html>\n" + "  <head>\n" + "    <title>新建HTML页面</title>\n"
				+ "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" + "  </head>\n"
				+ "  <body>This is a HTML page.</body>\n" + "</html>";
		FileAndString.string2File(contents, file);
	}

	void writeJavaFile(File file, String pag) {
		pag = pag.replace("/" + StudioConfig.PHANTOM_PROJECT_NAME + "/src/", "").replace("/", ".");
		String contents = "package " + pag + ";\n\n";
		contents += "public class " + file.getName().substring(0, file.getName().lastIndexOf(".")) + "{\n\n}";
		FileAndString.string2File(contents, file);
	}

}
