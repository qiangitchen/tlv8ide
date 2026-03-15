package com.tulin.v8.ide.perspectives;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

/**
 * Studio透视图
 */
public class RelEngPerspective implements IPerspectiveFactory {
	public static String ID = "com.tulin.v8.ide.perspectives.RelEngPerspective";
	private IPageLayout factory;

	public RelEngPerspective() {
		super();
	}

	public void createInitialLayout(IPageLayout layout) {
		this.factory = layout;
		addViews();

		addNewWizardShortcuts();
		addPerspectiveShortcuts();
		addViewShortcuts();
	}

	private void addViews() {
		IFolderLayout topLeft = factory.createFolder("topLeft", IPageLayout.LEFT, 0.25f, factory.getEditorArea());
		topLeft.addView("org.eclipse.ui.navigator.ProjectExplorer"); // 项目资源管理器
		topLeft.addView("org.eclipse.jdt.ui.PackageExplorer"); // 包资源管理器
		topLeft.addView("com.tulin.v8.ide.navigator.views.modelview");// 模型视图
		topLeft.addPlaceholder("org.eclipse.ui.views.ResourceNavigator");
		topLeft.addPlaceholder("org.eclipse.jdt.ui.TypeHierarchy");
		topLeft.addPlaceholder(JavaUI.ID_PACKAGES_VIEW);
		IFolderLayout bottom = factory.createFolder("bottomRight", IPageLayout.BOTTOM, 0.75f, factory.getEditorArea());
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW); // 控制台
		// bottom.addView(IPageLayout.ID_PROBLEM_VIEW); // 问题视图
	}

	private void addPerspectiveShortcuts() {
		factory.addPerspectiveShortcut("org.eclipse.team.ui.TeamSynchronizingPerspective");
		factory.addPerspectiveShortcut("org.eclipse.team.cvs.ui.cvsPerspective");
		factory.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective");
	}

	/*
	 * 新建选项
	 */
	private void addNewWizardShortcuts() {
		// File Folder
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");
		// Java
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard");
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewClassCreationWizard");
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard");
		// Mapper
		factory.addNewWizardShortcut("com.tulin.v8.ide.wizards.NewMapperWizard");
		// v8 ide file
		factory.addNewWizardShortcut("com.tulin.v8.ide.wizards.SampleNewWizard");
		factory.addNewWizardShortcut("com.tulin.v8.ide.wizards.ChartNewWizard");
		factory.addNewWizardShortcut("com.tulin.v8.ureport.wizards.UReportFileNewWizard");
		// vue
		factory.addNewWizardShortcut("com.tulin.v8.vue.wizards.NewVueWizard");
		// My webtools
		factory.addNewWizardShortcut("com.tulin.v8.webtools.wizards.JSPNewWizard");
		factory.addNewWizardShortcut("com.tulin.v8.webtools.wizards.HTMLNewWizard");
		// Web File
		factory.addNewWizardShortcut("org.eclipse.wst.jsdt.ui.NewJSWizard");
		factory.addNewWizardShortcut("org.eclipse.wst.css.ui.internal.wizard.NewCSSWizard");
		// factory.addNewWizardShortcut("org.eclipse.wst.html.ui.internal.wizard.NewHTMLWizard");
		// JSP
		// factory.addNewWizardShortcut("org.eclipse.jst.jsp.ui.internal.wizard.NewJSPWizard");
	}

	private void addViewShortcuts() {
		// factory.addShowViewShortcut("org.eclipse.ant.ui.views.AntView");
		factory.addShowViewShortcut(JavaUI.ID_PACKAGES); // 包资源视图
		factory.addShowViewShortcut("org.eclipse.team.ui.GenericHistoryView");
		factory.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW); // 控制台
		factory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW); // 问题视图
		factory.addShowViewShortcut(IPageLayout.ID_OUTLINE); // 大纲视图
		factory.addShowViewShortcut(IPageLayout.ID_TASK_LIST); // 任务视图
		factory.addShowViewShortcut(IPageLayout.ID_PROGRESS_VIEW); // 进度视图
		factory.addShowViewShortcut(IPageLayout.ID_PROJECT_EXPLORER); // 项目视图
		factory.addShowViewShortcut("com.tulin.v8.ide.navigator.views.modelview"); // 模型视图
	}

}
