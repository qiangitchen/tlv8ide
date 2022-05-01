package com.tulin.v8.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.resource.ImageDescriptor;
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

	static IProject project = null;

	public static IProject getCurrentProject() {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			public void run() {
				// 1.根据当前编辑器获取工程
				IEditorPart part = getActiveEditor();
				if (part != null) {
					Object object = part.getEditorInput().getAdapter(IFile.class);
					if (object != null) {
						project = ((IFile) object).getProject();
					}
				}
				if (project == null) {
					// 没有单开编辑器时 获取当前选中的项目
					ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow()
							.getSelectionService();
					ISelection selection = selectionService.getSelection();
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
						} else if (element instanceof EditPart) {
							IFile file = (IFile) ((DefaultEditDomain) ((EditPart) element).getViewer().getEditDomain())
									.getEditorPart().getEditorInput().getAdapter(IFile.class);
							project = file.getProject();
						}
					}
				}
			}
		});
		if (project == null) {
			project = getProject("tlv8");
		}
		return project;
	}

	public static IProject getProject(String name) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
	}

	public static String getCurrentProjectName() {
		IProject project = getCurrentProject();
		return project.getName();
	}

	public static IResource getCurrentProjectWebFolder() {
		IProject project = getCurrentProject();
		IResource resource = project.findMember("WebContent");
		if (resource == null) {
			resource = project.findMember("WebRoot");
		}
		if (resource == null) {
			resource = project.findMember("src/main/webapp");
		}
		return resource;
	}

	public static String getCurrentProjectWebFolderName() {
		return getCurrentProjectWebFolder().getName();
	}

	public static String getCurrentProjectWebFolderPath() {
		return getCurrentProjectWebFolder().getLocation().toFile().getAbsolutePath();
	}
}
