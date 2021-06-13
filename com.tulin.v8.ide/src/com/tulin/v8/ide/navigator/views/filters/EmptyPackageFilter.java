package com.tulin.v8.ide.navigator.views.filters;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class EmptyPackageFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parent, Object element) {
		if ((element instanceof IPackageFragment)) {
			IPackageFragment pkg = (IPackageFragment) element;
			try {
				return (pkg.hasChildren())
						|| (hasUnfilteredResources(viewer, pkg));
			} catch (JavaModelException localJavaModelException) {
				return false;
			}
		}
		return true;
	}

	private boolean hasUnfilteredResources(Viewer viewer, IPackageFragment pkg)
			throws JavaModelException {
		Object[] resources = pkg.getNonJavaResources();
		int length = resources.length;
		if (length == 0) {
			return false;
		}
		if (!(viewer instanceof StructuredViewer)) {
			return true;
		}
		ViewerFilter[] filters = ((StructuredViewer) viewer).getFilters();
		for (int i = 0; i < length; i++) {
			int j = 0;
			while (filters[j].select(viewer, pkg, resources[i])) {
				j++;
				if (j >= filters.length) {
					return true;
				}
			}
		}
		return false;
	}
}
