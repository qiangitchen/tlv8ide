/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;

import zigen.plugin.db.ui.internal.TreeLeaf;

public class DragElementAdapter extends DragSourceAdapter {

	StructuredViewer viewer;

	public DragElementAdapter(StructuredViewer viewer) {
		this.viewer = viewer;
	}

	public void dragSetData(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

		Iterator iter = selection.iterator();
		int i = 0;

		StringBuffer sb = new StringBuffer();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof TreeLeaf) {
				if (i > 0) {
					sb.append(", "); //$NON-NLS-1$
				}
				sb.append(((TreeLeaf) obj).getName());
				i++;

			}
		}

		event.data = sb.toString();

	}

}
