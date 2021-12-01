package com.tulin.v8.ide.views.navigator.filters;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class JavaFileFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parent, Object element) {
		if ((element instanceof ITypeRoot)) {
			return false;
		}
		if ((element instanceof IPackageFragment))
			try {
				return ((IPackageFragment) element).getNonJavaResources().length > 0;
			} catch (JavaModelException localJavaModelException) {
				return true;
			}
		return true;
	}
}
