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
		IFolderLayout topLeft = factory.createFolder("topLeft", // NON-NLS-1
				IPageLayout.LEFT, 0.25f, factory.getEditorArea());
		topLeft.addView("org.eclipse.ui.navigator.ProjectExplorer"); // 项目资源管理器
		topLeft.addView("org.eclipse.jdt.ui.PackageExplorer"); // 包资源管理器
		topLeft.addView("com.tulin.v8.ide.navigator.views.modelview");// 模型试图
		topLeft.addPlaceholder("org.eclipse.ui.views.ResourceNavigator");
		topLeft.addPlaceholder("org.eclipse.jdt.ui.TypeHierarchy");
		topLeft.addPlaceholder(JavaUI.ID_PACKAGES_VIEW);
		IFolderLayout bottom = factory.createFolder("bottomRight", // NON-NLS-1
				IPageLayout.BOTTOM, 0.75f, factory.getEditorArea());
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW); // 控制台
	}

	private void addPerspectiveShortcuts() {
		factory.addPerspectiveShortcut("org.eclipse.team.ui.TeamSynchronizingPerspective"); // NON-NLS-1
		factory.addPerspectiveShortcut("org.eclipse.team.cvs.ui.cvsPerspective"); // NON-NLS-1
		factory.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective"); // NON-NLS-1
	}

	/*
	 * 新建选项
	 */
	private void addNewWizardShortcuts() {
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard");// NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewClassCreationWizard");// NON-NLS-1
		//Mapper
		factory.addNewWizardShortcut("com.tulin.v8.ide.wizards.NewMapperWizard");// NON-NLS-1
		//File Folder
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");// NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");// NON-NLS-1
		// factory.addNewWizardShortcut("org.eclipse.jst.jsp.ui.internal.wizard.NewJSPWizard");//
		// NON-NLS-1
		factory.addNewWizardShortcut("com.tulin.v8.webtools.wizards.JSPNewWizard");// NON-NLS-1
		// factory.addNewWizardShortcut("org.eclipse.wst.html.ui.internal.wizard.NewHTMLWizard");//
		// NON-NLS-1
		factory.addNewWizardShortcut("com.tulin.v8.webtools.wizards.HTMLNewWizard");// NON-NLS-1
		factory.addNewWizardShortcut("com.tulin.v8.ide.wizards.SampleNewWizard");// NON-NLS-1
		factory.addNewWizardShortcut("com.tulin.v8.ide.wizards.ChartNewWizard");// NON-NLS-1
		factory.addNewWizardShortcut("com.tulin.v8.ureport.wizards.UReportFileNewWizard");// NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.wst.jsdt.ui.NewJSWizard");// NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.wst.css.ui.internal.wizard.NewCSSWizard");// NON-NLS-1
	}

	private void addViewShortcuts() {
		factory.addShowViewShortcut("org.eclipse.ant.ui.views.AntView"); // NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.team.ui.GenericHistoryView"); // NON-NLS-1
		factory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW); // 问题视图
		factory.addShowViewShortcut(IPageLayout.ID_OUTLINE); // 大纲视图
		factory.addShowViewShortcut(IPageLayout.ID_TASK_LIST); // 任务视图
		factory.addShowViewShortcut(IPageLayout.ID_PROGRESS_VIEW); // 进度视图
		factory.addShowViewShortcut(IPageLayout.ID_PROJECT_EXPLORER); // 项目视图
	}

}
