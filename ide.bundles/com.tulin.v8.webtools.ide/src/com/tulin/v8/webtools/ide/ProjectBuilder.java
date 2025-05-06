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
import org.eclipse.swt.widgets.Display;

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
			processContainer(getProject());
		}
		IResourceDelta delta = getDelta(getProject());
		if (delta == null) {
			return null;
		}
		processDelta(delta);

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
			int kind = children[i].getFlags();
			if (kind == IResourceDelta.MARKERS) {
				continue;
			}
			final IResource resource = children[i].getResource();
			if (resource != null && resource instanceof IFile && resource.exists()) {
				for (int j = 0; j < taskTagDetectors.size(); j++) {
					final ITaskTagDetector detector = (ITaskTagDetector) taskTagDetectors.get(j);
					if (detector.isSupported((IFile) resource)) {
						applyDetector(resource, detector);
						break;
					}
				}
			}
			processDelta(children[i]);
		}
	}

	private void applyDetector(final IResource resource, final ITaskTagDetector detector) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				try {
					ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
						public void run(IProgressMonitor monitor) throws CoreException {
							try {
								resource.deleteMarkers(IMarker.TASK, false, 0);
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
		});
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
