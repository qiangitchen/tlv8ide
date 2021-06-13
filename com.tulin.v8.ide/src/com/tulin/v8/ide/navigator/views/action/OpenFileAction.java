package com.tulin.v8.ide.navigator.views.action;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.internal.filesystem.local.LocalFile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

import com.tulin.v8.ide.StudioPlugin;
import com.tulin.v8.ide.Sys;
import com.tulin.v8.ide.navigator.views.job.OpenTableViewEditorJob;
import com.tulin.v8.ide.ui.editors.fn.FnEditor;
import com.tulin.v8.ide.ui.editors.function.FunctionEditor;
import com.tulin.v8.ide.ui.editors.page.WebPageEditor;
import com.tulin.v8.ide.ui.editors.process.ProcessEditor;
import com.tulin.v8.ide.ui.editors.process.ProcessEditorInput;
import com.tulin.v8.ide.ui.internal.FlowDraw;
import com.tulin.v8.ide.utils.StudioUtil;

import zigen.plugin.db.core.DBType;
import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.ITable;
import zigen.plugin.db.ui.internal.OracleSequence;
import zigen.plugin.db.ui.internal.OracleSource;
import zigen.plugin.db.ui.internal.Schema;
import zigen.plugin.db.ui.internal.TreeNode;
import zigen.plugin.db.ui.jobs.ConnectDBJob;
import zigen.plugin.db.ui.jobs.OpenSourceEditorJob;
import zigen.plugin.db.ui.jobs.OracleSequeceSearchJob;
import zigen.plugin.db.ui.jobs.OracleSourceSearchJob;
import zigen.plugin.db.ui.jobs.RefreshFolderJob;
import zigen.plugin.db.ui.jobs.TableTypeSearchJob;

@SuppressWarnings("restriction")
public class OpenFileAction extends Action implements Runnable {
	TreeViewer viewer;
	private boolean showDialog = false;

	public OpenFileAction(TreeViewer viewer) {
		this.viewer = viewer;
		setText(Messages.getString("View.Action.openfile.5"));
		setText(Messages.getString("View.Action.openfile.6"));
		setImageDescriptor(ImageDescriptor.createFromImage(PlatformUI
				.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_OBJ_FILE)));
	}

	public void run() {
		openFile();
	}

	private void openFile() {
		ISelection selection = viewer.getSelection();
		StudioPlugin.setSelection(selection);
		Object element = ((StructuredSelection) selection).getFirstElement();
		if (element instanceof DataBase) {
			DataBase db = (DataBase) element;
			if (!db.isExpanded()) {
				db.setConnected(true);
				db.setExpanded(true);
				ConnectDBJob job = new ConnectDBJob(viewer, db);
				job.setPriority(ConnectDBJob.SHORT);
				job.setUser(false);
				job.setSystem(false);
				job.schedule();
			} else {
				changeExpandedState(viewer, (TreeNode) element);
			}
		} else if (element instanceof ITable) {
			// 打开表&视图编辑器
			OpenTableViewEditorJob job = new OpenTableViewEditorJob(viewer,
					(ITable) element);
			job.setPriority(OpenTableViewEditorJob.SHORT);
			job.setUser(showDialog);
			job.schedule();
		} else if (element instanceof OracleSource
				|| element instanceof OracleSequence) {
			OpenSourceEditorJob job = new OpenSourceEditorJob(viewer);
			job.setPriority(OpenSourceEditorJob.SHORT);
			job.setUser(showDialog);
			job.schedule();
		} else if (element instanceof FlowDraw) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			FlowDraw treeobj = (FlowDraw) obj;
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			try {
				try {
					ProcessEditorInput localFileEditorInput2 = new ProcessEditorInput(
							treeobj.getElement());
					IDE.openEditor(page, localFileEditorInput2,
							ProcessEditor.ID);
				} catch (Exception e) {
					Sys.packErrMsg(e.toString());
				}
			} catch (Exception e) {
				Sys.packErrMsg(e.toString());
				showMessage(Messages.getString("View.Action.openfile.3")
						+ obj.toString() + "<path:" + treeobj.getPath()
						+ Messages.getString("View.Action.openfile.4")
						+ e.toString());
			}
		} else if (element instanceof TreeNode) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			TreeNode treeobj = (TreeNode) obj;
			String containerName = treeobj.getPath();
			if (containerName != null && !"".equals(containerName)) {
				File fle = new File(containerName);
				if (fle.isFile()) {
					try {
						IFile iFile = StudioPlugin
								.getWorkspace()
								.getRoot()
								.getFileForLocation(
										new Path(fle.getAbsolutePath()));
						String editorid = getEditorID(containerName,
								treeobj.getBiz());
						if (iFile != null && iFile.exists()) {
							try {
								iFile.refreshLocal(0, null);
							} catch (CoreException localCoreException1) {
								localCoreException1.printStackTrace();
							}
							if (editorid != null) {
								try {
									FileEditorInput localFileEditorInput2 = new FileEditorInput(
											iFile);
									IDE.openEditor(page, localFileEditorInput2,
											editorid);
								} catch (Exception e) {
									Sys.packErrMsg(e.toString());
								}
							} else {
								try {
									IDE.openEditor(page, iFile);
								} catch (Exception e) {
									Sys.packErrMsg(e.toString());
								}
							}
						} else {
							if (editorid != null) {
								LocalFile localLocalFile = new LocalFile(fle);
								FileStoreEditorInput localFileStoreEditorInput = new FileStoreEditorInput(
										localLocalFile);
								IDE.openEditor(page, localFileStoreEditorInput,
										editorid);
							} else {
								IFileStore fileStore = EFS.getLocalFileSystem()
										.getStore(new Path(containerName));
								IDE.openEditorOnFileStore(page, fileStore);
							}
						}
					} catch (Exception e) {
						Sys.packErrMsg(e.toString());
						showMessage(Messages
								.getString("View.Action.openfile.3")
								+ obj.toString()
								+ "<path:"
								+ treeobj.getPath()
								+ Messages.getString("View.Action.openfile.4")
								+ e.toString());
					}
				} else {
					// 未知文件不做处理
				}
			}
			changeExpandedState(viewer, (TreeNode) element);
		}
	}

	private String getEditorID(String containerName, String bizName) {
		if (containerName.toLowerCase().endsWith(".fun.xml")) {
			return FunctionEditor.ID;
		} else if (containerName.toLowerCase().endsWith(".fn.xml")) {
			return FnEditor.ID;
		} else if (StudioUtil.isTuLinPage(containerName)
				&& !"mobileUI".equals(bizName)) {
			return WebPageEditor.ID;
		}
		return null;
	}

	private void changeExpandedState(TreeViewer viewer, TreeNode element) {
		if (!viewer.getExpandedState(element)) {
			viewer.expandToLevel(element, 1);
			if (element instanceof Schema) {
				Schema schema = (Schema) element;
				if (!schema.isExpanded()) {
					schema.setExpanded(true);
					TableTypeSearchJob job = new TableTypeSearchJob(viewer,
							schema);
					job.setPriority(TableTypeSearchJob.SHORT);
					job.setUser(showDialog);
					job.schedule();
				}
			} else if (element instanceof Folder) {
				Folder folder = (Folder) element;
				if (!folder.isExpanded()) {
					folder.setExpanded(true);
					Schema schema = folder.getSchema();
					if (schema != null) {
						switch (DBType.getType(schema.getDbConfig())) {
						case DBType.DB_TYPE_ORACLE:
							if (schema != null) {
								if ("TABLE".equals(folder.getName())) {
									return;
								} else if ("SEQUENCE".equals(folder.getName())) { //$NON-NLS-1$
									OracleSequeceSearchJob job = new OracleSequeceSearchJob(
											viewer, folder);
									job.setPriority(OracleSequeceSearchJob.SHORT);
									job.setUser(showDialog);
									job.schedule();
									return;
								} else if ("VIEW".equals(folder.getName())) { //$NON-NLS-1$
									RefreshFolderJob job = new RefreshFolderJob(
											viewer, folder);
									job.setPriority(OracleSequeceSearchJob.SHORT);
									job.setUser(showDialog);
									job.schedule();
									return;
								} else {
									String[] sTypes = schema.getSourceType();
									if (sTypes != null) {
										for (int i = 0; i < sTypes.length; i++) {
											String stype = sTypes[i];
											if (stype.equals(folder.getName())) {
												OracleSourceSearchJob job = new OracleSourceSearchJob(
														viewer, folder);
												job.setPriority(OracleSourceSearchJob.SHORT);
												job.setUser(showDialog);
												job.schedule();
												return;
											}
										}
									}
								}
							}
						default:
							RefreshFolderJob job = new RefreshFolderJob(viewer,
									folder);
							job.setPriority(RefreshFolderJob.SHORT);
							job.setUser(showDialog);
							job.schedule();
							break;
						}
					}
				}
			}
		} else {
			viewer.collapseToLevel(element, 1);
		}
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				StudioPlugin.getResourceString("perspective.title.0"),
				message);
	}
}
