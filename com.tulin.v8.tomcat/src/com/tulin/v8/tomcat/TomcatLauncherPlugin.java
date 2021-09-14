package com.tulin.v8.tomcat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.tulin.v8.tomcat.editors.ProjectListElement;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TomcatLauncherPlugin extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.tulin.v8.tomcat";

	private static TomcatLauncherPlugin plugin;
	
	private ResourceBundle resourceBundle;
	
	public static String NATURE_ID = PLUGIN_ID + ".tomcatnature";

	public static final String TOMCAT_PREF_HOME_KEY = "tomcatDir";
	public static final String TOMCAT_PREF_BASE_KEY = "tomcatBase";
	public static final String TOMCAT_PREF_CONFIGFILE_KEY = "tomcatConfigFile";
	public static final String TOMCAT_PREF_SET_KEY = "tomcatSetting";
	public static final String TOMCAT_PREF_VERSION_KEY = "tomcatVersion";
	public static final String TOMCAT_PREF_JRE_KEY = "tomcatJRE";
	public static final String TOMCAT_PREF_JVM_PARAMETERS_KEY = "jvmParameters";
	public static final String TOMCAT_PREF_JVM_CLASSPATH_KEY = "jvmClasspath";
	public static final String TOMCAT_PREF_JVM_BOOTCLASSPATH_KEY = "jvmBootClasspath";
	public static final String TOMCAT_PREF_PROJECTSINCP_KEY = "projectsInCp";
	public static final String TOMCAT_PREF_PROJECTSINSOURCEPATH_KEY = "projectsInSourcePath";
	public static final String TOMCAT_PREF_COMPUTESOURCEPATH_KEY = "computeSourcePath";
	public static final String TOMCAT_PREF_DEBUGMODE_KEY = "tomcatDebugMode";
	public static final String TOMCAT_PREF_TARGETPERSPECTIVE = "targetPerspective";
	public static final String TOMCAT_PREF_SECURITYMANAGER = "enabledSecurityManager";
	public static final String TOMCAT_PREF_MANAGER_URL = "managerUrl";
	public static final String TOMCAT_PREF_MANAGER_USER = "managerUser";
	public static final String TOMCAT_PREF_MANAGER_PASSWORD = "managerPassword";
	public static final String TOMCAT_VERSION5 = "tomcatV5";
	public static final String TOMCAT_VERSION6 = "tomcatV6";
	public static final String TOMCAT_VERSION7 = "tomcatV7";
	public static final String TOMCAT_VERSION8 = "tomcatV8";
	public static final String TOMCAT_VERSION9 = "tomcatV9";
	public static final String TOMCAT_VERSION10 = "tomcatV10";
	public static final String TOMCAT_DEFAULTSET = "default";
	public static final String TOMCAT_MYSELFSET = "myself";
	public static final String TOMCAT_PREF_CONFMODE_KEY = "configMode";
	public static final String SERVERXML_MODE = "serverFile";
	public static final String CONTEXTFILES_MODE = "contextFiles";
	public static final String TOMCAT_PREF_CONTEXTSDIR_KEY = "contextsDir";

	public static final String TOMCAT_HOME_CLASSPATH_VARIABLE = "TOMCAT_HOME";

	public TomcatLauncherPlugin() {
		try {
			resourceBundle = ResourceBundle.getBundle("com.tulin.v8.tomcat.PluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		getWorkspace().addResourceChangeListener(new TomcatProjectChangeListener(), IResourceChangeEvent.PRE_DELETE);
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static TomcatLauncherPlugin getDefault() {
		return plugin;
	}

	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	public String getTomcatVersion() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		String result = pref.getString(TOMCAT_PREF_VERSION_KEY);
		if (result.equals(""))
			result = TOMCAT_VERSION6;
		return result;
	}

	public TomcatBootstrap getTomcatBootstrap() {
		TomcatBootstrap tomcatBootsrap = null;
		if (getTomcatVersion().equals(TOMCAT_VERSION5)) {
			tomcatBootsrap = new Tomcat5Bootstrap();
		}
		if (getTomcatVersion().equals(TOMCAT_VERSION6)) {
			tomcatBootsrap = new Tomcat6Bootstrap();
		}
		if (getTomcatVersion().equals(TOMCAT_VERSION7)) {
			tomcatBootsrap = new Tomcat7Bootstrap();
		}
		if (getTomcatVersion().equals(TOMCAT_VERSION8)) {
			tomcatBootsrap = new Tomcat8Bootstrap();
		}
		if (getTomcatVersion().equals(TOMCAT_VERSION9)) {
			tomcatBootsrap = new Tomcat9Bootstrap();
		}
		if (getTomcatVersion().equals(TOMCAT_VERSION10)) {
			tomcatBootsrap = new Tomcat10Bootstrap();
		}
		return tomcatBootsrap;
	}

	public List getProjectsInCP() {
		return readProjectsFromPreferenceStore(TOMCAT_PREF_PROJECTSINCP_KEY);
	}

	static List readProjectsFromPreferenceStore(String keyInPreferenceStore) {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		String stringList = pref.getString(keyInPreferenceStore);
		List projectsIdList = new ArrayList();
		StringTokenizer tokenizer = new StringTokenizer(stringList, ";");
		while (tokenizer.hasMoreElements()) {
			projectsIdList.add(tokenizer.nextToken());
		}
		return ProjectListElement.stringsToProjectsList(projectsIdList);

	}

	public List readProjectsInSourcePathFromPref() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		if (!(pref.contains(TOMCAT_PREF_PROJECTSINSOURCEPATH_KEY))) {
			pref.setValue(TOMCAT_PREF_COMPUTESOURCEPATH_KEY, true);
			return computeProjectsInSourcePath();
		} else {
			return readProjectsFromPreferenceStore(TOMCAT_PREF_PROJECTSINSOURCEPATH_KEY);
		}
	}

	private List computeProjectsInSourcePath() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] allProjects = root.getProjects();
		ArrayList tempList = new ArrayList(allProjects.length);
		ArrayList alreadyAdded = new ArrayList();
		for (int i = 0; i < allProjects.length; i++) {
			IProject project = allProjects[i];
			try {
				if ((project.isOpen()) && project.hasNature(JavaCore.NATURE_ID)) {
					IJavaProject javaProject = (IJavaProject) project.getNature(JavaCore.NATURE_ID);
					if (!alreadyAdded.contains(project)) {
						tempList.add(new ProjectListElement(javaProject.getProject()));
						alreadyAdded.add(project);
					}
				}
			} catch (CoreException e) {
				logException(e);
			}
		}
		return tempList;
	}

	public List getProjectsInSourcePath() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		boolean automaticallyComputed = pref.getBoolean(TOMCAT_PREF_COMPUTESOURCEPATH_KEY);

		if (automaticallyComputed) {
			return computeProjectsInSourcePath();
		} else {
			return readProjectsInSourcePathFromPref();
		}
	}

	public void setProjectsInCP(List projectsInCP) {
		saveProjectsToPreferenceStore(projectsInCP, TOMCAT_PREF_PROJECTSINCP_KEY);
	}

	public void setProjectsInSourcePath(List projectsInCP) {
		saveProjectsToPreferenceStore(projectsInCP, TOMCAT_PREF_PROJECTSINSOURCEPATH_KEY);
	}

	static void saveProjectsToPreferenceStore(List projectList, String keyInPreferenceStore) {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		StringBuffer buf = new StringBuffer();
		Iterator it = projectList.iterator();
		while (it.hasNext()) {
			ProjectListElement each = (ProjectListElement) it.next();
			buf.append(each.getID());
			buf.append(';');
		}
		pref.setValue(keyInPreferenceStore, buf.toString());
	}
	
	public Object getTomcatJRE() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		String result = pref.getString(TOMCAT_PREF_JRE_KEY);
		if (result.equals(""))
			result = JavaRuntime.getDefaultVMInstall().getId();
		return result;
	}
	
	public String getTomcatBase() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		return pref.getString(TOMCAT_PREF_BASE_KEY);
	}

	public String getTomcatDir() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		return pref.getString(TOMCAT_PREF_HOME_KEY);
	}
	
	public String getConfigMode() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		return pref.getString(TOMCAT_PREF_CONFMODE_KEY);
	}
	
	public String getConfigFile() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		return pref.getString(TOMCAT_PREF_CONFIGFILE_KEY);
	}

	public boolean isSecurityManagerEnabled() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		return pref.getBoolean(TOMCAT_PREF_SECURITYMANAGER);
	}

	public IPath getTomcatIPath() {
		IPath tomcatPath = getTomcatClasspathVariable();
		if (tomcatPath == null) {
			return new Path(getDefault().getTomcatDir());
		} else {
			return new Path(TOMCAT_HOME_CLASSPATH_VARIABLE);
		}
	}

	private IPath getTomcatClasspathVariable() {
		IPath tomcatPath = JavaCore.getClasspathVariable(TOMCAT_HOME_CLASSPATH_VARIABLE);
		if (tomcatPath == null) {
			this.initTomcatClasspathVariable();
			tomcatPath = JavaCore.getClasspathVariable(TOMCAT_HOME_CLASSPATH_VARIABLE);
		}
		return tomcatPath;
	}
	
	public void initTomcatClasspathVariable() {
		try {
			JavaCore.setClasspathVariable(TOMCAT_HOME_CLASSPATH_VARIABLE, new Path(getDefault().getTomcatDir()), null);
		} catch (JavaModelException e) {
			logException(e);
		}
	}

	public boolean isDebugMode() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		return !pref.getBoolean(TOMCAT_PREF_DEBUGMODE_KEY);
	}

	public String getJvmParamaters() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		return pref.getString(TOMCAT_PREF_JVM_PARAMETERS_KEY);
	}

	public String getJvmClasspath() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		return pref.getString(TOMCAT_PREF_JVM_CLASSPATH_KEY);
	}

	public String getJvmBootClasspath() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		return pref.getString(TOMCAT_PREF_JVM_BOOTCLASSPATH_KEY);
	}
	
	public static String getResourceString(String key) {
		ResourceBundle bundle = getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}
	
	public static Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
	
	static public boolean checkTomcatSettingsAndWarn() {
		if (!isTomcatConfigured()) {
			String msg = getResourceString("msg.noconfiguration");
			MessageDialog.openWarning(getShell(), "Tomcat", msg);
			return false;
		}
		return true;
	}
	
	static public boolean isTomcatConfigured() {
		return !(getDefault().getTomcatDir().equals(""));
	}
	
	public String getManagerAppUrl() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		return pref.getString(TOMCAT_PREF_MANAGER_URL);
	}

	public String getManagerAppUser() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		return pref.getString(TOMCAT_PREF_MANAGER_USER);
	}

	public String getManagerAppPassword() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		return pref.getString(TOMCAT_PREF_MANAGER_PASSWORD);
	}

	public String getContextsDir() {
		IPreferenceStore pref = getDefault().getPreferenceStore();
		return pref.getString(TOMCAT_PREF_CONTEXTSDIR_KEY);
	}

	private ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
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
	
	static public void log(String msg) {
		logDebug(msg);
	}
	
	static public void log(Exception ex) {
		logException(ex);
	}
}
