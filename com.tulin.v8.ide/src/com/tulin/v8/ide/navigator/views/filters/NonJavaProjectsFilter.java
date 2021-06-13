package com.tulin.v8.ide.navigator.views.filters;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class NonJavaProjectsFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parent, Object element) {
		if ((element instanceof IJavaProject))
			return true;
		if ((element instanceof IProject)) {
			return !((IProject) element).isOpen();
		}
		return true;
	}
}
