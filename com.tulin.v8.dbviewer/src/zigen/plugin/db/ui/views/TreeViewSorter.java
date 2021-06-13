/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import zigen.plugin.db.core.TableColumn;
import zigen.plugin.db.ui.internal.Bookmark;
import zigen.plugin.db.ui.internal.BookmarkFolder;
import zigen.plugin.db.ui.internal.Column;
import zigen.plugin.db.ui.internal.Folder;
import zigen.plugin.db.ui.internal.Root;

public class TreeViewSorter extends ViewerSorter {

	public int compare(Viewer viewer, Object e1, Object e2) {
		if (e1 instanceof Root) {
			return (-1);

		} else if (e2 instanceof Root) {
			return (1);

		} else if (e1 instanceof BookmarkFolder && e2 instanceof BookmarkFolder) {
			String a = e1.toString();
			String b = e2.toString();
			return a.compareTo(b);

		} else if (e1 instanceof BookmarkFolder && e2 instanceof Bookmark) {
			return (-1);

		} else if (e2 instanceof BookmarkFolder && e1 instanceof Bookmark) {
			return (1);

		} else if (e1 instanceof Column && e2 instanceof Column) {
			Column c1 = (Column) e1;
			Column c2 = (Column) e2;

			if (c1.getColumn() != null && c2.getColumn() != null) {
				TableColumn tc1 = (TableColumn) c1.getColumn();
				TableColumn tc2 = (TableColumn) c2.getColumn();

				if (tc1.getSeq() < tc2.getSeq()) {
					return (-1);
				} else if (tc2.getSeq() < tc1.getSeq()) {
					return (1);
				} else {
					return (0);
				}

			}
			return (0);

		} else if (e1 instanceof Folder && e2 instanceof Folder) {
			// Folder folder1 = (Folder) e1;
			// Folder folder2 = (Folder) e2;
			return (0);

		} else {
			String a = e1.toString();
			String b = e2.toString();
			return a.compareTo(b);
		}

	}
}
