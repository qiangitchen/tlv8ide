/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.util.Comparator;

public class ConstraintNameSorter implements Comparator {

	public int compare(Object arg0, Object arg1) {

		if (arg0 instanceof TablePKColumn && arg1 instanceof TablePKColumn) {
			TablePKColumn pk1 = (TablePKColumn) arg0;
			TablePKColumn pk2 = (TablePKColumn) arg1;

			return pk1.getName().compareTo(pk2.getName());

		} else if (arg0 instanceof TableIDXColumn && arg1 instanceof TableIDXColumn) {
			TableIDXColumn idx1 = (TableIDXColumn) arg0;
			TableIDXColumn idx2 = (TableIDXColumn) arg1;

			return idx1.getName().compareTo(idx2.getName());

		}
		return 0;
	}

}
