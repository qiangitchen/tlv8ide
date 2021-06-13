package com.tulin.v8.ide.navigator.views.filters;

import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class InnerClassFilesFilter extends ViewerFilter {
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if ((element instanceof IClassFile)) {
			IClassFile classFile = (IClassFile) element;
			return classFile.getElementName().indexOf('$') == -1;
		}
		return true;
	}
}
