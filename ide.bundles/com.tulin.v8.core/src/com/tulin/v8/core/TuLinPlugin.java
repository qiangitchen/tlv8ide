package com.tulin.v8.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
//import org.eclipse.gef.DefaultEditDomain;
//import org.eclipse.gef.EditPart;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

@SuppressWarnings("restriction")
public class TuLinPlugin extends AbstractUIPlugin {
	private static TuLinPlugin plugin;

	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static TuLinPlugin getDefault() {
		return plugin;
	}

	public Image getImage(String path) {
		Image image = getImageRegistry().get(path);
		if (image == null) {
			ImageDescriptor descriptor = getImageDescriptor(path);
			if (descriptor != null) {
				getImageRegistry().put(path, image = descriptor.createImage());
			}
		}
		return image;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(getPluginId(), path);
	}

	public static Image getIcon(String imgname) {
		return getDefault().getImage("/icons/" + imgname);
	}

	public static Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	public IWorkbenchPage getPage() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		return window.getActivePage();
	}

	public static IEditorPart getActiveEditor() {
		IEditorPart editor = getDefault().getPage().getActiveEditor();
		return editor;
	}

	public static IWorkbenchPage getActivePage() {
		return getDefault().internalGetActivePage();
	}

	private IWorkbenchPage internalGetActivePage() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return null;
		return window.getActivePage();
	}

	static ISelection selection = null;

	public static void setSelection(ISelection selection) {
		TuLinPlugin.selection = selection;
	}

	public static ISelection getSelection() {
		if (TuLinPlugin.selection != null) {
			return TuLinPlugin.selection;
		}
		ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
		ISelection selection = selectionService.getSelection();
		return selection;
	}

	private static IProject project = null;

	/**
	 * 获取当前项目
	 * 
	 * @return IProject
	 */
	public static IProject getCurrentProject() {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject();
		if (project != null) {
			return project;
		}
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			public void run() {
				ISelection selection = getSelection();
				if (selection != null) {
					if (selection instanceof IStructuredSelection) {
						Object element = ((IStructuredSelection) selection).getFirstElement();
						if (element instanceof IResource) {
							project = ((IResource) element).getProject();
						} else if (element instanceof PackageFragmentRootContainer) {
							IJavaProject jProject = ((PackageFragmentRootContainer) element).getJavaProject();
							project = jProject.getProject();
						} else if (element instanceof IJavaElement) {
							IJavaProject jProject = ((IJavaElement) element).getJavaProject();
							project = jProject.getProject();
						} else if (element instanceof MultiPageEditorPart) {
							MultiPageEditorPart meditor = (MultiPageEditorPart) element;
							if (meditor.getSelectedPage() instanceof IEditorPart) {
								IEditorPart part = (IEditorPart) meditor.getSelectedPage();
								Object object = part.getEditorInput().getAdapter(IFile.class);
								if (object != null) {
									project = ((IFile) object).getProject();
								}
							}
						} else if (element instanceof IEditorPart) {
							IEditorPart part = (IEditorPart) element;
							Object object = part.getEditorInput().getAdapter(IFile.class);
							if (object != null) {
								project = ((IFile) object).getProject();
							}
						} /*
							 * else if (element instanceof EditPart) { IFile file = (IFile)
							 * ((DefaultEditDomain) ((EditPart) element).getViewer().getEditDomain())
							 * .getEditorPart().getEditorInput().getAdapter(IFile.class); project =
							 * file.getProject(); }
							 */
					} else if (selection instanceof TextSelection) {
						TextSelection sel = (TextSelection) selection;
						System.out.println(sel);
					}
				}
				if (project == null) {
					// 1.根据当前编辑器获取工程
					IEditorPart part = getActiveEditor();
					if (part != null) {
						IFile ifile = part.getEditorInput().getAdapter(IFile.class);
						if (ifile != null) {
							project = ifile.getProject();
						} else {
							project = null;
						}
					} else {
						project = null;
					}
				}
			}
		});
		if (project == null) {
			project = getProject("tlv8");
		}
		if (project == null || !project.exists()) {
			project = getProject("tlv8-main");
		}
		if (project == null || !project.exists()) {
			project = getProject("tlv8-v3-main");
		}
		if (project == null || !project.exists()) {
			project = getProject("tlv8-admin");
		}
		if (project == null || !project.exists()) {
			project = getProject("tlv8-common");
		}
		if (project == null || !project.exists()) {
			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			if (projects.length > 0) {
				project = projects[0];
			}
		}
		return project;
	}

	/**
	 * 获取指定名称的项目
	 */
	public static IProject getProject(String name) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
	}

	/**
	 * 获取当前（选中的）项目
	 */
	public static String getCurrentProjectName() {
		IProject project = getCurrentProject();
		return project.getName();
	}

	/**
	 * 获取当前项目的web目录
	 */
	public static IResource getCurrentProjectWebFolder() {
		IProject project = getCurrentProject();
		return getProjectWebFolder(project);
	}

	/**
	 * 获取指定项目的Web目录
	 * 
	 * @param name
	 * @return IResource
	 */
	public static IResource getProjectWebFolder(String name) {
		IProject project = getProject(name);
		return getProjectWebFolder(project);
	}

	/**
	 * 获取项目的Web页面位置
	 * 
	 * @param project
	 * @return IResource
	 */
	public static IResource getProjectWebFolder(IProject project) {
		IResource resource = project.getFolder("WebContent");
		if (resource == null || !resource.exists()) {
			resource = project.getFolder("WebRoot");
		}
		if (resource == null || !resource.exists()) {
			resource = project.findMember("src/main/webapp");
		}
		if (resource == null || !resource.exists()) {
			resource = project.findMember("src/main/resources/static");
		}
		return resource;
	}

	/**
	 * 获取当前项目web目录的名称
	 */
	public static String getCurrentProjectWebFolderName() {
		return getCurrentProjectWebFolder().getName();
	}

	/**
	 * 获取当前项目的位置
	 */
	public static String getCurrentProjectWebFolderPath() {
		return getCurrentProjectWebFolder().getLocation().toFile().getAbsolutePath();
	}

	/**
	 * 获取项目的位置
	 */
	public static String getProjectWebFolderPath(IProject project) {
		return getProjectWebFolder(project).getLocation().toFile().getAbsolutePath();
	}

	/**
	 * 获取项目字符编码
	 * 
	 * @param project
	 * @return String
	 */
	public static String getProjectCharset(IProject project) {
		try {
			String charset = project.getDefaultCharset();
			if (charset.equals("MS932")) {
				charset = "Windows-31J";
			}
			return charset;
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * 获取IFile所属项目的字符编码
	 * 
	 * @param ifile
	 * @return String
	 */
	public static String getIFileCharset(IFile ifile) {
		return getProjectCharset(ifile.getProject());
	}

	/**
	 * 获取当前项目的字符编码
	 * 
	 * @return String
	 */
	public static String getCurrentProjectCharset() {
		return getProjectCharset(getCurrentProject());
	}

	/**
	 * 查找指定（模糊）名称的项目-返回查找到的第一个项目，没有则返回null
	 */
	public static IProject findProject(String... names) {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			boolean ishv = true;
			for (String name : names) {
				if (!project.getName().contains(name)) {
					ishv = false;
				}
			}
			if (ishv) {
				return project;
			}
		}
		return null;
	}
}
