package com.tulin.v8.ide;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import com.tulin.v8.ide.perspectives.RelEngPerspective;
import com.tulin.v8.ide.utils.StudioConfig;

/*
 * 透视图相关操作
 * @工作空间重构或透视图被全部关闭时默认启动打开Studio透视图
 */
public class d implements Runnable {
	d(StudioStartup paramStudioStartup) {
	}

	public void run() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		try {
			IPerspectiveDescriptor localIPerspectiveDescriptor = workbench.getActiveWorkbenchWindow().getActivePage()
					.getPerspective();
			if (StudioPlugin.isWorkspacesInit() || localIPerspectiveDescriptor == null) {
				workbench.showPerspective(RelEngPerspective.ID, workbench.getActiveWorkbenchWindow());
				loadProject();
			}
		} catch (WorkbenchException e) {
			e.printStackTrace();
		}
		StudioPlugin.setWorkspacesInited();
	}

	/*
	 * 初始化时自动加载项目
	 */
	private void loadProject() {
		WorkspaceModifyOperation process = new WorkspaceModifyOperation() {

			@Override
			protected void execute(IProgressMonitor monitor)
					throws CoreException, InvocationTargetException, InterruptedException {
				IWorkspace localIWorkspace = ResourcesPlugin.getWorkspace();
				IProject localIProject = localIWorkspace.getRoot().getProject(StudioConfig.PHANTOM_PROJECT_NAME);
				try {
					// if (!localIProject.exists()) {
					// localIProject.create(monitor);
					// }
					if (!localIProject.isOpen()) {
						localIProject.open(monitor);
						localIProject.refreshLocal(2, monitor);
					}
				} catch (CoreException localCoreException1) {
					// localCoreException1.printStackTrace();
				}
			}
		};
		try {
			StudioPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().run(true, true, process);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}