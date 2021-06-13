/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.util.Comparator;

import zigen.plugin.db.ui.internal.Column;

public class ColumnSeqSorter implements Comparator {

	public int compare(Object arg0, Object arg1) {

		if (arg0 instanceof Column && arg1 instanceof Column) {
			Column col1 = (Column) arg0;
			Column col2 = (Column) arg1;

			if (col1.getSeq() < col2.getSeq()) {
				return -1;
			} else {
				return 1;
			}
		}
		return 0;

	}

}
