package com.tulin.v8.ide.navigator.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IWorkingSet;

import com.tulin.v8.ide.utils.StudioConfig;

import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jface.viewers.IStructuredSelection;

/*
 * 资源视图
 */
@SuppressWarnings("restriction")
public class ResourseViewer extends PackageExplorerPart {
	public static final String KEY_DIR_LOCATION = "com.tulin.v8.ide.navigator.views.ResourseViewer";

	public ResourseViewer() {
	}

	public IAdaptable getProjectInput() {
		IWorkspaceRoot ws_root = ResourcesPlugin.getWorkspace().getRoot();
		IProject proj = ws_root.getProject(StudioConfig.PHANTOM_PROJECT_NAME);
		if (!proj.exists())
			return getSite().getPage().getInput();
		return proj;
	}

	public void reset() {
		getTreeViewer().setInput(getProjectInput());
		getTreeViewer().refresh();
	}

	protected IAdaptable getInitialInput() {
		return getProjectInput();
	}

	public IAdaptable getDefaultPageInput() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		return workspace.getRoot();
	}

	@Override
	public IWorkingSet getFilterWorkingSet() {
		return super.getFilterWorkingSet();
	}

	@Override
	public void refresh(IStructuredSelection selection) {
		super.refresh(selection);
	}

}
