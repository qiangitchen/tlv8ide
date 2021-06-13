package com.tulin.v8.ide;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IJsGlobalScopeContainer;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.JsGlobalScopeContainerInitializer;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths.BuildPathSupport;
import org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths.CPListElement;
import org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths.CPUserLibraryElement;

import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.ui.jsLibraryDoc.JsXDocParser;
import com.tulin.v8.ide.utils.FileHelper;
import com.tulin.v8.ide.utils.StudioConfig;

@SuppressWarnings({ "restriction", "rawtypes", "unchecked" })
public class StudioJsLibraryInit {
	public static String STUDIO_PATH = CommonUtil.rebuildFilePath(StudioConfig
			.getStudioPath());
	public static String STUDIO_CONFIG_JSLIBPATH = CommonUtil
			.rebuildFilePath(StudioConfig.getStudioAppRootPath()) + "/jsdoc/js";
	public static IJavaScriptProject fDummyProject;
	public static String UI_LIBRARY_NAME = "uiJsLib";
	public static String MOBILEUI_LIBRARY_NAME = "userUiJsLib";
	public static String UI_CONFIGXML_NAME = "config.xml";
	public static String MOBILEUI_CONFIGXML_NAME = "mconfig.xml";
	public static CPUserLibraryElement currentUILibraryElement;
	public static CPUserLibraryElement currentMobileUILibraryElement;
	public static String UI_JSFILE_NAME = "uiJsLibrary.js";
	public static String MOBILEUI_JSFILE_NAME = "userUiJsLibrary.js";

	public static void config(boolean paramBoolean) {
		try {
			try {
				PlatformUI.getWorkbench().getProgressService()
						.run(true, true, new b(paramBoolean));
			} catch (InterruptedException localInterruptedException) {
				localInterruptedException.printStackTrace();
			}
		} catch (InvocationTargetException localInvocationTargetException) {
			localInvocationTargetException.printStackTrace();
		}
	}

	public static void a(IProgressMonitor paramIProgressMonitor,
			boolean paramBoolean) {
		b(paramIProgressMonitor, paramBoolean);
	}

	private static void b(IProgressMonitor paramIProgressMonitor,
			boolean paramBoolean) {
		fDummyProject = createPlaceholderProject();
		c(paramIProgressMonitor, paramBoolean);
		// d(paramIProgressMonitor, paramBoolean);
	}

	private static void c(IProgressMonitor paramIProgressMonitor,
			boolean paramBoolean) {
		File localFile1 = new File(STUDIO_CONFIG_JSLIBPATH + "/"
				+ UI_JSFILE_NAME);
		long l = localFile1.lastModified();
		int i = 0;
		ArrayList localArrayList1 = new ArrayList();
		ArrayList localArrayList2 = new ArrayList();
		String str = STUDIO_CONFIG_JSLIBPATH + "/";
		File localFile2 = new File(str);
		File[] arrayOfFile1 = localFile2.listFiles();
		if (arrayOfFile1 != null) {
			for (File localObject2 : arrayOfFile1) {
				if ((localObject2.isFile())
						&& (localObject2.getName().endsWith(".xdoc"))) {
					if (localObject2.lastModified() > l)
						i = 1;
					localArrayList1.add(localObject2);
				} else {
					if ((!localObject2.isFile())
							|| (!localObject2.getName().endsWith(".js"))
							|| (localObject2.getName().equals(UI_JSFILE_NAME))
							|| (localObject2.getName()
									.equals(MOBILEUI_JSFILE_NAME)))
						continue;
					if (((File) localObject2).lastModified() > l)
						i = 1;
					localArrayList2.add(localObject2);
				}
			}
		}
		String path = STUDIO_CONFIG_JSLIBPATH + "/tulin-library/UI";
		File localFile3 = new File(path);
		if (!localFile3.exists())
			localFile3.mkdirs();
		File[] arrayOfFile2 = localFile3.listFiles();
		if (arrayOfFile2 != null)
			for (File localObject3 : arrayOfFile2) {
				if (((File) localObject3).isFile()
						&& ((File) localObject3).getName().endsWith(".xdoc")) {
					if (((File) localObject3).lastModified() > l)
						i = 1;
					localArrayList1.add(localObject3);
				} else {
					if ((!localObject3.isFile())
							|| (!localObject3.getName().endsWith(".js")))
						continue;
					if (((File) localObject3).lastModified() > l)
						i = 1;
					localArrayList2.add(localObject3);
				}
			}
		int m = 1;
		String[] arrayOfString = JavaScriptCore.getUserLibraryNames();
		for (int j = 0; j < arrayOfString.length; j++) {
			if (!arrayOfString[j].equals(UI_LIBRARY_NAME))
				continue;
			IPath localObject3 = new Path("org.eclipse.wst.jsdt.USER_LIBRARY")
					.append(arrayOfString[j]);
			try {
				IJsGlobalScopeContainer localIJsGlobalScopeContainer = JavaScriptCore
						.getJsGlobalScopeContainer((IPath) localObject3,
								fDummyProject);
				currentUILibraryElement = new CPUserLibraryElement(
						arrayOfString[j], localIJsGlobalScopeContainer,
						fDummyProject);
				CPListElement[] arrayOfCPListElement1 = currentUILibraryElement
						.getChildren();
				if ((arrayOfCPListElement1.length > 0)
						&& (arrayOfCPListElement1[0].getPath().toString()
								.startsWith(STUDIO_CONFIG_JSLIBPATH)))
					m = 0;
			} catch (JavaScriptModelException localJavaScriptModelException1) {
				JavaScriptPlugin.log(localJavaScriptModelException1);
			}
		}
		if ((!paramBoolean) && (i == 0) && (m == 0))
			return;
		Document localDocument = DocumentHelper.createDocument();
		localDocument.addElement("config");
		Object localObject3 = new StringBuffer();
		ArrayList localArrayList3 = new ArrayList();
		for (int i2 = 0; i2 < localArrayList1.size(); i2++) {
			Object localObject4 = CommonUtil
					.rebuildFilePath(((File) localArrayList1.get(i2))
							.getAbsolutePath());
			localDocument
					.getRootElement()
					.addElement("doc")
					.addAttribute("name",
							((File) localArrayList1.get(i2)).getName())
					.addAttribute("source-path", (String) localObject4);
			localArrayList3.addAll(JsXDocParser.parse(FileHelper.readFileAsStr(
					(String) localObject4, "\n", false, "utf-8")));
		}
		if (localArrayList3.size() > 0) {
			((StringBuffer) localObject3).append(
					JsXDocParser.createXDocContext(localArrayList3)).append(
					"\n");
		}
		for (int i2 = 0; i2 < localArrayList2.size(); i2++) {
			Object localObject4 = CommonUtil
					.rebuildFilePath(((File) localArrayList2.get(i2))
							.getAbsolutePath());
			localDocument
					.getRootElement()
					.addElement("js")
					.addAttribute("name",
							((File) localArrayList2.get(i2)).getName())
					.addAttribute("source-path", (String) localObject4);
			String localObject5 = FileHelper.readFileAsStr(
					(String) localObject4, "\n", false, "utf-8");
			if (((String) localObject4).endsWith("form.js")) {
				localObject5 = "function Xforms(){};\nvar xforms = {};\n"
						+ localObject5;
			}
			// localObject5 = deleteFunctionContext(localObject5);
			((StringBuffer) localObject3).append(localObject5).append("\n");
		}
		if (currentUILibraryElement == null)
			try {
				IPath localIPath1 = new Path(
						"org.eclipse.wst.jsdt.USER_LIBRARY")
						.append(UI_LIBRARY_NAME);
				Object localObject4 = JavaScriptCore.getJsGlobalScopeContainer(
						localIPath1, fDummyProject);
				currentUILibraryElement = new CPUserLibraryElement(
						UI_LIBRARY_NAME,
						(IJsGlobalScopeContainer) localObject4, fDummyProject);
			} catch (JavaScriptModelException localJavaScriptModelException2) {
				localJavaScriptModelException2.printStackTrace();
			}
		CPListElement[] arrayOfCPListElement2 = currentUILibraryElement
				.getChildren();
		for (int i3 = 0; i3 < arrayOfCPListElement2.length; i3++)
			currentUILibraryElement.remove(arrayOfCPListElement2[i3]);
		updateJsLibrary(paramIProgressMonitor, currentUILibraryElement,
				fDummyProject);
		IPath localIPath2 = Path.fromOSString(STUDIO_CONFIG_JSLIBPATH);
		Object localObject5 = localIPath2.append(UI_JSFILE_NAME).makeAbsolute();
		CPListElement localCPListElement = new CPListElement(
				currentUILibraryElement, null, 1, (IPath) localObject5, null);
		localCPListElement.setAttribute("javadoc_location",
				BuildPathSupport.guessJavadocLocation(localCPListElement));
		currentUILibraryElement.add(localCPListElement);
		updateJsLibrary(paramIProgressMonitor, currentUILibraryElement,
				fDummyProject);
		FileHelper.writeFile(STUDIO_CONFIG_JSLIBPATH + "/" + UI_JSFILE_NAME,
				((StringBuffer) localObject3).toString(), "utf-8");
		FileHelper.writeformatXMLFile(STUDIO_CONFIG_JSLIBPATH + "/"
				+ UI_CONFIGXML_NAME, localDocument, "utf-8");
	}

	@SuppressWarnings("unused")
	private static void d(IProgressMonitor paramIProgressMonitor,
			boolean paramBoolean) {
		File localFile1 = new File(STUDIO_CONFIG_JSLIBPATH + "/"
				+ MOBILEUI_JSFILE_NAME);
		long l = localFile1.lastModified();
		int i = 0;
		ArrayList localArrayList1 = new ArrayList();
		String str = STUDIO_CONFIG_JSLIBPATH;
		File localFile2 = new File(str);
		File[] arrayOfFile1 = localFile2.listFiles();
		if (arrayOfFile1 != null)
			for (File localObject2 : arrayOfFile1) {
				if ((!localObject2.isFile())
						|| (!localObject2.getName().endsWith(".xdoc")))
					continue;
				if (localObject2.lastModified() > l)
					i = 1;
				localArrayList1.add(localObject2);
			}
		String p = STUDIO_CONFIG_JSLIBPATH + "/tulin-library/userUI";
		File localFile3 = new File(p);
		if (!localFile3.exists())
			localFile3.mkdirs();
		File[] arrayOfFile2 = localFile3.listFiles();
		if (arrayOfFile2 != null)
			for (File localObject3 : arrayOfFile2) {
				if ((!((File) localObject3).isFile())
						|| (!((File) localObject3).getName().endsWith(".xdoc")))
					continue;
				if (((File) localObject3).lastModified() > l)
					i = 1;
				localArrayList1.add(localObject3);
			}
		int m;
		String[] arrayOfString = JavaScriptCore.getUserLibraryNames();
		for (m = 0; m < arrayOfString.length; m++) {
			if (!arrayOfString[m].equals(MOBILEUI_LIBRARY_NAME))
				continue;
			IPath localObject3 = new Path("org.eclipse.wst.jsdt.USER_LIBRARY")
					.append(arrayOfString[m]);
			try {
				IJsGlobalScopeContainer localIJsGlobalScopeContainer = JavaScriptCore
						.getJsGlobalScopeContainer((IPath) localObject3,
								fDummyProject);
				currentMobileUILibraryElement = new CPUserLibraryElement(
						arrayOfString[m], localIJsGlobalScopeContainer,
						fDummyProject);
				CPListElement[] arrayOfCPListElement1 = currentMobileUILibraryElement
						.getChildren();
				if ((arrayOfCPListElement1.length > 0)
						&& (arrayOfCPListElement1[0].getPath().toString()
								.startsWith(STUDIO_CONFIG_JSLIBPATH)))
					m = 0;
			} catch (JavaScriptModelException localJavaScriptModelException1) {
				JavaScriptPlugin.log(localJavaScriptModelException1);
			}
		}
		if ((!paramBoolean) && (i == 0) && (m == 0))
			return;
		Document localDocument = DocumentHelper.createDocument();
		localDocument.addElement("config");
		Object localObject3 = new StringBuffer();
		ArrayList localArrayList2 = new ArrayList();
		Object localObject4;
		for (int i2 = 0; i2 < localArrayList1.size(); i2++) {
			localObject4 = CommonUtil.rebuildFilePath(((File) localArrayList1
					.get(i2)).getAbsolutePath());
			localDocument
					.getRootElement()
					.addElement("doc")
					.addAttribute("name",
							((File) localArrayList1.get(i2)).getName())
					.addAttribute("source-path", (String) localObject4);
			localArrayList2.addAll(JsXDocParser.parse(FileHelper.readFileAsStr(
					(String) localObject4, "\n", false, "utf-8")));
		}
		if (localArrayList2.size() > 0)
			((StringBuffer) localObject3).append(
					JsXDocParser.createXDocContext(localArrayList2)).append(
					"\n");
		if (currentMobileUILibraryElement == null)
			try {
				IPath localIPath1 = new Path(
						"org.eclipse.wst.jsdt.USER_LIBRARY")
						.append(UI_LIBRARY_NAME);
				localObject4 = JavaScriptCore.getJsGlobalScopeContainer(
						localIPath1, fDummyProject);
				currentMobileUILibraryElement = new CPUserLibraryElement(
						MOBILEUI_LIBRARY_NAME,
						(IJsGlobalScopeContainer) localObject4, fDummyProject);
			} catch (JavaScriptModelException localJavaScriptModelException2) {
				localJavaScriptModelException2.printStackTrace();
			}
		CPListElement[] arrayOfCPListElement2 = currentMobileUILibraryElement
				.getChildren();
		for (int i3 = 0; i3 < arrayOfCPListElement2.length; i3++)
			currentMobileUILibraryElement.remove(arrayOfCPListElement2[i3]);
		updateJsLibrary(paramIProgressMonitor, currentMobileUILibraryElement,
				fDummyProject);
		IPath localIPath2 = Path.fromOSString(STUDIO_CONFIG_JSLIBPATH);
		IPath localIPath3 = localIPath2.append(MOBILEUI_JSFILE_NAME)
				.makeAbsolute();
		CPListElement localCPListElement = new CPListElement(
				currentMobileUILibraryElement, null, 1, localIPath3, null);
		localCPListElement.setAttribute("javadoc_location",
				BuildPathSupport.guessJavadocLocation(localCPListElement));
		currentMobileUILibraryElement.add(localCPListElement);
		updateJsLibrary(paramIProgressMonitor, currentMobileUILibraryElement,
				fDummyProject);
		FileHelper.writeFile(STUDIO_CONFIG_JSLIBPATH + "/"
				+ MOBILEUI_JSFILE_NAME,
				((StringBuffer) localObject3).toString(), "utf-8");
		FileHelper.writeformatXMLFile(STUDIO_CONFIG_JSLIBPATH + "/"
				+ MOBILEUI_CONFIGXML_NAME, localDocument, "utf-8");
	}

	public static void updateJsLibrary(IProgressMonitor paramIProgressMonitor,
			CPUserLibraryElement paramCPUserLibraryElement,
			IJavaScriptProject paramIJavaScriptProject) {
		try {
			JsGlobalScopeContainerInitializer localJsGlobalScopeContainerInitializer = JavaScriptCore
					.getJsGlobalScopeContainerInitializer("org.eclipse.wst.jsdt.USER_LIBRARY");
			IJsGlobalScopeContainer localIJsGlobalScopeContainer = paramCPUserLibraryElement
					.getUpdatedContainer();
			try {
				localJsGlobalScopeContainerInitializer
						.requestJsGlobalScopeContainerUpdate(
								paramCPUserLibraryElement.getPath(),
								paramIJavaScriptProject,
								localIJsGlobalScopeContainer);
			} catch (CoreException localCoreException) {
				localCoreException.printStackTrace();
			}
			paramIProgressMonitor.worked(1);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	public static String deleteFunctionContext(String paramString) {
		try {
			StringBuffer localStringBuffer = new StringBuffer();
			Pattern localPattern = Pattern
					.compile("[=|:]{0,1}[ ]*function[^{}]+\\{");
			Matcher localMatcher = localPattern.matcher(paramString);
			int i = 0;
			while (localMatcher.find()) {
				int j = localMatcher.end();
				if (i > j)
					continue;
				localStringBuffer.append(paramString.substring(i, j));
				int k = 0;
				int m = 0;
				for (int n = j; n < paramString.length(); n++) {
					int i1 = paramString.charAt(n);
					if (i1 == 123) {
						if ((m == 124) || (m == 94))
							continue;
						k++;
					} else if (i1 == 125) {
						if ((m == 124) || (m == 94) || (m == 40))
							continue;
						if (k == 0) {
							i = n;
							break;
						}
						k--;
					}
					m = i1;
				}
			}
			localStringBuffer.append(paramString.substring(i,
					paramString.length()));
			return localStringBuffer.toString();
		} catch (Exception localException) {
		}
		return "";
	}

	public static IJavaScriptProject createPlaceholderProject() {
		String str = "####jsdtinternal";
		IWorkspaceRoot localIWorkspaceRoot = ResourcesPlugin.getWorkspace()
				.getRoot();
		while (true) {
			IProject localIProject = localIWorkspaceRoot.getProject(str);
			if (!localIProject.exists())
				return JavaScriptCore.create(localIProject);
			str = str + '1';
		}
	}
}
