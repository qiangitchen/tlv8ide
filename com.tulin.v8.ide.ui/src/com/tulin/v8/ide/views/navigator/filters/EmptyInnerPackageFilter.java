package com.tulin.v8.ide.views.navigator.filters;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class EmptyInnerPackageFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parent, Object element) {
		if ((element instanceof IPackageFragment)) {
			IPackageFragment pkg = (IPackageFragment) element;
			try {
				if (pkg.isDefaultPackage())
					return pkg.hasChildren();
				return (!pkg.hasSubpackages()) || (pkg.hasChildren())
						|| (pkg.getNonJavaResources().length > 0);
			} catch (JavaModelException localJavaModelException) {
				return false;
			}
		}

		return true;
	}
}
