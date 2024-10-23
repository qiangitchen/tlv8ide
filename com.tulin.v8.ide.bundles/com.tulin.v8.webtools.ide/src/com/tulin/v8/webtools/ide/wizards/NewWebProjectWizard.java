package com.tulin.v8.webtools.ide.wizards;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

public class NewWebProjectWizard extends BasicNewResourceWizard {
	WebProjectWizardPage page;

	@Override
	public void init(IWorkbench theWorkbench, IStructuredSelection currentSelection) {
		super.init(theWorkbench, currentSelection);
		setWindowTitle(Messages.getString("wizards.message.title"));
	}

	@Override
	public void addPages() {
		page = new WebProjectWizardPage();
		addPage(page);
		super.addPages();
	}

	@Override
	public boolean performFinish() {
		try {
			createProject(page.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void createProject(String projectName) throws Exception {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();

		IProject project = root.getProject(projectName);
		if (!project.exists()) {
			project.create(null);
			project.open(null);
		}
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { "org.eclipse.jdt.core.javanature" });
		ICommand[] commands = new ICommand[4];
		commands[0] = description.newCommand();
		commands[0].setBuilderName("org.eclipse.jdt.core.javabuilder");
		commands[1] = description.newCommand();
		commands[1].setBuilderName("org.eclipse.wst.common.modulecore.ModuleCoreNature");
		commands[2] = description.newCommand();
		commands[2].setBuilderName("org.eclipse.wst.common.project.facet.core.builder");
		commands[3] = description.newCommand();
		commands[3].setBuilderName("org.eclipse.wst.validation.validationbuilder");
		project.getDescription().setBuildSpec(commands);
		project.setDescription(description, null);

		String src = page.getSRC();
		IResource srcs = project.findMember(src);
		if (srcs == null) {
			new File(project.getLocation().toFile().getAbsoluteFile() + File.separator + src).mkdirs();
		}

		String cet = page.getCentont();
		IResource cets = project.findMember(cet);
		if (cets == null) {
			new File(project.getLocation().toFile().getAbsoluteFile() + File.separator + cet).mkdirs();
		}

		File webi = new File(
				project.getLocation().toFile().getAbsoluteFile() + File.separator + cet + File.separator + "WEB-INF");
		if (!webi.exists()) {
			webi.mkdirs();
		}
		File webf = new File(project.getLocation().toFile().getAbsoluteFile() + File.separator + cet + File.separator
				+ "WEB-INF" + File.separator + "web.xml");
		if (!webf.exists()) {
			webf.createNewFile();
		}
		BufferedWriter outw = new BufferedWriter(new FileWriter(webf));
		outw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<web-app xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
				+ "	xmlns=\"http://java.sun.com/xml/ns/javaee\"\r\n"
				+ "	xsi:schemaLocation=\"http://JAVA.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd\"\r\n"
				+ "	version=\"3.0\">\r\n" + "	<display-name>" + projectName + "</display-name>\r\n"
				+ "	<welcome-file-list>\r\n" + "		<welcome-file>index.html</welcome-file>\r\n"
				+ "		<welcome-file>index.htm</welcome-file>\r\n" + "		<welcome-file>index.jsp</welcome-file>\r\n"
				+ "		<welcome-file>default.html</welcome-file>\r\n"
				+ "		<welcome-file>default.htm</welcome-file>\r\n"
				+ "		<welcome-file>default.jsp</welcome-file>\r\n" + "	</welcome-file-list>\r\n" + "</web-app>");
		outw.close();

		String ot = page.getOut();
		IResource ots = project.findMember(ot);
		if (ots == null) {
			new File(project.getLocation().toFile().getAbsoluteFile() + File.separator + ot).mkdirs();
		}

		File clp = new File(project.getLocation().toFile().getAbsoluteFile() + File.separator + ".classpath");
		if (!clp.exists()) {
			clp.createNewFile();
		}
		BufferedWriter out = new BufferedWriter(new FileWriter(clp));
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<classpath>\r\n"
				+ "	<classpathentry kind=\"src\" path=\"" + src.replace("\\", "/") + "\"/>\r\n"
				+ "	<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.8\">\r\n"
				+ "		<attributes>\r\n" + "			<attribute name=\"owner.project.facets\" value=\"java\"/>\r\n"
				+ "		</attributes>\r\n" + "	</classpathentry>\r\n"
				+ "	<classpathentry kind=\"con\" path=\"com.tulin.v8.webtools.internal.web.container\"/>\r\n"
				+ "	<classpathentry kind=\"con\" path=\"org.eclipse.jst.j2ee.internal.web.container\"/>\r\n"
				+ "	<classpathentry kind=\"con\" path=\"org.eclipse.jst.j2ee.internal.module.container\"/>\r\n"
				+ "	<classpathentry kind=\"output\" path=\"" + ot.replace("\\", "/") + "\"/>\r\n" + "</classpath>");
		out.close();

		File sett = new File(project.getLocation().toFile().getAbsoluteFile() + File.separator + ".settings");
		if (!sett.exists()) {
			sett.mkdirs();
		}

		File jdtcp = new File(project.getLocation().toFile().getAbsoluteFile() + File.separator + ".settings"
				+ File.separator + "org.eclipse.jdt.core.prefs");
		if (!jdtcp.exists()) {
			jdtcp.createNewFile();
		}
		BufferedWriter outs = new BufferedWriter(new FileWriter(jdtcp));
		outs.write("eclipse.preferences.version=1\r\n"
				+ "org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=enabled\r\n"
				+ "org.eclipse.jdt.core.compiler.codegen.targetPlatform=1.8\r\n"
				+ "org.eclipse.jdt.core.compiler.compliance=1.8\r\n"
				+ "org.eclipse.jdt.core.compiler.problem.assertIdentifier=error\r\n"
				+ "org.eclipse.jdt.core.compiler.problem.enumIdentifier=error\r\n"
				+ "org.eclipse.jdt.core.compiler.source=1.8\r\n");
		outs.close();

		File wstcop = new File(project.getLocation().toFile().getAbsoluteFile() + File.separator + ".settings"
				+ File.separator + "org.eclipse.wst.common.component");
		if (!wstcop.exists()) {
			wstcop.createNewFile();
		}
		BufferedWriter outss = new BufferedWriter(new FileWriter(wstcop));
		outss.write(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><project-modules id=\"moduleCoreId\" project-version=\"1.5.0\">\r\n"
						+ "    <wb-module deploy-name=\"" + projectName + "\">\r\n"
						+ "        <wb-resource deploy-path=\"/\" source-path=\"/" + cet.replace("\\", "/")
						+ "\" tag=\"defaultRootSource\"/>\r\n"
						+ "        <wb-resource deploy-path=\"/WEB-INF/classes\" source-path=\"/"
						+ src.replace("\\", "/") + "\"/>\r\n" + "        <property name=\"context-root\" value=\""
						+ projectName + "\"/>\r\n" + "        <property name=\"java-output-path\" value=\"/"
						+ projectName + "/" + ot.replace("\\", "/") + "\"/>\r\n" + "    </wb-module>\r\n"
						+ "</project-modules>\r\n");
		outss.close();

		project.refreshLocal(2, null);
	}

}
