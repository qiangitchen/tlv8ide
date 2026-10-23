package com.tulin.v8.webtools.ide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.tulin.v8.webtools.ide.html.tasktag.AbstractTaskTagDetector;
import com.tulin.v8.webtools.ide.html.tasktag.HTMLTaskTagDetector;
import com.tulin.v8.webtools.ide.html.tasktag.ITaskTagDetector;
import com.tulin.v8.webtools.ide.html.tasktag.JavaScriptTaskTagDetector;
import com.tulin.v8.webtools.ide.html.tasktag.TaskTag;

public class ProjectBuilder extends IncrementalProjectBuilder {

	private List<AbstractTaskTagDetector> taskTagDetectors = new ArrayList<AbstractTaskTagDetector>();
	private TaskTag[] tags;

	public ProjectBuilder() {
		taskTagDetectors.add(new HTMLTaskTagDetector());
		taskTagDetectors.add(new JavaScriptTaskTagDetector());
	}

	@SuppressWarnings("rawtypes")
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {

		List<TaskTag> list = TaskTag.loadFromPreference(false);
		tags = list.toArray(new TaskTag[list.size()]);

		if (getProject() == null) {
			return null;
		}
		if (kind == FULL_BUILD) {
			// 全量构建：扫描整个项目
			processContainer(getProject());
		} else {
			// 增量构建：仅处理自上次构建以来的变更
			IResourceDelta delta = getDelta(getProject());
			if (delta != null) {
				processDelta(delta);
			}
		}

		getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
		return null;
	}

	private void processContainer(IContainer project) {
		try {
			IResource[] resources = project.members();
			for (int i = 0; i < resources.length; i++) {
//				if(resources[i]==null){
//					continue;
//				}
				if (resources[i] instanceof IContainer) {
					processContainer((IContainer) resources[i]);
				} else if (resources[i] instanceof IFile) {
					for (int j = 0; j < taskTagDetectors.size(); j++) {
						final ITaskTagDetector detector = (ITaskTagDetector) taskTagDetectors.get(j);
						if (detector.isSupported((IFile) resources[i])) {
							applyDetector(resources[i], detector);
							break;
						}
					}
				}
			}
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
	}

	private void processDelta(IResourceDelta delta) {
		IResourceDelta[] children = delta.getAffectedChildren();
		for (int i = 0; i < children.length; i++) {
			final int kind  = children[i].getKind();   // 变更类型: ADDED / REMOVED / CHANGED
			final int flags = children[i].getFlags();  // 位标志: CONTENT / MARKERS / F_MOVED_FROM / ...

			// 1) 跳过"仅标记变更"(不含实际内容变化)的 delta，避免任务标记反复重建
			if ((flags & IResourceDelta.MARKERS) != 0 && (flags & IResourceDelta.CONTENT) == 0) {
				processDelta(children[i]);
				continue;
			}

			// 2) 对已删除/移走的资源，不重复执行检测（先清理遗留 marker，再跳过）
			final IResource resource = children[i].getResource();
			if (resource != null && (kind == IResourceDelta.REMOVED || kind == IResourceDelta.REMOVED_PHANTOM)) {
				clearTaskMarkersQuietly(resource);
				processDelta(children[i]);
				continue;
			}

			// 3) 对新增/变化的文件执行任务标签检测
			if (resource != null && resource instanceof IFile && resource.exists()
					&& (kind == IResourceDelta.ADDED || kind == IResourceDelta.CHANGED)) {
				for (int j = 0; j < taskTagDetectors.size(); j++) {
					final ITaskTagDetector detector = (ITaskTagDetector) taskTagDetectors.get(j);
					if (detector.isSupported((IFile) resource)) {
						applyDetector(resource, detector);
						break;
					}
				}
			}

			// 4) 递归处理子节点（处理容器型资源内部的变化）
			processDelta(children[i]);
		}
	}

	/** 安全地删除资源上的所有任务标记，任何异常仅记录日志不抛出。 */
	private void clearTaskMarkersQuietly(IResource resource) {
		try {
			if (resource != null && resource.exists()) {
				resource.deleteMarkers(IMarker.TASK, false, IResource.DEPTH_ZERO);
			}
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
	}

	private void applyDetector(final IResource resource, final ITaskTagDetector detector) {
		try {
			// 构建线程本来就持有 workspace 锁，可直接执行 marker/资源操作，
			// 无需跳到 UI 线程（否则 UI 线程等 workspace 锁、构建线程等 UI 线程 → 死锁）。
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					try {
						resource.deleteMarkers(IMarker.TASK, false, IResource.DEPTH_ZERO);
						detector.detect((IFile) resource, tags);
					} catch (Exception ex) {
						WebToolsPlugin.logException(ex);
					}
				}
			}, null);
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
	}

	public static void doBuild(IProject project) {
		try {
			if (project.hasNature(ProjectNature.HTML_NATURE_ID)) {
				project.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());

				project.build(IncrementalProjectBuilder.FULL_BUILD, ProjectNature.HTML_BUILDER_ID,
						new HashMap<String, String>(), new NullProgressMonitor());
			}
		} catch (Exception ex) {
			WebToolsPlugin.logException(ex);
		}
	}

}
