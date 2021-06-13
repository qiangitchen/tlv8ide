/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TableViewContentProvider implements IStructuredContentProvider {

	Object[] contents;

	// TableViewer viewer;

	public TableViewContentProvider() {}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// this.viewer = (TableViewer) viewer;
		if (newInput instanceof Object[]) {
			contents = (Object[]) newInput;
		} else {
			contents = null;
		}
	}

	public Object[] getElements(Object inputElement) {
		// if (contents != null && contents == inputElement) {
		Object[] out = new Object[contents.length - 1];
		System.arraycopy(contents, 1, out, 0, out.length);
		return out;

		// }
		// return new Object[0];
	}

	public void dispose() {
		contents = null;
	}

	public Object[] getContents() {
		return contents;
	}

}
