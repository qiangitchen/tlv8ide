package com.tulin.v8.ide.navigator.views.filters;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ContainedLibraryFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if ((element instanceof IPackageFragmentRoot)) {
			IPackageFragmentRoot root = (IPackageFragmentRoot) element;
			if (root.isArchive()) {
				IResource resource = root.getResource();
				if (resource != null) {
					IProject jarProject = resource.getProject();
					IProject container = root.getJavaProject().getProject();
					return !container.equals(jarProject);
				}
			}
		}
		return true;
	}
}
