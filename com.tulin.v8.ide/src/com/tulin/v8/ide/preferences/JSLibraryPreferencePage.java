package com.tulin.v8.ide.preferences;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IJsGlobalScopeContainer;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths.BuildPathSupport;
import org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths.CPListElement;
import org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths.CPUserLibraryElement;

import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.StudioJsLibraryInit;
import com.tulin.v8.ide.ui.jsLibraryDoc.JsXDocParser;
import com.tulin.v8.ide.utils.FileHelper;
import com.tulin.v8.ide.utils.StudioConfig;
import com.tulin.v8.ide.widgets.JSLibraryConfigComposite;
import com.tulin.v8.ide.widgets.JSLibraryEntity;

/*
 * JS库配置
 */
@SuppressWarnings({ "restriction", "rawtypes", "unchecked" })
public class JSLibraryPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private static String jdField_a_of_type_JavaLangString = "studio";
	private CPUserLibraryElement jdField_a_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement;
	private Document jdField_a_of_type_OrgDom4jDocument;
	private CPUserLibraryElement jdField_b_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement;
	private Document jdField_b_of_type_OrgDom4jDocument;
	JSLibraryConfigComposite jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite;
	private IJavaScriptProject jdField_a_of_type_OrgEclipseWstJsdtCoreIJavaScriptProject;

	public JSLibraryPreferencePage() {
		setTitle(Messages.getString("preferencePages.JSLibrary.1"));
		a();
	}

	void a() {
		this.jdField_a_of_type_OrgEclipseWstJsdtCoreIJavaScriptProject = StudioJsLibraryInit.createPlaceholderProject();
		String[] arrayOfString = JavaScriptCore.getUserLibraryNames();
		for (int i = 0; i < arrayOfString.length; i++) {
			if (!arrayOfString[i].equals(StudioJsLibraryInit.UI_LIBRARY_NAME))
				continue;
			Object localObject = new Path("org.eclipse.wst.jsdt.USER_LIBRARY").append(arrayOfString[i]);
			try {
				IJsGlobalScopeContainer localIJsGlobalScopeContainer1 = JavaScriptCore.getJsGlobalScopeContainer(
						(IPath) localObject, this.jdField_a_of_type_OrgEclipseWstJsdtCoreIJavaScriptProject);
				this.jdField_a_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement = new CPUserLibraryElement(
						arrayOfString[i], localIJsGlobalScopeContainer1,
						this.jdField_a_of_type_OrgEclipseWstJsdtCoreIJavaScriptProject);
			} catch (JavaScriptModelException localJavaScriptModelException3) {
				JavaScriptPlugin.log(localJavaScriptModelException3);
			}
		}
		if (this.jdField_a_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement == null)
			try {
				IPath localIPath1 = new Path("org.eclipse.wst.jsdt.USER_LIBRARY")
						.append(StudioJsLibraryInit.UI_LIBRARY_NAME);
				Object localObject = JavaScriptCore.getJsGlobalScopeContainer(localIPath1,
						this.jdField_a_of_type_OrgEclipseWstJsdtCoreIJavaScriptProject);
				this.jdField_a_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement = new CPUserLibraryElement(
						StudioJsLibraryInit.UI_LIBRARY_NAME, (IJsGlobalScopeContainer) localObject,
						this.jdField_a_of_type_OrgEclipseWstJsdtCoreIJavaScriptProject);
			} catch (JavaScriptModelException localJavaScriptModelException1) {
				localJavaScriptModelException1.printStackTrace();
			}
		for (int j = 0; j < arrayOfString.length; j++) {
			if (!arrayOfString[j].equals(StudioJsLibraryInit.MOBILEUI_LIBRARY_NAME))
				continue;
			Object localObject = new Path("org.eclipse.wst.jsdt.USER_LIBRARY").append(arrayOfString[j]);
			try {
				IJsGlobalScopeContainer localIJsGlobalScopeContainer2 = JavaScriptCore.getJsGlobalScopeContainer(
						(IPath) localObject, this.jdField_a_of_type_OrgEclipseWstJsdtCoreIJavaScriptProject);
				this.jdField_b_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement = new CPUserLibraryElement(
						arrayOfString[j], localIJsGlobalScopeContainer2,
						this.jdField_a_of_type_OrgEclipseWstJsdtCoreIJavaScriptProject);
			} catch (JavaScriptModelException localJavaScriptModelException4) {
				JavaScriptPlugin.log(localJavaScriptModelException4);
			}
		}
		if (this.jdField_b_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement == null)
			try {
				IPath localIPath2 = new Path("org.eclipse.wst.jsdt.USER_LIBRARY")
						.append(StudioJsLibraryInit.MOBILEUI_LIBRARY_NAME);
				Object localObject = JavaScriptCore.getJsGlobalScopeContainer(localIPath2,
						this.jdField_a_of_type_OrgEclipseWstJsdtCoreIJavaScriptProject);
				this.jdField_b_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement = new CPUserLibraryElement(
						StudioJsLibraryInit.MOBILEUI_LIBRARY_NAME, (IJsGlobalScopeContainer) localObject,
						this.jdField_a_of_type_OrgEclipseWstJsdtCoreIJavaScriptProject);
			} catch (JavaScriptModelException localJavaScriptModelException2) {
				localJavaScriptModelException2.printStackTrace();
			}
	}

	void b() {
		String str1 = StudioJsLibraryInit.STUDIO_PATH;
		String str2 = StudioJsLibraryInit.STUDIO_CONFIG_JSLIBPATH;
		this.jdField_a_of_type_OrgDom4jDocument = FileHelper
				.getXmlDocument(str2 + "/" + StudioJsLibraryInit.UI_CONFIGXML_NAME);
		List localList1 = this.jdField_a_of_type_OrgDom4jDocument.getRootElement().selectNodes("js");
		List localList2 = this.jdField_a_of_type_OrgDom4jDocument.getRootElement().selectNodes("doc");
		ArrayList localArrayList = new ArrayList();
		Object localObject1 = localList1.iterator();
		while (((Iterator) localObject1).hasNext()) {
			Element localObject2 = (Element) ((Iterator) localObject1).next();
			JSLibraryEntity localObject3 = new JSLibraryEntity();
			((JSLibraryEntity) localObject3).setOwenElement((Element) localObject2);
			((JSLibraryEntity) localObject3).setDoc(false);
			((JSLibraryEntity) localObject3).setSourcePath(((Element) localObject2).attributeValue("source-path"));
			((JSLibraryEntity) localObject3).setName(((Element) localObject2).attributeValue("name"));
			((JSLibraryEntity) localObject3).setLocalPath(str2.replaceFirst(str1, jdField_a_of_type_JavaLangString)
					+ "/" + StudioJsLibraryInit.UI_JSFILE_NAME);
			localArrayList.add(localObject3);
		}
		localObject1 = localList2.iterator();
		while (((Iterator) localObject1).hasNext()) {
			Element localObject2 = (Element) ((Iterator) localObject1).next();
			JSLibraryEntity localObject3 = new JSLibraryEntity();
			((JSLibraryEntity) localObject3).setOwenElement((Element) localObject2);
			((JSLibraryEntity) localObject3).setDoc(true);
			((JSLibraryEntity) localObject3).setSourcePath(((Element) localObject2).attributeValue("source-path"));
			((JSLibraryEntity) localObject3).setName(((Element) localObject2).attributeValue("name"));
			((JSLibraryEntity) localObject3).setLocalPath(str2.replaceFirst(str1, jdField_a_of_type_JavaLangString)
					+ "/" + StudioJsLibraryInit.UI_JSFILE_NAME);
			localArrayList.add(localObject3);
		}
		this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.dsTableViewer.setInput(localArrayList);
		this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.dsTableViewer.refresh();
		this.jdField_b_of_type_OrgDom4jDocument = FileHelper
				.getXmlDocument(str2 + "/" + StudioJsLibraryInit.MOBILEUI_CONFIGXML_NAME);
		localObject1 = this.jdField_b_of_type_OrgDom4jDocument.getRootElement().selectNodes("js");
		Object localObject2 = this.jdField_b_of_type_OrgDom4jDocument.getRootElement().selectNodes("doc");
		Object localObject3 = new ArrayList();
		Iterator localIterator = ((List) localObject1).iterator();
		Element localElement;
		JSLibraryEntity localJSLibraryEntity;
		while (localIterator.hasNext()) {
			localElement = (Element) localIterator.next();
			localJSLibraryEntity = new JSLibraryEntity();
			localJSLibraryEntity.setOwenElement(localElement);
			localJSLibraryEntity.setDoc(false);
			localJSLibraryEntity.setSourcePath(localElement.attributeValue("source-path"));
			localJSLibraryEntity.setName(localElement.attributeValue("name"));
			localJSLibraryEntity.setLocalPath(str2.replaceFirst(str1, jdField_a_of_type_JavaLangString) + "/"
					+ StudioJsLibraryInit.MOBILEUI_JSFILE_NAME);
			((List) localObject3).add(localJSLibraryEntity);
		}
		localIterator = ((List) localObject2).iterator();
		while (localIterator.hasNext()) {
			localElement = (Element) localIterator.next();
			localJSLibraryEntity = new JSLibraryEntity();
			localJSLibraryEntity.setOwenElement(localElement);
			localJSLibraryEntity.setDoc(true);
			localJSLibraryEntity.setSourcePath(localElement.attributeValue("source-path"));
			localJSLibraryEntity.setName(localElement.attributeValue("name"));
			localJSLibraryEntity.setLocalPath(str2.replaceFirst(str1, jdField_a_of_type_JavaLangString) + "/"
					+ StudioJsLibraryInit.MOBILEUI_JSFILE_NAME);
			((List) localObject3).add(localJSLibraryEntity);
		}
		this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.mdsTableViewer.setInput(localObject3);
		this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.mdsTableViewer.refresh();
	}

	protected Control createContents(Composite paramComposite) {
		Composite localComposite = new Composite(paramComposite, 0);
		localComposite.setLayout(new FillLayout());
		this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite = new JSLibraryConfigComposite(
				localComposite, 0);
		b();
		a(this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite);
		return localComposite;
	}

	private void a(Composite paramComposite) {
		this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.dsTable.addListener(41, new l(this));
		this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.mdsTable.addListener(41, new m(this));
		this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.dsTable.addSelectionListener(new n(this));
		this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.mdsTable
				.addSelectionListener(new o(this));
		this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.addJsBtn.addListener(13, new p(this));
		this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.addJsDocBtn.addListener(13, new q(this));
		this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.deleteBtn.addListener(13, new r(this));
	}

	void c() {
		int i = this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.tabFolder.getSelectionIndex();
		TableViewer localTableViewer = null;
		Document localDocument = null;
		if (i == 0) {
			localTableViewer = this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.dsTableViewer;
			localDocument = this.jdField_a_of_type_OrgDom4jDocument;
		} else {
			localTableViewer = this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.mdsTableViewer;
			localDocument = this.jdField_b_of_type_OrgDom4jDocument;
		}
		List localList = (List) localTableViewer.getInput();
		TableItem[] arrayOfTableItem = localTableViewer.getTable().getSelection();
		for (int j = 0; j < arrayOfTableItem.length; j++) {
			JSLibraryEntity localJSLibraryEntity = (JSLibraryEntity) arrayOfTableItem[j].getData();
			localList.remove(localJSLibraryEntity);
			localDocument.getRootElement().remove(localJSLibraryEntity.getOwenElement());
		}
		localTableViewer.refresh();
	}

	public void d() {
		String str1 = CommonUtil.rebuildFilePath(StudioConfig.getStudioPath());
		FileDialog localFileDialog = new FileDialog(getShell(), 2);
		localFileDialog.setText(Messages.getString("preferencePages.JSLibrary.2"));
		localFileDialog.setFilterExtensions(new String[] { "*.xdoc" });
		String str2 = localFileDialog.open();
		if (str2 != null) {
			str2 = CommonUtil.rebuildFilePath(str2);
			String str3 = str2.substring(0, str2.lastIndexOf("/")) + "/";
			String[] arrayOfString = localFileDialog.getFileNames();
			int i = this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.tabFolder.getSelectionIndex();
			TableViewer localTableViewer = null;
			String str4 = null;
			Document localDocument = null;
			if (i == 0) {
				localTableViewer = this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.dsTableViewer;
				str4 = StudioJsLibraryInit.UI_JSFILE_NAME;
				localDocument = this.jdField_a_of_type_OrgDom4jDocument;
			} else {
				localTableViewer = this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.mdsTableViewer;
				str4 = StudioJsLibraryInit.MOBILEUI_JSFILE_NAME;
				localDocument = this.jdField_b_of_type_OrgDom4jDocument;
			}
			List localList = (List) localTableViewer.getInput();
			for (int j = 0; j < arrayOfString.length; j++) {
				JSLibraryEntity localJSLibraryEntity = new JSLibraryEntity();
				localJSLibraryEntity.setDoc(true);
				localJSLibraryEntity.setName(arrayOfString[j]);
				localJSLibraryEntity.setSourcePath(str3 + arrayOfString[j]);
				localJSLibraryEntity.setLocalPath(
						StudioJsLibraryInit.STUDIO_CONFIG_JSLIBPATH.replaceFirst(str1, jdField_a_of_type_JavaLangString)
								+ "/" + str4);
				if (localList.contains(localJSLibraryEntity))
					continue;
				localList.add(localJSLibraryEntity);
				Element localElement = localDocument.getRootElement().addElement("doc")
						.addAttribute("name", localJSLibraryEntity.getName())
						.addAttribute("source-path", localJSLibraryEntity.getSourcePath());
				localJSLibraryEntity.setOwenElement(localElement);
			}
			localTableViewer.refresh();
		}
	}

	public void e() {
		String str1 = CommonUtil.rebuildFilePath(StudioConfig.getStudioPath());
		String str2 = CommonUtil.rebuildFilePath(StudioConfig.getStudioAppRootPath());
		String str3 = str2 + "/jsdoc/js";
		File localFile = new File(str3);
		if (!localFile.exists())
			localFile.mkdirs();
		FileDialog localFileDialog = new FileDialog(getShell(), 2);
		localFileDialog.setText(Messages.getString("preferencePages.JSLibrary.3"));
		localFileDialog.setFilterExtensions(new String[] { "*.js" });
		String str4 = localFileDialog.open();
		if (str4 == null)
			return;
		str4 = CommonUtil.rebuildFilePath(str4);
		String str5 = str4.substring(0, str4.lastIndexOf("/")) + "/";
		String[] arrayOfString = localFileDialog.getFileNames();
		int i = this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.tabFolder.getSelectionIndex();
		TableViewer localTableViewer = null;
		String str6 = null;
		Document localDocument = null;
		if (i == 0) {
			localTableViewer = this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.dsTableViewer;
			str6 = StudioJsLibraryInit.UI_JSFILE_NAME;
			localDocument = this.jdField_a_of_type_OrgDom4jDocument;
		} else {
			localTableViewer = this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.mdsTableViewer;
			str6 = StudioJsLibraryInit.MOBILEUI_JSFILE_NAME;
			localDocument = this.jdField_b_of_type_OrgDom4jDocument;
		}
		List localList = (List) localTableViewer.getInput();
		for (int j = 0; j < arrayOfString.length; j++) {
			JSLibraryEntity localJSLibraryEntity = new JSLibraryEntity();
			localJSLibraryEntity.setDoc(false);
			localJSLibraryEntity.setName(arrayOfString[j]);
			localJSLibraryEntity.setSourcePath(str5 + arrayOfString[j]);
			localJSLibraryEntity.setLocalPath(
					StudioJsLibraryInit.STUDIO_CONFIG_JSLIBPATH.replaceFirst(str1, jdField_a_of_type_JavaLangString)
							+ "/" + str6);
			if (localList.contains(localJSLibraryEntity))
				continue;
			localList.add(localJSLibraryEntity);
			Element localElement = localDocument.getRootElement().addElement("js")
					.addAttribute("name", localJSLibraryEntity.getName())
					.addAttribute("source-path", localJSLibraryEntity.getSourcePath());
			localJSLibraryEntity.setOwenElement(localElement);
		}
		localTableViewer.refresh();
	}

	public void a(IProgressMonitor paramIProgressMonitor) {
		c(paramIProgressMonitor);
		b(paramIProgressMonitor);
	}

	private void b(IProgressMonitor paramIProgressMonitor) {
		List localList = (List) this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.mdsTableViewer
				.getInput();
		CPListElement[] arrayOfCPListElement = this.jdField_b_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement
				.getChildren();
		for (int i = 0; i < arrayOfCPListElement.length; i++)
			this.jdField_b_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement
					.remove(arrayOfCPListElement[i]);
		StudioJsLibraryInit.updateJsLibrary(paramIProgressMonitor,
				this.jdField_b_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement,
				this.jdField_a_of_type_OrgEclipseWstJsdtCoreIJavaScriptProject);
		StringBuffer localStringBuffer = new StringBuffer();
		ArrayList localArrayList1 = new ArrayList();
		ArrayList localArrayList2 = new ArrayList();
		Object localObject1 = localList.iterator();
		while (((Iterator) localObject1).hasNext()) {
			JSLibraryEntity localJSLibraryEntity = (JSLibraryEntity) ((Iterator) localObject1).next();
			if (localJSLibraryEntity.isDoc())
				localArrayList1.add(localJSLibraryEntity);
			else
				localArrayList2.add(localJSLibraryEntity);
		}
		localObject1 = new ArrayList();
		for (int j = 0; j < localArrayList1.size(); j++)
			((List) localObject1).addAll(JsXDocParser.parse(FileHelper
					.readFileAsStr(((JSLibraryEntity) localArrayList1.get(j)).getSourcePath(), "\n", false, "utf-8")));
		if (((List) localObject1).size() > 0)
			localStringBuffer.append(JsXDocParser.createXDocContext((List) localObject1)).append("\n");
		Object localObject2 = localArrayList2.iterator();
		while (((Iterator) localObject2).hasNext()) {
			JSLibraryEntity localObject3 = (JSLibraryEntity) ((Iterator) localObject2).next();
			String localObject4 = FileHelper.readFileAsStr(((JSLibraryEntity) localObject3).getSourcePath(), "\n",
					false, "utf-8");
			if (((JSLibraryEntity) localObject3).getSourcePath().endsWith("form.js"))
				localObject4 = "function Xforms(){};\nvar xforms = {};\n" + (String) localObject4;
			// localObject4 = StudioJsLibraryInit
			// .deleteFunctionContext((String) localObject4);
			localStringBuffer.append(localObject4).append("\n");
		}
		localObject2 = Path.fromOSString(StudioJsLibraryInit.STUDIO_CONFIG_JSLIBPATH);
		Object localObject3 = ((IPath) localObject2).append(StudioJsLibraryInit.MOBILEUI_JSFILE_NAME).makeAbsolute();
		Object localObject4 = new CPListElement(
				this.jdField_b_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement, null, 1,
				(IPath) localObject3, null);
		((CPListElement) localObject4).setAttribute("javadoc_location",
				BuildPathSupport.guessJavadocLocation((CPListElement) localObject4));
		this.jdField_b_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement
				.add((CPListElement) localObject4);
		StudioJsLibraryInit.updateJsLibrary(paramIProgressMonitor,
				this.jdField_b_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement,
				this.jdField_a_of_type_OrgEclipseWstJsdtCoreIJavaScriptProject);
		FileHelper.writeFile(
				StudioJsLibraryInit.STUDIO_CONFIG_JSLIBPATH + "/" + StudioJsLibraryInit.MOBILEUI_JSFILE_NAME,
				localStringBuffer.toString(), null);
		FileHelper.writeformatXMLFile(
				CommonUtil.rebuildFilePath(StudioConfig.getStudioAppRootPath()) + "/jsdoc/js" + "/"
						+ StudioJsLibraryInit.MOBILEUI_CONFIGXML_NAME,
				this.jdField_b_of_type_OrgDom4jDocument, "utf-8");
	}

	private void c(IProgressMonitor paramIProgressMonitor) {
		List localList = (List) this.jdField_a_of_type_ComStudioWidgetsJSLibraryConfigComposite.dsTableViewer
				.getInput();
		CPListElement[] arrayOfCPListElement = this.jdField_a_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement
				.getChildren();
		for (int i = 0; i < arrayOfCPListElement.length; i++)
			this.jdField_a_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement
					.remove(arrayOfCPListElement[i]);
		StudioJsLibraryInit.updateJsLibrary(paramIProgressMonitor,
				this.jdField_a_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement,
				this.jdField_a_of_type_OrgEclipseWstJsdtCoreIJavaScriptProject);
		StringBuffer localStringBuffer = new StringBuffer();
		ArrayList localArrayList1 = new ArrayList();
		ArrayList localArrayList2 = new ArrayList();
		Object localObject1 = localList.iterator();
		while (((Iterator) localObject1).hasNext()) {
			JSLibraryEntity localJSLibraryEntity = (JSLibraryEntity) ((Iterator) localObject1).next();
			if (localJSLibraryEntity.isDoc())
				localArrayList1.add(localJSLibraryEntity);
			else
				localArrayList2.add(localJSLibraryEntity);
		}
		localObject1 = new ArrayList();
		for (int j = 0; j < localArrayList1.size(); j++)
			((List) localObject1).addAll(JsXDocParser.parse(FileHelper
					.readFileAsStr(((JSLibraryEntity) localArrayList1.get(j)).getSourcePath(), "\n", false, "utf-8")));
		if (((List) localObject1).size() > 0)
			localStringBuffer.append(JsXDocParser.createXDocContext((List) localObject1)).append("\n");
		Object localObject2 = localArrayList2.iterator();
		while (((Iterator) localObject2).hasNext()) {
			JSLibraryEntity localObject3 = (JSLibraryEntity) ((Iterator) localObject2).next();
			String localObject4 = FileHelper.readFileAsStr(((JSLibraryEntity) localObject3).getSourcePath(), "\n",
					false, "utf-8");
			if (((JSLibraryEntity) localObject3).getSourcePath().endsWith("form.js"))
				localObject4 = "function Xforms(){};\nvar xforms = {};\n" + (String) localObject4;
			// localObject4 = StudioJsLibraryInit
			// .deleteFunctionContext(localObject4);
			localStringBuffer.append(localObject4).append("\n");
		}
		localObject2 = Path.fromOSString(StudioJsLibraryInit.STUDIO_CONFIG_JSLIBPATH);
		Object localObject3 = ((IPath) localObject2).append(StudioJsLibraryInit.UI_JSFILE_NAME).makeAbsolute();
		Object localObject4 = new CPListElement(
				this.jdField_a_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement, null, 1,
				(IPath) localObject3, null);
		((CPListElement) localObject4).setAttribute("javadoc_location",
				BuildPathSupport.guessJavadocLocation((CPListElement) localObject4));
		this.jdField_a_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement
				.add((CPListElement) localObject4);
		StudioJsLibraryInit.updateJsLibrary(paramIProgressMonitor,
				this.jdField_a_of_type_OrgEclipseWstJsdtInternalUiWizardsBuildpathsCPUserLibraryElement,
				this.jdField_a_of_type_OrgEclipseWstJsdtCoreIJavaScriptProject);
		FileHelper.writeFile(StudioJsLibraryInit.STUDIO_CONFIG_JSLIBPATH + "/" + StudioJsLibraryInit.UI_JSFILE_NAME,
				localStringBuffer.toString(), null);
		FileHelper.writeformatXMLFile(CommonUtil.rebuildFilePath(StudioConfig.getStudioAppRootPath()) + "/jsdoc/js"
				+ "/" + StudioJsLibraryInit.UI_CONFIGXML_NAME, this.jdField_a_of_type_OrgDom4jDocument, "utf-8");
	}

	protected void performApply() {
		try {
			try {
				PlatformUI.getWorkbench().getProgressService().run(true, true, new s(this));
			} catch (InvocationTargetException localInvocationTargetException) {
				localInvocationTargetException.printStackTrace();
			}
		} catch (InterruptedException localInterruptedException) {
			localInterruptedException.printStackTrace();
		}
	}

	public boolean performOk() {
		performApply();
		return true;
	}

	protected void performDefaults() {
		StudioJsLibraryInit.config(true);
		b();
	}

	public void init(IWorkbench paramIWorkbench) {
	}
}
