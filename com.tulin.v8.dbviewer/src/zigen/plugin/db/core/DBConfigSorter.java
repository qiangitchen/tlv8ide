/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.math.BigDecimal;
import java.util.Comparator;

public class DBConfigSorter implements Comparator {

	boolean isDesc = false;

	public DBConfigSorter() {}

	public int compare(Object o1, Object o2) {
		IDBConfig first = (IDBConfig) o1;
		IDBConfig second = (IDBConfig) o2;

		String v1 = first.getDbName();
		String v2 = second.getDbName();

		try {
			BigDecimal d1 = new BigDecimal(v1);
			BigDecimal d2 = new BigDecimal(v2);

			if (d1.doubleValue() < d2.doubleValue()) {
				if (isDesc) {
					return (1);
				} else {
					return (-1);
				}
			} else if (d2.doubleValue() < d1.doubleValue()) {
				if (isDesc) {
					return (-1);
				} else {
					return (1);
				}
			} else {
				return (0);
			}

		} catch (NumberFormatException ex) {
			if (isDesc) {
				return (v2.compareTo(v1)); // for desc
			} else {
				return (v1.compareTo(v2)); // for asc
			}

		}
	}

}
