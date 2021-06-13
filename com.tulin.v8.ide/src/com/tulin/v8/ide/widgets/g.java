package com.tulin.v8.ide.widgets;

import java.util.List;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

@SuppressWarnings("rawtypes")
class g implements IStructuredContentProvider {
	g(JSLibraryConfigComposite paramJSLibraryConfigComposite) {
	}

	public Object[] getElements(Object paramObject) {
		if ((paramObject instanceof List))
			return ((List) paramObject).toArray();
		return new Object[0];
	}

	public void dispose() {
	}

	public void inputChanged(Viewer paramViewer, Object paramObject1,
			Object paramObject2) {
	}
}