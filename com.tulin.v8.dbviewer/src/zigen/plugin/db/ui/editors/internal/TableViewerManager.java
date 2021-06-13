/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;

import zigen.plugin.db.core.TableElement;
import zigen.plugin.db.core.TimeWatcher;

public class TableViewerManager {

	public static void insert(TableViewer viewer, TableElement newElement) {

		Object obj = viewer.getInput();

		if (obj instanceof TableElement[]) {
			TableElement[] old = (TableElement[]) viewer.getInput();

			TableElement[] wk = new TableElement[old.length + 1];
			System.arraycopy(old, 0, wk, 0, old.length);

			wk[old.length] = newElement;

			viewer.setInput(wk);

		} else {
			throw new IllegalArgumentException("The mistake is found in the specified object. "); //$NON-NLS-1$
		}
	}

	public static void update(TableViewer viewer, TableElement from, TableElement to) {

		Object obj = viewer.getInput();
		TimeWatcher tw = new TimeWatcher();
		tw.start();
		if (obj instanceof TableElement[]) {
			TableElement[] elements = (TableElement[]) viewer.getInput();
			for (int i = 1; i < elements.length; i++) {
				if (elements[i].equals(from)) {
					TableElement ele = elements[i];
					ele.copy(to);
					ele.clearMofiedColumn();
					ele.setRecordNo(from.getRecordNo());
					ele.isNew(false);
					viewer.update(ele, null);
					break;
				} else {


				}
			}
		} else {
			throw new IllegalArgumentException("The mistake is found in the specified object. "); //$NON-NLS-1$
		}
		tw.stop();
	}

	public static void remove(TableViewer viewer, Object target) {
		Object obj = viewer.getInput();

		if (obj instanceof TableElement[]) {
			TableElement[] elements = (TableElement[]) viewer.getInput();

			List newList = new ArrayList();
			for (int i = 0; i < elements.length; i++) {
				if (!elements[i].equals(target)) {
					newList.add(elements[i]);
				}
			}
			viewer.setInput((TableElement[]) newList.toArray(new TableElement[0]));
			// viewer.refresh();

		} else {
			throw new IllegalArgumentException("The mistake is found in the specified object. "); //$NON-NLS-1$
		}
	}

	public static void remove(TableViewer viewer, Object[] target) {
		TableElement[] contents = (TableElement[]) viewer.getInput();

		LinkedList srcList = new LinkedList(Arrays.asList(contents));
		for (int i = 0; i < target.length; i++) {
			srcList.remove(target[i]);
		}
		contents = (TableElement[]) srcList.toArray(new TableElement[srcList.size()]);

		viewer.setInput(contents);
	}


}
