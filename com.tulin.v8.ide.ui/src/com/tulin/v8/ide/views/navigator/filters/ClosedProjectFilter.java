package com.tulin.v8.ide.views.navigator.filters;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ClosedProjectFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parent, Object element) {
		if ((element instanceof IJavaElement))
			return ((IJavaElement) element).getJavaProject().getProject()
					.isOpen();
		if ((element instanceof IResource))
			return ((IResource) element).getProject().isOpen();
		return true;
	}
}
