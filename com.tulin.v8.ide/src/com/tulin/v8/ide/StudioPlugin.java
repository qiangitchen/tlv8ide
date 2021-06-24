package com.tulin.v8.ide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
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
import org.eclipse.ui.services.IEvaluationService;
import org.osgi.framework.BundleContext;

import com.tulin.v8.ide.navigator.views.IStatusChangeListener;
import com.tulin.v8.ide.navigator.views.PluginSettingsManager;
import com.tulin.v8.ide.navigator.views.XmlController;
import com.tulin.v8.ide.utils.StudioConfig;

/**
 * The activator class controls the plug-in life cycle
 */
@SuppressWarnings({ "rawtypes", "unchecked", "deprecation", "restriction" })
public class StudioPlugin extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.tulin.v8.ide";
	// The shared instance
	private static StudioPlugin plugin;

	private ResourceBundle resourceBundle;

	private XmlController xmlController;

	public static final String TOMCAT_PREF_CONFIGFILE_KEY = "tomcatConfigFile";

	public static final String WORKSPACES_INITED_KEY = "workspaces_inited_key";

	/**
	 * The constructor
	 */
	public StudioPlugin() {
		try {
			resourceBundle = ResourceBundle.getBundle("com.tulin.v8.ide.StudioPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		xmlController = new XmlController(getStateLocation());
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		try {
			xmlController.save();
		} catch (Exception e) {

		}
		super.stop(context);
	}

	public static StudioPlugin getDefault() {
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
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public String getConfigFile() {
		AbstractUIPlugin plugin = (AbstractUIPlugin) Platform.getPlugin("com.tulin.v8.tomcat");
		IPreferenceStore pref = plugin.getPreferenceStore();
		return pref.getString(TOMCAT_PREF_CONFIGFILE_KEY);
	}

	/*
	 * 获取系统图标
	 */
	public static Image getSysIcon() {
		return getIcon("sample.gif");
	}

	/*
	 * 获取指定名称的icon图片
	 */
	public static Image getIcon(String imgname) {
		return StudioPlugin.getDefault().getImage("/icons/" + imgname);
	}

	public static IEvaluationContext getApplicationContext() {
		IEvaluationService es = (IEvaluationService) PlatformUI.getWorkbench().getService(IEvaluationService.class);
		return es == null ? null : es.getCurrentState();
	}

	public static IEvaluationContext getEmptyEvalContext() {
		IEvaluationContext c = new EvaluationContext(getApplicationContext(), Collections.EMPTY_LIST);
		c.setAllowPluginActivation(true);
		return c;
	}

	public static IEvaluationContext getEvalContext(Object selection) {
		IEvaluationContext c = new EvaluationContext(getApplicationContext(), selection);
		c.setAllowPluginActivation(true);
		return c;
	}

	public static IStatus createStatus(int severity, int aCode, String aMessage, Throwable exception) {
		return new Status(severity, PLUGIN_ID, aCode, aMessage != null ? aMessage : "No message.", exception);
	}

	public static IStatus createErrorStatus(int aCode, String aMessage, Throwable exception) {
		return createStatus(4, aCode, aMessage, exception);
	}

	/*
	 * 写测试日志
	 */
	public static void logDebug(String message) {
		ILog log = getDefault().getLog();
		IStatus status = new Status(IStatus.INFO, getPluginId(), 0, message, null);
		log.log(status);
	}

	/*
	 * 写错误日志
	 */
	public static void logError(String message) {
		ILog log = getDefault().getLog();
		IStatus status = new Status(IStatus.ERROR, getPluginId(), 0, message, null);
		log.log(status);
	}

	/*
	 * 写异常日志
	 */
	public static void logException(Throwable ex) {
		ILog log = getDefault().getLog();
		IStatus status = null;
		if (ex instanceof CoreException) {
			status = ((CoreException) ex).getStatus();
		} else {
			status = new Status(IStatus.ERROR, getPluginId(), 0, ex.toString(), ex);
		}
		log.log(status);
		ex.printStackTrace();
	}

	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/*
	 * 工作空间初始化
	 */
	public static boolean isWorkspacesInit() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		return !pref.getBoolean(WORKSPACES_INITED_KEY);
	}

	/*
	 * 标记工作空间初始化完成
	 */
	public static void setWorkspacesInited() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		pref.setValue(WORKSPACES_INITED_KEY, true);
	}

	public static void log(String string) {
		logDebug(string);
	}

	public static String getResourceString(String key) {
		ResourceBundle bundle = getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	private ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public static Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	public PluginSettingsManager getPluginSettingsManager() {
		return xmlController.getPluginSettingsManager();
	}

	private static List listeners = new ArrayList();

	public static void addStatusChangeListener(Object sampleView) {
		if (!listeners.contains(sampleView))
			listeners.add(sampleView);
	}

	public static void removeStatusChangeListener(IStatusChangeListener listener) {
		listeners.remove(listener);
	}

	public static void fireStatusChangeListener(Object obj, int status) {
		for (Iterator iter = listeners.iterator(); iter.hasNext();) {
			IStatusChangeListener element = (IStatusChangeListener) iter.next();
			if (element != null) {
				element.statusChanged(obj, status);
			}
		}

	}

	static ISelection selection;

	public static void setSelection(ISelection s) {
		selection = s;
	}

	public static ISelection getSelection() {
		return selection;
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

	public IDialogSettings getDialogSettingsSection(String name) {
		IDialogSettings dialogSettings = getDialogSettings();
		IDialogSettings section = dialogSettings.getSection(name);
		if (section == null) {
			section = dialogSettings.addNewSection(name);
		}
		return section;
	}

	public static String getWorkPath() {
		String currentPath = StudioConfig.getWorkspacesPath();
		return currentPath;
	}

	public static String getProjectPath() {
		String uiPath = getWorkPath() + "/" + StudioConfig.PHANTOM_PROJECT_NAME;
		return uiPath;
	}

	public static String getBIZPath() {
		String uiPath = getWorkPath() + "/" + StudioConfig.PHANTOM_PROJECT_NAME + "/src";
		return uiPath;
	}

	public static String getUIPath() {
		String uiPath = getWorkPath() + "/" + StudioConfig.PHANTOM_PROJECT_NAME + "/" + StudioConfig.PROJECT_WEB_FOLDER;
		return uiPath;
	}

	public static String getMobileUIPath() {
		String uiPath = getWorkPath() + "/" + StudioConfig.PHANTOM_PROJECT_NAME + "/" + StudioConfig.PROJECT_WEB_FOLDER
				+ "/mobileUI";
		return uiPath;
	}

	public static final String getVersion() {
		// if (isOldVersion()) {
		String versions = getPluginVersion();
		return versions.substring(0, versions.lastIndexOf("."));
		// } else {
		// Version version = getDefault().getBundle().getVersion();
		// return version.getMajor() + "." + version.getMinor() + "." +
		// version.getMicro();
		// }
	}

	public static final String getPluginVersion() {
		return (String) getDefault().getBundle().getHeaders().get("Bundle-Version");
		// if (isOldVersion()) {
		// return
		// (String)Platform.getBundle(getPluginId()).getHeaders().get("Bundle-Version");
		// } else {
		// Version version = getDefault().getBundle().getVersion();
		// String versionnm = version.getMajor() + "." + version.getMinor() +
		// "." + version.getMicro();
		// return versionnm + "." +
		// getDefault().getBundle().getVersion().getQualifier();
		// }
	}

	public static final String getPluginName() {
		return (String) Platform.getBundle(getPluginId()).getHeaders().get("Bundle-Name");
	}

	public static final String getPluginVendor() {
		return (String) Platform.getBundle(getPluginId()).getHeaders().get("Bundle-Vendor");
	}

	@Deprecated
	public static boolean isOldVersion() {
//		return Platform.getPluginRegistry() != null;
		return false;
	}

	public static void log(IStatus status) {
		ILog log = getDefault().getLog();
		log.log(status);
	}

	public static void log(String message, Throwable excp) {
		Status status = new Status(4, getPluginId(), message, excp);
		log(status);
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
		return project;
	}

	public static IProject getProject(String name) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
	}

	public static String getCurrentProjectName() {
		IProject project = StudioPlugin.getCurrentProject();
		return project.getName();
	}

	public static String getCurrentProjectWebFolder() {
		IProject project = StudioPlugin.getCurrentProject();
		String WEB_FOLDER = project.getFolder("WebContent").getName();
		if (WEB_FOLDER == null) {
			WEB_FOLDER = project.getFolder("WebRoot").getName();
		}
		return WEB_FOLDER;
	}

}
