/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.util.ArrayList;
import java.util.List;

public class ConstraintUtil {

	public static boolean isPKColumn(TablePKColumn[] pks, String columnName) {
		if (pks == null)
			return false;
		for (int i = 0; i < pks.length; i++) {
			if (pks[i].getColumnName().equals(columnName)) {
				return true;
			}
		}
		return false;
	}

	public static TableIDXColumn[] getFirstUniqueIndex(TableIDXColumn[] indxs) {

		if (indxs != null && indxs.length > 0) {
			List result = new ArrayList(indxs.length);
			String constraintName = indxs[0].getName();

			for (int i = 0; i < indxs.length; i++) {
				TableIDXColumn indx = indxs[i];
				if (constraintName.equals(indx.getName())) {
					result.add(indx);
				} else {
					break;
				}
			}
			return (TableIDXColumn[]) result.toArray(new TableIDXColumn[0]);

		} else {
			return null;
		}

	}

	public static boolean isUniqueIDXColumn(TableIDXColumn[] uniqueIndexs, String columnName) {
		if (uniqueIndexs == null) {
			return false;
		}
		for (int i = 0; i < uniqueIndexs.length; i++) {
			if (uniqueIndexs[i].getColumnName().equals(columnName)) {
				return !uniqueIndexs[i].isNonUnique();
			}
		}

		return false;
	}


}
