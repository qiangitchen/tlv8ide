package com.tulin.v8.ide;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.tulin.v8.core.FileAndString;
import com.tulin.v8.core.utils.CommonUtil;
import com.tulin.v8.ide.utils.FileHelper;
import com.tulin.v8.ide.utils.StudioConfig;

@SuppressWarnings({ "rawtypes" })
public class ProjectManager {
	private static ProjectManager a = null;

	public static ProjectManager getInstance() {
		if (a == null)
			a = new ProjectManager();
		return a;
	}

	public void build() {
		a(StudioConfig.PHANTOM_PROJECT_NAME, StudioConfig.getUIPath());
		jsdtscope();
	}

	private void jsdtscope() {
		String str = StudioConfig.getUISettingPath() + "/.jsdtscope";
		File file = new File(str);
		if (!file.exists()) {
			String str2 = CommonUtil.rebuildFilePath(StudioConfig.getStudioAppRootPath());
			String localObject = FileAndString.FileToString(str2 + "/templet/.settings/.jsdtscope");
			FileHelper.saveFile(str, localObject);
		} else {
			try {
				FileWriter localFileWriter2 = new FileWriter(str);
				localFileWriter2.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				localFileWriter2.append("<classpath>\n");
				localFileWriter2.append(
						"<classpathentry kind=\"con\" path=\"org.eclipse.wst.jsdt.launching.baseBrowserLibrary\"/>\n");
				localFileWriter2.append(
						"<classpathentry kind=\"con\" path=\"org.eclipse.wst.jsdt.launching.JRE_CONTAINER\"/>\n");
				localFileWriter2.append("<classpathentry excluding=\"**/**\" kind=\"src\" path=\"\"/>\n");
				localFileWriter2.append("<classpathentry kind=\"con\" path=\"org.eclipse.wst.jsdt.USER_LIBRARY/"
						+ StudioJsLibraryInit.UI_LIBRARY_NAME + "\"/>\n");
				localFileWriter2.append("<classpathentry kind=\"con\" path=\"org.eclipse.wst.jsdt.USER_LIBRARY/"
						+ StudioJsLibraryInit.MOBILEUI_LIBRARY_NAME + "\"/>\n");
				localFileWriter2.append("<classpathentry kind=\"output\" path=\"\"/>\n");
				localFileWriter2.append("</classpath>\n");
				localFileWriter2.flush();
				localFileWriter2.close();// 文件写入完毕，关闭Writer
			} catch (IOException ee) {
				ee.printStackTrace();
			}
		}
	}

	private void a(String paramString1, String paramString2) {
		String str1 = paramString2 + "/.project";
		int i = 1;
		File localFile = new File(str1);
		if ((localFile.exists()) && (i != 0)) {
			try {
				Document prjdom = FileHelper.readFileAsXML(str1);
				List localList = prjdom
						.selectNodes("/projectDescription/natures/nature[text()='org.eclipse.wst.jsdt.core.jsNature']");
				boolean isedit = false;
				if (localList.size() == 0) {
					Element localElement = (Element) prjdom.selectSingleNode("/projectDescription/natures");
					localElement.addElement("nature").setText("org.eclipse.wst.jsdt.core.jsNature");
					isedit = true;
				}
				Element natureSpr = (Element) prjdom.selectSingleNode(
						"/projectDescription/natures/nature[text()='com.genuitec.eclipse.springframework.springnature']");
				if (natureSpr != null) {
					natureSpr.getParent().remove(natureSpr);
					isedit = true;
				}
				Element natureDPL = (Element) prjdom.selectSingleNode(
						"/projectDescription/natures/nature[text()='com.genuitec.eclipse.ast.deploy.core.deploymentnature']");
				if (natureDPL != null) {
					natureDPL.getParent().remove(natureDPL);
					isedit = true;
				}
				Element natureWEB = (Element) prjdom.selectSingleNode(
						"/projectDescription/natures/nature[text()='com.genuitec.eclipse.j2eedt.core.webnature']");
				if (natureWEB != null) {
					natureWEB.getParent().remove(natureWEB);
					isedit = true;
				}
				Element vactorWeb = (Element) prjdom.selectSingleNode(
						"/projectDescription/buildSpec/buildCommand/name[text()='com.genuitec.eclipse.j2eedt.core.WebClasspathBuilder']");
				if (vactorWeb != null) {
					vactorWeb.getParent().getParent().remove(vactorWeb.getParent());
					isedit = true;
				}
				Element vactorJS = (Element) prjdom.selectSingleNode(
						"/projectDescription/buildSpec/buildCommand/name[text()='org.eclipse.wst.jsdt.core.javascriptValidator']");
				if (vactorJS != null) {
					vactorJS.getParent().getParent().remove(vactorJS.getParent());
					isedit = true;
				}
				Element vactorSPr = (Element) prjdom.selectSingleNode(
						"/projectDescription/buildSpec/buildCommand/name[text()='com.genuitec.eclipse.springframework.springbuilder']");
				if (vactorSPr != null) {
					vactorSPr.getParent().getParent().remove(vactorSPr.getParent());
					isedit = true;
				}
				Element vactorJ2EE = (Element) prjdom.selectSingleNode(
						"/projectDescription/buildSpec/buildCommand/name[text()='com.genuitec.eclipse.j2eedt.core.J2EEProjectValidator']");
				if (vactorJ2EE != null) {
					vactorJ2EE.getParent().getParent().remove(vactorJ2EE.getParent());
					isedit = true;
				}
				Element vactorDepl = (Element) prjdom.selectSingleNode(
						"/projectDescription/buildSpec/buildCommand/name[text()='com.genuitec.eclipse.j2eedt.core.DeploymentDescriptorValidator']");
				if (vactorDepl != null) {
					vactorDepl.getParent().getParent().remove(vactorDepl.getParent());
					isedit = true;
				}
				Element vactorWST = (Element) prjdom.selectSingleNode(
						"/projectDescription/buildSpec/buildCommand/name[text()='org.eclipse.wst.validation.validationbuilder']");
				if (vactorWST != null) {
					vactorWST.getParent().getParent().remove(vactorWST.getParent());
					isedit = true;
				}
				Element vactorAST = (Element) prjdom.selectSingleNode(
						"/projectDescription/buildSpec/buildCommand/name[text()='com.genuitec.eclipse.ast.deploy.core.DeploymentBuilder']");
				if (vactorAST != null) {
					vactorAST.getParent().getParent().remove(vactorAST.getParent());
					isedit = true;
				}
				if (isedit)
					FileHelper.saveFile(str1, prjdom.asXML());
				return;
			} catch (Exception e) {
				// e.printStackTrace();
				StudioPlugin.logError(e.toString());
			}
		}
		if (!localFile.exists()) {
			String domstr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><projectDescription>\n\t<name>" + paramString1
					+ "</name>\n" + "\t<comment></comment>\n" + "\t<projects>\n" + "\t</projects>\n" + "\t<buildSpec>\n"
					+ "\t\t<buildCommand>\n" + "\t\t\t<name>org.eclipse.jdt.core.javabuilder</name>\n"
					+ "\t\t\t<arguments>\n" + "\t\t\t</arguments>\n" + "\t\t</buildCommand>\n" + "\t</buildSpec>\n"
					+ "\t<natures>\n" + "\t\t<nature>org.eclipse.jdt.core.javanature</nature>\n"
					+ (i != 0 ? "\t\t<nature>org.eclipse.wst.jsdt.core.jsNature</nature>\n" : "") + "\t</natures>\n"
					+ "</projectDescription>";
			FileHelper.saveFile(str1, domstr);
		}
	}

}
