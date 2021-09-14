package com.tulin.v8.ide.views.navigator.filters;

import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jdt.internal.ui.viewsupport.ProblemTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

@SuppressWarnings("restriction")
public class EmptyLibraryContainerFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (((element instanceof PackageFragmentRootContainer))
				&& ((viewer instanceof ProblemTreeViewer))) {
			return ((ProblemTreeViewer) viewer).hasFilteredChildren(element);
		}
		return true;
	}
}
