package com.tulin.v8.ide.views.navigator;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceAdapter;

public class DragBookmarkAdapter extends DragSourceAdapter {

	TreeViewer viewer;

	public DragBookmarkAdapter(TreeViewer viewer) {

		this.viewer = viewer;
	}

}
