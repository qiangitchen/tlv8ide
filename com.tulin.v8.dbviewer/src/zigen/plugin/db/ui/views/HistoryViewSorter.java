/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.History;

public class HistoryViewSorter extends ViewerSorter {

	public int compare(Viewer viewer, Object e1, Object e2) {
		if (e1 instanceof Folder && e2 instanceof Folder) {
			String a = e1.toString();
			String b = e2.toString();
			return a.compareTo(b);

		} else if (e1 instanceof History && e2 instanceof History) {
			History c1 = (History) e1;
			History c2 = (History) e2;

			return c1.getSqlHistory().getDate().compareTo(c2.getSqlHistory().getDate());

		} else {
			String a = e1.toString();
			String b = e2.toString();
			return a.compareTo(b);
		}

	}
}
